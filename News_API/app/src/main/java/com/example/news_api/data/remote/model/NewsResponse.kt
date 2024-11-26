package com.example.news_api.data.remote.model

import com.google.gson.annotations.SerializedName

/**
 * Classe que representa a resposta completa da API ao buscar notícias.
 *
 * @property results Uma lista de objetos [NewsDto], que contém os detalhes de cada notícia retornada pela API.
 */
data class NewsResponse(
    @SerializedName("results") val results: List<NewsDto> // Associa o campo "results" da API à lista de objetos [NewsDto].
)
