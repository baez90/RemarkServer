package de.k3its.remarkServer.tests

import de.k3its.remarkServer.setupRemark
import de.k3its.remarkServer.setupStyles
import io.kotlintest.matchers.shouldBe
import io.kotlintest.specs.StringSpec
import mu.KLogger
import mu.KotlinLogging
import org.apache.commons.io.FileUtils
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths

/**
 * @author Peter Kurfer
 * Created on 7/7/17.
 */
class SetupTest : StringSpec() {

	private val logger : KLogger = KotlinLogging.logger("TestLogger")

	init {
		"should copy the existing styles template to the folder specified in path" {
			val tmpDir = createTempDir()

			setupStyles(File("$tmpDir${File.separator}test.md").toPath())
			Files.exists(Paths.get("$tmpDir${File.separator}slides-style.css")) shouldBe true

			logger.debug { "delete tmp directory $tmpDir" }
			FileUtils.deleteDirectory(tmpDir)
		}

		"should copy embedded remark.js file to specified working directory" {
			val tmpDir = createTempDir()
			setupRemark(tmpDir.toString())
			Files.exists(Paths.get("$tmpDir${File.separator}remark-latest.min.js")) shouldBe true

			logger.debug { "delete tmp directory $tmpDir" }
			FileUtils.deleteDirectory(tmpDir)
		}
	}
}