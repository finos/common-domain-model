package cdm.base.datetime.functions;

import cdm.base.datetime.BusinessCenters;
import cdm.base.datetime.metafields.FieldWithMetaBusinessCenterEnum;
import cdm.base.datetime.metafields.ReferenceWithMetaBusinessCenters;
import cdm.event.common.functions.FilterCashTransfers;

import java.util.*;

public class CombineBusinessCentersImpl extends CombineBusinessCenters{
    @Override
    protected BusinessCenters.BusinessCentersBuilder doEvaluate(BusinessCenters list1, BusinessCenters list2) {
        List<? extends FieldWithMetaBusinessCenterEnum> bc1 = getBC(list1);
        List<? extends FieldWithMetaBusinessCenterEnum> bc2 = getBC(list2);
        Set<FieldWithMetaBusinessCenterEnum> bcs12 = new HashSet<>();   // avoid duplicates by using a set
        bcs12.addAll(bc1);
        bcs12.addAll(bc2);
        List<FieldWithMetaBusinessCenterEnum> bc12 = new ArrayList<>();
        bc12.addAll(bcs12);
        BusinessCenters.BusinessCentersBuilder result = new BusinessCenters.BusinessCentersBuilderImpl();
        result.addBusinessCenter(bc12);

        return result;
    }

    private List<? extends FieldWithMetaBusinessCenterEnum> getBC(BusinessCenters bc) {
        ReferenceWithMetaBusinessCenters ref = bc.getBusinessCentersReference();
        if (ref != null) {
            BusinessCenters refBC = ref.getValue();
            return getBC(refBC);
        }
        return bc.getBusinessCenter();
    }
}
