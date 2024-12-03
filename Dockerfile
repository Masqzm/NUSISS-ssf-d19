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
COPY --from=builder /compileDir/target/day18-0.0.1-SNAPSHOT.jar day18.jar

ENV SERVER_PORT=1234

EXPOSE ${SERVER_PORT}

ENTRYPOINT java -jar day18.jar

HEALTHCHECK --interval=30s --timeout=30s --retries=3 \
CMD curl -s -f http://localhost:${SERVER_PORT}/healthy || exit 1