package com.example.kafkabatch.entity;

import lombok.Data;

/**
 *  WebLog entity class representing a web log entry.
 *
 */
//실제로 weblog라고 검색하면, 이러한 정보가 나옴, 사용자의 요청 정보에 대한 내용이 있음.
@Data
public class WebLog {
    private String userId;
    private String url;
    private String ipAddress;
    private String timestamp;
    private String sessionId;
}
