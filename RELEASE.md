# *Product Model - FpML synonym mappings for CreditSeniorityEnum*

_Background_

An issue was found recently with the mapping for the EMIR field `2.143 - Seniority`. This release adds the necessary enumeration CreditSeniorityEnum in tradestate.synonym to fix the issue.

_What is being released?_

_Enumerations_

- Added mapping coverage for EMIR field `2.143 - Seniority` by adding `CreditSeniorityEnum` enumeration in tradestate.synonym.

_Samples_

- Added 3 new Credit samples to test the mapping for EMIR field `2.143 - Seniority`:
  - cdindex-ex01-cdx-seniority-Senior.xml
  - cdindex-ex02-cdx-seniority-Subordinate.xml
  - cdindex-ex03-cdx-seniority-Other.xml

_Review directions_

In the CDM Portal, select Translate and review the following samples:

- fpml-5-10/products/credit
  - cdindex-ex01-cdx-seniority-Senior.xml
  - cdindex-ex02-cdx-seniority-Subordinate.xml
  - cdindex-ex03-cdx-seniority-Other.xml