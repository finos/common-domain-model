def dict_comp(d1, d2, prefix=''):
    if d1 == d2:
        return True
    if type(d1) != type(d2):
        print(f'Types differ for path {prefix} - d1: {type(d1)}, d2: {type(d2)}')
        return False
    if isinstance(d1, dict) and isinstance(d2, dict):
        if d1.keys() != d2.keys():
            print(f'Keys for path {prefix} differ: d1: {d1.keys()}, d2: {d2.keys()}')
            return False
        for k, v in d1.items():
            if not dict_comp(v, d2[k], f'{prefix}.{k}'):
                return False
    elif isinstance(d1, list) and isinstance(d2, list):
        for i, (e1, e2) in enumerate(zip(d1, d2)):
            if not dict_comp(e1, e2, f'{prefix}[{i}]'):
                return False
        if len(d1) != len(d2):
            print(f'Lists at {prefix} are of diffrent lenght d1: {len(d1)}, d2: {len(d2)}')
        else:
            print(f'Lists at {prefix} differ for unknown reason?!?')
        return False
    else:
        print(f'Elements for ptah {prefix} differ: d1: {d1}, d2 {d2}')
        return False

# EOF
