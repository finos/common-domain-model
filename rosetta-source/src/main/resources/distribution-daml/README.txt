Users of the distribution are encouraged to start with the CDM documentation found in documentation/common-domain-modal/index.html to understand the organisation of the model and its components.

The ISDA Digital CDM distribution is organised into the folders below.  Please also see LICENCE.txt for licence terms.

cdm-events.xsd
The XML Schema that specifies the lifecycle events that are ingested as part of the Rosetta Ingestion service.  Can be used to validate CDM Event XML documents.

/common-domain-model
The set of files that describe the Common Domain Model.  The files use the .rosetta extension as that carries special meaning in the Rosetta DSL and CDM Portal, to view the files simply use the text editor of your choice as these are in effect, plain text files.

New starters should begin with the Trade and BusinessEvent classes as these are foundational to the CDM work done thus far.  These classes can be found in event-common-type.rosetta.

The file names in the common-domain-model folder should be considered in aggregate, as classes in one file can and do reference classes in other files.  The names of files should not be used as indication of the nature of its contents.

/documentation
Contains the CDM documentation in html format.  In the common-domain-model sub-folders there is an index.html file.  Open this file to use the documentation.  You must extract the .zip archive before your browser can assess all the files and render them correctly.

/daml
The DAML source code generated from the CDM.  To download and install the DAML SDK go to https://docs.daml.com/getting-started/installation.html?ref=rosetta.

/lib
The compiled DAML artefacts (.dar) generated from the CDM.
