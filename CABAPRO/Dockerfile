# ---------- Build stage ----------
FROM maven:3.9.9-eclipse-temurin-21 AS build
WORKDIR /app

# Copiamos el pom.xml y descargamos dependencias primero (cache)
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copiamos el resto del proyecto
COPY src ./src

# Compilamos sin ejecutar tests
RUN mvn clean package -DskipTests

# ---------- Runtime stage ----------
FROM eclipse-temurin:21-jdk-alpine
WORKDIR /app

# Copiamos el .jar generado
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]