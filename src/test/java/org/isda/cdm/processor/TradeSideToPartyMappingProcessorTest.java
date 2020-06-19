package org.isda.cdm.processor;

import cdm.base.staticdata.party.metafields.ReferenceWithMetaParty.ReferenceWithMetaPartyBuilder;
import com.regnosys.rosetta.common.translation.Mapping;
import com.regnosys.rosetta.common.translation.Path;
import com.rosetta.model.lib.path.RosettaPath;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TradeSideToPartyMappingProcessorTest {

    private static final String TRADE_SIDE_1 = "side1";
    private static final String TRADE_SIDE_2 = "side2";
    private static final String PARTY_A = "partyA";
    private static final String PARTY_B = "partyB";
    private static final String NOT_MAPPED_ERROR = "Not mapped";

    private RosettaPath rosettaPath;
    private List<Mapping> mappings;

    @BeforeEach
    void setUp() {
        rosettaPath = RosettaPath.valueOf("Event.payerReceiver.payerPartyReference");
        mappings = Arrays.asList(
                new Mapping(Path.parse("swap.tradeSide[0].id"), TRADE_SIDE_1, null, null, NOT_MAPPED_ERROR, false, false),
                new Mapping(Path.parse("swap.tradeSide[1].id"), TRADE_SIDE_2, null, null, NOT_MAPPED_ERROR, false, false),
                new Mapping(Path.parse("swap.tradeSide[0].orderer.party.href"), PARTY_A, null, PARTY_A, NOT_MAPPED_ERROR, false, false),
                new Mapping(Path.parse("swap.tradeSide[1].orderer.party.href"), PARTY_B, null, PARTY_B, NOT_MAPPED_ERROR, false, false));
    }

    @Test
    void shouldMapTradeSide1ToPartyA() {
        ReferenceWithMetaPartyBuilder builder = new ReferenceWithMetaPartyBuilder();
        builder.setExternalReference(TRADE_SIDE_1);

        Path synonymPath = Path.parse("swap.tradeSide[0].id");
        TradeSideToPartyMappingProcessor processor =
                new TradeSideToPartyMappingProcessor(rosettaPath, Collections.emptyList(), mappings);
        processor.map(synonymPath, builder, null);

        assertEquals(PARTY_A, builder.getExternalReference());
    }

    @Test
    void shouldMapTradeSide2ToPartyB() {
        ReferenceWithMetaPartyBuilder builder = new ReferenceWithMetaPartyBuilder();
        builder.setExternalReference(TRADE_SIDE_2);

        Path synonymPath = Path.parse("swap.tradeSide[1].id");
        TradeSideToPartyMappingProcessor processor =
                new TradeSideToPartyMappingProcessor(rosettaPath, Collections.emptyList(), mappings);
        processor.map(synonymPath, builder, null);

        assertEquals(PARTY_B, builder.getExternalReference());
    }
}