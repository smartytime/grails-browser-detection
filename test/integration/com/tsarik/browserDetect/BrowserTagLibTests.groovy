package com.tsarik.browserDetect

import org.springframework.web.context.request.RequestContextHolder as RCH
import grails.test.GroovyPagesTestCase

class BrowserTagLibTests extends GroovyPagesTestCase {

    protected void tearDown() {
        super.tearDown()
    }

    void testIsChrome() {
		RCH.currentRequestAttributes().currentRequest.addHeader("user-agent",
	        "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/535.1 (KHTML, like Gecko) Chrome/14.0.835.202 Safari/535.1")

	    assertOutputEquals("chrome", "<browser:isFirefox>firefox</browser:isFirefox><browser:isChrome>chrome</browser:isChrome>")
    }
}
