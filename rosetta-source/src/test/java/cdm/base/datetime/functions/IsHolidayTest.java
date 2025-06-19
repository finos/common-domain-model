package cdm.base.datetime.functions;

import cdm.base.datetime.BusinessCenterEnum;
import cdm.base.datetime.BusinessCenters;
import javax.inject.Inject;
import com.rosetta.model.lib.records.Date;
import org.isda.cdm.functions.AbstractFunctionTest;
import org.junit.jupiter.api.Test;

import java.util.List;

import static cdm.base.datetime.functions.BusinessCenterHolidaysTestData.LONDON_TARGET_BC;
import static cdm.base.datetime.functions.BusinessCenterHolidaysTestData.TARGET_BC;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class IsHolidayTest extends AbstractFunctionTest {

    @Inject
    private GetAllBusinessCenters businessCentersFunc;

    @Inject
    private IsHoliday func;

    @Test
    void shouldGetResult() {
        List<BusinessCenterEnum> londonTargetBc = getBusinessCenters(LONDON_TARGET_BC);
        assertEquals(Boolean.FALSE, func.evaluate(Date.of(2021,12, 20), londonTargetBc));
        assertEquals(Boolean.FALSE, func.evaluate(Date.of(2021,12, 19), londonTargetBc));
        assertEquals(Boolean.TRUE, func.evaluate(Date.of(2021,12, 27), londonTargetBc));
        assertEquals(Boolean.TRUE, func.evaluate(Date.of(2021,12, 28), londonTargetBc));

        List<BusinessCenterEnum> targetBc = getBusinessCenters(TARGET_BC);
        assertEquals(Boolean.FALSE, func.evaluate(Date.of(2021,12, 27), targetBc));
    }

    private List<BusinessCenterEnum> getBusinessCenters(BusinessCenters businessCenters) {
        return businessCentersFunc.evaluate(businessCenters);
    }
}
