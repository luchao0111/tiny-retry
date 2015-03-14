package com.github.tinyretry.timer.event;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.github.tinyretry.timer.DefaultMcJobRepository;
import com.github.tinyretry.timer.domain.McJob;
import com.github.tinyretry.timer.domain.McJobRepository;

/**
 * <pre>
 * desc: ��Ϣ���͹�����
 * created: 2012-5-7 ����02:24:31
 * author: xiaofeng.zhouxf
 * todo: 
 * history:
 * </pre>
 */
public class McJobNotifySupport {

	public void sendNotify(McJobNotify notify) {
		notifyDispatcher(notify);
	}

	/**
	 * �ַ���Ϣ
	 * 
	 * @param notify
	 */
	private void notifyDispatcher(McJobNotify notify) {
		McJobRepository repository = DefaultMcJobRepository.getInstance();
		String groupId = notify.getGroupId();
		if (StringUtils.isNotBlank(groupId)) {

			Map<String, McJob> jobs = repository.getJobs();

			if (jobs == null || jobs.size() == 0) {
				return;
			}

			Collection<McJob> values = jobs.values();
			if (values == null || values.size() == 0) {
				return;
			}
			Iterator<McJob> iterator = values.iterator();

			while (iterator.hasNext()) {
				McJob job = iterator.next();
				if (job instanceof McJobNotifyListener && StringUtils.equals(job.getDefinition().getGroupId(), groupId)) {
					if (!StringUtils.equalsIgnoreCase(notify.getFromJobName(), job.getDefinition().getName())) {
						((McJobNotifyListener) job).onFile(notify);
					}
				}
			}
		}
	}
}
