package com.github.tinyretry.timer.event;

/**
 * <pre>
 * desc: 
 * created: Jul 24, 2013 3:42:18 PM
 * author: xiangfeng
 * todo: 
 * history:
 * </pre>
 */
public interface ReportAble {

	/**
	 * ��ȡjob�ĺ�ʱ�������jobע�ᵽִ�гɹ�����ʧ�ܵ�ʱ��
	 * 
	 * @return ��ʱ����λ����
	 */
	public long getElapsedTime();

	/**
	 * ����ִ�е�ʱ��
	 * 
	 * @return
	 */
	public long getActualRunTime();

	/**
	 * ���سɹ���,С�ڵ���1��С��������С�������4λ
	 * 
	 * @return
	 */
	public float successRate();
}
