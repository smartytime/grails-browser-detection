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

	void testMSIE7() {
		RCH.currentRequestAttributes().currentRequest.addHeader("user-agent",
	        "Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 5.1; GTB6.4; .NET CLR 1.1.4322; FDM; .NET CLR 2.0.50727; .NET CLR 3.0.04506.30; .NET CLR 3.0.4506.2152; .NET CLR 3.5.30729)")

	    assert !userAgentIdentService.isFirefox()
	    assert !userAgentIdentService.isChrome()
		assert userAgentIdentService.isMsie()
	    assert !userAgentIdentService.isIOsDevice()
	    assert !userAgentIdentService.isMobile()
	    assert userAgentIdentService.getBrowserVersion() == "7.0"
	}

	void testMSIE6() {
		RCH.currentRequestAttributes().currentRequest.addHeader("user-agent",
	        "Mozilla/4.0 (compatible; MSIE 6.0; Windows 98; Rogers Hi·Speed Internet; (R1 1.3))")

	    assert !userAgentIdentService.isFirefox()
	    assert !userAgentIdentService.isChrome()
		assert userAgentIdentService.isMsie()
	    assert !userAgentIdentService.isIOsDevice()
	    assert !userAgentIdentService.isMobile()
	    assert userAgentIdentService.getBrowserVersion() == "6.0"
	}
}