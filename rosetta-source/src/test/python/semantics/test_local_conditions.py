'''Tests of the local registration of conditions'''
import inspect
import pytest
from rosetta.runtime.utils import rosetta_local_condition
from rosetta.runtime.utils import execute_local_conditions
from rosetta.runtime.utils import ConditionViolationError


def test_pre_post_conditions():
    '''Tests the registration of functions in two different registries'''
    _pre_registry = {}
    _post_registry = {}
    self = inspect.currentframe()

    # A local PRE condition
    @rosetta_local_condition(_pre_registry)
    def some_local_condition():
        print(f'Pre {self}')
        return True

    # A local POST condition
    @rosetta_local_condition(_post_registry)
    def some_local_post_condition():
        print(f'Post {self}')
        return True

    # Check all PRE conditions
    execute_local_conditions(_pre_registry, 'Pre-condition')

    print('Some Code....')

    # Check all POST conditions
    execute_local_conditions(_post_registry, 'Post-condition')


def test_raise_local_cond():
    '''checks if exception is raised and it is of the correct type'''
    _registry = {}
    @rosetta_local_condition(_registry)
    def some_failing_local_post_condition():
        return False

    with pytest.raises(ConditionViolationError):
        execute_local_conditions(_registry, 'condition')


if __name__ == '__main__':
    test_pre_post_conditions()
    test_raise_local_cond()

# EOF
