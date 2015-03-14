package com.github.tinyretry.retry.domain;

import java.io.Serializable;
import java.util.Date;

/**
 * �첽��ʷ�����
 * 
 * @author xiaofeng Date 2012-09-05
 */
public class McTaskHistoryDO implements Serializable {

	private static final long serialVersionUID = 2697826406113224823L;

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
	 * �������ʱ��
	 */
	private Date finishedTime;

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
	 * ����Ӧ��Ӧ����
	 */
	private String appCode;

	/**
	 * ��ǰ�������ߣ������ǻ����������
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
	 * setter for column �������ʱ��
	 */
	public void setFinishedTime(Date finishedTime) {
		this.finishedTime = finishedTime;
	}

	/**
	 * getter for column �������ʱ��
	 */
	public Date getFinishedTime() {
		return this.finishedTime;
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
	 * setter for column ����Ӧ��Ӧ����
	 */
	public void setAppCode(String appCode) {
		this.appCode = appCode;
	}

	/**
	 * getter for column ����Ӧ��Ӧ����
	 */
	public String getAppCode() {
		return this.appCode;
	}

	/**
	 * setter for column ��ǰ�������ߣ������ǻ����������
	 */
	public void setProcessor(String processor) {
		this.processor = processor;
	}

	/**
	 * getter for column ��ǰ�������ߣ������ǻ����������
	 */
	public String getProcessor() {
		return this.processor;
	}
}
