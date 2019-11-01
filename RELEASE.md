# *DerivHack Feedback - Support Assignment of References*

What is being released

From Barclays DerivHack 2019 event feedback, users requested the Function Syntax to support assigning references in addition to assigning values. This is necessary when specifying how the Lineage object should be populated, in the Function Syntax’s `assign-output` statements.

The Function Syntax now supports the `as-key` keyword within the context of an `assign-output` statement that effectively communicates to the reader (and to the underlying generated code) that it is the reference that will be assigned and not the value itself. 

For example, in the below code snippet, we assign the global key of the Execution Event to the appropriate element on the Lineage object.

```
assign-output allocationEvent -> lineage -> eventReference:
  executionEvent as-key
```

Review direction

In the Textual Browser, see the Execute, Allocate and Settle Functions where examples of where the new keyword is being used. The change has also been affected in the generated java code which can be used by CDM adopters. 
