package edu.temple.beatbuddy.screen

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import edu.temple.beatbuddy.R
import edu.temple.beatbuddy.repository.auth.AuthViewModel

@Composable
fun SignUpScreen(
    authViewModel: AuthViewModel,
    goToSignInScreen: () -> Unit,
) {
    var email by remember { mutableStateOf("") }
    var fullname by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

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
                text = "Create an Account",
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Normal,
                fontSize = 18.sp
            )
        }

        OutlinedTextField(
            value = fullname,
            onValueChange = {
                fullname = it
            },
            label = { Text("Full name") },
            leadingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.person),
                    contentDescription = ""
                )
            }
        )

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
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                authViewModel.signUp(
                    fullName = fullname,
                    email = email,
                    password = password
                )

                goToSignInScreen()
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
                    text = "Sign Up",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }

        val annotatedString = buildAnnotatedString {
            append("Already have an account? ")
            withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.primary)) {
                pushStringAnnotation(tag = "Sign in", annotation = "Sign in")
                append("Sign in")
            }
        }

        ClickableText(
            text = annotatedString,
            onClick = { goToSignInScreen() }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewSignUp() {
    SignUpScreen(AuthViewModel(), {})
}