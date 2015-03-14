package com.github.tinyretry.timer.ext;

import org.quartz.Job;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.tinyretry.timer.domain.McJob;

/**
 * <pre>
 * desc: ����������jobִ��ǰ��ִ��һЩ��������jobʵ������
 * created: 2012-7-16 ����09:53:01
 * author: xiaofeng.zhouxf
 * todo: 
 * history:
 * </pre>
 */
public abstract class McJobListener implements JobListener {
	private static final Logger logger = LoggerFactory.getLogger(McJobListener.class);
	/**
	 * ����������
	 */
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void jobToBeExecuted(JobExecutionContext context) {
		if (context == null) {
			logger.warn("jobToBeExecuted Listener [{0}]'s context is empty", this.getName());
			return;
		}
		before(buildMcJobDefinition(context));
	}

	public void jobExecutionVetoed(JobExecutionContext context) {

	}

	public void jobWasExecuted(JobExecutionContext context, JobExecutionException jobException) {
		if (context == null) {
			logger.warn("jobWasExecuted Listener [{0}]'s context is empty", this.getName());
			return;
		}
		after(buildMcJobDefinition(context));
	}

	private McJobDefinition buildMcJobDefinition(JobExecutionContext context) {
		McJobDefinition definition = new McJobDefinition();
		Job job = context.getJobInstance();

		if (job != null && job instanceof McJob) {
			definition = ((McJob) job).getDefinition();
		} else {
			JobDetail jobDetail = context.getJobDetail();
			if (jobDetail != null) {
				definition.setDescription(jobDetail.getDescription());
				definition.setGroupId(jobDetail.getGroup());
				definition.setJobClass(jobDetail.getJobClass());
				definition.setJobDataMap(jobDetail.getJobDataMap());
				definition.setName(name);
			}
		}
		return definition;
	}

	/**
	 * �ڶ�ʱ����ÿһ��ִ��ǰִ��
	 * 
	 * @param definition
	 */
	public abstract void before(McJobDefinition definition);

	/**
	 * �ڶ�ʱ����ÿһ��ִ�к�ִ��
	 * 
	 * @param definition
	 */
	public abstract void after(McJobDefinition definition);

}
