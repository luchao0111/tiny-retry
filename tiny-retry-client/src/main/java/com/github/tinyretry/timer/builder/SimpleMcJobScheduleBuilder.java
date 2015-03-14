package com.github.tinyretry.timer.builder;

import java.util.HashMap;
import java.util.Map;

import com.github.tinyretry.timer.McJobScheduleException;
import com.github.tinyretry.timer.McJobScheduleFactory;
import com.github.tinyretry.timer.domain.McJobGroup;

/**
 * <pre>
 * desc: �����ڴ�job�ֿ�ĵ��ȷ���job��ע���ᱻ�����ڴ�ֿ⣬�Ժ�ִ�ж���Ӳֿ���ȡ�����ظ���������
 * ʹ�÷�����
 * 
 * 1. ����job
 * 
 * ����McJobGroup,����cornExpression����repeatInterval������groupId
 * 
 *  McJobGroup perTowSec = new McJobGroup();
 * 	towPerSecond.setGroupId("testGroup");
 *  //ע�⣬���������0
 * 	towPerSecond.setRepeatInterval(0);
 * 
 * 
 * 
 * �̳�McJob����job��ʵ��execute()����
 * 
 * class TestJob extends McJob implements McJobNotifyListener {
 * 		public boolean onFile(McJobNotify notify) {
 * 			.....
 * 		}
 * 		public void execute() throws Exception {
 * 			.....
 * 		}
 * 	}
 * 
 * 
 *  McJobDefinition definition = new McJobDefinition();
 *  //����ע������ظ�������ͬʱע�ᣬ��Ϊ����������������λ
 *  definition.setName("TestJob");
 *  definition.setGroupId("testGroup");
 *  TestJob testJob= new TestJob()
 * 
 * 2. �������ȷ���������ע��job
 *  
 * McJobScheduleBuilder mjsb = new DisposableMcJobScheduleBuilder();
 * mjsb.createMcJobSchedule("TestSchedule",10).addGroups(perTowSec).executeJob(testJob);
 * 
 * 
 * 
 * created: 2012-9-20 ����01:21:56
 * author: xiaofeng.zhouxf
 * todo: 
 * history:
 * </pre>
 */
public class SimpleMcJobScheduleBuilder extends McJobScheduleBuilder {

    private static final Map<String, McJobGroup> groupCache = new HashMap<String, McJobGroup>();

    /**
     * ������������
     * 
     * @throws McJobScheduleException
     */
    public McJobScheduleBuilder createMcJobSchedule(String scheduleName, int maxThreads) throws McJobScheduleException {
        this.schedule = McJobScheduleFactory.createMcJobSchedule(maxThreads, scheduleName, scheduleName
                                                                                           + INSTANT_SUFFIX);
        return this;
    }

    /**
     * ��ӷ���
     * 
     * @param repeatInterval
     * @param repeatCount
     * @return
     */
    public McJobScheduleBuilder addGroup(int repeatInterval, int repeatCount) {
        String groupId = repeatInterval + "-" + repeatCount;

        McJobGroup tmpGroup = groupCache.get(groupId);
        if (tmpGroup == null) {
            tmpGroup = new McJobGroup(groupId, repeatInterval, repeatCount);
        }

        groupCache.put(groupId, tmpGroup);

        this.addGroups(tmpGroup);

        return this;
    }

}
