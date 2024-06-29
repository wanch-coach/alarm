FROM openjdk:17-jdk

ARG JAR_FILE=build/libs/wanchcoach-0.0.1-SNAPSHOT.jar

ADD ${JAR_FILE} wanchcoach.jar

EXPOSE 8082

# 환경변수 설정
ARG ENCRYPT_KEY
ENV ENCRYPT-KEY $ENCRYPT_KEY

ENTRYPOINT ["java","-Dspring.cloud.config.profile=test","-jar","/wanchcoach.jar"]
