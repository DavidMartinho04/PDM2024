package com.example.news_api.data.remote

import com.example.news_api.data.remote.api.NewsApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Objeto responsável por criar e fornecer uma instância única de Retrofit definida para a API.
 *
 */
object RetrofitInstance {

    /**
     * A URL base utilizada para todas as requisições à API.
     */
    private const val BASE_URL = "https://api.nytimes.com/svc/"

    /**
     * Propriedade que inicia a instância da interface [NewsApi].
     *
     * Utiliza o Retrofit para construir uma implementação da interface [NewsApi], que define os endpoints da API.
     *
     * - `baseUrl`: Define a URL base da API.
     * - `addConverterFactory`: Configura o Retrofit para converter automaticamente as respostas JSON em objetos Kotlin, com o Gson.
     * - `build`: Constrói a instância do Retrofit com as configurações fornecidas.
     * - `create`: Gera a implementação da interface [NewsApi].
     */
    val api: NewsApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(NewsApi::class.java)
    }
}
