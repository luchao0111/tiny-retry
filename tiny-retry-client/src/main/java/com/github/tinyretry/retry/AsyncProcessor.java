package com.github.tinyretry.retry;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.lang.SerializationUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.commons.model.Observable;
import com.github.commons.model.Observer;
import com.github.tinyretry.retry.constants.TaskStatus;
import com.github.tinyretry.retry.domain.McTaskDO;
import com.github.tinyretry.retry.domain.ProcessResult;
import com.github.tinyretry.retry.domain.RetryJob;
import com.github.tinyretry.retry.domain.Task;
import com.github.tinyretry.retry.exception.RetryRemoteException;
import com.github.tinyretry.retry.service.RetryTaskService;
import com.github.tinyretry.retry.tools.RetryJobTools;
import com.github.tinyretry.timer.McJobSchedule;
import com.github.tinyretry.timer.McJobScheduleException;
import com.github.tinyretry.timer.domain.McJobGroup;

/**
 * <pre>
 * desc: �첽��������
 * 
 * created: 2012-8-23 ����02:41:29
 * author: xiaofeng.zhouxf
 * todo: 
 * history:
 * </pre>
 */
public class AsyncProcessor implements Observer<ProcessResult> {

    private static final Logger          logger             = LoggerFactory.getLogger(AsyncProcessor.class);

    // ���Կ�ܵ�����mcJobSchedule
    private McJobSchedule                retryMcJobSchedule = null;

    private ConcurrentLinkedQueue<Entry> cacheQueue         = new ConcurrentLinkedQueue<Entry>();

    private RetryTaskService             retryTaskService;

    // �Ƿ��첽�������,������Ҫ��������˵�������û��з���
    private boolean                      memoryAble         = true;

    /**
     * ���ó�ʼ������,Ĭ�������ڴ滺����ģʽ
     */
    public void init() {

        ExecutorService newSingleThreadExecutor = Executors.newSingleThreadExecutor();

        newSingleThreadExecutor.submit(new Runnable() {

            public void run() {
                while (true) {
                    try {
                        Entry entry = cacheQueue.poll();
                        if (entry != null) {
                            register(entry.getTask(), entry.getData(), entry.getNextTime());
                        } else {
                            Thread.sleep(500);
                        }
                    } catch (Throwable e) {
                        logger.error(e.getMessage(), e);
                    }
                }

            }
        });
    }

    /**
     * ִ���첽����
     * 
     * @param task ��Ҫִ�е�����
     * @param data ��Ҫִ�е����������
     * @param callbacks �ص�����������ִ�н���ʱ�ص������޵�һ�ε��ã���ʱ�����޷����õ�
     * @throws RetryRemoteException Զ�̷���ʧ�ܣ�����û�м�¼�ɹ������쳣˵��������ʧ����
     * @throws McJobScheduleException ע�᱾��jobʧ��,�����Եȴ���ʱ����,����ѡ�����
     */
    public void execute(Task task, Serializable data, Observer<ProcessResult>... callbacks)
                                                                                           throws RetryRemoteException,
                                                                                           McJobScheduleException {
        // ==========0. check pararm==========
        checkParams(task);

        // ==========1. build pararm==========
        // cloneһ�����Ⲣ������
        Task cloneTask = cloneTask(task);
        long calculateNextTime = calculateNextTime(cloneTask);
        // ==========2. build db record==========
        logger.info("Task submit: " + task.getTaskId() + " name: " + cloneTask.getTaskName());
        if (cloneTask.getTaskId() <= 0) {
            cloneTask = buildTask(cloneTask, TaskStatus.DEALING, calculateNextTime, data);
        }

        if (!memoryAble) {
            // ==========3. create job==========
            register(cloneTask, data, calculateNextTime, callbacks);
        } else {
            cacheQueue.add(new Entry(cloneTask, data, calculateNextTime));
        }
    }

    private void register(Task task, Serializable data, long calculateNextTime, Observer<ProcessResult>... callbacks)
                                                                                                                     throws McJobScheduleException {
        RetryJob job = RetryJobTools.generateMcJob(RetryJobTools.generateMcJobDefinition(RetryJobTools.generateJobName(task),
                                                                                         RetryJobTools.generateJobName(task),
                                                                                         task.getDescription()));
        job.setProcessor(task.getProcessor());
        // add job observer
        job.addObserver(this);
        if (callbacks != null && callbacks.length > 0) {
            for (Observer<ProcessResult> observer : callbacks) {
                job.addObserver(observer);
            }
        }
        job.setTask(task);
        job.setData(data);

        McJobGroup jobGroup = RetryJobTools.generateMcJobGroup(RetryJobTools.generateJobName(task), calculateNextTime);

        List<McJobGroup> groups = new ArrayList<McJobGroup>(1);
        groups.add(jobGroup);

        // ==========4. run the job immadiately==========
        logger.info("Register job:" + task.getTaskId() + " name: " + task.getTaskName());
        retryMcJobSchedule.setGroups(groups);
        retryMcJobSchedule.init();
        retryMcJobSchedule.registerJob(job);
        // �ظ�����û��ϵ
        retryMcJobSchedule.start();
    }

    /**
     * ִ���첽���񣬸ýӿڻᴴ��һ�����ݿ����񣬵��ǲ����ڱ���ע��job ֻ���������ںܳ���������ʺ��ø÷���������������ݿ�ѹ��������!!!
     * 
     * @param task ��Ҫִ�е�����
     * @param data ��Ҫִ�е����������
     * @throws RetryRemoteException Զ�̷���ʧ�ܣ�����û�м�¼�ɹ������쳣˵��������ʧ����
     * @throws McJobScheduleException ע�᱾��jobʧ��,�����Եȴ���ʱ����,����ѡ�����
     */
    public void execWithOutRegisterJob(Task task, Serializable data) throws RetryRemoteException,
                                                                    McJobScheduleException {
        // ==========0. check pararm==========
        checkParams(task);

        // ==========1. build pararm==========
        // cloneһ�����Ⲣ������
        Task cloneTask = cloneTask(task);
        long calculateNextTime = calculateNextTime(cloneTask);

        // ==========2. build db record==========
        logger.info("Task submit: " + task.getTaskId());
        if (cloneTask.getTaskId() <= 0) {
            cloneTask = buildTask(cloneTask, TaskStatus.WAITING, calculateNextTime, data);
        }
    }

    public void update(Observable<ProcessResult> observable, ProcessResult result) {

        if (observable != null && observable instanceof RetryJob && ((RetryJob) observable).getTask() != null) {
            RetryJob job = (RetryJob) observable;
            job.setFinlshedTime(System.currentTimeMillis());
            long taskId = job.getTask().getTaskId();

            if (result != null) {
                McTaskDO mcTaskDO = new McTaskDO();
                mcTaskDO.setId(taskId);
                short status = TaskStatus.SUCCESS.getStatus();
                if (!result.isSuccess()) {
                    if (job.getTask().getCurrentTime() < job.getTask().getRetryTime()) {
                        status = TaskStatus.WAITING.getStatus();
                    } else {
                        status = TaskStatus.FAIL.getStatus();
                        // if failed, will update the data
                        mcTaskDO.setNextTime(calculateNextTime(job.getTask()));
                    }

                    mcTaskDO.setFailReason(result.getFailReason());

                    if (result.isUpdateData()) {
                        mcTaskDO.setContext(generateByteArrayData(result.getData()));
                    }

                    mcTaskDO.setStatus(status);
                    // 1. record the result
                    try {
                        // update the new processor
                        mcTaskDO.setProcessor(RetryJobTools.getIPAddress());
                        retryTaskService.updateMcTask(mcTaskDO);
                    } catch (RetryRemoteException e) {
                        logger.error("Update task status exception", e);
                        // continue unlock zk to preventing deadlock
                    }

                    logger.error("fail deal job" + taskId + "-" + job.getTask().getAppCode() + "-"
                                 + job.getTask().getTaskName() + "--->" + job.getElapsedTime() + " -- "
                                 + job.getActualRunTime() + " -- " + job.successRate());
                } else {
                    // ���ǵ����ݿ�ѹ����ֱ��ɾ��
                    try {
                        retryTaskService.deleteMcTask(mcTaskDO);
                        logger.error("Success deal task and deleted:" + taskId + "-" + job.getTask().getAppCode() + "-"
                                     + job.getTask().getTaskName() + "--->" + job.getElapsedTime() + " -- "
                                     + job.getActualRunTime() + " -- " + job.successRate());
                    } catch (RetryRemoteException e) {
                        logger.error("delete task status exception", e);
                    }
                }
            }
        }
    }

    private byte[] generateByteArrayData(Serializable data) {
        return SerializationUtils.serialize(data);
    }

    private Task buildTask(Task task, TaskStatus jobStatus, long calculateNextTime, Serializable data)
                                                                                                      throws RetryRemoteException {
        McTaskDO mcTaskDO = new McTaskDO();
        mcTaskDO.setAppCode(task.getAppCode());
        mcTaskDO.setNextTime(calculateNextTime);
        mcTaskDO.setPriority(task.getPriority() >= 0 ? task.getPriority() : 0);
        mcTaskDO.setRetryTime(task.getRetryTime() >= 0 ? task.getRetryTime() : 0);
        mcTaskDO.setStatus(jobStatus.getStatus());
        mcTaskDO.setType(task.getTaskName());
        mcTaskDO.setVersion((short) 1);
        mcTaskDO.setContext(generateByteArrayData(data));
        mcTaskDO.setProcessor(RetryJobTools.getIPAddress());
        mcTaskDO = retryTaskService.insertMcTask(mcTaskDO);
        task.setTaskId(mcTaskDO.getId());
        logger.info("New task :" + task.getTaskId());
        return task;
    }

    private Task cloneTask(Task task) {
        return new Task(task);
    }

    private long calculateNextTime(Task task) {
        long nextTime = task.getNextTime();
        if (nextTime <= 0) {
            nextTime = RetryJobTools.calculateNextTime(task.getRetryPeriod(), task.getRetryFactor(),
                                                       task.getCurrentTime() > 0 ? task.getCurrentTime() - 1 : 0);
        }
        return nextTime;
    }

    private void checkParams(Task task) throws RetryRemoteException {
        if (task == null) {
            throw new RetryRemoteException("param task can't be null");
        }

        if (StringUtils.isBlank(task.getAppCode())) {
            throw new RetryRemoteException("param appcode can't be blank");
        }

        if (StringUtils.isBlank(task.getTaskName())) {
            throw new RetryRemoteException("param taskname can't be blank");
        }
    }

    public void setRetryMcJobSchedule(McJobSchedule retryMcJobSchedule) {
        this.retryMcJobSchedule = retryMcJobSchedule;
    }

    public int compareTo(ProcessResult o) {
        return 0;
    }

    public void setRetryTaskService(RetryTaskService retryTaskService) {
        this.retryTaskService = retryTaskService;
    }

    class Entry {

        Task         task;
        Serializable data;
        long         nextTime;

        public Entry(Task task, Serializable data, long nextTime){
            super();
            this.task = task;
            this.data = data;
            this.nextTime = nextTime;
        }

        public Task getTask() {
            return task;
        }

        public void setTask(Task task) {
            this.task = task;
        }

        public Serializable getData() {
            return data;
        }

        public void setData(Serializable data) {
            this.data = data;
        }

        public long getNextTime() {
            return nextTime;
        }

        public void setNextTime(long nextTime) {
            this.nextTime = nextTime;
        }

    }
}
