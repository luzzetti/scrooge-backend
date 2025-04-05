
# Stage 1: Build the application
FROM maven:3.9.5-eclipse-temurin-21-alpine AS build
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline

COPY src ./src
RUN mvn package -DskipTests

# Stage 2: Create the runtime image
FROM eclipse-temurin:21-jre
WORKDIR /app
EXPOSE 8090

# Add a self-signed certificate to the Java keystore
COPY ./certs/scrooge.io.crt /etc/ssl/certs/scrooge.io.crt
RUN keytool -import -trustcacerts -keystore "$JAVA_HOME"/lib/security/cacerts \
    -storepass changeit -noprompt -alias selfsigned-scrooge-cert \
    -file /etc/ssl/certs/scrooge.io.crt

COPY --from=build /app/target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
