
# CDM Contributrion Workflow
![CDM Contribution Workflow](../.github/cdm-contribution-workflow.png)

# Issues

## A:  Raising Issues

[GitHub Issues](https://github.com/finos/common-domain-model/issues) are used as the primary method for tracking anything to do with the CDM and anyone involved in the project may raise an Issue.  There are three types of Issue and each has a prescribed template:

- **Feature:** used to suggest functional improvements to the CDM.
- **Task:**  used to break down larger features into actionable units of work.
- **Bug:**  used to report a bug or unexpected behaviour in the system.

General questions or discussions on a topic should be raised as a [Discussion](https://github.com/finos/common-domain-model/discussions) in GitHub, not an Issue. Working Group meeting agendas and minutes were also managed as Issues in the past; they have now been migrated to Discussions.

## B:  Issue Triage

When an Issue is created, it will automatically be tagged with the label “Triage”.

The CDM Release Management team should pick up the new Issue and perform the following activities:

First, review the **Issue quality**; contact the creator if necessary (or make minor updates directly):

Next, **tag the Issue**:

1. Add appropriate Labels:
2. **Assign the Issue** to a Working Group by adding as a Project
3. If known, the assignee, milestone and relationship should be assigned on the Issue

Once all the above activities are complete, remove the “Triage” label.

## C: Adding Issues to WG Agendas

All of the Working Groups follow a consistent process of agenda curation and review of Issues and PRs.  These items are tracked through a lifecycle on the Kanban board in GitHub through the following status changes:

![CDM Contribution Workflow](../.github/cdm-wg-lifecycle.png)


- Items may be moved from No Status to Pipeline and Current by the Release Management team, by the WG Chair, or (in most cases) by the Issue creator.
- The Release Management team is responsible for this process for the SWG/CDM/DWG.
- The assigned team member should follow this process:

During the WG meeting

- The WG chair should use the WG Kanban board (project board) to drive the meeting agenda
- Suggest they start with items in the Follow-up column before moving to Current
- Ensure the attendees come to a clear and unanimous decision (encourage approved unless objected to, or voting if necessary)
- Annotate the item (Issue or PR) with the decision and any commentary
  
### D) — Issue Approval at WG:

- The Working Group discusses the issue and decides whether to approve it. At this point, if the issue is approved, the issue is categorised by the WG members as either:

    - Minor - This is where the issue is not complex or contentious enough, or does not require further discussion, so the PR raised does not need to be re-reviewed by the working group
    - Major - This is where the issue is deemed important enough that the PR requires review on the Working Group once it is created, which is shown in Steps G-J
 
 - The Issue is then assigned to a maintainer on the call, for when the PR is created and ready to be reviewed

- If the issue is not approved, then it will continue onto Step E

### E) — Follow-up:

- Feedback, clarifications, or requested changes are gathered. The issue is then sent back into the review cycle (to be discussed for a WG meeting). 
- If the issue is approved, then the contributer can create a Pull Request (PR) for the issue.

## Pull Request Creation & Review
### F) — Pull Request Raised:

- A developer submits a PR that implements the approved issue. This step happens after issue approval.
- Release Management will create the Release PR suitable for maintainer reviewal and this is the only PR that shall be reviewed by CDM maintainers

### G) — Triage & Linked to Issue:

- Release Management will briefly review the PR and link it to the issue.

 - The PR is classified as either:

   - Major - Goes to H
   - Minor - Skips the need for the PR raised to go through WG approval, the Release PR is created, also linked to the issue, and goes directly to L

## Major PR Path

### H) - Scheduled for WG Meeting

- Release Management will add the PR onto the Pull Request project tab. This will initially be put in "Pipeline" until the point the contributer deems it as "Ready", which is marked on step H. 
- Once the PR is in the "Current"column, it will be discussed on the next WG Meeting.

### J) — PR Approval at WG
- The Working Group reviews the pull request. If the PR is not approved, then it moves to Step K.

### K) — Follow-up
- Feedback or requested changes are made. The PR is then re-reviewed on the net WG call.

- If the PR is approved, then the Release PR can be created and is moved on for maintainer review.

## Maintainer Review & Release
### L) — Maintainers Assigned:

- One or more assigned maintainers are able to review the Release PR.

### M) — Maintainers Review:

- Maintainers assigned to the Release PR will review the PR once it is ready to be reviewed.

### N) — Maintainers Approval:

- Maintainers formally approve the Release PR.

### P) — Merge & Schedule Release:

- The PR is merged into the main codebase, and the changes are ready for inclusion in an upcoming release.

### Q) — Closed:

- The process is complete. The issue/PR are officially closed.

Use Control + Shift + m to toggle the tab key moving focus. Alternatively, use esc then tab to move to the next interactive element on the page.
 
