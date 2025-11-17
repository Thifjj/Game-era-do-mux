package com.example.eradomux.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState // <--- IMPORTANTE
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll // <--- IMPORTANTE
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
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
fun TelaMenu(
    nomeJogador: String,
    tipoJogador: String,
    avatarId: Int,
    onNavegar: (String) -> Unit,
    onSairClick: () -> Unit
) {
    val listaAvatares = listOf(
        R.drawable.imgperfil,
        R.drawable.imgperfil2,
        R.drawable.imgperfil3,
        R.drawable.imgperfil4
    )
    val imagemAvatarExibicao = listaAvatares.getOrElse(avatarId) { R.drawable.imgperfil }

    // Estado para controlar a rolagem dos botões
    val scrollState = rememberScrollState()

    Box(modifier = Modifier.fillMaxSize()) {

        // 1. IMAGEM DE FUNDO
        Image(
            painter = painterResource(id = R.drawable.fundomenu),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        // 2. ESTRUTURA PRINCIPAL
        Row(modifier = Modifier.fillMaxSize()) {

            // === COLUNA DA ESQUERDA (MENU E LOGO) ===
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(0.35f)
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = listOf(Color.Black.copy(alpha = 0.9f), Color.Black.copy(alpha = 0.6f))
                        )
                    )
                    .border(width = 1.dp, color = CorDourado.copy(alpha = 0.5f))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(vertical = 16.dp, horizontal = 12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    // SpaceBetween empurra o Logo pro topo e o Sair pro fundo
                    verticalArrangement = Arrangement.SpaceBetween
                ) {

                    // -- LOGO DO JOGO --
                    Image(
                        painter = painterResource(id = R.drawable.eradomux),
                        contentDescription = "Logo",
                        modifier = Modifier
                            .fillMaxWidth(0.8f)
                            .aspectRatio(1.5f)
                    )

                    // -- LISTA DE BOTÕES (AGORA COM SCROLL) --
                    Column(
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f) // Ocupa todo o espaço disponível no meio
                            .padding(vertical = 12.dp) // Espaço para não colar no logo/botão sair
                            .verticalScroll(scrollState) // <--- PERMITE ROLAR COM O DEDO
                    ) {
                        BotaoMenuPrincipal("Jogar") { onNavegar("Jogar") }
                        BotaoMenuPrincipal("Tutorial") { onNavegar("Tutorial") }
                        BotaoMenuPrincipal("Estatísticas") { onNavegar("Estatísticas") }
                        BotaoMenuPrincipal("Perfil") { onNavegar("Perfil") }
                        BotaoMenuPrincipal("Sobre") { onNavegar("Sobre") }
                        // Se você adicionar mais botões aqui, a lista vai rolar automaticamente
                    }

                    // -- BOTÃO SAIR (FIXO NO FUNDO) --
                    BotaoMenuPrincipal("Sair", isExit = true) { onSairClick() }
                }
            }

            // === ÁREA DA DIREITA (CONTEÚDO LIVRE) ===
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(0.65f)
            ) {
                // -- INFO DO JOGADOR (Topo Direito) --
                Row(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(top = 16.dp, end = 16.dp)
                        .background(Color.Black.copy(alpha = 0.6f), RoundedCornerShape(8.dp))
                        .border(1.dp, CorDourado, RoundedCornerShape(8.dp))
                        .padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = imagemAvatarExibicao),
                        contentDescription = "Avatar",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(40.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .border(1.dp, CorDourado, RoundedCornerShape(4.dp))
                    )
                    Spacer(modifier = Modifier.width(12.dp))

                    Box(
                        modifier = Modifier
                            .height(30.dp)
                            .width(180.dp)
                            .background(CorVermelhoEscuro, RoundedCornerShape(4.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "$nomeJogador ($tipoJogador)",
                            color = CorTextoBranco,
                            fontFamily = FontFamily.Serif,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )
                    }
                }

                // ---------------------------------------------------------
                // -- LOGO PARCEIRO --
                // ---------------------------------------------------------
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomEnd),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.retangulo_papel),
                        contentDescription = null,
                        contentScale = ContentScale.FillBounds,
                        modifier = Modifier
                            .width(220.dp)
                            .height(80.dp)
                    )

                    Image(
                        painter = painterResource(id = R.drawable.logounivali),
                        contentDescription = "Univali",
                        modifier = Modifier
                            .height(70.dp)
                            .width(220.dp),
                        contentScale = ContentScale.Fit
                    )
                }
            }
        }
    }
}

@Composable
fun BotaoMenuPrincipal(text: String, isExit: Boolean = false, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(40.dp)
            .background(
                if (isExit) Color(0xFF300000) else CorVermelhoBotao,
                shape = RoundedCornerShape(4.dp)
            )
            .border(1.dp, CorDourado.copy(alpha = 0.6f), RoundedCornerShape(4.dp))
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = CorDourado,
            fontFamily = FontFamily.Serif,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )
    }
}