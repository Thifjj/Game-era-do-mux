package com.example.eradomux.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.eradomux.R
import com.example.eradomux.ui.BotaoMedieval
import com.example.eradomux.ui.CampoMedieval
import com.example.eradomux.ui.*

@Composable
fun TelaCadastro(
    onVoltarClick: () -> Unit,
    onCadastrarClick: (String, String, String) -> Unit, // REMOVIDO: avatarId
    onEscolherAvatarClick: () -> Unit, // <--- NOVO PARAMETRO
    currentAvatarId: Int
) {
    var nome by remember { mutableStateOf("") }
    var senha by remember { mutableStateOf("") }

    val opcoesConta = listOf("Aluno", "Professor")
    var indiceOpcao by remember { mutableStateOf(0) }
    val opcaoAtual = opcoesConta[indiceOpcao]

    val scrollState = rememberScrollState()

    // Lista de avatares para mostrar a prévia na tela de cadastro
    val listaAvataresParaPreview = listOf(
        R.drawable.imgperfil,
        R.drawable.imgperfil2,
        R.drawable.imgperfil3,
        R.drawable.imgperfil4
    )
    // Pega a imagem do avatar que está atualmente selecionada
    val avatarParaExibir = listaAvataresParaPreview.getOrElse(currentAvatarId) { R.drawable.imgperfil }

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.backgroundlogin),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        Box(modifier = Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.3f)))

        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Image(
                painter = painterResource(id = R.drawable.eradomux),
                contentDescription = "Logo Era do Mux",
                modifier = Modifier
                    .weight(0.8f)
                    .aspectRatio(1f)
                    .padding(end = 16.dp)
                    .align(Alignment.CenterVertically)
            )

            Box(
                modifier = Modifier
                    .weight(1.2f)
                    .background(CorFundoTransparente, shape = RoundedCornerShape(4.dp))
                    .padding(16.dp)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.verticalScroll(scrollState)
                ) {
                    Text(
                        text = "Novo Registro",
                        color = CorDourado,
                        fontFamily = FontFamily.Serif,
                        fontSize = 18.sp,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    // --- NOVO: PRÉVIA DO AVATAR E BOTÃO PARA SELECIONAR ---
                    Text(
                        text = "Seu Avatar:",
                        color = CorDourado,
                        fontFamily = FontFamily.Serif,
                        fontSize = 14.sp,
                        modifier = Modifier.align(Alignment.Start).padding(bottom = 2.dp)
                    )
                    Box(
                        modifier = Modifier
                            .size(100.dp)
                            .background(Color.Black.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
                            .border(2.dp, CorDourado, RoundedCornerShape(8.dp))
                            .clickable { onEscolherAvatarClick() }, // <--- CHAMA A TELA DE SELEÇÃO
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(id = avatarParaExibir),
                            contentDescription = "Avatar Selecionado",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize().clip(RoundedCornerShape(6.dp))
                        )
                    }
                    Text(
                        text = "Toque para escolher",
                        color = Color.White.copy(alpha = 0.7f),
                        fontSize = 10.sp,
                        modifier = Modifier.padding(top = 4.dp, bottom = 8.dp)
                    )
                    // --- FIM NOVO ---


                    Text(
                        text = "Nome:",
                        color = CorDourado,
                        fontFamily = FontFamily.Serif,
                        fontSize = 14.sp,
                        modifier = Modifier.align(Alignment.Start).padding(bottom = 2.dp)
                    )
                    CampoMedieval(
                        value = nome,
                        onValueChange = { nome = it },
                        placeholder = "Seu nome"
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Senha:",
                        color = CorDourado,
                        fontFamily = FontFamily.Serif,
                        fontSize = 14.sp,
                        modifier = Modifier.align(Alignment.Start).padding(bottom = 2.dp)
                    )
                    CampoMedieval(
                        value = senha,
                        onValueChange = { senha = it },
                        placeholder = "******",
                        isPassword = true
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Tipo de conta:",
                        color = CorDourado,
                        fontFamily = FontFamily.Serif,
                        fontSize = 14.sp,
                        modifier = Modifier.align(Alignment.Start).padding(bottom = 2.dp)
                    )
                    Button(
                        onClick = {
                            if (indiceOpcao < opcoesConta.size - 1) indiceOpcao++ else indiceOpcao = 0
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = CorVermelhoBotao),
                        shape = RoundedCornerShape(8.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, CorDourado.copy(alpha = 0.5f)),
                        modifier = Modifier.fillMaxWidth().height(40.dp)
                    ) {
                        Text(
                            text = "< $opcaoAtual >",
                            color = CorTextoBranco,
                            fontFamily = FontFamily.Serif,
                            fontSize = 14.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        TextButton(onClick = onVoltarClick) {
                            Text("Voltar", color = Color.White, fontFamily = FontFamily.Serif, fontSize = 14.sp)
                        }

                        BotaoMedieval(text = "Criar", onClick = { onCadastrarClick(nome, senha, opcaoAtual) })
                    }
                }
            }
        }
    }
}