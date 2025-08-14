# _Infrastructure - Dependency Update_

_What is being released?_

This release updates Python generation to use the new FINOS-hosted Python [generator](https://github.com/finos/rune-python-generator) and [runtime](https://github.com/finos/rune-python-runtime).

These updated components now provide support for:

- The new serialization standard
- Metadata in Rune-defined types
- Types with circular dependencies
- Python generation when the input Rune includes multiple top-level namespaces

**Note:** Python generation from Rune-defined functions is not yet supported.

This release differs from the process of generating other language versions of CDM by using a standalone CLI included in the Python Generator. The new process is defined in the [build script](./rosetta-source/src/main/resources/build-resources/python/build-cdm-python.sh):

1. Determine the value of `rosetta.dsl.version` used in the [CDM pom](./pom.xml)
2. Find a Python Generator that matches the version number.  **The process stops if none are found.**
3. Build Python by invoking the CLI, providing the CDM Rune source and a target directory for the generated Python
4. Run tests
5. If successful, package the generated Python and releasing the Python package along with the most recently released runtime

The changes can be reviewed in PR: [#3975](https://github.com/finos/common-domain-model/pull/3975)
