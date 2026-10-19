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
import com.example.shelflife.ui.theme.CorCartao
import com.example.shelflife.ui.theme.CorDestaque
import com.example.shelflife.ui.theme.CorFundo
import com.example.shelflife.ui.theme.components.CampoTexto

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TelaAddLivro(
    aoSalvar: (titulo: String, autor: String, paginas: Int) -> Unit,
    aoVoltar: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var titulo by remember { mutableStateOf("") }
    var autor by remember { mutableStateOf("") }
    var paginas by remember { mutableStateOf("") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(CorFundo)
    ) {
        TopAppBar(
            title = { Text(text = "Adicionar livro") },
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

        Column(
            modifier = Modifier
                .fillMaxSize()
                .imePadding()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Spacer(modifier = Modifier.height(4.dp))

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
                        text = "Dados do livro",
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                    CampoTexto(valor = titulo, aoMudar = { titulo = it }, rotulo = "Título")
                    CampoTexto(valor = autor, aoMudar = { autor = it }, rotulo = "Autor (opcional)")
                    CampoTexto(
                        valor = paginas,
                        aoMudar = { paginas = it.filter(Char::isDigit).take(5) },
                        rotulo = "Total de páginas (opcional)",
                        numerico = true
                    )
                    Button(
                        onClick = {
                            if (titulo.isBlank()) {
                                Toast.makeText(context, "Digite o título do livro.", Toast.LENGTH_SHORT).show()
                            } else {
                                aoSalvar(titulo.trim(), autor.trim(), paginas.toIntOrNull() ?: 0)
                                Toast.makeText(
                                    context,
                                    "Livro \"${titulo.trim()}\" adicionado!",
                                    Toast.LENGTH_SHORT
                                ).show()
                                aoVoltar()
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = CorDestaque,
                            contentColor = Color.White
                        )
                    ) {
                        Text("Adicionar livro")
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviaTelaAddLivro() {
    TelaAddLivro(
        aoSalvar = { _, _, _ -> },
        aoVoltar = {}
    )
}