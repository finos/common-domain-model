package org.isda.cdm.workflows;

import java.util.List;
import java.util.Optional;

import com.google.common.collect.Lists;

public class WorkflowUtils {

	static <T> List<T> guard(List<T> list) {
		return Optional.ofNullable(list).orElse(Lists.newArrayList());		
	}
}
