package com.gtsarik.browserDetect

import grails.test.*
import org.springframework.web.context.request.RequestContextHolder as RCH
import org.springframework.web.context.request.RequestAttributes
import org.springframework.mock.web.MockHttpServletRequest
import org.springframework.mock.web.MockHttpSession
import javax.servlet.http.HttpServletRequest
import groovy.mock.interceptor.MockFor
import groovy.mock.interceptor.StubFor

class UserAgentIdentServiceTests extends GrailsUnitTestCase {

	def userAgentIdentService

    protected void setUp() {
        super.setUp()

	    mockLogging(UserAgentIdentService)
	    userAgentIdentService = new UserAgentIdentService()

	    MockHttpServletRequest request = new MockHttpServletRequest()
	    MockHttpSession session = new MockHttpSession()

	    def mockCtx = new StubFor(RequestAttributes)
	    mockCtx.demand.getCurrentRequest(9999) { request }
	    mockCtx.demand.getSession(9999) { session }

	    RCH.setRequestAttributes(mockCtx.proxyInstance())
    }


    protected void tearDown() {
        super.tearDown()
    }

    void testFirefox3_6_9() {
	    RCH.currentRequestAttributes().currentRequest.addHeader("user-agent",
	        "Mozilla/5.0 (Windows; U; Windows NT 6.0; en-GB; rv:1.9.2.9) Gecko/20100824 Firefox/3.6.9 ( .NET CLR 3.5.30729; .NET CLR 4.0.20506)")

	    assert userAgentIdentService.isFirefox()
	    assert !userAgentIdentService.isChrome()
	    assert !userAgentIdentService.isIOsDevice()
	    assert !userAgentIdentService.isMobile()
	    assert userAgentIdentService.getBrowserVersion() == "3.6.9"
    }

	void testChrome14_0_835_202() {
		RCH.currentRequestAttributes().currentRequest.addHeader("user-agent",
	        "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/535.1 (KHTML, like Gecko) Chrome/14.0.835.202 Safari/535.1")

	    assert !userAgentIdentService.isFirefox()
	    assert userAgentIdentService.isChrome()
	    assert !userAgentIdentService.isIOsDevice()
	    assert !userAgentIdentService.isMobile()
	    assert userAgentIdentService.getBrowserVersion() == "14.0.835.202"
	}
}