package com.example;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Encoders;
import org.apache.spark.sql.SparkSession;

public class KafkaConsumer {

    private final SparkSession sparkSession;

    public KafkaConsumer(SparkSession sparkSession) {
        this.sparkSession = sparkSession;
    }

    public Dataset<String> readFromKafka(String topic) {
        return sparkSession
                .read()
                .format("kafka")
                .option("bootstrap.servers", "localhost:9092")
                .option("group.id", "db_preprocessor")
                .option("subscribe", topic)
                .load()
                .selectExpr("CAST(value as STRING")
                .as(Encoders.STRING());
    }
}
