package com.itbcafrica.springbatch.controller;

import com.itbcafrica.springbatch.model.JobParamsRequest;
import com.itbcafrica.springbatch.service.JobService;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/job")
public class JobController {

    @Autowired
    private JobService jobService;

    @Autowired
    private JobOperator jobOperator;

    @GetMapping("/start/{jobName}")
    public String startJob(@PathVariable String jobName,
                           @RequestBody List<JobParamsRequest> jobParamsRequests) throws Exception {

        jobService.startJob(jobName, jobParamsRequests);

        return "Job Started ...";
    }


    @GetMapping("/stop/{executionId}")
    public String stopJob(@PathVariable Long executionId) throws Exception {
        jobOperator.stop(executionId);
        return "Job Stopped ...";
    }
}
