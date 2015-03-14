package com.github.tinyretry.retry.domain;

import java.io.Serializable;

/**
 * <pre>
 * desc: ������
 * 
 *  
 *  retryPeriod: ����� �������ں��룬������ʹ�������ļ�Ĭ��ֵ	 
 *  retryTime: ��������Դ�����������ʹ�������ļ�Ĭ��ֵ
 *  retryFactor: �������������Ȩ�أ�ÿ��һ��ִ�оͻ���Ը�ֵ��������ʹ�������ļ�Ĭ��ֵ
 *  appCode: �������Ӧ��ҵ���룬������ʹ�������ļ�Ĭ��ֵ
 *  processor: �����������
 *  taskName: �����task���֣� ��֤��һ��Ӧ������Ψһ�ģ���Ҫ��λ����ѯ
 *  
 *  description: ѡ����������� 
 *  nextTime: ѡ����´�ִ�е�ʱ�� 
 *  currentTime: ѡ�����ǰִ�д���,Ĭ��Ϊ1 
 *  taskId: ѡ�����ǰ���е�����id, ������д����������Ѽ�¼���ݿ⣬����д��Ӧ��¼ID	 
 *  priority: ѡ����������ȼ���Խ�����ȼ�Խ��
 * 
 * created: 2012-8-23 ����01:36:26
 * author: xiaofeng.zhouxf
 * todo: 
 * history:
 * </pre>
 */
public class Task implements Serializable {
	private static final long serialVersionUID = -6160900702908678856L;

	/**
	 * ��չ��¼��Ϣ�������ַ����ļ�¼����
	 */
	private String taskStrId;

	/**
	 * ѡ�����ǰ���е�����id, ������д����������Ѽ�¼���ݿ⣬����д��Ӧ��¼ID
	 */
	private long taskId;

	/**
	 * ����� �������ں��룬������ʹ�������ļ�Ĭ��ֵ
	 */
	private long retryPeriod;

	/**
	 * ��������Դ�����������ʹ�������ļ�Ĭ��ֵ
	 */
	private short retryTime;

	/**
	 * �������������Ȩ�أ�ÿ��һ��ִ�оͻ���Ը�ֵ��������ʹ�������ļ�Ĭ��ֵ
	 */
	private float retryFactor;

	/**
	 * �����������
	 */
	private Processor processor;

	/**
	 * �����task���֣� ��֤��һ��Ӧ������Ψһ�ģ���Ҫ��λ����ѯ
	 */
	private String taskName;

	/**
	 * ѡ�����������
	 */
	private String description;

	/**
	 * ѡ����´�ִ�е�ʱ��
	 */
	private long nextTime;

	/**
	 * ִ�г�ʱʱ�䣬��λ����,��ʱ��ᱻ����ע�ᵽjob��,֮ǰ��job�ᱻע��
	 */
	private long executeTimeOut;

	/**
	 * ѡ�����ǰִ�д���,Ĭ��Ϊ1
	 */
	private int currentTime = 1;

	/**
	 * �������Ӧ��ҵ���룬������ʹ�������ļ�Ĭ��ֵ
	 */
	private String appCode;

	/**
	 * ѡ����������ȼ���Խ�����ȼ�Խ��
	 */
	private short priority;

	/**
	 * ��չ�ֶ�,���ڴ洢һЩ������Ϣ
	 */
	private String extMsg;

	// =========================

	public Task() {
		super();
	}

	/**
	 * ����һ���µ�task
	 * 
	 * @param task
	 */
	public Task(Task task) {
		this.setTaskId(task.getTaskId());
		this.setAppCode(task.getAppCode());
		this.setCurrentTime(task.getCurrentTime());
		this.setDescription(task.getDescription());
		this.setExecuteTimeOut(task.getExecuteTimeOut());
		this.setNextTime(task.getNextTime());
		this.setPriority(task.getPriority());
		this.setProcessor(task.getProcessor());
		this.setRetryFactor(task.getRetryFactor());
		this.setRetryPeriod(task.getRetryPeriod());
		this.setTaskName(task.getTaskName());
		this.setRetryTime(task.getRetryTime());
	}

	public Task(Processor processor, String taskName) {
		super();
		this.processor = processor;
		this.taskName = taskName;
	}

	public Task(long retryPeriod, short retryTime, float retryFactor, Processor process, String taskName) {
		this(process, taskName);
		this.retryPeriod = retryPeriod;
		this.retryTime = retryTime;
		this.retryFactor = retryFactor;
	}

	// =========================
	public long getRetryPeriod() {
		return retryPeriod;
	}

	/**
	 * ����� �������ں��룬������ʹ�������ļ�Ĭ��ֵ
	 */
	public void setRetryPeriod(long retryPeriod) {
		this.retryPeriod = retryPeriod;
	}

	public short getRetryTime() {
		return retryTime;
	}

	/**
	 * ��������Դ�����������ʹ�������ļ�Ĭ��ֵ
	 */
	public void setRetryTime(short retryTime) {
		this.retryTime = retryTime;
	}

	public float getRetryFactor() {
		return retryFactor;
	}

	/**
	 * �������������Ȩ�أ�ÿ��һ��ִ�оͻ���Ը�ֵ��������ʹ�������ļ�Ĭ��ֵ
	 */
	public void setRetryFactor(float retryFactor) {
		this.retryFactor = retryFactor;
	}

	public Processor getProcessor() {
		return processor;
	}

	/**
	 * �����������
	 */
	public void setProcessor(Processor processor) {
		this.processor = processor;
	}

	public String getDescription() {
		return description;
	}

	/**
	 * ѡ�����������
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	public String getTaskName() {
		return taskName;
	}

	/**
	 * �����task���֣� ��֤��һ��Ӧ������Ψһ�ģ���Ҫ��λ����ѯ
	 */
	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	public long getNextTime() {
		return nextTime;
	}

	/**
	 * ѡ����´�ִ�е�ʱ��
	 */
	public void setNextTime(long nextTime) {
		this.nextTime = nextTime;
	}

	public int getCurrentTime() {
		return currentTime;
	}

	/**
	 * ��ǰִ�д���,Ĭ��Ϊ1
	 */
	public void setCurrentTime(int currentTime) {
		this.currentTime = currentTime;
	}

	public String getAppCode() {
		return appCode;
	}

	/**
	 * �������Ӧ��ҵ���룬������ʹ�������ļ�Ĭ��ֵ
	 */
	public void setAppCode(String appCode) {
		this.appCode = appCode;
	}

	public short getPriority() {
		return priority;
	}

	/**
	 * ѡ����������ȼ���Խ�����ȼ�Խ��
	 */
	public void setPriority(short priority) {
		this.priority = priority;
	}

	public long getTaskId() {
		return taskId;
	}

	/**
	 * ѡ�����ǰ���е�����id, ������д����������Ѽ�¼���ݿ⣬����д��Ӧ��¼ID
	 */
	public void setTaskId(long taskId) {
		this.taskId = taskId;
	}

	public long getExecuteTimeOut() {
		return executeTimeOut;
	}

	public void setExecuteTimeOut(long executeTimeOut) {
		this.executeTimeOut = executeTimeOut;
	}

	public String getTaskStrId() {
		return taskStrId;
	}

	public void setTaskStrId(String taskStrId) {
		this.taskStrId = taskStrId;
	}

	public String getExtMsg() {
		return extMsg;
	}

	public void setExtMsg(String extMsg) {
		this.extMsg = extMsg;
	}

}
