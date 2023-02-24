# *Mappings and model change - Simplify FpML Synonym Hierarchy*

_Background_

This release will simplify and rationalise the FpML synonym hierarchy and versions. This will make it easier for mappers to work on synonyms to map FpML documents into CDM objects.

_What is being released?_

The following two mapping files are being renamed:

- `mapping-fpml-synonym.rosetta` to `mapping-fpml-confirmation-tradestate-synonym.rosetta`
- `mapping-fpml-process-synonym.rosetta` to `mapping-fpml-confirmation-workflowstep-synonym.rosetta`

The following synonym groups within these files are being renamed:

- `FpML_5_10_Processes` to `FpML_5_Confirmation_To_WorkflowStep`
- `FpML_5_10` to `FpML_5_Confirmation_To_TradeState`


All synonym groups related to specific versions such as `5.10`, `5.12` and `5.13` have been removed. The above groupings will now contain synonyms for all FpML 5 versions.

_Review Directions_

In the CDM Portal, select the Textual Browser to inspect the synonym source groups mentioned above.

