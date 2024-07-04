package com.soujava.mydoctor.core

import android.graphics.Bitmap
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import com.soujava.mydoctor.BuildConfig

object PromptGemini {

    var returnText = ""

    private val generativeModel = GenerativeModel(
        modelName = "gemini-1.5-pro",
        apiKey = BuildConfig.apiKey
    )


    suspend fun generateText(prompt: String) = generativeModel.generateContent(
        content {
            text(prompt)
        }
    )

    suspend fun generateImage(prompt: String, image:Bitmap) = generativeModel.generateContent(
         content {
             text(prompt)
             image(image )
         }
     )

    suspend fun generateImages(prompt: String, paths:List<String>) = generativeModel.generateContent(
        content {
            text(prompt)
            for (path in paths)
                imagePathToBitmap(path)?.let { image(it) }
        }
    )

}