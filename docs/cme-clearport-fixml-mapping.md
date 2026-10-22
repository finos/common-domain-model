# CME ClearPort & CME IRS Submission FIXML 5.0 SP2 to FINOS CDM Ingestion Mapping

## Overview

This specification documents the end-to-end ingestion mapping from **CME ClearPort** and **CME Cleared IRS Submission (`cme-submission-irs-1-0`)** **FIXML 5.0 SP2** `TradeCaptureReport` (`TrdCaptRpt`) messages into the **FINOS Common Domain Model (CDM)**.

Following the **CDM Contribution Review Working Group (CRWG)** guidance (`#5216` / `#5249`), this module provides:

1. **Declarative XML Deserialisation (`RosettaXMLConfiguration`)**: `rosetta-source/src/main/resources/xml-config/cme-clearport-fixml-xml-config.json` binds the XML elements and attributes of `http://www.fixprotocol.org/fixt11` directly onto the Rune DSL syntax tree in `namespace cdm.ingest.cme.clearport.tradecapture`.
2. **Standard `[ingest XML]` Rune DSL Entry Points**:
   - `Ingest_CmeClearPortFixmlToTradeState` (`CmeClearPortFixmlDocument -> TradeState`)
   - `Ingest_CmeClearPortFixmlToWorkflowStep` (`CmeClearPortFixmlDocument -> WorkflowStep`)
3. **Unified Pre-Clearing (`cme-submission-irs-1-0`) & Post-Clearing (`ClearPort`) Coverage**: Supports both post-clearing CME ClearPort `TrdCaptRpt` confirmations (carrying `TrdID`, `RptID`, and `Instrmt` blocks) and pre-clearing CME IRS Submission `TrdCaptRpt` payloads (carrying `<Hdr>`, `<Instrmt SecTyp="IRS"/>`, `<TrdRegTS>`, and `<RptSide>` / `<Pty>` blocks derived from the CDM `cme-submission-irs-1-0` suite).

---

## Architecture & File Layout

| Artifact | Path | Purpose |
| :--- | :--- | :--- |
| **FIXML Input Types** | `rosetta-source/src/main/rosetta/ingest-cme-clearport-tradecapture-type.rosetta` | Defines `CmeClearPortFixmlDocument` (`[rootType]`), `CmeClearPortTradeCaptureReport`, `CmeClearPortHeader`, `CmeClearPortInstrument`, `CmeClearPortRegulatoryTimestamp`, `CmeClearPortReportSide`, and `CmeClearPortParty` in `namespace cdm.ingest.cme.clearport.tradecapture`. |
| **Ingestion Functions** | `rosetta-source/src/main/rosetta/ingest-cme-clearport-tradecapture-func.rosetta` | Defines `[ingest XML]` functions `Ingest_CmeClearPortFixmlToTradeState` and `Ingest_CmeClearPortFixmlToWorkflowStep`, plus scalar-parameterized `Ingest_CmeClearPortTradeCaptureToTradeState` and `Ingest_CmeClearPortTradeCaptureToWorkflowStep`. |
| **Rune XML Config** | `rosetta-source/src/main/resources/xml-config/cme-clearport-fixml-xml-config.json` | Maps `<FIXML>`, `<TrdCaptRpt>`, `<Hdr>`, `<Instrmt>`, `<TrdRegTS>`, `<RptSide>`, and `<Pty>` XML elements and attributes via `RosettaObjectMapperCreator.forXML(...)`. |
| **Post-Clearing Samples** | `examples/samples/cme-clearport/cme-clearport-tradecapture-sample.xml`<br>`examples/samples/cme-clearport/cme-clearport-swap-settlement-sample.xml` | Post-clearing CME ClearPort `TradeCaptureReport` fixtures (Cleared Futures & Cleared USD SOFR IRS). |
| **Pre-Clearing Samples** | `examples/samples/cme-clearport/cme-submission-irs-1-0-usd-irs-sample.xml`<br>`examples/samples/cme-clearport/cme-submission-irs-1-0-usd-ois-sofr-sample.xml` | Pre-clearing CME IRS Submission fixtures aligned with the CDM `cme-submission-irs-1-0` suite (`USD-IRS-1` & `USD-OIS-SOFR-1`). |
| **JUnit 5 Test Suite** | `examples/src/test/java/org/finos/cdm/example/ingestion/IngestCmeClearPortFixmlTest.java` | Verifies XML deserialisation and CDM ingestion across all four fixtures. |

---

## Attribute & Element Mapping Matrix

### 1. Trade & Event Identifiers

| FIXML 5.0 SP2 XPath | FIX Tag | Rune Input Attribute | Target CDM Path | Identifier Type / Notes |
| :--- | :--- | :--- | :--- | :--- |
| `/FIXML/TrdCaptRpt/@TrdID` | `Tag 1003` (` TradeID `) | `CmeClearPortTradeCaptureReport -> trdId` | `TradeState -> trade -> tradeIdentifier[0]` | `TradeIdentifierTypeEnum -> UniqueTransactionIdentifier` (Primary Cleared Trade ID) |
| `/FIXML/TrdCaptRpt/@ExecID` | `Tag 17` (` ExecID `) | `CmeClearPortTradeCaptureReport -> execId` | `TradeState -> trade -> tradeIdentifier[0]` | Fallback primary trade identifier for pre-clearing `cme-submission-irs-1-0` messages |
| `/FIXML/TrdCaptRpt/RptSide/@OrigTrdID` | `Tag 1126` (` OrigTradeID `) | `CmeClearPortReportSide -> origTradeId` | `TradeState -> trade -> tradeIdentifier[1..*]` | `TradeIdentifierTypeEnum -> UniqueTransactionIdentifier` (Originating platform trade ID, e.g., `MKSA`) |
| `/FIXML/TrdCaptRpt/@RptID` | `Tag 571` (` TradeReportID `) | `CmeClearPortTradeCaptureReport -> rptId` | `WorkflowStep -> eventIdentifier[0]` | Lifecycle report event identifier (defaults to `execId` when `@RptID` is omitted) |

### 2. Trade Date, Header & Regulatory Timestamps

| FIXML 5.0 SP2 XPath | FIX Tag | Rune Input Attribute | Target CDM Path | Notes |
| :--- | :--- | :--- | :--- | :--- |
| `/FIXML/TrdCaptRpt/@TrdDt` | `Tag 75` (` TradeDate `) | `CmeClearPortTradeCaptureReport -> trdDt` | `TradeState -> trade -> tradeDate` | Direct ISO-8601 `date (1..1)` binding |
| `/FIXML/TrdCaptRpt/@TxnTm` | `Tag 60` (` TransactTime `) | `CmeClearPortTradeCaptureReport -> txnTm` | `WorkflowStep -> timestamp[0]` | `EventTimestampQualificationEnum -> transactionCreationDateTime` |
| `/FIXML/TrdCaptRpt/Hdr/@SID` / `@TID` | `Tag 49` / `Tag 56` | `CmeClearPortHeader -> senderId` / `targetId` | `WorkflowStep -> messageInformation` | Populates `sentBy` (`MKSA`) and `sentTo` (`CME`) |

### 3. Counterparty & Clearing Party Roles (`RptSide / Pty`)

All `/FIXML/TrdCaptRpt/RptSide/Pty` elements (`ClearPort` and `cme-submission-irs-1-0`) are normalised into `TradeState -> trade -> party` and `TradeState -> trade -> partyRole`:

| FIXML `Pty/@R` (`PartyRole` Tag 452) | FIXML `Pty/@Src` (`PartyIDSource` Tag 447) | Target CDM `PartyRoleEnum` | Description |
| :--- | :--- | :--- | :--- |
| `R="4"` | `C` / `D` / `G` / `N` | `PartyRoleEnum -> ClearingFirm` | Futures Commission Merchant (FCM) / Clearing Member (`955`, `DIRECT_SELF_CLEARING`) |
| `R="7"` | `D` / `N` (`LEI`) | `PartyRoleEnum -> ExecutingEntity` | Trading / Executing Firm (`Michigan Avenue Bank - Executing Bank`, LEI `54930059B0N5BT694847`) |
| `R="24"` | `C` / `D` / `N` | `PartyRoleEnum -> ClearingClient` | Customer / Position Account (`ONB2-TEST-I`, `EVENTTEST1`) |
| `R="1"` (or default) | `C` / `D` / `N` | `PartyRoleEnum -> Counterparty` | Executing Firm / Trade Originator |

---

## Programmatic Usage (Java / `RosettaObjectMapperCreator`)

``` Java
ObjectMapper cmeXmlMapper = getXmlMapper("xml-config/cme-clearport-fixml-xml-config.json");
URL url = Resources.getResource("ingest/input/cme-clearport/cme-clearport-tradecapture-sample.xml");
CmeClearPortFixmlDocument fixmlDoc = cmeXmlMapper.readValue(url, CmeClearPortFixmlDocument.class);

TradeState tradeState = ingestToTradeState.evaluate(fixmlDoc);
WorkflowStep workflowStep = ingestToWorkflowStep.evaluate(fixmlDoc);
```
