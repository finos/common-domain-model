# _Enrich Corporate Action_

_Background_

This releases enrich the description of Corporate Action, mainly used in Event model (in root path `ObservationInstruction`), with new attributes to represent some information that has been missing so far to ensure exhaustive description of such event from business standpoint.

_What is being released?_

- four new values are added in the list of existing type `CorporateActionTypeEnum` :

    - `BankruptcyOrInsolvency`
    - `IssuerNationalization`
    - `Relisting`
    - `BespokeEvent`

- a set of attributes is added to CorporateAction. all of them have optional cardinality (0..1) :
    
   - `recordDate date` (existing type)
   - `announcementDate date` (existing type)
   - `informationSource InformationSource` (existing type)
   - `dividendObservation PriceSchedule` (existing type)
   - `bespokeEventDescription string` (existing type)
   - `adjustmentFactor AdjustmentFactor` (new type : see details below)
  
- new type `AdjustmentFactor` : to populate the adjustment factor value, as well as to describe the resolvable terms required when calculating an adjustment factor, depending the type of Corporate Action at stake (merger, split, etc.) :

     - compulsory attribute : `value number`  (existing type) : to represent the multipler value applied to the price of the underlier impacted by a Corporate Action. this attribute is compulsory
     - optional attribute : `calculation PriceAdjustmentFactorCalculationTerms` (new type : see details below)

- new type `PriceAdjustmentFactorCalculationTerms` : to represent the input terms involved in the calculation of the adjustment factor applied to the price of the underlier impacted by a Corporate Action ; all attributes are optional, given that the need for populating such terms, depends on the event type of the Corporate Action, and new conditions are added to ensure consistency in the choices that can be made :

    - some attributes are existing types `number` (example `shareForShareRatio`, `shareForRightsRatio`, `dividendRatio`) 
      - or `Price` (example `rightsSubscriptionPrice`) 
      - or `string` (example `bespokeCalculationFormula`)
      
    - other ones are using new type, such as `SpinOff`, `Merger` and `AccrualFactor` which in turn are-use existing types:
      - example with `SpinOff` attributes : `Security` is re-used by both `parentCompany` and `childCompany`
      - `Price` is re-used by both `parentPriceObservation` and `childPriceObservation`

_Review Directions_

The changes can be reviewed in PR: [#3642](https://github.com/finos/common-domain-model/pull/3642) 

