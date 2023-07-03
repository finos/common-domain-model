import sys
import pytest
import argparse

from cdm.event.common.TradeState import TradeState
import sys,os

from cdm_comparison_test import cdm_comparison_test_from_file

def test_trade_state (cdm_sample=None):
	if cdm_sample == None:
		dirPath = os.path.dirname(__file__)
		sys.path.append(os.path.join(dirPath))
		cdm_sample = dirPath + '/../../../main/resources/result-json-files/fpml-5-10/products/rates/EUR-Vanilla-account.json'
	cdm_comparison_test_from_file (cdm_sample, TradeState)

if __name__ == "__main__":
	cdm_sample = sys.argv[1] if len(sys.argv) > 1 else None
	test_trade_state(cdm_sample)