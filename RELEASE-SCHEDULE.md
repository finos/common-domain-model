# FINOS CDM Contribution Review Working Group

## Release schedule

| Release  | Status              | Initial Release | Active LTS Start | Maintenance Start | End-of-life               |
| :--:     | :---:               |  :---:          | :---:            | :---:             | :---:                     |
| 2.x      | **Maintenance**     |  2019-03-31     | 2022-11-09       | 2022-11-09        | 2023-04-30                |
| 3.x      | **Current**         |  2022-11-09     | 2023-04-30       | 2023-10-31        | -                         |
| 4.x      | **In Development**  |  2023-04-30     | 2023-10-31       | -                 | -                         |
| 5.x      | **Not Started**     |  2023-10-31     | -                | -                 | -                         | 

Dates are subject to change.

<p><img src="CDM Release Schedule.svg" alt="CDM Release Schedule"/></p>

### Release Phases

There are five phases that a CDM release can be in: 'Current', 'Active
Long Term Support (LTS)', 'In Development', 'Maintenance', and 'Not Started'.

 * Current - Is the current stable version.
 * Active LTS - New features, bug fixes, and updates that have been audited by
 the Contribution Review Working Group and have been determined to be appropriate
 and stable for the release line.
 * Maintenance - Critical bug fixes and security updates. New features may be
 added at the discretion of the maintainers - typically only in cases where
 the new feature supports migration to later release lines.
 * In Development - New content under development to be included in the next major
 release.

Please refer to the Development Guidelines for further information on [semantic
versioning](https://cdm.docs.rosetta-technology.io/source/contribution.html#version-management) guidelines.

The term 'supported release lines' will be used to refer to all release lines
that are not End-of-Life.

### End-of-Life Releases

|  Release |      Status     |  Initial Release | Active LTS Start | Maintenance LTS Start | End-of-life |
|:--------:|:---------------:|:----------------:|:----------------:|:---------------------:|:-----------:|
| [2.x][]  | **End-of-Life** |   2019-03-31     | 2022-11-09       | 2022-11-09            | 2023-04-30  |

## Mandate

The Contribution Review working group's purpose is:

* Management/execution of the release and support process for all releases.

Its responsibilities are:

* Define the release process.
* Define the content of releases.
* Generate and create releases.
* Test Releases.
* Manage the LTS and Current branches including backporting changes to
  these branches.
* Define the policy for what gets backported to release streams.
