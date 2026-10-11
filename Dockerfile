FROM maven:3.9.9-eclipse-temurin-25 AS build

WORKDIR /workspace
COPY pom.xml .
RUN mvn -B -DskipTests dependency:go-offline
COPY src ./src
RUN mvn -B -DskipTests package

FROM eclipse-temurin:25-jre-noble AS runtime

RUN apt-get update \
    && apt-get install -y --no-install-recommends \
        fontconfig \
        libx11-6 \
        libxext6 \
        libxi6 \
        libxrender1 \
        libxtst6 \
    && rm -rf /var/lib/apt/lists/* \
    && groupadd --system app \
    && useradd --system --gid app --create-home app

WORKDIR /app
COPY --from=build --chown=app:app /workspace/target/smart-campus-eco-1.0.0.jar /app/app.jar

USER app
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
