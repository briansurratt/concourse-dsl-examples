package software.surratt.concoursedsl.examples.sharedefs

import io.poyarzun.concoursedsl.domain.ResourceType

// this file contains the resource types shared across multiple pipelines

// https://github.com/nulldriver/cf-cli-resource
val CloudfoundryCliResourceType = ResourceType(
        "CloudfoundryCliResourceType",
        "docker-image",
        source = mapOf(
                "repository" to "nulldriver/cf-cli-resource",
                "tag" to "latest"
        )
)

// https://github.com/pivotal-cf/email-resource
val EmailResourceType = ResourceType(
        "EmailResourceType",
        "docker-image",
        source = mapOf(
                "repository" to "pcfseceng/email-resource",
                "tag" to "latest"
        )
)
