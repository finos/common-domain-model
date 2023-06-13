---
title: CDM Java Distribution Guidelines
---

This section provides directions for downloading and using the Java
version of CDM. Topics covered are listed below:

-   Prerequisites
-   Introduction
-   Setting Up Google\'s Guice Injector
-   Generating Global Keys and Qualifications
-   Validating the CDM instance

# Prerequisites

-   Java SDK 8

# Introduction

-   The CDM in Java is built using [maven](https://maven.apache.org)(#) and is published using the
    REGnosys artifactory. The repository can be accessed using the maven
    settings file that is included in the cdm sources jar or by copying
    the repository information to your own settings.
-   The CDM in Java code, a related set of examples, and a video
    tutorial accessing the code and loading the examples are available
    in the [CDM Portal](https://portal.cdm.rosetta-technology.io)(#):

Download page for zip files of the Java code and examples

![](/img/cdm-portal.png)

-   

    Direct links:

    :   [FINOS CDM as Java Code](#)

        [FINOS CDM as Java Examples](#)

Tutorials

![](/img/cdm-tutorials.png)

Direct link: [Tutorials](#)

-   The CDM model objects are classified into namespaces (cdm.base,
    cdm.base.staticdata, etc). These namespaces translate into Java
    packages with the same name, with each package containing a
    package-info file.

![](/img/cdm-distribution.png)

-   The CDM uses [builder pattern](#) for each of the pojos. The
    distribution ships with the json to java object serialisers.

# Setting Up Google\'s Guice Injector

CDM uses [Google\'s Guice](#) as a dependency manager. Injector is the
core of Guice that contains the whole object graph (context).

The first step is to initialise the injector. There are two options:

## Initialising the Injector, Option 1: Using provided CdmRuntimeModule

The CDM distribution comes with a pre-built CDM module that can be used
to create an injector. It provides bindings to required classes
(ModelObjectValidator and QualifyFunctionFactory) as well as binding in
implementations for several CDM functions such as Abs, Sum

``` Java
Injector injector = Guice.createInjector(new CdmRuntimeModule()));
```

## Initialising the Injector, Option 2: Build your own Module

To build a custom injector that is not based on the CDM\'s runtime
module, first create a Guice module with a minimum of the two bindings
shown belows:

``` Java
public class GenericModule extends AbstractModule {

  @Override
  protected void configure() {
    bind(ModelObjectValidator.class).to(RosettaTypeValidator.class);
    bind(QualifyFunctionFactory.class).to(QualifyFunctionFactory.Default.class);
  }
}
```

Once this module has been built it can be used to create the custom
injector.

``` Java
Injector injector = Guice.createInjector(new GenericModule()));
```

# Generating Global Keys and Qualifications

Within the model any data object marked with metadata key will have a
Global Key generated when that data object is populated. These Global
Keys are automatically generated using hash algorithms. The model
objects can be post-processed with Global Keys and qualified by using
the injector created in the previous step to run the code shown below:

``` Java
Contract cdmInstance = buildCdmInstance();
Contract.ContractBuilder builder = cdmInstance.toBuilder();
keyProcessor.runProcessStep(Contract.class, builder);
Contract updatedCdmInstance = builder.build();
```

# Validating the CDM instance

In order to validate the CDM instance, it is necessary to create a
RosettaTypeValidator and post process the instance as follows:

``` Java
RosettaTypeValidator validator = injector.getInstance(RosettaTypeValidator.class);
ValidationReport validationReport = validator.runProcessStep(cdmInstance.getClass(), cdmInstance.toBuilder());
if (validationReport.success()) {
  List<ValidationResult<?>> validationResults = validationReport.validationFailures();
}
```

If the validation is unsuccessful then the validation results object
will contain the list of all the validation failures.
