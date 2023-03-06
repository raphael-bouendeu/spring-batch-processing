package com.itbcafrica.springbatch.service;


import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.HashMap;
import java.util.Map;

//@Service
public class SecondJobScheduler {

    @Qualifier("secondJob")
    @Autowired
    private Job secondJob;

    @Autowired
    private JobLauncher jobLauncher;

    //@Scheduled(cron = "0 0/1 * 1/1 * ?")
    public void secondJobStarter() throws Exception {
        Map<String, JobParameter> params = new HashMap<> ();
        params.put ( "currentTime", new JobParameter ( System.currentTimeMillis () ) );
        JobExecution jobExecution = null;
        JobParameters jobParameters = new JobParameters ( params );

        jobExecution = jobLauncher.run ( secondJob, jobParameters );

        System.out.println ( "JobExecution ID  =" + jobExecution.getId () );
    }
}
