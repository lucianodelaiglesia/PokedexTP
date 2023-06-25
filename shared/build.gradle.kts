plugins {
    kotlin("multiplatform")
    kotlin("native.cocoapods")
    id("com.android.library")
    kotlin("plugin.serialization") version "1.6.10"
    id("app.cash.sqldelight") version "2.0.0-rc01"
}

@OptIn(org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi::class)
kotlin {
    targetHierarchy.default()

    android {
        compilations.all {
            kotlinOptions {
                jvmTarget = "1.8"
            }
        }
    }
    iosX64()
    iosArm64()
    iosSimulatorArm64()

    cocoapods {
        summary = "Some description for the Shared Module"
        homepage = "Link to the Shared Module homepage"
        version = "1.0"
        ios.deploymentTarget = "14.1"
        podfile = project.file("../iosApp/Podfile")
        framework {
            baseName = "shared"
        }
    }
    
    sourceSets {

        val ktorVersion = "2.0.0-beta-1"
        val napierVersion = "2.6.1"

        val commonMain by getting {
            dependencies {
                //KTOR
                implementation("io.ktor:ktor-client-core:$ktorVersion")
                implementation("io.ktor:ktor-client-logging:$ktorVersion")

                //NAPIER
                implementation("io.github.aakira:napier:$napierVersion")

                //KOTLIN SERIALIZATION
                implementation("io.ktor:ktor-client-content-negotiation:$ktorVersion")
                implementation("io.ktor:ktor-serialization-kotlinx-json:$ktorVersion")

                //SQLDELIGHT
                implementation("app.cash.sqldelight:sqlite-driver:2.0.0-rc01")
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
        val androidMain by getting {
            dependencies {
                //KTOR
                implementation("io.ktor:ktor-client-okhttp:$ktorVersion")

                //SQLDELIGHT
                implementation("app.cash.sqldelight:android-driver:2.0.0-rc01")
            }
        }
/*
        //Tuve que comentar esta parte porque me tiraba un error de que ya existia un módulo iosMain
        val iosX64Main by getting
        val iosArm64Main by getting
        val iosSimulatorArm64Main by getting

        val iosMain by getting {
            dependsOn(commonMain)
            iosX64Main.dependsOn(this)
            iosArm64Main.dependsOn(this)
            iosSimulatorArm64Main.dependsOn(this)
            dependencies{
                //KTOR
                implementation("io.ktor:ktor-client-ios:$ktorVersion")

                //SQLDELIGHT
                implementation("app.cash.sqldelight:native-driver:2.0.0-rc01")
            }
        }

 */
    }
}

android {
    namespace = "com.example.pokedextp"
    compileSdk = 33
    defaultConfig {
        minSdk = 26
    }
}

sqldelight {
    databases {
        create("MyDatabase") {
            packageName.set("com.example.pokedextp")
        }
    }
}