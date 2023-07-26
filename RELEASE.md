# *Infrastructure - Security Update*

_What is being released?_

Third party software libraries updated to comply with the “Common Vulnerabilities and Exposures” standard (CVE, https://www.cve.org/).

- Guava dependency (group id `com.google.guava`, artifact id `guava`) upgraded to version 32.0.1-jre remove vulnerability in earlier versions [CVE-2023-2976](https://cve.mitre.org/cgi-bin/cvename.cgi?name=CVE-2023-2976)

# *Infrastructure - Dependency Updates*

_What is being released?_

This release updates the bundle dependency version.  The bundle dependency comprises a number of dependencies that are released together as a bundle, including [rosetta-common](https://github.com/REGnosys/rosetta-common) and [rosetta-code-generators](https://github.com/REGnosys/rosetta-code-generators).

- Bundle version upgrade includes:
    - 6.8.2: Bug fix related to the processing of JSON schemas for ingestion. This update does not affect the model or test expectations.
