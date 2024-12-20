@echo off

echo ***** Converting codelists to json
set SRCDIR=..\fpml_source_data
call merge-json business-center
call merge-json commodity-business-calendar
call merge-json floating-rate-index
call merge-json inflation-index-description
call merge-json credit-event-type
call merge-json master-agreement-type
call merge-json master-confirmation-type
call merge-json master-confirmation-annex-type
call merge-json commodity-reference-price
call merge-json credit-rating-agency
call merge-json information-provider
call merge-json interpolation-method
call merge-json settlement-rate-option
call merge-json determination-method
call merge-json credit-seniority

echo *** Converting phase 2 codelists to json
del /q %SRCDIR%\party-role-type*.xml    REM this confuses the XSLT file matcher for some reasson

call merge-json price-quote-units
call merge-json commodity-information-provider
rem call merge-json currency
call merge-json entity-type
call merge-json party-role
call merge-json corporate-action
call merge-json contractual-definitions
call merge-json contractual-supplement
call merge-json governing-law
call merge-json legal-document-publisher
