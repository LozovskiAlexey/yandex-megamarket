FROM adoptopenjdk/openjdk11:alpine-slim
MAINTAINER alozovskiy
COPY target/mega-market-0.0.1-SNAPSHOT.jar mega-market.jar
ENTRYPOINT ["java","-jar", "-Duser.timezone=UTC","/mega-market.jar"]
