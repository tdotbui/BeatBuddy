package edu.temple.beatbuddy.user_auth.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import edu.temple.beatbuddy.R
import edu.temple.beatbuddy.user_auth.model.AuthResult.*
import edu.temple.beatbuddy.user_auth.repository.SignInViewModel
import edu.temple.beatbuddy.utils.Helpers

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SignInScreen(
    signInViewModel: SignInViewModel = hiltViewModel(),
    goToSignUpScreen: () -> Unit,
    onSignIn: () -> Unit
) {
    val context = LocalContext.current
    val keyboard = LocalSoftwareKeyboardController.current

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Box(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 28.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Welcome!",
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp
                )

                Text(
                    text = "Sign In Your Account",
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Normal,
                    fontSize = 18.sp
                )
            }

            OutlinedTextField(
                value = email,
                onValueChange = {
                    email = it
                },
                label = { Text("Email") },
                leadingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.email),
                        contentDescription = ""
                    )
                }
            )

            OutlinedTextField(
                value = password,
                onValueChange = {
                    password = it
                },
                label = { Text("Password") },
                leadingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.password),
                        contentDescription = ""
                    )
                },
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    keyboard?.hide()
                    signInViewModel.signInWithEmailAndPassword(email, password)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(48.dp)
                    .padding(horizontal = 32.dp, vertical = 16.dp)
                    .clip(shape = RoundedCornerShape(50.dp)),
                contentPadding = PaddingValues(),
                colors = ButtonDefaults.buttonColors(Color.Transparent)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(48.dp)
                        .background(
                            brush = Brush.horizontalGradient(
                                listOf(
                                    MaterialTheme.colorScheme.primary,
                                    MaterialTheme.colorScheme.tertiary
                                )
                            ),
                            shape = RoundedCornerShape(50.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Sign In",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }

            val annotatedString = buildAnnotatedString {
                append("Don't have an account? ")
                withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.primary)) {
                    pushStringAnnotation(tag = "Register", annotation = "Register")
                    append("Register")
                }
            }

            ClickableText(
                text = annotatedString,
                onClick = { goToSignUpScreen() }
            )
        }

        when (signInViewModel.signInResponse) {
            is Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.White.copy(alpha = 0.3f))
                        .wrapContentSize(Alignment.Center)
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
            is Success -> {
                onSignIn()
            }
            is Error -> {
                Helpers.showMessage(context, (signInViewModel.signInResponse as Error).exception.localizedMessage)
                signInViewModel.resetResponse()
                email = ""
                password = ""
            }
            else -> {}
        }
    }
}

//@Preview(showBackground = true)
//@Composable
//fun PreviewSignIn() {
//    SignInScreen({}, {})
//}