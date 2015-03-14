package com.github.tinyretry.timer;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.apache.commons.lang.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.quartz.spi.TriggerFiredBundle;

import com.github.tinyretry.timer.McJobSchedule;
import com.github.tinyretry.timer.McJobScheduleException;
import com.github.tinyretry.timer.McJobScheduleFactory;
import com.github.tinyretry.timer.domain.McJob;
import com.github.tinyretry.timer.domain.McJobGroup;
import com.github.tinyretry.timer.event.McJobNotify;
import com.github.tinyretry.timer.event.McJobNotifyListener;
import com.github.tinyretry.timer.ext.McJobDefinition;
import com.github.tinyretry.timer.ext.McJobListener;
import com.github.tinyretry.timer.ext.jobfactory.McJobFactory;

/**
 * <pre>
 * desc: 
 * created: 2012-7-12 ����02:57:32
 * author: xiaofeng.zhouxf
 * todo: 
 * history:
 * </pre>
 */
public class McJobScheduleTest {

    private McJobGroup          towPerSecond = null;
    private static final String groupId      = "testGroup";
    private static int          beforeCount1 = 0;
    private static int          afterCount1  = 0;
    private static int          beforeCount2 = 0;
    private static int          afterCount2  = 0;
    private static int          notifyCount1 = 0;
    private static int          notifyCount2 = 0;

    @Before
    public void init() {
        // ==========> ��һ�� ����GROUP ================
        towPerSecond = new McJobGroup(); // ����һ����ʱ��
        towPerSecond.setGroupId(groupId); // ������ID
        // towPerSecond.setCornExpression("0 0 */1 * * ?"); //���嶨ʱ�ı����
        towPerSecond.setRepeatInterval(3000); // ��������ִ��ʱ��
        towPerSecond.setRepeatCount(0); // ���Դ�����0Ϊ���޴�
    }

    @Test
    public void testStart() throws InterruptedException, McJobScheduleException {
        // ==========> �ڶ��� ����JOB ================
        TestJob1 job1 = new TestJob1(); // ����һ��JOB
        McJobDefinition definition = new McJobDefinition(); // �������JOB����Ϣ
        definition.setGroupId(groupId); // ���JOB �������飬����Ķ�ʱʱ��ִ��
        definition.setName("job1"); // JOB ����
        definition.setDescription("test1111111111"); // ����

        // ==========> ������ ��������� �ⲽ�ǿ�ѡ�� ================
        List<McJobListener> jobListeners = new ArrayList<McJobListener>(1); // ������
        int afterCount = 0;
        McJobListener listener1 = new McJobListener() {

            @Override
            public void before(McJobDefinition definitionInner) {
                System.out.println("job1 before........" + definitionInner.getName());
                beforeCount1++;
            }

            @Override
            public void after(McJobDefinition definitionInner) {
                System.out.println("job1 after........" + definitionInner.getName());
                afterCount1++;
            }
        };
        listener1.setName("JobListener1");
        jobListeners.add(listener1);
        definition.setJobListeners(jobListeners); // /���ú����JOB�ļ�����
        job1.setDefinition(definition); // ���ú�JOB����

        TestJob2 job2 = new TestJob2();
        McJobDefinition definition2 = new McJobDefinition();
        definition2.setGroupId(groupId);
        definition2.setName("job2");
        definition2.setDescription("test22222222222");

        List<McJobListener> jobListeners2 = new ArrayList<McJobListener>(1);
        McJobListener listener2 = new McJobListener() {

            @Override
            public void before(McJobDefinition definitionInner) {
                System.out.println("job2 before........" + definitionInner.getName());
                beforeCount2++;
            }

            @Override
            public void after(McJobDefinition definitionInner) {
                System.out.println("job2 after........" + definitionInner.getName());
                afterCount2++;
            }
        };
        listener2.setName("JobListener2");
        jobListeners2.add(listener2);
        definition2.setJobListeners(jobListeners2);
        job2.setDefinition(definition2);

        // ==========> ���Ĳ� ��������� ================

        // ����Ϊ����ʾ��Ĭ����ֻ��Ҫ��һ���߳�������
        McJobSchedule js = McJobScheduleFactory.createMcJobSchedule(2, new McJobFactory() {

            @Override
            public McJob createJob(TriggerFiredBundle bundle) {
                // job����
                String name = bundle.getJobDetail().getName();
                if (StringUtils.isNotBlank(name)) {
                    // ����Դ������job
                    McJob job = repository.getJob(name);

                    return job;
                }
                return null;
            }
        });

        // ==========> ���岽 ��GROUP ������������ ================
        List<McJobGroup> groups = new ArrayList<McJobGroup>(1);
        groups.add(towPerSecond);
        js.setGroups(groups);

        // ==========> ������ ��ʼ�������� ================
        js.init();

        // ==========> ���߲� ע��JOB�������� ================
        js.registerJob(job1);
        js.registerJob(job2);

        // ==========> �ڰ˲� ���������� ================
        js.start();

        // =========һ������ǿ����===================
        // �ӵ�ǰ����������ȡ�Ĵ洢�ˣ����ǻ�û�ֱ�ִ�е�JOB
        List<McJobDefinition> currentStoreJob = js.getCurrentStoreJob();
        System.out.println("=========currentStore1===========");
        if (currentStoreJob != null) for (McJobDefinition df : currentStoreJob) {
            System.out.println(df.getGroupId() + "===========" + df.getName());
        }

        Thread.sleep(1000);
        // �ӵ�ǰ����������ȡ�������е�JOB
        List<McJobDefinition> currentRuningJob = js.getCurrentRunningJob();
        System.out.println("=========currentRuning1===========");
        if (currentRuningJob != null) {
            for (McJobDefinition df : currentRuningJob) {
                System.out.println(df.getGroupId() + "===========" + df.getName());
            }
        }

        Thread.sleep(3000l);

        // js.stop();
        System.out.println("stop.....");
        Thread.sleep(10000l);

        js.start();

        System.out.println("unRegisterJob.....");
        js.unRegisterJob(job1);
        js.unRegisterJob(job2);
        Thread.sleep(10000l);

        // js.init();
        // js.registerJob(job1);
        // js.registerJob(job2);

        js.start();

        currentRuningJob = js.getCurrentRunningJob();
        System.out.println("=========currentRuning2===========");
        if (currentRuningJob != null) {
            for (McJobDefinition df : currentRuningJob) {
                System.out.println(df.getGroupId() + "===========" + df.getName());
            }
        }

        currentStoreJob = js.getCurrentStoreJob();
        System.out.println("=========currentStore2===========");
        if (currentStoreJob != null) for (McJobDefinition df : currentStoreJob) {
            System.out.println(df.getGroupId() + "===========" + df.getName());
        }

        Thread.sleep(10000l);
        Assert.assertEquals(1, job1.getJobCount());
        Assert.assertEquals(1, job2.getJobCount());

        Assert.assertEquals(1, beforeCount1);
        Assert.assertEquals(1, afterCount1);
        Assert.assertEquals(1, beforeCount2);
        Assert.assertEquals(1, afterCount2);
        Assert.assertEquals(1, notifyCount1);
        Assert.assertEquals(1, notifyCount2);

        Thread.sleep(300000l);
    }

    // =============================JOB����==============
    // �̳�McJob ������ʵ�ּ����ӿ�McJobNotifyListener
    class TestJob2 extends McJob implements McJobNotifyListener {

        private int jobCount = 0;

        // ����GROUP�ڣ�JOB����ڲ���Ϣ
        public boolean onFile(McJobNotify notify) {
            System.out.println("notify2: " + notify.getGroupId() + " ::" + notify.getSource());
            notifyCount1++;
            return false;
        }

        public boolean execute() throws Exception {
            System.out.println("TestJob2.........");
            jobCount++;
            McJobNotify notify = new McJobNotify(TestJob2.class.getName());
            notify.setFromJobName(this.getDefinition().getName());
            notify.setGroupId(this.getDefinition().getGroupId());

            // ����GROUP �� JOB����ڲ���Ϣ
            this.sendNotify(notify);
            Thread.sleep(5000);
            return true;
        }

        public int getJobCount() {
            return jobCount;
        }

    }

    class TestJob1 extends McJob implements McJobNotifyListener {

        private int jobCount = 0;

        public boolean onFile(McJobNotify notify) {
            System.out.println("notify1: " + notify.getGroupId() + " ::" + notify.getSource());
            notifyCount2++;
            return false;
        }

        public boolean execute() throws Exception {
            System.out.println("TestJob1...........");
            jobCount++;
            McJobNotify notify = new McJobNotify(TestJob2.class.getName());
            notify.setFromJobName(this.getDefinition().getName());
            notify.setGroupId(this.getDefinition().getGroupId());
            this.sendNotify(notify);
            Thread.sleep(5000);
            return true;
        }

        public int getJobCount() {
            return jobCount;
        }
    }
}
