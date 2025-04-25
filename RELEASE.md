# _ Add Title_

_Background_

This change releases enrich the description of **Corporate Action**, mainly used in **Event model** (in root path `ObservationInstruction`)

- by adding new optional attributes to existing type CorporateAction, among which an important one from business standpoint, based on new type AdjustmentFactor
- also by completing existing type CorporateActionTypeEnum with values that were missing in the list

_What is being released?_

- a set of attributes is added to `CorporateAction` ; all of them have optional cardinality `(0..1)` :

    - `recordDate` date (existing type)
    - `annoucementDate` date (existing type)
    - `informationSource InformationSource` (existing type)
    - `dividendObservation PriceSchedule` (existing type)
    - `bespokeEventDescription string` (existing type)
    - `adjustmentFactor AdjustmentFactor` (new type) : see details below

- new type `AdjustmentFactor` : for specifying any additional details e.g. further descriptions depending on the particular type of Corporate Action, adjustment factor and calculations terms to resolve for calculating the adjustment factor - hencing coming with two attributes :

    - `value number`  (existing type) : to represent the multipler value applied to the price of the underlier impacted by a Corporate Action.
    - `calculation PriceAdjustmentFactorCalculationTerms` (new type) : see details below

- new type `PriceAdjustmentFactorCalculationTerms` : to represent the input terms involved in the calculation of the adjustment factor applied to the price of the underlier impacted by a Corporate Action. All of them are optional, given that need for populating depends on the event type of the Corporate Action. Accordingly, new conditions are also added to ensure consistency in the choices that can be made :

    - some attributes are existing types `number` (example `shareForShareRatio`, `shareForRightsRatio`, `dividendRatio`) or `Price` (example `rightsSubscriptionPrice`) or `string` (example `bespokeCalculationFormula`)
    - other ones are new types to enrich description of particular event type, such as `SpinOff`, `Merger`, `AccrualFactor` (example of attributes for `SpinOff`, logic being similar for other attributes : `parentCompany Security`, `parentPriceObservation Price`, `childCompany Security` and `childPriceObservation Price`)

- four new values are added in the list of existing type CorporateActionTypeEnum :

    - BankruptcyOrInsolvency
    - IssuerNationalization
    - Relisting
    - BespokeEvent

_Review Directions_

The changes can be reviewed in PR: [#3642](https://github.com/finos/common-domain-model/pull/3642) 

