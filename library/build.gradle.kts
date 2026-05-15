plugins {
    id("com.android.library")
    id("kotlin-parcelize")
    id("com.android.legacy-kapt")
    id("maven-publish")
    id("signing")
    alias(uuBuild.plugins.kotlin.serialization)
    alias(uuBuild.plugins.kotlin.compose)
    alias(uuBuild.plugins.uu.library)
    alias(uuBuild.plugins.uu.android.test)
}

android {
    buildFeatures {
        dataBinding = true
        compose = true
    }
}

composeCompiler {
    reportsDestination.set(layout.buildDirectory.dir("compose_compiler"))
    val stabilityConfig = rootProject.layout.projectDirectory.file("stability_config.conf")
    if (stabilityConfig.asFile.exists()) {
        stabilityConfigurationFiles.add(stabilityConfig)
    }
}

dependencies {
    implementation(uuBuild.androidx.annotation)
    implementation(uuBuild.androidx.appcompat)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.recyclerview)
    implementation(libs.uu.core.ktx)
    implementation(libs.androidx.test.rules)
    implementation(libs.androidx.navigation3.runtime.android)
    implementation(libs.androidx.compose.material3.android)

    testImplementation(platform(uuBuild.junit.bom))
    testImplementation(uuBuild.junit.jupiter)
    testImplementation(uuBuild.junit.jupiter.api)
    testImplementation(uuBuild.junit.jupiter.engine)
    testImplementation(uuBuild.junit.jupiter.params)
    testRuntimeOnly(uuBuild.junit.platform.launcher)
    testImplementation(uuBuild.mockito.junit.jupiter)
    testImplementation(libs.kotlinx.coroutines.test)

    testImplementation(libs.uu.test.ktx)

    androidTestImplementation(uuBuild.androidx.junit)
    androidTestImplementation(uuBuild.androidx.espresso.core)

    androidTestImplementation(libs.uu.test.ktx)
}

android {
    testOptions {
        unitTests.all {
            it.useJUnitPlatform()
        }
    }
}
