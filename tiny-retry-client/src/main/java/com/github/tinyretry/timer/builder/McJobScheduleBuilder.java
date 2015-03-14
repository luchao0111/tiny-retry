package com.github.tinyretry.timer.builder;

import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.tinyretry.timer.McJobSchedule;
import com.github.tinyretry.timer.McJobScheduleException;
import com.github.tinyretry.timer.domain.McJob;
import com.github.tinyretry.timer.domain.McJobGroup;

/**
 * <pre>
 * desc: �����Ķ�ʱ���񹹽���ʽ
 * created: 2012-9-20 ����01:21:56
 * author: xiaofeng.zhouxf
 * todo: 
 * history:
 * </pre>
 */
public abstract class McJobScheduleBuilder {
	private static final Logger logger = LoggerFactory.getLogger(McJobScheduleBuilder.class);

	/**
	 * ���������
	 */
	protected McJobSchedule schedule;

	/**
	 * ����ʵ���ڵ����������Ƶĺ�׺
	 */
	protected final String INSTANT_SUFFIX = "_INSTANT";

	/**
	 * ������������
	 * 
	 * @throws McJobScheduleException
	 * 
	 */
	public abstract McJobScheduleBuilder createMcJobSchedule(String scheduleName, int maxThreads)
			throws McJobScheduleException;

	/**
	 * ����job
	 * 
	 * @param job
	 * @return
	 */
	public McJobScheduleBuilder executeJob(McJob job) {
		if (job != null && schedule != null) {
			try {
				schedule.init();
				schedule.registerJob(job);
				schedule.start();
			} catch (McJobScheduleException e) {
				logger.error("ExecuteJob exception.", e);
			}
		}
		return this;
	}

	public McJobScheduleBuilder addGroups(McJobGroup... groups) {
		if (groups != null && schedule != null) {
			schedule.setGroups(Arrays.asList(groups));
		}
		return this;
	}

	public McJobSchedule getSchedule() {
		return schedule;
	}

}
