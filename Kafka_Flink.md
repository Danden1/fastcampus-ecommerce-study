# Kafka


목적 : Event/Message 전송

장점: 고가용성, 빠른 처리

단점: 순서 보장 어려움, 가볍게 이용하기 어려움.


## RabbitMQ랑 다른 점

- RabbitMQ는 producer가 메시지를 전달할 시기 통제 가능.
- transactional하지만, 처리량이 kafka에 비해 낮음.
- 점도 작은 단위에 어울림. Flexible하고 쉬움.


### 기타
solace: 증권 쪽에서 많이 사용한다고 함.

Redis pub/sub: low latency, instant Msg를 사용하는 데에 강점. (채팅처럼)


## 용어

(용어는 이전에 kafka 공부한 내용 참고.)

[kafka 공부했던 내용](https://github.com/Danden1/Kafka-core-inflearn)


# Spring Batch

반복적이고 대뮤고 작업을 처리함.

ETL, 로그 처리 및 분석에 효율적.

## batch 용어

- job : 배치 작업의 논리적 단위. 여러 개의 step으로 관리됨. 가장 상위 개념
- step : job을 구성하는 기본 실행 단위. 보통 ItemReader, ItemProcessor, ItemWriter로 구성됨.
- ItemReader: 배치 작업에서 데이터를 읽음.
- ItemProcessor: 읽어온 데이터 변환 / 가공하는 역할.
- ItemWriter: DB에 데이터를 저장하거나 파일에 쓰는 등의 역할
- JobInstance: 특정 job의 실행을 나타내는 인스턴스.

