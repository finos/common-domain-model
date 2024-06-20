---
title: Reference Data Model
---

The CDM only integrates the reference data components that are
specifically needed to model the in-scope products, events, legal
agreements and function components.

This translates into the representation of the **party** and **legal
entity**.

Parties are not explicitly qualified as a legal entity or a natural
person, although the model provides the ability to associate a person
(or set of persons) to a party, which use case would imply that such
party would be a legal entity (even if not formally specified as such).

The `LegalEntity` type is used when only a legal entity reference is
appropriate i.e. the value would never be that of a natural person.

``` Haskell
type Party:
  [metadata key]
  partyId PartyIdentifier (1..*)
  name string (0..1)
    [metadata scheme]
  businessUnit BusinessUnit (0..*)
  person NaturalPerson (0..*)
  personRole NaturalPersonRole (0..*)
  account Account (0..1)
  contactInformation ContactInformation (0..1)
```

``` Haskell
type NaturalPerson:
  [metadata key]
  personId PersonIdentifier (0..*)
    [metadata scheme]
  honorific string (0..1)
  firstName string (0..1)
  middleName string (0..*)
  initial string (0..*)
  surname string (0..1)
  suffix string (0..1)
  dateOfBirth date (0..1)
```

``` Haskell
type LegalEntity:
  [metadata key]
  entityId string (0..*)
    [metadata scheme]
  name string (1..1)
    [metadata scheme]
```
