import org.jooq.meta.kotlin.*
plugins {
  id("org.springframework.boot") version "3.4.1"
  id("io.spring.dependency-management") version "1.1.7"
  id("nu.studer.jooq") version "10.2"
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

jooq {
  version.set("3.20.10")
  edition.set(nu.studer.gradle.jooq.JooqEdition.OSS)
  configurations {
    create("main") {
      jooqConfiguration {
        logging = org.jooq.meta.jaxb.Logging.WARN
        generator {
          name = "org.jooq.codegen.KotlinGenerator"
          database {
            name = "org.jooq.meta.extensions.ddl.DDLDatabase"
            properties {
              add(org.jooq.meta.jaxb.Property().withKey("scripts").withValue("src/main/resources/db/migration/*.sql"))
              add(org.jooq.meta.jaxb.Property().withKey("sort").withValue("flyway"))
              add(org.jooq.meta.jaxb.Property().withKey("unqualifiedSchema").withValue("none"))
            }
          }
          generate {
            isDeprecated = false
            isRecords = true
            isImmutablePojos = true
            isFluentSetters = true
          }
          target {
            packageName = "com.example.aisearch.jooq"
            directory = "build/generated-src/jooq/main"
          }
        }
      }
    }
  }
}

dependencies {
  implementation("org.springframework.boot:spring-boot-starter-webflux")
  implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
  implementation("org.jetbrains.kotlin:kotlin-reflect")

  implementation("io.r2dbc:r2dbc-h2")
  implementation("org.springframework.boot:spring-boot-starter-jdbc")
  implementation("org.flywaydb:flyway-core")
  implementation("com.clickhouse:clickhouse-jdbc:0.6.0:all")
  implementation("com.h2database:h2")
  implementation("org.apache.httpcomponents.client5:httpclient5:5.6")
  jooqGenerator("org.jooq:jooq-meta-extensions:3.20.10")
}


configurations.named("jooqGenerator") {
  resolutionStrategy.eachDependency {
    if (requested.group == "org.jooq") {
      useVersion("3.20.10")
      because("Keep jOOQ codegen classpath consistent (fix NoSuchMethodError Jdbc.getUrlProperty)")
    }
  }
}
