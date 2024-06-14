'''used to establish configuration shared across the tests'''
import pathlib

CDM_JSON_SAMPLE_SOURCE = str(pathlib.Path(pathlib.Path().parent.absolute(),
                                          'rosetta-source',
                                          'src',
                                          'main',
                                          'resources',
                                          'result-json-files',
                                          'fpml-5-10',
                                          'products'))
# EOF
