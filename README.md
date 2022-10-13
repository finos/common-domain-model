[![FINOS - Incubating](https://cdn.jsdelivr.net/gh/finos/contrib-toolbox@master/images/badge-incubating.svg)](https://community.finos.org/docs/governance/Software-Projects/stages/incubating)

# CDM - Common Domain Model

## What is the CDM

The Common Domain Model (CDM) is a standardised, machine-readable and machine-executable blueprint for how financial products are traded and managed across the transaction lifecycle. It is represented as a `domain model`_ and distributed in open source.

## Purpose

A single, digital processing standard for trade events and actions enhances financial markets' operational efficiency in several ways:

* **Enables inter-operability and straight-through processing** across firms, solutions and platforms, reducing the need for reconciliation caused by variations in how each firm records trade lifecycle events.
* **Accelerates financial technology innovation** by providing a common, readily operational foundation for how technologies like distributed ledger, smart contracts, cloud computing, and artificial intelligence can be applied to financial markets.
* **Delivers better regulatory oversight**, promotes transparency and alignment between regulators and market participants and enables consistency in regulatory reporting, by providing a standard representation of trade data and supporting machine executable reporting requirements.

## Design Principles

To support these objectives, the CDM is built according to a set of design principles that include the following concepts:

* **Normalisation** through abstraction of common components
* **Composability** where objects are composed and qualified from the bottom up
* **Mapping** to existing industry messaging formats
* **Embedded logic** to represent industry processes
* **Modularisation** into logical layers

These design principles are further detailed in the :ref:`design-principles` section of the CDM documentation.

## Governance

The CDM [governance framework](https://cdm.docs.rosetta-technology.io/source/contribution.html#governance) regulates the development of the CDM in open source.

## Scope

The product scope of the CDM includes OTC derivatives, cash securities, securities financing, and commodities.

## CDM Components


**The CDM is made of three sets of components**, as laid-out in the CDM components diagram below:

* The ISDA CDM Distribution (in *blue*)
* The Rosetta DSL (in *grey*)
* CDM Applications (in *green*)

.. figure:: images/cdm-components-diagram.png

## ISDA CDM Distribution

The ISDA CDM distribution is openly accessible to all industry participants, subject to the ISDA CDM open source licence. This distribution is fully downloadable.

## Installation

OS X, Linux and Windows:

```sh
mvn clean install
```

## Usage example

A few motivating and useful examples of how your project can be used. Spice this up with code blocks and potentially screenshots / videos ([LiceCap](https://www.cockos.com/licecap/) is great for this kind of thing).

_For more examples and usage, please refer to the [Wiki][wiki]._

## Development setup

Describe how to install all development dependencies and how to run an automated test-suite of some kind. Potentially do this for multiple platforms.

```sh
make install
npm test
```


## Roadmap

 TO BE FILLED IN BY ISDA/ICMA/ISLA

## Contributing


### Contribution via Rosetta

The [Rosetta Design](https://rosetta-technology.io/design) application can be used to contribute to the CDM without setting up any development environment. Rosetta Design’s [source control integration](https://docs.rosetta-technology.io/rosetta/rosetta-products/1-workspace/#source-control-integration) means that a PR is automatically created to a fork of the FINOS CDM under the [Rosetta Models](https://github.com/rosetta-models) GitHub Organisation.

Steps:
  1. Create a Workspace for the CDM in Rosetta Design
  2. Review change in Rosetta Design
  3. Contribute change in Rosetta Design which will create PR in the rosetta-models organisation
  4. Create a PR to the Finos Github. See instructions [here](https://docs.github.com/en/pull-requests/collaborating-with-pull-requests/proposing-changes-to-your-work-with-pull-requests/creating-a-pull-request-from-a-fork)


### Contribution via Legend

FINOS TO FILL IN: SECTION ON CONTRIBUTING VIA LEGEND


### Contributing via GitHub

1. Fork it (<https://github.com/finos/{project slug}/fork>)
2. Create your feature branch (`git checkout -b feature/fooBar`)
3. Read our [contribution guidelines](.github/CONTRIBUTING.md) and [Community Code of Conduct](https://www.finos.org/code-of-conduct)
4. Commit your changes (`git commit -am 'Add some fooBar'`)
5. Push to the branch (`git push origin feature/fooBar`)
6. Create a new Pull Request

_NOTE:_ Commits and pull requests to FINOS repositories will only be accepted from those contributors with an active, executed Individual Contributor License Agreement (ICLA) with FINOS OR who are covered under an existing and active Corporate Contribution License Agreement (CCLA) executed with FINOS. Commits from individuals not covered under an ICLA or CCLA will be flagged and blocked by the FINOS Clabot tool (or [EasyCLA](https://community.finos.org/docs/governance/Software-Projects/easycla)). Please note that some CCLAs require individuals/employees to be explicitly named on the CCLA.

*Need an ICLA? Unsure if you are covered under an existing CCLA? Email [help@finos.org](mailto:help@finos.org)*


## License

TO BE FILLED IN BY ISDA/ICMA/ISLA

?? Copyright 2022 

?? Distributed under the [Apache License, Version 2.0](http://www.apache.org/licenses/LICENSE-2.0).

?? SPDX-License-Identifier: [Apache-2.0](https://spdx.org/licenses/Apache-2.0)
