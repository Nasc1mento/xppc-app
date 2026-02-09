plugins {
    alias(libs.plugins.java)
    alias(libs.plugins.application)
    alias(libs.plugins.shadow)
}

group = "br.edu.ifpe"
version = libs.versions.app.version.get()

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(libs.versions.java.get()))
    }
}


application {
    mainClass.set("br.edu.ifpe.launcher.AppMain")

}

repositories {
    mavenCentral()
}

dependencies {
    implementation(libs.apache.commons.csv)
    implementation(libs.apache.commons.jexl3)
    implementation(libs.apache.poi)
    implementation(libs.apache.poi.ooxml)
    implementation(libs.lombok)
    implementation(libs.logback)
    implementation(libs.flatlaf)
    implementation(libs.flatlaf.extras)

    compileOnly(libs.lombok)
    annotationProcessor(libs.lombok)

    testImplementation(libs.assert.swing)
    testImplementation(platform(libs.junit.bom))
    testImplementation(libs.junit.jupiter)
    testRuntimeOnly(libs.junit.launcher)
}

tasks.test {
    useJUnitPlatform()
}

tasks.register("xPPCVersion") {
    doLast {
        println(version)
    }
}


tasks.withType<ProcessResources> {
    filesMatching("**/application.properties") {
        expand(
            mapOf(
                "name" to "xPPC - Aplicação para Geração Automatizada de Projetos Pedagógicos de Cursos Superiores do IFPE",
                "shortname" to "xPPC",
                "version" to version
            ),
        )
    }
}

tasks.shadowJar {
    manifest {
        attributes(
            "SplashScreen-Image" to "loading.gif"
        )
    }
}