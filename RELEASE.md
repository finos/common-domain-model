# _Event Model - Add Repo Reprice Function._

_Background_

In the repo market it is common for countparties to agree to set a new price on collateral to adjust exposure. When th ecollateral
is repriced a the collateral amount or cash amount is adjusted. This release includes a new function to support the repricing lifecycle 
event. 

_What is being released?_

A new function `Create_RepricePrimitiveInstruction` is added that accepts inputs of a new collateral price, collateral quantity or cash
amount. The new function adjusts the amounts and returns a primitive instruction that is used together with a business event to
modify the trade and trade state.