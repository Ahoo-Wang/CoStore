dependencies {
    implementation(project(":core"))
    implementation(libs.aws.s3)
    implementation(libs.aws.sts)
}

tasks.test {
    useJUnitPlatform()
}
