plugins {
    id("java")
    id("application")
    id("com.gradleup.shadow") version "9.2.2"
}

group = "br.ifpe.edu"
version = "1.0.0"


tasks.withType<ProcessResources> {
    filesMatching("**/application.properties") {
        expand(mapOf("projectVersion" to project.version))
    }
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(25))
    }
}


application {
    mainClass.set("br.ifpe.edu.AppMain")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.apache.commons:commons-csv:1.14.1")
    implementation("org.apache.commons:commons-jexl3:3.6.0")
    implementation("org.apache.poi:poi:5.5.1")
    implementation("org.apache.poi:poi-ooxml:5.5.1")
    implementation("org.projectlombok:lombok:1.18.42")
    implementation("ch.qos.logback:logback-classic:1.5.22")
    implementation("com.formdev:flatlaf:3.6.1")
    implementation("com.formdev:flatlaf-extras:3.6.1")

    compileOnly("org.projectlombok:lombok:1.18.42")
    annotationProcessor("org.projectlombok:lombok:1.18.42")

    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.test {
    useJUnitPlatform()
}
