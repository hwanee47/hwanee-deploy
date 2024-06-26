FROM gradle:7.3.3-jdk17 as builder

RUN mkdir /app

WORKDIR /app

# RUN apt-get update && apt-get install -y git && rm -rf /var/lib/apt/lists/*

ADD ./build/libs/deploy-app.jar /app/deploy-app.jar

EXPOSE 8081

ENTRYPOINT ["java", "-jar", "deploy-app.jar"]
