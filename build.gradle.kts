plugins {
    alias(libs.plugins.kotlin.jvm) apply false
}

allprojects {
    group = "me.ahoo.costore"
    version = "0.1.0"

    repositories {
        mavenCentral()
    }
}
