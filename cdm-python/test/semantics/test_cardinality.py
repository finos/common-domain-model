'''testing cardinality enforcement'''
import datetime
import pytest
from cdm.base.datetime.DateList import DateList
from rosetta.runtime.utils import ConditionViolationError


def test_1_many_fail():
    '''DateList cannot be empty'''
    dl = DateList(date=[])
    with pytest.raises(ConditionViolationError):
        dl.validate_conditions()


def test_1_many_fail_empty_constructor():
    '''DateList cannot be empty'''
    dl = DateList()
    with pytest.raises(ConditionViolationError):
        dl.validate_conditions()


def test_1_many_pass():
    '''Valid DateList'''
    dl = DateList(date=[datetime.date(2020, 1, 1)])
    dl.validate_conditions()


if __name__ == "__main__":
    print("test_1_many_pass")
    test_1_many_pass()
    print("test_1_many_fail")
    test_1_many_fail()
    print("test_1_many_fail_empty_constructor")
    test_1_many_fail_empty_constructor()


# EOF
