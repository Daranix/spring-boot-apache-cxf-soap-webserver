package com.daranix.webservice.server.config;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.UnsupportedCallbackException;
import javax.xml.soap.SOAPConstants;
import javax.xml.soap.SOAPEnvelope;

import org.apache.cxf.Bus;
import org.apache.cxf.jaxws.EndpointImpl;
import org.apache.cxf.ws.security.wss4j.DefaultCryptoCoverageChecker;
import org.apache.cxf.ws.security.wss4j.WSS4JInInterceptor;
import org.apache.cxf.ws.security.wss4j.WSS4JOutInterceptor;
import org.apache.wss4j.common.ConfigurationConstants;
import org.apache.wss4j.common.ext.WSPasswordCallback;
import org.opensaml.xmlsec.signature.support.SignatureConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.ws.config.annotation.EnableWs;
import org.springframework.ws.config.annotation.WsConfigurerAdapter;
import org.springframework.ws.soap.SoapEnvelope;

import com.daranix.webservice.server.utils.soap.SOAPBindingType;
import com.daranix.webservice.server.utils.soap.SOAPEndpoint;
import com.daranix.webservice.server.utils.soap.SOAPService;

@EnableWs
@Configuration
public class WebServiceConfiguration extends WsConfigurerAdapter {

	// https://stackoverflow.com/questions/63242860/spring-boot-apache-cxf-publish-endpoints-with-annotations-only
	// By default apache cxf uses /services endpoint for wsdls

	/* Datos keystore */
	@Value("${keystore.alias}")
	private String keystoreAlias;
	@Value("${keystore.password}")
	private String keystorePassword;
	@Value("${keystore.file}")
	private String keystoreFile;
	@Value("${keystore.type}")
	private String keystoreType;
	@Value("${keystore.pkpass}")
	private String keystorePkPass;

	@Autowired
	private Bus bus;

	@Autowired
	private List<SOAPService> endpoints;

	@PostConstruct
	public void generateEndpoints() {
		for (SOAPService bean : endpoints) {

			if (bean.getClass().getAnnotation(SOAPEndpoint.class) == null) {
				throw new IllegalArgumentException("Missed @SoapEndpoint for " + bean.getClass().getName());
			}

			EndpointImpl endpoint = new EndpointImpl(bus, bean);
			SOAPEndpoint endpointInfo = bean.getClass().getAnnotation(SOAPEndpoint.class);
			endpoint.setBindingUri(endpointInfo.bindingUri().getValue());
			endpoint.publish(endpointInfo.publish());
			endpoint.getOutInterceptors().add(new WSS4JOutInterceptor(interceptorProperties(endpointInfo.bindingUri())));
			endpoint.getInInterceptors().add(new WSS4JInInterceptor(interceptorProperties(endpointInfo.bindingUri())));
			endpoint.getInInterceptors().add(new DefaultCryptoCoverageChecker());
		}
	}
	
	public Map<String, Object> interceptorProperties(SOAPBindingType soapBindingType) {
		Map<String, Object> properties = new HashMap<>();
		//properties.put(ConfigurationConstants.ACTION, ConfigurationConstants.SIGNATURE + " " + ConfigurationConstants.TIMESTAMP);
		properties.put(ConfigurationConstants.ACTION, ConfigurationConstants.SIGNATURE);
		properties.put("signingProperties", signingProperties());
		properties.put(ConfigurationConstants.SIG_PROP_REF_ID, "signingProperties");
		properties.put(ConfigurationConstants.SIG_KEY_ID, "DirectReference");
		properties.put(ConfigurationConstants.USER, keystoreAlias);
		String envelope = soapBindingType == SOAPBindingType.SOAP11 ? SOAPConstants.URI_NS_SOAP_ENVELOPE : SOAPConstants.URI_NS_SOAP_1_2_ENVELOPE;
		properties.put(ConfigurationConstants.SIGNATURE_PARTS, "{Element}{"+ envelope +"}Body");
		properties.put(ConfigurationConstants.PW_CALLBACK_REF, new CallbackHandler() {
			@Override
			public void handle(Callback[] callbacks) throws IOException, UnsupportedCallbackException {
				WSPasswordCallback pc = (WSPasswordCallback) callbacks[0];
				pc.setPassword(keystorePassword);
			}
		});
		properties.put(ConfigurationConstants.SIG_ALGO, SignatureConstants.ALGO_ID_SIGNATURE_RSA_SHA256);
		return properties;
	}

	public Properties signingProperties() {
		Properties properties = new Properties();
		properties.put("org.apache.wss4j.crypto.merlin.provider", "org.apache.wss4j.common.crypto.Merlin");
		properties.put("org.apache.wss4j.crypto.merlin.keystore.type", keystoreType);
		properties.put("org.apache.wss4j.crypto.merlin.keystore.password", keystorePassword);
		properties.put("org.apache.wss4j.crypto.merlin.keystore.alias", keystoreAlias);
		properties.put("org.apache.wss4j.crypto.merlin.keystore.private.password", keystorePkPass);
		properties.put("org.apache.wss4j.crypto.merlin.keystore.file", new FileSystemResource(keystoreFile).getPath());
		return properties;
	}

}