package com.example.lightcinema

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.lightcinema.ui.screens.filminfo.MovieInfoScreen
import com.example.lightcinema.ui.theme.LightCinemaTheme

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {

        installSplashScreen()


        super.onCreate(savedInstanceState)

        setContent {
            LightCinemaTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
//                    PosterScreen()
                    MovieInfoScreen()


//                    CinemaScreen()
//                    AuthScreen()
                    val context = LocalContext.current
//                    val coffeeDrinks: Array<Country> =
//                        arrayOf(
//                            Country("Americano"),
//                            Country("Cappuccino"),
//                            Country("Espresso"),
//                            Country("Latte"),
//                            Country("Mocha")
//                        )
//                        arrayOf()
//                    val coffeeDrinks = remember {
//                        mutableStateListOf<Country>(
//                            Country("Americano"),
//                            Country("Cappuccino"),
//                            Country("Espresso"),
//                            Country("Latte"),
//                            Country("Mocha")
//                        )
//                    }
//
//                    Log.d(
//                        "Aboba",
//                        coffeeDrinks.filter { it.isSelected }
//                            .joinToString(separator = ", ") { it.name })
//
//                    var expanded by remember { mutableStateOf(false) }
//
//                    var selectedText by remember { mutableStateOf("Страны") }
//
//                    Box(
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .padding(32.dp)
//                    ) {
//                        ExposedDropdownMenuBox(
//                            expanded = expanded,
//                            onExpandedChange = {
//                                expanded = !expanded
//                            },
//                        ) {
//                            TextField(
//                                value = when (val chosenStrings =
//                                    coffeeDrinks.filter { it.isSelected }
//                                        .joinToString(separator = ", ") { it.name }) {
//                                    "" -> "Страны"
//                                    else -> chosenStrings
//                                },
//                                onValueChange = {},
//                                readOnly = true,
//                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
//                                modifier = Modifier.menuAnchor(),
//                                maxLines = 1
//                            )
//
//                            ExposedDropdownMenu(
//                                expanded = expanded,
//                                onDismissRequest = { expanded = false }
//                            ) {
//                                coffeeDrinks.forEachIndexed { index, country ->
//                                    Row {
//                                        Checkbox(
//                                            checked = country.isSelected,
//                                            onCheckedChange = {
//                                                coffeeDrinks[index] = coffeeDrinks[index].copy(isSelected = it)
////                                                country.isSelected = !country.isSelected
//                                            })
//                                        DropdownMenuItem(
//                                            text = { Text(text = country.name) },
//                                            onClick = {
////                                                country.isSelected = !country.isSelected
//                                            },
//                                        )
//                                    }
//
//                                }
//                            }
//
//                        }
//                    }


                }
            }

        }
    }

    fun setSpecialtySelectedAtIndex(index: Int, isSelected: Boolean) {

    }

    data class Country(val name: String, var isSelected: Boolean = false)
}


