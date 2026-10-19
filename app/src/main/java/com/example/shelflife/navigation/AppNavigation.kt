package com.example.shelflife.navigation

import com.example.shelflife.data.LivrosStorage
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.sp
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.shelflife.data.LivrosIniciais
import com.example.shelflife.model.Amigo
import com.example.shelflife.model.Livro
import com.example.shelflife.ui.theme.CorCartao
import com.example.shelflife.ui.theme.CorDestaque
import com.example.shelflife.ui.theme.CorFundo
import com.example.shelflife.ui.theme.CorTextoSecundario
import com.example.shelflife.ui.theme.screens.AmigosStorage
import com.example.shelflife.ui.theme.screens.TelaAmigos
import com.example.shelflife.ui.theme.screens.TelaAnotacao
import com.example.shelflife.ui.theme.screens.TelaDetalheAmigo
import com.example.shelflife.ui.theme.screens.TelaDetalhes
import com.example.shelflife.ui.theme.screens.TelaEstante
import com.example.shelflife.ui.theme.screens.TelaInicio
import com.example.shelflife.ui.theme.screens.TelaPerfil
import com.example.shelflife.ui.theme.screens.TelaRanking
import com.example.shelflife.ui.theme.screens.TelaAddLivro

private data class ItemBarra(val rota: String, val emoji: String, val rotulo: String)

private val itensBarra = listOf(
    ItemBarra(Rotas.HOME, "🏠", "Home"),
    ItemBarra(Rotas.RANKING, "👑", "Ranking"),
    ItemBarra(Rotas.ESTANTE, "📚", "Estante"),
    ItemBarra(Rotas.AMIGOS, "👥", "Amigos"),
    ItemBarra(Rotas.PERFIL, "🙂", "Perfil")
)

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val context = LocalContext.current

    val amigos = remember {
        mutableStateListOf<Amigo>().apply { addAll(AmigosStorage.carregar(context)) }
    }

    fun adicionarAmigo(amigo: Amigo) {
        if (amigos.none { it.id == amigo.id }) {
            amigos.add(0, amigo)
            AmigosStorage.salvar(context, amigos)
        }
    }

    fun removerAmigo(amigo: Amigo) {
        amigos.removeAll { it.id == amigo.id }
        AmigosStorage.salvar(context, amigos)
    }

    val livros = remember {
        mutableStateListOf<Livro>().apply { addAll(LivrosStorage.carregar(context)) }
    }

    fun adicionarLivro(titulo: String, autor: String, paginas: Int) {
        val novoId = (livros.maxOfOrNull { it.id } ?: -1) + 1
        livros.add(
            0,
            Livro(
                id = novoId,
                titulo = titulo,
                autor = autor.ifBlank { "Autor desconhecido" },
                paginasTotal = paginas
            )
        )
        LivrosStorage.salvar(context, livros)
    }

    fun removerLivro(livro: Livro) {
        livros.removeAll { it.id == livro.id }
        LivrosStorage.salvar(context, livros)
    }

    fun atualizarLivro(livro: Livro) {
        val indice = livros.indexOfFirst { it.id == livro.id }
        if (indice != -1) livros[indice] = livro
        LivrosStorage.salvar(context, livros)
    }

    val entradaAtual by navController.currentBackStackEntryAsState()
    val rotaAtual = entradaAtual?.destination?.route

    Scaffold(
        containerColor = CorFundo,
        bottomBar = {
            NavigationBar(containerColor = CorCartao) {
                itensBarra.forEach { item ->
                    NavigationBarItem(
                        selected = rotaAtual == item.rota,
                        onClick = {
                            navController.navigate(item.rota) {
                                popUpTo(Rotas.HOME)
                                launchSingleTop = true
                            }
                        },
                        icon = { Text(text = item.emoji, fontSize = 20.sp) },
                        label = { Text(text = item.rotulo, fontSize = 11.sp) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedTextColor = Color.White,
                            unselectedTextColor = CorTextoSecundario,
                            indicatorColor = CorDestaque
                        )
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Rotas.HOME,
            modifier = Modifier
                .padding(innerPadding)
                .consumeWindowInsets(innerPadding)
        ) {
            composable(Rotas.HOME) {
                TelaInicio(
                    livros = livros,
                    amigos = amigos,
                    aoAdicionarLivro = { titulo -> adicionarLivro(titulo, "", 0) },
                    aoClicarLivro = { livro -> navController.navigate(Rotas.detalheLivro(livro.id)) },
                    aoVerRanking = { navController.navigate(Rotas.RANKING) }
                )
            }

            composable(Rotas.RANKING) {
                TelaRanking(
                    amigos = amigos,
                    aoClicarAmigo = { amigo -> navController.navigate(Rotas.detalheAmigo(amigo.id)) },
                    aoRemoverAmigo = { amigo -> removerAmigo(amigo) }
                )
            }

            composable(Rotas.ESTANTE) {
                TelaEstante(
                    livros = livros,
                    aoAbrirAddLivro = { navController.navigate(Rotas.ADD_LIVRO) },
                    aoClicarLivro = { livro -> navController.navigate(Rotas.detalheLivro(livro.id)) }
                )
            }

            composable(Rotas.ADD_LIVRO) {
                TelaAddLivro(
                    aoSalvar = { titulo, autor, paginas -> adicionarLivro(titulo, autor, paginas) },
                    aoVoltar = { navController.popBackStack() }
                )
            }

            composable(Rotas.AMIGOS) {
                TelaAmigos(
                    amigos = amigos,
                    aoAdicionar = { amigo -> adicionarAmigo(amigo) },
                    aoRemover = { amigo -> removerAmigo(amigo) },
                    aoAbrirDetalhe = { id -> navController.navigate(Rotas.detalheAmigo(id)) }
                )
            }

            composable(Rotas.PERFIL) {
                TelaPerfil(
                    livros = livros,
                    amigos = amigos,
                    aoVerEstante = { navController.navigate(Rotas.ESTANTE) },
                    aoVerAmigos = { navController.navigate(Rotas.AMIGOS) },
                    aoAbrirLivro = { id -> navController.navigate(Rotas.detalheLivro(id)) },
                    aoAbrirAmigo = { id -> navController.navigate(Rotas.detalheAmigo(id)) }
                )
            }

            composable(
                route = Rotas.DETALHE_AMIGO,
                arguments = listOf(navArgument("amigoId") { type = NavType.IntType })
            ) { entrada ->
                val id = entrada.arguments?.getInt("amigoId")
                TelaDetalheAmigo(
                    amigo = amigos.firstOrNull { it.id == id },
                    amigos = amigos,
                    aoVoltar = { navController.popBackStack() },
                    aoRemover = { amigo -> removerAmigo(amigo) }
                )
            }

            composable(
                route = Rotas.DETALHE_LIVRO,
                arguments = listOf(navArgument("livroId") { type = NavType.IntType })
            ) { entrada ->
                val id = entrada.arguments?.getInt("livroId") ?: -1
                TelaDetalhes(
                    livro = livros.firstOrNull { it.id == id },
                    aoAtualizar = { livro -> atualizarLivro(livro) },
                    aoRemover = { livro -> removerLivro(livro) },
                    aoAbrirAnotacoes = { navController.navigate(Rotas.anotacao(id)) },
                    aoVoltar = { navController.popBackStack() }
                )
            }

            composable(
                route = Rotas.ANOTACAO,
                arguments = listOf(navArgument("livroId") { type = NavType.IntType })
            ) { entrada ->
                val id = entrada.arguments?.getInt("livroId") ?: -1
                TelaAnotacao(
                    livro = livros.firstOrNull { it.id == id },
                    aoSalvar = { livro -> atualizarLivro(livro) },
                    aoVoltar = { navController.popBackStack() }
                )
            }
        }
    }
}