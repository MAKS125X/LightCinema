package com.example.lightcinema.ui.screens.visitor.about

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.lightcinema.ui.theme.LightCinemaTheme


@Composable
fun AboutProgramScreen() {
    Column(verticalArrangement = Arrangement.SpaceAround, modifier = Modifier.fillMaxSize().padding(horizontal = 20.dp, vertical = 10.dp)) {
        Text(
            text = "Самарский университет\n" +
                    "Кафедра программных систем\n" +
                    "Курсовой проект по дисциплине Программная инженерия",
            textAlign = TextAlign.Center, fontSize = 16.sp
        )
        Text(
            text = "Тема проекта: \n“Автоматизированная система бронирования билетов в кинотеатре”",
            textAlign = TextAlign.Center, fontSize = 20.sp
        )
        Text(
            text = "Разработчики студенты группы 6413-020302D\n" +
                    "Валиуллин Валерий Витальевич\n" +
                    "Воропаев Алексей Игоревич\n" +
                    "Чеплакова Елизавета Юрьевна\n" +
                    "Щёлоков Максим Игоревич",
            textAlign = TextAlign.Center,
            fontSize = 16.sp
        )
    }
}

@Preview(showBackground = true)
@Composable
fun AboutProgramScreenPreview() {
    LightCinemaTheme {
        AboutProgramScreen()
    }
}