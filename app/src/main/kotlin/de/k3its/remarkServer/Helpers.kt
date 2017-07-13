package de.k3its.remarkServer

/**
 * @author Peter Kurfer
 * Created on 7/13/17.
 */

fun MutableList<String>.concat(): String {
	val stringBuilder = StringBuilder()
	this.forEach { stringBuilder.appendln(it) }
	return stringBuilder.toString()
}