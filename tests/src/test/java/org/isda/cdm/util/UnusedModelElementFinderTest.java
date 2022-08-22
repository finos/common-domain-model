package org.isda.cdm.util;

import com.google.common.collect.ImmutableList;
import com.regnosys.rosetta.common.util.ClassPathUtils;
import com.regnosys.rosetta.transgest.ModelLoaderImpl;
import org.junit.jupiter.api.Test;
import util.UnusedModelElementFinder;

import java.net.URL;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UnusedModelElementFinderTest {

    @Test
    public void getlistOfOrphanedTypes() {
//new ModelLoaderImpl(ClassPathUtils.findRosettaFilePaths().stream().map(ClassPathUtils::toUrl).toArray(URL[]::new))
        ModelLoaderImpl modelLoader = new ModelLoaderImpl(ClassPathUtils.findPathsFromClassPath(ImmutableList.of("model", "rosetta"), ".*\\.rosetta", Optional.empty(), ClassPathUtils.class.getClassLoader()
                ).stream()
                .map(ClassPathUtils::toUrl)
                .toArray(URL[]::new));
        UnusedModelElementFinder unusedModelElementFinder = new UnusedModelElementFinder(modelLoader);

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
