package com.soujava.mydoctor.presenter.screens.commons

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp


@Composable
fun TextArea(
    modifier:Modifier = Modifier,
    input: MutableState<String>, label: String, placeholder: String,
    hasError: Boolean = false,
    imeAction: ImeAction = ImeAction.Done,
    onDone: () -> Unit = {},
) {
    OutlinedTextField(
        value = input.value,
        isError = hasError,
        modifier = modifier
            .padding(top = 8.dp)
            .height(190.dp)
            .fillMaxWidth()
            .background(Color.White, RoundedCornerShape(16.dp))
            .border(1.dp, Color.Black, RoundedCornerShape(16.dp))
            .padding(12.dp),
        onValueChange = {
            input.value = it
        },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text,
            imeAction =imeAction
        ),
        keyboardActions = KeyboardActions(
            onDone = { onDone() },
        ),
        label = { AppText(text = label, types = Types.FIELD) },
        placeholder = { AppText(text = placeholder, types = Types.PLACEHOLDER) },
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color.Transparent,
            unfocusedBorderColor = Color.Transparent,
            errorBorderColor = Color.Transparent
        )
    )
}

@Composable
fun TextField(
    modifier: Modifier = Modifier,
    input: MutableState<String>,
    label: String,
    placeholder: String,
    imeAction: ImeAction = ImeAction.Done,
    onDone: () -> Unit = {}
) {
    OutlinedTextField(
        modifier = modifier
            .padding(start = 20.dp, end = 20.dp)
            .fillMaxWidth(),
        value = input.value,
        maxLines = 1,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text,
            imeAction = imeAction
        ),
        keyboardActions = KeyboardActions(
            onDone = { onDone() },
        ),
        label = { AppText(text = label, types = Types.FIELD) },
        placeholder = { AppText(text = placeholder, types = Types.PLACEHOLDER) },
        onValueChange = {
            input.value = it
        })
}

@Composable
fun CepField(
    modifier: Modifier = Modifier,
    input: MutableState<String>,
    label: String,
    placeholder: String,
    onSearch: () -> Unit,
    onValueChange: (String) -> Unit
) {
    OutlinedTextField(
        modifier = modifier
            .padding(start = 20.dp, end = 20.dp)
            .fillMaxWidth(),
        value = input.value,
        maxLines = 1,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Search
        ),
        keyboardActions = KeyboardActions(
            onSearch = { onSearch() },
        ),
        label = { AppText(text = label, types = Types.FIELD) },
        placeholder = { AppText(text = placeholder, types = Types.PLACEHOLDER) },
        onValueChange = onValueChange
        )
}

@Composable
fun NumberField(
    modifier: Modifier = Modifier,
    input: MutableState<String>,
    label: String,
    placeholder: String,
    imeAction: ImeAction = ImeAction.Done,
    onDone: () -> Unit
) {
    OutlinedTextField(
        modifier = modifier
            .padding(start = 20.dp, end = 20.dp)
            .fillMaxWidth(),
        value = input.value,
        maxLines = 1,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number,
            imeAction = imeAction
        ),
        keyboardActions = KeyboardActions(
            onDone = { onDone() },
        ),
        label = { AppText(text = label, types = Types.FIELD) },
        placeholder = { AppText(text = placeholder, types = Types.PLACEHOLDER) },
        onValueChange = {
                input.value = it
        })
}


@Composable
fun Email(email: MutableState<String>) {
    OutlinedTextField(
        modifier = Modifier
            .padding(start = 20.dp, end = 20.dp)
            .fillMaxWidth(),
        value = email.value,
        maxLines = 1,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Email,
            imeAction = ImeAction.Next
        ),

        label = { AppText(text = "Digite seu email", types = Types.FIELD) },
        placeholder = { AppText(text = "Digite seu email", types = Types.PLACEHOLDER) },
        onValueChange = {
            email.value = it
        })
}


@Composable
fun Password(
    password: MutableState<String>,
    imeAction: ImeAction = ImeAction.Done,
) {

    val showPassword = remember { mutableStateOf(false) }

    OutlinedTextField(
        modifier = Modifier
            .padding(start = 20.dp, end = 20.dp)
            .fillMaxWidth(),
        value = password.value,
        onValueChange = { password.value = it },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password,
            imeAction = imeAction
        ),

        singleLine = true,
        maxLines = 1,
        label = { AppText(text = "Digite sua senha", types = Types.FIELD) },
        placeholder = { AppText(text = "Digite sua senha", types = Types.PLACEHOLDER) },
        visualTransformation = if (showPassword.value) VisualTransformation.None else PasswordVisualTransformation(),
        trailingIcon = {
            PasswordToggleIcon(
                onToggleClick = { showPassword.value = !showPassword.value },
                isVisible = showPassword.value
            )
        }
    )
}


@Composable
fun PasswordToggleIcon(onToggleClick: () -> Unit, isVisible: Boolean) {
    val icon = if (isVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility
    val description = if (isVisible) "Esconder senha" else "Mostrar senha"

    IconButton(
        onClick = onToggleClick,
        modifier = Modifier.padding(end = 12.dp)
    ) {
        Icon(icon, contentDescription = description)
    }
}