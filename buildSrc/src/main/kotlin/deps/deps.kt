@file:Suppress("ClassName")

package deps

val oolong = dependency("org.oolong-kt", "oolong", "2.1.0")

object android {
    object gradle : Group("com.android.tools.build", "4.2.0-alpha13") {
        val plugin = artifact("gradle")
    }
}

object androidx : Group("androidx", "") {
    object appcompat : Group("$groupId.appcompat", "1.3.0-alpha02") {
        val core = artifact("appcompat")
    }

    object compose : Group("$groupId.compose", "1.0.0-alpha04") {
        object foundation : Group("$groupId.foundation", version) {
            val core = artifact("foundation")
            val layout = artifact("foundation-layout")
        }

        object material : Group("$groupId.material", version) {
            val core = artifact("material")
        }

        object runtime : Group("$groupId.runtime", version) {
            val core = artifact("runtime")
        }

        object ui : Group("$groupId.ui", compose.version) {
            val tooling = artifact("ui")
        }
    }

    object ui : Group("$groupId.ui", compose.version) {
        val tooling = artifact("ui-tooling")
    }
}

object kotlin : Group("org.jetbrains.kotlin", "1.4.10") {
    object gradle {
        val plugin = artifact("kotlin-gradle-plugin")
    }

    object test {
        val junit5 = artifact("kotlin-test-junit5")
        val jvm = artifact("kotlin-test")
    }
}

object square : Group("com.squareup", "") {
    object moshi : Group("$groupId.moshi", "1.10.0") {
        val core = artifact("moshi")
        val kotlin = artifact("moshi-kotlin")
    }
}