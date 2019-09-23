_What is being released_

*Proposed model changes as part of an on-going effort to augment the product coverage with securities*

Added function `EvaluatePortfolioState` which takes `Portfolio` as an input and calculates the `PortfolioState`, including a list of `Position` based on the given `AggregationParameters`.

_Review direction_

- In the CDM Textual Browser, review function `EvaluatePortfolioState`.
- In the downloaded CDM Distribution, review sample function implementation `EvaluatePortfolioStateImpl.java` and test `EvaluatePortfolioStateTest.java`.
