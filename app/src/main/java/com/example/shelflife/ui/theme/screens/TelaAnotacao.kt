package com.example.shelflife.ui.theme.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shelflife.data.LivrosIniciais
import com.example.shelflife.model.Livro
import com.example.shelflife.ui.theme.CorCartao
import com.example.shelflife.ui.theme.CorDestaque
import com.example.shelflife.ui.theme.CorFundo
import com.example.shelflife.ui.theme.CorTextoSecundario
import com.example.shelflife.ui.theme.components.CampoTexto

private const val LIMITE_ANOTACAO = 1000

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TelaAnotacao(
    livro: Livro?,
    aoSalvar: (Livro) -> Unit,
    aoVoltar: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var texto by remember(livro?.id) { mutableStateOf(livro?.anotacao ?: "") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(CorFundo)
    ) {
        TopAppBar(
            title = { Text(text = "Anotações") },
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

        if (livro == null) {
            Text(
                text = "Livro não encontrado.",
                color = CorTextoSecundario,
                fontSize = 14.sp,
                modifier = Modifier.padding(16.dp)
            )
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .imePadding()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = livro.titulo,
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Escreva o que quiser lembrar sobre este livro.",
                    color = CorTextoSecundario,
                    fontSize = 13.sp
                )
                CampoTexto(
                    valor = texto,
                    aoMudar = { if (it.length <= LIMITE_ANOTACAO) texto = it },
                    rotulo = "Sua anotação",
                    minLinhas = 8,
                    maxLinhas = 14
                )
                Text(
                    text = "${texto.length}/$LIMITE_ANOTACAO",
                    color = CorTextoSecundario,
                    fontSize = 12.sp
                )
                Button(
                    onClick = {
                        aoSalvar(livro.copy(anotacao = texto.trim()))
                        Toast.makeText(context, "Anotação salva!", Toast.LENGTH_SHORT).show()
                        aoVoltar()
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = CorDestaque,
                        contentColor = Color.White
                    )
                ) {
                    Text("Salvar anotação")
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviaTelaAnotacao() {
    TelaAnotacao(livro = LivrosIniciais.first(), aoSalvar = {}, aoVoltar = {})
}