package com.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.connector.kafka.sink.KafkaRecordSerializationSchema;
import org.apache.flink.connector.kafka.sink.KafkaSink;
import org.apache.flink.connector.kafka.source.KafkaSource;
import org.apache.flink.connector.kafka.source.enumerator.initializer.OffsetsInitializer;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.assigners.SlidingProcessingTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;

public class WebAbnormalDetector {
    public static void main(String[] args) throws Exception {

        //flink env
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        //kafka connector 이용해서 데이터 가져옴.
        KafkaSource<String> kafkaSource = KafkaSource.<String>builder()
                .setBootstrapServers("localhost:9092")
                .setTopics("weblog_for_ad")
                .setGroupId("abnormal-detector")
                .setValueOnlyDeserializer(new SimpleStringSchema())
                .setStartingOffsets(OffsetsInitializer.earliest())
                .build();

        // String data를 object로 변환.
        DataStream<WebLog> webLogDataStream = env.fromSource(
                kafkaSource,
                //들어온 데이터 시점
                WatermarkStrategy.forMonotonousTimestamps(),
                "kafkaSource"
        ).map(jsonString -> {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(jsonString, WebLog.class);
        });



        //Window 기반 이상 탐지
        DataStream<String> windowAbnormal = webLogDataStream
                //ip address 가져오도록 mapping
                .map(log -> new Tuple2<>(log.getIpAddress(), 1))
                .returns(Types.TUPLE(Types.STRING, Types.INT))
                //ip address 기준
                .keyBy(value -> value.f0)
                //term 10초, window size: 1 min
                //event time이 아닌 들어오는 시점을 기준으로 할 것이므로 ProcessingTimeWindow 사용!
                .window(SlidingProcessingTimeWindows.of(Time.minutes(1), Time.seconds(10)))
                .sum(1)
                .filter(ipCount -> ipCount.f1 > 20)
                .map(ipCount -> "Detected Abnormally Access from Ip : " + ipCount.f0  + ", tries " + ipCount.f1);



        //데이터 처리 후, kafka에 데이터 넣음.
        KafkaSink<String> kafkaSink = KafkaSink.<String>builder()
                .setBootstrapServers("localhost:9092")
                .setRecordSerializer(KafkaRecordSerializationSchema.builder()
                        .setTopic("alert")
                        .setValueSerializationSchema(new SimpleStringSchema())
                        .build())
                .build();

        //kafka에 데이터 전송
        //"Detected Abnormally Access from Ip : " + ipCount.f0  + ", tries " + ipCount.f1 이 메시지가 kafka에 들어가게 됨.
        windowAbnormal.sinkTo(kafkaSink);

        env.execute("Abnormal-Detector");
    }
}