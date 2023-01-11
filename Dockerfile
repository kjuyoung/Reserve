FROM openjdk:17

RUN mkdir -p /app/reserve
RUN mkdir /app/reserve/log
RUN mkdir /app/reserve/logs

ARG JAR_FILE=./build/libs/*.jar
COPY ${JAR_FILE} /app/reserve/reserve.jar
ENV PROFILE local
ENV TZ Asiz/Seoul
EXPOSE 8080
ENTRYPOINT ["java","-Dspring.profiles.active=${PROFILE}","-jar","/app/reserve/reserve.jar"]