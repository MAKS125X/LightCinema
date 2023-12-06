package com.example.lightcinema.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SessionInfoButton(boxText: String, additionInfo: String, onClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(4.dp, 4.dp)
    ) {
        Box(contentAlignment = Alignment.Center, modifier = Modifier
            .clickable {
                onClick()
            }
            .background(
                color = MaterialTheme.colorScheme.primary, shape = RoundedCornerShape(16.dp)
            )
            .requiredWidth(54.dp)) {
            Text(
                text = boxText,
                maxLines = 1,
                color = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.padding(4.dp, 4.dp),
                fontSize = 12.sp
            )
        }
        if (additionInfo != "") {
            Text(
                text = "${additionInfo}₽",
                fontSize = 12.sp,
            )
        }

    }
}