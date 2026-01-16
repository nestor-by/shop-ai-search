plugins {
  id("org.springframework.boot") version "3.4.1"
  id("io.spring.dependency-management") version "1.1.7"
  kotlin("jvm") version "2.0.21"
  kotlin("plugin.spring") version "2.0.21"
}

kotlin {
  jvmToolchain(21)
}

group = "com.example"
version = "0.0.1-SNAPSHOT"

repositories {
  mavenCentral()
}

dependencies {
  implementation("org.springframework.boot:spring-boot-starter-webflux")
  implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
  implementation("org.jetbrains.kotlin:kotlin-reflect")

  implementation("org.springframework.boot:spring-boot-starter-jdbc")
  implementation("org.flywaydb:flyway-core")
  implementation("com.clickhouse:clickhouse-jdbc:0.6.0:all")
  implementation("com.h2database:h2")
  implementation("org.apache.httpcomponents.client5:httpclient5:5.6")
}
