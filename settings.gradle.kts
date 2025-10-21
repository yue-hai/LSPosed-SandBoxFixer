// 配置 Gradle 在解析插件依赖项时应该使用的仓库
pluginManagement {
    // 定义了插件仓库的列表
    repositories {
        // 使用 Google 的 Maven 仓库，这是获取 Android 相关插件的主要来源
        google {
            // 更精细地控制此仓库中可以查找哪些依赖项
            content {
                // 只从 Google 仓库下载 Group ID 符合正则表达式 com.android.* 的插件
                includeGroupByRegex("com\\.android.*")
                // 只从 Google 仓库下载 Group ID 符合正则表达式 com.google.* 的插件
                includeGroupByRegex("com\\.google.*")
                // 只从 Google 仓库下载 Group ID 符合正则表达式 androidx.* 的插件
                includeGroupByRegex("androidx.*")
            }
        }
        // 使用 Maven Central 仓库，这是一个非常常见的、包含了大量 Java 和其他语言库的公共仓库
        mavenCentral()
        // 使用 Gradle 官方的插件门户仓库
        gradlePluginPortal()
    }
}

// 添加注解，抑制不稳定的 API 提示
@Suppress("UnstableApiUsage")
// 用于集中管理项目中所有模块的依赖项解析行为，这样做可以确保整个项目使用统一的仓库配置，避免混乱
dependencyResolutionManagement {
    // repositoriesMode.set 设置仓库的管理模式
    // RepositoriesMode.FAIL_ON_PROJECT_REPOS 是一个推荐的最佳实践，它会禁止在子模块的 build.gradle(.kts) 文件中单独定义仓库
    // 如果子模块中定义了仓库，构建过程会失败，从而强制所有仓库都在这个 settings 文件中统一管理
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    
    // repositories 定义了项目依赖库的仓库列表
    repositories {
        // 使用 Google 的 Maven 仓库，用于获取 Android 相关的库（如 AndroidX, Firebase 等）
        google()
        // 使用 Maven Central 仓库，用于获取通用的开源库
        mavenCentral()
        // 添加 Xposed API 的仓库地址，告诉 Gradle 去哪里找 Xposed API
        maven { url = uri("https://api.xposed.info/") }
    }
}

// 设置根项目的名称，这个名称通常会显示在 Android Studio 的项目视图顶部
rootProject.name = "SandBoxFixer"
// include 方法用于将一个子模块包含到项目中，:app 表示包含名为 app 的模块，这是 Android 应用项目的主模块
include(":app")
