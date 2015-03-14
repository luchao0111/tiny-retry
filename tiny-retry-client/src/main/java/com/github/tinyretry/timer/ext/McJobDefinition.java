package com.github.tinyretry.timer.ext;

import java.util.List;

import org.quartz.JobDataMap;

/**
 * <pre>
 * desc: job������
 * created: 2012-7-16 ����09:32:01
 * author: xiaofeng.zhouxf
 * todo: ������ĳ�������������Ϣ�������������ƣ���ID,�������б����Ϣ
 * history:
 * </pre>
 */
public class McJobDefinition {

	/**
	 * job����
	 */
	private String name;

	/**
	 * ����
	 */
	private String groupId;

	/**
	 * job����
	 */
	private String description;

	/**
	 * ��ǰjob��class��Ϣ
	 */
	private Class jobClass;

	/**
	 * ��job�Ĺ�������
	 */
	private JobDataMap jobDataMap;

	/**
	 * �����ļ�����
	 */
	private List<McJobListener> jobListeners;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Class getJobClass() {
		return jobClass;
	}

	public void setJobClass(Class jobClass) {
		this.jobClass = jobClass;
	}

	public JobDataMap getJobDataMap() {
		return jobDataMap;
	}

	public void setJobDataMap(JobDataMap jobDataMap) {
		this.jobDataMap = jobDataMap;
	}

	public List<McJobListener> getJobListeners() {
		return jobListeners;
	}

	public void setJobListeners(List<McJobListener> jobListeners) {
		this.jobListeners = jobListeners;
	}
}
