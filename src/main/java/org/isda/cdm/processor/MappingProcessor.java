package org.isda.cdm.processor;

import java.util.List;

import com.regnosys.rosetta.common.translation.Mapping;
import com.rosetta.model.lib.RosettaModelObject;
import com.rosetta.model.lib.RosettaModelObjectBuilder;
import com.rosetta.model.lib.path.RosettaPath;
import com.rosetta.model.lib.process.AttributeMeta;
import com.rosetta.model.lib.process.BuilderProcessor;

/**
 * Processor implementation that calls map function is the current path matches the expected path.
 */
public abstract class MappingProcessor implements BuilderProcessor {

	private final RosettaPath path;
	private final List<Mapping> mappings;

	MappingProcessor(RosettaPath path, List<Mapping> mappings) {
		this.path = path;
		this.mappings = mappings;
	}

	@Override
	public <R extends RosettaModelObject> void processRosetta(RosettaPath currentPath, Class<? extends R> rosettaType
			, RosettaModelObjectBuilder builder, RosettaModelObjectBuilder parent, AttributeMeta... meta) {
		if (builder!=null && currentPath.matchesIgnoringIndex(path)) {
			map(builder, parent);
		}
	}

	@Override
	public <R extends RosettaModelObject> void processRosetta(RosettaPath currentPath, Class<? extends R> rosettaType,
			List<? extends RosettaModelObjectBuilder> builder, RosettaModelObjectBuilder parent, AttributeMeta... meta) {
		if (builder!=null && currentPath.matchesIgnoringIndex(path)) {
			map(builder, parent);
		}
	}

	

	@Override
	public <T> void processBasic(RosettaPath path, Class<T> rosettaType, T instance, RosettaModelObjectBuilder parent, AttributeMeta... meta) {
		// Do nothing
	}

	@Override
	public <T> void processBasic(RosettaPath path, Class<T> rosettaType, List<T> instance,
			RosettaModelObjectBuilder parent, AttributeMeta... meta) {
	}

	@Override
	public Report report() {
		return null;
	}

	/**
	 * Perform custom mapping logic and updates resultant mapped value on builder object.
	 */
	protected abstract <R extends RosettaModelObject> void map(RosettaModelObjectBuilder builder, RosettaModelObjectBuilder parent);
	protected abstract void map(List<? extends RosettaModelObjectBuilder> builder,
			RosettaModelObjectBuilder parent);
	
	RosettaPath getPath() {
		return path;
	}

	List<Mapping> getMappings() {
		return mappings;
	}
}