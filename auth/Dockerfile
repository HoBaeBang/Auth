#첫 번째 단계 : 빌드 환경 구축
FROM gradle:8.6.0-jdk17 as builder
WORKDIR /build
# gradle:7.4-jdk17-alpine 이미지를 기반으로 새로운 빌드 스테이지를 시작합니다. 이 이미지는 Gradle 7.4와 JDK 17이 설치된 Alpine Linux를 사용합니다.
# WORKDIR /build 명령어는 작업 디렉토리를 /build로 설정합니다.



# 그래들 파일이 변경되었을 때만 새롭게 의존패키지 다운로드 받게함.
COPY build.gradle settings.gradle /build/
RUN gradle build -x test --parallel --continue > /dev/null 2>&1 || true
# build.gradle과 settings.gradle 파일을 이미지의 /build 디렉토리로 복사합니다. 이는 의존성 관리 파일들로, 이 단계는 그래들 설정이 변경될 때만 의존성을 다시 다운로드하게 합니다.
# gradle build -x test --parallel --continue 명령어로 의존성을 다운로드하고 프로젝트를 빌드합니다. -x test는 테스트를 건너뛰고, --parallel은 병렬 빌드를 활성화하며, --continue는 실패해도 가능한 한 많은 작업을 계속하라는 의미입니다. 출력은 무시됩니다.

# 빌더 이미지에서 애플리케이션 빌드
COPY . /build
RUN gradle build -x test --parallel
# 현재 디렉토리의 모든 파일을 이미지의 /build 디렉토리로 복사합니다.
# 다시 한번 gradle build -x test --parallel 명령어로 애플리케이션을 빌드합니다.


# 두 번째 단계: 애플리케이션 실행 환경 구성
# APP
FROM khipu/openjdk17-alpine
WORKDIR /app
# openjdk:17-alpine 이미지를 기반으로 새로운 스테이지를 시작합니다. 이 이미지는 OpenJDK 17이 설치된 Alpine Linux를 사용합니다.
# WORKDIR /app 명령어는 작업 디렉토리를 /app로 설정합니다.


# 빌더 이미지에서 jar 파일만 복사
# COPY --from=builder /build/build/libs/*-SNAPSHOT.jar ./app.jar
COPY --from=builder ./build/build/libs/auth-0.0.1-SNAPSHOT.jar .
# 첫 번째 스테이지에서 빌드된 auth-0.0.1-SNAPSHOT.jar 파일을 현재 스테이지의 작업 디렉토리(/app)로 복사합니다.

EXPOSE 8081
# 컨테이너에서 애플리케이션은 8081 포트에서 실행됩니다. 이 포트를 컨테이너 외부로 노출합니다.

# root 대신 nobody 권한으로 실행
USER nobody
# nobody 사용자로 애플리케이션을 실행합니다. 이는 보안을 강화하기 위한 조치입니다.
ENTRYPOINT [                                                \
   "java",                                                 \
   "-jar",                                                 \
   "-Djava.security.egd=file:/dev/./urandom",              \
   "-Dsun.net.inetaddr.ttl=0",                             \
   "auth-0.0.1-SNAPSHOT.jar"]
# 컨테이너가 시작될 때 실행될 명령어를 정의합니다. 여기서는 Java 애플리케이션을 실행하는 java -jar 명령어와 함께 몇 가지 시스템 프로

#docker run -p 8080:8081 -e SPRING_DATASOURCE_URL=jdbc:mysql://localhost:3309 -e SPRING_DATASOURCE_PASSWORD=0120 -d auth