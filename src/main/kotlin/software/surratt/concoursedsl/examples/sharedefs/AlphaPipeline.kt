package software.surratt.concoursedsl.examples.sharedefs

import io.poyarzun.concoursedsl.domain.Pipeline
import io.poyarzun.concoursedsl.domain.Resource
import io.poyarzun.concoursedsl.domain.Step
import io.poyarzun.concoursedsl.dsl.generateYML
import io.poyarzun.concoursedsl.dsl.invoke
import io.poyarzun.concoursedsl.dsl.job

private val AlphaSourceCode = Resource(
        "alpha-sourcecode",
        "git",
        source = mapOf(
                "uri" to "git@github.com:exampleproject/alpha.git",
                "branch" to "master",
                "private_key" to "((github-key))"
        )
)

/**
 *
 */
val AlphaPipeline = Pipeline().apply {


    resourceTypes.add(CloudfoundryCliResourceType)
    resources.addAll(
            mutableListOf(
                    CloudFoundryDev,
                    BlueGreenStage,
                    BlueGreenProd,
                    AlphaSourceCode,
                    PipelineSourceCode)
    )

    job("dev") {
        plan {
            get(PipelineSourceCode.name) {
                trigger = true
            }
            get(AlphaSourceCode.name) {
                trigger = true
            }
            task("build") {
                file = "pipeline/tasks/build.yml"
                inputMapping = mapOf(
                        "source-code" to AlphaSourceCode.name,
                        "pipeline" to PipelineSourceCode.name
                )
            }
            put(CloudFoundryDev.name) {
                params = mapOf(
                        "manifest" to "build/manifest.yml"
                )
            }
        }

        onFailure = Step.PutStep("email alert",
                EmailAlertResource.name,
                params = mapOf(
                        "subject_text" to "Dev build failed: \${BUILD_PIPELINE_NAME}/\${BUILD_JOB_NAME}/\${BUILD_NAME}",
                        "body_text" to "Dev build failed: \${ATC_EXTERNAL_URL}/teams/main/pipelines/\${BUILD_PIPELINE_NAME}/jobs/\${BUILD_JOB_NAME}/builds/\${BUILD_NAME}"
                )
        )

    }


    job("stage") {
        plan {
            get(PipelineSourceCode.name) {
                trigger = true
            }
            get(AlphaSourceCode.name) {
                trigger = true
            }
            task("build") {
                file = "pipeline/tasks/build.yml"
                inputMapping = mapOf(
                        "source-code" to AlphaSourceCode.name,
                        "pipeline" to PipelineSourceCode.name
                )
            }
            put(BlueGreenStage.name) {
                params = mapOf(
                        "manifest" to "build/manifest.yml"
                )
            }
        }

        onFailure = Step.PutStep("email alert",
                EmailAlertResource.name,
                params = mapOf(
                        "subject_text" to "Stage build failed: \${BUILD_PIPELINE_NAME}/\${BUILD_JOB_NAME}/\${BUILD_NAME}",
                        "body_text" to "Stage build failed: \${ATC_EXTERNAL_URL}/teams/main/pipelines/\${BUILD_PIPELINE_NAME}/jobs/\${BUILD_JOB_NAME}/builds/\${BUILD_NAME}"
                )
        )

    }

    job("prod") {
        plan {
            get(PipelineSourceCode.name) {
                trigger = true
            }
            get(AlphaSourceCode.name) {
                trigger = true
            }
            task("build") {
                file = "pipeline/tasks/build.yml"
                inputMapping = mapOf(
                        "source-code" to AlphaSourceCode.name,
                        "pipeline" to PipelineSourceCode.name
                )
            }
            put(BlueGreenProd.name) {
                params = mapOf(
                        "manifest" to "build/manifest.yml"
                )
            }

        } // end plan

        onFailure = Step.PutStep("email alert",
                EmailAlertResource.name,
                params = mapOf(
                        "subject_text" to "Prod build failed: \${BUILD_PIPELINE_NAME}/\${BUILD_JOB_NAME}/\${BUILD_NAME}",
                        "body_text" to "Prod build failed: \${ATC_EXTERNAL_URL}/teams/main/pipelines/\${BUILD_PIPELINE_NAME}/jobs/\${BUILD_JOB_NAME}/builds/\${BUILD_NAME}"
                )
        )

    } // end prod job

}

fun main(args: Array<String>) {
    println(generateYML(AlphaPipeline))
}