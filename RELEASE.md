# Infrastructure / Regression Test Improvements

_What is being released_

Regression test changes to allow more granular model validation test assertions.

# Instance Viewer Changes

_What is being released_

- Fixed bug where some edges where self referencing so they would leave empty arrows on the nodes in the graph.
- Switched to a unified approach to word wrapping inside each node in the graph.
- Implemented a change that will pass the node ancestry on to the UI thereby allowing us to populate edges with parent access fields such as ‘before’ and ‘after’.
