package cdm.base.datetime.functions;

import cdm.base.datetime.BusinessCenters;
import cdm.base.datetime.metafields.FieldWithMetaBusinessCenterEnum;
import cdm.base.datetime.metafields.ReferenceWithMetaBusinessCenters;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CombineBusinessCentersImpl extends CombineBusinessCenters {

    @Override
    protected BusinessCenters.BusinessCentersBuilder doEvaluate(BusinessCenters list1, BusinessCenters list2) {
        BusinessCenters.BusinessCentersBuilder result = BusinessCenters.builder();

        if (list1 == null && list2 == null)  return result;
        List<? extends FieldWithMetaBusinessCenterEnum> bc1 = getBC(list1);
        List<? extends FieldWithMetaBusinessCenterEnum> bc2 = getBC(list2);

        Set<FieldWithMetaBusinessCenterEnum> bcs12 = new HashSet<>();   // avoid duplicates by using a set
        if (bc1!= null) bcs12.addAll(bc1);
        if (bc2!=null) bcs12.addAll(bc2);
        List<FieldWithMetaBusinessCenterEnum> bc12 = new ArrayList<>(bcs12);
        result.addBusinessCenter(bc12);

        return result;
    }

    private List<? extends FieldWithMetaBusinessCenterEnum> getBC(BusinessCenters bc) {
        if (bc==null) return new ArrayList<>();
        ReferenceWithMetaBusinessCenters ref = bc.getBusinessCentersReference();
        if (ref != null) {
            BusinessCenters refBC = ref.getValue();
            return getBC(refBC);
        }
        return bc.getBusinessCenter();
    }
}
