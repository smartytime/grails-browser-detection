package org.geeks.browserdetection

import org.springframework.web.context.request.RequestContextHolder as RCH
import grails.test.GroovyPagesTestCase

class BrowserTagLibTests extends GroovyPagesTestCase {

    protected void tearDown() {
        super.tearDown()
    }

    void testIsChrome() {
		RCH.currentRequestAttributes().currentRequest.addHeader("user-agent",
	        "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/535.1 (KHTML, like Gecko) Chrome/14.0.835.202 Safari/535.1")

	    assertOutputEquals("chrome", "<browser:isFirefox>firefox</browser:isFirefox>"
			    + "<browser:isChrome>chrome</browser:isChrome>"
			    + "<browser:isMobile>mobile</browser:isMobile>")
    }

	void testVersionСomparison_equalIe6() {
		RCH.currentRequestAttributes().currentRequest.addHeader("user-agent",
	        "Mozilla/4.08 (compatible; MSIE 6.0; Windows NT 5.1)")

	    assertOutputEquals("MSIE 6.0", "<browser:isMsie version='6.0'>MSIE 6.0</browser:isMsie>"
			    + "<browser:isMsie version='7.0'>MSIE 7.0</browser:isMsie>"
			    + "<browser:isFirefox>firefox</browser:isFirefox>"
			    + "<browser:isChrome>chrome</browser:isChrome>"
			    + "<browser:isMobile>mobile</browser:isMobile>")

		assertOutputEquals("MSIE 6.0", "<browser:isIE6>MSIE 6.0</browser:isIE6>")
    }

	void testIsIe7() {
		RCH.currentRequestAttributes().currentRequest.addHeader("user-agent",
	        "Mozilla/5.0 (MSIE 7.0; Macintosh; U; SunOS; X11; gu; SV1; InfoPath.2; .NET CLR 3.0.04506.30; .NET CLR 3.0.04506.648)")

	    assertOutputEquals("MSIE 7.0", "<browser:isIE7>MSIE 7.0</browser:isIE7>")
    }

	void testIsIe7b() {
		RCH.currentRequestAttributes().currentRequest.addHeader("user-agent",
	        "Mozilla/4.0 (compatible; MSIE 7.0b; Windows NT 6.0)")

	    assertOutputEquals("MSIE 7.0", "<browser:isIE7>MSIE 7.0</browser:isIE7>")
    }

	void testIsIe8() {
		RCH.currentRequestAttributes().currentRequest.addHeader("user-agent",
	        "Mozilla/5.0 (compatible; MSIE 8.0; Windows NT 5.0; Trident/4.0; InfoPath.1; SV1; .NET CLR 3.0.4506.2152; .NET CLR 3.5.30729; .NET CLR 3.0.04506.30)")

	    assertOutputEquals("MSIE 8.0", "<browser:isIE8>MSIE 8.0</browser:isIE8>")
    }

	void testIsIe9() {
		RCH.currentRequestAttributes().currentRequest.addHeader("user-agent",
	        "Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; Win64; x64; Trident/5.0; .NET CLR 3.5.30729; .NET CLR 3.0.30729; .NET CLR 2.0.50727; Media Center PC 6.0)")

	    assertOutputEquals("MSIE 9.0", "<browser:isIE9 version='9.0'>MSIE 9.0</browser:isIE9>")
    }

	void testVersionСomparison_lessIe7() {
		RCH.currentRequestAttributes().currentRequest.addHeader("user-agent",
	        "Mozilla/4.08 (compatible; MSIE 6.0; Windows NT 5.1)")

	    assertOutputEquals("lower MSIE 7.0", "<browser:isMsie versionLower='7.0'>lower MSIE 7.0</browser:isMsie>"
			    + "<browser:isMsie versionLower='6.0'>lower MSIE 6.0</browser:isMsie>"
			    + "<browser:isFirefox>firefox</browser:isFirefox>"
			    + "<browser:isChrome>chrome</browser:isChrome>"
			    + "<browser:isMobile>mobile</browser:isMobile>")
    }

	void testVersionСomparison_moreIe6() {
		RCH.currentRequestAttributes().currentRequest.addHeader("user-agent",
	        "Mozilla/5.0 (MSIE 7.0; Macintosh; U; SunOS; X11; gu; SV1; InfoPath.2; .NET CLR 3.0.04506.30; .NET CLR 3.0.04506.648)")

	    assertOutputEquals("greater MSIE 6.0", "<browser:isMsie versionGreater='6.0'>greater MSIE 6.0</browser:isMsie>"
			    + "<browser:isMsie versionGreater='7.0'>greater MSIE 7.0</browser:isMsie>"
			    + "<browser:isFirefox>firefox</browser:isFirefox>"
			    + "<browser:isChrome>chrome</browser:isChrome>"
			    + "<browser:isMobile>mobile</browser:isMobile>")
    }

	void testVersionСomparison_isSafari5() {
		RCH.currentRequestAttributes().currentRequest.addHeader("user-agent",
	        "Mozilla/5.0 (Windows; U; Windows NT 6.1; zh-HK) AppleWebKit/533.18.1 (KHTML, like Gecko) Version/5.0.2 Safari/533.18.5")

	    assertOutputEquals("Safari 5.*", "<browser:isSafari version='5.*'>Safari 5.*</browser:isSafari>"
			    + "<browser:isMsie versionGreater='7.0'>greater MSIE 7.0</browser:isMsie>"
			    + "<browser:isFirefox>firefox</browser:isFirefox>"
			    + "<browser:isChrome>chrome</browser:isChrome>"
			    + "<browser:isMobile>mobile</browser:isMobile>")
    }

	void testIsOther() {
		RCH.currentRequestAttributes().currentRequest.addHeader("user-agent",
	        "undefined (undefined; undefined)")

	    assertOutputEquals("undefined", "<browser:isOther>undefined</browser:isOther>")
    }
}