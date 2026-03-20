plugins {
    alias(libs.plugins.kotlin.jvm)
}

kotlin {
    jvmToolchain(17)
}

dependencies {
    implementation(project(":costore-core"))
    implementation(libs.aws.s3)
    implementation(libs.aws.sts)

    testImplementation(libs.kotlin.test)
    testImplementation(libs.fluent.assert)
    testImplementation(libs.mockk)
    testRuntimeOnly(libs.junit.platform.launcher)
}

tasks.test {
    useJUnitPlatform()
}
