# pylint: disable=unused-import,missing-function-docstring,invalid-name
from datetime import date
from cdm.event.common.Trade import Trade
from cdm.event.common.TradeIdentifier import TradeIdentifier
from cdm.product.template.TradableProduct import TradableProduct
from cdm.product.template.Product import Product


def test_trade():
    product = Product()
    tradableProduct = TradableProduct(product=product)
    tradeIdentifier=[TradeIdentifier(issuer='Acme Corp')]

    t = Trade(
        tradeDate=date(2023, 1, 1),
        tradableProduct=tradableProduct,
        tradeIdentifier=tradeIdentifier
    )
    print(t.model_dump())
    print('Done!')


if __name__ == '__main__':
    test_trade()

# EOF
