### Multi-stage dockerisation (Stage 1)
# Install JDK
FROM eclipse-temurin:23-jdk AS builder

LABEL maintainer="hazim"

# BUILD APP
#-----------
# Create dir /compileDir & change current dir into /app
WORKDIR /compileDir

# Copy files/dir over (COPY [src] [dst/dir name])
COPY mvnw .
COPY .mvn .mvn

COPY pom.xml .
COPY src src    

# Build jar app
#For Railway
RUN chmod a+x ./mvnw && ./mvnw package -Dmaven.test.skip=true

# No need for entrypoint in stage 1 as it will run in stage 2
# ENTRYPOINT java -jar target/day18-0.0.1-SNAPSHOT.jar


### Multi-stage dockerisation (Stage 2)
FROM eclipse-temurin:23-jdk

# Create dir /app & change current dir into /app
WORKDIR /app

# Copy compiled jar from first container (builder) and rename to preferred name
COPY --from=builder /compileDir/target/day19-0.0.1-SNAPSHOT.jar day19.jar

ENV SERVER_PORT=3000
ENV SPRING_DATA_REDIS_HOST=localhost
ENV SPRING_DATA_REDIS_PORT=6379
ENV SPRING_DATA_REDIS_DATABASE=0
ENV SPRING_DATA_REDIS_USERNAME=""
ENV SPRING_DATA_REDIS_PASSWORD=""

EXPOSE ${SERVER_PORT}

ENTRYPOINT java -jar day19.jar