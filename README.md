# fastcampus-ecommerce-study
국내 1티어급 이커머스 플랫폼으로 배우는 대용량 데이터 처리 끝판왕 공부 자료


## 공부 이유

1. 다른 도메인을 체험해보고 싶음
   1. 이러한 요구 사항을 만족하기 위해 어떤 기술스택을 사용하는 지 등
2. 현재 회사 특서상 데이터를 많이 다룸. 이 데이터를 효율적으로 처리하는 방법에 대해 공부해보고 실제로 적용해보고 싶음.

## 환경 설정

### docker

#### mysql

docker desktop 이용해서 설치


#### elasticsearch + kibana

```
docker run --name es01-test -d --net elastic \
  -p 9200:9200 -p 9300:9300 \
  -e "discovery.type=single-node" \
  elasticsearch:7.17.26
```


```
docker run --name kib01-test --net elastic -d \
  -p 5601:5601 \
  -e "ELASTICSEARCH_HOSTS=http://es01-test:9200" \
  kibana:7.17.26
```


## 공부 내용

### 실시간 처리와 배치 처리

[kafka + flink 정리](Kafka_Flink.md)