# CME ClearPort FIXML 5.0 SP2 Ingestion Mapping to ISDA CDM TradeState

**Author:** Corrente Applied Cryptography Group  
**Email:** `standards@correntelabs.com`  
**Target Specification:** FINOS Common Domain Model (CDM)  
**Related Issue:** FINOS CDM Issue #5204 (Standardized Machine Settlement Binding)  
**Regulatory Standards:** CFTC 17 CFR § 38.151 (CEA Core Principle 2), 17 CFR § 39.12(a)(1) (DCO Fair and Open Access)

---

## 1. Executive Overview

This specification defines the ingestion mapping from **CME ClearPort FIXML 5.0 SP2 TradeCaptureReport (`<TrdCaptRpt>`)** into the canonical **ISDA Common Domain Model (CDM) `TradeState`** and **`WorkflowStep`**.

Under Commodity Futures Trading Commission (CFTC) regulations (17 CFR § 39.12(a)(1)), Derivatives Clearing Organizations (DCOs) must provide fair, open, and non-discriminatory clearing access to market participants who satisfy objective risk and capital standards. By establishing a standardized, deterministic translation from CME ClearPort's clearing wire protocol to CDM Rosetta structures, market participants can ingest, model, and automate lifecycle clearing events (such as Equity DvP, IRS Swap settlement, and Variation Margin calls) without proprietary broker adapters.

---

## 2. Ingestion Mapping Matrix

| FIXML 5.0 SP2 XML Path | Data Type | CDM Rosetta Path (`cdm.event.common.TradeState`) | Description |
| :--- | :--- | :--- | :--- |
| `/FIXML/TrdCaptRpt/@TrdID` | `string` | `trade.tradeIdentifier.assignedIdentifier.identifier` | Unique trade identifier assigned by CME ClearPort |
| `/FIXML/TrdCaptRpt/@RptID` | `string` | `trade.executionDetails.executionIdentifier` | Trade report message identifier |
| `/FIXML/TrdCaptRpt/@ExecID` | `string` | `trade.executionDetails.executionIdentifier` | Execution venue reference identifier |
| `/FIXML/TrdCaptRpt/@TrdDt` | `date` | `trade.tradeDate.date` | Trade execution date |
| `/FIXML/TrdCaptRpt/@TxnTm` | `dateTime` | `trade.executionDetails.executionTimestamp` | Transaction execution timestamp |
| `/FIXML/TrdCaptRpt/@LastPx` | `number` | `trade.tradeLot.priceQuantity.price.value` | Executed transaction price |
| `/FIXML/TrdCaptRpt/@LastQty` | `number` | `trade.tradeLot.priceQuantity.quantity.value` | Executed contract volume / lot quantity |
| `/FIXML/TrdCaptRpt/@NetMny` | `number` | `trade.tradeLot.settlementTerms.settlementAmount` | Notional net settlement cash amount |
| `/FIXML/TrdCaptRpt/@Ccy` | `string` | `trade.tradeLot.priceQuantity.price.unit.currency` | Settlement currency code (ISO 4217) |
| `/FIXML/TrdCaptRpt/Instrmt/@Sym` | `string` | `trade.product.security.symbol` | Contract symbol or instrument ticker (e.g. `ES_FUT`) |
| `/FIXML/TrdCaptRpt/Instrmt/@SecTyp` | `string` | `trade.product.security.securityType` | Instrument classification (`FUT`, `OPT`, `SWP`) |
| `/FIXML/TrdCaptRpt/Instrmt/@ID` | `string` | `trade.product.security.identifier` | Security identifier (ISIN / CUSIP) |
| `/FIXML/TrdCaptRpt/RptSide[@Side="1"]/Pty[@R="1"]/@ID` | `string` | `trade.party[0].partyId` | Buyer counterparty identifier (`PartyRoleEnum -> Counterparty`) |
| `/FIXML/TrdCaptRpt/RptSide[@Side="2"]/Pty[@R="1"]/@ID` | `string` | `trade.party[1].partyId` | Seller counterparty identifier (`PartyRoleEnum -> Counterparty`) |
| `/FIXML/TrdCaptRpt/RptSide/Pty[@R="4"]/@ID` | `string` | `trade.partyRole.role` | Clearing member firm or direct self-clearing entity (`PartyRoleEnum -> ClearingFirm`) |

---

## 3. Rosetta Function Definitions

The formal mapping functions are specified in:
* `rosetta-source/src/main/rosetta/ingest-cme-clearport-tradecapture-func.rosetta`:
  - `Ingest_CmeClearPortTradeCaptureToTradeState`: Parses atomic execution attributes into `TradeState`.
  - `Ingest_CmeClearPortTradeCaptureToWorkflowStep`: Binds trade capture reports to workflow transition states (`NEW`, `CANCEL`, `REPLACE`).

Test XML fixtures demonstrating complete round-trip conformance are included in:
* `examples/samples/cme-clearport/cme-clearport-tradecapture-sample.xml` (Equity Futures DvP)
* `examples/samples/cme-clearport/cme-clearport-swap-settlement-sample.xml` (Interest Rate Swap Settlement)

---

## 4. Reference Implementation

An open-source reference implementation of this translation bridge, along with high-throughput market data sequencing and SPAN 2 margin simulation, is available in the `@corrente-labs/cme-stream` package.
