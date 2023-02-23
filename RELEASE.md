# *CDM Distribution - Licence Terms - Minor typo corrections*

_Background_

The URL pointing to the CDM Portal link is broken in the Licence Terms and 4.0.0-DEV.0 appears as 4.0.0-DEV

_What is being released?_

The URL pointing to the CDM portal has been adjusted on the Licence Term. The reference to the Licence Terms has also been updated to 4.0.0-DEV.0

_Review Directions_

- In the CDM Portal, download a CDM distribution and inspect that the link to the CDM portal in Licence document point to the main CDM portal page.
- In the CDM Portal, open the Licence Terms licence and inspect that is shown correctly as 4.0.0-DEV.0.

# *Mappings and model change - isApplicable attribute for AutomaticExercise*

_Background_

This release adds an element isApplicable inside AutomaticExercise, previously approved by the ARC.

_What is being released?_

In data type `AutomaticExercise`:

- Attribute `isApplicable` of data type `AutomaticExercise` added in the CDM Model.
- Mappings around `AutomaticExercise` were updated.

_Review Directions_

In the CDM Portal, select the Textual Browser to inspect the types mentioned above and review the changes.
In the CDM Portal, select Ingestion and review the following samples in the test pack: 

* `fpml-5-10 > incomplete-products > equity-options` 
* `fpml-5-10 > incomplete-products > commodity-derivatives` 
