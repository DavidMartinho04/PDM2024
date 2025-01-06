package com.example.loja_pdm.presentation.createaccount

// Importações necessárias para os componentes e funções do Compose e Firebase
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.loja_pdm.data.firebase.registerUser
import com.example.loja_pdm.presentation.viewmodels.UserViewModel
import com.example.loja_pdm.ui.components.ProgressIndicators

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FinalizeAccountScreen(
    onCreateAccountClick: () -> Unit, // Callback para navegação após criação de conta
    userViewModel: UserViewModel = viewModel() // Injeta o ViewModel para gestão de dados do utilizador
) {
    // Definições de cores para a interface
    val primaryColor = Color(0xFFFF6F00)  // Laranja
    val secondaryColor = Color(0xFF6F6F6F)  // Cinza escuro
    val textFieldBackground = Color(0xFF333333) // Cor de fundo dos campos de texto (preto)
    val blackColor = Color(0xFF181818) // Preto
    val whiteColor = Color(0xFFFFFFFF) // Branco

    // Estados locais para os valores do formulário
    var address by remember { mutableStateOf("") }
    var postalCode by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf("Masculino") } // Género inicial padrão

    // Contexto para exibir Toasts
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .background(blackColor)  // Fundo preto
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Título do ecrã
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = "Quase Lá!",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = whiteColor,  // Texto branco
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Indicadores de progresso (etapa atual e total)
        ProgressIndicators(
            currentStep = 2,
            totalSteps = 2,
            activeColor = primaryColor,  // Laranja
            inactiveColor = textFieldBackground  // Preto
        )
        Spacer(modifier = Modifier.height(35.dp))

        // Campo para Morada
        OutlinedTextField(
            value = address,
            onValueChange = { address = it },
            label = { Text("Morada", color = whiteColor) },  // Texto branco
            singleLine = true,
            colors = TextFieldDefaults.outlinedTextFieldColors(
                containerColor = textFieldBackground,  // Fundo preto
                focusedBorderColor = primaryColor,  // Laranja
                unfocusedBorderColor = textFieldBackground,  // Preto
                focusedTextColor = whiteColor,  // Texto branco
                unfocusedTextColor = whiteColor  // Texto branco
            ),
            modifier = Modifier.fillMaxWidth(0.9f)
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Campo para Código Postal
        OutlinedTextField(
            value = postalCode,
            onValueChange = { input ->
                // Permite apenas números e o caractere "-"
                if (input.matches(Regex("^[0-9-]*$"))) {
                    postalCode = input
                }
            },
            label = { Text("Código Postal", color = whiteColor) },  // Texto branco
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            singleLine = true,
            colors = TextFieldDefaults.outlinedTextFieldColors(
                containerColor = textFieldBackground,  // Fundo preto
                focusedBorderColor = primaryColor,  // Laranja
                unfocusedBorderColor = textFieldBackground,  // Preto
                focusedTextColor = whiteColor,  // Texto branco
                unfocusedTextColor = whiteColor  // Texto branco
            ),
            modifier = Modifier.fillMaxWidth(0.9f)
        )

        Spacer(modifier = Modifier.height(30.dp))

        // Seção de seleção de Género
        Text(
            text = "Género",
            fontSize = 16.sp,
            color = whiteColor,  // Branco
            modifier = Modifier
                .fillMaxWidth(1f)
                .padding(start = 5.dp)
        )
        Row(
            Modifier
                .fillMaxWidth(1f)
                .padding(vertical = 5.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Lista de opções de género
            listOf("Masculino", "Feminino", "Outro").forEach { option ->
                Row(
                    Modifier
                        .selectable(
                            selected = (gender == option),
                            onClick = { gender = option }
                        )
                        .padding(horizontal = 5.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = (gender == option),
                        onClick = { gender = option },
                        colors = RadioButtonDefaults.colors(selectedColor = primaryColor)  // Laranja
                    )
                    Text(text = option, fontSize = 13.sp, color = whiteColor)  // Branco
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Botão para finalizar criação da conta
        Button(
            onClick = {
                if (postalCode.matches(Regex("^\\d{4}-\\d{3}$"))) {  // Valida o formato do código postal
                    userViewModel.updateFinalizeData(
                        address = address,
                        postalCode = postalCode,
                        gender = gender,
                    )

                    // Regista o utilizador no Firebase
                    registerUser(
                        userViewModel = userViewModel,
                        onSuccess = {
                            Toast.makeText(context, "Conta criada com sucesso!", Toast.LENGTH_SHORT).show()
                            onCreateAccountClick()
                        },
                        onFailure = { e ->
                            Toast.makeText(context, "Erro ao criar conta: ${e.message}", Toast.LENGTH_SHORT).show()
                        }
                    )
                } else {
                    Toast.makeText(context, "Por favor, insira um código postal no formato ####-###.", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier
                .width(150.dp)
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = primaryColor)  // Laranja
        ) {
            Text("Criar Conta", color = Color.White, fontSize = 16.sp)
        }
        Spacer(modifier = Modifier.weight(1f))
    }
}

