
docker run -d --name broker -p 9092:9092
docker exec --workdir /opt/kafka/bin/ -it broker sh

./kafka-topics.sh --create --topic my-topic --bootstrap-server localhost:9092

curl "http://localhost:8080/send?message=HelloKafka"


docker ps

docker stop broker

docker rm broker
