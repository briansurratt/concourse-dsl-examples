package software.surratt.concoursedsl.examples.readme

/**
 * This is the code from the README.md file in the concoursedsl project
 */

import io.poyarzun.concoursedsl.domain.Pipeline
import io.poyarzun.concoursedsl.domain.Step
import io.poyarzun.concoursedsl.dsl.*

// Since the pipeline is executed at generation time, it's
// easy to use a table-driven approach
val services = mapOf(
        "mailer" to "github.com/mailer.git",
        "mint" to "github.com/mint.git",
        "third" to "github.com/third.git"
)

val customPipeline = Pipeline().apply {
    
    for ((name, repo) in services) {
        resource(name, type = "git") {
            source = mapOf("uri" to repo, "branch" to "master")
        }
    }

    job("unit") {
        plan {
            getAllRepos() {
                trigger = true
            }

            task("test") { file = "mailer/ci/test.yml" }
        }
    }

    job("build") {
        plan {
            getAllRepos() {
                trigger = true
            }
            task("build") { file = "mailer/ci/build.yml" }
        }
    }

}

// Extending the DSL is equally easy, and works well with "Extract Function" in IDEA
private fun StepBuilder.getAllRepos(additionalConfig: Step.GetStep.() -> Unit) {
    for (name in services.keys) {
        get(name, additionalConfig)
    }
}

fun main(args: Array<String>) {
    println(generateYML(customPipeline))
}