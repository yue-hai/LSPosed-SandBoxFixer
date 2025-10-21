// Top-level build file where you can add configuration options common to all sub-projects/modules.
// 声明和管理在项目中使用的 Gradle 插件，用于声明项目所需的插件及其版本，而不是要将它们应用到根项目上
plugins {
    // 声明 Android 应用程序插件，用于将所有代码和资源打包成最终的 APK
    alias(libs.plugins.android.application) apply false
    // 声明 Kotlin Android 插件，让 Gradle 能够理解和编译 Kotlin 代码（.kt 文件），并将其与 Android 框架正确集成
    alias(libs.plugins.kotlin.android) apply false
    // 声明 Kotlin Compose 插件，启用 Jetpack Compose 框架的编译器插件
    alias(libs.plugins.kotlin.compose) apply false
}
