plugins {
    //trick: for the same plugin versions in all sub-modules
    id("com.android.application").version("7.3.1").apply(false)
    id("com.android.library").version("7.3.1").apply(false)
    kotlin("android").version("1.7.10").apply(false)
    kotlin("multiplatform").version("1.7.10").apply(false)
}

buildscript {
    val sqlDelightVersion = "1.5.4"
    val kotlinVersion = "1.7.10"
    dependencies {
        classpath("com.squareup.sqldelight:gradle-plugin:$sqlDelightVersion")
        classpath("org.jetbrains.kotlin:kotlin-serialization:${kotlinVersion}")
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}
