# _PTRR - Compression Service Provider Party Role_

_Background_

A new party role is needed to uniquely identify if a party is acting as a compression service provider, since the existing party roles are not sufficient to identify if a counterparty is acting as a third-party service provider or CCP for the compression event.
This release will allow us to identify a counterparty acting as a compression service provider.

_What is being released?_

_Enumerations_

- Updated `PartyRoleEnum` enumeration with new `CompressionServiceProvider` value to represent counterparties acting as compression service providers.

_Review directions_

In the CDM Portal, select the Textual Browser and inspect each of the changes identified above.
