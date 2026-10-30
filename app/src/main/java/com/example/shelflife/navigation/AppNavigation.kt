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
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.shelflife.data.local.AppDatabase
import com.example.shelflife.data.AmigosIniciais
import com.example.shelflife.data.LivrosIniciais
import com.example.shelflife.model.Amigo
import com.example.shelflife.model.Livro
import com.example.shelflife.ui.theme.CorCartao
import com.example.shelflife.ui.theme.CorDestaque
import com.example.shelflife.ui.theme.CorFundo
import com.example.shelflife.ui.theme.CorTextoSecundario
import com.example.shelflife.ui.screens.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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
    val coroutineScope = rememberCoroutineScope()

    val amigos = remember { mutableStateListOf<Amigo>() }
    val livros = remember { mutableStateListOf<Livro>() }

    LaunchedEffect(Unit) {
        withContext(Dispatchers.IO) {
            val livrosDoBanco = db.livroDao().buscarTodos()
            if (livrosDoBanco.isEmpty()) {
                LivrosIniciais.forEach { livro ->
                    db.livroDao().inserir(livro)
                }
                livros.addAll(db.livroDao().buscarTodos())
            } else {
                livros.addAll(livrosDoBanco)
            }

            val amigosDoBanco = db.amigoDao().buscarTodos()
            if (amigosDoBanco.isEmpty()) {
                AmigosIniciais.forEach { amigo ->
                    db.amigoDao().inserir(amigo)
                }
                amigos.addAll(db.amigoDao().buscarTodos())
            } else {
                amigos.addAll(amigosDoBanco)
            }
        }
    }

    fun adicionarAmigo(amigo: Amigo) {
        coroutineScope.launch(Dispatchers.IO) {
            if (amigos.none { it.id == amigo.id }) {
                val id = db.amigoDao().inserir(amigo)
                val amigoComId = amigo.copy(id = id.toInt())
                withContext(Dispatchers.Main) {
                    amigos.add(0, amigoComId)
                }
            }
        }
    }

    fun removerAmigo(amigo: Amigo) {
        coroutineScope.launch(Dispatchers.IO) {
            db.amigoDao().deletar(amigo)
            withContext(Dispatchers.Main) {
                amigos.removeAll { it.id == amigo.id }
            }
        }
    }

    fun adicionarLivro(titulo: String, autor: String, paginas: Int) {
        coroutineScope.launch(Dispatchers.IO) {
            val novoLivro = Livro(
                titulo = titulo,
                autor = autor.ifBlank { "Autor desconhecido" },
                paginasTotal = paginas
            )
            val id = db.livroDao().inserir(novoLivro)
            val livroComId = novoLivro.copy(id = id.toInt())
            withContext(Dispatchers.Main) {
                livros.add(0, livroComId)
            }
        }
    }

    fun removerLivro(livro: Livro) {
        coroutineScope.launch(Dispatchers.IO) {
            db.livroDao().deletar(livro)
            withContext(Dispatchers.Main) {
                livros.removeAll { it.id == livro.id }
            }
        }
    }

    fun atualizarLivro(livro: Livro) {
        coroutineScope.launch(Dispatchers.IO) {
            db.livroDao().atualizar(livro)
            withContext(Dispatchers.Main) {
                val indice = livros.indexOfFirst { it.id == livro.id }
                if (indice != -1) livros[indice] = livro
            }
        }
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
                val id = entrada.arguments?.getInt("amigoId") ?: -1
                TelaDetalheAmigo(
                    amigoId = id,
                    amigoDao = db.amigoDao(),
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
                    livroId = id,
                    livroDao = db.livroDao(),
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
                    livro = livros.firstOrNull { id == it.id },
                    aoSalvar = { livro -> atualizarLivro(livro) },
                    aoVoltar = { navController.popBackStack() }
                )
            }
        }
    }
}