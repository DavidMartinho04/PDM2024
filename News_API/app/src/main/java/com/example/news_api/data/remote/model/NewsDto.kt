package com.example.news_api.data.remote.model

import com.google.gson.annotations.SerializedName

/**
 * Classe que mostra os dados das notícias retornados pela API.
 *
 * @property title O título da notícia.
 * @property abstract Um resumo ou descrição breve da notícia.
 * @property url O URL que direciona para a página completa da notícia.
 * @property multimedia Uma lista de objetos [Multimedia], que mostra as imagens associadas à notícia.
 */
data class NewsDto(
    @SerializedName("title") val title: String, // Associa o campo "title" da API ao campo "title" da classe.
    @SerializedName("abstract") val abstract: String, // Associa o campo "abstract" da API ao campo "abstract" da classe.
    @SerializedName("url") val url: String, // Associa o campo "url" da API ao campo "url" da classe.
    @SerializedName("multimedia") val multimedia: List<Multimedia>? // Associa o campo "multimedia" da API a uma lista de objetos [Multimedia].
)

/**
 * Classe que representa os dados multimédia de uma notícia.
 *
 * @property url O URL da imagem.
 * @property format O formato da imagem (ex.: "Super Jumbo", "Large Thumbnail").
 */
data class Multimedia(
    @SerializedName("url") val url: String, // Associa o campo "url" da API ao campo "url" da classe.
    @SerializedName("format") val format: String // Associa o campo "format" da API ao campo "format" da classe.
)
