# *CDM Model: Data Templates*

_What is being released_

This release introduces the concept of data templates to the CDM.  Data templates provide a way to store data which is common across multiple CDM objects in a single referenced template.

One of the driving use-cases for templates in the CDM is Equity Swaps.  Due to the high volume of contracts with near identical product details from Equity Financing desks.  The product details duplicated on each contract can be extracted into a single template, and each contract can then specify a reference to that template.  Each contract would only specify the unique product details, which can be merged with the template to form a fully specified object when required.

In the business domain the same is achieved via master confirmation or portfolio swap agreements which can be bespoke or standard, or via clients having standard term sheets agreed and sitting with sales desks to be used when writing all future deals (a working practice which has parallels in almost every asset class).

*Model Changes*

The annotation type `[metadata template]` has been added to the model.  This annotation indicates that a data type is eligible to be used as a template.  The designation applies to all encapsulated types in that data type.

For example, currently, the only date type in the model that has been assigned this new annotation is `ContractualProduct`.  The designation of template eligibility also applies to `EconomicTerms` which is an encapsulated type in `ContractualProduct`, and also likewise applies to `Payout` which is an encapsulated type in `EconomicTerms`.

Other than the new annotation, data templates do not have any impact on the model, i.e. no new types, attributes, or conditions.

_Review Directions_

- In the CDM Portal, use the Textual Browser to review the type `ContractualProduct` and annotation `[metadata template]`.

*Merging Utilities and Examples*

Once a template reference has been resolved, it will be necessary to merge the template data to form a single fully populated object.  This release includes code utilities, written in Java, that can merge the data from two objects into one.  These utilities can be extended by implementors to change the merging strategy to meet their requirements.

This release also includes a example to show usage of data template and the merging utilities.

_Review Directions_

- In the CDM Portal, go to the Downloads page, and download the Java Examples.  Review the example in Java class, `com.regnosys.cdm.example.template.TemplateExample`.
