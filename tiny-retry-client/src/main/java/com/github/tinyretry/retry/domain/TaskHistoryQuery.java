package com.github.tinyretry.retry.domain;

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
public class TaskHistoryQuery {
	/**
	 * ��������
	 */
	private Long id;

	/**
	 * ��������
	 */
	private String type;

	/**
	 * ����İ汾
	 */
	private Short version;

	/**
	 * ����״̬
	 */
	private Short status;

	/**
	 * ��¼����ʱ��
	 */
	private Date beginCreateTime;

	/**
	 * ��¼����ʱ��
	 */
	private Date endCreateTime;

	/**
	 * ��¼����޸�ʱ��
	 */
	private Date beginModifiedTime;

	/**
	 * ��¼����޸�ʱ��
	 */
	private Date endModifiedTime;

	/**
	 * ��¼����޸�ʱ��
	 */
	private Long beginFinishTime;

	/**
	 * ��¼����޸�ʱ��
	 */
	private Long endFinishTime;

	/**
	 * �����Ӧ��Ӧ��
	 */
	private String appCode;

	/**
	 * �����ߣ�����ָ�������߷����
	 */
	private String processor;

	/**
	 * ��ҳ����
	 */
	private Integer offset;

	/**
	 * ��ҳ����
	 */
	private Integer length;

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

	public Date getBeginCreateTime() {
		return beginCreateTime;
	}

	public void setBeginCreateTime(Date beginCreateTime) {
		this.beginCreateTime = beginCreateTime;
	}

	public Date getEndCreateTime() {
		return endCreateTime;
	}

	public void setEndCreateTime(Date endCreateTime) {
		this.endCreateTime = endCreateTime;
	}

	public Date getBeginModifiedTime() {
		return beginModifiedTime;
	}

	public void setBeginModifiedTime(Date beginModifiedTime) {
		this.beginModifiedTime = beginModifiedTime;
	}

	public Date getEndModifiedTime() {
		return endModifiedTime;
	}

	public void setEndModifiedTime(Date endModifiedTime) {
		this.endModifiedTime = endModifiedTime;
	}

	public Long getBeginFinishTime() {
		return beginFinishTime;
	}

	public void setBeginFinishTime(Long beginFinishTime) {
		this.beginFinishTime = beginFinishTime;
	}

	public Long getEndFinishTime() {
		return endFinishTime;
	}

	public void setEndFinishTime(Long endFinishTime) {
		this.endFinishTime = endFinishTime;
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

	public Integer getOffset() {
		return offset;
	}

	public void setOffset(Integer offset) {
		this.offset = offset;
	}

	public Integer getLength() {
		return length;
	}

	public void setLength(Integer length) {
		this.length = length;
	}

}
