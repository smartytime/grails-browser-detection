class BrowserDetectGrailsPlugin {
    def version = "0.1"
    def grailsVersion = "1.3.2 > *"
    def dependsOn = [:]
    def pluginExcludes = [
            "grails-app/views/error.gsp",
            "grails-app/views/test/test.gsp",
            "grails-app/views/layouts/main.gsp",
            "grails-app/controllers/com/nolatechhelp/browserDetect/TestController.groovy"
    ]

    def author = "Gennady Tsarik"
    def authorEmail = "vare6gin@gmail.com"
    def title = "Browser Detection Plug-in"
    def description = '''\\
Refactored and extended version of the plugin created by Kevin M. Gill, Edvinas Bartkus, Jesse Varnado. Almost all code
was removed and replaced with browser detection code based on user-agent-utils library (http://user-agent-utils.java.net/).
Old interfaces are saved for compatibility reasons.
'''
    def documentation = "http://grails.org/plugin/browser-detect"

    def doWithWebDescriptor = { xml ->
    }

    def doWithSpring = {
    }

    def doWithDynamicMethods = { ctx ->
    }

    def doWithApplicationContext = { applicationContext ->
    }

    def onChange = { event ->
    }

    def onConfigChange = { event ->
    }
}
