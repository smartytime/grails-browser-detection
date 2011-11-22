package com.gtsarik.browserDetect

/**
 * <pre>
 * {@code
 * <browser:choice>
 * <browser:isIPhone>
 *     iPhone code
 * </browser:isIPhone>
 * <browser:isBlackberry>
 *     BlackBerry code
 * </browser:isBlackberry>
 * <browser:otherwise>
 *     code for other browsers
 * </browser:otherwise>
 * </browser:choice>
 * }
 * </pre>
 */
class BrowserTagLib {

	static namespace = 'browser'

	private static def CHOICE_STACK = "${this.name}_choiceStack"

	private enum HierarchyLevelType {
		ChoiceTag,
		ConditionTag
	}

	private class HierarchyLevelHolder {
		HierarchyLevelType levelType

		Closure otherwise
		boolean successfulCondition
	}

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
	    handleBrowser attrs, body, { userAgentIdentService.isMsie() }
    }

    def isNotMsie = { attrs, body ->
	    handle body, { !userAgentIdentService.isMsie() }
    }

    def isFirefox = { attrs, body ->
	    handleBrowser attrs, body, { userAgentIdentService.isFirefox() }
    }

    def isNotFirefox = { attrs, body ->
	    handle body, { !userAgentIdentService.isFirefox() }
    }

    def isChrome = { attrs, body ->
	    handleBrowser attrs, body, { userAgentIdentService.isChrome() }
    }

    def isNotChrome = { attrs, body ->
	    handle body, { !userAgentIdentService.isChrome() }
    }

    def isSafari = { attrs, body ->
	    handleBrowser attrs, body, { userAgentIdentService.isSafari() }
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

	private def handleBrowser(attrs, body, serviceMethodName){
		def version = null
		def comparisonType = null
		if(attrs.version){
			version = attrs.version
			comparisonType = ComparisonType.EQUAL
		} else if(attrs.versionLess){
			version = attrs.versionLess
			comparisonType = ComparisonType.LESS
		} else if(attrs.versionMore){
			version = attrs.versionMore
			comparisonType = ComparisonType.GREATER
		}

		handle body, { userAgentIdentService."$serviceMethodName"(version, comparisonType) }
	}

	private def handle(body, condition){
		def stack = getStack()
		def parent = (!stack.empty()) ? stack.peek() : null

		// skip if successful condition is met in choice tag
		if(parent && parent.levelType == HierarchyLevelType.ChoiceTag
				&& parent.successfulCondition){
			return
		}

		if(condition()){
			def conditionTagHolder = new HierarchyLevelHolder()
			conditionTagHolder.levelType = HierarchyLevelType.ConditionTag
			stack.push(conditionTagHolder)

			out << body()

			stack.pop()

			if(parent && parent.levelType == HierarchyLevelType.ChoiceTag){
				parent.successfulCondition = true
			}
		}
	}

	def choice = { attrs, body ->
		def stack = getStack()

		if (!stack.empty() && stack.peek().levelType == HierarchyLevelType.ChoiceTag) {
			throw new IllegalStateException("choice tag can't be putted under other choice tag")
		}

		def choiceTagHolder = new HierarchyLevelHolder()
		choiceTagHolder.levelType = HierarchyLevelType.ChoiceTag
		stack.push(choiceTagHolder)

		out << body()

		// if no successful condition and otherwise tag presented,
		// execute otherwise tag
		if(!choiceTagHolder.successfulCondition && choiceTagHolder.otherwise){
			out << choiceTagHolder.otherwise()
		}

		stack.pop()
	}

	/**
	 * Returns hierarchy stack, Creates one if it does
	 * not exist yet.
	 */
	private Stack<HierarchyLevelHolder> getStack(){
		try {
			pageScope."$CHOICE_STACK"
		} catch (e){
			def stack = new Stack()
			pageScope."$CHOICE_STACK" = stack

			stack
		}
	}

	/**
	 * This content is rendered in case of nothing is true in choice tag
	 */
	def otherwise = { attrs, body ->
		def stack = getStack()

		if(stack == null || stack.empty()){
			throw new IllegalStateException("otherwise tag should be under choice tag")
		}

		stack.peek().otherwise = body
	}
}