plugins {
    alias(libs.plugins.java)
    alias(libs.plugins.application)
}

group = "br.edu.ifpe"
version = libs.versions.app.version.get()


application {
    mainClass.set("br.edu.ifpe.launcher.AppMain")
}

repositories {
    mavenCentral()
    maven { url = uri("./offline-repository") }
}

dependencies {
    implementation(libs.apache.commons.csv)
    implementation(libs.apache.commons.jexl3)
    implementation(libs.apache.poi)
    implementation(libs.apache.poi.ooxml)
    implementation(libs.logback)
    implementation(libs.flatlaf)
    implementation(libs.flatlaf.extras)

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
                "version" to version
            ),
        )
    }
}