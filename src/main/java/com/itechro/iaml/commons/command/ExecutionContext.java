package com.itechro.iaml.commons.command;

import java.util.HashMap;
import java.util.Map;

/**
 * Execution context for the command patten
 *
 * @author : chamara
 */
public abstract class ExecutionContext {

	protected Map<String, Object> contextParam = new HashMap<>();

	protected void putParam(String paramKey, Object paramVal) {
		contextParam.put(paramKey, paramVal);
	}

	protected <T> T getParam(String paramKey) {
		T param = (T) contextParam.get(paramKey);
		return param;
	}
}
