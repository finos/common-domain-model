# *CDM Model: Legal Agreement namespace classification*

_What is being released_

This refactor is the fourth incremental change that will further transform the org.isda.cdm file into a hierarchical namespace.

This fourth refactor includes the changes for the __cdm.legalagreement.*__ set of namespaces.

The namespaces contain components used across the CDM for 
* Generic documentation concepts: legal agreement, contract, calc agent, disruption and extraordinary events,
* Common legal agreement concepts,
* Contract (i.e. transaction confirmation) concepts, 
* Credit support concepts: CSA, collateral, elections, initial margin, threshold, minimum transfer amount,
* and Master agreement concepts.

_Review Direction_

In Rosetta Core (https://ui.rosetta-technology.io/), review the File or Namespace structure in the Editor Textual View. In the CDM Portal, 
navigate to the Downloads tile, then download artefacts in Java, DAML, Typescript or Scala distribution format and review the reorganised source folder with new cdm.legalagreement.* files.

# *New mapping syntax is being released to support selected variations in patterns observed during synonym mappings from other models to the CDM*

_What is being released_

`pattern` -A synonym on a a basicType or an enumerated type can optionally be followed by the pattern construct that consists of the keyword `pattern` followed by two quoted strings. 
The first string is a regular expression using the PERL standard syntax used to match against the input value. The second string is a replacement expression used to reformat the matched input before it is processed for the basic type or enum. E.g.
```
       Frequency:
              + periodMultiplier
                    [value "Tenor" maps 2 pattern "([0-9]*).*" "$1"]
              + period
                    [value "Tenor" maps 2 pattern "[0-9]*(.*)" "$1"]
```
A tenor value of "1Y" will be deconstructed into a period of 'Year' and a periodMultiplier of '1'

`dateFormat` - A date/time synonym can be followed by a format construct. The keyword `dateFormat` should be followed by a string. 
The string should be a date format and will be interpreted following the pattern documented here [Date format](https://docs.oracle.com/javase/8/docs/api/java/time/format/DateTimeFormatter.html). This format will be used to interpret dates/times from the input element.
E.g.
```
	+ tradeDate
        [value "startDate" dateFormat "MM/dd/yy"]
```
_Review Directions_

 - The first use of the pattern and format syntax has been in the context of an initial set of mapping synonyms for ORE XML.  The pattern syntax is used to map the tenor field - splitting it up into separate period and period multiplier fields. The format syntax is used to map dates from formats accepted in ORE XML to the CDM. 
 - For an illustration of the use of the new pattern syntax, 1) go to the CDM Portal, 2) click on the link for Rosetta, 3) in the Files panel on the left, select synonym-cdm-ore, 4) and then view the mappings for Frequency 
 - To view a an example in rosetta, 1) first verify that the status indicator in the lower right is green, 2) go to the ingestion feature, under Synonyms, select ORE_1_0_39,  deselect the other synonym sets, 3) Select INTEGRATION TESTS, 4) click the play button next to Ingestion 5) scroll down select the ORE test ingestion for Vanilla_IR_Swap 6) In the right side panel with the CDM projection, check the period and period multiplier for any Period, and check any date to see the results of the mapping
