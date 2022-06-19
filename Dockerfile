FROM adoptopenjdk/openjdk11:jdk-11.0.5_10-alpine
MAINTAINER alozovskiy
COPY target/mega-market-0.0.1-SNAPSHOT.jar mega-market.jar
ENTRYPOINT ["java","-jar", "-Duser.timezone=UTC","/mega-market.jar"]
