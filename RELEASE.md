# _Infrastructure - Dependency Update_

_What is being released?_

This release updates the rosetta-dsl dependency.

Version updates include:
  - 8.5.0: Introduces constructor syntax to instantiate data types and records. For further details see DSL release notes: https://github.com/REGnosys/rosetta-dsl/releases/tag/8.5.0.
  - 8.5.1: Patches including Java 8 compatibility for the generated Java code, and support for lists of basic types and enums in reports. For further details see DSL release notes: https://github.com/REGnosys/rosetta-dsl/releases/tag/8.5.1.
  - 8.5.2: Patches to improve code generation and validation of constructor expressions. For further details see DSL release notes: https://github.com/REGnosys/rosetta-dsl/releases/tag/8.5.2.

The model has been simplified by replacing construction functions (typically of the form `Create_<TypeName>`) with an appropriate constructor expression. There are no functional changes, so the test expectations stay the same.

The changes can be reviewed in PR [#2381](https://github.com/finos/common-domain-model/pull/2381).

# _Modelling Master Agreements_

_Background_

Legal documentation is required to describe the contract between parties on every transaction. The ISLA Global Master Securities Lending Agreement (GMSLA) is the standard master agreement used by the securities financing industry to cover securities lending transactions.

In 2020 ISLA created the Clause Library which extracts the business outcomes from the clauses in the GMSLA. As part of the ongoing work to digitise this legal document, in 2022 ISLA initiated a Document Digitisation Working Group in conjunction with D2LT. The focus of this working group was to codify the clauses into the CDM.

The culmination of this work is the addition of new structures to the model that will support the representation of master agreements. The approach taken has been to create a structure that is generic so that it can be used to describe any master agreement.

_Model Changes_

This PR adds new types under the existing `MasterAgreementSchedule` type which will allow the representation of legal agreement clauses with their associated election criteria. These additional criteria include clause variants and any variables that are required to define the elections agreed upon for that clause.

The types have been created to be generic and do not enforce any specific clause naming convention. The actual legal text from the document is not expected to be held in the model or in the actual data that is included in the object. The model uses clause and variant identifiers to reference textual data that is held outside of the model itself. 

The identifiers themselves can be defined by the publisher of a legal document that they would like to be supported by the model. At the moment the intention is to include the identifiers for the ISLA clauses and variants in the model in enumerated lists – in the future the [docReference] clause might be able to be used so that these identifiers can instead be held outside of the model.

_Data Types_

- Added `MasterAgreementClause`
   - Holds all data that is required to define the election made for a specific clause in the master agreement
   - The `identifier` is a unique id for this clause (see the `MasterAgreementClauseIdentifierEnum` for more details). It is associated to the publisher of the document, the document itself and the clause.
   - The `name` is an optional string which can be used to hold the name of this clause
   - The `counterparty` is optional but can be used to specify which of the counterparties on the agreement the clause applies to.
   - The `otherParty` is optional but can be used for umbrella agreements where it is necessary to define specific clauses as applicable to specific parties.
   - The `variant` defines what variant of the clause has been elected. The variant must be specified, and for certain clauses it is possible that multiple variants can be elected.
- Added `MasterAgreementClauseVariant`
  - Holds all the details of the variant of the clause that has been elected
  - The `identifier` is a unique id for this variant (see the `MasterAgreementVariantIdentifierEnum` for more details). It is associated to the publisher of the document, the document itself, the clause from that document and the variant.
  - The `name` is an optional string which can be used to hold the name of this variant
  - The `counterparty` is optional but can be used where it is necessary for a clause to assign a variant to a specific counterparty on the agreement.
  - The `otherParty` is optional and can be used to assign variants to different parties who may or may not be one of the counterparties on the agreement.
  - The optional `variableSet` array is where any additional data required to define the criteria of the election can be held (see `MasterAgreementVariableSet`). 
- Added `MasterAgreementVariableSet`
  - An array to hold any additional details that are required to define the election for a clause/variant.
  - This is presented as a name/value pair and also includes a call to itself, allowing infinite nesting capabilities. This is required as some clauses require the entry of tables of values rather than just single values.
  - Conditions are included to ensure either a name/value pair or a variableSet is represented, never both.
  - A condition to only allow two levels of nesting (i.e. support the modelling of a table of data) has also been included.
- Updated `MasterAgreementSchedule`
  - A new item `clause` of type `MasterAgreementClause` has been added

_Enumerated Lists_

- `PartyRoleEnum`
  - 3 new entries have been made to this enumerated list: BeneficialOwner, Borrower and Lender
  - Not specifically required for this development, these items were suggested in Issue #2158.
- `MasterAgreementClauseIdentifierEnum`
  - An enumerated list of clause identifiers
  - Clause identifiers are used so that we do not need to define specific types in the model to represent individual clauses.
  - The identifier is designed to be used to reference data and text that is outside of the model. This keeps copyrighted material outside of the model and also prevents large amounts of text being put into the model.
  - Identifier structure is: publisher_document_clauseId
Where:
	publisher is `legalAgreementIdentification -> publisher`
	document is `legalAgreementIdentification -> agreementName -> masterAgreementType`
	clauseId is the unique 3 digit code assigned to this clause in the Clause Library (see below note on Clause Libraries)
  - The structure uses underscores “_” to separate each component of the id. Separating out each component also makes it easier for an application to convert the id into a URI for use in an API call.
- `MasterAgreementVariantIdentifierEnum`
  - An enumerated list of variant identifiers
  - Variant identifiers are used so that we do not need to define specific types in the model to represent each variant related to all clauses.
  - The identifier is designed to be used to reference data and text that is outside of the model.
  - Identifier structure is: publisher_document_clauseId_variantId
Where:
	publisher is `legalAgreementIdentification -> publisher`
	document is `legalAgreementIdentification -> agreementName -> masterAgreementType`
	clauseId is the unique 3 digit code assigned to this clause in the Clause Library
	variantId is the unique 2 digit code assigned to this variant of the associated clause in the Clause Library (see below note on Clause Libraries)
  - The structure uses underscores “_” to separate each component of the id. Separating out each component makes it easier for an application to convert the id into a URI for use in an API call. 

For this initial contribution the full list of clause and variant identifiers for the ISLA GMSLA Clause library have been included in the enumerated lists. As a side note it is worth pointing out that there are discussions underway on how we can keep lists of data outside of the model itself.

_Clause Libraries_

The Clause Libraries for the 3 trading associations, ISLA, ISDA and ICMA, have been (will be) developed in conjunction with the same company, D2LT. We can leverage the consistency in our collective approach here to define standard identifiers that we use for the clauses and variants. 

In discussion with D2LT it was determined that a maximum of 3 digits – in conjunction with the document publisher and document name – would be required to represent a clause uniquely. This allows up to 999 clauses to be represented in a single document from a single publisher. 

Similarly, a maximum of 2 additional digits would be required to support the representation of variants for each clause. This structure will allow up to 99 different variants to be defined for a single clause.

Inspect Pull Request: [#2340](https://github.com/finos/common-domain-model/pull/2340)
