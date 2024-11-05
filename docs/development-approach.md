## Agile Development Approach

The on-going development of the CDM adheres to a methodology inspired by
the *Agile* software development framework. It focuses on generating business value from the user's perspective through delivery of small, releasable changes contribuiting that business value. 

Development by the CDM Maintainer team is being planned along a series
of 2-week *sprints* aligned onto the CDM Architecture and Review
Committee cycle. This development is guided by high-level priorities set
on a quarterly basis. 

## Focus on business value

Any CDM development work must start from a business case describing the
business benefit being sought from the proposed development, as seen
from the perspective of the user who will enjoy that benefit. This is
know as a *user story* in the agile framework.

### What a user story looks like

A good user story comprises three elements which can be summarised into
one sentence: who, what and why.

-   *Who* defines the user (or more generally the set of users defined
    by some profiling) that will enjoy that benefit
-   *What* defines the feature to be delivered, as a verbal proposition
    applying to the user - i.e. "the user can do this or that"
-   *Why* specifies the benefit, i.e. what makes this feature important
    for the user

Since a story is from the user's perspective, it must be written in
plain language, or at least in language that is intelligible by that
user. It must be free of technical jargon that only the developer of
that feature may understand, so that it can be communicated to the user.

Further details about the business case (including documents, pictures,
sample data etc.) may be attached to a user story to complement that
summary, but the sumamry itself should be whole and self-explanatory.

### Story vs task

By contrast, how a story gets delivered is *not* part of that story:

-   *How* describes the set of *tasks* that will need to be executed to
    deliver the story. This is where the story is being decomposed into
    units of work written in terms that are actionable by the
    developers.

Tasks will typically map to steps in the software development lifecycle:
analysis, design, build, test, deploy. They must be planned before the
story is scheduled for development, as part of the *sprint planning*
process. Based on this planning, a set of stories is being prioritised
for development in the upcoming sprint. Those stories are communicated
to users at the Architecture and Review Committee, but not their
underlying tasks or techical details.

### Some examples

Instead of writing:

> "Commodity Swap Follow Up w/Enum values, mapping, samples"

Which is unclear, assumes some context which not all users may have
("follow-up" from what?) and has no explicit benefit, write:

> "A Commodity user of the CDM can map a set of basic Commodity
> attributes to represent simple Commodity derivative products."

In which some of the underlying tasks may be:

> -   "Map basic Commodity enumerations"
> -   "Add Commodity samples to the ingestion test pack"
> -   etc.

As a rule, a task is written in the imperative mode as an injunction to
the developer, whereas a story should be written as a sentence starting
with the user's profile as the subject of a verbal proposition. A story
written in the imperative mode is more likely a task and improperly
written.

For instance, instead of writing:

> "Release member contribution for DayCountFraction."

Write:

> "A user of interest rate products is able to model products that use
> the ACT/364 day count convention in the CDM."

Where the "Release" injunction is attached to a deploy-type task.

## Delivery of small releasable units

### What is a releasable unit?

To maintain on-going momentum in the development of the CDM, delivery is
organised around small but releasable units of change. This means that
any change must be small enough to be achievable during a single sprint
(usually), but large enough to be releasable as a cohesive whole. In
particular, a change unit should not be regressive or break existing
functionality, even if only temporarily (except when retiring such
functionality is the purpose of that change) - in agile terms, it must
be *shippable*. This principle applies to on-going development by the
CDM Maintainers as well as to outside contributions.

**The CDM development approach aligns the concepts of user story and
releasable unit**, therefore stories should be calibrated to be
achievable during a single sprint.

> 1 contribution = 1 releasable unit = 1 user story

---
**Note:**
A CDM [release](versioning.md) may contain more than 1 releasable
unit. Every unit should still be shippable in isolation, even if they
may end-up being shipped as a group.

---

### Epics

Some larger changes may not be achievable in a single sprint: e.g. if
they impact a large number of objects or core features of the model.
Such changes are known as *epics* and need to be decomposed into several
user stories. Developers or contributors are responsible for ensuring
that the changes are being delivered in small, incremental units and
must plan accordingly.

Particularly for complex stories, not all of that story's tasks may
necessarily be known in advance and therefore guaranteed to fit in one
single sprint. A story may demand some prior analysis before it can be
decomposed into development tasks. It may also require several design
iterations before development can start. Those prior discovery tasks
should be fit into a single sprint and the actual development scheduled
in a subsequent sprint.

The discovery phase may reveal that the story is not well calibrated and
is in fact an epic that should be further decomposed. This is an
acceptable scenario which does not contravene the prescribed development
approach, as long as development has not yet started. In that case the
story should be requalified and several stories be spun-out as a result,
before development can start.



