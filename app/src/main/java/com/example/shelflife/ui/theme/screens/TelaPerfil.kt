package com.example.shelflife.ui.theme.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shelflife.R
import com.example.shelflife.data.LivrosIniciais
import com.example.shelflife.model.Amigo
import com.example.shelflife.model.Livro
import com.example.shelflife.ui.theme.CorCartao
import com.example.shelflife.ui.theme.CorFundo
import com.example.shelflife.ui.theme.CorTextoSecundario
import com.example.shelflife.ui.theme.components.BarraProgresso
import com.example.shelflife.ui.theme.components.CapaLivro
import com.example.shelflife.util.MINUTOS_POR_NIVEL
import com.example.shelflife.util.NOME_USUARIO
import com.example.shelflife.util.formatarMinutos

@Composable
fun TelaPerfil(
    livros: List<Livro>,
    amigos: List<Amigo>,
    aoVerEstante: () -> Unit,
    aoVerAmigos: () -> Unit,
    aoAbrirLivro: (Int) -> Unit,
    aoAbrirAmigo: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val minutosTotais = livros.sumOf { it.minutosLidos }
    val maisLidos = livros
        .filter { it.minutosLidos > 0 }
        .sortedByDescending { it.minutosLidos }
        .take(5)
    val nivel = minutosTotais / MINUTOS_POR_NIVEL + 1
    val minutosNoNivel = minutosTotais % MINUTOS_POR_NIVEL
    val amigosSuperados = amigos.count { it.minutosLidos < minutosTotais }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(CorFundo)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
                .background(Color.DarkGray),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.usuario),
                contentDescription = "Foto de perfil",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = NOME_USUARIO,
            color = Color.White,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Tempo total lendo livros",
            color = CorTextoSecundario,
            fontSize = 13.sp
        )
        Text(
            text = formatarMinutos(minutosTotais),
            color = Color.White,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(24.dp))

        CabecalhoSecao(titulo = "Livros mais lidos", aoClicar = aoVerEstante)
        Spacer(modifier = Modifier.height(8.dp))
        if (maisLidos.isEmpty()) {
            Text(
                text = "Registre sua leitura nos detalhes de um livro para ele aparecer aqui.",
                color = CorTextoSecundario,
                fontSize = 13.sp,
                modifier = Modifier.fillMaxWidth()
            )
        } else {
            LazyRow(modifier = Modifier.fillMaxWidth()) {
                items(maisLidos, key = { it.id }) { livro ->
                    ItemLivro(livro = livro, aoClicar = { aoAbrirLivro(livro.id) })
                    Spacer(modifier = Modifier.width(12.dp))
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Nível: $nivel",
            color = Color.White,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))
        BarraProgresso(
            progresso = minutosNoNivel.toFloat() / MINUTOS_POR_NIVEL,
            altura = 20.dp,
            corTrilho = CorCartao
        )
        Text(
            text = "Faltam ${formatarMinutos(MINUTOS_POR_NIVEL - minutosNoNivel)} para o nível ${nivel + 1}",
            color = CorTextoSecundario,
            fontSize = 12.sp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        CabecalhoSecao(titulo = "Amigos", aoClicar = aoVerAmigos)
        Text(
            text = if (amigos.isEmpty())
                "Adicione amigos na aba Amigos para comparar sua leitura."
            else
                "Você leu mais que $amigosSuperados dos seus ${amigos.size} amigos",
            color = CorTextoSecundario,
            fontSize = 13.sp,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(12.dp))
        LazyRow(modifier = Modifier.fillMaxWidth()) {
            items(amigos, key = { it.id }) { amigo ->
                ItemAmigo(amigo = amigo, aoClicar = { aoAbrirAmigo(amigo.id) })
                Spacer(modifier = Modifier.width(12.dp))
            }
        }
    }
}

@Composable
fun CabecalhoSecao(titulo: String, aoClicar: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = titulo, color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
        IconButton(onClick = aoClicar) {
            Text(text = "→", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun ItemLivro(livro: Livro, aoClicar: () -> Unit) {
    Card(
        onClick = aoClicar,
        modifier = Modifier.width(90.dp),
        colors = CardDefaults.cardColors(containerColor = CorCartao)
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CapaLivro(
                livro = livro,
                modifier = Modifier.size(width = 74.dp, height = 100.dp)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = livro.titulo,
                color = Color.White,
                fontSize = 11.sp,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun ItemAmigo(amigo: Amigo, aoClicar: () -> Unit) {
    Card(
        onClick = aoClicar,
        modifier = Modifier.width(80.dp),
        colors = CardDefaults.cardColors(containerColor = CorCartao)
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape)
                    .background(Color.DarkGray),
                contentAlignment = Alignment.Center
            ) {
                if (amigo.imagemRes != null) {
                    Image(
                        painter = painterResource(id = amigo.imagemRes),
                        contentDescription = "Foto de ${amigo.nome}",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    Text(text = amigo.emoji, fontSize = 24.sp)
                }
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = amigo.nome, color = Color.White, fontSize = 12.sp)
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviaTelaPerfil() {
    TelaPerfil(
        livros = LivrosIniciais,
        amigos = listOf(
            Amigo(0, "Ovin", 5400, "🐣", R.drawable.amigo_0),
            Amigo(1, "Cogumelito", 8550, "🍄", R.drawable.amigo_1),
            Amigo(2, "Sapinho", 3000, "🐸", R.drawable.amigo_2)
        ),
        aoVerEstante = {},
        aoVerAmigos = {},
        aoAbrirLivro = {},
        aoAbrirAmigo = {}
    )
}