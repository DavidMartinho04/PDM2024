package com.example.loja_pdm.presentation.login

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.loja_pdm.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    onLoginSuccess: (String) -> Unit, // Callback para navegação com base no tipo de utilizador
    onCreateAccountClick: () -> Unit // Callback para navegação ao ecrã de criação de conta
) {
    // Definições de cores para a interface
    val primaryColor = Color(0xFFFF6F00)  // Laranja
    val secondaryColor = Color(0xFF6F6F6F)  // Cinza escuro
    val White = Color(0xFFFFFFFF) // Branco
    val textFieldBackground = Color(0xFF333333) // Cor de fundo dos campos de texto (preto)
    val blackColor = Color(0xFF181818) // Preto

    // Estados para email e palavra-passe
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) } // Controla a visibilidade da palavra-passe

    // Inicializa Firebase Auth e Firestore
    val context = LocalContext.current
    val auth = FirebaseAuth.getInstance()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(blackColor) // Fundo preto para o ecrã
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Logo da aplicação
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "Logo Bem Ajudar",
            modifier = Modifier
                .size(300.dp)
                .padding(top = 24.dp)
        )

        Spacer(modifier = Modifier.height(4.dp))

        // Mensagem de boas-vindas
        Text(
            text = "Bem Vindo",
            fontSize = 22.sp,
            fontWeight = FontWeight.Normal,
            color = primaryColor,
            textAlign = TextAlign.Center
        )
        Text(
            text = "à Urban Shoes!",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = primaryColor,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Campo de Email
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email", color = White) },
            trailingIcon = {
                Icon(
                    imageVector = Icons.Default.Email,
                    contentDescription = "Ícone de Email",
                    tint = primaryColor
                )
            },
            singleLine = true,
            colors = TextFieldDefaults.outlinedTextFieldColors(
                containerColor = textFieldBackground,
                focusedBorderColor = primaryColor,
                unfocusedBorderColor = textFieldBackground,
                focusedTextColor = White,
                unfocusedTextColor = White
            ),
            modifier = Modifier.fillMaxWidth(0.95f)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Campo de Palavra-Passe
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Palavra-Passe", color = White) },
            trailingIcon = {
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(
                        imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                        contentDescription = if (passwordVisible) "Ocultar Palavra-Passe" else "Mostrar Palavra-Passe",
                        tint = primaryColor
                    )
                }
            },
            singleLine = true,
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                containerColor = textFieldBackground,
                focusedBorderColor = primaryColor,
                unfocusedBorderColor = textFieldBackground,
                focusedTextColor = White,
                unfocusedTextColor = White
            ),
            modifier = Modifier.fillMaxWidth(0.95f)
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Botão de Iniciar Sessão
        Button(
            onClick = {
                if (email.isNotEmpty() && password.isNotEmpty()) {
                    auth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                val currentUser = auth.currentUser
                                if (currentUser != null) {
                                    val userEmail = currentUser.email
                                    if (userEmail != null) {
                                        // Passa o email do utilizador autenticado
                                        onLoginSuccess(userEmail)
                                    } else {
                                        Toast.makeText(
                                            context,
                                            "Email do utilizador não encontrado!",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                } else {
                                    Toast.makeText(
                                        context,
                                        "Erro ao obter dados do utilizador!",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            } else {
                                val errorMessage = translateFirebaseError(task.exception?.message)
                                Toast.makeText(context, "Erro no login: $errorMessage", Toast.LENGTH_SHORT).show()
                            }
                        }
                } else {
                    Toast.makeText(context, "Por favor, preencha todos os campos!", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier
                .width(180.dp)
                .height(54.dp),
            colors = ButtonDefaults.buttonColors(containerColor = primaryColor)
        ) {
            Text(
                text = "Iniciar Sessão",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Texto para redefinir a palavra-passe
        TextButton(
            onClick = {
                if (email.isNotEmpty()) {
                    auth.sendPasswordResetEmail(email)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                Toast.makeText(
                                    context,
                                    "Email para redefinição enviado para $email!",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                val errorMessage = translateFirebaseError(task.exception?.message)
                                Toast.makeText(context, "Erro: $errorMessage", Toast.LENGTH_SHORT).show()
                            }
                        }
                } else {
                    Toast.makeText(context, "Por favor, insira o email para redefinir a palavra-passe.", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text(
                text = "Esqueci-me da palavra passe",
                color = primaryColor,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        // Texto para criar conta
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "Ainda não tem conta?",
                color = secondaryColor,
                fontSize = 14.sp
            )
            TextButton(onClick = onCreateAccountClick) {
                Text(
                    text = "Criar Conta",
                    color = primaryColor,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }
        }
    }
}

// Função para traduzir mensagens de erro do Firebase
fun translateFirebaseError(message: String?): String {
    val errorMessages = mapOf(
        "The email address is badly formatted." to "O formato do email é inválido.",
        "The password is invalid or the user does not have a password." to "A palavra-passe está incorreta.",
        "There is no user record corresponding to this identifier. The user may have been deleted." to "Este email não está registado.",
        "The email address is already in use by another account." to "O email já está em uso por outra conta.",
        "Password should be at least 6 characters" to "A palavra-passe deve ter pelo menos 6 caracteres.",
        "We have blocked all requests from this device due to unusual activity. Try again later." to "Bloqueamos os pedidos deste dispositivo devido a atividade incomum. Tente mais tarde."
    )
    return errorMessages[message] ?: "Ocorreu um erro inesperado. Tente novamente."
}
