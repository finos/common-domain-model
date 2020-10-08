package cdm.base.staticdata.party.processor;

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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class TradeSideToPartyMappingProcessorTest {

    private static final String TRADE_SIDE_1 = "side1";
    private static final String TRADE_SIDE_2 = "side2";
    private static final String PARTY_A = "partyA";
    private static final String PARTY_B = "partyB";
    private static final String NOT_MAPPED_ERROR = "Not mapped";

    // model path is outside tradableProduct.product
    private RosettaPath OUTSIDE_PRODUCT_PATH = RosettaPath.valueOf("WorkflowStep.businessEvent.primitives(0).termsChange.after."
            + "execution.collateral.independentAmount");
    // model path is outside tradableProduct.product
    private RosettaPath INSIDE_PRODUCT_PATH = RosettaPath.valueOf("WorkflowStep.businessEvent.primitives(0).termsChange.after."
            + "execution.tradableProduct.product.contractualProduct.economicTerms.payout.interestRatePayout(0).payerReceiver");

    private MappingContext mappingContext;

    @BeforeEach
    void setUp() {
        List<Mapping> mappings = Arrays.asList(
                new Mapping(Path.parse("TrdCaptRpt.Instrmt.SecXML.FpML.trade.swap.swapStream[1].payerPartyReference.href"), TRADE_SIDE_1, null, null, NOT_MAPPED_ERROR, false, false),
                new Mapping(Path.parse("TrdCaptRpt.Instrmt.SecXML.FpML.trade.swap.swapStream[1].receiverPartyReference.href"), TRADE_SIDE_2, null, null, NOT_MAPPED_ERROR, false, false),
                new Mapping(Path.parse("TrdCaptRpt.Instrmt.SecXML.FpML.trade.swap.tradeSide[0].id"), TRADE_SIDE_1, null, null, NOT_MAPPED_ERROR, false, false),
                new Mapping(Path.parse("TrdCaptRpt.Instrmt.SecXML.FpML.trade.swap.tradeSide[1].id"), TRADE_SIDE_2, null, null, NOT_MAPPED_ERROR, false, false),
                new Mapping(Path.parse("TrdCaptRpt.Instrmt.SecXML.FpML.trade.swap.tradeSide[0].orderer.party.href"), PARTY_A, null, PARTY_A, NOT_MAPPED_ERROR, false, false),
                new Mapping(Path.parse("TrdCaptRpt.Instrmt.SecXML.FpML.trade.swap.tradeSide[1].orderer.party.href"), PARTY_B, null, PARTY_B, NOT_MAPPED_ERROR, false, false));
        mappingContext = new MappingContext(mappings, Collections.emptyMap());
    }

    @Test
    void shouldMapTradeSide1ToPartyA() {
        ReferenceWithMetaPartyBuilder parent = new ReferenceWithMetaPartyBuilder();

        Path synonymPath = Path.parse("TrdCaptRpt.Instrmt.SecXML.FpML.trade.swap.swapStream[1].payerPartyReference");
        TradeSideToPartyMappingProcessor processor =
                new TradeSideToPartyMappingProcessor(OUTSIDE_PRODUCT_PATH, Collections.emptyList(), mappingContext);
        processor.map(synonymPath, Optional.empty(), parent);

        assertEquals(PARTY_A, parent.getExternalReference());
    }

    @Test
    void shouldMapTradeSide2ToPartyB() {
        ReferenceWithMetaPartyBuilder parent = new ReferenceWithMetaPartyBuilder();

        Path synonymPath = Path.parse("TrdCaptRpt.Instrmt.SecXML.FpML.trade.swap.swapStream[1].receiverPartyReference");
        TradeSideToPartyMappingProcessor processor =
                new TradeSideToPartyMappingProcessor(OUTSIDE_PRODUCT_PATH, Collections.emptyList(), mappingContext);
        processor.map(synonymPath, Optional.empty(), parent);

        assertEquals(PARTY_B, parent.getExternalReference());
    }

    @Test
    void shouldNotMapTradeSideForPathOutsideProductPath() {
        ReferenceWithMetaPartyBuilder parent = new ReferenceWithMetaPartyBuilder();

        Path synonymPath = Path.parse("TrdCaptRpt.Instrmt.SecXML.FpML.trade.swap.swapStream[1].payerPartyReference");
        TradeSideToPartyMappingProcessor processor =
                new TradeSideToPartyMappingProcessor(INSIDE_PRODUCT_PATH, Collections.emptyList(), mappingContext);
        processor.map(synonymPath, Optional.empty(), parent);

        assertNull(parent.getExternalReference());
    }
}