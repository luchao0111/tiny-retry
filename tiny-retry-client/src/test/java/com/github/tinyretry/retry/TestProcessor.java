package com.github.tinyretry.retry;

import java.io.Serializable;

import com.github.tinyretry.retry.domain.ProcessContext;
import com.github.tinyretry.retry.domain.ProcessResult;
import com.github.tinyretry.retry.domain.Processor;
import com.github.tinyretry.retry.domain.Task;

public class TestProcessor extends Task implements Processor {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public TestProcessor(){
        this.setProcessor(this);
    }

    @Override
    public ProcessResult execute(ProcessContext context) {
        // ͨ��context��ȡdata. data����retryPorcessClient.async()�����ĵڶ�������
        Serializable data = context.getData();

        System.out.println("receive data: ---> " + data);

        return ProcessResult.SUCCESS;
    }

}
