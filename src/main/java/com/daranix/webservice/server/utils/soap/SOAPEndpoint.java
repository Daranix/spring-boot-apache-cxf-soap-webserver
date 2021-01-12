package com.daranix.webservice.server.utils.soap;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.xml.ws.soap.SOAPBinding;

import org.springframework.ws.server.endpoint.annotation.Endpoint;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Endpoint
public @interface SOAPEndpoint {
	 SOAPBindingType bindingUri() default SOAPBindingType.SOAP12;
	 String publish();
}
