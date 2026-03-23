dependencies {
    implementation(libs.kotlinx.coroutines.core)

}

tasks.test {
    useJUnitPlatform()
}
