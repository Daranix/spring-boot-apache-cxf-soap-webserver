package com.daranix.webservice.server.utils.soap;

import javax.xml.ws.soap.SOAPBinding;

public enum SOAPBindingType {

	SOAP11(SOAPBinding.SOAP11HTTP_BINDING),
	SOAP12(SOAPBinding.SOAP12HTTP_BINDING);
	
	private final String value;
	
	
	SOAPBindingType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
	
}
