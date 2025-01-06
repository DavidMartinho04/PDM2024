package com.example.loja_pdm.data.firebase

import android.net.Uri
import com.example.loja_pdm.presentation.viewmodels.Cart
import com.example.loja_pdm.presentation.viewmodels.Product
import com.example.loja_pdm.presentation.viewmodels.UserViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.util.UUID

fun registerUser(
    userViewModel: UserViewModel, // ViewModel que contém os dados do utilizador
    onSuccess: () -> Unit, // Callback para executar em caso de sucesso
    onFailure: (Exception) -> Unit // Callback para executar em caso de falha
) {
    val auth = FirebaseAuth.getInstance() // Obtem a instância do Firebase Authentication
    val db = FirebaseFirestore.getInstance() // Obtem a instância do Firestore

    // Criação do utilizador no Firebase Authentication
    auth.createUserWithEmailAndPassword(userViewModel.email, userViewModel.password)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val userId = task.result?.user?.uid // Obtem o UID do utilizador criado

                // Dados do utilizador a serem guardados no Firestore
                val userData = mapOf(
                    "id" to userId, // ID único do utilizador
                    "photoUrl" to (userViewModel.photoUrl ?: ""), // URL da imagem de perfil
                    "name" to userViewModel.name, // Nome do utilizador
                    "birthDate" to userViewModel.birthDate, // Data de nascimento
                    "phone" to userViewModel.phone, // Número de telemóvel
                    "email" to userViewModel.email, // Endereço de email
                    "address" to userViewModel.address, // Morada do utilizador
                    "postalCode" to userViewModel.postalCode, // Código postal
                    "gender" to userViewModel.gender, // Género do utilizador
                )

            userId?.let {
                // Guarda os dados no Firestore, na coleção "users", com o UID como documento
                db.collection("users").document(it)
                    .set(userData) // Adiciona os dados do utilizador
                    .addOnSuccessListener { onSuccess() } // Chama o callback de sucesso
                    .addOnFailureListener { e -> onFailure(e) } // Chama o callback de falha com a exceção
            }
        } else {
            // Caso a criação do utilizador falhe, chama o callback de falha com a exceção
            onFailure(task.exception ?: Exception("Erro desconhecido ao criar utilizador."))
        }
    }
}

// Função para fazer upload da foto para o Firebase Storage
fun uploadPhotoToFirebaseStorage(
    photoUri: Uri, // URI da foto a ser carregada
    onSuccess: (String) -> Unit, // Callback de sucesso com o URL da foto
    onFailure: (Exception) -> Unit // Callback de falha com a exceção
) {
    val storageRef = FirebaseStorage.getInstance().reference
    val fileName = "${UUID.randomUUID()}.jpg" // Nome único para o ficheiro
    val photoRef = storageRef.child("user_photos/$fileName")

    // Faz upload do ficheiro para o Firebase Storage
    photoRef.putFile(photoUri)
        .addOnSuccessListener {
            // Obtém o URL de download após o upload
            photoRef.downloadUrl.addOnSuccessListener { uri ->
                onSuccess(uri.toString())
            }
        }
        .addOnFailureListener { exception ->
            onFailure(exception)
        }
}

// Função para remover um item do carrinho
fun removeItemFromCart(product: Cart, userEmail: String) {
    val db = FirebaseFirestore.getInstance()
    db.collection("carrinhos")
    .whereEqualTo("userEmail", userEmail)
    .whereEqualTo("productId", product.id)
    .whereEqualTo("tamanho", product.size)
    .get()
    .addOnSuccessListener { result ->
        result.documents.forEach { document ->
            document.reference.delete().addOnSuccessListener {
                println("Item removido com sucesso!")
            }.addOnFailureListener { e ->
                println("Erro ao remover item: $e")
            }
        }
    }
}

fun adjustQuantity(
    product: Cart,
    userEmail: String,
    increment: Boolean,
    onSuccess: (() -> Unit)? = null,
    onFailure: ((Exception) -> Unit)? = null
) {
    val db = FirebaseFirestore.getInstance()

    db.collection("carrinhos")
        .whereEqualTo("userEmail", userEmail)
        .whereEqualTo("productId", product.id)
        .whereEqualTo("tamanho", product.size)
        .get()
        .addOnSuccessListener { result ->
            val document = result.documents.firstOrNull()
            if (document != null) {
                val currentQuantity = document.getLong("quantidade")?.toInt() ?: 1
                val newQuantity = if (increment) currentQuantity + 1 else maxOf(0, currentQuantity - 1)

                if (newQuantity > 0) {
                    document.reference.update("quantidade", newQuantity)
                        .addOnSuccessListener { onSuccess?.invoke() }
                        .addOnFailureListener { e -> onFailure?.invoke(e) }
                } else {
                    document.reference.delete()
                        .addOnSuccessListener { onSuccess?.invoke() }
                        .addOnFailureListener { e -> onFailure?.invoke(e) }
                }
            } else {
                onFailure?.invoke(Exception("Documento não encontrado"))
            }
        }
        .addOnFailureListener { e ->
            onFailure?.invoke(e)
        }
}

fun fetchUserData(
    userEmail: String,
    onSuccess: (String, String, String, String) -> Unit,
    onFailure: (Exception) -> Unit
) {
    val db = FirebaseFirestore.getInstance()
    db.collection("users")
        .whereEqualTo("email", userEmail)
        .get()
        .addOnSuccessListener { result ->
            val userDoc = result.documents.firstOrNull()
            if (userDoc != null) {
                val name = userDoc.getString("name") ?: ""
                val phone = userDoc.getString("phone") ?: ""
                val address = userDoc.getString("address") ?: ""
                val postalCode = userDoc.getString("postalCode") ?: ""
                onSuccess(name, phone, address, postalCode)
            } else {
                onFailure(Exception("Utilizador não encontrado"))
            }
        }
        .addOnFailureListener { exception ->
            onFailure(exception)
        }
}

fun fetchArticles(
    onSuccess: (List<Product>) -> Unit,
    onFailure: (Exception) -> Unit
) {
    val db = FirebaseFirestore.getInstance()
    db.collection("artigos")
        .get()
        .addOnSuccessListener { result ->
            val produtos = result.documents.map { document ->
                Product(
                    id = document.getDouble("id")?.toInt() ?: 0,
                    name = document.getString("name") ?: "",
                    imgUrl = document.getString("img") ?: "",
                    cor = document.getString("cor") ?: "",
                    marca = document.getString("marca") ?: "",
                    modelo = document.getString("modelo") ?: "",
                    preco = document.getDouble("preco") ?: 0.0,
                    tipo = document.getString("tipo") ?: ""
                )
            }
            onSuccess(produtos)
        }
        .addOnFailureListener { exception ->
            onFailure(exception)
        }
}

fun fetchProductById(
    productId: Int,
    onSuccess: (Product?) -> Unit,
    onFailure: (Exception) -> Unit
) {
    val db = FirebaseFirestore.getInstance()
    db.collection("artigos")
        .whereEqualTo("id", productId)
        .get()
        .addOnSuccessListener { result ->
            val product = result.documents.firstOrNull()?.let { document ->
                Product(
                    id = document.getLong("id")?.toInt() ?: 0,
                    name = document.getString("name") ?: "",
                    imgUrl = document.getString("img") ?: "",
                    cor = document.getString("cor") ?: "",
                    marca = document.getString("marca") ?: "",
                    modelo = document.getString("modelo") ?: "",
                    preco = document.getDouble("preco") ?: 0.0,
                    tipo = document.getString("tipo") ?: ""
                )
            }
            onSuccess(product)
        }
        .addOnFailureListener { exception ->
            onFailure(exception)
        }
}

fun isProductFavorite(
    userEmail: String,
    productId: Int,
    onSuccess: (Boolean) -> Unit,
    onFailure: (Exception) -> Unit
) {
    val db = FirebaseFirestore.getInstance()
    db.collection("favoritos")
        .whereEqualTo("userEmail", userEmail)
        .whereEqualTo("productId", productId)
        .get()
        .addOnSuccessListener { result ->
            onSuccess(result.documents.isNotEmpty())
        }
        .addOnFailureListener { exception ->
            onFailure(exception)
        }
}

fun fetchFavoriteProducts(
    userEmail: String,
    onSuccess: (List<Product>) -> Unit,
    onFailure: (Exception) -> Unit
) {
    val db = FirebaseFirestore.getInstance()
    db.collection("favoritos")
        .whereEqualTo("userEmail", userEmail)
        .get()
        .addOnSuccessListener { result ->
            val favoriteIds = result.documents.mapNotNull { it.getLong("productId")?.toInt() }
            if (favoriteIds.isNotEmpty()) {
                db.collection("artigos")
                    .whereIn("id", favoriteIds)
                    .get()
                    .addOnSuccessListener { articles ->
                        val products = articles.documents.mapNotNull { doc ->
                            Product(
                                id = doc.getLong("id")?.toInt() ?: 0,
                                name = doc.getString("name") ?: "",
                                imgUrl = doc.getString("img") ?: "",
                                cor = doc.getString("cor") ?: "",
                                marca = doc.getString("marca") ?: "",
                                modelo = doc.getString("modelo") ?: "",
                                preco = doc.getDouble("preco") ?: 0.0,
                                tipo = doc.getString("tipo") ?: ""
                            )
                        }
                        onSuccess(products)
                    }
                    .addOnFailureListener { exception ->
                        onFailure(exception)
                    }
            } else {
                onSuccess(emptyList()) // Nenhum favorito encontrado
            }
        }
        .addOnFailureListener { exception ->
            onFailure(exception)
        }
}

fun fetchArticlesByType(
    tipo: String,
    onSuccess: (List<Product>) -> Unit,
    onFailure: (Exception) -> Unit
) {
    val db = FirebaseFirestore.getInstance()
    db.collection("artigos")
        .whereEqualTo("tipo", tipo)  // Filtra pelo tipo especificado (Homem, Mulher, Criança)
        .get()
        .addOnSuccessListener { result ->
            val produtos = result.documents.mapNotNull { document ->
                try {
                    Product(
                        id = (document.get("id") as? Number)?.toInt() ?: 0,
                        name = document.getString("name") ?: "",
                        imgUrl = document.getString("img") ?: "",
                        cor = document.getString("cor") ?: "",
                        marca = document.getString("marca") ?: "",
                        modelo = document.getString("modelo") ?: "",
                        preco = (document.get("preco") as? Number)?.toDouble() ?: 0.0,
                        tipo = document.getString("tipo") ?: ""
                    )
                } catch (e: Exception) {
                    null  // Ignora documentos mal formatados
                }
            }
            onSuccess(produtos)
        }
        .addOnFailureListener { exception ->
            onFailure(exception)
        }
}
