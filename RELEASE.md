# *Model Change - Removed unnecessary comments from the logical model

_What is being released?_

This release provides a cleaner version of the CDM logical model that is viewable in the ISDA CDM Portal.  About 500 unnecessary commments were removed from the model in order to minimize distractions while reading the model.  These comments were preceded by // or inserted between /* and * /. Over 100 of these comments began with the phrase TODO or something like it, indicating future work to be considered.  All of these comments have been archived and are under review to identify and prioritize work to be done.

The only type of comments that have not been removed are those that provide useful guidance to the users, most typically found in the product qualifications and synonyms where additional explanation is helpful for user interpretation between lines of code.

_Review directions_

In the CDM Portal, select the Textual Browser, search for //.  The only cases that should be found are examples of guidance comments or the use of // in a URL.  Also, search for /* , there should not be any cases of this notation.
