''' Compare CDM / JSON deserialization and serialization'''
import json
from pathlib import Path
import sys
import os
from pydantic import ValidationError
from cdm.version import __build_time__
from cdm.event.common.TradeState import TradeState
sys.path.append(os.path.abspath(os.path.join(os.path.dirname(os.path.realpath(__file__)), os.pardir)))
from serialization.dict_comp import dict_comp
from test_helpers.config import CDM_JSON_SAMPLE_SOURCE

def cdm_comparison_test_from_file(path, class_name):
    '''loads the json from a file and runs the comparison'''
    print('testing: ',
          path,
          ' with className: ',
          class_name.__name__,
          ' using CDM built at: ',
          __build_time__)
    json_str = Path(path).read_text()
    json_dict = json.loads (json_str)
    print('json_dict["trade"]["tradeDate"]: ' + json.dumps(json_dict["trade"]["tradeDate"]))
    try:
        print('raw parse from json_str')
        cdm_object = class_name.model_validate_json(json_str)
        trade = cdm_object.trade
        print('trade.tradeDate:', str(trade.tradeDate))
        json_data_out = cdm_object.model_dump_json(indent=4, exclude_defaults=True)
        generated_dict = json.loads(json_data_out)    
        assert dict_comp(json_dict, generated_dict), "Failed corrected dict comparison"
        print('passed: dicts matched')
    except ValidationError as e:
        print('failed to parse')
        print(e)

# EOF
