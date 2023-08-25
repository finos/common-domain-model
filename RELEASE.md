# _Event Model - Add Repo Substitution Function._

_Background_

In the repo market, if agreed by the counterparties on the contract, it is possible for the seller to replace collateral that was originally
delivered at settlement with different collateral. The new collateral will most likely have a new quantity and price. 

_What is being released?_

A new function `Create_SubstitutionPrimitiveInstruction` is added that accepts inputs of a new payout that include new cash and collateral
amounts and price, and returns a new primitive instruction that can be used to execute a business event. In the case where
there was a contractual agreement to substitute collateral the original trade remains with reference to it's contract, identifiers, and 
other details. There is a also a new qualification function `Qualify_Substitution` that validates the results meet the substitution criteria.

