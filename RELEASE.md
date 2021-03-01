# Technical Change - Builder Refactor*

_What is being released?_

Upgraded the CDM code generators to ease future community contributions. This further simplifies the creation of Java function implementations by the CDM project team and the wider community.
Note: this change will not be backwards compatible with current CDM Java implementations.

_How to review the change_

In the CDM Portal, use the Download icon to inspect the generated Java code. Please note that the structure of the generated Java objects, those that represent CDM data types, have changed.

_Technical details of the change_

All the CDM types are now being generated in Java as interfaces instead of classes with implementation provided separately e.g. AdjustableDate is now a Java interface with an implementation called AdjustableDateImpl.
CDM Java objects are instantiated using the builder pattern with builder classes like AdjustableDateBuilder. These classes again have been made into interfaces that extends the POJO interfaces. These also have implementation provided e.g. AdjustableDateBuilder is an interface implemented by AdjustableDateBuilder.
The benefit of this change is that an AdjustableDateBuilder is a AdjustableDate, which makes the implementations of the code generators and functions much easier and more maintainable.

_Using the new structure_

If you are only using CDM objects and functions that have been generated elsewhere you shouldn't need to make any changes.
If you have code that changes CDM objects in Java or instantiates them using the builder classes there are some changes you will need to make. In all these examples XXX stands for the name of a CDM type.
 - The setXXXBuilder(XXXBuilder xxxBuilder) methods have been removed. The setXXX(XXX xxx) methods should be used instead as they will now accept XXXBuilders as they now implement XXX
 - Similarly the addXXXBuilder methods have also been removed and addXXX methods should be used
 - The setXXXRef methods have been renamed to setXXXValue becuase they have always set the value and not the ref
 - Function implementations - Any parameter of a function that was previously of type List<XXX> should now be List<? extends XXX>

