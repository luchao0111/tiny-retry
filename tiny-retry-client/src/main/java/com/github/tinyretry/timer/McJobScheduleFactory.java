package com.github.tinyretry.timer;

import org.quartz.spi.JobStore;

import com.github.tinyretry.timer.domain.McJobRepository;
import com.github.tinyretry.timer.ext.McJobStore;
import com.github.tinyretry.timer.ext.jobfactory.McJobFactory;

/**
 * <pre>
 * desc: 
 * �������������.
 * Ŀǰ������֧�ֶ�ʱ��������ȣ�֧���Զ����߳�����
 * ֧���Զ���job�洢��ֻ��Ҫʵ��McJobStore���ɣ�������ʹ��Ĭ���ڴ汣��job��
 * ��Ϊʵ�ֱȽϸ��ӣ�������ʺϸ�Ӧ�õ�������������ʱ�����־û�jobû���κ����ơ�
 * 
 * ע�⣺
 *    1. ����ʱscheduleName,schedulerInstanceId��һ��jvm�в����ظ�������ᱨ��
 *    2. ʧ��������ģ���в�֧�֣���Ҫjob��ҵ���Լ�����ɡ�
 * created: 2012-7-16 ����09:12:26
 * author: xiaofeng.zhouxf
 * todo: 
 * history:
 * </pre>
 */
public class McJobScheduleFactory {

	public static McJobSchedule createMcJobSchedule(int maxThreads, String scheduleName, String schedulerInstanceId)
			throws McJobScheduleException {
		return createMcJobSchedule(null, maxThreads, scheduleName, schedulerInstanceId, null, null);
	}

	public static McJobSchedule createMcJobSchedule(int maxThreads) throws McJobScheduleException {
		return createMcJobSchedule(null, maxThreads, null, null, null, null);
	}

	public static McJobSchedule createMcJobSchedule(McJobRepository repository, int maxThreads,
			String scheduleName, String scheduleNameId) throws McJobScheduleException {
		return createMcJobSchedule(repository, maxThreads, scheduleName, scheduleNameId, null, null);
	}

	public static McJobSchedule createMcJobSchedule(int maxThreads, String scheduleName, String scheduleNameId,
			McJobStore jobStore) throws McJobScheduleException {
		return createMcJobSchedule(null, maxThreads, scheduleName, scheduleNameId, jobStore, null);
	}

	public static McJobSchedule createMcJobSchedule(int maxThreads, String scheduleName, String scheduleNameId,
			JobStore jobStore, McJobFactory jobFactory) throws McJobScheduleException {
		return createMcJobSchedule(null, maxThreads, scheduleName, scheduleNameId, jobStore, jobFactory);
	}

	public static McJobSchedule createMcJobSchedule(int maxThreads, McJobFactory jobFactory)
			throws McJobScheduleException {
		return createMcJobSchedule(maxThreads, null, null, null, jobFactory);
	}

	public static McJobSchedule createMcJobSchedule(McJobRepository repository, int maxThreads, String scheduleName,
			String scheduleNameId, JobStore jobStore, McJobFactory jobFactory) throws McJobScheduleException {
		return new McJobSchedule(repository, maxThreads, scheduleName, scheduleNameId, jobStore, jobFactory).init();
	}
}
