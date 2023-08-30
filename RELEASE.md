# _Examples - Add Repo Demo to Examples._

_Background_

ICMA's CDM for Repo and Bonds initiative is intended to support the digitalization of the 
repo market through the adoption of a standardized domain model and lifecycle events based on 
industry best practices and standards.  

The repo elements of the model were designed with participation and contribution 
of the ICMA CDM for Repo and Bonds Steering Committee.

_Background_


Repo lifecycle events are supported through a set of functions that accept
a small set of inputs to auto-generate primitive instructions needed to execute
business events. Repo lifecycle events include, rolling, re-rating, interium payments, 
pair-off and shaping.

#### _What is being released?_

This release includes a working trading application in Java that demonstrates how to create 
repurchase transactions using CDM objects and functions. The demo includes creating 
a new repurchase transaction, rolling, re-rating, terminating, and interest payments
using CDM `PrimitiveInstruction` objects and `Create_BusinessEvent` and other functions.

To run this demo, rebuild the sources in com.icma.cdm.example, open the class RepoTradingApp and
run main(). Examples of creating CDM objects and executing business events can be found in the IcmaRepoUtil,
RepoExecutionCreation, and RepoLifeCycle files.
