package com.example;

import lombok.Getter;
import org.apache.spark.sql.SparkSession;


public class SessionManger {

    @Getter
    private final SparkSession spark;

    public SessionManger() {
        this.spark = SparkSession.builder()
                .master("local[*]")
                .appName("ub_preprocessor")
                .getOrCreate();
    }


}
