package com.itbcafrica.springbatch.service;

import com.itbcafrica.springbatch.model.JobParamsRequest;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class JobService {

    @Autowired
    private JobLauncher jobLauncher;

    @Qualifier("firstJob")
    @Autowired
    private Job firstJob;

    @Qualifier("secondJob")
    @Autowired
    private Job secondJob;

    @Async
    public void startJob(String jobName, List<JobParamsRequest> jobParamsRequests) throws Exception {
        Map<String, JobParameter> params = new HashMap<>();

        jobParamsRequests.stream().forEach(jobParamsRequest -> {
            params.put(jobParamsRequest.getParamKey(), new JobParameter(jobParamsRequest.getParamValue()));
        });

        JobExecution jobExecution = null;
        JobParameters jobParameters = new JobParameters(params);
        if (jobName.equals("First Job")) {
            jobExecution = jobLauncher.run(firstJob, jobParameters);
        } else if (jobName.equals("Second Job")) {
            jobExecution = jobLauncher.run(secondJob, jobParameters);
        }
        System.out.println("JobExecution ID " + jobExecution.getId());
    }
}
