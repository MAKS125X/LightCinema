package com.example.lightcinema.ui.screens.authorization

import android.util.Log
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
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.lightcinema.data.auth.models.User
import com.example.lightcinema.data.common.ApiResponse
import com.example.lightcinema.ui.common.AuthTextField
import com.example.lightcinema.ui.common.LoadIndicator
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun AuthScreen(
    authViewModel: AuthViewModel = viewModel(factory = AuthViewModel.Factory),
    tokenViewModel: TokenViewModel = viewModel(factory = TokenViewModel.Factory),
    onSuccess: () -> Unit,
) {
    val pagerState = rememberPagerState {
        2
    }

    val nickname by authViewModel.nickname.collectAsState()

    val registerPassword by authViewModel.registerPassword.collectAsState()

    val registerRepeatPassword by authViewModel.registerRepeatPassword.collectAsState()

    val loginPassword by authViewModel.loginPassword.collectAsState()

    val token by tokenViewModel.token.observeAsState()


    if (token != null) {
        onSuccess()
    }


    val userResult by authViewModel.userResult.collectAsState(null)

    val context = LocalContext.current

//    Log.d("asd", "${userResult.toString()}\n${token}")

    when (val response = userResult) {
        is ApiResponse.Failure -> {
            Toast.makeText(context, response.errorMessage, Toast.LENGTH_LONG).show()
            Log.d("asd", "${userResult.toString()}\n${token}")
        }

        ApiResponse.Loading -> {}
        is ApiResponse.Success -> {
            tokenViewModel.saveToken(response.data)
            onSuccess()
        }

        else -> {}
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {

//        onSuccess()

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp, 0.dp, 20.dp, 0.dp)
                .border(
                    BorderStroke(1.dp, MaterialTheme.colorScheme.onBackground),
                    RoundedCornerShape(16.dp, 16.dp, 16.dp, 16.dp)
                )
        ) {

            Tabs(pagerState = pagerState)
            TabsContent(
                pagerState = pagerState,
                nickname = nickname,
                onEmailChanged = { authViewModel.setEmail(it) },
                loginPassword = loginPassword,
                onLoginPasswordChanged = { authViewModel.setLoginPassword(it) },
                registrationPassword = registerPassword,
                onRegistrationPasswordChanged = { authViewModel.setRegisterPassword(it) },
                repeatPassword = registerRepeatPassword,
                onRepeatPasswordChanged = { authViewModel.setRegisterRepeatPassword(it) },
                onRegistrationClick = {
                    if (nickname == "") {
                        Toast.makeText(context, "Введите имя пользователя", Toast.LENGTH_LONG)
                            .show()
                    }
                    if (registerPassword == "" || registerRepeatPassword == "") {
                        Toast.makeText(context, "Введите пароль reg", Toast.LENGTH_LONG)
                            .show()
                    } else if (registerPassword != registerRepeatPassword) {
                        Toast.makeText(context, "Введённые пароли не совпадают", Toast.LENGTH_LONG)
                            .show()
                    } else {
                        authViewModel.register()
                    }

                },
                onLoginClick = {
                    if (nickname == "") {
                        Toast.makeText(context, "Введите имя пользователя", Toast.LENGTH_LONG)
                            .show()
                    }
                    if (loginPassword == "") {
                        Toast.makeText(context, "Введите пароль login", Toast.LENGTH_LONG)
                            .show()
                    } else {
                        authViewModel.login()
                    }
                },
                apiResponse = userResult
            )
        }
    }
}

@Composable
fun RegistrationTab(
    email: String,
    onNicknameChanged: (String) -> Unit,
    password: String,
    onPasswordChanged: (String) -> Unit,
    repeatPassword: String,
    onRepeatPasswordChanged: (String) -> Unit,
    onRegistrationClick: () -> Unit,
    apiResponse: ApiResponse<User>?
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
                onValueChange = { onNicknameChanged(it) },
                labelText = "Имя пользователя",
                keyboardType = KeyboardType.Text,
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
            if (apiResponse is ApiResponse.Loading) {
                LoadIndicator()
            }
        }
    }
}

@Composable
fun LoginTab(
    email: String,
    onNicknameChanged: (String) -> Unit,
    loginPassword: String,
    onLoginPassword: (String) -> Unit,
    onLoginClicked: () -> Unit,
    apiResponse: ApiResponse<User>?
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
                onValueChange = { onNicknameChanged(it) },
                labelText = "Имя пользователя",
                keyboardType = KeyboardType.Text,
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
            if (apiResponse is ApiResponse.Loading) {
                LoadIndicator()
            }
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
        val focusManager = LocalFocusManager.current
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

                    focusManager.clearFocus()
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
    nickname: String,
    onEmailChanged: (String) -> Unit,
    loginPassword: String,
    onLoginPasswordChanged: (String) -> Unit,
    registrationPassword: String,
    onRegistrationPasswordChanged: (String) -> Unit,
    repeatPassword: String,
    onRepeatPasswordChanged: (String) -> Unit,
    onRegistrationClick: () -> Unit,
    onLoginClick: () -> Unit,
    apiResponse: ApiResponse<User>?
) {
    val color = MaterialTheme.colorScheme.surfaceTint
    if (pagerState.currentPage == 0) {
        RoundedCornerShape(25.dp, 25.dp, 0.dp, 0.dp)
    } else {
        RoundedCornerShape(25.dp, 25.dp, 0.dp, 0.dp)
    }
    HorizontalPager(
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
            0 -> {
                RegistrationTab(
                    nickname,
                    { onEmailChanged(it) },
                    registrationPassword,
                    { onRegistrationPasswordChanged(it) },
                    repeatPassword,
                    { onRepeatPasswordChanged(it) },
                    { onRegistrationClick() },
                    apiResponse
                )
            }

            1 -> LoginTab(
                nickname,
                { onEmailChanged(it) },
                loginPassword,
                { onLoginPasswordChanged(it) },
                { onLoginClick() },
                apiResponse
            )

        }
    }
}

