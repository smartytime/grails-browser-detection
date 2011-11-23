<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
    </head>
    <body>
        You are using
        <browser:choice>
	        <browser:isMobile>
		        a mobile browser. And it is...
		        <browser:choice>
			        <browser:isiPad>
				        iPad!
			        </browser:isiPad>
			        <browser:isiPhone>
				        iPhone!
			        </browser:isiPhone>
			        <browser:isAndroid>
				        Android!
			        </browser:isAndroid>
			        <browser:otherwise>
				        I do not know...
			        </browser:otherwise>
			    </browser:choice>
	        </browser:isMobile>
	        <browser:otherwise>
		        <browser:isChrome>
			        Chrome.
		        </browser:isChrome>
		        <browser:isFirefox>
			        Firefox.
		        </browser:isFirefox>
		        <browser:isMsie>
			        Internet Explorer :(
		        </browser:isMsie>
	        </browser:otherwise>
        </browser:choice>
	</body>
</html>