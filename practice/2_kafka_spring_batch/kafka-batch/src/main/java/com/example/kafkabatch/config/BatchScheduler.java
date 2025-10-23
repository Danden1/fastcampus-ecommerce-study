package com.example.kafkabatch.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@EnableScheduling
public class BatchScheduler {

    private final JobLauncher jobLauncher;

    private final Job webLogGenAndProduceJob;

    public BatchScheduler(JobLauncher jobLauncher, Job webLogGenAndProduceJob) {
        this.jobLauncher = jobLauncher;
        this.webLogGenAndProduceJob = webLogGenAndProduceJob;
    }

    //간단한 작업은 서비스 만들어서 호출하기도 함. 직장에서도 이런 비슷한 방식으로 하고 있음.
    @Scheduled(fixedDelay = 10_000)
    public void runBatchJob() throws JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException {
        jobLauncher.run(webLogGenAndProduceJob, new JobParametersBuilder().addLong("time", System.currentTimeMillis()).toJobParameters());
    }
}
