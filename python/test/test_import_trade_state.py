'''test the generated Python'''
# pylint: disable=import-outside-toplevel at top, unused-import
import pytest
 
def test_import_tradestate():
    '''confirm that tradestate can be imported'''
    print ("Testing import of TradeState from finos.cdm.event.common.TradeState")
    try:
        from finos.cdm.event.common.TradeState import TradeState
    except ImportError:
        pytest.fail("Importing finos.cdm.event.common.TradeState failed")
    