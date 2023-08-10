import pytest
from pydantic import ValidationError
from rosetta.runtime.utils import ConditionViolationError
from cdm.base.math.NonNegativeQuantity import NonNegativeQuantity
from cdm.base.math.UnitType import UnitType

'''
def test_recursive_conds():
    unit = UnitType(currency='EUR')
    mq = NonNegativeQuantity(value=10, unit=unit)
    mq.validate_model()

'''

def test_recursive_conds_base_fail():
    unit = UnitType(currency='EUR')
    mq = NonNegativeQuantity(unit=unit)
    with pytest.raises(ConditionViolationError):
        mq.validate_model()

def test_recursive_conds_direct_fail():
    unit = UnitType(currency='EUR')
    mq = NonNegativeQuantity(value=-10, unit=unit)
    with pytest.raises(ConditionViolationError):
        mq.validate_model()


def test_attrib_validity():
    unit = UnitType(currency='EUR')
    mq = NonNegativeQuantity(value=10, unit=unit)
    mq.frequency = 'Blah'
    with pytest.raises(ValidationError):
        mq.validate_model()

if __name__ == "__main__":
	test_recursive_conds_base_fail()
	test_recursive_conds_direct_fail()
	test_attrib_validity()

# EOF
