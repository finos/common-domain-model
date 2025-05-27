# _Event Model - Enrichment of Corporate Action_

_Background_

As of today there is already an existing component in CDM to describe **Corporate Action** under Event model, that is `CorporateAction`.
However, the current list of attributes is quite poor with regards to business expectation, meaning that **some information is currently missing**, when making a gap analysis between `CorporateAction` and the set of information being actually required e.g. "record date", "adjustment factor", "bespoke description", and other ones.

_What is being released?_

This release enriches the description of **Corporate Action** with new attributes to represent the missing information, in order to ensure that `CorporateAction` provides an exhaustive representation, in line with the business expectations

- four new values are added in the list of existing type `CorporateActionTypeEnum` :

   - `BankruptcyOrInsolvency`
   - `IssuerNationalization`
   - `Relisting`
   - `BespokeEvent`

- a set of attributes is added to `CorporateAction` ; all of them have optional cardinality (0..1) :

   - `recordDate date` (existing type)
   - `annoucementDate date` (existing type)
   - `informationSource InformationSource` (existing type)
   - `dividendObservation PriceSchedule` (existing type)
   - `bespokeEventDescription string` (existing type)
   - `adjustmentFactor AdjustmentFactor` (**new type** : see details below)

- **new type** `AdjustmentFactor` : to populate the adjustment factor value, as well as to describe the resolvable terms required when calculating an adjustment factor, depending the type of Corporate Action at stake (merger, split, etc.) :

   - compulsory attribute : `value number`  (existing type) : to represent the multipler value applied to the price of the underlier impacted by a Corporate Action. this attribute is compulsory
   - optional attribute : `calculation` `PriceAdjustmentFactorCalculationTerms` (**new type** : see details below)

- **new type** `PriceAdjustmentFactorCalculationTerms` : to represent the input terms involved in the calculation of the adjustment factor applied to the price of the underlier impacted by a Corporate Action ; all attributes are optional, given that the need for populating such terms, depends on the event type of the Corporate Action, and new conditions are added to ensure consistency in the choices that can be made :

- some attributes re-use existing types :
   - `number` (examples: `shareForShareRatio`, `shareForRightsRatio`, `dividendRatio`)
   - or `Price` (example: `rightsSubscriptionPrice`)
   - or `string` (example: `bespokeCalculationFormula`)

- other ones are using new type, such as `SpinOff`, `Merger` and `AccrualFactor` which in turn are-use existing types:
   - example with `SpinOff` attributes : `Security` is re-used by both `parentCompany` and `childCompany`
   - Price is re-used by both `parentPriceObservation` and `childPriceObservation`

_Review Directions_

Changes can be reviewed in PR [#3642](https://github.com/finos/common-domain-model/pull/3642)
