package com.github.tinyretry.retry.constants;

/**
 * <pre>
 * desc: ����״̬
 * created: 2012-6-13 ����02:37:46
 * author: xiaofeng.zhouxf
 * todo: 
 * history:
 * </pre>
 */
public enum TaskStatus {
	// ������
	WAITING((short) 1),
	// ������
	DEALING((short) 2),
	// ����ʧ��
	FAIL((short) 3),
	// ����ɹ�
	SUCCESS((short) 4),
	// ������
	REMOVING((short) 5),
	// �ȴ��˹���Ԥ
	MANUAL((short) 6),
	// ��ʱ������
	TIMEOUT_DEAILING((short) 7);

	private short status;

	private TaskStatus(short status) {
		this.status = status;
	}

	public short getStatus() {
		return status;
	}
}
