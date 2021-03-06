/*
 * Licensed under Apache-2.0
 *
 * Designed and developed by Aidan Follestad (@afollestad)
 */
package com.afollestad.iconrequest.extensions

import android.net.Uri
import java.io.Closeable
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream

internal fun File.wipe(): File {
  if (!exists()) return this
  if (isDirectory) {
    val contents = listFiles()
    if (contents != null && contents.isNotEmpty()) {
      for (fi in contents) fi.wipe()
    }
  }
  delete()
  return this
}

@Throws(Exception::class)
internal fun String.writeAll(file: File) {
  toByteArray(charset("UTF-8")).writeAll(file)
}

@Throws(Exception::class)
internal fun ByteArray.writeAll(file: File) {
  var os: OutputStream? = null
  try {
    os = FileOutputStream(file)
    os.write(this)
    os.flush()
  } finally {
    os?.closeQuietly()
  }
}

internal fun Closeable.closeQuietly() {
  try {
    close()
  } catch (ignored: Throwable) {
  }
}

internal fun File.deleteRelevantChildren() {
  for (fi in this.listFiles()) {
    if (!fi.isDirectory && (fi.name.endsWith(".png") ||
            fi.name.endsWith(".xml") ||
            fi.name.endsWith(".json"))
    ) {
      if (fi.delete()) {
        "Deleted: ${fi.absolutePath}".log("DeleteRelevantChildren")
      }
    }
  }
}

internal fun File.toUri(): Uri {
  return Uri.fromFile(this)
}
