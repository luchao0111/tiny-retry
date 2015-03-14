package com.github.tinyretry.timer.domain;

import java.util.Map;

import com.github.tinyretry.timer.ext.repository.SimpleMcJobRepository;

/**
 * <pre>
 * desc: ������Դ�⣬�����洢��ʱ�����б����Ӧ�ķ����б�
 * 
 * ���ݽṹ���£�
 * 
 * McJobRepository{
 *    [groupId:McJobGroup{ 
 *       [jobName:McJob,....]
 *     },...
 *    ]
 * }  
 * 
 * created: 2012-5-7 ����02:10:58
 * author: xiaofeng.zhouxf
 * todo: 
 * history:
 * </pre>
 * 
 * @see SpringMcJobRepository
 * @see SimpleMcJobRepository
 */
public interface McJobRepository {

	/**
	 * ��Ӷ�ʱ��������ֿ�
	 * 
	 * @param groupId
	 *            �������ڵ���ID
	 * @param job
	 *            ����
	 * @return ��ӳɹ�����true,���򷵻�false
	 */
	boolean addJob(String groupId, McJob job);

	/**
	 * ������ֿ�ɾ����ʱ����
	 * 
	 * @param groupId
	 *            �������ڵ���ID
	 * @param job
	 *            ����
	 * @return ɾ���ɹ�����true,���򷵻�false
	 */
	boolean removeJob(String groupId, McJob job);

	/**
	 * boolean removeJob(String groupId, McJob job);
	 * 
	 * @param groupId
	 *            �������ڵ���ID
	 * @param jobName
	 *            job������
	 * @return
	 */
	boolean removeJob(String groupId, String jobName);

	/**
	 * ������ID��ȡ������
	 * 
	 * @param groupId
	 * @return ���ط���
	 */
	McJobGroup getGroups(String groupId);

	/**
	 * ��ȡ���е�������
	 * 
	 * @return Map,keyΪ����ID��valueΪ�������
	 */
	Map<String, McJobGroup> getGroups();

	/**
	 * ��ӷ��鵽��Դ���У�
	 * 
	 * @param groups
	 */
	void addGroups(McJobGroup groups);

	/**
	 * ���ݷ���IDɾ��������
	 * 
	 * @param groupId
	 */
	void removeGroups(String groupId);

	/**
	 * ����job���ƻ�ȡ����
	 * 
	 * @param name
	 * @return
	 */
	McJob getJob(String name);

	/**
	 * ����job�����ж������Ƿ����
	 * 
	 * @param name
	 *            job����
	 * @return job���ڷ���true,����false
	 */
	boolean jobExists(String name);

	/**
	 * ���ݷ���ID�жϷ����Ƿ����
	 * 
	 * @param groupId
	 *            ����ID
	 * @return group���ڷ���true,����false
	 */
	boolean groupExists(String groupId);

	/**
	 * ��ȡ��ǰ����job�б�
	 * 
	 * @return
	 */
	Map<String, McJob> getJobs();
}
