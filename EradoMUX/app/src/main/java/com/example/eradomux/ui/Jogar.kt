package com.example.eradomux.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.eradomux.R
import com.example.eradomux.ui.CorDourado
import com.example.eradomux.ui.CorVermelhoBotao
import com.example.eradomux.ui.CorVermelhoEscuro

// Modelo simples para representar uma fase
data class Fase(
    val numero: Int,
    val nome: String,
    val estrelasGanhas: Int, // 0 a 3
    val isBloqueada: Boolean = false
)

@Composable
fun TelaSelecaoFases(
    onVoltarClick: () -> Unit,
    onFaseClick: (Int) -> Unit
) {
    // Dados fictícios das 4 fases
    val listaFases = listOf(
        Fase(1, "O Início", 3),          // Completou tudo (3 estrelas)
        Fase(2, "Floresta Negra", 2),    // Foi bem (2 estrelas)
        Fase(3, "Caverna do Mux", 0),    // Ainda não jogou (0 estrelas)
        Fase(4, "Castelo Final", 0, true) // Bloqueada (exemplo)
    )

    Box(modifier = Modifier.fillMaxSize()) {
        // 1. Fundo
        Image(
            painter = painterResource(id = R.drawable.fundojogar), // Use o mesmo fundo do menu
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        // Sombra escura
        Box(modifier = Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.5f)))

        // 2. Conteúdo
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // --- CABEÇALHO ---
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Botão Voltar
                TextButton(onClick = onVoltarClick) {
                    Text("< Voltar", color = Color.White, fontFamily = FontFamily.Serif)
                }

                Spacer(modifier = Modifier.weight(1f))

                // Título
                Text(
                    text = "Mapa do Reino",
                    color = CorDourado,
                    fontSize = 28.sp,
                    fontFamily = FontFamily.Serif,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.weight(1f))
                Spacer(modifier = Modifier.width(40.dp)) // Espaço para equilibrar o botão voltar
            }

            Spacer(modifier = Modifier.height(24.dp))

            // --- GRID DE FASES ---
            LazyVerticalGrid(
                columns = GridCells.Fixed(2), // 2 Colunas
                contentPadding = PaddingValues(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(listaFases) { fase ->
                    CardFase(fase = fase, onClick = { onFaseClick(fase.numero) })
                }
            }
        }
    }
}

@Composable
fun CardFase(fase: Fase, onClick: () -> Unit) {
    // Se estiver bloqueada, fica mais escura e cinza
    val corFundo = if (fase.isBloqueada) Color.DarkGray else CorVermelhoBotao.copy(alpha = 0.9f)
    val corBorda = if (fase.isBloqueada) Color.Gray else CorDourado

    Box(
        modifier = Modifier
            .height(140.dp) // Altura do cartão
            .background(corFundo, RoundedCornerShape(12.dp))
            .border(2.dp, corBorda, RoundedCornerShape(12.dp))
            .clickable(enabled = !fase.isBloqueada) { onClick() }
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Título da Fase
            Text(
                text = "Fase ${fase.numero}",
                color = if (fase.isBloqueada) Color.Gray else CorDourado,
                fontFamily = FontFamily.Serif,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = if (fase.isBloqueada) "Bloqueado" else fase.nome,
                color = Color.White.copy(alpha = 0.8f),
                fontFamily = FontFamily.Serif,
                fontSize = 14.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            // --- SISTEMA DE ESTRELAS ---
            if (!fase.isBloqueada) {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Cria 3 estrelas
                    repeat(3) { index ->
                        // Se o índice for menor que as estrelas ganhas, pinta de Dourado. Senão, Preto/Cinza
                        val corEstrela = if (index < fase.estrelasGanhas) CorDourado else Color.Black.copy(alpha = 0.5f)

                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            tint = corEstrela,
                            modifier = Modifier
                                .size(32.dp)
                                .padding(horizontal = 2.dp)
                        )
                    }
                }
            }
        }
    }
}