## Semantic Versioning

The CDM is released using the semantic versioning 2.0 system - See
[SemVer 2.0.0](https://semver.org/spec/v2.0.0.html). At high-level, the
format of a version number is MAJOR.MINOR.PATCH (e.g. `1.23.456`),
where:

-   A MAJOR (`1`) version may introduce backward-incompatible changes
    and will be used as high level release name (e.g. "CDM Version
    1").
-   A MINOR (`23`) version may introduce new features but in a
    backward-compatible way, for example supporting a new type of event
    or function.
-   A PATCH (`456`) version is for backward-compatible bug fixes, for
    example fixing the logic of a condition.

In addition, pre-release versions of a major release will be denoted
with a DEV tag as follows:

-   MAJOR.0.0-DEV.x (e.g. `1.0.0-DEV.789`), where x gets incremented
    with each new pre-release version until it becomes the MAJOR.0.0
    release.

The minor, patch and pre-release numbers may sometimes increment by more
than one unit. This is because release candidates may be created but not
immediately released. Subsequently, a version associated with the next
incremental unit may be released that includes the changes from the
earlier release candidate.

Unless under exceptional circumstances, the major number will be
incremented by one unit only.

## Backward Compatibility

Like other types of software, *backward compatibility* in the context of
a domain model means that an implementor of that model would not have to
make any change to update to such version.

-   Prohibited changes:
    -   Change to the structure (e.g. the attributes of a data type or
        the inputs of a function) or removal of any model element
    -   Change to the name of any model element (e.g. types, attributes,
        enums, functions or reporting rules)
    -   Change to any condition or cardinality constraint that makes
        validation more restrictive
    -   Change to the DSL that results in any existing expression
        becoming invalid
    -   Change to the DSL that results in change to any of the generated
        code's public interfaces
-   Allowed changes:
    -   Change that relaxes any condition or cardinality constraint
    -   Change to any synonym that improves, or at least does not
        degrade, the mapping coverage
    -   Addition of new examples or test packs
    -   Change to the user documentation or model descriptions
    -   Addition of new data types, optional attributes, enumerations,
        rules or functions that do not impact current functionality

Exceptions to backward compatibility may be granted for emergency bug
fixes following decision from the relevant governance body.

## Version Availability

Several versions of the CDM will be made available concurrently, with a
dual objective.

-   The latest *development* version (i.e. with a pre-release tag)
    fosters continued, rapid change development and involves model
    contributions made by the industry community. Changes that break
    backward compatibility are allowed. This development version is
    available in read-only and read-write access on the CDM's
    modelling-platforms.
-   The latest *production* version (i.e. without any pre-release tag)
    offers a stable, well-supported production environment for consumers
    of the model. Unless under exceptional circumstances, no new
    disruptive feature shall be introduced, mostly bug fixes only. Any
    change shall adhere to a strict governance process as it must be
    backward-compatible. Generally, it can only be developed by a CDM
    Maintainer. This production version is available in read-only access
    through the CDM's modelling-platforms.
-   Earlier production versions, when still supported, are also
    available in read-only access for industry members who are still
    implementing older versions of the model. Over time, those earlier
    production versions enter *long-term support* in which
    supportability will be degraded, until they eventually become
    unsupported.

**Example**. Assume that the latest major release of the model is 5. The
various versions available would be as follows:

-   5.0.0 and any subsequent 5.x would be the latest production version.
    Backward-compatibility to the initial 5.0.0 version would be
    maintained for any 5.x successor version.
-   The latest 4.x and 3.x may also be supported, but 2.x could be under
    long-term support and 1.x unsupported altogether.
-   6.0.0-DEV.x would be the latest development version. It can, and
    will generally, contain changes that are not backward-compatible
    with version 5. Backward-compatibility between successive
    6.0.0-DEV.x versions is also not assured. Once fully developed,
    version 6.0.0 can be tagged as a major release and becomes the new
    latest production version.

---
**Note:**
The above example is for illustration only and not indicative of
actually supported CDM versions.

