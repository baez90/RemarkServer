package de.k3its.remarkServer

import com.xenomachina.argparser.ArgParser
import de.k3its.remarkServer.generation.RemarkHtmlGenerator
import io.vertx.core.Vertx
import io.vertx.ext.web.Router
import io.vertx.ext.web.handler.StaticHandler
import java.io.File

/**
 * @author Peter Kurfer
 * Created on 7/6/17.
 */

fun main(args: Array<String>) {

	val clArgs = CLArgs(ArgParser(args))

	val markdownPath = File(clArgs.markdownFile).toPath()
	setupStyles(markdownPath)
	setupRemark(clArgs.workingDirectory)

	val generator = RemarkHtmlGenerator(markdownPath, "${clArgs.workingDirectory}/index.html")
	generator.start()

	val vertx = Vertx.vertx()
	val server = vertx.createHttpServer()
	val router = Router.router(vertx)

	val staticHandler = StaticHandler.create()
			.setAllowRootFileSystemAccess(true)
			.setWebRoot(clArgs.workingDirectory)
			.setCachingEnabled(false)
			.setIndexPage("index.html")

	router.route("/*").handler(staticHandler)

	server.requestHandler({ router.accept(it) }).listen(clArgs.port)
}