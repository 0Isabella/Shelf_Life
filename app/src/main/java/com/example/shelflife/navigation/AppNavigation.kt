package com.example.shelflife.navigation

import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.shelflife.data.local.AppDatabase
import com.example.shelflife.data.repository.AmigoRepository
import com.example.shelflife.data.repository.LivroRepository
import com.example.shelflife.ui.theme.CorCartao
import com.example.shelflife.ui.theme.CorDestaque
import com.example.shelflife.ui.theme.CorFundo
import com.example.shelflife.ui.theme.CorTextoSecundario
import com.example.shelflife.ui.screens.*
import com.example.shelflife.viewmodel.AmigosViewModel
import com.example.shelflife.viewmodel.AmigosViewModelFactory
import com.example.shelflife.viewmodel.LivrosViewModel
import com.example.shelflife.viewmodel.LivrosViewModelFactory

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

    val db = remember { AppDatabase.obterInstancia(context) }
    val livrosViewModel: LivrosViewModel = viewModel(
        factory = LivrosViewModelFactory(LivroRepository(db.livroDao()))
    )
    val amigosViewModel: AmigosViewModel = viewModel(
        factory = AmigosViewModelFactory(AmigoRepository(db.amigoDao()))
    )

    val livrosUiState by livrosViewModel.uiState.collectAsState()
    val amigosUiState by amigosViewModel.uiState.collectAsState()
    val livros = livrosUiState.livros
    val amigos = amigosUiState.amigos

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
                    aoAdicionarLivro = { titulo -> livrosViewModel.adicionarLivro(titulo, "", 0) },
                    aoClicarLivro = { livro -> navController.navigate(Rotas.detalheLivro(livro.id)) },
                    aoVerRanking = { navController.navigate(Rotas.RANKING) },
                    aoAbrirPerfil = { navController.navigate(Rotas.PERFIL) }
                )
            }

            composable(Rotas.RANKING) {
                TelaRanking(
                    amigos = amigos,
                    aoClicarAmigo = { amigo -> navController.navigate(Rotas.detalheAmigo(amigo.id)) },
                    aoRemoverAmigo = { amigo -> amigosViewModel.removerAmigo(amigo) }
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
                    aoSalvar = { titulo, autor, paginas -> livrosViewModel.adicionarLivro(titulo, autor, paginas) },
                    aoVoltar = { navController.popBackStack() }
                )
            }

            composable(Rotas.AMIGOS) {
                TelaAmigos(
                    amigos = amigos,
                    aoAdicionar = { amigo -> amigosViewModel.adicionarAmigo(amigo) },
                    aoRemover = { amigo -> amigosViewModel.removerAmigo(amigo) },
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
                val id = entrada.arguments?.getInt("amigoId") ?: -1
                TelaDetalheAmigo(
                    amigo = amigos.firstOrNull { it.id == id },
                    todosAmigos = amigos,
                    aoAtualizar = { amigo -> amigosViewModel.atualizarAmigo(amigo) },
                    aoVoltar = { navController.popBackStack() },
                    aoRemover = { amigo -> amigosViewModel.removerAmigo(amigo) }
                )
            }

            composable(
                route = Rotas.DETALHE_LIVRO,
                arguments = listOf(navArgument("livroId") { type = NavType.IntType })
            ) { entrada ->
                val id = entrada.arguments?.getInt("livroId") ?: -1
                TelaDetalhes(
                    livro = livros.firstOrNull { it.id == id },
                    aoAtualizar = { livro -> livrosViewModel.atualizarLivro(livro) },
                    aoRemover = { livro -> livrosViewModel.removerLivro(livro) },
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
                    livro = livros.firstOrNull { id == it.id },
                    aoSalvar = { livro -> livrosViewModel.atualizarLivro(livro) },
                    aoVoltar = { navController.popBackStack() }
                )
            }
        }
    }
}