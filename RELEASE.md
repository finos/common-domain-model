# _Refactor payer / receiver (Party1 / Party2) for a Tradeable Product's embedded payout_

_Background_

An InterestRatePayout defines the payerReceiver as a counterparty (Party1 or Party2) as defined in TradeableProduct. For trades with settlementPayout this works but when you add the interestRatePayout for bonds or equities with Product or Instrument->Underlier, the payer/receiver are incorrect and there is no way to link the payer to an Issuer. 
There is a related problem that it's not possible to define the Issuer on the Asset.

_What is being released?_

As per GitHub Issue [3590](https://github.com/finos/common-domain-model/issues/3590) and [3567](https://github.com/finos/common-domain-model/issues/3567): 

1) Add new PartyRole "Issuer"
2) Add productParty Counterparty (0..1) to TransferableProduct
3) Add to AssetBase:
	-assetPayer Party (0..1)
	-assetPayerRole PartyRole (0..1)
	-assetAncillaryParty AncillaryParty (0..*)
4) Add new function input tests, bond-execution-with-bond-details.

_Review Directions_

Changes can be reviewed in PR: [3666](https://github.com/finos/common-domain-model/pull/3666)
