# fastcampus-ecommerce-study
국내 1티어급 이커머스 플랫폼으로 배우는 대용량 데이터 처리 끝판왕 공부 자료


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
