package com.daranix.webservice.server.test;

import org.springframework.ws.server.endpoint.annotation.RequestPayload;

import com.daranix.webservice.server.utils.soap.SOAPEndpoint;
import com.daranix.webservice.server.utils.soap.SOAPService;

@SOAPEndpoint(publish = "/testService")
public class TestServiceImpl implements ITestService {

    public TestResponse<String> makeTest(TestRequestString request) {
        TestResponse<String> response = new TestResponse<String>("Hey you requested:" + request.getData());
        return response;
    }
}