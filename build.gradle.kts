plugins {
    //trick: for the same plugin versions in all sub-modules
    id("com.android.application").version(libs.versions.gradlePlugin).apply(false)
    id("com.android.library").version(libs.versions.gradlePlugin).apply(false)
    kotlin("android").version(libs.versions.kotlin).apply(false)
    kotlin("multiplatform").version(libs.versions.kotlin).apply(false)
    id("com.rickclephas.kmp.nativecoroutines").version(libs.versions.kmp.nativecoroutines).apply(false)
    id("org.jetbrains.kotlin.jvm").version(libs.versions.kotlin).apply(false)
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}
