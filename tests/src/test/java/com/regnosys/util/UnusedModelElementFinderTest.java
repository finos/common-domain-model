package com.regnosys.util;

import com.google.common.collect.ImmutableList;
import com.regnosys.rosetta.common.util.ClassPathUtils;
import com.regnosys.rosetta.common.util.UrlUtils;
import com.regnosys.rosetta.rosetta.RosettaModel;
import com.regnosys.rosetta.transgest.ModelLoader;
import com.regnosys.testing.RosettaTestingInjectorProvider;
import org.eclipse.xtext.testing.InjectWith;
import org.eclipse.xtext.testing.extensions.InjectionExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import util.UnusedModelElementFinder;

import jakarta.inject.Inject;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(InjectionExtension.class)
@InjectWith(RosettaTestingInjectorProvider.class)
public class UnusedModelElementFinderTest {
    @Inject
    private ModelLoader modelLoader;

    @Test
    public void getlistOfOrphanedTypes() {
        List<RosettaModel> models =
                modelLoader.loadRosettaModels(
                        ClassPathUtils.findPathsFromClassPath(ImmutableList.of("model", "rosetta"), ".*\\.rosetta", Optional.empty(), ClassPathUtils.class.getClassLoader())
                                .stream()
                                .map(UrlUtils::toUrl));
        UnusedModelElementFinder unusedModelElementFinder = new UnusedModelElementFinder(models);

        unusedModelElementFinder.run();
        assertEquals(7, unusedModelElementFinder.getListOfTypes().size(), unusedModelElementFinder.getListOfTypes().toString());

        assertTrue(unusedModelElementFinder.getListOfTypes().contains("cdm.test.Test1"), "ListOfTypes should contain cdm.test.Test1");
        assertTrue(unusedModelElementFinder.getListOfTypes().contains("cdm.test.Test2"), "ListOfTypes should contain cdm.test.Test2");
        assertTrue(unusedModelElementFinder.getListOfTypes().contains("cdm.test.Test3"), "ListOfTypes should contain cdm.test.Test3");
        assertTrue(unusedModelElementFinder.getListOfTypes().contains("cdm.test.Test4Unused"), "ListOfTypes should contain cdm.test.Test4Unused");
        assertTrue(unusedModelElementFinder.getListOfTypes().contains("cdm.test.DirectionEnum"), "ListOfTypes should contain cdm.test.DirectionEnum");
        assertTrue(unusedModelElementFinder.getListOfTypes().contains("cdm.test.TestEnum2Unused"), "ListOfTypes should contain cdm.test.TestEnum2Unused");
        assertTrue(unusedModelElementFinder.getListOfTypes().contains("cdm.test.TestEnum3UsedInFuncOnly"), "ListOfTypes should contain cdm.test.TestEnum3UsedInFuncOnly");

        assertEquals(5, unusedModelElementFinder.getListOfUsedTypes().size(), unusedModelElementFinder.getListOfUsedTypes().toString());

        assertTrue(unusedModelElementFinder.getListOfUsedTypes().contains("cdm.test.Test1"), "ListOfUsedTypes should contain cdm.test.Test1");
        assertTrue(unusedModelElementFinder.getListOfUsedTypes().contains("cdm.test.Test2"), "ListOfUsedTypes should contain cdm.test.Test2");
        assertTrue(unusedModelElementFinder.getListOfUsedTypes().contains("cdm.test.Test3"), "ListOfUsedTypes should contain cdm.test.Test3");
        assertTrue(unusedModelElementFinder.getListOfUsedTypes().contains("cdm.test.DirectionEnum"), "ListOfUsedTypes should contain cdm.test.DirectionEnum");
        assertTrue(unusedModelElementFinder.getListOfUsedTypes().contains("cdm.test.TestEnum3UsedInFuncOnly"), "ListOfUsedTypes should contain cdm.test.TestEnum3UsedInFuncOnly");


        assertEquals(2, unusedModelElementFinder.getListOfOrphanedTypes().size(), unusedModelElementFinder.getListOfOrphanedTypes().toString());
        assertTrue(unusedModelElementFinder.getListOfOrphanedTypes().contains("cdm.test.TestEnum2Unused"), "ListOfOrphanedTypes should contain cdm.test.TestEnum2Unused");
        assertTrue(unusedModelElementFinder.getListOfOrphanedTypes().contains("cdm.test.Test4Unused"), "ListOfOrphanedTypes should contain cdm.test.Test4Unused");

        assertEquals(1, unusedModelElementFinder.getListOfDeprecatedTypes().size(), unusedModelElementFinder.getListOfDeprecatedTypes().toString());
        assertTrue(unusedModelElementFinder.getListOfDeprecatedTypes().contains("cdm.test.Test3"), "ListOfDeprecatedTypes should contain cdm.test.Test3");

    }
}
