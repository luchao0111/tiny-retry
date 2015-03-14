package com.github.tinyretry.retry.domain;

import java.io.Serializable;
import java.util.Date;

/**
 * �첽�����
 * 
 * @author xiaofeng Date 2012-09-05
 */
public class McTaskDO implements Serializable {

	private static final long serialVersionUID = 6612913770737906515L;

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
	 * ���ȼ���ֵԽ�����ȼ�Խ�ߣ�Ĭ��9, ���99
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
	 * setter for column ��������
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * getter for column ��������
	 */
	public Long getId() {
		return this.id;
	}

	/**
	 * setter for column ��������
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * getter for column ��������
	 */
	public String getType() {
		return this.type;
	}

	/**
	 * setter for column �������������Ϣ
	 */
	public void setContext(byte[] context) {
		this.context = context;
	}

	/**
	 * getter for column �������������Ϣ
	 */
	public byte[] getContext() {
		return this.context;
	}

	/**
	 * setter for column ����İ汾
	 */
	public void setVersion(Short version) {
		this.version = version;
	}

	/**
	 * getter for column ����İ汾
	 */
	public Short getVersion() {
		return this.version;
	}

	/**
	 * setter for column ����״̬
	 */
	public void setStatus(Short status) {
		this.status = status;
	}

	/**
	 * getter for column ����״̬
	 */
	public Short getStatus() {
		return this.status;
	}

	/**
	 * setter for column ��ʼֵ��0��ÿ����һ�μ�1
	 */
	public void setRetryTime(Short retryTime) {
		this.retryTime = retryTime;
	}

	/**
	 * getter for column ��ʼֵ��0��ÿ����һ�μ�1
	 */
	public Short getRetryTime() {
		return this.retryTime;
	}

	/**
	 * setter for column ��һ��ִ�е�ʱ�䣬��ǰʱ����January 1, 1970 UTC ֮��Ĳ�ֵ
	 */
	public void setNextTime(Long nextTime) {
		this.nextTime = nextTime;
	}

	/**
	 * getter for column ��һ��ִ�е�ʱ�䣬��ǰʱ����January 1, 1970 UTC ֮��Ĳ�ֵ
	 */
	public Long getNextTime() {
		return this.nextTime;
	}

	/**
	 * setter for column �������¼���һ��ִ��ʧ��ԭ��
	 */
	public void setFailReason(String failReason) {
		this.failReason = failReason;
	}

	/**
	 * getter for column �������¼���һ��ִ��ʧ��ԭ��
	 */
	public String getFailReason() {
		return this.failReason;
	}

	/**
	 * setter for column ��¼����ʱ��
	 */
	public void setGmtCreate(Date gmtCreate) {
		this.gmtCreate = gmtCreate;
	}

	/**
	 * getter for column ��¼����ʱ��
	 */
	public Date getGmtCreate() {
		return this.gmtCreate;
	}

	/**
	 * setter for column ��¼����޸�ʱ��
	 */
	public void setGmtModified(Date gmtModified) {
		this.gmtModified = gmtModified;
	}

	/**
	 * getter for column ��¼����޸�ʱ��
	 */
	public Date getGmtModified() {
		return this.gmtModified;
	}

	/**
	 * setter for column ���ȼ���ֵԽ�����ȼ�Խ�ߣ�Ĭ��9
	 */
	public void setPriority(Short priority) {
		this.priority = priority;
	}

	/**
	 * getter for column ���ȼ���ֵԽ�����ȼ�Խ�ߣ�Ĭ��9
	 */
	public Short getPriority() {
		return this.priority;
	}

	/**
	 * setter for column �����Ӧ��Ӧ��
	 */
	public void setAppCode(String appCode) {
		this.appCode = appCode;
	}

	/**
	 * getter for column �����Ӧ��Ӧ��
	 */
	public String getAppCode() {
		return this.appCode;
	}

	/**
	 * setter for column �����ߣ�����ָ�������߷����
	 */
	public void setProcessor(String processor) {
		this.processor = processor;
	}

	/**
	 * getter for column �����ߣ�����ָ�������߷����
	 */
	public String getProcessor() {
		return this.processor;
	}
}
