A number of data elements defined in FpML are restricted to holding one of a limited set of possible values, e.g. currency, business centers, etc. Such restricted sets of values are frequently referred to as domains.

Domains in FpML that are expected to change over time are coded using a strategy that has been defined by the Architecture Working Group, referred to as 'Coding Schemes'. 

Each Coding Scheme is associated with a URI. Coding Schemes can be categorized as one of the following:

* An external coding Scheme, which has a well-known URI. In this case the URI is assigned by an external body, and may or may not have its own versioning, date syntax and semantics.
 
* An external coding Scheme, which does not have a well-known URI. In this case FpML assigns a URI as a proxy to refer to the concept of the external Scheme.  URI will not be versioned.

* An FpML-defined coding Scheme. In this case the Scheme is fully under FpML control and the URI will change reflecting newer versions and revisions as the scheme evolves and changes.

A coding scheme provides alternate identifiers for one identity. It is not used to identify things other than the identity of the thing that contains it.


The "codelist.zip" file contains the following coding Scheme XML files:

FpML Schemes (represented in XML)
================================
 account-type
 accruing-fee-type
 action-type
 algorithm-role
 allocation-reporting-status
 allocation-settlement-task-type
 applicable-purpose
 applicable-transaction-type
 approval-type
 asset-class 
 asset-measure
 assignment-fee-rule
 benchmark-rate
 broker-confirmation-type
 bullion-delivery-location
 business-center
 business-process
 cashflow-type
 cdx-index-annex-source
 cftc-commodity-code - (The CFTC based code for Large Trader Position Reporting (Part 20) purposes)
 cftc-commodity-reference-price  - (Deleted. Replaced by The CFTC based code for Large Trader Position Reporting (Part 20) purposes is no longer supported by CFTC. Replaced with ISDA commodity-reference-price)
 cftc-organization-type (status-working-draft)
 clearance-system
 clearing-exception-reason
 clearing-status
 collateral-arrangement
 collateral-asset-definitions-1-0
 collateral-dispute-resolution-method-reason
 collateral-interest-response-reason
 collateral-margin-call-response-reason
 collateral-response-reason
 collateral-retraction-reason
 collateral-substitution-response-reason
 collateral-type
 collateralized-exposure-grouping
 commodity-business-calendar
 commodity-coal-product-source
 commodity-coal-product-type
 commodity-coal-quality-adjustments
 commodity-coal-transportation-equipment
 commodity-delivery-risk
 commodity-environmental-tracking-system
 commodity-expire-relative-to-event
 commodity-floating-rate-index
 commodity-frequency-type
 commodity-fx-type
 commodity-information-provider
 commodity-market-disruption
 commodity-market-disruption-fallback
 commodity-metal-brand-manager
 commodity-metal-brand-name
 commodity-metal-product-type
 commodity-metal-shape 
 commodity-oil-product-grade
 commodity-oil-product-type
 commodity-pay-relative-to-event
 commodity-quantity-frequency
 commodity-reference-price (starts with v-3-0 ISDA CRP)
 commodity-reference-price-1-0 - (version 1-0 - not in use)
 commodity-reference-price-2-0 - (Deprecated. Replaced by cftc-commodity-reference-price) 
 compounding-frequency
 compression-type
 confirmation-method
 contractual-definitions
 contractual-supplement
 corporate-action 
 coupon-type
 credit-approval-model
 credit-document
 credit-limit-check-reason
 credit-limit-type
 credit-matrix-transaction-type
 credit-rating-agency
 credit-seniority
 credit-seniority-trading
 credit-support-agreement-type
 csa-dealer-status-entity-classification
 csa-local-party-status-entity-classification
 cut-name
 currency-defaults (available only in value-added set-of schemes)
 date-adjustment-type (status-working-draft)
 day-count
 day-count-fraction
 declear-reason
 delivery-method
 derivative-calculation-method
 designated-priority
 determination-method
 embedded-option-type
 entity-classification
 entity-classification-csa-dealer-status - (Deleted. Replaced by csa-dealer-status-entity-classification)
 entity-classification-csa-local-party-status - (Deleted. Replaced by csa-local-party-status-entity-classification)
 entity-classification-esma - (Deleted. Replaced by esma-entity-classification)
 entity-classification-sec - (Deleted. Replaced by sec-entity-classification)
 entity-type
 equity-matrix-transaction-type
 esma-currency-pair-classification
 esma-entity-classification
 esma-mifir-otc-classification
 esma-mifir-short-sale
 esma-mifir-trading-capacity 
 esma-mifir-trading-waiver
 esma-reporting-boolean
 esma-product-classification
 event-status
 event-type
 exchange-date
 execution-type
 execution-venue-type
 exposure-type
 currency
 facility-feature
 facility-type
 floating-rate-index
 floating-rate-index-loan - (Deleted. Replaced by loan-floating-rate-index)
 fx-template-terms
 generic-exercise-style
 governing-law
 hedge-type
 holding-posted-collateral
 independent-amount-determination
 independent-amount-eligibility
 ineligible-party-reason-type- (status-working-draft)
 inflation-index-description
 inflation-index-source
 inflation-main-publication
 information-provider
 initial-margin-interest-rate-terms
 interpolation-method
 lc-purpose
 lc-type
 legal-document-name
 legal-document-publisher
 legal-document-style
 lender-classification
 link-type
 loan-floating-rate-index (Deprecated in FpML 5-12. Replaced by floating-rate-index + metadata field "InLoan")
 loan-covenant-obligation-category-type (status-working-draft)
 loan-covenant-obligation-metric-adjustment-type (status-working-draft)
 loan-covenant-obligation-metric-type (status-working-draft)
 loan-covenant-obligation-numerator-denominator-type (status-working-draft)
 loan-covenant-obligation-task-type (status-working-draft)
 loan-covenant-obligation-type (status-working-draft)
 loan-legal-action-approval-status-type (status-working-draft)
 loan-legal-action-status-type (status-working-draft)
 loan-legal-action-type (status-working-draft)
 loan-legal-action-task-type (status-working-draft)
 local-jurisdiction
 local-status
 margin-quote-type
 market-disruption
 master-agreement-type
 master-agreement-version
 master-confirmation-annex-type
 master-confirmation-type
 matrix-type
 mortgage-sector
 no-settle-period-type
 non-iso-currency
 option-type
 organization-characteristic
 organization-type
 org-type-category
 originating-event
 package-type
 party-group-type
 party-relationship-type
 party-role
 party-role-type
 rate-administrator (available only in value-added set-of schemes)
 person-role
 perturbation-type
 position-change-type
 position-status
 position-update-reason-code
 pretrade-party-role
 price-quote-units
 pricing-context
 pricing-input-type
 pricing-model 
 product-taxonomy
 product-type-simple
 query-parameter-operator
 quote-timing
 reason-code
 region
 regulatory-corporate-sector
 reporting-boolean
 reporting-currency-type
 reporting-level
 reporting-purpose
 reporting-regime
 reporting-role
 report-status
 requested-action
 requested-collateral-allocation-action 
 requested-withdrawal-action
 resource-type
 restructuring
 scheduled-date-type
 sec-entity-classification
 service-advisory-category
 service-processing-cycle
 service-processing-event
 service-processing-step
 service-status
 set-of-schemes (catalog of all schemes)
 settled-entity-matrix-source
 settlement-day
 settlement-method
 settlement-price-default-election
 settlement-price-source
 settlement-rate-option
 sftr-action-type
 sftr-credit-quality
 spread-schedule-type
 supervisory-body
 tax-form-type
 terminating-event
 trade-cashflows-status
 trade-settlement-task-type
 trading-party-role
 transaction-characteristic
 transport-currency
 unit-role
 verification-method
 verification-status
 weather-data-provider
 weather-index-reference-level
 withdrawal-reason
 withholding-tax-reason
 
External Schemes (represented in Specification, not in XML)
===========================================================
 countryScheme - provides codes for country
 creditRatingScheme - Contains a code representing the credit rating agencies
 creditRatingNotationScheme - Contains a code representing the credit rating i.e., an evaluation of the credit worthiness of a debtor.
 creditRatingScaleScheme - The scale, which can be used to qualify the exposure duration, e.g., long term, short term, ...
 currencyScheme - provides codes for currency
 debtTypeScheme - The debt, which provides the ability to distinguish between the type of debt, e.g., high yield, deposit, ...
 entityIdScheme - qualifier for using entity identifiers
 entityNameScheme - specifies entity names used
 exchangeIdScheme - specifies a set of exchange identifiers
 floatingRateIndexScheme - allows floating rate index code definitions - (represented in xml)
 industryClassificationScheme - Contains a code representing the party's industry sector classification
 instrumentIdScheme - specifies a set of instrument identifiers
 interconnectionPointScheme - Identification of the border(s) or border point(s) of a transportation contract. Use the list of EIC codes for timelines for electricity (T Codes) or the list of EIC codes for measurement points for gas (Z Codes).
 issuerIdScheme - provides code for identifying issuers of Unique Swap IDs (USIs), also known as Unique Transaction Identifiers.  This code follows the CFTC's 10 character issuer identification system, which begins with 102 or 103 to identify CFTC vs. NFA issued organization identifiers.
 partyIdScheme - provides codes for identification of parties
 personIdScheme - scheme to Identify the type of person identifier. Alternatives: Identification of a person using a National Identity Number; Identification of a person using a Passport Number; Identification of a person using a concatenation of fields.
 productTypeScheme - Identification using a CFI Code (Classification for Financial Instruments - ISO 10962). CFI, ESMA sub asset class and FISN are Alternative to ISDA product taxonomy
 routingIdCodeScheme - The specification of the routing id code, which can be used to determine the coding convention for the settlement
 timezoneLocationScheme - Specific geographic location codes for which the time number is the prevailing time.
 weatherStationAirportScheme - A code identifying a Weather Station Airport (based on the the IATA standard) in ftp://ftp.ncdc.noaa.gov/pub/data/inventories/COOP.TXT
 weatherStationWBANScheme - A code identifying a Weather Station WBAN in ftp://ftp.ncdc.noaa.gov/pub/data/inventories/COOP.TXT
 weatherStationWMOScheme - A code identifying a Weather Index WMO in ftp://ftp.ncdc.noaa.gov/pub/data/inventories/COOP.TXT


Additional Information
======================
The Coding Schemes follow the content model defined by the CodeList.xsd schema file.

The FpML coding Schemes have been validated using Xerces J v 2.4.1.

For more information about Code Lists, visit:
http://www.genericode.org/
http://www.idealliance.org/proceedings/xml04/abstracts/paper86.html 