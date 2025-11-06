---
title: Major Release Scheduling Guidelines
---

# Major Release Scheduling Guidelines

The [Steering Working Group](CDM-Steering-WG.md) has the role of defining major releases of CDM and shaping their content.  This section discusses the objectives for defining major releases and guidelines that the Steering Working Group (SWG) must follow in scheduling major releases.

## Objectives of Defining Major Releases

* To identify and communicate to users of CDM when changes will happen that could affect them in a profound way, e.g.
  - Changes to existing functionality that may create challenges for upgrading
  - Changes to technology architecture that may create challenges for upgrading
  - Introduction of major new functionality that may affect how users use CDM going forward
* To help developers of CDM understand the roadmap for the most critical changes to the CDM, so they can better plan their work
* To promote planned and new CDM capabilities to encourage adoption
  
##  Objectives of Defining Guidelines for Scheduling and Approving Major Releases

* To ensure that major releases are planned, scheduled, and approved in a predictable, consistent, and transparent way
  - Ensure smoother development
  - Reduce conflict
* To ensure that we follow industry best-practices for evolving software.
  
##  Overall Principles for Scheduling Major Releases

* Major releases shall be planned ahead of time and these plans reviewed and approved by the SWG  so that consumers of CDM are aware of the planned changes and can plan for those changes.
There is a balance between moving too quickly (and creating many changes, potentially discouraging adoption) and moving too slowly (and not addressing major issues in a timely fashion).  The SWG will be tasked with assessing and maintaining that balance and communicating its decisions.  That balance is likely to change over time as the CDM software matures; likely major release frequency will slow down in the future.
* Part of the role of the guidelines will be to help the CDM SWG to resist pressure to create too many major releases.  However, the guidelines need to provide the SWG with enough flexibility to address major challenges relatively quickly and flexibly when required.
  * Defining the guidelines is important to implement the above objective
    
## Detailed Guidelines – Scheduling Major Releases

* No major release will be planned/scheduled  (decision and content) without formal approval at a meeting of the SWG
  * *Rationale:*  Designation of a major release is an important decision that requires transparency and control
 
* The intention is that major releases shall be planned and reviewed at the SWG at least 3 months ahead of the anticipated release date.  
  * *Rationale:*  Giving the community advance warning of major changes will help CDM users plan for how they will use CDM and avoid major surprises.  It will also help CDM developers plan their own changes
    
* It is anticipated that for at least the next several years (say 4-5) at least one major release will be planned each year.
  * *Rationale:* we anticipate that there will be an accumulation of desired changes that cannot be accommodated within a minor release and we wish to ensure that these can be addressed without undue delay
    
* Any addition to the scope/contents of (or technical change to) a planned major release requires SWG approval
  * *Rationale:*   similar to the above guideline on scheduling major releases
    
* If planned scope items for a major release are not available in time for the planned release date, the SWG will need to decide whether to slip the release date or drop the item, based on industry priorities
  
* These guidelines can be overridden in exceptional circumstances by a formal vote of the SWG.
  * *Rationale:*  Sometimes unanticipated issues will come up and we need the ability to move quickly in these cases. However, there should be an explicit decision process when breaking a guideline.
    
* These guidelines can be amended by the SWG following a formal review process

##  Detailed Guidelines – Long Term Planning and Outreach

* Ideally the SWG will establish plans for upcoming major releases for at least the following 9-12 months
  * *Rationale:*  this provides transparency for the users and potential users of CDM (supporting adoption)
    
* Major release schedules shall be published on the CDM GitHub repository once approved by the SWG (in the [roadmap](https://github.com/finos/common-domain-model/blob/master/ROADMAP.md)).
  * *Rationale:* as above

##  Detailed Guidelines – Changes vs.  Major Versions

* Breaking changes (as defined in the [change control guidelines](change-control-guidelines#backward-compatibility)) can only be implemented in a major version
  - *Rationale:* this is required to ensure that within a single major version there is stability across minor versions.
    
* Changes (PRs) will be categorized into those that can only be done in major releases (because they contain breaking changes) and others.  PRs requiring a major release shall only be approved for major releases.
  - *Rationale:* this is necessary to ensure that the meaning of major releases is enforced
    
* Even in a new major version, changes that are contrary to the change control guidelines will not be approved unless the SWG executes an exception process.
  - *Rationale:* this is required to ensure that CDM provides stability across major versions, in terms of functionality that is supported

* When a major version includes breaking changes, the SWG will endeavour to ensure that appropriate migration guides and transition plans are in place
  - *Rationale:*  this is to support CDM users in migrating to new versions of CDM
