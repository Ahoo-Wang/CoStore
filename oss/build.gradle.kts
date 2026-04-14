dependencies {
    implementation(project(":core"))
    implementation(libs.aliyun.oss)
    testImplementation(libs.reactor.core)
    testImplementation(libs.kotlinx.coroutines.core)
    testImplementation(libs.reactor.test)
}
