package de.k3its.remarkServer

import java.io.File
import java.io.FileOutputStream
import java.nio.file.Files
import java.nio.file.Path

/**
 * @author Peter Kurfer
 * Created on 7/6/17.
 */

object Setup {
	fun setupRemark(workingDirectory: String) {
		Files.createDirectories(File(workingDirectory).toPath())
		val remarkScript = File("$workingDirectory${File.separator}remark-latest.min.js")
		FileOutputStream(remarkScript).use { outStream ->
			ClassLoader.getSystemResourceAsStream("static/remark-latest.min.js").use { remarkStream ->
				remarkStream.copyTo(outStream)
			}
		}
	}

	fun setupStyles(markdownPath: Path) {
		val stylesFile = File("${markdownPath.parent}${File.separator}slides-style.css")
		if (!stylesFile.exists()) {
			FileOutputStream(stylesFile).use { outStream ->
				ClassLoader.getSystemResourceAsStream("static/slides-style.css").use { stylesStream ->
					stylesStream.copyTo(outStream)
				}
			}
		}
	}
}