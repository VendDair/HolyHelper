package com.venddair.holyhelper

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.documentfile.provider.DocumentFile
import java.io.File
import java.io.FileOutputStream

object FilePicker {
    private var callback: ((String) -> Unit)? = null
    private lateinit var filePickerLauncher: ActivityResultLauncher<Intent>

    fun init(activity: ComponentActivity) {
        filePickerLauncher = activity.registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                result.data?.data?.let { uri ->
                    getFilePath(activity, uri)?.let { path ->
                        /*if (path.endsWith(".img", true)) {
                            callback?.invoke(path)
                        }*/
                        callback?.invoke(path)
                    }
                }
            }
            callback = null
        }
    }


    fun pickFile(callback: (path: String) -> Unit) {
        this.callback = callback
        val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
            type = "*/*"
            addCategory(Intent.CATEGORY_OPENABLE)
            putExtra(Intent.EXTRA_TITLE, "Select uefi.img")
        }
        filePickerLauncher.launch(intent)
    }

    private fun getFilePath(context: Context, uri: Uri): String? {
        val documentFile = DocumentFile.fromSingleUri(context, uri)
        val fileName = documentFile?.name ?: return null

        //if (!fileName.endsWith(".img", true)) return null

        return try {
            context.contentResolver.openInputStream(uri)?.use { inputStream ->
                val outputFile = File(context.cacheDir, fileName)
                FileOutputStream(outputFile).use { output ->
                    inputStream.copyTo(output)
                }
                outputFile.absolutePath
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}