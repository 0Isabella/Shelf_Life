package com.example.shelflife.navigation

object Rotas {

    const val ADD_LIVRO = "add_livro"
    const val HOME = "home"
    const val RANKING = "ranking"
    const val AMIGOS = "amigos"
    const val ESTANTE = "estante"
    const val PERFIL = "perfil"
    const val DETALHE_AMIGO = "detalhe_amigo/{amigoId}"
    const val DETALHE_LIVRO = "detalhe_livro/{livroId}"
    const val ANOTACAO = "anotacao/{livroId}"

    fun detalheAmigo(id: Int) = "detalhe_amigo/$id"
    fun detalheLivro(id: Int) = "detalhe_livro/$id"
    fun anotacao(id: Int) = "anotacao/$id"
}