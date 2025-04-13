FROM maven:3.9.4-eclipse-temurin-17 as builder

WORKDIR /app

# Copy project files
COPY pom.xml .
COPY src ./src

# Build the app (skip tests for speed)
RUN mvn clean package -DskipTests

# ----------- STAGE 2: Run the built JAR ----------- #
FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

# Copy the JAR file from the builder stage
COPY --from=builder /app/target/*.jar app.jar

COPY src/main/resources/ssl/tidb-truststore.jks tidb-truststore.jks

EXPOSE 8082

ENTRYPOINT ["java", "-Djavax.net.ssl.trustStore=tidb-truststore.jks", "-Djavax.net.ssl.trustStorePassword=changeit", "-jar", "app.jar"]