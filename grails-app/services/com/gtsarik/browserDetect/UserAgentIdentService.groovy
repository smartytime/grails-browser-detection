package com.gtsarik.browserDetect

import com.gtsarik.browserDetect.UserAgentIdentService.Browsers

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

		def userAgent = getUserAgentString()
		def userAgentInfo = getRequest().session.getAttribute(AGENT_INFO_TOKEN)

		// returns cached instances
		if (userAgentInfo != null && userAgentInfo.userAgentString == userAgent) {
			return userAgentInfo
		}

		if (userAgentInfo != null && userAgentInfo.userAgentString != userAgent) {
			log.warn "User agent string has changed in a single session!"
			log.warn "Previous User Agent: ${userAgentInfo.userAgentString}"
			log.warn "New User Agent: ${userAgent}"
			log.warn "Discarding existing agent info and creating new..."
		} else {
			log.debug "User agent info does not exist in session scope, creating..."
		}

		def browserVersion = null
		def browserType = null
		def operatingSystem = null
		def platform = null
		def security = "unknown"
		def language = "en-US"

		userAgentInfo = new UserAgentInfo()

		if (userAgent == null) {
			userAgentInfo.browserType = Browsers.CLIENT_UNKNOWN
			return userAgentInfo
		}

		browserType = Browsers.CLIENT_OTHER

		// determining browser type and version
		int pos
		// the order of browsers for checking is significant because
		// for example Chrome user-agent contains 'Safari' substring
		for(Browsers browser : [Browsers.CLIENT_FIREFOX, Browsers.CLIENT_CHROME,
				Browsers.CLIENT_SAFARI, Browsers.CLIENT_SEAMONKEY, Browsers.CLIENT_MSIE]) {

			if((pos = userAgent.indexOf(browser.name)) >= 0){
				browserType = browser
				// browser name and version are separated by /
				browserVersion = userAgent.substring(pos + browser.name.length() + 1).trim()

				// normal browsers have whitespace after version,
				// but IE has ;
				def stopCharacters = [" ", ";"]
				for(int i=0; i < browserVersion.length(); i++){
					if(browserVersion[i] in stopCharacters){
						browserVersion = browserVersion.substring(0, i)

						break
					}
				}

				log.debug("Browser type: ${browser.name} $browserVersion")

				break;
			}
		}

		// if browser is still undefined, check for BlackBerry
		if(browserType == Browsers.CLIENT_OTHER
				&& userAgent.indexOf(Browsers.CLIENT_BLACKBERRY.name) >= 0){

			browserType = Browsers.CLIENT_BLACKBERRY
			// we are interested in the first occurrence of /
			browserVersion = userAgent.substring(userAgent.indexOf("/")).trim()

			if (browserVersion.indexOf(" ") > 0){
				browserVersion = browserVersion.substring(0, browserVersion.indexOf(" "))
			}

			log.debug("Browser type: ${Browsers.CLIENT_BLACKBERRY.name} $browserVersion")
		}

		// figuring out information about OS
		if (userAgent.indexOf("(") > 0) {
			String osInfo = userAgent.substring(userAgent.indexOf("(") + 1)
			osInfo = osInfo.substring(0, osInfo.indexOf(")"))

			String[] infoParts = osInfo.split(" ")
			platform = (infoParts.size() > 0 ? infoParts[0] : '')
			operatingSystem = (infoParts.size() > 2 ? infoParts[2] : '')

			if (browserType != Browsers.CLIENT_MSIE && infoParts.size() > 1) {
				if (infoParts[1].equals("U")){
					security = "strong"
				}
				if (infoParts[1].equals("I")){
					security = "weak"
				}
				if (infoParts[1].equals("N")){
					security = "none"
				}

				language = (infoParts.size() > 3 ? infoParts[3] : '')
			}

		} else {
			if (browserType == Browsers.CLIENT_BLACKBERRY) {
				operatingSystem = "BlackBerry $browserVersion"
			}
		}

		userAgentInfo.browserVersion = browserVersion
		userAgentInfo.browserType = browserType
		userAgentInfo.operatingSystem = operatingSystem
		userAgentInfo.platform = platform
		userAgentInfo.security = security
		userAgentInfo.language = language
		userAgentInfo.userAgentString = userAgent

		getRequest().session.setAttribute(AGENT_INFO_TOKEN, userAgentInfo)
		return userAgentInfo
	}


	boolean isChrome() {
		isBrowserType(Browsers.CLIENT_CHROME)
	}

	boolean isFirefox() {
		isBrowserType(Browsers.CLIENT_FIREFOX)
	}

	boolean isMsie() {
		isBrowserType(Browsers.CLIENT_MSIE)
	}

	boolean isOther() {
		isBrowserType(Browsers.CLIENT_OTHER)
	}

	boolean isSafari() {
		isBrowserType(Browsers.CLIENT_SAFARI)
	}

	private boolean isBrowserType(Browsers browserType){
		getUserAgentInfo().browserType == browserType
	}

	boolean isIPhone() {
		isIOsDevice("iPhone")
	}

	boolean isIPad() {
		isIOsDevice("iPad")
	}

	private boolean isIOsDevice(String platform){
		def userAgent = getUserAgentInfo()
		return (userAgent.browserType == Browsers.CLIENT_SAFARI &&
				(userAgent.platform =~ /${platform}/ ||
						userAgent.operatingSystem =~ /${platform}/))
	}

	boolean isIOsDevice() {
		isIPhone() || isIPad()
	}

	boolean isAndroid() {
		getUserAgentInfo().operatingSystem =~ /Android/
	}

	boolean isPalm() {
		getUserAgentInfo().userAgentString =~ /(webOS|Palm(OS)?)/
	}

	boolean isWebkit() {
		getUserAgentInfo().userAgentString =~ /WebKit/
	}

	boolean isWindowsMobile() {
		getUserAgentInfo().userAgentString =~ /(Windows CE|PPC)/
	}

	boolean isBlackberry() {
		isBrowserType(Browsers.CLIENT_BLACKBERRY)
	}

	boolean isSeamonkey() {
		isBrowserType(Browsers.CLIENT_SEAMONKEY)
	}

	boolean isMobile() {
		isIPhone() || isAndroid() || isBlackberry() || isPalm() || isWindowsMobile()
	}

	String getBrowserVersion() {
		getUserAgentInfo().browserVersion
	}

	String getOperatingSystem() {
		getUserAgentInfo().operatingSystem
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

class UserAgentInfo {
	Browsers browserType
	String browserVersion
	String operatingSystem
	String platform
	String security
	String language
	String userAgentString
}