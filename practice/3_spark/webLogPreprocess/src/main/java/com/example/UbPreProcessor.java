package com.example;


import com.example.entity.WebLog;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

public class UbPreProcessor {
    public static void main(String[] args) {


        SessionManger sessionManger = new SessionManger();
        try (SparkSession sparkSession = sessionManger.getSpark()) {

            sparkSession.conf().set("spark.neo4j.bolt.url", "bolt://localhost:7687");
            sparkSession.conf().set("spark.neo4j.bolt.username", "neo4j");
            sparkSession.conf().set("spark.neo4j.bolt.password", "password");

            KafkaConsumer kafkaConsumer = new KafkaConsumer(sparkSession);
            Dataset<String> originalLogData = kafkaConsumer.readFromKafka("ublog");

            // 데이터 처리
            WebLogProcessor webLogProcessor = new WebLogProcessor();

            //의문점: kafka처럼 serializer는 지원을 안해주는 지?
            Dataset<WebLog> webLogData = webLogProcessor.mapToWeblog(originalLogData);
            //세션 나눔.
            Dataset<Row> sessionizedLog = webLogProcessor.assignSession(webLogData);
            Dataset<Row> groupingLog = webLogProcessor.analyzeUserJourney(sessionizedLog);
            Dataset<Row> transformedLog = webLogProcessor.transformUrlData(groupingLog);


            transformedLog.write().option("header", "true")
                    .mode("overwrite")
                    .csv("sampleData");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}