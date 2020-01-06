# *Rosetta Syntax Upgrade: Migrate isProduct, isEvent and alias to use the func syntax*

_What is being released_

As part of on-going improvements, the Rosetta syntax has been upgraded to consolidate language features. This work aims to simplify the syntax needed when adding and editing the CDM.

* The Event Qualification logic is now expressed using the `func` syntax.
  The `isEvent` has now changed to use the standard `func` syntax. To differentiate this kind of function from others, it is annotated with `[qualification event]` and must always result in a `boolean`.

* The Product Qualification logic is now expressed using the `func` syntax.
  The `isProduct` has now changed to use the standard `func` syntax. To differentiate this kind of function from others, it is annotated with `[qualification product]` and must always result in a `boolean`.

* The `alias` syntax was previously used in two different ways, within functions and as a top-level element designed to be used in the `isEvent` and `isProduct` logic. The latter has now been expressed as a `func`.

_Review Directions_

In the Textual Browser, review func `Allocation`.

The change for 'Allocation' expresses the same logic but defines the input and output types consistent with other parts of the model.:

Previous syntax:
```
isEvent Allocation <"...">
	WorkflowEvent -> businessEvent -> primitives count = 1
	and WorkflowEvent -> businessEvent -> primitives -> allocation exists
```

Updated syntax:
```
func Allocation: <"...">
	[qualification event]
	inputs: workflowEvent WorkflowEvent(1..1)
	output: is_event boolean (1..1)
  
	assign-output is_event:
		workflowEvent -> businessEvent -> primitives count = 1
		and workflowEvent -> businessEvent -> primitives -> allocation exists

```

Similarly, see func `NewTrade` for an event qualification example.
