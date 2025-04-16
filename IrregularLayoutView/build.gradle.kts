plugins {
	alias(libs.plugins.androidLibrary)
	// 添加 maven-publish 插件
	id("maven-publish")  // 必需插件‌:ml-citation{ref="3,8" data="citationList"}
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
		sourceCompatibility = JavaVersion.VERSION_11
		targetCompatibility = JavaVersion.VERSION_11
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



// 配置发布到 GitHub Packages
afterEvaluate {
	publishing {
		repositories {
			publications {
				register<MavenPublication>("release") {
					from(components["release"]) // 发布 release 变体的 AAR
					groupId = "org.dy"
					artifactId = "irregularlayoutview"
					version = "1.0.1"
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