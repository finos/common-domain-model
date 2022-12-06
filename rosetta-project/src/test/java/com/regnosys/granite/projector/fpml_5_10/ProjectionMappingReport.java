package com.regnosys.granite.projector.fpml_5_10;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import com.regnosys.ingest.test.framework.ingestor.synonym.MappingResult;
import com.rosetta.model.lib.path.RosettaPath;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.StringReader;
import java.util.*;

import static com.google.common.collect.Multimaps.filterKeys;

public class ProjectionMappingReport {
	private final List<MappingResult> mappingResults;

	public ProjectionMappingReport(List<MappingResult> mappingResults) {
		this.mappingResults = mappingResults;
	}

	public List<MappingResult> getMappingResults() {
		return mappingResults;
	}

	static class ProjectionMappingReportBuilder {

		private final Set<RosettaPath> expectedUnmappedPaths;
		private final Set<RosettaPath> ignoredPaths;
		private final Multimap<RosettaPath, String> originalPaths;
		private final Multimap<RosettaPath, String> projectedPaths;

		ProjectionMappingReportBuilder(Set<RosettaPath> expectedUnmappedPaths, Set<RosettaPath> ignoredPaths, String originalXml, String projectedXml) {
			this.expectedUnmappedPaths = expectedUnmappedPaths;
			this.ignoredPaths = ignoredPaths;
			this.originalPaths = buildPaths(originalXml);
			this.projectedPaths = buildPaths(projectedXml);
		}

		ProjectionMappingReport buildMappings() {
			// paths in original but not in the projected
			Sets.SetView<RosettaPath> difference = Sets.difference(originalPaths.keySet(), projectedPaths.keySet());

			// paths + values in original but not in the projected
			Multimap<RosettaPath, String> pathsNotMapped = filterKeys(originalPaths, difference::contains);

			// paths + values in original but not in the projected with expectedUnmappedPaths filtered out
			Multimap<RosettaPath, String> unmapped = filterKeys(pathsNotMapped, x ->
				expectedUnmappedPaths.stream().noneMatch(x::endsWith) && ignoredPaths.stream().noneMatch(x.trimFirst()::equals));

			Multimap<RosettaPath, String> mapped = filterKeys(projectedPaths, x -> !difference.contains(x));

			List<MappingResult> mappingResults = new ArrayList<>();
			mapped.forEach((path, value) -> mappingResults.add(new MappingResult(true, path, value, Map.of(path, value), List.of(),
				false, false, false, MappingResult.Result.Success)));

			unmapped.forEach((path, value) -> mappingResults.add(new MappingResult(false, path, value, Map.of(), List.of("Field not projected"),
				false, false, false, MappingResult.Result.Fail_MappedNone)));

			return new ProjectionMappingReport(mappingResults);
		}

		private static Multimap<RosettaPath, String> buildPaths(String xml) {
			Multimap<RosettaPath, String> pathvalue = ArrayListMultimap.create();

			try {
				XMLInputFactory factory = XMLInputFactory.newInstance();
				XMLStreamReader reader = factory.createXMLStreamReader(new StringReader(xml));

				RosettaPath path = null;
				while (reader.hasNext()) {
					reader.next();

					switch (reader.getEventType()) {
					case XMLStreamConstants.START_ELEMENT:
						String qName = reader.getName().getLocalPart();
						path = Optional.ofNullable(path)
							.map(p -> p.newSubPath(qName))
							.orElseGet(() -> RosettaPath.valueOf(qName));
						// Attributes
						for (int i = 0; i < reader.getAttributeCount(); i++) {
							if (reader.getAttributeName(i).getNamespaceURI().equals("http://www.w3.org/2001/XMLSchema-instance")) {
								continue; // ignore all schema related attributes
							}
							String attributeQName = reader.getAttributeName(i).getLocalPart().trim();
							String attributeValue = reader.getAttributeValue(i).trim();
							if (attributeQName.equals("fpmlVersion")) {
								continue; // ignore FpML version
							}
							pathvalue.put(path.newSubPath(attributeQName), attributeValue);
						}
						break;
					case XMLStreamConstants.CHARACTERS:
						String value = reader.getText().trim();
						if (!value.isEmpty()) {
							pathvalue.put(path, value);
						}
						break;
					case XMLStreamConstants.END_ELEMENT:
						path = path.getParent();
						break;
					}
				}
			} catch (XMLStreamException e) {
				throw new RuntimeException(e);
			}
			return pathvalue;
		}
	}
}
