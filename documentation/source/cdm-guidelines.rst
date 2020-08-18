CDM Java Distribution Guidelines
================================

This guide provides directions for downloading the Java version of CDM.  Topics covered are listed below:

* Prerequisites
* Introduction
* How To: Setup Google's Guice Injector
* How To: Generate Global Keys and Qualifications
* How To: Validate the CDM instance

Prerequisites
-------------

* Java SDK 11

1. Introduction
---------------

* The CDM in Java is built using `maven <https://maven.apache.org>`_ and is published using the REGnosys artifactory.

* The CDM in Java code, a related set of examples, and a video tutorial accessing the code and loading the examples are available on the     latest version of the `ISDA CDM Portal <https://portal.cdm.rosetta-technology.io/#>`_:

Download page for zip files of the Java code and examples

.. figure:: screenshots/cdm-portal.png

* Direct links:
    `ISDA CDM as Java Code <https://isda:isda@regnosys.jfrog.io/regnosys/libs-snapshot/com/isda/cdm-distribution>`_

    `ISDA CDM as Java Examples <https://isda:isda@regnosys.jfrog.io/regnosys/libs-snapshot/com/regnosys/isda-cdm-examples>`_

Tutorials

.. figure:: screenshots/cdm-tutorials.png

Direct link: `Tutorials <https://vimeo.com/359012532>`_

* The CDM model objects are classified into namespaces (cdm.base, cdm.base.staticdata etc). These namespaces translate into Java packages with the same name, with each package containing a package-info file.

.. figure:: screenshots/cdm-distribution.png

* The CDM uses `builder pattern <https://en.wikipedia.org/wiki/Builder_pattern>`_ for each of the pojos. The distribution ships with the json to java object serialisers.

2 How To: Setup Google's Guice Injector
---------------------------------------

CDM uses `Google's Guice <https://github.com/google/guice>`_ as a dependency manager. . Injector is the core of Guice that contains the whole object graph (context).

The first step is to initialise the injector. There are 2 options:

2.1 Option 1: Using provided CdmRuntimeModule
"""""""""""""""""""""""""""""""""""""""""""""

The CDM distribution comes with a pre-built CDM module that can be used to create an injector.

.. code-block:: Java

    Injector injector = Guice.createInjector(new CdmRuntimeModule()));

2.2 Option 2: Build your own Module
"""""""""""""""""""""""""""""""""""

To build a custom injector that is not based on the CDM's runtime module, first create a Guice module with at least the following 2 bindings, as follows:

.. code-block:: Java

    public class GenericModule extends AbstractModule {

      @Override
      protected void configure() {
        bind(ModelObjectValidator.class).to(RosettaTypeValidator.class);
        bind(QualifyFunctionFactory.class).to(QualifyFunctionFactory.Default.class);
      }
    }

Once this module has been built it can be used to create the custom injector.

.. code-block:: Java

    Injector injector = Guice.createInjector(new GenericModule()));

3 How To: Generate Global Keys and Qualifications
-------------------------------------------------

Within the model any data object marked with `metadata key` will have a global key generated when that data object is populated. These global keys are automatically generated using hash algorithms.

In order to post process the model objects with Global Keys, and qualify them, the steps below should be followed:

Using the injector created in the previous step,  generate Global Keys and Run Qualifications.

.. code-block:: Java

 Contract cdmInstance = buildCdmInstance();
 Contract.ContractBuilder builder = cdmInstance.toBuilder();
 keyProcessor.runProcessStep(Contract.class, builder);
 Contract updatedCdmInstance = builder.build();


4 How To: Validate the CDM instance
-----------------------------------

In order to validate the CDM instance, it is necessary to create a RosettaTypeValidator and post process the instance as follows:

.. code-block:: Java

 RosettaTypeValidator validator = injector.getInstance(RosettaTypeValidator.class);
 ValidationReport validationReport = validator.runProcessStep(cdmInstance.getClass(), cdmInstance.toBuilder());
 if (validationReport.success()) {
   List<ValidationResult<?>> validationResults = validationReport.validationFailures();
 }


If the validation is unsuccessful then the validation results object will contain the list of all the validation failures.
