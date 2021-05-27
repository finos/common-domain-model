package cdm.base.datetime.functions;

import cdm.base.datetime.BusinessCenterEnum;
import cdm.base.datetime.BusinessCenters;
import com.google.inject.Inject;
import com.rosetta.model.lib.records.Date;
import com.rosetta.model.lib.records.DateImpl;
import org.isda.cdm.functions.AbstractFunctionTest;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RetrieveBusinessCenterHolidaysImplTest extends AbstractFunctionTest {

    @Inject
    private final RetrieveBusinessCenterHolidaysImpl func = new RetrieveBusinessCenterHolidaysImpl();

    public final BusinessCenterEnum london = BusinessCenterEnum.GBLO;
    public final BusinessCenterEnum target = BusinessCenterEnum.EUTA;
    public final BusinessCenterEnum us = BusinessCenterEnum.USGS;

    public final List<Date> londonHolidays2021 = List.of(
            DateImpl.of(2021,1,1),
            DateImpl.of(2021,4,2),
            DateImpl.of(2021,4,5),
            DateImpl.of(2021,5,3),
            DateImpl.of(2021,5,31),
            DateImpl.of(2021,8,30),
            DateImpl.of(2021,12,27),
            DateImpl.of(2021,12,28)
    );

    public final List<Date> usGsHolidays2021 = List.of(
            DateImpl.of(2021,1,1),
            DateImpl.of(2021,1,18),
            DateImpl.of(2021,2,15),
 //           DateImpl.of(2021,04,02),
            DateImpl.of(2021,5,31),
            DateImpl.of(2021,7,4),
            DateImpl.of(2021,9,6),
            DateImpl.of(2021,10,11),
            DateImpl.of(2021,11,11),
            DateImpl.of(2021,12,24)
    );

    public final List<Date> targetHolidays2021 = List.of(
            DateImpl.of(2021,1,1),
            DateImpl.of(2021,4,2),
            DateImpl.of(2021,4,5),
            DateImpl.of(2021,5,1)
    );

    public final List<Date> londonAndUsGsHolidays2021 = List.of(
            DateImpl.of(2021,1,1),
            DateImpl.of(2021,1,18),
            DateImpl.of(2021,2,15),
            DateImpl.of(2021,4,2),
            DateImpl.of(2021,4,5),
            DateImpl.of(2021,5,3),
            DateImpl.of(2021,5,31),
            DateImpl.of(2021,7,4),
            DateImpl.of(2021,8,30),
            DateImpl.of(2021,9,6),
            DateImpl.of(2021,10,11),
            DateImpl.of(2021,11,11),
            DateImpl.of(2021,12,24),
            DateImpl.of(2021,12,27),
            DateImpl.of(2021,12,28)
    );

    public final List<Date> londonAndUsGsAndTargetHolidays2021 = List.of(
            DateImpl.of(2021,1,1),
            DateImpl.of(2021,1,18),
            DateImpl.of(2021,2,15),
            DateImpl.of(2021,4,2),
            DateImpl.of(2021,4,5),
            DateImpl.of(2021,5,1),
            DateImpl.of(2021,5,3),
            DateImpl.of(2021,5,31),
            DateImpl.of(2021,7,4),
            DateImpl.of(2021,8,30),
            DateImpl.of(2021,9,6),
            DateImpl.of(2021,10,11),
            DateImpl.of(2021,11,11),
            DateImpl.of(2021,12,24),
            DateImpl.of(2021,12,27),
            DateImpl.of(2021,12,28)
    );

    public final List<Date> londonAndTargetHolidays2021 = List.of(
            DateImpl.of(2021,1,1),
            DateImpl.of(2021,4,2),
            DateImpl.of(2021,4,5),
            DateImpl.of(2021,5,1),
            DateImpl.of(2021,5,3),
            DateImpl.of(2021,5,31),
            DateImpl.of(2021,8,30),
            DateImpl.of(2021,12,27),
            DateImpl.of(2021,12,28)
    );

    @Test
    void shouldRetrieve() {
        initializeHolidays();

        List<BusinessCenterEnum> list1 = List.of( london, target );
        List<BusinessCenterEnum> list2 = List.of( london, us );
        List<BusinessCenterEnum> list3 = List.of( target);
        List<BusinessCenterEnum> list4 = List.of( london );
        List<BusinessCenterEnum> list5 = List.of( london, target, us );

        BusinessCenters londonBC = BusinessCenters.builder().addBusinessCenterValue(list4);
        BusinessCenters londonTargetBC = BusinessCenters.builder().addBusinessCenterValue(list1);
        BusinessCenters londonUsBC = BusinessCenters.builder().addBusinessCenterValue(list2);
        BusinessCenters targetBC = BusinessCenters.builder().addBusinessCenterValue(list3);
        BusinessCenters londonTargetUSBC = BusinessCenters.builder().addBusinessCenterValue(list5);

        BusinessCenters londonTargetBCRef = BusinessCenters.builder().setBusinessCentersReferenceValue(londonTargetBC);
        BusinessCenters londonUsBCRef = BusinessCenters.builder().setBusinessCentersReferenceValue(londonUsBC);
        BusinessCenters targetBCRef = BusinessCenters.builder().setBusinessCentersReferenceValue(targetBC);

        check(londonHolidays2021, func.evaluate(londonBC));
        check(londonAndTargetHolidays2021, func.evaluate(londonTargetBC));
        check(londonAndUsGsAndTargetHolidays2021, func.evaluate(londonTargetUSBC));
        check(targetHolidays2021, func.evaluate(targetBC));

        check(londonAndTargetHolidays2021, func.evaluate(londonTargetBCRef));
        check(londonAndUsGsHolidays2021, func.evaluate(londonUsBCRef));
        check(targetHolidays2021, func.evaluate(targetBCRef));
    }

    void initializeHolidays() {
        func.setHolidays(london, londonHolidays2021);
        func.setHolidays(us, usGsHolidays2021);
        func.setHolidays(target,targetHolidays2021);
    }


    void check(List<? extends Date> expected, List<? extends Date> actual) {
        assertEquals(expected.size(), actual.size());
        int n = expected.size();
        for(int i = 0; i < n; i++) {
            assertEquals(expected.get(i), actual.get(i));
        }
    }
}
