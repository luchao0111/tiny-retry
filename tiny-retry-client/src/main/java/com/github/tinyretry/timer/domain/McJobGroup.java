package com.github.tinyretry.timer.domain;

import org.apache.commons.lang.StringUtils;
import org.quartz.SimpleTrigger;

/**
 * <pre>
 * desc: ��ʱ������
 * created: 2012-5-7 ����02:09:19
 * author: xiaofeng.zhouxf
 * todo: ��ʱ�������ĳ���������ʱ��������������ԣ�������ʱ��㣬�ظ������ȡ�
 * history:
 * </pre>
 */
public class McJobGroup {
	/**
	 * ����ID���������������飬��ͬ���ID���벻ͬ
	 */
	private String groupId;
	/**
	 * ��ʱ���ʽ
	 * 
	 * @see org.quartz.CronExpression
	 */
	private String cornExpression;
	/**
	 * ���ٴ���100����
	 */
	private long repeatInterval;

	/**
	 * ���Դ���
	 */
	private int repeatCount = SimpleTrigger.REPEAT_INDEFINITELY;

	public McJobGroup() {
		super();
	}

	
	
	public McJobGroup(String groupId, int repeatInterval, int repeatCount) {
		super();
		this.groupId = groupId;
		this.repeatInterval = repeatInterval;
		this.repeatCount = repeatCount;
	}

	public McJobGroup(String groupId, String cornExpression) {
		super();
		this.groupId = groupId;
		this.cornExpression = cornExpression;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String getCornExpression() {
		return cornExpression;
	}

	public void setCornExpression(String cornExpression) {
		this.cornExpression = cornExpression;
	}

	public long getRepeatInterval() {
		return repeatInterval;
	}

	public void setRepeatInterval(long repeatInterval) {
		this.repeatInterval = repeatInterval;
	}

	public int getRepeatCount() {
		return repeatCount;
	}

	public void setRepeatCount(int repeatCount) {
		this.repeatCount = repeatCount;
	}

	public boolean validate() {
		if (StringUtils.isBlank(groupId) || (StringUtils.isBlank(cornExpression) && this.repeatInterval < 100)) {
			return false;
		}
		return true;
	}
}
