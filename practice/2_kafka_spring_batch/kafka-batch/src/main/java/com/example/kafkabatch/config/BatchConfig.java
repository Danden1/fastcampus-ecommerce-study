package com.example.kafkabatch.config;

import com.example.kafkabatch.entity.WebLog;
import com.example.kafkabatch.rw.WebLogReader;
import com.example.kafkabatch.rw.WebLogWriter;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@EnableBatchProcessing
public class BatchConfig {

    private final JobRepository jobRepository;

    private final PlatformTransactionManager transactionManager;

    private final KafkaTemplate<String, WebLog> kafkaTemplate;

//    @Autowired
    public BatchConfig(JobRepository jobRepository, PlatformTransactionManager transactionManager, KafkaTemplate<String, WebLog> kafkaTemplate ) {
        this.jobRepository = jobRepository;
        this.transactionManager = transactionManager;
        this.kafkaTemplate = kafkaTemplate;
    }

    @Bean
    public Job webLogGenAndProduceJob() {
        return new JobBuilder("webLogGenAndProduceJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(WebLogGenAndProduceStep())
                .build();

    }


    @Bean
    public Step WebLogGenAndProduceStep() {
        return new StepBuilder("weblogGenAndProduceStep", jobRepository)
                .<WebLog, WebLog>chunk(10, transactionManager)
                .reader(webLogReader())
                .writer(webLogWriter())
                .build();
    }

    @Bean
    @StepScope
    public WebLogWriter webLogWriter() {
        return new WebLogWriter(kafkaTemplate);
    }

    @Bean
    @StepScope
    public WebLogReader webLogReader() {
        return new WebLogReader();
    }

}
