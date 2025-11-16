plugins {
    id("java")
    id("application")
    id("com.gradleup.shadow") version "9.2.2"
}

group = "br.ifpe.edu"
version = "1.1-SNAPSHOT"

application {
    mainClass.set("br.ifpe.edu.AppMain")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.apache.poi:poi:5.4.1")
    implementation("org.apache.poi:poi-ooxml:5.4.1")
    implementation("com.opencsv:opencsv:5.12.0")
    implementation("com.formdev:flatlaf:3.6.1")
    implementation("com.ezylang:EvalEx:3.5.0")

    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.test {
    useJUnitPlatform()
}
