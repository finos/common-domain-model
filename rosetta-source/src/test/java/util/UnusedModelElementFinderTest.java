package util;
import com.google.common.collect.ImmutableList;
import com.regnosys.rosetta.common.util.ClassPathUtils;
import com.regnosys.rosetta.transgest.ModelLoaderImpl;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UnusedModelElementFinderTest {

    @Test
    public void getlistOfOrphanedTypes() throws IOException {

        ModelLoaderImpl modelLoader = new ModelLoaderImpl(ClassPathUtils.findPathsFromClassPath(ImmutableList.of("rosetta"), ".*\\.rosetta", Optional.empty(), ClassPathUtils.class.getClassLoader()
                ).stream()
                .map(ClassPathUtils::toUrl)
                .toArray(URL[]::new));
        UnusedModelElementFinder unusedModelElementFinder = new UnusedModelElementFinder(modelLoader);

        unusedModelElementFinder.run();

        assertEquals(0, unusedModelElementFinder.getListOfOrphanedTypes().size(), unusedModelElementFinder.getListOfOrphanedTypes().toString());

    }
}
