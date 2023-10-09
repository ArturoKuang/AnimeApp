plugins {
    jacoco
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jmailen.kotlinter")
    id("com.github.nbaztec.coveralls-jacoco")
}

jacoco {
    toolVersion = "0.8.9"
}

coverallsJacoco {
    reportPath = "${rootProject.projectDir}/app/build/reports/coverage/androidTest/debug/connected/report.xml"
}

tasks {
    val jacocoTestReport by registering(JacocoReport::class) {
        dependsOn("testDebugUnitTest", "createDebugCoverageReport")

        group = "Reporting"
        description = "Generate Jacoco coverage reports"

        reports {
            csv.required.set(false)
            xml.required.set(true)
            html.required.set(true)
        }

        val fileFilter = listOf(
            "androidx/**/*.*",
            "**/view/*.*",
            "**/data/*.*",
            "**/data/model/*.*",
            "**/generated/callback/*.*",
            "**/lambda$*.class",
            "**/lambda.class",
            "**/*lambda.class",
            "**/*lambda*.class"
        )

        val debugTree = fileTree(mapOf(
            "dir" to "${buildDir}/intermediates/javac/debug/classes",
            "excludes" to fileFilter
        ))

        val mainSrc = "src/main/java"

        sourceDirectories.setFrom(mainSrc)
        classDirectories.setFrom(debugTree)
        executionData.setFrom(fileTree(mapOf(
            "dir" to buildDir,
            "includes" to listOf("jacoco/testDebugUnitTest.exec")
        )))
    }
}

android {
    namespace = "com.anime"
    compileSdk = 33

    defaultConfig {
        applicationId = "com.anime"
        minSdk = 24
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        getByName("debug") {
            enableUnitTestCoverage = true
            enableAndroidTestCoverage = true
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.8"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    lintChecks("com.slack.lint.compose:compose-lint-checks:1.2.0")
    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")
    implementation("androidx.activity:activity-compose:1.7.2")
    implementation(platform("androidx.compose:compose-bom:2023.03.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2023.03.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
}
