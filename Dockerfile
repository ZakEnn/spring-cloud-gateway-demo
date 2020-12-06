FROM openjdk:8-jdk-alpine
LABEL maintainer="ennaje.zakari@gmail.com"

VOLUME /tmp

ADD target/*.jar /app.jar

RUN sh -c 'touch /app.jar'
ENV JAVA_OPTS="-XX:+UnlockExperimentalVMOptions -XX:+UseCGroupMemoryLimitForHeap -XX:MaxRAMFraction=1 -XshowSettings:vm -Xms16m -Xmx256m"

ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar /app.jar"  ]

EXPOSE 8090