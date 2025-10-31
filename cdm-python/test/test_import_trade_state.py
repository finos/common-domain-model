'''test the generated Python'''
# pylint: disable=import-outside-toplevel at top, unused-import
import pytest

def test_import_tradestate():
    '''confirm that tradestate can be imported'''
    try:
        from cdm.event.common.TradeState import TradeState
    except ImportError:
        pytest.fail("Importing cdm.event.common.TradeState failed")