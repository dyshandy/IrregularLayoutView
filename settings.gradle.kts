pluginManagement {
	repositories {
		mavenLocal()
		maven { setUrl("https://maven.aliyun.com/repository/central") }
		maven { setUrl("https://maven.aliyun.com/repository/public") }
		maven { setUrl("https://maven.aliyun.com/repository/jcenter") }
		maven { setUrl("https://maven.aliyun.com/repository/google") }
		maven { setUrl("https://maven.aliyun.com/repository/releases") }
		maven { setUrl("https://maven.aliyun.com/repository/snapshots") }
		maven { setUrl("https://maven.aliyun.com/repository/gradle-plugin") }
		maven { setUrl("https://jitpack.io") }
		maven { setUrl("https://tencent-tds-maven.pkg.coding.net/repository/shiply/repo") }
		maven { setUrl("https://mirrors.tencent.com/nexus/repository/maven-public") }
		google {
			content {
				includeGroupByRegex("com\\.android.*")
				includeGroupByRegex("com\\.google.*")
				includeGroupByRegex("androidx.*")
			}
		}
		mavenCentral()
		maven { setUrl("https://plugins.jetbrains.com/maven") }
		gradlePluginPortal()
	}
}
dependencyResolutionManagement {
	repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
	repositories {
		mavenLocal()
		maven { setUrl("https://maven.aliyun.com/repository/central") }
		maven { setUrl("https://maven.aliyun.com/repository/public") }
		maven { setUrl("https://maven.aliyun.com/repository/jcenter") }
		maven { setUrl("https://maven.aliyun.com/repository/google") }
		maven { setUrl("https://maven.aliyun.com/repository/releases") }
		maven { setUrl("https://maven.aliyun.com/repository/snapshots") }
		maven { setUrl("https://maven.aliyun.com/repository/gradle-plugin") }
		maven { setUrl("https://jitpack.io") }
		maven { setUrl("https://tencent-tds-maven.pkg.coding.net/repository/shiply/repo") }
		maven { setUrl("https://mirrors.tencent.com/nexus/repository/maven-public") }
		google()
		mavenCentral()
		maven { setUrl("https://plugins.jetbrains.com/maven") }
		maven {
			url = uri("https://maven.pkg.github.com/dyshandy/IrregularLayoutView")
			credentials {
				username = System.getenv("GITHUB_USERNAME")
				password = System.getenv("GITHUB_TOKEN")
			}
		}
	}
}
rootProject.name = "IrregularLayoutView"
include(":IrregularLayoutView")
include(":app")
 