# NoSQL

## Elasticsearch

분상형 검색 및 분석 엔진.

대규모 데이터를 빠르게 저장하고 검색할 수 있는 솔루션.

1. 로그 저장소 (현재 회사에서 이용 중)
2. text 검색 엔진
3. vector db
   

트랜잭션 관리, join은 힘듦.

### 주요 entity

- Lucene: Apache에서 개발한 고성능, 풀 텍스트 검색 라이브러리. java로 구현되어 있음.
- Index: RDB의 table 느낌. 1개는 여러 개의 shard로 구성됨.
- shard: index에 저장되는 데이터는 여러 개의 shard로 나뉘어 저장, 검색됨. primary, replica 샤드 존재. 1개는 보통 20 ~ 50GB 정도로 관리된느 것이 좋음. 그 보다 작으면 overhead 발생. 크면 처리 속도 저하.
- Document: RDB row 느낌. json 형태, document의 id 별도로 존재.
- segment: 실제로 저장되는 단위. 메모리에 위치한 translog가 disk에 commit 되는 개념.



### vector db

고차원 벡터를효율적으로 저장, 검색 및 분석하는 DB.

일반적으로 이미지, 오디어, 텍스트 등 데이터를 임베딩 벡터로 변환하고 이를 기반으로 빠르게 찾음

-> 임베딩은 AI를 이용하고 유사도는 cosine similarity 를 이용하나? (CV 공부할 때는 그랬음.) 그렇다면 모델이 바뀌면 임베딩 관련해서 동작 잘 안할 것 같은데... 내가 아는 지식이 맞는 지조사 필요.

-> 강의 뒷부분에 나옴. 임베딩은 따로 공식이 있고, 유사도는 코사인, 유클리드 거리, product dot 등 많다고 함.