package com.soujava.mydoctor.core

import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import com.google.gson.Gson
import com.google.zxing.integration.android.IntentIntegrator
import com.soujava.mydoctor.domain.models.JsonTriage
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.security.SecureRandom
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Locale



import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import android.util.Log
import androidx.camera.core.ImageCapture.OnImageCapturedCallback
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.view.LifecycleCameraController
import androidx.compose.ui.unit.sp
import java.lang.Exception
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.core.content.ContextCompat




import androidx.camera.core.ImageCapture
import com.google.gson.reflect.TypeToken
import com.soujava.mydoctor.domain.models.Data
import com.soujava.mydoctor.domain.models.History
import com.soujava.mydoctor.domain.models.MedicalPrescription
import java.io.File
import java.util.concurrent.Executor
import java.text.SimpleDateFormat
import java.util.Date

val gson = Gson()

fun takePhotoAndSaveToFile(
    controller: LifecycleCameraController,context: Context,
    onPhotoSaved: (File) -> Unit,
    onError: (ImageCaptureException) -> Unit
) {
    // Crie um nome de arquivo único com base na data e hora
    val name = SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-SSS", Locale.getDefault())
        .format(System.currentTimeMillis())
    val photoFile = File(context.filesDir, "${name}.jpg")

    // Crie as opções de saída do arquivo
    val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

    // Capture a foto e salve no arquivo
    controller.takePicture(
        outputOptions,
        ContextCompat.getMainExecutor(context),
        object : ImageCapture.OnImageSavedCallback {
            override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                onPhotoSaved(photoFile) // Chame o callback com o arquivo salvo
            }

            override fun onError(exception: ImageCaptureException) {
                onError(exception) // Chame o callback de erro
            }
        }
    )
}

fun takePhoto(
    controller: LifecycleCameraController,
    context: Context,
    onPhotoTaken: (Bitmap) -> Unit
) {
    controller.takePicture(
        ContextCompat.getMainExecutor(context),
        object : OnImageCapturedCallback() {
            override fun onCaptureSuccess(image: ImageProxy) {
                super.onCaptureSuccess(image)

                val matrix = Matrix().apply {
                    postRotate(image.imageInfo.rotationDegrees.toFloat())
                }
                val rotatedBitmap = Bitmap.createBitmap(
                    image.toBitmap(),
                    0,
                    0,
                    image.width,
                    image.height,
                    matrix,
                    true
                )

                onPhotoTaken(rotatedBitmap)
            }

            override fun onError(exception: ImageCaptureException) {
                super.onError(exception)
                Log.e("Camera", "Couldn't take photo: ", exception)
            }
        }
    )
}

fun imagePathToBitmap(imagePath: String): Bitmap? {
    return BitmapFactory.decodeFile(imagePath)
}

fun getBitmapFromUri(context: Context, imageUri: Uri): Bitmap? {
    return try {val inputStream = context.contentResolver.openInputStream(imageUri)
        BitmapFactory.decodeStream(inputStream)
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}


 fun startQRScanner(context: Activity, qrScanLauncher: ActivityResultLauncher<Intent>) {
    val integrator = IntentIntegrator(context)
    integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE)
    integrator.setPrompt("Scan a QR code")
    integrator.setCameraId(0) // 0 para a câmera traseira, 1 para a câmera frontal
    integrator.setBeepEnabled(true)
    integrator.setBarcodeImageEnabled(true)
    qrScanLauncher.launch(integrator.createScanIntent())
}



 fun copyToClipboard(context: Context, text: String) {
    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clip = ClipData.newPlainText("Copied Text", text)
    clipboard.setPrimaryClip(clip)
}

fun getFormattedDate(): String {
    val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale("pt", "BR"))
    val date = Date()
    val formattedDate = dateFormat.format(date)
    return formattedDate
}

fun getCurrentTime(): String {
    val currentTime = LocalTime.now()
    val formatter = DateTimeFormatter.ofPattern("HH:mm")
    return currentTime.format(formatter)
}

fun generateRandomKey(): String {
    val charPool : List<Char> = ('A'..'Z') + ('a'..'z') + ('0'..'9')
    val random = SecureRandom()

    return (1..10)
        .map { random.nextInt(charPool.size) }
        .map(charPool::get)
        .joinToString("")
}

fun currentTimeFlow(): Flow<String> = flow {
    while (true) {
        emit(getCurrentTime())
        delay(1000)
    }

}

fun parseJson(jsonString: String): JsonTriage {
    return gson.fromJson(jsonString, JsonTriage::class.java)

}
fun parseJsonHistoryData(jsonString: String): Data {
    return gson.fromJson(jsonString, Data::class.java)

}

fun parseJsonMedicalPrescription(jsonString: String): MedicalPrescription {
    return gson.fromJson(jsonString, MedicalPrescription::class.java)
}
fun parseJsonMedicalPrescriptionList(jsonString: String): List<MedicalPrescription> {
    val listType = object : TypeToken<List<MedicalPrescription>>() {}.type
    return gson.fromJson(jsonString, listType)
}

fun isEmailValid(email: String): Boolean {
    val emailRegex = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z|a-z]{2,}\$".toRegex()
    return emailRegex.matches(email)
}
fun isPasswordValid(password: String): Boolean {
    val passwordRegex = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{5,}\$".toRegex()
    return passwordRegex.find(password) != null
}



fun formatText(text: String): AnnotatedString {
    return buildAnnotatedString {
        var currentIndex = 0
        while (currentIndex < text.length) {
            // Check for title (##)
            if (text.startsWith("##", currentIndex)) {
                val endIndex = text.indexOf("\n", currentIndex + 2)
                if (endIndex != -1) {
                    withStyle(style = androidx.compose.ui.text.SpanStyle(fontWeight = FontWeight.Bold, fontSize =18.sp)) {
                        append(text.substring(currentIndex + 2, endIndex).trim())
                    }
                    currentIndex = endIndex + 1
                    append("\n") // Add a newline after the title
                    continue
                }}

            // Check for subtitle (**)
            if (text.startsWith("**", currentIndex)) {
                val endIndex = text.indexOf("**", currentIndex + 2)
                if (endIndex != -1) {
                    withStyle(style = androidx.compose.ui.text.SpanStyle(fontWeight = FontWeight.Bold)) {
                        append(text.substring(currentIndex + 2, endIndex).trim())
                    }
                    currentIndex = endIndex + 2
                    continue
                }
            }

            // Check for list item (*)
            if (text.startsWith("*", currentIndex)) {
                val endIndex = text.indexOf("\n", currentIndex + 1)
                if (endIndex != -1) {
                    withStyle(style = androidx.compose.ui.text.SpanStyle(fontSize = 16.sp)) {
                        append("• ${text.substring(currentIndex + 1, endIndex).trim()}")
                    }
                    currentIndex = endIndex + 1
                    append("\n") // Add a newline after the list item
                    continue
                }
            }

            // Append regular text
            val nextSpecialCharIndex = text.indexOfAny(charArrayOf('#', '*', '\n'), currentIndex)
            val endIndex = if (nextSpecialCharIndex != -1) nextSpecialCharIndex else text.length
            append(text.substring(currentIndex, endIndex))
            currentIndex = endIndex
        }
    }
}