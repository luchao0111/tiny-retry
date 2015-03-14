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
public class TaskQuery {
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
	private Long beginNextTime;

	/**
	 * ��¼����޸�ʱ��
	 */
	private Long endNextTime;

	/**
	 * �����Ӧ��Ӧ��
	 */
	private String appCode;

	/**
	 * �����ߣ�����ָ�������߷����
	 */
	private String processor;

	/**
	 * ��ҳ����,count����������
	 */
	private Integer offset;

	/**
	 * ��ҳ����,���100��count����������
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

	public Long getBeginNextTime() {
		return beginNextTime;
	}

	public Long getEndNextTime() {
		return endNextTime;
	}

	public void setBeginNextTime(Long beginNextTime) {
		this.beginNextTime = beginNextTime;
	}

	public void setEndNextTime(Long endNextTime) {
		this.endNextTime = endNextTime;
	}

}
