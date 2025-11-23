#＝＝＝＝＝＝＝＝＝＝階段一maven打包＝＝＝＝＝＝＝＝＝＝＝＝
FROM maven:3.9.9-eclipse-temurin-17 AS build
WORKDIR /app

# 先複製 pom.xml 並下載依賴。
COPY pom.xml .
RUN mvn dependency:go-offline

# 再複製 src 原始碼並進行打包
COPY src ./src
RUN mvn clean package -DskipTests

#＝＝＝＝＝＝＝＝＝＝階段二服務佈置＝＝＝＝＝＝＝＝＝＝＝＝
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app

# 從 build 階段複製 jar 出來
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
