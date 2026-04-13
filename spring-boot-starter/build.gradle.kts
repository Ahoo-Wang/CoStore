plugins {
    alias(libs.plugins.kotlin.spring)
}
dependencies {
    implementation(project(":core"))
    implementation(project(":s3"))
    implementation(project(":oss"))

    api(libs.aws.s3)
    api(libs.aliyun.oss)

    implementation(libs.spring.boot)
    implementation(libs.spring.boot.autoconfigure)
}
