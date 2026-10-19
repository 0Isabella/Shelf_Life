package com.example.shelflife.ui.theme.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shelflife.model.Livro
import com.example.shelflife.ui.theme.CorDestaque
import com.example.shelflife.ui.theme.CorFundo
import com.example.shelflife.ui.theme.CorPlaceholder
import com.example.shelflife.ui.theme.CorTextoSecundario

@Composable
fun CapaLivro(livro: Livro, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(CorPlaceholder),
        contentAlignment = Alignment.Center
    ) {
        if (livro.imagemRes != null) {
            Image(
                painter = painterResource(id = livro.imagemRes),
                contentDescription = "Capa de ${livro.titulo}",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        } else {
            Text(
                text = livro.titulo.take(1).uppercase(),
                color = Color.White.copy(alpha = 0.7f),
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun BarraProgresso(
    progresso: Float,
    modifier: Modifier = Modifier,
    altura: Dp = 10.dp,
    corTrilho: Color = CorFundo
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(altura)
            .clip(RoundedCornerShape(altura / 2))
            .background(corTrilho)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(progresso.coerceIn(0f, 1f))
                .fillMaxHeight()
                .clip(RoundedCornerShape(altura / 2))
                .background(CorDestaque)
        )
    }
}

@Composable
fun CampoTexto(
    valor: String,
    aoMudar: (String) -> Unit,
    rotulo: String,
    modifier: Modifier = Modifier,
    numerico: Boolean = false,
    minLinhas: Int = 1,
    maxLinhas: Int = 1
) {
    OutlinedTextField(
        value = valor,
        onValueChange = aoMudar,
        label = { Text(text = rotulo) },
        singleLine = maxLinhas == 1,
        minLines = minLinhas,
        maxLines = maxLinhas,
        keyboardOptions = KeyboardOptions(
            keyboardType = if (numerico) KeyboardType.Number else KeyboardType.Text
        ),
        modifier = modifier.fillMaxWidth(),
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
}