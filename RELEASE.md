# ISDA Create Ingestion Mappings

_What is being released_

- Added `ISDA Create` synonym mappings for `LegalAgreementBase.partyInformation` and conditional mapping for `csdInitialMargin2016EnglishLaw`, `csaInitialMargin2016JapaneseLaw` and `csaInitialMargin2016NewYorkLaw` attributes ensure only 2016 agreements are mapping (e.g. not 2018 agreements).

_Review Directions_

In the Ingestion Panel, see:
- ISDA-Create > any file, following ingestion see that `partyInformation` and `csdInitialMargin2016EnglishLaw`/ `csaInitialMargin2016JapaneseLaw`/`csaInitialMargin2016NewYorkLaw` are now mapped.
