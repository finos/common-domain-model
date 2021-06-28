package cdm.base.datetime.functions;

import cdm.base.datetime.BusinessCenterEnum;
import cdm.base.datetime.BusinessCenters;
import cdm.base.datetime.DateGroup;
import com.google.inject.Inject;
import com.rosetta.model.lib.records.DateImpl;
import org.isda.cdm.functions.AbstractFunctionTest;
import org.junit.jupiter.api.Test;
import com.rosetta.model.lib.records.Date;
import com.rosetta.model.lib.records.DateImpl;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RetrieveBusinessCenterHolidaysImplTest extends AbstractFunctionTest {

    @Inject
    public static final RetrieveBusinessCenterHolidaysImpl func = new RetrieveBusinessCenterHolidaysImpl();

    public static final BusinessCenterEnum london = BusinessCenterEnum.GBLO;
    public static final BusinessCenterEnum target = BusinessCenterEnum.EUTA;
    public static final BusinessCenterEnum us = BusinessCenterEnum.USGS;

    public static final List<Date> londonHolidays2021 = List.of(
            DateImpl.of(2020,12,25),
            DateImpl.of(2020,12,28),
            DateImpl.of(2021,1,1),
            DateImpl.of(2021,4,2),
            DateImpl.of(2021,4,5),
            DateImpl.of(2021,5,3),
            DateImpl.of(2021,5,31),
            DateImpl.of(2021,8,30),
            DateImpl.of(2021,12,27),
            DateImpl.of(2021,12,28)
    );

    public static final List<Date> usGsHolidays2021 = List.of(
            DateImpl.of(2021,1,1),
            DateImpl.of(2021,1,18),
            DateImpl.of(2021,2,15),
 //           DateImpl.of(2021,04,02),
            DateImpl.of(2021,5,31),
            DateImpl.of(2021,7,4),
            DateImpl.of(2021,9,6),
            DateImpl.of(2021,10,11),
            DateImpl.of(2021,11,11),
            DateImpl.of(2021,11,25),
            DateImpl.of(2021,12,24)
    );

    public static final List<Date> targetHolidays2021 = List.of(
            DateImpl.of(2021,1,1),
            DateImpl.of(2021,4,2),
            DateImpl.of(2021,4,5),
            DateImpl.of(2021,5,1)
    );

    public static final List<Date> londonAndUsGsHolidays2021 = List.of(
            DateImpl.of(2020,12,25),
            DateImpl.of(2020,12,28),
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
            DateImpl.of(2021,11,25),
            DateImpl.of(2021,12,24),
            DateImpl.of(2021,12,27),
            DateImpl.of(2021,12,28)
    );

    public static final List<Date> londonAndUsGsAndTargetHolidays2021 = List.of(
            DateImpl.of(2020,12,25),
            DateImpl.of(2020,12,28),
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
            DateImpl.of(2021,11,25),
            DateImpl.of(2021,12,24),
            DateImpl.of(2021,12,27),
            DateImpl.of(2021,12,28)
    );

    public static final List<Date> londonAndTargetHolidays2021 = List.of(
            DateImpl.of(2020,12,25),
            DateImpl.of(2020,12,28),
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
    public static final List<BusinessCenterEnum> lonTargetList = List.of( london, target );
    public static final List<BusinessCenterEnum> lonUsList = List.of( london, us );
    public static final List<BusinessCenterEnum> targetList = List.of( target);
    public static final List<BusinessCenterEnum> londonList = List.of( london );
    public static final List<BusinessCenterEnum> lonTargUsList = List.of( london, target, us );

    public static final BusinessCenters londonBC = BusinessCenters.builder().addBusinessCenterValue(londonList);
    public static final BusinessCenters londonTargetBC = BusinessCenters.builder().addBusinessCenterValue(lonTargetList);
    public static final BusinessCenters londonUsBC = BusinessCenters.builder().addBusinessCenterValue(lonUsList);
    public static final BusinessCenters targetBC = BusinessCenters.builder().addBusinessCenterValue(targetList);
    public static final BusinessCenters londonTargetUSBC = BusinessCenters.builder().addBusinessCenterValue(lonTargUsList);
    public static final BusinessCenters londonTargetBCRef = BusinessCenters.builder().setBusinessCentersReferenceValue(londonTargetBC);
    public static final BusinessCenters londonUsBCRef = BusinessCenters.builder().setBusinessCentersReferenceValue(londonUsBC);
    public static final BusinessCenters targetBCRef = BusinessCenters.builder().setBusinessCentersReferenceValue(targetBC);

    @Test
    void shouldRetrieve() {
        initializeHolidays();

        check(londonHolidays2021, func.evaluate(londonBC));
        check(londonAndTargetHolidays2021, func.evaluate(londonTargetBC));
        check(londonAndUsGsAndTargetHolidays2021, func.evaluate(londonTargetUSBC));
        check(targetHolidays2021, func.evaluate(targetBC));

        check(londonAndTargetHolidays2021, func.evaluate(londonTargetBCRef));
        check(londonAndUsGsHolidays2021, func.evaluate(londonUsBCRef));
        check(targetHolidays2021, func.evaluate(targetBCRef));
    }

    public static void initializeHolidays() {
        func.setHolidays(london, londonHolidays2021);
        func.setHolidays(us, usGsHolidays2021);
        func.setHolidays(target,targetHolidays2021);
    }

    void check(List<? extends Date> expected, DateGroup actualCollection) {
        List<? extends Date> actual = actualCollection.getDates();
        check(expected, actual);
    }

    void check(List<? extends Date> expected, List<? extends Date> actual) {
        int n = expected.size();
        for(int i = 0; i < n; i++) {
            assertEquals(expected.get(i), actual.get(i));
        }
        assertEquals(expected.size(), actual.size());
    }
}
