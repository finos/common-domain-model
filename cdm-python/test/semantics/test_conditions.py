'''Full attribute validation - pydantic and constraints'''
import pytest
from pydantic import ValidationError
from rosetta.runtime.utils import ConditionViolationError
from cdm.base.math.NonNegativeQuantity import NonNegativeQuantity
from cdm.base.math.CapacityUnitEnum import CapacityUnitEnum
from cdm.base.math.UnitType import UnitType
from cdm.base.datetime.Frequency import Frequency
from cdm.base.datetime.PeriodExtendedEnum import PeriodExtendedEnum


def test_recursive_conditions_base_fail():
    '''condition_0_AmountOnlyExists violation'''
    unit = UnitType(currency='EUR')
    mq = NonNegativeQuantity(unit=unit)
    with pytest.raises(ConditionViolationError):
        mq.validate_model()


def test_recursive_conditions_direct_fail():
    '''Negative quantity condition violation'''
    unit = UnitType(currency='EUR')
    mq = NonNegativeQuantity(value=-10, unit=unit)
    with pytest.raises(ConditionViolationError):
        mq.validate_model()


def test_bad_attrib_validation():
    '''Invalid attribute assigned'''
    unit = UnitType(currency='EUR')
    mq = NonNegativeQuantity(value=10, unit=unit)
    mq.frequency = 'Blah'
    with pytest.raises(ValidationError):
        mq.validate_model()


def test_correct_attrib_validation():
    '''Valid attribute assigned'''
    unit = UnitType(currency='EUR')
    mq = NonNegativeQuantity(value=10, unit=unit)
    mq.frequency = Frequency(periodMultiplier=1, period=PeriodExtendedEnum.M)
    mq.validate_model()


if __name__ == "__main__":
    test_recursive_conditions_base_fail()
    test_recursive_conditions_direct_fail()
    test_bad_attrib_validation()
    test_correct_attrib_validation()

# EOF
