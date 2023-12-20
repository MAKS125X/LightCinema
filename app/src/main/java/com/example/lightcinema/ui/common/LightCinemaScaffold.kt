package com.example.lightcinema.ui.common

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.ExitToApp
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.lightcinema.ui.screens.main.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LightCinemaScaffold(
    showTopAppBar: Boolean,
    onProfileClick: () -> Unit,
    onLogoutClick: () -> Unit,
    content: @Composable (PaddingValues) -> Unit
) {
    val mainViewModel: MainViewModel =
        viewModel<MainViewModel>(factory = MainViewModel.Factory)
    Scaffold(topBar = {
        if (showTopAppBar) {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = mainViewModel.getName())
                        IconButton(onClick = {
                            mainViewModel.logout()
                            onLogoutClick()
                        }) {
                            Icon(
                                imageVector = Icons.Outlined.ExitToApp,
                                contentDescription = "Выйти из системы",
                                modifier = Modifier
                                    .size(24.dp)
                                    .padding(0.dp)
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surfaceTint,
                    titleContentColor = MaterialTheme.colorScheme.onTertiaryContainer,
                    navigationIconContentColor = MaterialTheme.colorScheme.onTertiaryContainer,
                    actionIconContentColor = MaterialTheme.colorScheme.onTertiaryContainer
                ),
                navigationIcon = {
                    IconButton(onClick = {
                        onProfileClick()
                    }) {
                        Icon(
                            imageVector = Icons.Outlined.AccountCircle,
                            contentDescription = "Профиль",
                            modifier = Modifier.size(48.dp)
                        )
                    }
                },
                actions = {
                    Column(horizontalAlignment = Alignment.End) {
                        Text(text = "Самара, Космопорт")
                        Text(
                            text = "ул.Пушкина, д.133",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }

                }
            )
        }
    }
    ) {
        content(it)
    }

}