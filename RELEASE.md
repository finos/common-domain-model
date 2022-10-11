# *Product Model - Enhancements of the FpML Mapping for FX, Credit and IR Products*

_Background_

This release extends the product mapping coverage for several FX, Credit and Rates festures.

_What is being released?_

- The currency scheme supporting for FX non deliverable forwards, as well as its settlement date. 
- For FX options, the optional FpML element `soldAs` that represents whether the product was originally sold as a put or a call is now represented in the `optionType` CDM attribute.
- Mapping coverage for the strike price and the product identifier of some Credit Default Swaptions. 
- Coverage for the possible FpML values for credit seniority. 
- For rates, changes in the type of `maximumNotionalAmount` and `minimumNotionalAmount` in `MultipleExercise` from `number` to `NonNegativeQuantitySchedule`, and mapping coverage of this two elements. 
- Mapping coverage for the FpML element `floatingRateMultiplierSchedule`.
- Mapping coverage for FRA product identifier too. 
- It forces to map the adjustable date identifier into an external key. 
- Mapping coverage for payer and receiver account references. 
- Expansion of the mapping coverage to `AssetClassEnum`.
