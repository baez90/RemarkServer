package de.k3its.remarkServer.generation

import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.launch
import mu.KLogger
import mu.KotlinLogging
import org.apache.commons.lang.text.StrBuilder
import org.apache.velocity.Template
import org.apache.velocity.VelocityContext
import org.apache.velocity.app.VelocityEngine
import org.apache.velocity.runtime.RuntimeConstants
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader
import java.io.File
import java.io.PrintWriter
import java.nio.file.*


/**
 * @author Peter Kurfer
 * Created on 7/6/17.
 */

class RemarkHtmlGenerator(val markdownPath: Path, htmlTargetPath: String) {

	private object Constants {
		val REMARK_TEMPLATE_PATH = "/templates/remarkTemplate.vm"
	}

	private val logger : KLogger = KotlinLogging.logger{}
	private val velocityEngine: VelocityEngine = VelocityEngine()
	private val htmlTemplate: Template
	private val htmlTargetFile: File
	private val stylesFilePath: Path
	private val watcher: WatchService = FileSystems.getDefault().newWatchService()
	private val dirWatchKeys: MutableList<WatchKey> = mutableListOf()

	init {
		velocityEngine.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath")
		velocityEngine.setProperty("classpath.resource.loader.class", ClasspathResourceLoader::class.java.name)
		velocityEngine.init()
		htmlTemplate = velocityEngine.getTemplate(Constants.REMARK_TEMPLATE_PATH)
		htmlTargetFile = File(htmlTargetPath)
		stylesFilePath = File("${markdownPath.parent}${File.separator}slides-style.css").toPath()
		registerWatch(markdownPath)
		registerWatch(htmlTargetFile.toPath())
	}

	fun start() {
		launch(CommonPool) {
			while (true) {
				dirWatchKeys.forEach { key ->
					val events = key.pollEvents().filter {
						event ->
						event.kind() == StandardWatchEventKinds.ENTRY_MODIFY && when (event.context()) {
							is Path -> event.context().toString().matches(Regex("^.*\\.(md|css)\$"))
							else -> false
						}
					}.firstOrNull()
					if (events != null) {
						val stringBuilder = StringBuilder()
						Files.readAllLines(markdownPath).forEach { stringBuilder.appendln(it) }
						regenerateHtml(Files.readAllLines(markdownPath).concat(), Files.readAllLines(stylesFilePath).concat())
					}
				}

				delay(1000)
			}
		}
	}

	private fun regenerateHtml(markdownBody: String, stylesBody: String) {
		val templateContext = VelocityContext()
		templateContext.put("markdownBody", markdownBody)
		templateContext.put("stylesBody", stylesBody)
		PrintWriter(htmlTargetFile).use {
			htmlTemplate.merge(templateContext, it)
		}
		logger.info { "Regenerated remark html" }
	}

	private fun registerWatch(path: Path) {
		val watchKey = path.parent.register(watcher, java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY)
		dirWatchKeys.add(watchKey)
	}
}

private fun MutableList<String>.concat(): String {
	val stringBuilder = StrBuilder()
	this.forEach { stringBuilder.appendln(it) }
	return stringBuilder.toString()
}