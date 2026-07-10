FROM maven:3.9-eclipse-temurin-17 AS build

WORKDIR /workspace

COPY pom.xml ./
COPY core/pom.xml core/pom.xml
COPY usecases/pom.xml usecases/pom.xml
COPY adapters/pom.xml adapters/pom.xml
COPY app/pom.xml app/pom.xml

RUN mvn -B -q dependency:go-offline || true

COPY core/src core/src
COPY usecases/src usecases/src
COPY adapters/src adapters/src
COPY app/src app/src

RUN mvn -B -DskipTests clean package


FROM eclipse-temurin:17-jre AS runtime

RUN apt-get update \
    && apt-get install -y --no-install-recommends curl \
    && rm -rf /var/lib/apt/lists/* \
    && groupadd --system voting \
    && useradd --system --gid voting --home-dir /app --no-create-home voting

WORKDIR /app

COPY --from=build /workspace/app/target/voting-system-api.jar app.jar

RUN chown -R voting:voting /app

USER voting

EXPOSE 8080

HEALTHCHECK --interval=30s --timeout=5s --start-period=60s --retries=5 \
    CMD curl -f http://localhost:8080/actuator/health/readiness || exit 1

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
