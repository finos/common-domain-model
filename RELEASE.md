# Build Modernization — Introduce `thin` Maven Profile for Minimal Runtime Distribution

_Background_

The current `cdm-java` (rosetta-source) distribution bundles a significant volume of supplemental resources into the primary runtime artifact. This results in an artifact size of approximately 65 MB in Maven Central, which leads to unnecessary memory overhead and an expanded security surface area in production and cloud-native environments.

By decoupling the core generated model logic from these supplemental files, this PR provides a "Thin JAR" alternative that improves deployment efficiency, reduces class-loading times, and simplifies security compliance audits.

_What is being released?_

* Introduction of the `thin` Maven Profile: An opt-in build configuration that generates a lean runtime artifact.
* Secondary Artifact with Maven Classifier: Implementation of the `thin` classifier, allowing the build to generate a secondary, lean JAR (e.g., `cdm-java-thin.jar`) alongside the standard distribution ensuring full backward compatibility for existing pipelines.
* Targeted Resource Exclusions: The following resources are excluded from the `thin` artifact to minimize bloat:

```xml
<excludes>
    <exclude>build-resources/**</exclude>
    <exclude>cdm-sample-files/**</exclude>
    <exclude>config/**</exclude>
    <exclude>functions/**</exclude>
    <exclude>ingest/**</exclude>
    <exclude>ingestions/**</exclude>
    <exclude>mapping-analytics/**</exclude>
    <exclude>release/**</exclude>
    <exclude>result-json-files/**</exclude>
    <exclude>schemas/**</exclude>
    <exclude>**/rosetta/*.rosetta</exclude>
</excludes>

```

_Review Directions_

Maintainers and users can verify the build locally by running:
`mvn clean install -Pthin -DskipTests`

This should produce both the standard JAR and the new `-thin.jar` artifact in the `target` directory and local `.m2` repository.