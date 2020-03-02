# *Namespaces: Initial roll-out (Base Package)*

_What is being released_

The Model is being refactored into namespaces to make it more modular. This will enable the users of CDM to import only 
the part of the model they are interested in. 

This refactoring is structural ONLY, and does not change the functionality of the Model in any way.
 
This refactor is the first of many incremental changes that will transform the CDM into hierarchical namespace tree. The first refactor is 
for the __base.cdm.*__ package.

This package contain basic components used across the CDM for date, time, math and also static data (party, asset, identifier).

_Review Directions_

In the CDM Portal, navigate to the Downloads tile, review the generated Java source to see the new cdm.base.* package.
