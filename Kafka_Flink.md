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

# Flink

실시간 데이터 스리리밍 처리에 최적화된 분산형 데이터 처리 시스템.

배치 처리 -> 대상 데이터가 고정. 전체 데이터 모두 수집 후, 결과 생성.

**스트림 처리** -> 대상 데이터가 비고정. 입력 데이터는 끝나지 않을 수 있기 때문에 도착하는 대로 지속적으로 처리해야 함.


- JobManager : Flink 클러스터의 중앙 제어 역할. job 생명 주기 관리, 작업 스케줄링, 상태를 관리함.
- TaskManager: 실제 데이터 처리 작업을 수행하는 작업자 노드. 여러 개의 작업 슬롯가지고 있고 병렬로 실행.
- State: 스트리밍 처리 중 유지해야 하는 데이터
- Connector: 외부 시스팀과 Flink 간의 데이터 흐름 관리
- Window: 일정 기간 동안의 데이터를 그룹화 하여 처리.

#### 언제 배치 처리, 스트리밍 처리를 할지?

- 결과가 언제 필요한지
  - 이상 탐지처럼 실시간으로 결과가 필요한지 -> 스티리밍
  - 트렌드 분석 등 -> 배치
- 모았다가 처리할지
- 조금 늦어도 되는지?
- 재처리 필요한 지
  - 조금 애매.
  - 스트림은 다시 처리하는 것이 거의 안됨.
- 처리가 복잡할지


## window

- **tumbling window** : 특정 시간 동안
- **sliding window**: 특정 구간 동안. -> 데이터가 중첩이 됨.(5분전 상황처럼, 9시 3분 ~ 9시 8분, 9시 4분 ~ 9시 9분 처럼)
- **Session Window** : 세션 동안. 클릭 또는 광고. 특정 행위가 몇 초 이상 없다면, session 끝났다고 정의됨. session 간의 간격을 session gap이라고 함.
- **Global Window**: 행위 정의 동안. 주문 완료까지의 흐름 분석 등. ux 기반으로 분석한다고 함. (좀 재미있는 듯?)
