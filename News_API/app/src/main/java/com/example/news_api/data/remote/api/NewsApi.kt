package com.example.news_api.data.remote.api

import com.example.news_api.data.remote.model.NewsResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Interface que define os endpoints da API do New York Times para serem utilizados com Retrofit.
 */
interface NewsApi {

    /**
     * Função que obtém as principais notícias de uma determinada secção do New York Times.
     *
     * @param section A secção das notícias que se pretende obter (ex.: "sports", "world", "technology").
     * @param apiKey A chave da API necessária para autenticar a requisição à API do New York Times.
     * @return Um objeto [NewsResponse] que contém a lista de notícias obtidas da API.
     */
    @GET("topstories/v2/{section}.json") // Define o endpoint para obter as notícias principais de uma secção específica.
    suspend fun getTopStories(
        @Path("section") section: String, // Substitui o parâmetro {section} no URL pelo valor fornecido.
        @Query("api-key") apiKey: String  // Adiciona a chave da API como um parâmetro de query na requisição.
    ): NewsResponse
}
