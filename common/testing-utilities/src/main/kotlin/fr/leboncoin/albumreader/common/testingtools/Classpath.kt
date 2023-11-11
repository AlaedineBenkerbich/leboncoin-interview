package fr.leboncoin.albumreader.common.testingtools

import java.nio.charset.Charset

/**
 * Utility methods for classpath.
 */
object Classpath {

    /**
     * @return Given resource path text content.
     */
    fun resourceText(resourcePath: String, charset: Charset = Charsets.UTF_8): String =
        Classpath::class.java.classLoader.getResource(resourcePath)!!.readText(charset)
}