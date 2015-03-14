package com.github.tinyretry.retry.client;

import java.io.Serializable;

import com.github.commons.model.Observer;
import com.github.tinyretry.retry.AsyncProcessor;
import com.github.tinyretry.retry.domain.ProcessResult;
import com.github.tinyretry.retry.domain.Task;
import com.github.tinyretry.retry.exception.RetryRemoteException;
import com.github.tinyretry.timer.McJobScheduleException;

/**
 * <pre>
 * desc: ���Դ���ͻ���
 * 
 * ʹ�÷�����
 * 
 * 1. ����{@linkplain com.github.tinyretry.retry.domain.Task Task}ʵ��testTask��
 * 2. ����retryPorcessClient.async(testTask, "asdf"),�ڶ�������������ִ����Ҫ�õ�������
 * 
 * 
 * 
 * created: 2012-9-3 ����11:58:13
 * author: xiaofeng.zhouxf
 * todo: 
 * history:
 * </pre>
 */
public class RetryPorcessClient {

    private AsyncProcessor asyncProcessor;

    /**
     * <pre>
     * ���Ƽ����첽ִ���������� 
     *  ���ڱ���ע����������,ʧ�ܺ�Żᱶ���䵽��Ⱥ��һ̨������ִ��
     * </pre>
     * 
     * @param task �첽���������
     * @param data ���������
     * @throws McJobScheduleException
     * @throws RetryRemoteException
     */
    public void async(Task task, Serializable data) throws McJobScheduleException, RetryRemoteException {
        asyncProcessor.execute(task, data);
    }

    /**
     * <pre>
     * ���Ƽ����첽ִ���������� 
     *  ���ڱ���ע����������,ʧ�ܺ�Żᱶ���䵽��Ⱥ��һ̨������ִ��
     * </pre>
     * 
     * @param task �첽���������
     * @param data ���������
     * @param callbacks ִ�н���ʱ�ص�,��ֻ�����״�ִ��,���������ʱҲ��Ҫ��,������{@code ReTryDispatcher}
     * @throws McJobScheduleException
     * @throws RetryRemoteException
     */
    public void async(Task task, Serializable data, Observer<ProcessResult>... callbacks)
                                                                                         throws McJobScheduleException,
                                                                                         RetryRemoteException {
        asyncProcessor.execute(task, data, callbacks);
    }

    /**
     * <pre>
     * �첽Զ��ִ���������� 
     * ���񲻻��ڱ�������ִ�У���Ҫ������Զ��ȡ�������ִ�� ������ݿ����һ��ѹ�������ã�����
     * </pre>
     * 
     * @param task �첽���������
     * @param data ���������
     * @throws McJobScheduleException
     * @throws RetryRemoteException
     */
    public void asyncRemote(Task task, Serializable data) throws McJobScheduleException, RetryRemoteException {
        asyncProcessor.execWithOutRegisterJob(task, data);
    }

    public void setAsyncProcessor(AsyncProcessor asyncProcessor) {
        this.asyncProcessor = asyncProcessor;
    }

}
