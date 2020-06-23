package org.isda.cdm.processor;

import com.regnosys.rosetta.common.translation.Mapping;
import com.regnosys.rosetta.common.translation.Path;
import com.rosetta.model.lib.RosettaModelObject;
import com.rosetta.model.lib.RosettaModelObjectBuilder;
import com.rosetta.model.lib.meta.ReferenceWithMeta;
import com.rosetta.model.lib.path.RosettaPath;
import com.rosetta.model.lib.process.AttributeMeta;
import com.rosetta.model.lib.process.BuilderProcessor;

import java.util.List;
import java.util.function.Consumer;

/**
 * Processor implementation that calls map function is the current path matches the expected path.
 */
public abstract class MappingProcessor implements BuilderProcessor {

	private final RosettaPath path;
	private final List<Path> synonymPaths;
	private final List<Mapping> mappings;

	MappingProcessor(RosettaPath path, List<Path> synonymPaths, List<Mapping> mappings) {
		this.path = path;
		this.synonymPaths = synonymPaths;
		this.mappings = mappings;
	}

	@Override
	public <R extends RosettaModelObject> boolean processRosetta(RosettaPath currentPath,
			Class<? extends R> rosettaType,
			RosettaModelObjectBuilder builder,
			RosettaModelObjectBuilder parent,
			AttributeMeta... meta) {
		if (builder != null && currentPath.matchesIgnoringIndex(path)) {
			synonymPaths.forEach(p -> map(p, builder, parent));
		}
		return true;
	}

	@Override
	public <R extends RosettaModelObject> boolean processRosetta(RosettaPath currentPath,
			Class<? extends R> rosettaType,
			List<? extends RosettaModelObjectBuilder> builder,
			RosettaModelObjectBuilder parent,
			AttributeMeta... meta) {
		if (builder != null && matchesProcessorPathForMultipleCardinality(currentPath, rosettaType)) {
			synonymPaths.forEach(p -> map(p, builder, parent));
		}
		return true;
	}

	@Override
	public <T> void processBasic(RosettaPath currentPath, Class<T> rosettaType, T instance, RosettaModelObjectBuilder parent, AttributeMeta... meta) {
		if (instance != null && currentPath.matchesIgnoringIndex(path)) {
			synonymPaths.forEach(p -> mapBasic(p, instance, parent));
		}
	}

	@Override
	public <T> void processBasic(RosettaPath currentPath, Class<T> rosettaType, List<T> instance, RosettaModelObjectBuilder parent, AttributeMeta... meta) {
		if (instance != null && currentPath.matchesIgnoringIndex(path)) {
			synonymPaths.forEach(p -> mapBasic(p, instance, parent));
		}
	}

	@Override
	public Report report() {
		return null;
	}

	/**
	 * Perform custom mapping logic and updates resultant mapped value on builder object.
	 */
	protected void map(Path synonymPath, RosettaModelObjectBuilder builder, RosettaModelObjectBuilder parent) {
		// Default behaviour - do nothing
	}

	/**
	 * Perform custom mapping logic and updates resultant mapped value on builder object.
	 */
	protected void map(Path synonymPath, List<? extends RosettaModelObjectBuilder> builder, RosettaModelObjectBuilder parent) {
		// Default behaviour - do nothing
	}

	/**
	 * Perform custom mapping logic and updates resultant mapped value on builder object.
	 */
	protected <T> void mapBasic(Path synonymPath, T instance, RosettaModelObjectBuilder parent) {
		// Default behaviour - do nothing
	}

	/**
	 * Perform custom mapping logic and updates resultant mapped value on builder object.
	 */
	protected <T> void mapBasic(Path synonymPath, List<T> instance, RosettaModelObjectBuilder parent) {
		// Default behaviour - do nothing
	}

	RosettaPath getPath() {
		return path;
	}

	List<Path> getSynonymPaths() {
		return synonymPaths;
	}

	List<Mapping> getMappings() {
		return mappings;
	}

	void setValueAndUpdateMappings(String synonymPath, Consumer<String> setter) {
		setValueAndUpdateMappings(Path.parse(synonymPath), setter);
	}

	void setValueAndUpdateMappings(Path synonymPath, Consumer<String> setter) {
		MappingProcessorUtils.setValueAndUpdateMappings(synonymPath, setter, mappings, path);
	}

	private boolean matchesProcessorPathForMultipleCardinality(RosettaPath currentPath, Class<?> rosettaType) {
		return ReferenceWithMeta.class.isAssignableFrom(rosettaType) ?
				// so the parse handlers match on the list rather than each list item
				currentPath.matchesIgnoringIndex(path.getParent()) :
				currentPath.matchesIgnoringIndex(path);
	}
}