# _Product Model - Commodity Payout - Delivery features_

_Background_

EMIR Refit requires the reporting of delivery information for European electricity and gas commodity products, both physical and financial, in the form of the following fields:

- 2.119. Delivery point or zone
- 2.120. Interconnection Point
- 2.121. Load type
- 2.122. Delivery interval start time
- 2.123. Delivery interval end time
- 2.127. Days of the week

The present release adds CDM model support for the information necessary for the reporting of these fields.

## Changes Made:
#### Updated Readme.md and fixed issues in cdm.finos.org.
- Updated Governance.md and Code of Conduct with links replaced by Terms of Reference PDFs, now located in the "docs" directory.
- Added relative links in the readme to ensure they work seamlessly after merging.
- Included Common Domain Model in the Open Source at FINOS PDF in the Readme.md section.
- Added a roadmap section for upcoming releases.
- Edited the "Getting Involved" section.
- Updated icons for cdm.finos.org.
- Updated the banner for cdm.finos.org.
- Refined the text to explain what Common Domain Model (CDM) is and its benefits.
- Included a PDF document explaining what CDM is.

- Model support for European electricity and gas commodity delivery fields, with the exception of duration, delivery capacity and price quantity interval.

_Data types_

- Added type `LocationIdentifier`.
- Added attribute `delivery` of the type `CommodityDeliveryInformation` to `CommodityPayout` type.
- Added type `CommodityDeliveryInformation`.
- Added type `CommodityDeliveryPeriods`.
- Added type `CommodityDeliveryProfile`.
- Added type `CommodityDeliveryProfileBlock`.
- Added attribute `deliveryPeriod` of the type `CommodityDeliveryPeriods` to type `SchedulePeriod`.

_Enumerations_

- Added enumeration `CommodityLocationIdentifierTypeEnum`.
- Added enumeration `LoadTypeEnum`.


_Review directions_

The changes can be reviewed in PR [#2160](https://github.com/finos/common-domain-model/pull/2160).