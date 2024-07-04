package com.soujava.mydoctor.presenter.screens.commons

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


enum class SpaceType {
    SMALL, MEDIUM, LARGE
}


@Composable
fun Space(space:Int)= Spacer(modifier = Modifier.height(space.dp))



@Composable
fun AppSpace(spaceType:SpaceType = SpaceType.MEDIUM) {
    when (spaceType) {
        SpaceType.SMALL -> Space(10)
        SpaceType.MEDIUM -> Space(20)
        SpaceType.LARGE -> Space(30)
    }
}