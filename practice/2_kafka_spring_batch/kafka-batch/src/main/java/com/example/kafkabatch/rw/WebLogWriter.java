package com.example.kafkabatch.rw;

import com.example.kafkabatch.entity.WebLog;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.kafka.core.KafkaTemplate;

public class WebLogWriter implements ItemWriter<WebLog> {

    private final KafkaTemplate<String, WebLog> kafkaTemplate;
    private final String KAFKA_TOPIC = "web_log_topic";

    public WebLogWriter(KafkaTemplate<String, WebLog> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    //kafka에서 데이터 씀
    //BatchConfig에 10개로 설정되어 있기 때문에 10개씩 읽음.
    @Override
    public void write(Chunk<? extends WebLog> chunk) throws Exception {
        for (WebLog webLog : chunk) {
            kafkaTemplate.send(KAFKA_TOPIC, webLog);
        }
    }
}
