# *Legal Agreement Model - Extraordinary Events and Substitution Clauses*

_Background_

The representation of operational clauses for extra-ordinarary events and substitutions terms have been repositioned from the description of the economic terms to a new type `TransactionAdditionalTerms` inserted as an attribute of the legal `Agreement` that characterises the contract details of a `Trade`. The representation of these clauses in the legal agreement will be particularly be helpful in the digital representation of OTC Trade Long Form Confirmations.

As agreed with legal and process SMEs, the structural definitions of the `TransactionAdditionalTerms` is layed out similarly to the ISDA definition grouping. The representation of `equityAdditionalTerms` and    `foreignExchangeAdditionalTerms` have been detailled based on the ISDA definitions for equity and Fx asset classes respectively. Addditional terms associated to commodity, credit, interest rate and digital assets have been inserted as placeholders and wll be expanded in future work. 

#### _What is being released?_

- `TransactionAdditionalTerms` is created as a new attribute of the `Agreement` type, with the following features: 
    - `equityAdditionalTerms` with the corresponding extraordinary Events (prior existing type, removed from EconomicTerms), determination Terms and substitution provisions.
    - `foreignExchangeAdditionalTerms` with the corresponding disruptionEvents` and `determinationTerms`
    - `commoditiesAdditionalTerms` left as a placeholder string type
    - `interestRateAdditionalTerms` left as a placeholder string type
    - `digitalAssetAdditionalTerms` left as a placeholder string type

- The determination terms are described by the type `DeterminationRolesAndTerms`. It relies on  `DeterminationRoleEnum`, with the parties role i.e. `CalculationAgent`, `HedgingParty`, `DeterminingParty`. TThe determinating party and the disputing party are annotated with the enum `CounterpartyRoleEnum`. 

- The substitution provisions are optional item and represent the modality of the trigger events, who is to Substitute and the description of the substitution provisions which for now are represented with a `AdditionalBespokeTerm` data type.

- The labels referencing the various ISDA definitions in `ContractualDefinitionsEnum` have been adjusted to folllow the same pattern. The corresponding FpML synonym mappings have been adjusted.
  
- The following publisher of legal agreement have been added in the `LegalAgreementPublisherEnum`: EMTA, TheFXCommittee and ISDAClearstream

- A new type `AdditionalBespokeTerm` was added to allow fo the association of a specific name to a set of bespoke terms.
  
For clarity, whenever some attribute of type string have been added created or refactored as part of this release, the name of that attribute was made explicit in that regards by including the annotation “bespoke” in their name. For instance :
- `extraordinaryEvents`->`additionalBespokeTerms`
- `determinationTerms`->`fallbackLanguageBespokeTerms`
- `substitutionProvisions`->`substitutionBespokeTerms`

#### _Review directions_

In the CDM Portal, select the Textual Browser and search for the model elements mentioned above.
