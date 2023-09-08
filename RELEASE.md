# _CDM Party Role - Compression Service Provider_

_Background_

The existing party roles are not sufficient to identify if a counterparty is acting as a third-party service provider or CCP for the compression event. A new party role is needed to uniquely identify if a party is acting as a compression service provider.

_What is being released?_

This release provides the ability to identify a counterparty acting as a compression service provider by updating the relevant enumeration.

_Enumerations_

- Updated `PartyRoleEnum` enumeration with new `CompressionServiceProvider` value to represent counterparties acting as compression service providers.

_Review directions_

In the CDM Portal, select the Textual Browser and inspect the change identified above.
