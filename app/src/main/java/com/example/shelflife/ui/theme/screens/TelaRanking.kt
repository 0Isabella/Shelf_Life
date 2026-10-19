package com.example.shelflife.ui.theme.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shelflife.R
import com.example.shelflife.model.Amigo
import com.example.shelflife.ui.theme.CorCartao
import com.example.shelflife.ui.theme.CorDestaque
import com.example.shelflife.ui.theme.CorFundo
import com.example.shelflife.ui.theme.CorTextoSecundario

enum class CriterioRanking(val rotulo: String) {
    TEMPO("Tempo"),
    LIVROS("Livros"),
    SEQUENCIA("Sequência")
}

@Composable
fun TelaRanking(
    amigos: List<Amigo>,
    aoClicarAmigo: (Amigo) -> Unit,
    aoRemoverAmigo: (Amigo) -> Unit,
    modifier: Modifier = Modifier
) {
    var criterio by remember { mutableStateOf(CriterioRanking.TEMPO) }
    var busca by remember { mutableStateOf("") }
    var amigoParaRemover by remember { mutableStateOf<Amigo?>(null) }

    val ordenados = when (criterio) {
        CriterioRanking.TEMPO -> amigos.sortedByDescending { it.minutosLidos }
        CriterioRanking.LIVROS -> amigos.sortedByDescending { it.livrosLidos }
        CriterioRanking.SEQUENCIA -> amigos.sortedByDescending { it.sequenciaDias }
    }

    val visiveis = ordenados
        .withIndex()
        .filter { it.value.nome.contains(busca.trim(), ignoreCase = true) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(CorFundo)
            .padding(16.dp)
    ) {
        Text(
            text = "Ranking",
            color = Color.White,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Quem mais leu entre seus amigos",
            color = CorTextoSecundario,
            fontSize = 13.sp
        )
        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = busca,
            onValueChange = { busca = it },
            label = { Text(text = "Buscar amigo", color = Color.White) },
            leadingIcon = { Text(text = "🔍") },
            trailingIcon = {
                if (busca.isNotEmpty()) {
                    IconButton(onClick = { busca = "" }) {
                        Text(text = "✕", color = Color.White, fontSize = 16.sp)
                    }
                }
            },
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = CorDestaque,
                unfocusedBorderColor = CorTextoSecundario,
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White
            ),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            CriterioRanking.entries.forEach { opcao ->
                FilterChip(
                    selected = criterio == opcao,
                    onClick = { criterio = opcao },
                    label = { Text(text = opcao.rotulo) },
                    colors = FilterChipDefaults.filterChipColors(
                        containerColor = CorCartao,
                        labelColor = Color.White,
                        selectedContainerColor = CorDestaque,
                        selectedLabelColor = Color.White
                    )
                )
            }
        }
        Spacer(modifier = Modifier.height(12.dp))

        if (visiveis.isEmpty()) {
            Text(
                text = if (amigos.isEmpty())
                    "Você ainda não tem amigos. Adicione alguém na aba Amigos."
                else
                    "Nenhum amigo encontrado.",
                color = CorTextoSecundario,
                fontSize = 14.sp,
                modifier = Modifier.padding(top = 16.dp)
            )
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                items(visiveis, key = { it.value.id }) { item ->
                    ItemRanking(
                        posicao = item.index + 1,
                        amigo = item.value,
                        criterio = criterio,
                        aoClicar = { aoClicarAmigo(item.value) },
                        aoClicarLongo = { amigoParaRemover = item.value }
                    )
                }
            }
        }
    }

    amigoParaRemover?.let { amigo ->
        AlertDialog(
            onDismissRequest = { amigoParaRemover = null },
            title = { Text("Remover amigo?") },
            text = { Text("${amigo.nome} sairá do seu ranking.") },
            confirmButton = {
                TextButton(onClick = {
                    aoRemoverAmigo(amigo)
                    amigoParaRemover = null
                }) { Text("Remover") }
            },
            dismissButton = {
                TextButton(onClick = { amigoParaRemover = null }) { Text("Cancelar") }
            }
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun ItemRanking(
    posicao: Int,
    amigo: Amigo,
    criterio: CriterioRanking,
    aoClicar: () -> Unit,
    aoClicarLongo: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .combinedClickable(onClick = aoClicar, onLongClick = aoClicarLongo),
        colors = CardDefaults.cardColors(containerColor = CorCartao)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = if (posicao == 1) "👑" else "$posicao",
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.width(32.dp)
            )

            Box(
                modifier = Modifier
                    .size(48.dp)
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
                    Text(text = amigo.emoji, fontSize = 22.sp)
                }
            }

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = amigo.nome,
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = valorDoCriterio(amigo, criterio),
                    color = CorTextoSecundario,
                    fontSize = 13.sp
                )
            }
        }
    }
}

private fun valorDoCriterio(amigo: Amigo, criterio: CriterioRanking): String =
    when (criterio) {
        CriterioRanking.TEMPO -> formatarTempo(amigo.minutosLidos)
        CriterioRanking.LIVROS ->
            if (amigo.livrosLidos == 1) "1 livro" else "${amigo.livrosLidos} livros"
        CriterioRanking.SEQUENCIA ->
            if (amigo.sequenciaDias == 1) "1 dia seguido" else "${amigo.sequenciaDias} dias seguidos"
    }

private fun formatarTempo(minutos: Int): String = "${minutos / 60}h ${minutos % 60}min"

@Preview(showBackground = true)
@Composable
private fun PreviaTelaRanking() {
    TelaRanking(
        amigos = listOf(
            Amigo(0, "Ovin", 5400, imagemRes = R.drawable.amigo_0, livrosLidos = 12, sequenciaDias = 4),
            Amigo(1, "Cogumelito", 8550, imagemRes = R.drawable.amigo_1, livrosLidos = 7, sequenciaDias = 21),
            Amigo(2, "Sapinho", 3000, imagemRes = R.drawable.amigo_2, livrosLidos = 15, sequenciaDias = 1),
            Amigo(3, "Nome", 1200, imagemRes = R.drawable.amigo_3, livrosLidos = 3, sequenciaDias = 9)
        ),
        aoClicarAmigo = {},
        aoRemoverAmigo = {}
    )
}