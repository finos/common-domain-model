# *CDM to ISDA Create Projection*

_What is being released_

Infrastructure work to support the projection from CDM into ISDA Create JSON documents. Initial projection service implementation and a simple mapping class support the projection of '`LegalAgreement` CDM objects for English Law.  Further mapping classes will be added soon.

*Bug Fix* This is a fix that will prevent incomplete sequence sample files from being generated. These files are generated for use within Rosetta Core Instance Viewer for seeding the visualisation with sample data.

_Review direction_

The projection functionality will be made available in Rosetta Core, and linked from CDM Portal, where users can drag and drop CDM JSON files in, which will be projected into ISDA Create documents and displayed as JSON.
