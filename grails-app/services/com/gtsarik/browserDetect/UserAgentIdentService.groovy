package com.gtsarik.browserDetect

import nl.bitwalker.useragentutils.UserAgent
import nl.bitwalker.useragentutils.Browser
import nl.bitwalker.useragentutils.OperatingSystem
import nl.bitwalker.useragentutils.RenderingEngine
import nl.bitwalker.useragentutils.BrowserType
import javax.transaction.NotSupportedException

class UserAgentIdentService extends WebTierService {

	final static String AGENT_INFO_TOKEN = "${this.name}_agentInfo"

	boolean transactional = false

	def getUserAgentString() {
		getRequest().getHeader("user-agent")
	}

	def getUserAgent() {

		def userAgentString = getUserAgentString()
		def userAgent = getRequest().session.getAttribute(AGENT_INFO_TOKEN)

		// returns cached instance
		if (userAgent != null && userAgent.userAgentString == userAgentString) {
			return userAgent
		}

		if (userAgent != null && userAgent.userAgentString != userAgent) {
			log.warn "User agent string has changed in a single session!"
			log.warn "Previous User Agent: ${userAgent.userAgentString}"
			log.warn "New User Agent: ${userAgentString}"
			log.warn "Discarding existing agent info and creating new..."
		} else {
			log.debug "User agent info does not exist in session scope, creating..."
		}

		userAgent = parseUserAgent(userAgentString)

		getRequest().session.setAttribute(AGENT_INFO_TOKEN, userAgent)
		return userAgent
	}

	private def parseUserAgent(String userAgentString){
		UserAgent.parseUserAgentString(userAgentString)
	}

	boolean isChrome() {
		isBrowser(Browser.CHROME)
	}

	boolean isFirefox() {
		isBrowser(Browser.FIREFOX)
	}

	boolean isMsie() {
		// why people use it?
		isBrowser(Browser.IE)
	}

	boolean isOther() {
		isBrowser(Browser.UNKNOWN)
	}

	boolean isSafari() {
		isBrowser(Browser.SAFARI)
	}

	private boolean isBrowser(Browser browserForChecking){
		def browser = getUserAgent().browser

		browser.group == browserForChecking || browser == browserForChecking
	}

	private boolean isOs(OperatingSystem osForChecking){
		def os = getUserAgent().operatingSystem

		os.group == osForChecking || os == osForChecking
	}

	boolean isIPhone() {
		def os = getUserAgent().operatingSystem

		os == OperatingSystem.iOS4_IPHONE || os == OperatingSystem.MAC_OS_X_IPHONE
	}

	boolean isIPad() {
		isOs(OperatingSystem.MAC_OS_X_IPAD)
	}

	boolean isIOsDevice() {
		isOs(OperatingSystem.IOS)
	}

	boolean isAndroid() {
		isOs(OperatingSystem.ANDROID)
	}

	boolean isPalm() {
		isOs(OperatingSystem.PALM)
	}

	boolean isWebkit() {
		getUserAgent().browser.renderingEngine == RenderingEngine.WEBKIT
	}

	boolean isWindowsMobile() {
		def os = getUserAgent().operatingSystem

		os == OperatingSystem.WINDOWS_MOBILE || os == OperatingSystem.WINDOWS_MOBILE7
	}

	boolean isBlackberry() {
		isOs(OperatingSystem.BLACKBERRY)
	}

	boolean isSeamonkey() {
		isBrowser(Browser.SEAMONKEY)
	}

	boolean isMobile() {
		getUserAgent().browser.browserType == BrowserType.MOBILE_BROWSER
	}

	String getBrowserName(){
		getUserAgent().browser.name
	}

	String getBrowserVersion() {
		getUserAgent().browserVersion.version
	}

	String getOperatingSystem() {
		getUserAgent().operatingSystem.name
	}

	String getPlatform() {
		throw new NotSupportedException()
	}

	String getSecurity() {
		throw new NotSupportedException()
	}

	String getLanguage() {
		throw new NotSupportedException()
	}

	/**
	 * It is left for compatibility reasons.
	 */
	@Deprecated
	String getBrowserType() {
		getBrowserName()
	}
}