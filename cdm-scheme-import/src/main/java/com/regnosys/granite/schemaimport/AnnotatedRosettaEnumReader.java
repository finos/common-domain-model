package com.regnosys.granite.schemaimport;

import com.regnosys.rosetta.rosetta.RosettaDocReference;
import com.regnosys.rosetta.rosetta.RosettaEnumeration;
import com.regnosys.rosetta.rosetta.RosettaSegmentRef;
import com.regnosys.rosetta.transgest.ModelLoader;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public class AnnotatedRosettaEnumReader {
	private final ModelLoader modelLoader;

	 public AnnotatedRosettaEnumReader(ModelLoader modelLoader) {
		 this.modelLoader = modelLoader;
	 }

	public List<RosettaEnumeration> getAnnotatedEnum(String body, String corpus) {
		List<RosettaEnumeration> allRosettaEnums = modelLoader.rosettaElements(RosettaEnumeration.class);

		return allRosettaEnums.stream()
			.filter(rosettaEnum -> enumIsAnnotatedWithBodyAndCorpusDocRef(rosettaEnum, body, corpus))
			.collect(Collectors.toList());
	}

	private boolean enumIsAnnotatedWithBodyAndCorpusDocRef(RosettaEnumeration rosettaEnumeration, String body, String corpus) {
		return rosettaEnumeration
			.getReferences()
				.stream().anyMatch(documentReference ->
				refIsAnnotatedWithBodyAndReference(documentReference, body, corpus));

	}

	private boolean refIsAnnotatedWithBodyAndReference(RosettaDocReference documentReference, String body, String corpus) {
		return documentReference.getBody() != null
			&& body.equals(documentReference.getBody().getName())
			&& documentReference.getCorpuses().stream()
			.filter(Objects::nonNull)
			.anyMatch(x -> corpus.equals(x.getName()));
	}

	public Optional<String> getSchemaLocationForEnum(RosettaEnumeration rosettaEnumeration, String body, String corpus) {
	 	return rosettaEnumeration
			.getReferences()
			.stream()
			.flatMap(ref -> ref.getSegments().stream())
			.filter(s -> s.getSegment().getName().equals("schemeLocation"))
			.map(RosettaSegmentRef::getSegmentRef)
			.findAny();
	}
}
