# _Event Model -  Enhancements for Party Role Enum - Service Provider_

_Background_

The existing party roles are not sufficient to identify if a counterparty is acting as a third-party service provider or CCP for the compression event. A new party role enum value is needed to uniquely identify a party acting as a compression service provider in the context of the business event.

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

This release provides the ability to associate a party as a compression service provider.

_Enumerations_

- Augmented the `PartyRoleEnum` enumeration with new `CompressionServiceProvider` value to represent counterparties acting as compression service providers.

_Review directions_

In the CDM Portal, select the Textual Browser and inspect the change identified above.

Inspect Pull Request: [#2369](https://github.com/finos/common-domain-model/pull/2369)