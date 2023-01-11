plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
    id("com.android.library")
    id("com.squareup.sqldelight")
}

kotlin {
    android()
    
    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach {
        it.binaries.framework {
            baseName = "shared"
        }
    }

    val sqlDelightVersion = "1.5.4"
    val ktorVersion = "2.0.2"

    sourceSets {
        val commonMain by getting {
            dependencies {

                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
                implementation("com.squareup.sqldelight:runtime:$sqlDelightVersion")

                implementation("io.ktor:ktor-serialization-kotlinx-json:$ktorVersion")
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
        val androidMain by getting {
            dependencies {
                // Adding implementations required for apache mina library
                implementation("org.apache.mina:mina-core:3.0.0-M2")
                implementation("org.apache.sshd:sshd-core:2.1.0")
                implementation("org.apache.sshd:sshd-putty:2.1.0")
                implementation("org.apache.sshd:sshd-common:2.1.0")
                implementation("org.slf4j:slf4j-api:1.7.36")
                implementation("org.slf4j:slf4j-simple:1.6.4")

                implementation("com.squareup.sqldelight:android-driver:$sqlDelightVersion")
            }
        }
        val androidTest by getting
        val iosX64Main by getting
        val iosArm64Main by getting
        val iosSimulatorArm64Main by getting
        val iosMain by creating {
            dependsOn(commonMain)
            iosX64Main.dependsOn(this)
            iosArm64Main.dependsOn(this)
            iosSimulatorArm64Main.dependsOn(this)
            dependencies {
                implementation("com.squareup.sqldelight:native-driver:$sqlDelightVersion")
            }
        }
        val iosX64Test by getting
        val iosArm64Test by getting
        val iosSimulatorArm64Test by getting
        val iosTest by creating {
            dependsOn(commonTest)
            iosX64Test.dependsOn(this)
            iosArm64Test.dependsOn(this)
            iosSimulatorArm64Test.dependsOn(this)
        }
    }
}

android {
    namespace = "br.com.phs.sshconnection"
    compileSdk = 33
    defaultConfig {
        minSdk = 23
        targetSdk = 33
    }
}

sqldelight {
    database("AppDatabase") {
        packageName = "br.com.phs.sshconn.shared.cache"
    }
}