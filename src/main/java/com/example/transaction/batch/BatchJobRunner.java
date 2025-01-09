package com.example.transaction.batch;

import jakarta.annotation.PostConstruct;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class BatchJobRunner {

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private Job importTransactionsJob;

    @PostConstruct
    public void runBatchJob() {
        try {
            jobLauncher.run(importTransactionsJob, new JobParameters());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}