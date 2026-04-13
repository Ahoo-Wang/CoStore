plugins {
    alias(libs.plugins.kotlin.spring)
    kotlin("kapt")
}
java {
    registerFeature("ossSupport") {
        usingSourceSet(sourceSets[SourceSet.MAIN_SOURCE_SET_NAME])
        capability(group.toString(), "oss-support", version.toString())
    }
    registerFeature("s3Support") {
        usingSourceSet(sourceSets[SourceSet.MAIN_SOURCE_SET_NAME])
        capability(group.toString(), "s3-support", version.toString())
    }
}

dependencies {
    implementation(project(":core"))
    implementation(project(":s3"))
    implementation(project(":oss"))

    "ossSupportImplementation"(libs.aliyun.oss)
    "s3SupportImplementation"(libs.aws.s3)

    implementation(libs.spring.boot)
    implementation(libs.spring.boot.autoconfigure)
    kapt(libs.spring.boot.configuration.processor)
    kapt(libs.spring.boot.autoconfigure.processor)
    testImplementation(libs.spring.boot.starter.test)
}
