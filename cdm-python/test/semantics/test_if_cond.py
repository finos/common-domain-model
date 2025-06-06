import pytest
from rosetta.runtime.utils import ConditionViolationError
from rosetta.runtime.utils import if_cond
from cdm.base.math.QuantitySchedule import QuantitySchedule
from cdm.base.math.UnitType import UnitType


def test_if_cond_literals():
    class T:
        def __init__(self):
            self.cleared = 'Y'
            self.counterparty1FinancialEntityIndicator = None
            self.counterparty1FinancialEntityIndicator = None
            self.actionType = "NEWT"
            self.eventType = "CLRG"
            self.originalSwapUTI = 1
            self.originalSwapUSI = 'OKI'
    self = T()

    res = if_cond(
        ((self.cleared == "N") or (self.cleared == "I")),
        '((self.counterparty1FinancialEntityIndicator) is not None)',
        'if_cond((self.cleared == "Y"), \'((self.counterparty1FinancialEntityIndicator) == "NO")\', \'True\', self)',
        self)
    assert not res

    res = if_cond((((self.cleared == "Y") and (self.actionType == "NEWT")) and (self.eventType == "CLRG")),
        'if_cond(((self.originalSwapUTI) is None), \'((self.originalSwapUSI) is not None)\', \'if_cond(((self.originalSwapUTI) is not None), \\\'((self.originalSwapUSI) == "OKI")\\\', \\\'True\\\', self)\', self)',
        '((self.originalSwapUTI) is None)',
        self)
    assert res

def test_if_direct():
    class T:
        def __init__(self):
            self.x, self.y = 1, 2

    assert not if_cond(True,
        'if_cond(True, \'self.x > self.y\', \'True\', self)', 'True',  T())
    assert not if_cond(True,
        'if_cond(True, \'if_cond(True, \\\'self.x > self.y\\\', \\\'True\\\', self)\', \'True\', self)',
        'True', T())
    assert not if_cond(True,
        'if_cond(True, \'if_cond(True, \\\'if_cond(True, \\\\\\\'self.x > self.y\\\\\\\', \\\\\\\'True\\\\\\\', self)\\\', \\\'True\\\', self)\', \'True\', self)',
        'True',  T())

if __name__ == "__main__":
    print('test_if_cond_literals',end='')
    test_if_cond_literals()
    print('...passed\ntest_if_direct', end='')
    test_if_direct()
    print('...passed')

# EOF
