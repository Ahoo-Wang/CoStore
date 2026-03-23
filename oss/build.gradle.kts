dependencies {
    implementation(project(":core"))
    implementation(libs.aliyun.oss)
}

tasks.test {
    useJUnitPlatform()
}
