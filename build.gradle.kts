plugins {
    java
    id("org.springframework.boot") version "2.2.2.RELEASE"
    id("io.spring.dependency-management") version "1.0.8.RELEASE"
}

group = "tracing_issue"

java.sourceCompatibility = JavaVersion.VERSION_11

repositories {
    mavenCentral()
}

dependencyManagement {
    imports {
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:Hoxton.RELEASE")
    }
}

dependencies {
    implementation("org.springframework.cloud:spring-cloud-starter-zipkin")
    implementation("org.springframework.cloud:spring-cloud-stream-binder-kafka")
    implementation("org.springframework.boot:spring-boot-starter-webflux")

    testImplementation("com.github.tomakehurst:wiremock-jre8-standalone:2.25.1")
    testImplementation("org.springframework.cloud:spring-cloud-stream-test-support")
}

extra["brave.version"] = "5.9.1"
