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

	String getUserAgentString() {
		getRequest().getHeader("user-agent")
	}

	private def getUserAgent() {

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

	boolean isChrome(ComparisonType comparisonType = null, String version = null) {
		isBrowser(Browser.CHROME, comparisonType, version)
	}

	boolean isFirefox(ComparisonType comparisonType = null, String version = null) {
		isBrowser(Browser.FIREFOX, comparisonType, version)
	}

	boolean isMsie(ComparisonType comparisonType = null, String version = null) {
		// why people use it?
		isBrowser(Browser.IE, comparisonType, version)
	}

	boolean isOther(ComparisonType comparisonType = null, String version = null) {
		isBrowser(Browser.UNKNOWN, comparisonType, version)
	}

	boolean isSafari(ComparisonType comparisonType = null, String version = null) {
		isBrowser(Browser.SAFARI, comparisonType, version)
	}

	boolean isOpera(ComparisonType comparisonType = null, String version = null) {
		isBrowser(Browser.OPERA, comparisonType, version)
	}

	private boolean isBrowser(Browser browserForChecking, ComparisonType comparisonType = null,
	                          String version = null){
		def userAgent = getUserAgent()
		def browser = userAgent.browser

		// browser checking
		if(!(browser.group == browserForChecking || browser == browserForChecking)){
			return false
		}

		// version checking
		if(version){
			if(!comparisonType){
				throw new IllegalArgumentException("comparisonType should be specified")
			}

			def compRes = compareVersions(userAgent.browserVersion.version, version)

			if(compRes == 0 && comparisonType == ComparisonType.EQUAL){
				return true
			}

			if(compRes == 1 && comparisonType == ComparisonType.GREATER){
				return true
			}

			if(compRes == -1 && comparisonType == ComparisonType.LOWER){
				return true
			}

			return false
		}

		true
	}

	/**
	 * Compares versions like x.y.z
	 *
	 * @return a negative integer, zero, or a positive integer as the first argument is less than,
	 * equal to, or greater than the second
	 */
	private int compareVersions(v1, v2){
		if(!v1){
			if(!v2){
				return 0
			} else {
				return -1
			}
		}

		if(!v2){
			return 1
		}

		def v1parts = v1.split("\\.")
		def v2parts = v2.split("\\.")

		def length = Math.min(v1parts.size(), v2parts.size())

		int compRes
		for(int i=0; i<length; i++){
			compRes = v1parts[i].toInteger().compareTo(v2parts[i].toInteger())

			if(compRes != 0){
				return compRes
			}
		}

		v1parts.size() <=> v2parts.size()
	}

	private boolean isOs(OperatingSystem osForChecking){
		def os = getUserAgent().operatingSystem

		os.group == osForChecking || os == osForChecking
	}

	boolean isiPhone() {
		def os = getUserAgent().operatingSystem

		os == OperatingSystem.iOS4_IPHONE || os == OperatingSystem.MAC_OS_X_IPHONE
	}

	boolean isiPad() {
		isOs(OperatingSystem.MAC_OS_X_IPAD)
	}

	boolean isiOsDevice() {
		isOs(OperatingSystem.IOS)
	}

	boolean isAndroid() {
		isOs(OperatingSystem.ANDROID)
	}

	boolean isPalm() {
		isOs(OperatingSystem.PALM)
	}

	boolean isLinux(){
		isOs(OperatingSystem.LINUX)
	}

	boolean isWindows(){
		isOs(OperatingSystem.WINDOWS)
	}

	boolean isOSX(){
		isOs(OperatingSystem.MAC_OS_X)
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

	/**
	 * It is left for compatibility reasons.
	 */
	@Deprecated
	def getUserAgentInfo() {
		def userAgent = getUserAgent()

		[
			browserType: userAgent.browser.name,
			browserVersion: userAgent.browserVersion.version,
			operatingSystem: userAgent.operatingSystem.name,
			platform: "",
			security: "",
			language: "",
			agentString: userAgent.userAgentString
		]
	}
}

public enum ComparisonType {
	LOWER,
	EQUAL,
	GREATER
}