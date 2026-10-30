package com.example.shelflife.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shelflife.data.local.LivroDao
import com.example.shelflife.model.Livro
import com.example.shelflife.ui.theme.CorCartao
import com.example.shelflife.ui.theme.CorDestaque
import com.example.shelflife.ui.theme.CorFundo
import com.example.shelflife.ui.theme.CorTextoSecundario
import com.example.shelflife.ui.components.BarraProgresso
import com.example.shelflife.ui.components.CampoTexto
import com.example.shelflife.ui.components.CapaLivro
import com.example.shelflife.util.formatarMinutos
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TelaDetalhes(
    livroId: Int,
    livroDao: LivroDao,
    aoAtualizar: (Livro) -> Unit,
    aoRemover: (Livro) -> Unit,
    aoAbrirAnotacoes: () -> Unit,
    aoVoltar: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var livro by remember { mutableStateOf<Livro?>(null) }

    LaunchedEffect(livroId) {
        withContext(Dispatchers.IO) {
            livro = livroDao.buscarPorId(livroId)
        }
    }

    var total by remember(livro?.id) {
        mutableStateOf(livro?.paginasTotal?.takeIf { it > 0 }?.toString() ?: "")
    }
    var atual by remember(livro?.id) { mutableStateOf(livro?.paginasLidas?.toString() ?: "") }
    var minutos by remember(livro?.id) { mutableStateOf("") }
    var confirmarRemocao by remember(livro?.id) { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(CorFundo)
    ) {
        TopAppBar(
            title = { Text(text = "Detalhes do livro") },
            navigationIcon = {
                IconButton(onClick = aoVoltar) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Voltar"
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = CorCartao,
                titleContentColor = Color.White,
                navigationIconContentColor = Color.White
            )
        )

        val currentLivro = livro
        if (currentLivro == null) {
            Text(
                text = "Carregando...",
                color = CorTextoSecundario,
                fontSize = 14.sp,
                modifier = Modifier.padding(16.dp)
            )
        } else {
            val progresso = if (currentLivro.paginasTotal > 0) {
                currentLivro.paginasLidas.toFloat() / currentLivro.paginasTotal
            } else {
                0f
            }

            val previsao = when {
                currentLivro.paginasTotal > 0 && currentLivro.paginasLidas >= currentLivro.paginasTotal ->
                    "Livro concluído"
                currentLivro.paginasLidas > 0 && currentLivro.minutosLidos > 0 ->
                    "Previsão para terminar: " + formatarMinutos(
                        (currentLivro.minutosLidos.toLong() * (currentLivro.paginasTotal - currentLivro.paginasLidas) /
                                currentLivro.paginasLidas).toInt()
                    )
                else -> "Registre páginas e tempo para ver a previsão de término"
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .imePadding()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(CorCartao)
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    CapaLivro(
                        livro = currentLivro,
                        modifier = Modifier
                            .width(90.dp)
                            .aspectRatio(0.67f)
                    )
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text(
                            text = currentLivro.titulo,
                            color = Color.White,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            lineHeight = 22.sp
                        )
                        Text(text = currentLivro.autor, color = CorTextoSecundario, fontSize = 13.sp)
                    }
                }

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = if (currentLivro.paginasTotal > 0)
                            "Páginas: ${currentLivro.paginasLidas} / ${currentLivro.paginasTotal} (${(progresso * 100).toInt()}%)"
                        else
                            "Páginas: não informado",
                        color = Color.White,
                        fontSize = 14.sp
                    )
                    BarraProgresso(progresso = progresso, corTrilho = CorCartao)
                    Text(
                        text = if (currentLivro.minutosLidos > 0)
                            "Tempo lido: ${formatarMinutos(currentLivro.minutosLidos)}"
                        else
                            "Tempo lido: ainda não registrado",
                        color = CorTextoSecundario,
                        fontSize = 13.sp
                    )
                    Text(text = previsao, color = CorDestaque, fontSize = 13.sp)
                }

                if (currentLivro.sinopse.isNotBlank()) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        colors = CardDefaults.cardColors(containerColor = CorCartao)
                    ) {
                        Text(
                            text = currentLivro.sinopse,
                            color = CorTextoSecundario,
                            fontSize = 12.sp,
                            lineHeight = 18.sp,
                            modifier = Modifier.padding(14.dp)
                        )
                    }
                }

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    colors = CardDefaults.cardColors(containerColor = CorCartao)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "Registrar leitura",
                            color = Color.White,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                        CampoTexto(
                            valor = total,
                            aoMudar = { total = it.filter(Char::isDigit).take(5) },
                            rotulo = "Total de páginas",
                            numerico = true
                        )
                        CampoTexto(
                            valor = atual,
                            aoMudar = { atual = it.filter(Char::isDigit).take(5) },
                            rotulo = "Página atual",
                            numerico = true
                        )
                        CampoTexto(
                            valor = minutos,
                            aoMudar = { minutos = it.filter(Char::isDigit).take(4) },
                            rotulo = "Minutos lidos nesta sessão (opcional)",
                            numerico = true
                        )
                        Button(
                            onClick = {
                                val novoTotal = total.toIntOrNull()
                                val novaPagina = atual.toIntOrNull()
                                val novosMinutos = if (minutos.isBlank()) 0 else minutos.toIntOrNull()
                                when {
                                    novoTotal == null || novoTotal <= 0 ->
                                        Toast.makeText(context, "Informe o total de páginas.", Toast.LENGTH_SHORT).show()
                                    novaPagina == null || novaPagina !in 0..novoTotal ->
                                        Toast.makeText(
                                            context,
                                            "A página atual deve estar entre 0 e $novoTotal.",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    novosMinutos == null ->
                                        Toast.makeText(context, "Minutos inválidos.", Toast.LENGTH_SHORT).show()
                                    else -> {
                                        val atualizado = currentLivro.copy(
                                            paginasTotal = novoTotal,
                                            paginasLidas = novaPagina,
                                            minutosLidos = currentLivro.minutosLidos + novosMinutos
                                        )
                                        livro = atualizado
                                        aoAtualizar(atualizado)
                                        minutos = ""
                                        Toast.makeText(context, "Progresso salvo!", Toast.LENGTH_SHORT).show()
                                    }
                                }
                            },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = CorDestaque,
                                contentColor = Color.White
                            )
                        ) {
                            Text("Salvar progresso")
                        }
                    }
                }

                Button(
                    onClick = aoAbrirAnotacoes,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = CorDestaque,
                        contentColor = Color.White
                    ),
                    contentPadding = PaddingValues(vertical = 16.dp)
                ) {
                    Text(
                        text = if (currentLivro.anotacao.isBlank()) "Anotações" else "Anotações • ver e editar",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                OutlinedButton(
                    onClick = { confirmarRemocao = true },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    Text(text = "Remover livro", color = Color.White)
                }

                Spacer(modifier = Modifier.height(16.dp))

                if (confirmarRemocao) {
                    AlertDialog(
                        onDismissRequest = { confirmarRemocao = false },
                        title = { Text("Remover livro?") },
                        text = { Text("\"${currentLivro.titulo}\" sairá da sua estante.") },
                        confirmButton = {
                            TextButton(onClick = {
                                confirmarRemocao = false
                                aoRemover(currentLivro)
                                aoVoltar()
                            }) { Text("Remover") }
                        },
                        dismissButton = {
                            TextButton(onClick = { confirmarRemocao = false }) { Text("Cancelar") }
                        }
                    )
                }
            }
        }
    }
}