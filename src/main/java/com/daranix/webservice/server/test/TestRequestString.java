package com.daranix.webservice.server.test;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@XmlAccessorType(XmlAccessType.FIELD)
@Data
@NoArgsConstructor
public class TestRequestString {

	@NonNull
	private String data;
	
}
