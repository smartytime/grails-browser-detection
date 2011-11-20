package com.gtsarik.browserDetect

import com.gtsarik.browserDetect.UserAgentIdentService.Browsers
import nl.bitwalker.useragentutils.UserAgent
import nl.bitwalker.useragentutils.Browser
import nl.bitwalker.useragentutils.OperatingSystem
import nl.bitwalker.useragentutils.RenderingEngine
import nl.bitwalker.useragentutils.BrowserType

class UserAgentIdentService extends WebTierService {

	final static String AGENT_INFO_TOKEN = "${this.name}_agentInfo"

	final static String CHROME = "chrome"
	final static String FIREFOX = "firefox"
	final static String SAFARI = "safari"
	final static String OTHER = "other"
	final static String MSIE = "msie"
	final static String UNKNOWN = "unknown"
	final static String BLACKBERRY = "blackberry"
	final static String SEAMONKEY = "seamonkey"

	enum Browsers {
		CLIENT_CHROME("Chrome"),
		CLIENT_FIREFOX("Firefox"),
		CLIENT_SAFARI("Safari"),
		CLIENT_OTHER("Other"),
		CLIENT_MSIE("MSIE"),
		CLIENT_UNKNOWN("Unknown"),
		CLIENT_BLACKBERRY("BlackBerry"),
		CLIENT_SEAMONKEY("SeaMonkey");

		final String name

		Browsers(String name){
			this.name = name
		}
	}

	boolean transactional = false

	def getUserAgentString() {
		getRequest().getHeader("user-agent")
	}

	def getUserAgentInfo() {

		def userAgentString = getUserAgentString()
		def userAgent = getRequest().session.getAttribute(AGENT_INFO_TOKEN)

		// returns cached instance
		if (userAgent != null && userAgent.userAgentString == userAgentString) {
			return userAgentInfo
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
		def browser = getUserAgentInfo().browser

		browser.group == browserForChecking || browser == browserForChecking
	}

	private boolean isOs(OperatingSystem osForChecking){
		def os = getUserAgentInfo().operatingSystem

		os.group == osForChecking || os == osForChecking
	}

	boolean isIPhone() {
		def os = getUserAgentInfo().operatingSystem

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
		getUserAgentInfo().browser.renderingEngine == RenderingEngine.WEBKIT
	}

	boolean isWindowsMobile() {
		def os = getUserAgentInfo().operatingSystem

		os == OperatingSystem.WINDOWS_MOBILE || os == OperatingSystem.WINDOWS_MOBILE7
	}

	boolean isBlackberry() {
		isOs(OperatingSystem.BLACKBERRY)
	}

	boolean isSeamonkey() {
		isBrowser(Browser.SEAMONKEY)
	}

	boolean isMobile() {
		getUserAgentInfo().browser.browserType == BrowserType.MOBILE_BROWSER
	}

	String getBrowserVersion() {
		getUserAgentInfo().browserVersion.version
	}

	String getOperatingSystem() {
		getUserAgentInfo().operatingSystem.name
	}

	String getPlatform() {
		getUserAgentInfo().platform
	}

	String getSecurity() {
		getUserAgentInfo().security
	}

	String getLanguage() {
		getUserAgentInfo().language
	}

	String getBrowserType() {
		switch (getUserAgentInfo().browserType) {
			case Browsers.CLIENT_FIREFOX:
				return FIREFOX
			case Browsers.CLIENT_CHROME:
				return CHROME
			case Browsers.CLIENT_SAFARI:
				return SAFARI
			case Browsers.CLIENT_SEAMONKEY:
				return SEAMONKEY
			case Browsers.CLIENT_MSIE:
				return MSIE
			case Browsers.CLIENT_BLACKBERRY:
				return BLACKBERRY
			default:
				return OTHER
		}
	}
}

/**
 * Wraps information about user-agent. It is similar to
 * previous versions of plug-in because of compatibility reasons.
 */
class UserAgentWrapper {
	Browsers browserType
	String browserVersion
	String operatingSystem
	String platform
	String security
	String language

	/**
	 * Source user-agent string
	 */
	String userAgentString
	/**
	 * All information
	 */
	UserAgent userAgent
}