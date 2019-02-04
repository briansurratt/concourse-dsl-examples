package software.surratt.concoursedsl.examples.sharedefs

import io.poyarzun.concoursedsl.domain.Resource

/**
 * The CF put resource for development deployment
 */
val CloudFoundryDev = Resource(
        "cloud-foundry-dev",
        "cf",
        source = mapOf(
                "api" to "https://api.pcf.nonprod.example.com",
                "username" to "admin",
                "password" to "admin",
                "organization" to "my-organization",
                "space" to "dev",
                "skip_cert_check" to "true"
        )
)

/**
 * The CF put resource for stage deployment
 */
val CloudFoundryStage = Resource(
        "cloud-foundry-stage",
        "cf",
        source = mapOf(
                "api" to "https://api.nonprod.pcf.example.com",
                "username" to "nadmin",
                "password" to "p455w0rd",
                "organization" to "my-organization",
                "space" to "stage",
                "skip_cert_check" to "true"
        )
)

/**
 * The CF put resource for production deployment
 */
val CloudFoundryProd = Resource(
        "cloud-foundry-prod",
        "cf",
        source = mapOf(
                "api" to "https://api.prod.pcf.example.com",
                "username" to "padmin",
                "password" to "53cr3t!",
                "organization" to "my-organization",
                "space" to "prod",
                "skip_cert_check" to "true"
        )
)

/**
 * The CF put resource for a BLUE / GREEN deployment to the stage environment.
 */
val BlueGreenStage = Resource(
        "blue-green-stage",
        // References the CloudfoundryCliResourceType resource defined in ResourceTypes.kt
        CloudfoundryCliResourceType.name,
        source = mapOf(
                "api" to "https://api.nonprod.pcf.example.com",
                "username" to "nadmin",
                "password" to "p455w0rd",
                "organization" to "my-organization",
                "space" to "stage",
                "skip_cert_check" to "true"
        )
)

/**
 * The CF put resource for a BLUE / GREEN deployment to the production environment.
 */
val BlueGreenProd = Resource(
        "blue-green-prod",
        CloudfoundryCliResourceType.name,
        source = mapOf(
                "api" to "https://api.prod.pcf.example.com",
                "username" to "padmin",
                "password" to "53cr3t!",
                "organization" to "my-organization",
                "space" to "prod",
                "skip_cert_check" to "true"
        )
)

/**
 * The resource used to send emails
 */
var EmailAlertResource = Resource(
        "email-alert",
        EmailResourceType.name,
        source = mapOf(
                "smtp" to mapOf(
                        "host" to "smtp.example.com",
                        "port" to "867",
                        "username" to "mailer",
                        "password" to "notspam"
                ),
                "from" to "alerts@exmample.com",
                "to" to "alice@example.com",
                "to" to "bob@example.com"
        )
)

/**
 * Defines the source code resource for the scripts shared by multiple pipelines
 */
val PipelineSourceCode = Resource(
        "pipeline-sourcecode",
        "git",
        source = mapOf(
                "uri" to "git@github.com:exampleproject/pipeline.git",
                "branch" to "master",
                "private_key" to "((github-key))"
        )
)

