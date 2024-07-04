package com.soujava.mydoctor.presenter.screens.commons

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.sp
import com.soujava.mydoctor.presenter.ui.theme.AppFont
import com.soujava.mydoctor.presenter.ui.theme.black
import com.soujava.mydoctor.presenter.ui.theme.lightGray


enum class Types{
    BUTTON, TEXT, TITLE, FIELD, SUBTITLE, SMALL, PLACEHOLDER, REGULAR,VERY_SMALL
}


@Composable
fun AppText(types: Types, color : Color = black, text: String, modifier:Modifier = Modifier) {
    when (types) {
        Types.BUTTON ->DoText( text, color, 17, AppFont.regular, modifier = modifier)
        Types.TEXT -> DoText( text, color, 14, AppFont.regular, modifier = modifier)
        Types.REGULAR -> DoText( text, color, 18, AppFont.bold, modifier = modifier)
        Types.TITLE -> DoText( text, color, 30, AppFont.bold, modifier = modifier)
        Types.SUBTITLE -> DoText( text, color, 14, AppFont.light, modifier = modifier)
        Types.FIELD -> DoText( text, color, 14, AppFont.regular, modifier = modifier)
        Types.SMALL -> DoText( text, color, 12, AppFont.light, modifier = modifier)
        Types.VERY_SMALL -> DoText( text, color, 9, AppFont.light, modifier = modifier)
        Types.PLACEHOLDER -> DoText( text, lightGray, 12, AppFont.light, modifier = modifier)
    }

}

@Composable
fun DoText(text: String, color : Color = black, sp: Int, font: FontFamily,modifier:Modifier ) {
    return  Text(text, fontFamily = font, fontSize = sp.sp, color = color, modifier = modifier)
}

