package com.github.tinyretry.timer.ext.jobfactory;

import org.apache.commons.lang.ClassUtils;
import org.quartz.Job;
import org.quartz.JobDetail;
import org.quartz.SchedulerException;
import org.quartz.spi.JobFactory;
import org.quartz.spi.TriggerFiredBundle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.tinyretry.timer.domain.McJob;
import com.github.tinyretry.timer.domain.McJobRepository;

/**
 * <pre>
 * desc: ���񴴽�������ʵ����{@code org.quartz.spi.JobFactory}
 * created: 2012-7-16 ����09:24:35
 * author: xiaofeng.zhouxf
 * todo: ���ڴ������������񵽴�����ʱ���ʱ�����ٷ������createJob����������ֻ��Ҫʵ��createJob����
 * history:
 * </pre>
 */
public abstract class McJobFactory implements JobFactory {
	private static final Logger logger = LoggerFactory.getLogger(McJobFactory.class);

	/**
	 * ������Դ��
	 */
	protected McJobRepository repository;

	public Job newJob(TriggerFiredBundle bundle) throws SchedulerException {

		JobDetail jobDetail = bundle.getJobDetail();
		if (jobDetail != null && ClassUtils.isAssignable(jobDetail.getJobClass(), McJob.class)) {
			return createJob(bundle);
		}
		try {
			return (jobDetail != null && jobDetail.getJobClass() != null ? (Job) jobDetail.getJobClass().newInstance()
					: null);
		} catch (InstantiationException e) {
			logger.error(e.getMessage(), e);
		} catch (IllegalAccessException e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}

	public abstract McJob createJob(TriggerFiredBundle bundle);

	public McJobRepository getRepository() {
		return repository;
	}

	public void setRepository(McJobRepository repository) {
		this.repository = repository;
	}

}
