java {
    registerFeature("reactorSupport") {
        usingSourceSet(sourceSets[SourceSet.MAIN_SOURCE_SET_NAME])
        capability(group.toString(), "reactor-support", version.toString())
    }
    registerFeature("coroutinesSupport") {
        usingSourceSet(sourceSets[SourceSet.MAIN_SOURCE_SET_NAME])
        capability(group.toString(), "coroutines-support", version.toString())
    }
}

dependencies {
    compileOnly(libs.jackson.annotations)
    compileOnly(libs.jakarta.validation.api)
    testImplementation(libs.jakarta.validation.api)
    testImplementation(libs.hibernate.validator)
    testImplementation(libs.jakarta.el)
    "coroutinesSupportImplementation"(libs.kotlinx.coroutines.core)
    "reactorSupportImplementation"(libs.reactor.core)
    testImplementation(libs.reactor.test)
}
