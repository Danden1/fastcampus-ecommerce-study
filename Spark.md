# Spark

spring batch 보다 좀 더 대용량 데이터를 배치 처리 할 수 있음?

이커머스에서 왜 배치 처리가 필요한지?
- 주요 KPI나 지표를 도출할 필요 있음.

### 배치 처리 주요 특징

1. 일괄 처리 : 일정량이 쌓일 때까지 기다렸다가 한 번에 처리.
2. 정기적 실행
3. 비실시간성
4. 자원 효율성 : 보통 사용량이 적은 시간대에 실행됨.
5. 신뢰성: 트랜잭션 관리, 오류 처리, 작업 재시작 등의 기능 필요

## Spring Batch

- 간편한 배치 작업 설정 가능.
- Spring 과 통합 쉬움.
- 작업 재시작, 오류 처리, 트랜잭션, 청크 관리 등 다양한 기능 제공
  - 실제 사내 서비스에도 이를 적용하고 싶음...
- 유연한 데이터 소스 지원함(db, kafka 등)

## Spark

- 클러스터 환경에서 데이터 분산 처리 가능.
- 메모리 기반이라 빠름.
- 클러스터 노드 추가하여 쉽게 확장 가능. -> 데이터 분석 등에 많이 씀.

### Clsuter

1. **Driver Program**: 애플리케이션의 진입점. 사용자 코드 실행되는 곳.
2. **cluster Manager**: 클스터 리소스 관리하는 역할.
3. **Worker Nodes**: 실질적인 데이터 처리를 수행하는 노드. (k8s 같음.)
4. **Executors**: worker 노드 내에서 실행되는 프로세스. 실제 작업 수행, 데이터 저장 및 작업 결과를 드라이버로 전송. 애플리케이션 실행되는 동안 할당된 리소스 계쏙 유지
5. **Tasks** : 하나의 job을 여러 개의 task로 분할하여 병렬 처리.


- Spark Contentx: driver 가 클러스터와 통신하는 인터페이스. 작업 요청 / 리소스 할당 관리함. Driverㅈ가 작성한 job을 Clsuter Manager에 제출 및 작업 분배
- DAG Scheduler: DAG 생성 및 작업을 여러 단계로 분할.
- Task Scheduler: DAG 스케줄러가 생상헌 스테이즈를 작은 task 단위로 나움. cluster manager를 통해 각 task를 적절한 Executor에 분배.

#### Job 실행과정
1. Driver에서 코드 실행 -> SparkContext가 Cluster Manager에 작업 요청 보냄.
2. Cluster Manager는 Worker 노드에 Executor를 할당하여 Task 실행
3. DAG Scheduler가 작업을 DAG로 나누고, Task Scheduelr가 이를 Task로 분할하여 실행.
4. Executoers는 할당된 Task 실행 -> 결과 Driver에 반환.

#### 중요개념
- RDD : 데이터 집함. 불변임. Executoers에 한 번만 전송되고, RDD를 메모리에 캐시함. 또한 lazy로 동작함.
- DAG: Spark의 논리적 표현. 작업을 효율적으로 스케줄링하고 장애로부터 복구 가능.



DAG 생성 -> Job 생성 -> Stage 나누기 -> Task 생성 및 실행