# Dockerfile

# Stage 1: Build application
FROM azul/zulu-openjdk:17-latest AS build
WORKDIR /app

# Gradle Wrapper 파일 캐싱
COPY gradlew .
COPY gradle gradle
COPY build.gradle settings.gradle ./
RUN chmod +x ./gradlew

# 소스 복사 및 빌드
COPY src src
RUN ./gradlew build -x test

# Stage 2: Runtime image
FROM azul/zulu-openjdk:17-latest
WORKDIR /app

COPY --from=build /app/build/libs/*.jar app.jar

EXPOSE 8080
ENV SPRING_PROFILES_ACTIVE=prod

ENTRYPOINT ["java", "-jar", "app.jar"]