package com.github.tinyretry.retry.constants;

/**
 * <pre>
 * desc: 
 * created: 2012-8-23 ����01:27:47
 * author: xiaofeng.zhouxf
 * todo: 
 * history:
 * </pre>
 */
public interface Constants {
	// Ĭ�ϵ������ļ�
	String DEFAULT_RETRY_CONFIG_FAILE = "/default_retry_config.xml";
	// zk�ڵ�����
	String RETRY_ZK_NODE_NAME_TASK = "retry_worker_{0}_task_{1}";

	// ��ѯ��һ������ʱ��������������ڵ�ǰʱ��Ļ����ϻ������ֵ����ѯ
	long NEXT_TIME_ADDTINO = 1500l;

	// һ���Ӳ�ѯһ��
	long NEXT_TIME_TIMEOUT = 60000l;

	String EMPTY = "empty";

	// ==========configuretion===============
	// ���Դ���������
	String RETRY_SYS_COUNT = "retry.sys.retry.count";
	// ��������������
	String RETRY_SYS_PERIOD_MILLISECOND = "retry.sys.retry.period.millisecond";
	// ������������������
	String RETRY_SYS_PERIOD_FACTOR = "retry.sys.retry.period.factor";
	// Ӧ����
	String RETRY_SYS_APP_CODE = "retry.app.code";
	// ����Ĭ�ϵ�ִ�г�ʱʱ��
	String RETRY_SYS_EXECUTE_TIMEOUT = "retry.sys.retry.execute.timeout";
	// ZK���ӵ�ַ
	String ZOOKEEPER_CONNECT_STR = "retry.zookeeper.connect.str";
	// zk��ʱʱ��
	String ZOOKEEPER_CONNECT_TIME_OUT = "retry.zookeeper.connect.time.out";
	// zk�������Դ���
	String ZOOKEEPER_CONNECT_RETRY_TIMES = "retry.zookeeper.connect.retry.times";
	// zk ���Լ��
	String ZOOKEEPER_CONNECT_RETRY_INTERVAL_TIME = "retry.zookeeper.connect.retry.interval.time";
	// zk ִ�г�ʱʱ��
	String ZOOKEEPER_EXECUTE_CONNECT_TIMEOUT = "retry.zookeeper.execute.connect.timeout";
	// zk ���ڵ�����
	String ZOOKEEPER_DEFAULT_PARENT = "retry.zookeeper.default.parent";

}
