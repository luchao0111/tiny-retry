package com.github.tinyretry.retry.domain;

import java.io.Serializable;
import java.util.Date;

/**
 * <pre>
 * desc: 
 * created: 2012-9-5 ����10:48:18
 * author: xiaofeng.zhouxf
 * todo: 
 * history:
 * </pre>
 */
public class TaskUpdate implements Serializable {
	private static final long serialVersionUID = -364248461076192575L;

	/**
	 * ��������
	 */
	private Long id;

	/**
	 * ��������
	 */
	private String type;

	/**
	 * �������������Ϣ
	 */
	private byte[] context;

	/**
	 * ����İ汾
	 */
	private Short version;

	/**
	 * ����״̬
	 */
	private Short status;

	/**
	 * ��ʼֵ��0��ÿ����һ�μ�1
	 */
	private Short retryTime;

	/**
	 * ��һ��ִ�е�ʱ�䣬��ǰʱ����January 1, 1970 UTC ֮��Ĳ�ֵ
	 */
	private Long nextTime;

	/**
	 * �������¼���һ��ִ��ʧ��ԭ��
	 */
	private String failReason;

	/**
	 * ��¼����ʱ��
	 */
	private Date gmtCreate;

	/**
	 * ��¼����޸�ʱ��
	 */
	private Date gmtModified;

	/**
	 * ���ȼ���ֵԽ�����ȼ�Խ�ߣ�Ĭ��9
	 */
	private Short priority;

	/**
	 * �����Ӧ��Ӧ��
	 */
	private String appCode;

	/**
	 * �����ߣ�����ָ�������߷����
	 */
	private String processor;

	/**
	 * �ϵ�״̬
	 */
	private Short oldStatus;

	/**
	 * �ϵİ汾
	 */
	private Short oldVersion;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public byte[] getContext() {
		return context;
	}

	public void setContext(byte[] context) {
		this.context = context;
	}

	public Short getVersion() {
		return version;
	}

	public void setVersion(Short version) {
		this.version = version;
	}

	public Short getStatus() {
		return status;
	}

	public void setStatus(Short status) {
		this.status = status;
	}

	public Short getRetryTime() {
		return retryTime;
	}

	public void setRetryTime(Short retryTime) {
		this.retryTime = retryTime;
	}

	public Long getNextTime() {
		return nextTime;
	}

	public void setNextTime(Long nextTime) {
		this.nextTime = nextTime;
	}

	public String getFailReason() {
		return failReason;
	}

	public void setFailReason(String failReason) {
		this.failReason = failReason;
	}

	public Date getGmtCreate() {
		return gmtCreate;
	}

	public void setGmtCreate(Date gmtCreate) {
		this.gmtCreate = gmtCreate;
	}

	public Date getGmtModified() {
		return gmtModified;
	}

	public void setGmtModified(Date gmtModified) {
		this.gmtModified = gmtModified;
	}

	public Short getPriority() {
		return priority;
	}

	public void setPriority(Short priority) {
		this.priority = priority;
	}

	public String getAppCode() {
		return appCode;
	}

	public void setAppCode(String appCode) {
		this.appCode = appCode;
	}

	public String getProcessor() {
		return processor;
	}

	public void setProcessor(String processor) {
		this.processor = processor;
	}

	public Short getOldStatus() {
		return oldStatus;
	}

	public void setOldStatus(Short oldStatus) {
		this.oldStatus = oldStatus;
	}

	public Short getOldVersion() {
		return oldVersion;
	}

	public void setOldVersion(Short oldVersion) {
		this.oldVersion = oldVersion;
	}
}
