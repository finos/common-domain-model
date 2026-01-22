# Product Model - Barrier Options Cardinality Updates

_Background_

Barrier Options can have multiple knock-ins and knock-outs which are not supported with the current cardinality. The cardinality of the `knockIn` or `knockOut` / `barrierCap` or `barrierFloor` attributes is currently `(0..1)`.

_What is being released?_

Relaxing the cardinality to `(0..*)` to handle multiple `knockIn` or `knockOut` / `barrierCap` or `barrierFloor`.

_Review Directions_

Changes can be reviewed in PR: [#4358](https://github.com/finos/common-domain-model/pull/4358)
