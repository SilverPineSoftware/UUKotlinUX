plugins {
    alias(uuBuild.plugins.uu.library.app)
    id("com.android.legacy-kapt")
}

android {
    buildFeatures {
        dataBinding = true
    }
}

dependencies {
    implementation(uuBuild.androidx.appcompat)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.google.material)
    implementation(libs.uu.core.ktx)
    implementation(project(":library"))

    androidTestImplementation(uuBuild.androidx.junit)
    androidTestImplementation(uuBuild.androidx.espresso.core)
}
