package org.isda.cdm.processor;

import java.util.List;

import com.regnosys.rosetta.common.translation.Mapping;
import com.rosetta.model.lib.RosettaModelObject;
import com.rosetta.model.lib.RosettaModelObjectBuilder;
import com.rosetta.model.lib.path.RosettaPath;
import com.rosetta.model.lib.process.Processor;

/**
 * Processor implementation that calls map function is the current path matches the expected path.
 */
public abstract class MappingProcessor implements Processor {

    private final RosettaPath path;
    private final List<Mapping> mappings;

    MappingProcessor(RosettaPath path, List<Mapping> mappings) {
        this.path = path;
        this.mappings = mappings;
    }

    @Override
    public <R extends RosettaModelObject> void processRosetta(RosettaPath currentPath, Class<? extends R> rosettaType, RosettaModelObjectBuilder<R> builder) {
        if (currentPath.endsWith(path)) {
            map(builder);
        }
    }

    @Override
    public <T> void processBasic(RosettaPath path, Class<T> rosettaType, T instance) {
        // Do nothing
    }

    @Override
    public Report report() {
        return null;
    }

    /**
     * Perform custom mapping logic and updates resultant mapped value on builder object.
     */
    protected abstract <R extends RosettaModelObject> void map(RosettaModelObjectBuilder<R> builder);

    RosettaPath getPath() {
        return path;
    }

    List<Mapping> getMappings() {
        return mappings;
    }
}
