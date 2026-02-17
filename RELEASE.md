# Math Model - New DSL Core Mathematical Functions

### Background

To support complex financial calculations new CDM fundamental mathematical capabilities are required. This release bridges that gap by providing native DSL implementations for exponential, natural logarithm, and square root operations.

For further information, see issue [#4169](https://github.com/finos/common-domain-model/issues/4169).

### What is being released?

This release introduces core mathematical function definitions within the `cdm.base.math` namespace. These functions utilize standard numerical analysis techniques adapted specifically for the Rune DSL to ensure deterministic execution without external dependencies.

New functions include:

* `Exp`: The exponential function, uses a 10th-degree Taylor-Maclaurin series expansion with argument reduction.
* `Log`: The natural logarithm, uses the Inverse Hyperbolic Tangent series expansion up to degree 19 with argument scaling.
* `Sqrt`: The square root function, approximates using the Newton-Raphson method with a logarithmic initial-guess ladder.

Verification and Testing:

The following JUnit scenarios have been added to validate the mathematical properties, series approximation accuracy, and boundary conditions:

* `cdm.base.math.functions.ExpTest`
* `cdm.base.math.functions.LogTest`
* `cdm.base.math.functions.SqrtTest`

As these are new functions, there are no backward compatibility issues or impacts on existing model logic.

### Review Directions

In Rosetta, select the Textual Browser and inspect the new definitions in `cdm.base.math`.
In IDE, select the file `rosetta-source/src/main/rosetta/base-math-func.rosetta` to inspect the new symbol definitions.

---