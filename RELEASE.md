# *Rosetta Grammar: Add override keyword*

_What is being released_

Adding a new override keyword
 - this allows an extending type to change the type of an attribute from its parent type
 - the new type of the attribute must be a type that extends the old type

_Review direction_

Create a new type that extends TradableProduct that adds a new field
Create a new type that extends Contract that overrides the tradeableProduct attribute to use the new type created above

