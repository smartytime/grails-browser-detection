package com.gtsarik.browserDetect

/**
 * <pre>
 * {@code
 * <browser:isIPhone>
 *     iPhone code
 * </browser:isIPhone>
 * <browser:isBlackberry>
 *     BlackBerry code
 * </browser:isBlackberry>
 * <browser:otherwise>
 *     code for other browsers
 * </browser:otherwise>
 * }
 * </pre>
 */
class BrowserTagLib {

	static namespace = 'browser'

	/**
	 * It is a key of token informing about logical branching results. If its value is
	 * false, otherwise tag is rendered.
	 */
	private static def SUCCESS_TOKEN = "${this.name}_successToken"

    def userAgentIdentService

	def isMobile = { attrs, body ->
        handle body, { userAgentIdentService.isMobile() }
    }

	def isNotMobile = { attrs, body ->
		handle body, { !userAgentIdentService.isMobile() }
    }

	def isIPhone = { attrs, body ->
        handle body, { userAgentIdentService.isIPhone() }
    }

	def isNotIPhone = { attrs, body ->
		handle body, { !userAgentIdentService.isIPhone() }
    }

	def isIPad = { attrs, body ->
        handle body, { userAgentIdentService.isIPad() }
    }

	def isNotIPad = { attrs, body ->
		handle body, { !userAgentIdentService.isIPad() }
    }

	def isIOS = { attrs, body ->
		handle body, { userAgentIdentService.isIOsDevice() }
    }

	def isAndroid = { attrs, body ->
		handle body, { userAgentIdentService.isAndroid() }
    }

	def isNotAndroid = { attrs, body ->
		handle body, { !userAgentIdentService.isAndroid() }
    }

	def isPalm = { attrs, body ->
        handle body, { userAgentIdentService.isPalm() }
    }

	def isNotPalm = { attrs, body ->
		handle body, { !userAgentIdentService.isPalm() }
    }

	def isWebkit = { attrs, body ->
        handle body, { userAgentIdentService.isWebkit() }
    }

	def isNotWebkit = { attrs, body ->
		handle body, { !userAgentIdentService.isWebkit() }
    }

	def isWindowsMobile = { attrs, body ->
		handle body, { userAgentIdentService.isWindowsMobile() }
    }

	def isNotWindowsMobile = { attrs, body ->
		handle body, { !userAgentIdentService.isWindowsMobile() }
    }

    def isMsie = { attrs, body ->
	    handle body, { userAgentIdentService.isMsie() }
    }

    def isNotMsie = { attrs, body ->
	    handle body, { !userAgentIdentService.isMsie() }
    }

    def isFirefox = { attrs, body ->
	    handle body, { userAgentIdentService.isFirefox() }
    }

    def isNotFirefox = { attrs, body ->
	    handle body, { !userAgentIdentService.isFirefox() }
    }

    def isChrome = { attrs, body ->
	    handle body, { userAgentIdentService.isChrome() }
    }

    def isNotChrome = { attrs, body ->
	    handle body, { !userAgentIdentService.isChrome() }
    }

    def isSafari = { attrs, body ->
	    handle body, { userAgentIdentService.isSafari() }
    }

    def isNotSafari = { attrs, body ->
	    handle body, { !userAgentIdentService.isSafari() }
    }

    def isBlackberry = { attrs, body ->
	    handle body, { userAgentIdentService.isBlackberry() }
    }

    def isNotBlackberry = { attrs, body ->
	    handle body, { !userAgentIdentService.isBlackberry() }
    }

	private def handle(body, condition){
		if(condition()){
			out << body()
			setSuccessToken(true)
		} else {
			setSuccessToken(false)
		}
	}

	/**
	 * This content is rendered in case of unsuccessful conditions of
	 * previous tags.
	 */
	def otherwise = { attrs, body ->
		def token = request[SUCCESS_TOKEN]

		if(token == null){
			throw new IllegalStateException("otherwise tag should be used after one of browser tags")
		}

		if(!token){
			out << body()
			setSuccessToken null
		}
	}

	/**
	 * Sets success token. Set true to prevent "otherwise" tag execution.
	 * Set null value after other tag is executed.
	 */
	private def setSuccessToken(Boolean value){
		request[SUCCESS_TOKEN] = value
	}
}