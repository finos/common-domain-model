''' Tests related to validating models created on the fly or ingested from a
    json file.
'''
import os
import sys
from datetime import date
import logging
from pathlib import Path
from cdm.event.common.Trade import Trade
from cdm.event.common.TradeIdentifier import TradeIdentifier
from cdm.product.template.TradableProduct import TradableProduct
from cdm.product.template.Product import Product
from cdm.product.template.TradeLot import TradeLot
from cdm.observable.asset.PriceQuantity import PriceQuantity
from cdm.base.staticdata.party.Party import Party
from cdm.base.staticdata.party.PartyIdentifier import PartyIdentifier
from cdm.base.staticdata.party.Counterparty import Counterparty
from cdm.base.staticdata.party.CounterpartyRoleEnum import CounterpartyRoleEnum
from cdm.base.staticdata.asset.common.Index import Index
from cdm.base.staticdata.identifier.AssignedIdentifier import AssignedIdentifier
from cdm.event.common.TradeState import TradeState

sys.path.append(
    os.path.abspath(
        os.path.join(os.path.dirname(os.path.realpath(__file__)), os.pardir)))
# pylint:disable=wrong-import-position,import-error
from test_helpers.config import CDM_JSON_SAMPLE_SOURCE


def test_trade():
    '''Constructs a simple Trade in memory and validates the model.'''
    price_quantity = PriceQuantity()
    trade_lot = TradeLot(priceQuantity=[price_quantity])
    product = Product(index=Index())
    counterparty = [
        Counterparty(role=CounterpartyRoleEnum.PARTY_1,
                     partyReference=Party(
                         partyId=[PartyIdentifier(identifier='Acme Corp')])),
        Counterparty(
            role=CounterpartyRoleEnum.PARTY_2,
            partyReference=Party(
                partyId=[PartyIdentifier(identifier='Wile E. Coyote')]))
    ]
    tradable_product = TradableProduct(product=product,
                                      tradeLot=[trade_lot],
                                      counterparty=counterparty)
    assigned_identifier = AssignedIdentifier(identifier='BIG DEAL!')
    trade_identifier = [
        TradeIdentifier(issuer='Acme Corp',
                        assignedIdentifier=[assigned_identifier])
    ]

    t = Trade(tradeDate=date(2023, 1, 1),
              tradableProduct=tradable_product,
              tradeIdentifier=trade_identifier)
    exceptions = t.validate_model(raise_exc=False)
    assert not exceptions


def test_rates():
    ''' The below sample json is conform to CDM 5.8.0, the python library
        generated for earlier or newer versions of CDM might fail to parse
        it correctly.
    '''
    path = os.path.join(os.path.dirname(__file__), 
                        CDM_JSON_SAMPLE_SOURCE,
                        'rates', 
                        'bond-option-uti.json')
    json_str = Path(path).read_text(encoding='utf8')
    ts = TradeState.model_validate_json(json_str)
    print(repr(ts))

    exceptions = ts.validate_model(raise_exc=False)
    assert not exceptions


if __name__ == '__main__':
    logging.getLogger().setLevel(logging.DEBUG)
    test_trade()
    test_rates()
    print('Done!')

# EOF
