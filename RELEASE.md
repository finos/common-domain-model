# Rosetta Syntax Upgrade

As part of on-going improvements, the Rosetta syntax has been upgraded to consolidate language features. This work aims to simplify the syntax needed when adding to, and editing the CDM. This will become increasingly important as the number of CDM contributors increase and as CDM contributions starting coming from a variety of sources. 

This release marks the first in a series of that will cover the scope described in this [wiki](https://github.com/REGnosys/rosetta-dsl/wiki/Rosetta-Syntax-Upgrade/31f933712fb7e3565e4cfb071f9fbb2580275920). We start by focusing on the migration from `calculation`, `function` and `spec` to `func` as these objects provide the largest benefit from consolidation. 

`func` offers a consistent syntax that can be used to express all 3 language features:
- `function`s that represents only the inputs and outputs. A function’s implementation is to be provided by the implementor
- `spec`s that define the inputs, outputs and a set of condition that must be valid for a function to have been correctly implemented
- `calculations` that are concrete and full implementations of functions. 

Examples showing the before and after of this change in relation to the above language features (and otrhers) are also available on the [wiki](https://github.com/REGnosys/rosetta-dsl/wiki/Rosetta-Syntax-Upgrade/31f933712fb7e3565e4cfb071f9fbb2580275920).

`alias` will also migrate to `func`, but will be addressed in a following release. 

The code generators have also been simplified as a result of migrating to func, which makes contributions to the open sourced code generators easier, also. 

See the Textual Browser for changes to the syntax and the Download Pack for changes to generated Java code. 
