package com.example;

import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Encoders;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

import java.util.Arrays;

public class WordCount {
    public static void main(String[] args) {
        SparkSession spark = SparkSession.builder()
                .appName("Word Count")
                .master("local[*]")
                .getOrCreate();


        Dataset<String> lines = spark.read().textFile("test.txt");

        Dataset<String> words = lines.flatMap(
                (FlatMapFunction<String, String>)  line -> Arrays.asList(line.split(" ")).iterator(),
                Encoders.STRING()
        );

        Dataset<Row> wordCounts = words.groupBy("value")
                .count()
                .withColumnRenamed("count", "wordCount");

        wordCounts.show();

    }
}