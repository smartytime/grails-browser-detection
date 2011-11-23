class BrowserDetectionGrailsPlugin {
    def version = "0.3"
    def grailsVersion = "1.3.2 > *"
    def dependsOn = [:]
    def pluginExcludes = [
            "grails-app/views/error.gsp",
            "grails-app/views/test/test.gsp",
            "grails-app/views/layouts/main.gsp",
            "grails-app/controllers/com/nolatechhelp/browserDetect/TestController.groovy"
    ]

    def author = "Kevin M. Gill, Edvinas Bartkus, Gennady Tsarik"
    def authorEmail = "vare6gin@gmail.com"
    def title = "Browser Detection Plug-in"
    def description = '''\\
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