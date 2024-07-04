import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Build
import android.os.Environment
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.WriterException
import com.google.zxing.common.BitMatrix
import com.google.zxing.qrcode.QRCodeWriter
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import java.util.EnumMap


fun generateQRCodeAndSave(activity: Activity, content: String, size: Int): String? {

    Log.e("QRCode", "HERE")
    val folder = File(Environment.getExternalStorageDirectory(), "QRCode")

    if (!folder.exists()) {
        if (!folder.mkdirs()) {
            Log.e("QRCode", "Falha ao criar diretório")
            return null // Retorna nulo se não conseguir criar o diretório
        }
    }

    val file = File(folder, "qr_code.png")
    try {
        val bitmap = generateQRCodeBitmap(content, size)
        saveBitmapAsPNG(bitmap, file)
        Log.e("QRCode", "QR Code salvo como: ${file.absolutePath}")
        return file.absolutePath
    } catch (e: Exception) {
        Log.e("QRCode", "Erro ao gerar e salvar QR Code", e)
        return null // Retorna nulo em caso de erro
    }
}

// Função para gerar um Bitmap do QR Code
 fun generateQRCodeBitmap(content: String, size: Int): Bitmap {
    val hints = EnumMap<EncodeHintType, Any>(EncodeHintType::class.java)
    hints[EncodeHintType.CHARACTER_SET] = "UTF-8"

    val writer = QRCodeWriter()
    val bitMatrix = writer.encode(content, BarcodeFormat.QR_CODE, size, size, hints)
    val width = bitMatrix.width
    val height = bitMatrix.height
    val bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)

    for (x in 0 until width) {
        for (y in 0 until height) {
            bmp.setPixel(x, y, if (bitMatrix[x, y]) Color.BLACK else Color.WHITE)
        }
    }

    return bmp
}

// Função para salvar um Bitmap como arquivo PNG
private fun saveBitmapAsPNG(bitmap: Bitmap, file: File) {
    try {
        val stream: OutputStream = FileOutputStream(file)
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
        stream.flush()
        stream.close()
    } catch (e: Exception) {
        Log.e("QRCode", "Erro ao salvar QR Code como PNG", e)
    }
}