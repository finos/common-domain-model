# *CDM Examples - Extended test coverage*

_Background_

This release updates the `examples` project improving the test scenario coverage for several Common Domain Model Java features. All extended scenarios use publicly available CDM Test Pack samples.

_What is being released?_

This release adds the following examples scenarios:

**Processor scenarios**
- `org.finos.cdm.example.processors.QualificationProcessorTests` with different qualification report expectation scenarios
- `org.finos.cdm.example.processors.ValidationProcessorTests` with different validation report expectation scenarios

**Qualification scenarios**
- `org.finos.cdm.example.qualification.QualifyBusinessEventTest` with `PartialTermination`, `ContractFormation`, `PartialNovation`
- `org.finos.cdm.example.qualification.QualifyProductTest` with `InterestRate_Fra`, `InterestRate_Option_Swaption`, `InterestRate_CrossCurrency_FixedFloat`, `InterestRate_CapFloor`, `InterestRate_IRSwap_FixedFloat`, `CreditDefaultSwap_SingleName`, `CreditDefaultSwap_Index`, `CreditDefaultSwap_Basket`, `EquitySwap_TotalReturnBasicPerformance_SingleName`, `EquitySwap_ParameterReturnVariance_SingleName`

**WorkflowStep transition scenarios**
- `org.finos.cdm.example.BusinessEventExecutionTest` with business event execution tests and instruction creation mocks for `ContractFormation`, `Execution`, `Novation`, `Increase`, `Decrease`, `Termination`, `Reset`, `Valuation`, `OptionExercise`, `Transfer`, `TermsChange`, `StockSplit`, `Allocation`, `CorporateAction`, `CreditEvent`, `Compression`, `Clearing`, ``, ``, ``, ``, ``, ``, ``, ``, ``, ``, ``

**Performance Metrics**
- `org.finos.cdm.example.performance.ProcessorPerformanceTests` with deserialization, object validation, qualification, and state transition metrics at product and event level.

_Review Directions_

Please inspect the changes identified above for the test scenarios in the `examples` module.

The coverage for this release can also be reviewed in PR [#3299](https://github.com/finos/common-domain-model/pull/3299).

The full scope description of the proposal can be found at [#3298](https://github.com/finos/common-domain-model/issues/3298).
