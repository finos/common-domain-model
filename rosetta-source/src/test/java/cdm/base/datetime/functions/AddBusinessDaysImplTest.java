package cdm.base.datetime.functions;

//import cdm.base.datetime.BusinessCenterEnum;
import cdm.base.datetime.BusinessCenters;
import javax.inject.Inject;
import com.rosetta.model.lib.records.Date;
import org.isda.cdm.functions.AbstractFunctionTest;
import org.junit.jupiter.api.Test;

import java.util.List;

import static cdm.base.datetime.functions.BusinessCenterHolidaysTestData.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class AddBusinessDaysImplTest extends AbstractFunctionTest {

    @Inject
    private GetAllBusinessCenters businessCentersFunc;

    @Inject
    private AddBusinessDays addBusinessDaysFunc;

    @Test
    void shouldAddDays1() {
        Date first = Date.of(2021, 12, 27);
        List<String> targetBc = getBusinessCenters(TARGET_BC);
        Date actual = addBusinessDaysFunc.evaluate(first, -1, targetBc);
        assertEquals(Date.of(2021, 12, 24), actual);
    }

    @Test
    void shouldAddDays2() {
        Date first = Date.of(2021, 12, 20);
        List<String> targetBc = getBusinessCenters(TARGET_BC);
        Date actual = addBusinessDaysFunc.evaluate(first, 5, targetBc);
        assertEquals(Date.of(2021, 12, 27), actual);
    }

    @Test
    void shouldAddDays3() {
        Date first = Date.of(2021, 12, 20);
        List<String> londonTargetBc = getBusinessCenters(LONDON_TARGET_BC);
        Date actual = addBusinessDaysFunc.evaluate(first, 5, londonTargetBc);
        assertEquals(Date.of(2021, 12, 29), actual);
    }

    @Test
    void shouldAddDays4() {
        Date first = Date.of(2021, 12, 20);
        List<String> londonTargetUsBc = getBusinessCenters(LONDON_TARGET_US_BC);
        Date actual = addBusinessDaysFunc.evaluate(first, 5, londonTargetUsBc);
        assertEquals(Date.of(2021, 12, 30), actual);
    }

    @Test
    void shouldAddDays5() {
        Date first = Date.of(2021, 12, 20);
        List<String> londonTargetBc = getBusinessCenters(LONDON_TARGET_BC);
        Date actual = addBusinessDaysFunc.evaluate(first, -5, londonTargetBc);
        assertEquals(Date.of(2021, 12, 13), actual);
    }

    private List<String> getBusinessCenters(BusinessCenters businessCenters) {
        return businessCentersFunc.evaluate(businessCenters);
    }
}
