package com.example.shelflife.util

const val NOME_USUARIO = "Leitor"
const val MINUTOS_POR_NIVEL = 600

fun formatarMinutos(minutos: Int): String = "${minutos / 60}h ${minutos % 60}min"