grails.project.class.dir = "target/classes"
grails.project.test.class.dir = "target/test-classes"
grails.project.test.reports.dir = "target/test-reports"
grails.project.work.dir = "work"
//grails.project.war.file = "target/${appName}-${appVersion}.war"
grails.project.dependency.distribution = {
	remoteRepository(id:"internal", url:"http://slice.nolatechhelp.com/archiva/repository/internal/") 	{
		authentication username:"user", password:"2285833"
	}
	remoteRepository(id:"snapshots", url:"http://slice.nolatechhelp.com/archiva/repository/snapshots/") 	{
		authentication username:"user", password:"2285833"
	}
}
grails.project.dependency.resolution = {
    // inherit Grails' default dependencies
    inherits("global") {
        // uncomment to disable ehcache
        // excludes 'ehcache'
    }
    log "warn" // log level of Ivy resolver, either 'error', 'warn', 'info', 'debug' or 'verbose'
    repositories {
        grailsPlugins()
        grailsHome()
        grailsCentral()

        mavenLocal()
        mavenCentral()
    }
    dependencies {
	    runtime "nl.bitwalker:UserAgentUtils:1.2.4"
    }
}
