# _CDM Model - OptionPayout Refactoring_

_Background_

In order to reduce redundancy and overcomplexity in the CDM model, a refactoring of the `OptionPayout` structure is required. The information contained in the fields inside the `optionStyle` (`americanExercise`, `europeanExercise`, and `bermudaExercise`) can be unified under a new type `ExerciseTerms`. This will reduce the redundancy of having the same information repeated under the 3 styles and improve the simplicity of the model. To distinguish whether the option is american, european, or bermuda, a new `style` enumeration is added to the model inside `ExerciseTerms`, with the values "american", "european", and "bermuda". Additionally, the `strike` field, previously under `exerciseTerms`, is moved outside and located directly under `OptionPayout`, given that it does not convey any information about the exercise terms of an option.

_What is being released?_

- The `optionStyle` is removed from the model, along with the `americanExercise`, `europeanExercise`, and `bermudaExercise` fields.
- A new `ExerciseTerms` structure is added to `OptionPayout`, containing all distinct fields found previously under the 3 exercise styles.
- A new `style` enumeration is added under `ExerciseTerms` to distinguish the style of the option.
- The structures `CancelableProvision`, `ExtendibleProvision`, and `OptionalEarlyTermination` have been modified to use `ExerciseTerms` instead of the old `americanExercise`, `europeanExercise`, and `bermudaExercise` fields.
- **Synonym mappings** have been modified to reflect these changes.

_Data types_

- Removed `OptionExercise` type.
- Removed `OptionStyle` type.
- Removed `AmericanExercise`, `EuropeanExercise`, and `BermudaExercise` types.
- `strike` attribute of type `OptionStrike` added to `OptionPayout` type.
- `exerciseTerms` attribute of type `ExerciseTerms` added to `OptionPayout` type.
- `exerciseTerms` attribute of type `OptionExercise` removed from `OptionPayout` type.
- `americanExercise`, `europeanExercise`, and `bermudaExercise` attributes of types `AmericanExercise`, `EuropeanExercise`, and `BermudaExercise` respectively removed from `CancelableProvision`, `ExtendibleProvision`, and `OptionalEarlyTermination` types.
- `exerciseTerms` attribute of type `ExerciseTerms` added to `CancelableProvision`, `ExtendibleProvision`, and `OptionalEarlyTermination` types.
-  Added new `ExerciseTerms` type with all of the distinct fields present before in `AmericanExercise`, `EuropeanExercise`, and `BermudaExercise` types.

_Enumerations_

- Added new `OptionExerciseStyleEnum` enumeration.

_Review directions_

In Rosetta, select the Textual View and inspect each of the changes identified above.

The changes can be reviewed in PR: https://github.com/finos/common-domain-model/pull/2716

# _Infrastructure - Dependency Update_

_What is being released?_

This release updates the `rosetta-bundle` dependencies.

Version updates include:
- `rosetta-bundle` 10.13.4: FpML Coding schema updated.

There are no changes to the model or test expectations.

_Review directions_

The changes can be reviewed in PR: https://github.com/finos/common-domain-model/pull/2707
