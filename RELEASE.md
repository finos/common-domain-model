# *Infrastructure - Security Update*

_What is being released?_

Third-party software libraries updated to comply with the “Common Vulnerabilities and Exposures” standard (CVE, https://www.cve.org/).

- `npm/axios` upgraded from version 0.30.1 to 1.12.0, see [GHSA-4hjh-wcwx-xvwj](https://github.com/advisories/GHSA-4hjh-wcwx-xvwj) for further details
- `npm/docusaurus` upgraded from version 2.4.1 to 3.8.1 upgraded from version 2.4.1 to 3.8.1 to remove a transitive dependency on axios 0.7.0.

_Review Directions_

Changes can be reviewed in PR: [#4054](https://github.com/finos/common-domain-model/pull/4054)
