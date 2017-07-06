package de.k3its.remarkServer

import com.xenomachina.argparser.ArgParser

/**
 * @author Peter Kurfer
 * Created on 7/6/17.
 */
class CLArgs(parser: ArgParser) {

	private val numberRegex : Regex = Regex("^\\d{4}$")

	val workingDirectory by parser.storing("-w", "--workingDir", help = "Path to the working directory where index.html will be generated")

	val markdownFile by parser.storing("-m", "--markdownFile", help = "Path to the markdown file which will be monitored")

	val port : Int by parser.storing("-p", "--port", help = "Port where server listens", transform = { toInt() })
}