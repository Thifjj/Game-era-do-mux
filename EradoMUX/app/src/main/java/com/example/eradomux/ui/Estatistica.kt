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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.eradomux.R
import com.example.eradomux.ui.BotaoMedieval
import com.example.eradomux.ui.*

data class EstatisticaItem(
    val titulo: String,
    val valor: String
)

@Composable
fun TelaEstatisticas(
    nomeJogador: String,
    avatarId: Int, // <--- NOVO PARAMETRO
    onVoltarClick: () -> Unit,
    onConquistasClick: () -> Unit
) {
    // Mesma lista de referência
    val listaAvatares = listOf(
        R.drawable.imgperfil, R.drawable.imgperfil2, R.drawable.imgperfil3, R.drawable.imgperfil4
    )
    val imagemAvatarExibicao = listaAvatares.getOrElse(avatarId) { R.drawable.imgperfil }
    // Lista simulada
    val listaEstatisticas = listOf(
        EstatisticaItem("Fases Concluídas", "8"),
        EstatisticaItem("Arbustos Coletados", "124"),
        EstatisticaItem("Javalis Caçados", "32"),
        EstatisticaItem("Aldeões Perdidos", "5"),
        EstatisticaItem("Fases Jogadas", "12")
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

        // 2. CONTEÚDO PRINCIPAL
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            // --- COLUNA DA ESQUERDA (PERFIL) ---
            Column(
                modifier = Modifier
                    .weight(0.3f)
                    .fillMaxHeight(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // AVATAR GRANDE (Fixo, igual ao do Menu)
                Image(
                    painter = painterResource(id = imagemAvatarExibicao), // <--- Imagem fixa
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

            // --- COLUNA DA DIREITA (TABELA) ---
            Box(
                modifier = Modifier
                    .weight(0.7f)
                    .fillMaxHeight(0.85f)
                    .background(Color.Black.copy(alpha = 0.4f))
                    .border(1.dp, CorDourado.copy(alpha = 0.5f))
                    .padding(16.dp)
            ) {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    itemsIndexed(listaEstatisticas) { index, item ->
                        LinhaEstatistica(item)

                        if (index < listaEstatisticas.size - 1) {
                            Spacer(modifier = Modifier.height(12.dp))
                            HorizontalDivider(color = CorDourado.copy(alpha = 0.3f), thickness = 1.dp)
                        }
                    }
                }
            }
        }

        // 3. BOTÕES E LOGO

        // Voltar (Topo Esquerdo)
        Box(modifier = Modifier.align(Alignment.TopStart).padding(16.dp)) {
            BotaoMedieval(text = "Voltar", onClick = onVoltarClick)
        }

        // Ir para Conquistas (Topo Direito)
        Box(modifier = Modifier.align(Alignment.TopEnd).padding(16.dp)) {
            BotaoMedieval(text = "Ir para Conquistas", onClick = onConquistasClick)
        }

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
    }
}

@Composable
fun LinhaEstatistica(item: EstatisticaItem) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = item.titulo,
            color = Color.White,
            fontFamily = FontFamily.Serif,
            fontSize = 20.sp,
            modifier = Modifier.padding(start = 8.dp)
        )

        Box(
            modifier = Modifier
                .width(60.dp)
                .height(40.dp)
                .background(CorVermelhoBotao, RoundedCornerShape(4.dp))
                .border(1.dp, CorDourado, RoundedCornerShape(4.dp)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = item.valor,
                color = CorDourado,
                fontFamily = FontFamily.Serif,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )
        }
    }
}