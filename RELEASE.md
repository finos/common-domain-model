# Math Model - New DSL Core Mathematical Functions

### Background

To support complex financial calculations new CDM fundamental mathematical capabilities are required. This release bridges that gap by providing native DSL implementations for exponential, natural logarithm, and square root operations.

For further information, see issue [#4169](https://github.com/finos/common-domain-model/issues/4169).

### What is being released?

This release introduces core mathematical function definitions within the `cdm.base.math` namespace. These functions utilize standard numerical analysis techniques adapted specifically for the Rune DSL to ensure deterministic execution without external dependencies.

New functions include:

`base-math-fuc.rosetta`

* `Exp`: The exponential function, uses a 10th-degree Taylor-Maclaurin series expansion with argument reduction.
* `Log`: The natural logarithm, uses the Inverse Hyperbolic Tangent series expansion up to degree 19 with argument scaling.
* `Sqrt`: The square root function, approximates using the Newton-Raphson method with a logarithmic initial-guess ladder.

`base-math-util-func.rosetta`

* `Exp_Pos_Router` & `Exp_Squaring_Helper`: Manage argument reduction and repeated squaring to keep inputs within a numerically stable sweet spot.

* `Exp_Core_Taylor` & `Log_Internal_Series`: Execute the 10th-degree Taylor-Maclaurin and 19th-degree Inverse Hyperbolic Tangent series, respectively.

* `Functional Summations (reduce)`: Both series approximations have been optimized to utilize the native Rune DSL reduce operator (e.g., [t1, t3...] reduce a, b [ a + b ]). This allows for concise, functional summations of the expansion terms.

Verification and Testing:

The following JUnit scenarios have been added to validate the mathematical properties, series approximation accuracy, and boundary conditions:

* `cdm.base.math.functions.ExpTest`
* `cdm.base.math.functions.LogTest`
* `cdm.base.math.functions.SqrtTest`

As these are new functions, there are no backward compatibility issues or impacts on existing model logic.

### Review Directions

In Rosetta, select the Textual Browser and inspect the new definitions in `cdm.base.math`.
In IDE, select the file:

* `rosetta-source/src/main/rosetta/base-math-func.rosetta` to inspect the new symbol definitions.
* `rosetta-source/src/main/rosetta/base-math-util-func.rosetta` to inspect the underlying mathematical helpers and the application of the `reduce` operator.

---
