package com.example.shelflife.ui.theme.screens

import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shelflife.R
import com.example.shelflife.data.LivrosIniciais
import com.example.shelflife.model.Amigo
import com.example.shelflife.model.Livro
import com.example.shelflife.ui.theme.CorCartao
import com.example.shelflife.ui.theme.CorDestaque
import com.example.shelflife.ui.theme.CorFundo
import com.example.shelflife.ui.theme.CorTextoSecundario
import com.example.shelflife.ui.theme.components.CampoTexto
import com.example.shelflife.ui.theme.components.CapaLivro
import com.example.shelflife.util.NOME_USUARIO
import com.example.shelflife.util.formatarMinutos

@Composable
fun TelaInicio(
    livros: List<Livro>,
    amigos: List<Amigo>,
    aoAdicionarLivro: (String) -> Unit,
    aoClicarLivro: (Livro) -> Unit,
    aoVerRanking: () -> Unit,
    modifier: Modifier = Modifier
) {
    val minutosTotais = livros.sumOf { it.minutosLidos }
    val ranking = amigos.sortedByDescending { it.minutosLidos }.take(4)

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(CorFundo)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        CartaoResumoUsuario(
            nome = NOME_USUARIO,
            resumo = "Total lido: ${formatarMinutos(minutosTotais)}",
            imagemRes = R.drawable.usuario
        )

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Column(modifier = Modifier.weight(1.4f)) {
                Text(
                    text = "Livros Principais",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                GradeLivrosPrincipais(livros = livros.take(6), aoClicarLivro = aoClicarLivro)

                Spacer(modifier = Modifier.height(12.dp))

                BotaoAdicionarLivro(aoAdicionar = aoAdicionarLivro)
            }

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Ranking",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                PreviaRanking(ranking = ranking, aoClicar = aoVerRanking)
            }
        }
    }
}

@Composable
private fun CartaoResumoUsuario(
    nome: String,
    resumo: String,
    @DrawableRes imagemRes: Int? = null,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(CorCartao)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Box(
            modifier = Modifier
                .size(56.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(Color.DarkGray),
            contentAlignment = Alignment.Center
        ) {
            if (imagemRes != null) {
                Image(
                    painter = painterResource(id = imagemRes),
                    contentDescription = "Foto de perfil",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            } else {
                Text(text = "🙂", fontSize = 26.sp)
            }
        }
        Column {
            Text(text = nome, color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = resumo, color = CorTextoSecundario, fontSize = 13.sp)
        }
    }
}

@Composable
private fun GradeLivrosPrincipais(livros: List<Livro>, aoClicarLivro: (Livro) -> Unit) {
    LazyHorizontalGrid(
        rows = GridCells.Fixed(2),
        modifier = Modifier
            .fillMaxWidth()
            .height(220.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(CorCartao)
            .padding(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(40.dp)
    ) {
        items(livros, key = { it.id }) { livro ->
            CapaLivro(
                livro = livro,
                modifier = Modifier
                    .width(80.dp)
                    .aspectRatio(0.72f)
                    .clickable { aoClicarLivro(livro) }
            )
        }
    }
}

@Composable
private fun BotaoAdicionarLivro(aoAdicionar: (String) -> Unit) {
    val context = LocalContext.current
    var novoLivro by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(6.dp))
            .background(CorCartao)
            .padding(16.dp),
    ) {
        CampoTexto(
            valor = novoLivro,
            aoMudar = { novoLivro = it },
            rotulo = "Nome do livro"
        )
        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {
                if (novoLivro.isNotBlank()) {
                    aoAdicionar(novoLivro.trim())
                    Toast.makeText(
                        context,
                        "Livro \"${novoLivro.trim()}\" adicionado!",
                        Toast.LENGTH_SHORT
                    ).show()
                    novoLivro = ""
                } else {
                    Toast.makeText(context, "Digite o nome de um livro.", Toast.LENGTH_SHORT).show()
                }
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = CorDestaque,
                contentColor = Color.White
            ),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Adicionar livro")
        }
    }
}

@Composable
private fun PreviaRanking(ranking: List<Amigo>, aoClicar: () -> Unit) {
    val coresPosicao = listOf(
        Color(0xFF2E2E2E),
        Color(0xFFC49A93),
        Color(0xFF8B3A2B),
        Color(0xFF7A5C55)
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(CorCartao)
            .clickable(onClick = aoClicar)
            .padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        if (ranking.isEmpty()) {
            Text(
                text = "Adicione amigos para ver o ranking",
                color = CorTextoSecundario,
                fontSize = 12.sp
            )
        }
        ranking.forEachIndexed { indice, amigo ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .clip(RoundedCornerShape(10.dp))
                    .background(coresPosicao.getOrElse(indice) { CorCartao }),
                contentAlignment = Alignment.Center
            ) {
                if (amigo.imagemRes != null) {
                    Image(
                        painter = painterResource(id = amigo.imagemRes),
                        contentDescription = "Foto de ${amigo.nome}",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(10.dp))
                    )
                } else {
                    Text(text = amigo.emoji, fontSize = 26.sp)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviaTelaInicio() {
    TelaInicio(
        livros = LivrosIniciais,
        amigos = listOf(
            Amigo(0, "Ovin", 5400, "🐣", R.drawable.amigo_0),
            Amigo(1, "Cogumelito", 8550, "🍄", R.drawable.amigo_1)
        ),
        aoAdicionarLivro = {},
        aoClicarLivro = {},
        aoVerRanking = {}
    )
}