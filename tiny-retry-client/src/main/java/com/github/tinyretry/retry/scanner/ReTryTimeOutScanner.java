package com.github.tinyretry.retry.scanner;

import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.tinyretry.retry.constants.TaskStatus;
import com.github.tinyretry.retry.domain.McTaskDO;
import com.github.tinyretry.retry.domain.RetryJob;
import com.github.tinyretry.retry.domain.Task;
import com.github.tinyretry.retry.domain.TaskQuery;
import com.github.tinyretry.retry.domain.TaskUpdate;
import com.github.tinyretry.retry.tools.RetryJobTools;
import com.github.tinyretry.timer.McJobSchedule;

/**
 * <pre>
 * desc: ���ݳ�ʱ�趨��������ݿ��м�¼�Ƿ�ʱ.
 * ����ѳ�ʱ���ж�job�Ƿ��ڱ��������У�����ע��job����ͨ��zk��ȡ������һ̨������������job
 * 
 * 
 * created: 2012-8-6 ����02:43:50
 * author: xiaofeng.zhouxf
 * todo: 
 * history:
 * </pre>
 */
public class ReTryTimeOutScanner extends RetryScanner {

    private static final Logger logger = LoggerFactory.getLogger(ReTryTimeOutScanner.class);

    /**
     * ����ִ��
     */
    public boolean execute() throws Exception {

        Map<String, Task> tasks = server.getTasks();

        // 1. Get tasks by appCode, order by priority
        List<McTaskDO> dbTasks = retryTaskService.queryMcTaskList(generateQuery());
        int dealCount = 0;
        if (dbTasks != null) {
            // 2. register the task order by priority
            for (McTaskDO task : dbTasks) {
                if (task != null && server.getTasks().containsKey(task.getType())) {

                    Task tempTask = server.getTasks().get(task.getType());

                    if (tempTask != null) {
                        Task cloneTask = new Task(tempTask);
                        cloneTask.setTaskId(task.getId());
                        cloneTask.setCurrentTime(task.getVersion());
                        cloneTask.setNextTime(task.getNextTime());

                        // ��ʱ��ע�������¿�ʼ
                        long currentMillisTime = System.currentTimeMillis();
                        long preTime = RetryJobTools.calculatePreTime(cloneTask.getNextTime(),
                                                                      cloneTask.getRetryPeriod(),
                                                                      cloneTask.getRetryFactor(),
                                                                      cloneTask.getCurrentTime());
                        long expirtTime = (cloneTask.getExecuteTimeOut() + preTime);
                        if (currentMillisTime > expirtTime) {

                            // 3. unregister job
                            RetryJob job = RetryJobTools.generateMcJob(RetryJobTools.generateMcJobDefinition(RetryJobTools.generateJobName(cloneTask),
                                                                                                             RetryJobTools.generateJobName(cloneTask),
                                                                                                             cloneTask.getDescription()));

                            try {
                                retryMcJobSchedule.unRegisterJob(job);
                            } catch (Exception e) {
                                logger.error("Unregister the timeout job exception.", e);
                                continue;
                            }

                            // 2.1 zk locked
                            // DistributedLock lock = new DefaultDistributedLock(
                            // RetryJobTools.generateTaskZKNodeName(task.getId(),
                            // task.getAppCode() != null ? task.getAppCode() : Constants.EMPTY));
                            try {
                                // if (lock.tryLock()) {
                                task.setStatus(TaskStatus.WAITING.getStatus());
                                TaskUpdate update = new TaskUpdate();
                                BeanUtils.copyProperties(update, task);
                                update.setNextTime(RetryJobTools.calculateNextTime(cloneTask.getPriority(),
                                                                                   cloneTask.getRetryFactor(),
                                                                                   update.getVersion()));
                                update.setOldStatus(TaskStatus.DEALING.getStatus());
                                Integer updateMcTask = retryTaskService.updateMcTaskStatus(update);

                                if (updateMcTask > 0) {
                                    cloneTask.setCurrentTime(task.getVersion());
                                    cloneTask.setNextTime(task.getNextTime());
                                    logger.info("Time out task info: {},{},{},{},{}",
                                                new Object[] { cloneTask.getTaskId(), currentMillisTime, preTime,
                                                        expirtTime, ToStringBuilder.reflectionToString(cloneTask) });
                                    dealCount++;
                                }
                                // }
                            } catch (Exception e) {
                                logger.error("Lock.tryLock or deal exception.", e);
                            }
                            // finally {
                            // lock.unlock();
                            // }
                        }
                    }
                }
            }
        }

        logger.info("ReTryTimeOutScanner running: {} ,dealed {}",
                    dbTasks != null ? String.valueOf(dbTasks.size()) : "0", dealCount);

        return true;
    }

    private TaskQuery generateQuery() {
        TaskQuery taskQuery = new TaskQuery();
        taskQuery.setAppCode(appCode);
        taskQuery.setStatus(TaskStatus.DEALING.getStatus());
        taskQuery.setOffset(0);
        taskQuery.setLength(1000);
        taskQuery.setEndNextTime(RetryJobTools.getBeforeNextTime(repeatInterval));
        return taskQuery;
    }

}
