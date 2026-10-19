package com.example.shelflife.ui.theme.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shelflife.data.LivrosIniciais
import com.example.shelflife.model.Livro
import com.example.shelflife.ui.theme.CorCartao
import com.example.shelflife.ui.theme.CorDestaque
import com.example.shelflife.ui.theme.CorFundo
import com.example.shelflife.ui.theme.CorTextoSecundario
import com.example.shelflife.ui.theme.components.BarraProgresso
import com.example.shelflife.ui.theme.components.CapaLivro

@Composable
fun TelaEstante(
    livros: List<Livro>,
    aoAbrirAddLivro: () -> Unit,
    aoClicarLivro: (Livro) -> Unit,
    modifier: Modifier = Modifier
) {
    var busca by remember { mutableStateOf("") }

    val termo = busca.trim()
    val livrosFiltrados = if (termo.isEmpty()) {
        livros
    } else {
        livros.filter {
            it.titulo.contains(termo, ignoreCase = true) ||
                    it.autor.contains(termo, ignoreCase = true)
        }
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(CorFundo)
            .imePadding(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        item {
            Column {
                Text(
                    text = "Estante",
                    color = Color.White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Toque num livro para ver os detalhes",
                    color = CorTextoSecundario,
                    fontSize = 13.sp
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = aoAbrirAddLivro,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = CorDestaque,
                        contentColor = Color.White
                    )
                ) {
                    Text("Adicionar livro")
                }
                Spacer(modifier = Modifier.height(12.dp))
                OutlinedTextField(
                    value = busca,
                    onValueChange = { if (it.length <= 30) busca = it },
                    label = { Text(text = "Buscar na estante") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = null,
                            tint = Color.White
                        )
                    },
                    trailingIcon = {
                        if (busca.isNotEmpty()) {
                            IconButton(onClick = { busca = "" }) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Limpar busca",
                                    tint = Color.White
                                )
                            }
                        }
                    },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = CorDestaque,
                        unfocusedBorderColor = CorTextoSecundario,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedLabelColor = CorDestaque,
                        unfocusedLabelColor = CorTextoSecundario,
                        cursorColor = CorDestaque
                    )
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Seus livros (${livrosFiltrados.size})",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        if (livros.isEmpty()) {
            item {
                Text(
                    text = "Sua estante está vazia.",
                    color = CorTextoSecundario,
                    fontSize = 14.sp
                )
            }
        } else if (livrosFiltrados.isEmpty()) {
            item {
                Text(
                    text = "Nenhum livro encontrado para \"$termo\".",
                    color = CorTextoSecundario,
                    fontSize = 14.sp
                )
            }
        } else {
            items(livrosFiltrados, key = { it.id }) { livro ->
                ItemLivroEstante(livro = livro, aoClicar = { aoClicarLivro(livro) })
            }
        }
    }
}

@Composable
private fun ItemLivroEstante(livro: Livro, aoClicar: () -> Unit) {
    val progresso = if (livro.paginasTotal > 0) {
        livro.paginasLidas.toFloat() / livro.paginasTotal
    } else {
        0f
    }

    Card(
        onClick = aoClicar,
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = CorCartao)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            CapaLivro(
                livro = livro,
                modifier = Modifier
                    .width(56.dp)
                    .aspectRatio(0.67f)
            )

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = livro.titulo,
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Text(text = livro.autor, color = CorTextoSecundario, fontSize = 13.sp)
                if (livro.paginasTotal > 0) {
                    BarraProgresso(progresso = progresso, altura = 6.dp)
                    Text(
                        text = "${(progresso * 100).toInt()}% • ${livro.paginasLidas}/${livro.paginasTotal} páginas",
                        color = CorTextoSecundario,
                        fontSize = 12.sp
                    )
                } else {
                    Text(
                        text = "Páginas não informadas",
                        color = CorTextoSecundario,
                        fontSize = 12.sp
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviaTelaEstante() {
    TelaEstante(
        livros = LivrosIniciais,
        aoAbrirAddLivro = {},
        aoClicarLivro = {}
    )
}