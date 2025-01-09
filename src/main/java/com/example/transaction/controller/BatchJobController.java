package com.example.transaction.controller;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BatchJobController {

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private Job importTransactionsJob;

    @PostMapping("/run-batch-job")
    public String runBatchJob() {
        try {
            jobLauncher.run(importTransactionsJob, new JobParameters());
            return "Batch Job Started Successfully!";
        } catch (Exception e) {
            e.printStackTrace();
            return "Failed to start Batch Job: " + e.getMessage();
        }
    } //no need? use command line <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< TODO
}