package com.example.eradomux.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.eradomux.R

@Composable
fun TelaConquistas(
    nomeJogador: String,
    avatarId: Int, // <--- NOVO PARAMETRO
    onVoltarClick: () -> Unit
) {
    val listaAvatares = listOf(
        R.drawable.imgperfil, R.drawable.imgperfil2, R.drawable.imgperfil3, R.drawable.imgperfil4
    )
    val imagemAvatarExibicao = listaAvatares.getOrElse(avatarId) { R.drawable.imgperfil }
    // Lista de textos baseada na sua imagem
    val listaConquistas = listOf(
        "Caçar 5 ou mais Javalis.",
        "Coletar 5 ou mais Frutas dos Arbustos.",
        "Concluir todas as Fases.",
        "Coletar todas as Frutas de todas as fases.",
        "Concluir 5 fases seguidas sem perder seu Aldeão."
    )

    Box(modifier = Modifier.fillMaxSize()) {

        // 1. FUNDO
        Image(
            painter = painterResource(id = R.drawable.fundomenu),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        // Camada escura
        Box(modifier = Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.3f)))

        // Logo no Papel (Canto Inferior Direito)
        Box(
            modifier = Modifier.align(Alignment.BottomEnd),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.retangulo_papel),
                contentDescription = null,
                contentScale = ContentScale.FillBounds,
                modifier = Modifier.width(220.dp).height(80.dp)
            )
            Image(
                painter = painterResource(id = R.drawable.logounivali),
                contentDescription = "Univali",
                modifier = Modifier.height(70.dp).width(220.dp),
                contentScale = ContentScale.Fit
            )
        }

        // 2. CONTEÚDO PRINCIPAL
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            // --- COLUNA DA ESQUERDA (PERFIL) - Igual ao Estatísticas ---
            Column(
                modifier = Modifier
                    .weight(0.3f)
                    .fillMaxHeight(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // AVATAR GRANDE
                Image(
                    painter = painterResource(id = imagemAvatarExibicao),
                    contentDescription = "Avatar Grande",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(140.dp)
                        .clip(CircleShape)
                        .border(3.dp, CorDourado, CircleShape)
                        .background(Color.Gray)
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Nome do Usuário
                Box(
                    modifier = Modifier
                        .background(CorVermelhoEscuro, RoundedCornerShape(4.dp))
                        .border(1.dp, CorDourado, RoundedCornerShape(4.dp))
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = nomeJogador,
                        color = CorDourado,
                        fontFamily = FontFamily.Serif,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                }
            }

            // --- COLUNA DA DIREITA (LISTA DE CONQUISTAS) ---
            Box(
                modifier = Modifier
                    .weight(0.7f)
                    .fillMaxHeight(0.85f)
                    .background(Color.Black.copy(alpha = 0.4f)) // Fundo translúcido
                    .border(1.dp, CorDourado.copy(alpha = 0.5f)) // Borda fina amarela
                    .padding(16.dp)
            ) {
                LazyColumn(
                    verticalArrangement = Arrangement.Center, // Centraliza verticalmente se tiver poucos itens
                    modifier = Modifier.fillMaxSize()
                ) {
                    itemsIndexed(listaConquistas) { index, textoConquista ->

                        // O Texto da Conquista
                        Text(
                            text = textoConquista,
                            color = Color.White,
                            fontFamily = FontFamily.Serif,
                            fontSize = 18.sp,
                            textAlign = TextAlign.Center, // Centralizado como na imagem
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 12.dp)
                        )

                        // Linha divisória (menos no último item)
                        if (index < listaConquistas.size - 1) {
                            HorizontalDivider(color = CorDourado.copy(alpha = 0.3f), thickness = 1.dp)
                        }
                    }
                }
            }
        }

        // 3. ELEMENTOS FLUTUANTES

        // Botão Voltar (Topo Esquerdo)
        Box(modifier = Modifier.align(Alignment.TopStart).padding(16.dp)) {
            BotaoMedieval(text = "Voltar", onClick = onVoltarClick)
        }


    }
}