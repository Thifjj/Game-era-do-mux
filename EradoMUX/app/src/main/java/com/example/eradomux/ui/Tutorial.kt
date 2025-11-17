package com.example.eradomux.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.eradomux.R
import com.example.eradomux.ui.BotaoMedieval
import com.example.eradomux.ui.*



// Modelo de dados para o tutorial
data class ItemTutorial(
    val titulo: String,
    val descricao: String,
    val icone: ImageVector
)

@Composable
fun TelaTutorial(
    nomeJogador: String,
    avatarId: Int,
    onVoltarClick: () -> Unit
) {
    val listaAvatares = listOf(
        R.drawable.imgperfil,
        R.drawable.imgperfil2,
        R.drawable.imgperfil3,
        R.drawable.imgperfil4
    )
    // Pega a imagem certa ou usa a primeira se der erro
    val imagemAvatar = listaAvatares.getOrElse(avatarId) { R.drawable.imgperfil }
    // Lista de instruções do jogo
    val listaTutorial = listOf(
        ItemTutorial(
            "Movimentação",
            "Use o Joystick virtual no canto esquerdo para mover seu personagem pelo mapa.",
            Icons.Default.PlayArrow // Seta indicando movimento/ação
        ),
        ItemTutorial(
            "Coleta",
            "Aproxime-se de arbustos e pressione o botão de ação para coletar frutas e recursos.",
            Icons.Default.ThumbUp // Mãozinha (Like) indicando interação manual
        ),
        ItemTutorial(
            "Combate",
            "Enfrente javalis e inimigos! Use sua arma para atacar e defender seu vilarejo.",
            Icons.Default.Close // O 'X' lembra duas espadas cruzadas ou perigo
        ),
        ItemTutorial(
            "Objetivo",
            "Complete as missões de cada fase sem perder seus aldeões para avançar de nível.",
            Icons.Default.Star // Estrela indicando objetivo/vitória
        )
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
                // AVATAR GRANDE
                Image(
                    painter = painterResource(id = imagemAvatar),
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

            // --- COLUNA DA DIREITA (LISTA DE TUTORIAL) ---
            Box(
                modifier = Modifier
                    .weight(0.7f)
                    .fillMaxHeight(0.85f)
                    .background(Color.Black.copy(alpha = 0.6f)) // Um pouco mais escuro para leitura
                    .border(1.dp, CorDourado.copy(alpha = 0.5f))
                    .padding(16.dp)
            ) {
                Column {
                    // Título do Manual
                    Text(
                        text = "Manual do Aventureiro",
                        color = CorDourado,
                        fontSize = 24.sp,
                        fontFamily = FontFamily.Serif,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 16.dp).align(Alignment.CenterHorizontally)
                    )

                    HorizontalDivider(color = CorDourado, thickness = 1.dp)
                    Spacer(modifier = Modifier.height(8.dp))

                    // Lista de Itens
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(listaTutorial) { item ->
                            Row(
                                verticalAlignment = Alignment.Top,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                // Ícone do Item
                                Icon(
                                    imageVector = item.icone,
                                    contentDescription = null,
                                    tint = CorDourado,
                                    modifier = Modifier.size(32.dp).padding(top = 2.dp)
                                )

                                Spacer(modifier = Modifier.width(12.dp))

                                // Textos
                                Column {
                                    Text(
                                        text = item.titulo,
                                        color = Color.White,
                                        fontFamily = FontFamily.Serif,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 18.sp
                                    )
                                    Text(
                                        text = item.descricao,
                                        color = Color.White.copy(alpha = 0.8f), // Texto um pouco mais suave
                                        fontFamily = FontFamily.Serif,
                                        fontSize = 14.sp
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // 3. BOTÕES E LOGO

        // Botão Voltar (Topo Esquerdo)
        Box(modifier = Modifier.align(Alignment.TopStart).padding(16.dp)) {
            BotaoMedieval(text = "Voltar", onClick = onVoltarClick)
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