package org.isda.cdm.documentation;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public final class PatternStreamer {
	private final Pattern pattern;
	public PatternStreamer(String regex) {
		this.pattern = Pattern.compile(regex, Pattern.MULTILINE | Pattern.DOTALL);
	}
	public Stream<MatchResult> results(CharSequence input) {
		List<MatchResult> list = new ArrayList<>();
		for (Matcher m = this.pattern.matcher(input); m.find(); )
			list.add(m.toMatchResult());
		return list.stream();
	}
}
