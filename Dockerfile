FROM maven:3.6.0-jdk-8-slim

EXPOSE 8080

VOLUME /root/.m2

WORKDIR /usr/animallogic/mctt-framework

COPY . /usr/animallogic/mctt-framework/

CMD ["mvn", "spring-boot:run"]