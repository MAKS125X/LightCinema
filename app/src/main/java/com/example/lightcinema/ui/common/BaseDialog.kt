package com.example.lightcinema.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.sharp.Close
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BaseDialog(
    title: String,
    buttonText: String,
    onButtonClick: () -> Unit,
    onDismissRequest: () -> Unit,
    modifier: Modifier,
    content: @Composable () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        modifier = modifier.background(
            MaterialTheme.colorScheme.background,
            RoundedCornerShape(16.dp)
        )
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
        ) {
//            Row(modifier.fillMaxWidth()) {
            Row(
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.padding(bottom = 12.dp)
            ) {
                Box(modifier = Modifier.weight(1f)) {

                }
                Text(
                    text = title,
                    textDecoration = TextDecoration.Underline,
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = 20.sp,
                    modifier = Modifier.padding(top = 12.dp)
                )
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .padding(0.dp),
                    contentAlignment = Alignment.TopEnd,
                ) {
//                    CompositionLocalProvider(LocalMinimumInteractiveComponentEnforcement provides false) {
                    IconButton(
                        onClick = { onDismissRequest() },
                        modifier = Modifier
                            .padding(0.dp)
                            .wrapContentSize()
                            .width(24.dp)
                            .height(24.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Sharp.Close,
                            contentDescription = "Закрыть",
                            tint = MaterialTheme.colorScheme.onBackground,
                            modifier = Modifier
                                .padding(0.dp)
                                .padding(top = 2.dp, end = 2.dp)
                        )
                    }
                }
            }
            content()
            Button(
                onClick = { onButtonClick() },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                ),
            ) {
                Text(text = buttonText, modifier = Modifier.padding(horizontal = 8.dp))
            }
        }
    }

}