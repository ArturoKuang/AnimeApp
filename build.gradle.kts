// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    base
    jacoco
    id("com.android.application") version "8.1.0" apply false
    id("org.jetbrains.kotlin.android") version "1.8.10" apply false
    id("com.github.nbaztec.coveralls-jacoco") version "1.2.16" apply false
    id("org.jmailen.kotlinter") version "3.16.0"
    id("io.gitlab.arturbosch.detekt").version("1.23.1")
}

tasks {
    val detektAll by registering(io.gitlab.arturbosch.detekt.Detekt::class) {
        parallel = true
        setSource(files(projectDir))
        include("**/*.kt")
        include("**/*.kts")
        exclude("**/resources/**")
        exclude("**/build/**")
        config.setFrom(files("$rootDir/config/detekt/detekt.yml"))
        buildUponDefaultConfig = false
    }
}

apply(from = "buildscripts/githooks.gradle")

subprojects {
    apply(from = "${rootProject.projectDir}/buildscripts/ktlint.gradle")
}

afterEvaluate {
    // We install the hook at the first occasion
    tasks.named("clean") {
        dependsOn(":installGitHooks")
    }
}
