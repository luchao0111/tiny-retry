package com.github.tinyretry.retry.scanner;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.SerializationUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.commons.model.Observer;
import com.github.tinyretry.retry.constants.TaskStatus;
import com.github.tinyretry.retry.domain.McTaskDO;
import com.github.tinyretry.retry.domain.ProcessResult;
import com.github.tinyretry.retry.domain.Task;
import com.github.tinyretry.retry.domain.TaskQuery;
import com.github.tinyretry.retry.domain.TaskUpdate;
import com.github.tinyretry.retry.tools.RetryJobTools;

/**
 * <pre>
 * desc: ��������
 * <lu>
 *   <li>һ��task��Ӧһ��ReTryjob</li>
 *   <li>��job������ʱ�����ڻ��ߵ��ڵ�ǰʱ�������������еȴ�����ִ��</li>
 *   <li>��job������ʱ�����ڵ�ǰʱ�������������еȴ��ӳ�ִ��</li>
 * </lu>
 * created: 2012-8-6 ����02:43:50
 * author: xiaofeng.zhouxf
 * todo: 
 * history:
 * </pre>
 */
public class ReTryDispatcher extends RetryScanner {
	private static final Logger logger = LoggerFactory
			.getLogger(ReTryDispatcher.class);

	/**
	 * ���������Ӧ�Ļص����� keyΪtaskname,valueΪ�ص�������
	 */
	private Map<String, Observer<ProcessResult>[]> observerMap;

	/**
	 * ����ִ��
	 */
	@SuppressWarnings("unchecked")
	public boolean execute() throws Exception {
		// �ѻ�ȡ�����������,������������
		for (int i = 0; i < 2; i++) {
			// �����Ϣһ��,�Ա������
			Thread.sleep(RandomUtils.nextInt(3000));
			// 1. Get tasks by appCode, order by priority
			List<McTaskDO> dbTasks = retryTaskService
					.queryMcTaskList(generateQuery());
			int dealCount = 0;
			if (dbTasks != null && dbTasks.size() > 0) {
				// 2. register the task order by priority
				for (McTaskDO task : dbTasks) {
					if (task != null
							&& server.getTasks().containsKey(task.getType())) {
						// 2.1 zk locked
						// DistributedLock lock = new DefaultDistributedLock(
						// RetryJobTools.generateTaskZKNodeName(task.getId(),
						// task.getAppCode() != null ? task.getAppCode() :
						// Constants.EMPTY));

						TaskUpdate update = new TaskUpdate();
						// prevent copy properties exception.
						update.setId(task.getId());
						try {
							// if (lock.tryLock()) {
							// 2.2 update task status
							task.setStatus(TaskStatus.DEALING.getStatus());
							task.setVersion((short) (1 + task.getVersion()
									.shortValue()));
							BeanUtils.copyProperties(update, task);
							update.setOldStatus(TaskStatus.WAITING.getStatus());
							Integer updateMcTask = retryTaskService
									.updateMcTaskStatus(update);

							if (updateMcTask > 0) {
							    
							    //�ӻ����л�ȡ����ʵ��
								Task cacheTask = server.getTasks().get(
										task.getType());
								if (cacheTask != null) {
									Task tempTask = new Task(cacheTask);
									tempTask.setTaskId(task.getId());
									tempTask.setCurrentTime(task.getVersion());
									tempTask.setNextTime(task.getNextTime());
									// 2.3 process the task

									if (observerMap != null && observerMap.containsKey(tempTask.getTaskName())) {
										asyncProcessor.execute(tempTask,
												deserializeData(task.getContext()),
												observerMap.get(tempTask.getTaskName()));
									} else {
										asyncProcessor.execute(tempTask,
												deserializeData(task.getContext()));
									}
									dealCount++;
								}
							} else {
								logger.info("Update task failed, task dealed by other thread.");
							}
							// }
						} catch (Exception e) {
							logger.error("Lock.tryLock or deal exception.", e);

							// if async process exceptin, should set failed to
							// the
							// status of task.
							task.setStatus(TaskStatus.FAIL.getStatus());
							update.setOldStatus(TaskStatus.DEALING.getStatus());
                            retryTaskService.updateMcTaskStatus(update);
						}
						// finally {
						// lock.unlock();
						// }
					}
				}
			} else {
				break;
			}

			logger.info("ReTryDispatcher running: {} ,dealed {}",
					dbTasks != null ? String.valueOf(dbTasks.size()) : "0",
					dealCount);
		}

		return true;
	}

	private Serializable deserializeData(byte[] data) {
		if (data != null && data.length > 0) {
			Object deserialize = SerializationUtils.deserialize(data);
			if (deserialize instanceof Serializable) {
				return (Serializable) deserialize;
			}
		}
		return null;
	}

	private TaskQuery generateQuery() {
		TaskQuery taskQuery = new TaskQuery();
		taskQuery.setAppCode(appCode);
		taskQuery.setStatus(TaskStatus.WAITING.getStatus());
		taskQuery.setOffset(0);
		taskQuery.setLength(50);
		taskQuery
				.setEndNextTime(RetryJobTools.getAfterNextTime(repeatInterval));
		return taskQuery;
	}

	public void setObserverMap(Map<String, Observer<ProcessResult>[]> observerMap) {
		this.observerMap = observerMap;
	}

}
