'''dict/list recursive comparison'''
def dict_comp(d1, d2, prefix=''):
    '''compare recursively a dictionary/list'''
    if d1 == d2:
        return True
    if type(d1) != type(d2):  # pylint: disable=unidiomatic-typecheck
        if (isinstance(d1, (float, int)) and isinstance(d2, str)
                or isinstance(d1, str)
                and isinstance(d2, (float, int))) and float(d1) == float(d2):
            return True
        print(f'Types differ for path {prefix} - d1: {type(d1)}, d2: {type(d2)}')
        return False
    if isinstance(d1, dict) and isinstance(d2, dict):
        if d1.keys() != d2.keys():
            print(f'Keys for path {prefix} differ: d1: {d1.keys()}, d2: {d2.keys()}')
            return False
        for k, v in d1.items():
            if not dict_comp(v, d2[k], f'{prefix}.{k}'):
                return False
        return True
    if isinstance(d1, list) and isinstance(d2, list):
        if len(d1) != len(d2):
            print(f'Lists at {prefix} are of different length d1: {len(d1)}, d2: {len(d2)}')
            return False
        for i, (e1, e2) in enumerate(zip(d1, d2)):
            if not dict_comp(e1, e2, f'{prefix}[{i}]'):
                return False
        return True
    print(f'Elements for path {prefix} differ: d1: {d1}, d2 {d2}')
    return False

# EOF
