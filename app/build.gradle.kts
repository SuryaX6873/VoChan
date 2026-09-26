import java.util.Properties
import java.io.FileInputStream

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.compose.compiler)
}

android {
    namespace = "com.voicechanger"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.voicechanger"
        minSdk = 33
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            // Use debug key for signing - allows installation without setting up a real keystore in CI
            signingConfig = signingConfigs.getByName("debug")
        }
    }
    
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    
    buildFeatures {
        compose = true
    }
    
    packaging {
        resources {
            resources.excludes.add("/META-INF/{AL2.0,LGPL2.1}")
            resources.excludes.add("META-INF/kotlinx_coroutines_core.version")

            // The part below is only needed for compose builds.
            // This packaging block is required to solve interdependency conflicts.
            // They arise only when using local maven repo, so I suppose online repos have some way of solving such issues.

            // Caused by: com.android.builder.merge.DuplicateRelativeFileException: 4 files found with path 'commonMain/default/linkdata/module' from inputs:
            // - AndroidIDE\libs_source\gradle\localMvnRepository\androidx\collection\collection\1.4.2\collection-1.4.2.jar
            // - AndroidIDE\libs_source\gradle\localMvnRepository\androidx\lifecycle\lifecycle-common\2.8.7\lifecycle-common-2.8.7.jar
            // - AndroidIDE\libs_source\gradle\localMvnRepository\androidx\annotation\annotation\1.8.1\annotation-1.8.1.jar
            // - AndroidIDE\libs_source\gradle\localMvnRepository\org\jetbrains\kotlinx\kotlinx-coroutines-core\1.7.3\kotlinx-coroutines-core-1.7.3.jar
            // And some others.
            resources.pickFirsts.add("nonJvmMain/default/linkdata/package_androidx/0_androidx.knm")
            resources.pickFirsts.add("nonJvmMain/default/linkdata/root_package/0_.knm")
            resources.pickFirsts.add("nonJvmMain/default/linkdata/module")

            resources.pickFirsts.add("nativeMain/default/linkdata/root_package/0_.knm")
            resources.pickFirsts.add("nativeMain/default/linkdata/module")

            resources.pickFirsts.add("commonMain/default/linkdata/root_package/0_.knm")
            resources.pickFirsts.add("commonMain/default/linkdata/module")
            resources.pickFirsts.add("commonMain/default/linkdata/package_androidx/0_androidx.knm")

            resources.pickFirsts.add("META-INF/kotlin-project-structure-metadata.json")

            resources.merges.add("commonMain/default/manifest")
            resources.merges.add("nonJvmMain/default/manifest")
            resources.merges.add("nativeMain/default/manifest")
        }
    }
}

kotlin {
    compilerOptions {
        jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.fromTarget("17"))
    }
}

dependencies {
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")
    implementation("androidx.activity:activity-compose:1.8.2")
    implementation(platform("androidx.compose:compose-bom:2024.09.00")) 
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    
    // Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2024.09.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
}
