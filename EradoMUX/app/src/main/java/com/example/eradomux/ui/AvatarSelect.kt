package com.example.eradomux.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
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


@Composable
fun TelaSelecaoAvatar(
    onVoltarClick: () -> Unit,
    // Retorna o INDEX do avatar selecionado (0, 1, 2 ou 3)
    onAvatarSelecionado: (Int) -> Unit
) {
    // Lista de IDs das imagens dos avatares
    val listaAvatares = listOf(
        R.drawable.imgperfil, // Certifique-se de ter essas imagens no res/drawable
        R.drawable.imgperfil2,
        R.drawable.imgperfil3,
        R.drawable.imgperfil4
    )

    // Estado para guardar o avatar que o usuário clicou (começa com o primeiro)
    var selectedAvatarIndex by remember { mutableStateOf(0) }

    Box(modifier = Modifier.fillMaxSize()) {

        // 1. Fundo (Mesmo do login/cadastro)
        Image(
            painter = painterResource(id = R.drawable.backgroundlogin),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        // 2. Sombra
        Box(modifier = Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.4f)))

        // 3. Conteúdo Central
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Escolha seu Guerreiro",
                color = CorDourado,
                fontFamily = FontFamily.Serif,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 32.dp)
            )

            // --- LISTA DE AVATARES (Horizontal) ---
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .background(CorFundoTransparente, RoundedCornerShape(8.dp))
                    .border(2.dp, CorDourado, RoundedCornerShape(8.dp))
                    .padding(vertical = 16.dp),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                itemsIndexed(listaAvatares) { index, avatarResId ->
                    val isSelected = (index == selectedAvatarIndex)

                    // Card individual do avatar
                    Box(
                        modifier = Modifier
                            .size(160.dp) // Tamanho do card do avatar
                            .background(Color.Transparent, RoundedCornerShape(8.dp))
                            .border(
                                width = if (isSelected) 4.dp else 2.dp, // Borda mais grossa se selecionado
                                color = if (isSelected) Color.Red else CorDourado, // Borda vermelha se selecionado
                                shape = RoundedCornerShape(8.dp)
                            )
                            .padding(4.dp) // Padding para a imagem não encostar na borda
                            .clickable { selectedAvatarIndex = index }
                    ) {
                        Image(
                            painter = painterResource(id = avatarResId),
                            contentDescription = "Avatar ${index + 1}",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(RoundedCornerShape(4.dp)) // Borda interna da imagem
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Botão "Escolher"
            BotaoMedieval(text = "Escolher", onClick = {
                onAvatarSelecionado(selectedAvatarIndex) // Retorna o ID da imagem
            })
        }

        // Botão Voltar (Topo Esquerdo)
        Box(modifier = Modifier.align(Alignment.TopStart).padding(16.dp)) {
            BotaoMedieval(text = "Voltar", onClick = onVoltarClick)
        }
    }
}