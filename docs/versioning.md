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

Please note, all contributions must follow the [change control guidelines](change-control-guidelines.md).

Click [here](roadmap.mdx) to viww the current planned release timeline.

---
**Note:**
The above example is for illustration only and not indicative of
actually supported CDM versions.

