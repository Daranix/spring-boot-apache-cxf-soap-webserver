package com.daranix.webservice.server.test;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;

@XmlAccessorType(XmlAccessType.FIELD)
@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
public class TestRequest<T> implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7085839785839543091L;
	
	
	@NonNull
	@XmlElement(required = true)
	private T data;
	

}
