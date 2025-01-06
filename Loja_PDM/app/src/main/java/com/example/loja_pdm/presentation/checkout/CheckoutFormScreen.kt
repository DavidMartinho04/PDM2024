package com.example.loja_pdm.presentation.checkout

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.loja_pdm.data.firebase.fetchUserData
import com.example.loja_pdm.presentation.viewmodels.CartViewModel
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FinalizePurchaseScreen(
    userEmail: String,
    navController: NavHostController,
    cartViewModel: CartViewModel
) {
    val primaryColor = Color(0xFFFF6F00)
    val textFieldBackground = Color(0xFF333333)
    val whiteColor = Color(0xFFFFFFFF)
    val disabledTextColor = Color(0xFF888888)
    val blackColor = Color(0xFF181818)
    val context = LocalContext.current
    val db = FirebaseFirestore.getInstance()

    // Estados de input
    var name by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var postalCode by remember { mutableStateOf("") }
    var paymentMethod by remember { mutableStateOf("Cartão de Crédito") }
    var isNewAddress by remember { mutableStateOf(false) }
    var cardNumberState by remember { mutableStateOf(TextFieldValue()) }
    var cvv by remember { mutableStateOf("") }
    var mbwayPhone by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(true) }

    val cartProducts = cartViewModel.cartProducts.value
    val postalCodeValid = postalCode.matches(Regex("""^\d{4}-\d{3}$"""))
    val cardNumberValid = cardNumberState.text.replace("-", "").length == 16
    val cvvValid = cvv.length == 3

    // Buscar dados do utilizador ao iniciar o ecrã
    LaunchedEffect(userEmail) {
        fetchUserData(
            userEmail = userEmail,
            onSuccess = { fetchedName, fetchedPhone, fetchedAddress, fetchedPostalCode ->
                name = fetchedName
                phone = fetchedPhone
                address = fetchedAddress
                postalCode = fetchedPostalCode
                isLoading = false
            },
            onFailure = { exception ->
                Toast.makeText(context, "Erro ao buscar dados do utilizador", Toast.LENGTH_SHORT)
                    .show()
                isLoading = false
            }
        )
    }

    // Layout da interface
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(blackColor)
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(40.dp))
        if (isLoading) {
            Text("A carregar dados do utilizador...", color = primaryColor)
        } else {
            // Título
            Text(
                text = "Finalizar Compra",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = primaryColor
            )

            Spacer(modifier = Modifier.height(30.dp))

            // Campo Nome
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Nome", color = whiteColor) },
                readOnly = true,
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    containerColor = textFieldBackground,
                    focusedBorderColor = primaryColor,
                    unfocusedBorderColor = textFieldBackground,
                    focusedTextColor = disabledTextColor,
                    unfocusedTextColor = disabledTextColor
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Campo Email
            OutlinedTextField(
                value = userEmail,
                onValueChange = { },
                label = { Text("Email", color = whiteColor) },
                readOnly = true,
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    containerColor = textFieldBackground,
                    focusedBorderColor = primaryColor,
                    unfocusedBorderColor = textFieldBackground,
                    focusedTextColor = disabledTextColor,
                    unfocusedTextColor = disabledTextColor
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Campo Telefone
            OutlinedTextField(
                value = phone,
                onValueChange = { phone = it },
                label = { Text("Telefone", color = whiteColor) },
                readOnly = true,
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    containerColor = textFieldBackground,
                    focusedBorderColor = primaryColor,
                    unfocusedBorderColor = textFieldBackground,
                    focusedTextColor = disabledTextColor,
                    unfocusedTextColor = disabledTextColor
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Campo Morada
            OutlinedTextField(
                value = address,
                onValueChange = { address = it },
                label = { Text("Morada", color = whiteColor) },
                readOnly = !isNewAddress,
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    containerColor = textFieldBackground,
                    focusedBorderColor = primaryColor,
                    unfocusedBorderColor = textFieldBackground,
                    focusedTextColor = if (isNewAddress) whiteColor else disabledTextColor,
                    unfocusedTextColor = if (isNewAddress) whiteColor else disabledTextColor
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Campo Código Postal
            OutlinedTextField(
                value = postalCode,
                onValueChange = { postalCode = it },
                label = { Text("Código Postal", color = whiteColor) },
                readOnly = !isNewAddress,
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    containerColor = textFieldBackground,
                    focusedBorderColor = primaryColor,
                    unfocusedBorderColor = textFieldBackground,
                    focusedTextColor = if (isNewAddress) whiteColor else disabledTextColor,
                    unfocusedTextColor = if (isNewAddress) whiteColor else disabledTextColor
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Checkbox para nova morada de entrega
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Checkbox(
                    checked = isNewAddress,
                    onCheckedChange = { isNewAddress = it },
                    colors = CheckboxDefaults.colors(checkedColor = primaryColor)
                )
                Text(text = "Morada Entrega Diferente", color = whiteColor)
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Seleção de método de pagamento
            Text(
                "Método de Pagamento",
                color = whiteColor,
                modifier = Modifier.align(Alignment.Start)
            )
            Spacer(modifier = Modifier.height(10.dp))
            listOf(
                "Cartão de Crédito" to Icons.Default.CreditCard,
                "MB Way" to Icons.Default.Phone
            ).forEach { (method, icon) ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .selectable(
                            selected = (paymentMethod == method),
                            onClick = { paymentMethod = method }
                        )
                        .padding(8.dp)
                ) {
                    // Ícone ao lado do método de pagamento
                    Icon(
                        imageVector = icon,
                        contentDescription = method,
                        tint = primaryColor,
                        modifier = Modifier.size(24.dp)
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    // Nome do método de pagamento
                    Text(
                        text = method,
                        fontSize = 16.sp,
                        color = whiteColor
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    // RadioButton para indicar seleção
                    RadioButton(
                        selected = (paymentMethod == method),
                        onClick = { paymentMethod = method },
                        colors = RadioButtonDefaults.colors(selectedColor = primaryColor)
                    )
                }
            }

            // Campos adicionais dependendo do método de pagamento
            if (paymentMethod == "Cartão de Crédito") {
                Spacer(modifier = Modifier.height(5.dp))
                // Campo Número do Cartão
                OutlinedTextField(
                    value = cardNumberState,
                    onValueChange = { input ->
                        // Remove os hífens e caracteres não numéricos
                        val cleanedInput = input.text.filter { it.isDigit() }

                        // Limita a 16 dígitos (sem contar os hífens)
                        val limitedInput = cleanedInput.take(16)

                        // Formata o número com hífens a cada 4 dígitos
                        val formattedInput = limitedInput.chunked(4).joinToString("-")

                        // Atualizar o estado mantendo o cursor no final
                        cardNumberState = TextFieldValue(
                            text = formattedInput,
                            selection = TextRange(formattedInput.length) // Cursor no fim
                        )
                    },
                    label = { Text("Número do Cartão", color = whiteColor) },
                    readOnly = false,
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        containerColor = textFieldBackground,
                        focusedBorderColor = primaryColor,
                        unfocusedBorderColor = textFieldBackground,
                        focusedTextColor = whiteColor,
                        unfocusedTextColor = whiteColor
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(
                    value = cvv,
                    onValueChange = {
                        if (it.length <= 3 && it.all { char -> char.isDigit() }) {
                            cvv = it
                        }
                    },
                    label = { Text("CVV", color = whiteColor) },
                    readOnly = false,
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        containerColor = textFieldBackground,
                        focusedBorderColor = primaryColor,
                        unfocusedBorderColor = textFieldBackground,
                        focusedTextColor = whiteColor,
                        unfocusedTextColor = whiteColor
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
            }

            if (paymentMethod == "MB Way") {
                Spacer(modifier = Modifier.height(5.dp))
                // Campo Telefone MB Way
                OutlinedTextField(
                    value = mbwayPhone,
                    onValueChange = {
                        if (it.length <= 9 && it.all { char -> char.isDigit() }) {
                            mbwayPhone = it
                        }
                    },
                    label = { Text("Nº Telefone", color = whiteColor) },
                    readOnly = false,
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        containerColor = textFieldBackground,
                        focusedBorderColor = primaryColor,
                        unfocusedBorderColor = textFieldBackground,
                        focusedTextColor = whiteColor,
                        unfocusedTextColor = whiteColor
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Spacer(modifier = Modifier.height(40.dp))
            // Botão de Finalizar Compra
            Button(
                onClick = {
                    if (address.isBlank() || postalCode.isBlank() || !postalCodeValid ||
                        (paymentMethod == "Cartão de Crédito" && (!cardNumberValid || !cvvValid)) ||
                        (paymentMethod == "MB Way" && mbwayPhone.length != 9)
                    ) {
                        Toast.makeText(context, "Preencha corretamente todos os campos obrigatórios!", Toast.LENGTH_SHORT).show()
                    } else {
                        val purchaseData = hashMapOf(
                            "userEmail" to userEmail,
                            "name" to name,
                            "phone" to phone,
                            "address" to address,
                            "postalCode" to postalCode,
                            "paymentMethod" to paymentMethod,
                            "cardNumber" to cardNumberState.text.replace("-", "").takeIf { paymentMethod == "Cartão de Crédito" },
                            "cvv" to cvv.takeIf { paymentMethod == "Cartão de Crédito" },
                            "mbwayPhone" to mbwayPhone.takeIf { paymentMethod == "MB Way" },
                            "purchaseDate" to Timestamp.now(),
                            "cartItems" to cartProducts.map { cart ->
                                mapOf(
                                    "id" to cart.id,
                                    "name" to cart.name,
                                    "imgUrl" to cart.imgUrl,
                                    "preco" to cart.preco,
                                    "quantity" to cart.quantity,
                                    "size" to cart.size
                                )
                            }
                        )
                        db.collection("compras").add(purchaseData)
                            .addOnSuccessListener {
                                // Limpar o carrinho no Firebase e ViewModel após a compra
                                db.collection("carrinhos")
                                    .whereEqualTo("userEmail", userEmail)
                                    .get()
                                    .addOnSuccessListener { result ->
                                        for (document in result.documents) {
                                            document.reference.delete()
                                        }
                                        cartViewModel.clearCart()
                                        Toast.makeText(context, "Compra finalizada com sucesso!", Toast.LENGTH_SHORT).show()
                                        navController.navigate("menu")
                                    }
                            }
                            .addOnFailureListener { e ->
                                Toast.makeText(context, "Erro ao finalizar compra: ${e.message}", Toast.LENGTH_SHORT).show()
                            }
                    }
                },
                modifier = Modifier
                    .width(190.dp)
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = primaryColor)
            ) {
                Text("Concluir Compra", color = Color.White, fontSize = 16.sp)
            }

            Spacer(modifier = Modifier.height(30.dp))
        }
    }
}

