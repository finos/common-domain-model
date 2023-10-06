# _Product Model - Refactoring of Extraordinary Events and Substitution Provisions_

_Background_

The description and determination of extraordinary events and substitution provisions do not pertain to economic terms of financial products. They should be positioned within the legal agreement that conditions the performance of the transaction. 

Accordingly, the representation of legal `Agreement` needs to be enhanced with additional terms at transaction level with  `TransactionAdditionalTerms`. This new data type will encompass the data effectively used to draft OTC Trade Long Form Confirmations. For each asset class the data set will describe:
1.	`ExtraordinaryEvents`, as previously an attribute of `EconomicTerms`,
2.	`DeterminationRolesAndTerms`, representing the roles of the parties determining the occurrence of the extraordinary events
3.	UnderlierSubstitutionProvision, describing the terms of a substitution of the underlier(s)

The `EquityAdditionalTerms` was designed per the ISDA Equity Derivatives Definitions 2002.  The `foreignExchangeAdditionalTerms` has been released mainly per the 1998 FX and Currency Option Definitions.  The  `commoditiesAdditionalTerms`, `interestRateAdditionalTerm`, `digitalAssetAdditionalTerms` have been created as placeholder strings, until related terms are digitized as well in CDM.

_What is being released?_

The description of Extraordinary events has been abstracted from the economic terms and the `TransactionAdditionalTerms` data type has been added as a new attribute of `Agreement`, with the following attributes:
1.	`equityAdditionalTerms` – attributes : extraordinaryEvents (prior existing type, removed from EconomicTerms), `determinationTerms and substitutionProvisions
2.	`foreignExchangeAdditionalTerms` – attributes : `disruptionEvents` and `determinationTerms`
3.	`commoditiesAdditionalTerms` – string type (“place holder” object)
4.	`interestRateAdditionalTerms` – string type (“place holder” object)
5.	`digitalAssetAdditionalTerms` – string type (“place holder” object)

`SubstitutionProvisions` is an optional item, mainly made of a role definition by re-using again `CounterpartyRoleEnum` with label `whoToSubstitute`, and the description of the substitution provisions as such is of type string.

A new generic type `Clause` has been used to introduce to document digitally bespoke Terms, that cannot be modelled for now in CDM but are critically needed to foster CDM implementation.
This type has been used for the following new attribute:
1.	`ExtraordinaryEvents` -> `additionalBespokeTerms`
2.	`ExtraordinaryEvents` -> `additionalDisruptionEvents` -> `additionalBespokeTerms`
3.	`UnderlierSubstitutionProvision` -> `substitutionBespokeTerms`

The description of certain existing data type has been simplified to remove the overlaps with existing ISDA documents, for example `AdditionalDisruptionEvents`.

The values of the Enum `ContractualDefinitionsEnum` have been aligned with the title of the documents they are referring to. The corresponding FpML synonym mapping have been adjusted.

References to the Emerging Markets Traders Association and the Foreign Exchange Committee have been added to the `LegalAgreementPublisherEnum`.

The FpML synonym mapping for extraordinary events and sub-components have been removed temporarily and will be reintroduced in a second part.

_Review directions_

In the CDM Portal, select the textual view or the graphical representation and inspect the structural definitions of the data type mentioned above.

PR: https://github.com/finos/common-domain-model/pull/2426
