package com.json.comparator.model;

/**
 *  Custom Exception class for handling parsing errors.
 */
public class JsonCompareException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public JsonCompareException(String message) {
		super(message);
	}
}
