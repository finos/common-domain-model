''' Jsonn test utilities'''
import json
from pathlib import Path
import sys
import os
dirPath = os.path.dirname(__file__)
sys.path.append(os.path.join(dirPath))

from dict_comp import dict_comp
from cdm.version import __build_time__


def cdm_comparison_test_from_file(path, class_name):
    '''loads the json from a file and runs the comparison'''
    print('testing: ' + path + ' with className: ' + class_name.__name__ + ' using CDM built at: ' + __build_time__)
    json_str = Path(path).read_text()
    cdm_comparison_test_from_string(json_str, class_name)


def cdm_comparison_test_from_string(json_str, class_name):
    '''loads the json from a string and runs the comparison'''
    cdm_object = class_name.parse_raw(json_str)
    json_data_out = cdm_object.json(exclude_defaults=True, indent=4)
    orig_dict = json.loads(json_str)
    generated_dict = json.loads(json_data_out)    
    assert dict_comp(orig_dict, generated_dict), "Failed corrected dict comparison"
    print('passed: dicts matched')

# EOF
