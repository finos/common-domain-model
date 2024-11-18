package cdm.base.staticdata.party.processor;

import cdm.base.staticdata.party.metafields.ReferenceWithMetaParty;
import cdm.base.staticdata.party.metafields.ReferenceWithMetaParty.ReferenceWithMetaPartyBuilder;
import com.regnosys.rosetta.common.translation.Mapping;
import com.regnosys.rosetta.common.translation.MappingContext;
import com.regnosys.rosetta.common.translation.Path;
import com.rosetta.model.lib.path.RosettaPath;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TradeSideToPartyMappingProcessorTest {

    private static final String TRADE_SIDE_1 = "side1";
    private static final String TRADE_SIDE_2 = "side2";
    private static final String PARTY_A = "partyA";
    private static final String PARTY_B = "partyB";
    private static final String NOT_MAPPED_ERROR = "Not mapped";

    // model path is outside product
    private RosettaPath PRODUCT_PATH = RosettaPath.valueOf("WorkflowStep.businessEvent.after(0).collateral.independentAmount");

    private MappingContext mappingContext;

    @BeforeEach
    void setUp() {
        List<Mapping> mappings = Arrays.asList(
                getErrorMapping(Path.parse("TrdCaptRpt.Instrmt.SecXML.FpML.trade.swap.swapStream[1].payerPartyReference.href"), TRADE_SIDE_1, null, NOT_MAPPED_ERROR),
                getErrorMapping(Path.parse("TrdCaptRpt.Instrmt.SecXML.FpML.trade.swap.swapStream[1].receiverPartyReference.href"), TRADE_SIDE_2, null, NOT_MAPPED_ERROR),
                getErrorMapping(Path.parse("TrdCaptRpt.Instrmt.SecXML.FpML.trade.swap.tradeSide[0].id"), TRADE_SIDE_1, null, NOT_MAPPED_ERROR),
                getErrorMapping(Path.parse("TrdCaptRpt.Instrmt.SecXML.FpML.trade.swap.tradeSide[1].id"), TRADE_SIDE_2, null, NOT_MAPPED_ERROR),
                getErrorMapping(Path.parse("TrdCaptRpt.Instrmt.SecXML.FpML.trade.swap.tradeSide[0].orderer.party.href"), PARTY_A, PARTY_A, NOT_MAPPED_ERROR),
                getErrorMapping(Path.parse("TrdCaptRpt.Instrmt.SecXML.FpML.trade.swap.tradeSide[1].orderer.party.href"), PARTY_B, PARTY_B, NOT_MAPPED_ERROR));
        mappingContext = new MappingContext(mappings, Collections.emptyMap(), null, null);
    }

    @Test
    void shouldMapTradeSide1ToPartyA() {
        ReferenceWithMetaPartyBuilder builder = ReferenceWithMetaParty.builder();

        Path synonymPath = Path.parse("TrdCaptRpt.Instrmt.SecXML.FpML.trade.swap.swapStream[1].payerPartyReference");
        TradeSideToPartyMappingProcessor processor =
                new TradeSideToPartyMappingProcessor(PRODUCT_PATH, Collections.emptyList(), mappingContext);
        processor.map(synonymPath, builder, null);

        assertEquals(PARTY_A, builder.getExternalReference());
    }

    @Test
    void shouldMapTradeSide2ToPartyB() {
        ReferenceWithMetaPartyBuilder builder =  ReferenceWithMetaParty.builder();

        Path synonymPath = Path.parse("TrdCaptRpt.Instrmt.SecXML.FpML.trade.swap.swapStream[1].receiverPartyReference");
        TradeSideToPartyMappingProcessor processor =
                new TradeSideToPartyMappingProcessor(PRODUCT_PATH, Collections.emptyList(), mappingContext);
        processor.map(synonymPath, builder, null);

        assertEquals(PARTY_B, builder.getExternalReference());
    }

    private Mapping getErrorMapping(Path xmlPath, String xmlValue, Object rosettaValue, String error) {
        return new Mapping(xmlPath, xmlValue, null, rosettaValue, error, false, false, false);
    }
}