plugins {
	alias(libs.plugins.androidLibrary)
	// 添加 maven-publish 插件
	id("maven-publish")  // 必需插件‌
}

android {
	namespace = "org.dy.irregularlayoutview"
	compileSdk = 35

	defaultConfig {
		minSdk = 19
		testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
		consumerProguardFiles("consumer-rules.pro")
	}

	buildTypes {
		release {
			isMinifyEnabled = false
			proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
		}
	}
	compileOptions {
		sourceCompatibility = JavaVersion.VERSION_17
		targetCompatibility = JavaVersion.VERSION_17
	}
	// 配置发布变体
	publishing {
		singleVariant("release") {
			withSourcesJar() // 包含源码
			withJavadocJar() // 包含文档（需配置 Dokka 生成 Kotlin 文档）
		}
	}
}

dependencies {
	implementation("androidx.appcompat:appcompat:1.6.1")
	implementation("com.yanzhenjie:album:2.1.2")
}

val  versions = "1.0.3"
// 配置发布到 GitHub Packages
afterEvaluate {
	publishing {
		repositories {
			publications {
				register<MavenPublication>("release") {
					from(components["release"]) // 发布 release 变体的 AAR
					groupId = "org.dy"
					artifactId = "irregularlayoutview"
					version = versions
					pom {
						description.set("`IrregularLayoutView` 是一个 Android 自定义控件模块，用于实现矩形布局 切割－捏合 并选择相册图片填充，缩放。用途，创建自定义moka")
						url.set("https://github.com/dyshandy/IrregularLayoutView")
					}
				}
			}
			maven {
				name = "GitHubPackages"
				url = uri("https://maven.pkg.github.com/dyshandy/IrregularLayoutView")
				credentials {
					username = project.findProperty("gpr.user") as String? ?: System.getenv("GITHUB_USERNAME")
					password = project.findProperty("gpr.key") as String? ?: System.getenv("GITHUB_TOKEN")
				}
			}
		}
	}
}

// 任务：生成或更新 README.md
tasks.register("updateReadme") {
	doLast {
		val version = versions
		val readmeFile = file("../README.md")
		val readmeContent = readmeFile.readText()
		val updatedReadmeContent = readmeContent.replace(Regex("version: \\d+\\.\\d+\\.\\d+"), "version: $version")
		readmeFile.writeText(updatedReadmeContent)
	}
}