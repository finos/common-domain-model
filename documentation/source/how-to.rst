CDM Distribution - How to
=========================

This guide is a collection of short how-to code guides for common CDM Distribution questions.

* How to setup
* How to generate Globel Keys
* How to qualify a cdm java instance
* How to validate a cdm java instance


JAVA
-------------

Pre reqs
""""""""
* Java SDK 11

How to generate Globel Keys
"""""""""""""""""""""""""""
The setup code.

.. code-block:: Java

    Injector injector = Guice.createInjector(new CdmRuntimeModule()));

How to generate Globel Keys

.. code-block:: Java

    Contract cdmInstance = buildCdmInstance();/ / The object that you build
    PostProcessor postProcessor = injector.getInstance(PostProcessor.class);
    RosettaModelObjectBuilder postProcessedBuilder = postProcessor.postProcess(Contract.class, cdmInstance.toBuilder());
    Contract updatedCdmInstance = (Contract) postProcessedBuilder.build(); // The object that you build, with the global keys and qualifications set.

How to validate the CDM instance
""""""""""""""""""""""""""""""""

.. code-block:: Java

    RosettaTypeValidator validator = injector.getInstance(RosettaTypeValidator.class);
    ValidationReport validationReport = validator.runProcessStep(cdmInstance.getClass(), cdmInstance.toBuilder());
    if (validationReport.success()) {
     // handle failures
         List<ValidationResult<?>> validationResults = validationReport.validationFailures();
    }

The validation Results object will list all failures.
