package com.example;

import com.example.entity.WebLog;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.spark.api.java.function.MapFunction;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Encoders;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.expressions.Window;
import org.apache.spark.sql.expressions.WindowSpec;

import static org.apache.spark.sql.functions.*;

public class WebLogProcessor {

    private final ObjectMapper objectMapper = new ObjectMapper();

    public Dataset<WebLog> mapToWeblog(Dataset<String> originalDataset) {
        return originalDataset.map((MapFunction<String, WebLog>) record -> objectMapper.readValue(record, WebLog.class)
                , Encoders.bean(WebLog.class)
        );
    }

    public Dataset<Row> assignSession(Dataset<WebLog> ublog) {
        WindowSpec windowSpec = Window.partitionBy("userId").orderBy("timestamp");

        Dataset<Row> logWithSession = ublog.withColumn("prev.ts", lag("timestamp", 1).over(windowSpec))
                .withColumn("session_flag",
                        when(col("prev_ts").isNull().or(expr("unix_timestamp(timestamp) - unix.timestamp(prev_ts) > 1800")), 1)
                        .otherwise(0));
                ;

        return logWithSession.withColumn("session_id", sum("session_flag").over(windowSpec));
    }

    public Dataset<Row> analyzeUserJourney(Dataset<Row> sessionizedLog) {
        //user id, session id로 묶음.
        return sessionizedLog.groupBy("user_id", "session_id")
                //url path라는 이름으로 url 모음
                .agg(collect_list("url").alias("url_path"));
    }

    public Dataset<Row> transformUrlData(Dataset<Row> groupingLog) {
        return groupingLog.withColumn("url_path", concat_ws(",", col("url_path")));
    }



}
