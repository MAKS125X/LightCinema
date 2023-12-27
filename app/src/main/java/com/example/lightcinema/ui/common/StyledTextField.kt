package com.example.lightcinema.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.lightcinema.ui.theme.LightCinemaTheme

@Preview(showBackground = true, device = Devices.PIXEL_2_XL)
@Composable
fun AuthTextField(
    modifier: Modifier = Modifier,
    value: String = "Aboba228",
    onValueChange: (String) -> Unit = { },
    labelText: String = "Имя пользователя",
    keyboardType: KeyboardType = KeyboardType.Text,
) {
    LightCinemaTheme {
        TextField(
            value = value,
            onValueChange = { onValueChange(it) },
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            colors = textDefaultColors(),
            visualTransformation = if (keyboardType == KeyboardType.Password)
                PasswordVisualTransformation()
            else VisualTransformation.None,
            shape = RoundedCornerShape(16.dp),
            singleLine = true,
            label = {
                Text(
                    text = labelText,
                    color = MaterialTheme.colorScheme.onTertiaryContainer,
                    modifier = Modifier.background(Color.Transparent)
                )
            },
            modifier = modifier
                .padding(10.dp, 0.dp, 10.dp, 0.dp)
                .border(1.dp, MaterialTheme.colorScheme.onBackground, RoundedCornerShape(16.dp)),
        )
    }
}

@Preview(showBackground = true, device = Devices.PIXEL_2_XL)
@Composable
fun StyledTextField(
    modifier: Modifier = Modifier,
    value: String = "Drive",
    onValueChange: (String) -> Unit = { },
    labelText: String = "Название фильма",
    trailingIcon: @Composable (() -> Unit)? = null,
    readOnly: Boolean = false,
    singleLine: Boolean = true,
    minLines: Int = if (singleLine) 1 else Int.MAX_VALUE,
    maxLines: Int = 3,
) {
    LightCinemaTheme {
        TextField(
            value = value,
            onValueChange = { onValueChange(it) },
            trailingIcon = trailingIcon,
            colors = textDefaultColors(),
            shape = RoundedCornerShape(16.dp),
            singleLine = singleLine,
            readOnly = readOnly,
            label = {
                Text(
                    text = labelText,
                    color = MaterialTheme.colorScheme.onTertiaryContainer,
                    modifier = Modifier.background(Color.Transparent)
                )
            },
            minLines = minLines,
            modifier = modifier
                .padding(10.dp, 0.dp, 10.dp, 0.dp)
                .border(1.dp, MaterialTheme.colorScheme.onBackground, RoundedCornerShape(16.dp)),
        )
    }
}

@Composable
fun textDefaultColors(): TextFieldColors = TextFieldDefaults.colors(
    focusedTextColor = MaterialTheme.colorScheme.onBackground,
    unfocusedTextColor = MaterialTheme.colorScheme.onBackground,
    disabledTextColor = MaterialTheme.colorScheme.onBackground,
    errorTextColor = MaterialTheme.colorScheme.onBackground,
    focusedContainerColor = MaterialTheme.colorScheme.background,
    unfocusedContainerColor = MaterialTheme.colorScheme.background,
    disabledContainerColor = MaterialTheme.colorScheme.background,
    errorContainerColor = MaterialTheme.colorScheme.background,
    focusedSupportingTextColor = Color.Transparent,
    unfocusedSupportingTextColor = Color.Transparent,
    disabledSupportingTextColor = Color.Transparent,
    errorSupportingTextColor = Color.Transparent,
//                focusedBorderColor = MaterialTheme.colorScheme.onBackground,
//                unfocusedBorderColor = MaterialTheme.colorScheme.onBackground,
//                disabledBorderColor = MaterialTheme.colorScheme.onBackground,
//                errorBorderColor = MaterialTheme.colorScheme.onBackground,
    focusedLabelColor = MaterialTheme.colorScheme.onBackground,
    unfocusedLabelColor = MaterialTheme.colorScheme.onBackground,
    disabledLabelColor = MaterialTheme.colorScheme.onBackground,
    errorLabelColor = MaterialTheme.colorScheme.onBackground,
    focusedIndicatorColor = Color.Transparent,
    unfocusedIndicatorColor = Color.Transparent,
    disabledIndicatorColor = Color.Transparent,
    errorIndicatorColor = Color.Transparent,
)