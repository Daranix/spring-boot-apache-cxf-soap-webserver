package com.daranix.webservice.server.test;

import java.io.Serializable;


public class TestResponse<T> implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3589905050959187000L;
	
	 
	private T data;

	public TestResponse(T data) {
		super();
		this.data = data;
	}


	public T getData() {
		return data;
	}


	public void setData(T data) {
		this.data = data;
	}
	
	
	
	
	
}
