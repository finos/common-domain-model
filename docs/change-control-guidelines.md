## Change Control Guidelines

This section discusses how changes to the CDM are controlled within and between releases, in particular:

* Principles
  * What we are trying to achieve with the change control guidelines; 
  * What constraints/objectives we have for putting these guidelines in place
* Rules
  * The specific rules we want to define and enforce to meet the principles
* Evaluation methods
  * How we want to ensure that the rules are evaluated and enforced during development
  * This includes development processes (e.g. review and approval) as well as automated tooling (e.g. regression test cases)

## Change Control Principles

* We are trying to ensure rapid, smooth, and predictable evolution of the model by controlling when and how breaking changes are introduced
  * We want to allow changes where needed, with defined process to make those changes, to meet evolved and improved understanding of the business and technical requirements.
  * We want to give ourselves some freedom to make changes more easily when there are newly introduced components/structures that may not be fully mature, but we don’t want to spend a lot of effort on planning for that. We will do this using the pull request approval guidelines for bug fixes, giving some scope for correcting recently introduced changes.
* Prohibiting breaking changes within a major version should allow users to upgrade to minor versions more quickly and easily, and plan for when to implement larger changes
  * By  limiting and control the amount of change to key business models and technology structures,  CDM users can have confidence that functionality they develop using CDM will continue to work with new versions of CDM with minimal effort, at least for a defined period of time

## Change Control Rules

* Unless explicitly indicated otherwise, components of CDM (such as data types and functions) will be under change control once released into production.
* Within multiple minor releases of a single major release, the following will not be changed:
  * Within business objects, any object that is valid in version M.N should be representable and valid in version M.N+1 .
    * For example, existing data fields may not be changed in type, reduced in cardinality, or removed, and new mandatory data fields may not be added.
    * Change to the name of any model element (e.g. types, attributes, enums, functions or reporting rules) is prohibited
    * Change to the DSL that results in any existing expression becoming invalid is prohibited
    * All validations that pass in version M.N should also pass in version M.N+1
  * Function signatures may not be changed in such a way as to invalidate previous callers (e.g addition of new mandatory parameters, or removal/change of existing parameters.)
    * Change to the DSL that results in change to any of the generated code's public interfaces is prohibited
  * Test cases that passed in a prior version shall continue to work.
  * We allow some minor exceptions to these rules for newly introduced functionality that may not be fully formed, as part of the PR process for defect corrections
  * Functionality shall not be removed between major versions without advance notice


Please note that full, bidirectional interoperability between minor versions is not required.  If an application uses functionality in version M.N, it does not need to fully interoperate with version M.N-1, assuming that the older version does not include that functionality.  However, if an application uses functionality found in version M.N, it should be able to interoperate with version M.N+1.

## Change Control Evaluation and Enforcement

* Designers and contributors to CDM are responsible for being aware of and following the change control guidelines.  This includes flagging pull requests when they involve breaking changes to controlled objects.
  * Backward incompatible changes shall be documented and include a migration guide (remap from old structures and functions to the new)
* Reviewers will be responsible for assessing (“double checking”) whether any changes may violate the change control guidelines, and flag questionable changes for further review.  
  * Part of the role of the Contribution Review Working Group (CRWG) and of the maintainers is to enforce these guidelines for any change.
* There will be a set of regression test cases developed for each supported major version.  Subsequent CDM minor and major versions will be tested against these test cases and a report prepared indicating which cases succeed and fail, and this will be compared against the guidelines.  For example:
  * CDM version 6.2 will be tested against the 6.1 test cases; all should succeed, unless included in the exception/noncontrolled list.
  * CDM version 6.0 will be tested against the latest 5.x test cases; the list of failures should be compared against the approved scope of change for 6.0.  (NB: performing this test might involve making some technical changes to the 5.0 test cases to work with the 6.0 technical architecture if that has changed, but the functionality should not otherwise be changed.)
 
## Backward Compatibility

Like other types of software, *backward compatibility* in the context of
a domain model means that an implementor of that model would not have to
make any change to update to such version.

-   Prohibited changes:
    -   Change to the structure (e.g. the attributes of a data type or
        the inputs of a function) or removal of any model element
    -   Change to the name of any model element (e.g. types, attributes,
        enums, functions or reporting rules)
    -   Change to any condition or cardinality constraint that makes
        validation more restrictive
    -   Change to the DSL that results in any existing expression
        becoming invalid
    -   Change to the DSL that results in change to any of the generated
        code's public interfaces
-   Allowed changes:
    -   Change that relaxes any condition or cardinality constraint
    -   Change to any synonym that improves, or at least does not
        degrade, the mapping coverage
    -   Addition of new examples or test packs
    -   Change to the user documentation or model descriptions
    -   Addition of new data types, optional attributes, enumerations,
        rules or functions that do not impact current functionality

Exceptions to backward compatibility may be granted for emergency bug
fixes following decision from the relevant governance body.

