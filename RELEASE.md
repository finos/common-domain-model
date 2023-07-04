# *Legal Agreement Model - Extraordinary Events and Substitution Clauses*

_Background_

The representation of operational clauses for extraordinary events and substitutions terms have been repositioned from the description of the economic terms to a new type `TransactionAdditionalTerms`, inserted as an attribute of the legal `Agreement` that characterises the contract details of a `Trade`. The representation of these clauses in the legal agreement will be particularly helpful in the digital representation of OTC Trade Long-Form Confirmations.

As agreed with legal and process SMEs, the structural definitions of the `TransactionAdditionalTerms` are laid out similarly to the ISDA definition grouping. The representation of `equityAdditionalTerms` and    `foreignExchangeAdditionalTerms` have been detailed based on the ISDA definitions for equity and FX asset classes, respectively. Additional terms associated to commodity, credit, interest rate and digital assets have been inserted as placeholders and will be expanded in future work. 

#### _What is being released?_

- `TransactionAdditionalTerms` is created as a new attribute of the `Agreement` type, with the following features: 
    - `equityAdditionalTerms` with the corresponding extraordinary events (prior existing type, removed from EconomicTerms), determination terms and substitution provisions.
    - `foreignExchangeAdditionalTerms` with the corresponding `disruptionEvents` and `determinationTerms`
    - `commoditiesAdditionalTerms` left as a placeholder string type
    - `interestRateAdditionalTerms` left as a placeholder string type
    - `digitalAssetAdditionalTerms` left as a placeholder string type

- The determination terms are described by the type `DeterminationRolesAndTerms`. They rely on `DeterminationRoleEnum`, with the party roles i.e. `CalculationAgent`, `HedgingParty`, `DeterminingParty`. The determining party and the disputing party are annotated with the enum `CounterpartyRoleEnum`. 

- The substitution provisions for the underlier willl be optional items and will annotate whom may substitute, what the trigger events are document the substitution provisions themselves represented with an `AdditionalBespokeTerm` data type.

- The labels referencing the various ISDA definitions in `ContractualDefinitionsEnum` have been adjusted to follow the same pattern. The corresponding FpML synonym mappings have been adjusted.
  
- The following publishers of legal agreement have been added in the `LegalAgreementPublisherEnum`: EMTA, TheFXCommittee and ISDAClearstream

- A new type `AdditionalBespokeTerm` was added to allow for the association of a specific name to a set of bespoke terms.
  
For clarity, whenever an attribute of type string was added created or refactored as part of this release, the name of that attribute was made explicit in that regard by including the annotation “bespoke” in their name. For instance:
- `extraordinaryEvents`->`additionalBespokeTerms`
- `determinationTerms`->`fallbackLanguageBespokeTerms`
- `substitutionProvisions`->`substitutionBespokeTerms`

#### _Review directions_

In the CDM Portal, select the Textual Browser and search for the model elements mentioned above.
