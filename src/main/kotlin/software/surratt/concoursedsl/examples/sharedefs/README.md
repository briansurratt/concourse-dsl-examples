# Motivation
After you've set up pipelines for multiple applications, you will see multiple patterns repeats across each pipeline
* non-standard resource types 
* resource based on those resource types 
* the cloud foundry resources for each environments
* referencing credentials from a secure storage
* tasks which repeated across pipelines 

Using simple text files, there are two basic solutions to this:
* have distinct YAML files for each pipeline and duplicate the common configuration across all of the files
* have one YAML file for all the applications and share the definitions across pipelines

The first option is harder to maintain because a change must be made across all the files, possibly causing inconsistency across pipelines.  The second option will result in more consistent behavior across pipelines, but the resulting file is harder to understand and maintain because of its size and scope.

# Solution
Using the DSL, we are creating objects which represent a pipeline and its component parts.  These objects (resource types, resources, tasks, etc) could be reused across multiple pipelines. This package will provide examples of sharing the definition of these types elements across multiple pipelines

In this example, two pipelines, alpha and beta, will be built with deployments to dev, stage and production environments.  These pipelines will get code from a project specific  repository as well as a repository containing shared scripts for building the code.  If a job fails, an email will be sent out.  All of the `resource-type` and `resource` definitions used by the two pipelines will defined only once. 

* `ResourceTypes.kt` - defines the `ResourceType` objects for all of the extensions resource types used 
* `Resources.kt` - defines the `Resource` objects for all the resources used across the pipelines
    * deployment targets
    * the shared pipelinesource code repository
    * resources for blue/green deployment and sending email 

# Additional Notes
There is a fair amount of duplication inside of the `Resourses.kt` file:
* source maps for the CF and Blue/Green resources are identical for stage and prod.  This code could be refactored to extract a single object shared across the types of deployments.
* the keys of the source maps are the across target resources.  The assembly of those source maps could be abstracted into using builder or factory patterns.
* data such as URLS could be extracted to constants

The `AlphaPipeline.kt` and `BetaPipeline.kt` files are identical except for the repository references and a few objects names.  This can be addressed through templating and factories.  These will be addressed in another project.

It may be appropriate to segment `Resource` definitions to seperate files based on usage (deployment, source code, communication, etc)