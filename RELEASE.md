# *Additional FX ingestion samples and product qualifications*

_What is being released_

* Additional FX ingestion samples have been added to demonstrate CDM support for FX Swaps, FX Options, and FX Options with Averaging Schedules.
* Qualification functions have been updated in the model to:
- support explicit qualification of Spot / Forward trades (func Qualify_ForeignExchange_Spot_Forward)
- support FX Options and other Options with Averaging Schedules (func Qualify_ForeignExchange_VanillaOption)
- support FX Swaps (e.g. Spot-Forward, Forward-Forward) (func Qualify_ForeignExchange_Swap)
* Further synonym mappings have been added to synonym-cdm-fpml to enable ingestion of the additional ingestion samples. 

_Review Directions_

In the CDM Portal, in the Ingestion section, review new and revised functions listed above, as well as the synonyms in the file noted above, and the following examples:

- product > fx > fx-ex03-fx-fwd
- product > fx > fx-ex08-fx-swap
- product > fx > fx-ex09-euro-opt
- product > fx > fx-ex10-amer-opt
- product > fx > fx-ex11-non-deliverable-option
- product > fx > fx-ex12-fx-barrier-option
- product > fx > fx-ex20-avg-rate-option-parametric
- product > fx > fx-ex22-avg-rate-option-specific
