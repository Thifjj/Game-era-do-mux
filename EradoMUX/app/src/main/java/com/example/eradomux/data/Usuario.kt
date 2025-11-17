package com.example.eradomux.data

data class Usuario(
    val id: String = "", // ID do documento no Firestore
    val nome: String = "",
    val senha: String = "",
    val nivel: Int = 1,
    val professor : Boolean = false,
)