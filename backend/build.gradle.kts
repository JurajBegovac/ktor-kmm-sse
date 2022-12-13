plugins {
    kotlin("jvm") version libs.versions.kotlin
    application
    kotlin("plugin.serialization") version libs.versions.kotlin
    id("io.ktor.plugin") version libs.versions.ktor
}

application {
    mainClass.set("com.releaseit.backend.ServerKt")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}


dependencies {
    implementation(project(":shared-models"))
    implementation(libs.ktor.server.content.negotation.jvm)
    implementation(libs.ktor.server.core.jvm)
    implementation(libs.ktor.serialization.json.jvm)
    implementation(libs.ktor.server.netty.jvm)
    implementation("ch.qos.logback:logback-classic:1.4.4")
}
