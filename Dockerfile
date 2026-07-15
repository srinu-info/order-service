# FROM maven
# WORKDIR /opt/server
# EXPOSE 8084
# COPY pom.xml .
# COPY src /opt/server/src
# RUN mvn clean package
# RUN mv /opt/server/target/order-service-0.0.1-SNAPSHOT.jar order.jar
# CMD ["java","-jar","order.jar"]

FROM maven:3.9-eclipse-temurin-17  AS builder
WORKDIR /opt/server
COPY pom.xml .
COPY src /opt/server/src
RUN mvn clean package

FROM eclipse-temurin:17-jre-alpine
WORKDIR /opt/server
EXPOSE 8085
COPY --from=builder /opt/server/target/order-service-0.0.1-SNAPSHOT.jar order.jar
CMD ["java","-jar","order.jar"]