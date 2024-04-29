import pytest
from rosetta.runtime.utils import _resolve_rosetta_attr
from src.com.rosetta.test.model.Foo import *
 
foo = Foo(one=42, three=[1, 2, 3])

def test_attribute_single ():
	assert _resolve_rosetta_attr(foo, 'one') == 42
def test_attribute_optional ():
	assert _resolve_rosetta_attr(foo, 'two') is None
def test_attribute_multi ():
	assert _resolve_rosetta_attr(foo, 'three') == [1, 2, 3]
def test_attribute_single_collection (): 
	assert _resolve_rosetta_attr([foo, foo], 'one') == [42, 42]
def test_attribute_optional_collection (): 
	assert _resolve_rosetta_attr([foo, foo], 'two') is None
def test_attribute_multi_collection ():
	assert _resolve_rosetta_attr([foo, foo], 'three') == [1, 2, 3, 1, 2, 3]

if __name__ == "__main__":
	test_attribute_single ()
	test_attribute_optional ()
	test_attribute_multi ()
	test_attribute_single_collection () 
	test_attribute_optional_collection () 
	test_attribute_multi_collection ()