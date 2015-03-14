package com.github.tinyretry.timer.ext.jobfactory;

import org.apache.commons.lang.StringUtils;
import org.quartz.spi.TriggerFiredBundle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.tinyretry.timer.domain.McJob;
import com.github.tinyretry.timer.domain.McJobGroup;

/**
 * <pre>
 * desc: ����Դ����ɾ��������������񹤹���
 * created: 2012-9-20 ����03:50:42
 * author: xiaofeng.zhouxf
 * todo: 
 * history:
 * </pre>
 */

public class RemoveAllJobFactory extends McJobFactory {
	private static final Logger logger = LoggerFactory.getLogger(RemoveAllJobFactory.class);

	@Override
	public McJob createJob(TriggerFiredBundle bundle) {
		// job����
		String name = bundle.getJobDetail().getName();
		if (StringUtils.isNotBlank(name)) {
			McJob job = null;
			if (repository != null) {
				// ����Դ������job
				job = repository.getJob(name);

				// ִ�е����Ͱ�job��group��ɾ�ˣ�����ռ�
				if (bundle.getNextFireTime() == null) {
					McJobGroup groups = repository.getGroups(job.getDefinition().getGroupId());
					if (groups.getRepeatCount() == 0) {
						repository.removeJob(job.getDefinition().getGroupId(), job);
						repository.removeGroups(job.getDefinition().getGroupId());
					}
				}
			}
			return job;
		}
		return null;
	}
}
