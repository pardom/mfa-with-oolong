plugins {
    kotlin("jvm")
}

dependencies {
    api(deps.oolong)
    testImplementation(deps.kotlin.test.junit5)
    testImplementation(deps.kotlin.test.jvm)
}
