plugins {
    // 引入 Android 应用程序插件，用于将所有代码和资源打包成最终的 APK
    alias(libs.plugins.android.application)
    // 引入 Kotlin Android 插件，让 Gradle 能够理解和编译 Kotlin 代码（.kt 文件），并将其与 Android 框架正确集成
    alias(libs.plugins.kotlin.android)
    // 引入 Kotlin Compose 插件，启用 Jetpack Compose 框架的编译器插件
    alias(libs.plugins.kotlin.compose)
}

// Android 项目构建配置的核心部分
android {
    // 定义项目的命名空间，用于生成 R 类，并作为 AndroidManifest.xml 中 package 属性的默认值
    namespace = "com.yuehai.sandboxfixer"
    // 指定用于编译应用的 Android API 级别，代码可以使用此版本及更低版本的所有 API
    compileSdk = 35
    
    // 应用的默认配置，适用于所有构建变体
    defaultConfig {
        // 应用的唯一标识符，用于在设备和 Google Play 商店中识别应用
        applicationId = "com.yuehai.sandboxfixer"
        // 应用可以运行的最低 Android API 级别
        minSdk = 26
        // 应用设计和测试所基于的目标 Android API 级别。建议始终使用最新的 API 级别
        targetSdk = 35
        // 应用的版本号，一个内部递增的整数，用于应用更新判断
        versionCode = 1
        // 应用的版本名称，显示给用户的字符串
        versionName = "0.1"
        // 指定用于运行仪器化测试 (Instrumented Tests) 的测试运行器
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    
    // 定义构建类型，通常至少包含 debug 和 release 两种
    buildTypes {
        // 配置发布 (release) 构建类型
        release {
            // 是否启用代码缩减 (Shrinking) 和混淆 (Obfuscation)。开启后会使用 R8 (ProGuard)
            isMinifyEnabled = true
            // 是否启用资源缩减 (Resource Shrinking)。它会移除未被引用的资源文件，必须在 isMinifyEnabled = true 时才能生效
            isShrinkResources = true
            // 指定混淆规则文件，包括 Gradle 默认的优化规则和项目自定义的规则
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    // 配置 Java 编译选项
    compileOptions {
        // 设置源码兼容的 Java 版本
        sourceCompatibility = JavaVersion.VERSION_11
        // 设置生成字节码兼容的 Java 版本
        targetCompatibility = JavaVersion.VERSION_11
    }
    // 配置 Kotlin 编译选项
    kotlinOptions {
        // 设置 Kotlin 编译器生成的 JVM 字节码版本
        jvmTarget = "11"
    }
    // 用于启用或禁用特定的构建功能
    buildFeatures {
        // 启用 Jetpack Compose 支持
        compose = true
    }
}

// 定义项目的依赖项
dependencies {
    // 为 Android 框架和支持库提供 Kotlin 扩展。它包括一些扩展函数，用于简化新 Android 组件的创建和现有组件的处理
    implementation(libs.androidx.core.ktx)
    // 提供了一些 Kotlin 扩展方法，LiveData、ViewModel、Lifecycle、协程、lifecycleScope 等
    implementation(libs.androidx.lifecycle.runtime.ktx)
    // 提供了 Activity 与 Compose 交互的桥梁，最主要的就是 setContent { ... } 函数
    implementation(libs.androidx.activity.compose)
    
    // Jetpack Compose 的 "Bill of Materials" (BOM)，用于统一管理所有 Compose 库的版本
    implementation(platform(libs.androidx.compose.bom))
    // Compose UI 核心库
    implementation(libs.androidx.compose.ui)
    // Compose 的图形处理库
    implementation(libs.androidx.compose.ui.graphics)
    // 用于在 Android Studio 中启用 @Preview 预览
    implementation(libs.androidx.compose.ui.tooling.preview)
    // 实现了 Material Design 3 (M3) 的 UI 组件库
    implementation(libs.androidx.compose.material3)
    
    // okhttp 是 android 平台使用最广泛的第三方网络框架，okhttp 做了很多网络优化，功能也很强大
    // implementation(libs.okhttp)
    // // 日志拦截器依赖
    // implementation(libs.okhttp.logging.interceptor)
    // // retrofit 是一个网络请求库，基于 okhttp，使用简单，功能强大
    // implementation(libs.retrofit)
    // // retrofit 的 Gson 转换器
    // implementation(libs.retrofit.converter.gson)
    
    // JUnit 4，用于运行本地单元测试
    testImplementation(libs.junit)
    // 用于在 Android 设备上运行仪器化测试的 JUnit 扩展
    androidTestImplementation(libs.androidx.test.ext.junit)
    // Compose 测试专用的 BOM，统一测试库版本
    androidTestImplementation(platform(libs.androidx.compose.bom))
    // 用于对 Jetpack Compose UI 进行仪器化测试的框架
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    // 用于在 Debug 构建中检查 Compose UI 的高级工具
    debugImplementation(libs.androidx.compose.ui.tooling)
    // 用于帮助 Android Studio 发现和运行 Compose 测试
    debugImplementation(libs.androidx.compose.ui.test.manifest)
    
    // Xposed / LSPosed 框架的 API，使用 compileOnly 告知编译器在运行时提供
    compileOnly(libs.xposed.api)
}
