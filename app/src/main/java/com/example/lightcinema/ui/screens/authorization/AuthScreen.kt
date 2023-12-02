package com.example.lightcinema.ui.screens.authorization

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.lightcinema.ui.theme.LightCinemaTheme
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun AuthScreen(
    authViewModel: AuthViewModel = viewModel(factory = AuthViewModel.Factory),
    onSuccessToken: () -> Unit = {}
) {
    LightCinemaTheme {
        val pagerState = rememberPagerState(0)

        val email by authViewModel.email.collectAsState()

        val registerPassword by authViewModel.registerPassword.collectAsState()

        val registerRepeatPassword by authViewModel.registerRepeatPassword.collectAsState()

        val loginPassword by authViewModel.loginPassword.collectAsState()

        val token by authViewModel.token.collectAsState()
        if (token != null) {
            onSuccessToken()
        }
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp, 0.dp, 20.dp, 0.dp)
                    .border(
                        BorderStroke(1.dp, MaterialTheme.colorScheme.onBackground),
                        RoundedCornerShape(16.dp, 16.dp, 16.dp, 16.dp)
                    )
            ) {
                val context = LocalContext.current
                Tabs(pagerState = pagerState)
                TabsContent(
                    pagerState = pagerState,
                    email,
                    { authViewModel.setEmail(it) },
                    registerPassword,
                    { authViewModel.setRegisterPassword(it) },
                    registerRepeatPassword,
                    { authViewModel.setRegisterRepeatPassword(it) },
                    loginPassword,
                    { authViewModel.setLoginPassword(it) },
                    {
                        authViewModel.register()
                        Toast.makeText(context, "Welcome to the club buddy", Toast.LENGTH_LONG)
                            .show()
                    },
                    { authViewModel.login() }
                )
            }
        }
    }
}

@Composable
fun RegistrationTab(
    email: String,
    onEmailChanged: (String) -> Unit,
    password: String,
    onPasswordChanged: (String) -> Unit,
    repeatPassword: String,
    onRepeatPasswordChanged: (String) -> Unit,
    onRegistrationClick: () -> Unit
) {
    Box(
        contentAlignment = Alignment.TopCenter,
        modifier = Modifier
            .height(400.dp)
            .background(
                MaterialTheme.colorScheme.surfaceTint,
                RoundedCornerShape(0.dp, 0.dp, 16.dp, 16.dp)
            )
            .border(
                BorderStroke(1.dp, MaterialTheme.colorScheme.onBackground),
                RoundedCornerShape(0.dp, 0.dp, 16.dp, 16.dp)
            )
    ) {
        val textFieldWidth = 400.dp
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Spacer(modifier = Modifier.height(20.dp))
            AuthTextField(
                value = email,
                onValueChange = { onEmailChanged(it) },
                labelText = "Почта",
                keyboardType = KeyboardType.Email,
                width = textFieldWidth
            )
            Spacer(modifier = Modifier.height(20.dp))
            AuthTextField(
                value = password,
                onValueChange = { onPasswordChanged(it) },
                labelText = "Пароль",
                keyboardType = KeyboardType.Password,
                width = textFieldWidth
            )
            Spacer(modifier = Modifier.height(20.dp))
            AuthTextField(
                value = repeatPassword,
                onValueChange = { onRepeatPasswordChanged(it) },
                labelText = "Подтвердите пароль",
                keyboardType = KeyboardType.Password,
                width = textFieldWidth
            )
            Spacer(modifier = Modifier.height(20.dp))
            Button(
                onClick = { onRegistrationClick() },
                shape = RoundedCornerShape(16.dp, 16.dp, 16.dp, 16.dp),
                colors = ButtonDefaults.buttonColors(
                    MaterialTheme.colorScheme.background,
                    MaterialTheme.colorScheme.onBackground
                ),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.onBackground)
            ) {
                Text(
                    text = "Зарегистрироваться",
                    textAlign = TextAlign.Center,
                )
            }
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Composable
fun LoginTab(
    email: String,
    onEmailChanged: (String) -> Unit,
    loginPassword: String,
    onLoginPassword: (String) -> Unit,
    onLoginClicked: () -> Unit
) {
    Box(
        contentAlignment = Alignment.TopCenter,
        modifier = Modifier
            .height(400.dp)
            .background(
                MaterialTheme.colorScheme.surfaceTint,
                RoundedCornerShape(0.dp, 0.dp, 16.dp, 16.dp)
            )
            .border(
                BorderStroke(1.dp, MaterialTheme.colorScheme.onBackground),
                RoundedCornerShape(0.dp, 0.dp, 16.dp, 16.dp)
            )
    ) {
        val textFieldWidth = 400.dp
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Spacer(modifier = Modifier.height(20.dp))
            AuthTextField(
                value = email,
                onValueChange = { onEmailChanged(it) },
                labelText = "Почта",
                keyboardType = KeyboardType.Email,
                width = textFieldWidth
            )
            Spacer(modifier = Modifier.height(20.dp))
            AuthTextField(
                value = loginPassword,
                onValueChange = { onLoginPassword(it) },
                labelText = "Пароль",
                keyboardType = KeyboardType.Password,
                width = textFieldWidth
            )
            Spacer(modifier = Modifier.height(20.dp))
            Button(
                onClick = { onLoginClicked() },
                shape = RoundedCornerShape(16.dp, 16.dp, 16.dp, 16.dp),
                colors = ButtonDefaults.buttonColors(
                    MaterialTheme.colorScheme.background,
                    MaterialTheme.colorScheme.onBackground
                ),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.onBackground)
            ) {
                Text(
                    text = "Войти",
                    textAlign = TextAlign.Center,
                )
            }
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Tabs(pagerState: PagerState) {
    val list = listOf(
        "Регистрация",
        "Войти"
    )
    val scope = rememberCoroutineScope()
    val color = MaterialTheme.colorScheme.surfaceTint
    TabRow(
        selectedTabIndex = pagerState.currentPage,
        modifier = Modifier.background(color, RoundedCornerShape(16.dp, 16.dp, 0.dp, 0.dp)),
        indicator = { },
        divider = { }
    ) {
        list.forEachIndexed { index, _ ->
            Tab(
                text = {
                    Text(
                        list[index],
                        color = MaterialTheme.colorScheme.onTertiaryContainer
                    )
                },
                selected = pagerState.currentPage == index,

                onClick = {
                    scope.launch {
                        pagerState.animateScrollToPage(index)
                    }
                },
                modifier = Modifier
                    .background(
                        color = if (pagerState.currentPage == index)
                            MaterialTheme.colorScheme.surfaceTint
                        else MaterialTheme.colorScheme.surface,
                        shape = RoundedCornerShape(16.dp, 16.dp, 0.dp, 0.dp),
                    )
                    .border(
                        1.dp,
                        color = if (pagerState.currentPage == index) {
                            if (pagerState.currentPage == 0) {
                                MaterialTheme.colorScheme.onTertiaryContainer
                            } else {
                                MaterialTheme.colorScheme.onTertiaryContainer
                            }
                        } else {
                            if (pagerState.currentPage == 0) {
                                Color.Transparent
                            } else {
                                Color.Transparent
                            }
                        },
                        if (pagerState.currentPage == index) {
                            if (pagerState.currentPage == 0) {
                                RoundedCornerShape(16.dp, 16.dp, 0.dp, 0.dp)
                            } else {
                                RoundedCornerShape(16.dp, 16.dp, 0.dp, 0.dp)
                            }
                        } else {
                            if (pagerState.currentPage == 0) {
                                RoundedCornerShape(16.dp, 16.dp, 16.dp, 0.dp)
                            } else {
                                RoundedCornerShape(0.dp, 25.dp, 0.dp, 0.dp)
                            }
                        }
                    )
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TabsContent(
    pagerState: PagerState,
    email: String,
    onEmailChanged: (String) -> Unit,
    loginPassword: String,
    onLoginPasswordChanged: (String) -> Unit,
    registrationPassword: String,
    onRegistrationPasswordChanged: (String) -> Unit,
    repeatPassword: String,
    onRepeatPasswordChanged: (String) -> Unit,
    onRegistrationClick: () -> Unit,
    onLoginClick: () -> Unit
) {
    val color = MaterialTheme.colorScheme.surfaceTint
    if (pagerState.currentPage == 0) {
        RoundedCornerShape(25.dp, 25.dp, 0.dp, 0.dp)
    } else {
        RoundedCornerShape(25.dp, 25.dp, 0.dp, 0.dp)
    }
    HorizontalPager(
        pageCount = 2,
        state = pagerState,
        modifier = Modifier
            .drawWithContent {
                drawContent()
                drawLine(
                    start = Offset(
                        x = if (pagerState.currentPage == 0) {
                            0f + 3f
                        } else {
                            size.width / 2 + 3f
                        }, y = 1f
                    ),
                    end = Offset(
                        x = if (pagerState.currentPage == 0) {
                            size.width / 2 - 3f
                        } else {
                            size.width - 3f
                        }, y = 1f
                    ),
                    strokeWidth = 10f,
                    color = color
                )
            }
    ) { page ->
        when (page) {
            0 -> RegistrationTab(
                email,
                { onEmailChanged(it) },
                registrationPassword,
                { onRegistrationPasswordChanged(it) },
                repeatPassword,
                { onRepeatPasswordChanged(it) },
                { onRegistrationClick() }
            )

            1 -> LoginTab(
                email,
                { onEmailChanged(it) },
                loginPassword,
                { onLoginPasswordChanged(it) },
                { onLoginClick() }
            )
        }
    }
}


@Preview(showBackground = true, device = Devices.PIXEL_2_XL)
@Composable
fun AuthTextField(
    value: String = "",
    onValueChange: (String) -> Unit = { },
    labelText: String = "Почта",
    keyboardType: KeyboardType = KeyboardType.Text,
    width: Dp = 200.dp,
) {
    LightCinemaTheme {
        OutlinedTextField(
            value = value,
            onValueChange = { onValueChange(it) },
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            colors = OutlinedTextFieldDefaults.colors(
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
                focusedBorderColor = MaterialTheme.colorScheme.onBackground,
                unfocusedBorderColor = MaterialTheme.colorScheme.onBackground,
                disabledBorderColor = MaterialTheme.colorScheme.onBackground,
                errorBorderColor = MaterialTheme.colorScheme.onBackground,
                focusedLabelColor = MaterialTheme.colorScheme.onBackground,
                unfocusedLabelColor = MaterialTheme.colorScheme.onBackground,
                disabledLabelColor = MaterialTheme.colorScheme.onBackground,
                errorLabelColor = MaterialTheme.colorScheme.onBackground,
            ),
            visualTransformation = if (keyboardType == KeyboardType.Password)
                PasswordVisualTransformation()
            else VisualTransformation.None,
            shape = RoundedCornerShape(16.dp),
            singleLine = true,
            placeholder = {
                Text(
                    text = labelText,
                    color = MaterialTheme.colorScheme.onTertiaryContainer
                )
            },
            modifier = Modifier
                .width(width)
                .padding(10.dp, 0.dp, 10.dp, 0.dp),
        )
    }
}
