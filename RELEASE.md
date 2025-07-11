# _CDM Distribution - Python Code Generation flag update_

_Background_

After an upgrade to using xtext, the incremental builds were running with value `true`. This resulted in the Python build not generating the expected number of enum files, as detailed in [Issue 3829](https://github.com/finos/common-domain-model/issues/3829).

_What is being released?_

Configuration changes have been made to set `incrementalXtextBuild` to `false` to ensure enum files are generated as expected.

The changes can be reviewed in PR: https://github.com/finos/common-domain-model/pull/3862

