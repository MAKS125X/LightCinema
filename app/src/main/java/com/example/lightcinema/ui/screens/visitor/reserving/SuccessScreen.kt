package com.example.lightcinema.ui.screens.visitor.reserving

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.lightcinema.R
import kotlinx.coroutines.delay

@Composable
fun SuccessScreen(
    viewModel: SuccessViewModel = viewModel(),
    onFinish: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Image(
            ImageVector.vectorResource(R.drawable.split_screen_background),
            contentDescription = "Заставка",
            contentScale = ContentScale.FillBounds
        )
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxSize()
                .padding(25.dp),
        ) {
            Text(
                text = viewModel.successString,
                color = MaterialTheme.colorScheme.onPrimary,
                fontSize = 26.sp,
                textAlign = TextAlign.Center

            )
            Text(
                text = "Брони доступны в личном кабинете",
                color = MaterialTheme.colorScheme.onPrimary,
                fontSize = 20.sp,
                textAlign = TextAlign.Center
            )
        }

    }
    LaunchedEffect(key1 = Unit) {
        delay(5000)
        onFinish()
    }
}