package com.daranix.webservice.server.test;

import javax.jws.WebService;

import com.daranix.webservice.server.utils.soap.SOAPService;


@WebService
public interface ITestService extends SOAPService {
    public TestResponse<String> makeTest(TestRequestString request);
}
