# *ORE Mappings: New mapping syntax*

_What is being released_

To support the ORE mappings new mapping syntax is being released

`pattern` - A synonym can optionally be followed by a the pattern construct. It is only applicable to enums and basic types other than date/times. The keyword `pattern` followed by two quoted strings. The first string is a [regular expression](https://docs.oracle.com/javase/7/docs/api/java/util/regex/Pattern.html) 
used to match against the input value. The second string is a replacement expression used to reformat the matched input before it is processed as usual for the basictype/enum. E.g. 
```
[value "Tenor" maps 2 pattern "([0-9]*).*" "$1"]
```

`format` - A date/time synonym can be followed by a format construct. The keyword `format` should be followed by a string. The string should be a [Date format](https://docs.oracle.com/javase/8/docs/api/java/time/format/DateTimeFormatter.html)
E.g.
```
[value "bar" path "baz" format "MM/dd/yy"]
```

The pattern syntax has been used to correctly mapp the tenor field from ORE - splitting it up into separate period and period multiplier fields.

_Review Directions_

In the CDM Portal, use the ingestion app to view the ORE test ingestion and check the period and period multiplier.
