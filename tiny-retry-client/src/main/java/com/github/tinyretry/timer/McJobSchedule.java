package com.github.tinyretry.timer;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.quartz.CronTrigger;
import org.quartz.Job;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.Scheduler;
import org.quartz.SchedulerConfigException;
import org.quartz.SchedulerException;
import org.quartz.SimpleTrigger;
import org.quartz.Trigger;
import org.quartz.impl.DirectSchedulerFactory;
import org.quartz.simpl.RAMJobStore;
import org.quartz.simpl.SimpleThreadPool;
import org.quartz.spi.JobFactory;
import org.quartz.spi.JobStore;
import org.quartz.spi.TriggerFiredBundle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.tinyretry.timer.domain.McJob;
import com.github.tinyretry.timer.domain.McJobGroup;
import com.github.tinyretry.timer.domain.McJobRepository;
import com.github.tinyretry.timer.ext.McJobDefinition;
import com.github.tinyretry.timer.ext.McJobListener;
import com.github.tinyretry.timer.ext.McJobStore;
import com.github.tinyretry.timer.ext.jobfactory.McJobFactory;

/**
 * <pre>
 * desc: ���������,��չquartz,���������ķ�ʽʹ�ñ���
 *   
 * ʹ�÷�����
 * <ul><li>����McJobGroup,����cornExpression����repeatInterval������groupId
 * <code>
 *      McJobGroup perTowSec = new McJobGroup();
 * 		towPerSecond.setGroupId("perTwoSec");
 * 		towPerSecond.setRepeatInterval(2000);
 * </code>
 * </li>
 * <li>�̳�<code>McJob</code>����job��ʵ��<code>execute()</code>����
 * <code>
 * class TestJob extends McJob implements McJobNotifyListener {
 * 		public boolean onFile(McJobNotify notify) {
 * 			.....
 * 		}
 * 		public void execute() throws Exception {
 * 			.....
 *          //������Ϣ��ͬ��group����ָ��group��job��
 * 			McJobNotify notify = new McJobNotify(TestJob2.class.getName());
 * 			notify.setFromJobName(this.getName());
 * 			notify.setGroupId(this.getGroupId());
 * 			this.sendNotify(notify);
 * 		}
 * 	}
 * 	</code>
 * </li>
 * <li>����<code>McJobSchedule</code>��js, <code>McJobSchedule js = new McJobSchedule()</code>   </li>
 *    -����<code>js.setGroups(perTowSec)</code>�����ѵ�1�����д�����McJobGroup��ӽ�ȥ-
 *    -����job����<code> McJobDefinition definition = new McJobDefinition();
 *  	//����ע������ظ�������ͬʱע�ᣬ��Ϊ����������������λ
 *  	definition.setName("TestJob");
 *  	definition.setGroupId("testGroup");
 * 		TestJob testJob= new TestJob()��
 *      testJob.setDefinition(definition);
 *    -����<code>js.registerJob(testJob)</code>�����ѵ�2���д�����jobע���ȥ
 *    
 * <li>����<code>js.init()</code>��ʼ��������</li>
 * <li>����<code>js.start()</code>����job</li></ul>
 * 
 * ���Ե���<code>js.stop()</code>����ͣ���е�job
 * 
 * ���notify��Ϣ�����ã��������Ϊָ��������賿1������job1���ѷ�������ύ��1��10�ֵ�����
 * ����1��10���ܵ�����ֻ��Ҫ����ĳ���¼���ִ�м��ɡ�
 * 
 * created: 2012-5-7 ����02:20:37
 * author: xiaofeng.zhouxf
 * history:
 * </pre>
 */
public class McJobSchedule {
	private static final Logger logger = LoggerFactory.getLogger(McJobSchedule.class);

	private McJobRepository repository;
	private Scheduler scheduler;
	private int maxThreads = 10;
	private String scheduleName = "MC_JOB_SCHEDULE";
	private String scheduleNameId = "MC_JOB_SCHEDULE_INSTANCT";
	private JobStore jobStore;
	private McJobFactory jobFactory;
	private volatile boolean isInited = false;
	private Object lockObj = new Object();

	public McJobSchedule() {
		super();
	}

	public McJobSchedule(int maxThreads) {
		super();
		if (maxThreads > 0) {
			this.maxThreads = maxThreads;
		}
	}

	public McJobSchedule(int maxThreads, String scheduleName, String scheduleNameId) {
		this(null, maxThreads, scheduleName, scheduleNameId, null, null);
	}

	public McJobSchedule(McJobRepository repository, int maxThreads, String scheduleName, String scheduleNameId) {
		this(repository, maxThreads, scheduleName, scheduleNameId, null, null);
	}

	public McJobSchedule(int maxThreads, String scheduleName, String scheduleNameId,
			McJobStore jobStore) {
		this(null, maxThreads, scheduleName, scheduleNameId, jobStore, null);
	}

	public McJobSchedule(McJobRepository repository, int maxThreads, String scheduleName,
			String scheduleNameId, JobStore jobStore) {
		this(repository, maxThreads, scheduleName, scheduleNameId, jobStore, null);
	}

	public McJobSchedule(int maxThreads, String scheduleName, String scheduleNameId, McJobFactory jobFactory) {
		this(null, maxThreads, scheduleName, scheduleNameId, null, jobFactory);
	}

	public McJobSchedule(int maxThreads, String scheduleName, String scheduleNameId, JobStore jobStore,
			McJobFactory jobFactory) {
		this(null, maxThreads, scheduleName, scheduleNameId, jobStore, jobFactory);
	}

	public McJobSchedule(McJobRepository repository, int maxThreads, String scheduleName, String scheduleNameId,
			JobStore jobStore, McJobFactory jobFactory) {
		super();
		if (maxThreads > 0) {
			this.maxThreads = maxThreads;
		}
		if (StringUtils.isNotBlank(scheduleName)) {
			this.scheduleName = scheduleName;
		}
		if (StringUtils.isNotBlank(scheduleNameId)) {
			this.scheduleNameId = scheduleNameId;
		}
		this.repository = repository;
		this.jobStore = jobStore;
		this.jobFactory = jobFactory;
	}

	// ====================================
	public McJobSchedule init() throws McJobScheduleException {
		if (isInited) {
			return this;
		}
		if (repository == null) {
			repository = DefaultMcJobRepository.getInstance();
		}
		synchronized (lockObj) {

			if (isInited) {
				return this;
			}

			SimpleThreadPool threadPool = new SimpleThreadPool(maxThreads, Thread.NORM_PRIORITY);
			try {
				threadPool.initialize();
				if (jobStore == null) {
					jobStore = new RAMJobStore();
				}
				DirectSchedulerFactory factory = DirectSchedulerFactory.getInstance();
				scheduler = factory.getScheduler(scheduleName);
				if (scheduler == null) {
					factory.createScheduler(scheduleName, scheduleNameId, threadPool, jobStore);
					scheduler = factory.getScheduler(scheduleName);
				}
				if (jobFactory == null) {
					scheduler.setJobFactory(new JobFactory() {
						public Job newJob(TriggerFiredBundle bundle) throws SchedulerException {
							// job����
							String name = bundle.getJobDetail().getName();
							if (StringUtils.isNotBlank(name)) {
								// ����Դ������job
								McJob job = repository.getJob(name);

								// ִ�е����Ͱ�job��ɾ�ˣ�����ռ�
								if (bundle.getNextFireTime() == null) {
									McJobGroup groups = repository.getGroups(job.getDefinition().getGroupId());
									if (groups.getRepeatCount() == 0) {
										repository.removeJob(job.getDefinition().getGroupId(), job);
									}
								}
								return job;
							}
							return null;
						}
					});
				} else {
					jobFactory.setRepository(repository);
					scheduler.setJobFactory(jobFactory);
				}

			} catch (SchedulerConfigException e) {
				logger.error(e.getMessage(), e);
				throw new McJobScheduleException("Init jobSchedule failed.", e);
			} catch (SchedulerException e) {
				logger.error(e.getMessage(), e);
				throw new McJobScheduleException("Init jobSchedule failed.", e);
			}
			isInited = true;
		}
		return this;
	}

	/**
	 * �����첽����,������ö��
	 * 
	 * @throws McJobScheduleException
	 *             ��ͣ�쳣
	 */
	public void start() throws McJobScheduleException {
		if (!isInited) {
			throw new McJobScheduleException("The job schedule is not initialized.");
		}

		// ����������
		try {
			scheduler.start();
		} catch (SchedulerException e) {
			throw new McJobScheduleException("schudule run exception", e);
		}
	}

	/**
	 * ��ͣĳ��job
	 * 
	 * @param job
	 * @throws McJobScheduleException
	 *             ��ͣ�쳣
	 * @throws IllegalArgumentException
	 *             ����������ȷʱ�׳�
	 */
	public void pauseJob(McJob job) throws McJobScheduleException, IllegalArgumentException {
		if (!isInited) {
			throw new McJobScheduleException("The job schedule is not initialized.");
		}

		if (job == null || StringUtils.isBlank(job.getDefinition().getName())
				|| StringUtils.isBlank(job.getDefinition().getGroupId())) {
			throw new IllegalArgumentException("Job's groupId and name,or jobListenr's name  can't be emtpy.");
		}
		try {
			scheduler.pauseJob(job.getDefinition().getName(), job.getDefinition().getGroupId());
		} catch (SchedulerException e) {
			logger.error(e.getMessage(), e);
			throw new McJobScheduleException("schudule run exception", e);
		}
	}

	/**
	 * ��ͣĳ��group
	 * 
	 * @param group
	 * @throws McJobScheduleException
	 *             ��ͣ�쳣
	 * @throws IllegalArgumentException
	 *             ����������ȷʱ�׳�
	 */
	public void pauseJob(String gourpId) throws McJobScheduleException, IllegalArgumentException {
		if (!isInited) {
			throw new McJobScheduleException("The job schedule is not initialized.");
		}

		if (StringUtils.isBlank(gourpId)) {
			throw new IllegalArgumentException("Job's groupId and name,or jobListenr's name  can't be emtpy.");
		}
		try {
			scheduler.pauseJobGroup(gourpId);
		} catch (SchedulerException e) {
			logger.error(e.getMessage(), e);
			throw new McJobScheduleException("schudule pause exception", e);
		}
	}

	/**
	 * ֹͣ�첽�������
	 * 
	 * @throws CloudStoreException
	 */
	public void stop() throws McJobScheduleException {
		if (!isInited) {
			throw new McJobScheduleException("The job schedule is not initialized.");
		}

		try {
			scheduler.standby();
		} catch (SchedulerException e) {
			logger.error(e.getMessage(), e);
			throw new McJobScheduleException("schudule pause exception", e);
		}
	}

	/**
	 * ֹͣ�첽�������
	 * 
	 * @throws CloudStoreException
	 */
	public void destroy() throws McJobScheduleException {
		if (!isInited) {
			throw new McJobScheduleException("The job schedule is not initialized.");
		}

		try {
			scheduler.shutdown();
		} catch (SchedulerException e) {
			logger.error(e.getMessage(), e);
			throw new McJobScheduleException("schudule scheduler exception", e);
		}
	}

	/**
	 * ע��job
	 * 
	 * @param job
	 * @throws McJobScheduleException
	 *             ��ע��jobʧ�ܻ����쳣ʱ�׳�
	 * @throws IllegalArgumentException
	 *             ����������ȷʱ�׳�
	 */
	public synchronized void registerJob(McJob job) throws McJobScheduleException, IllegalArgumentException {
		if (!isInited) {
			throw new McJobScheduleException("The job schedule is not initialized.");
		}

		if (job == null || !job.validate()) {
			throw new IllegalArgumentException("Job's groupId and name,or jobListenr's name  can't be emtpy.");
		}

		if (scheduler != null) {

			JobDetail detail = new JobDetail();
			detail.setName(job.getDefinition().getName());
			detail.setGroup(job.getDefinition().getGroupId());
			detail.setJobClass(job.getClass());
			if (job.getDefinition().getJobListeners() != null && job.getDefinition().getJobListeners().size() > 0) {
				for (McJobListener listener : job.getDefinition().getJobListeners())
					if (listener != null) {
						try {
							scheduler.addJobListener(listener);
							detail.addJobListener(listener.getName());
						} catch (SchedulerException e) {
							logger.error(e.getMessage(), e);
							throw new RuntimeException("Add job listener exception", e);
						}

					}
			}

			McJobGroup group = repository.getGroups(job.getDefinition().getGroupId());

			if (group == null || !group.validate()) {
				throw new IllegalArgumentException(
						"Job's group not exist or the params cornExpression is empty, repeatInterval is less than 100ms.");
			}

			if (!repository.addJob(job.getDefinition().getGroupId(), job)) {
				throw new McJobScheduleException("Add job to  repository failed , current job size:"
						+ repository.getJobs().size());
			}

			// repeatInterval ���ȼ����� CornExpression;
			Trigger trig = null;
			if (group.getRepeatInterval() >= 100) {
				trig = new SimpleTrigger();
				((SimpleTrigger) trig).setRepeatInterval(group.getRepeatInterval());
				((SimpleTrigger) trig).setRepeatCount(group.getRepeatCount());
				((SimpleTrigger) trig).setStartTime(new Date());
				((SimpleTrigger) trig).setName("trigger_" + job.getDefinition().getGroupId() + "_"
						+ job.getDefinition().getName());
			} else {
				trig = new CronTrigger();
				try {
					((CronTrigger) trig).setCronExpression(group.getCornExpression());
				} catch (ParseException e) {
				}
				((CronTrigger) trig).setStartTime(new Date());
				((CronTrigger) trig).setName("trigger_" + job.getDefinition().getGroupId() + "_"
						+ job.getDefinition().getName());
			}
			job.setRegisterTime(System.currentTimeMillis());
			try {
				scheduler.scheduleJob(detail, trig);
			} catch (SchedulerException e) {
				logger.error(e.getMessage(), e);
				throw new McJobScheduleException("Add job listener exception", e);
			}
		}
	}

	/**
	 * ע��job
	 * 
	 * @param job
	 * @throws McJobScheduleException
	 *             ��ע��jobʧ�ܻ����쳣ʱ�׳�
	 * @throws IllegalArgumentException
	 *             ����������ȷʱ�׳�
	 */
	public synchronized void unRegisterJob(McJob job) throws McJobScheduleException, IllegalArgumentException {
		if (!isInited) {
			throw new McJobScheduleException("The job schedule is not initialized.");
		}

		if (job == null || !job.validate()) {
			throw new IllegalArgumentException("Job's groupId and name can't be emtpy.");
		}

		repository.removeJob(job.getDefinition().getGroupId(), job);
		try {
			scheduler.pauseJob(job.getDefinition().getName(), job.getDefinition().getGroupId());
			scheduler.deleteJob(job.getDefinition().getName(), job.getDefinition().getGroupId());
		} catch (SchedulerException e) {
			logger.error(e.getMessage(), e);
			throw new McJobScheduleException("UnRegister job exception", e);
		}
	}

	/**
	 * ͨ��job���ʺ�������ע��һ��job
	 * 
	 * @param jobName
	 * @param groupId
	 * @throws McJobScheduleException
	 * @throws IllegalArgumentException
	 */
	public synchronized void unRegisterJob(String groupId, String jobName) throws McJobScheduleException,
			IllegalArgumentException {
		if (!isInited) {
			throw new McJobScheduleException("The job schedule is not initialized.");
		}

		if (StringUtils.isBlank(jobName) || StringUtils.isBlank(groupId)) {
			throw new IllegalArgumentException("Job's groupId and name can't be emtpy.");
		}

		repository.removeJob(groupId, jobName);
		try {
			scheduler.pauseJob(jobName, groupId);
			scheduler.deleteJob(jobName, groupId);
		} catch (SchedulerException e) {
			logger.error(e.getMessage(), e);
			throw new McJobScheduleException("UnRegister job exception", e);
		}
	}

	public void setGroups(List<McJobGroup> groups) {
		if (groups == null) {
			return;
		}
		if (repository == null) {
			repository = DefaultMcJobRepository.getInstance();
		}

		for (McJobGroup group : groups) {
			repository.addGroups(group);
		}
	}

	/**
	 * ��ȡ��ǰ�������е�job��Ϣ(��ǰʱ��)
	 * 
	 * @return
	 * @throws McJobScheduleException
	 */
	public List<McJobDefinition> getCurrentRunningJob() throws McJobScheduleException {
		if (scheduler == null || !this.isInited) {
			return null;
		}
		try {
			List<JobExecutionContext> jobContexts = scheduler.getCurrentlyExecutingJobs();
			List<McJobDefinition> jobInfo = null;
			if (jobContexts != null && jobContexts.size() > 0) {
				jobInfo = new ArrayList<McJobDefinition>(jobContexts.size());
				for (JobExecutionContext context : jobContexts) {
					Job jobInstance = context.getJobInstance();
					if (jobInstance != null && jobInstance instanceof McJob) {
						McJob mcjob = (McJob) jobInstance;
						jobInfo.add(mcjob.getDefinition());
					}
				}
			}
			return jobInfo;
		} catch (SchedulerException e) {
			logger.error(e.getMessage(), e);
			throw new McJobScheduleException("UnRegister job exception", e);
		}
	}

	/**
	 * ��ȡ��ǰ�ֿ��д洢�ģ�Ҳ���Ѿ��ڴ���job�б�
	 * 
	 * @return
	 * @throws McJobScheduleException
	 */
	public List<McJobDefinition> getCurrentStoreJob() throws McJobScheduleException {
		if (repository == null || !this.isInited) {
			return null;
		}
		Map<String, McJob> jobs = repository.getJobs();
		List<McJobDefinition> jobInfo = null;
		if (jobs != null && jobs.size() > 0) {
			Collection<McJob> values = jobs.values();
			jobInfo = new ArrayList<McJobDefinition>(jobs.size());
			for (McJob job : values) {
				jobInfo.add(job.getDefinition());
			}
		}
		return jobInfo;
	}

	public int getMaxThreads() {
		return maxThreads;
	}

	public void setMaxThreads(int maxThreads) {
		this.maxThreads = maxThreads;
	}

	public McJobRepository getRepository() {
		return repository;
	}

	public void setRepository(McJobRepository repository) {
		this.repository = repository;
	}

	public String getScheduleName() {
		return scheduleName;
	}

	public void setScheduleName(String scheduleName) {
		this.scheduleName = scheduleName;
	}

	public String getScheduleNameId() {
		return scheduleNameId;
	}

	public void setScheduleNameId(String scheduleNameId) {
		this.scheduleNameId = scheduleNameId;
	}

	public JobStore getJobStore() {
		return jobStore;
	}

	public void setJobStore(JobStore jobStore) {
		this.jobStore = jobStore;
	}

	public McJobFactory getJobFactory() {
		return jobFactory;
	}

	public void setJobFactory(McJobFactory jobFactory) {
		this.jobFactory = jobFactory;
	}

	/**
	 * ����jobע��ʱ������ƣ���ȡjobʵ��
	 * 
	 * @param name
	 * @return McJob
	 * @throws McJobScheduleException
	 */
	public McJob getJobByName(String name) throws McJobScheduleException {
		if (!isInited) {
			throw new McJobScheduleException("The job schedule is not initialized.");
		}

		if (StringUtils.isNotBlank(name) && repository != null) {
			return repository.getJob(name);
		}
		return null;
	}

	/**
	 * ����groupid��ȡjobGroup
	 * 
	 * @param groupId
	 * @return McJobGroup
	 * @throws McJobScheduleException
	 */
	public McJobGroup getMcJobGroupByGroupId(String groupId) throws McJobScheduleException {
		if (!isInited) {
			throw new McJobScheduleException("The job schedule is not initialized.");
		}

		if (StringUtils.isNotBlank(groupId) && repository != null) {
			return repository.getGroups(groupId);
		}
		return null;
	}
}
