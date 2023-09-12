# _Event Model -  Enhancements for Party Role Enum - Service Provider_

_Background_

The existing party roles are not sufficient to identify if a counterparty is acting as a third-party service provider or CCP for the compression event. A new party role enum value is needed to uniquely identify a party acting as a compression service provider in the context of the business event.

_What is being released?_

This release provides the ability to associate a party as a compression service provider.

_Enumerations_

- Augmented the `PartyRoleEnum` enumeration with new `CompressionServiceProvider` value to represent counterparties acting as compression service providers.

_Review directions_

In the CDM Portal, select the Textual Browser and inspect the change identified above.

Inspect Pull Request: [#2369](https://github.com/finos/common-domain-model/pull/2369)
