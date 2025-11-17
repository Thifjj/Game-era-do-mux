package com.example.eradomux.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.eradomux.R
// Importamos as cores e componentes que criamos
import com.example.eradomux.ui.BotaoMedieval
import com.example.eradomux.ui.CampoMedieval
import com.example.eradomux.ui.CorDourado
import com.example.eradomux.ui.CorFundoTransparente

@Composable
fun TelaLogin(
    onLoginClick: (String, String) -> Unit,
    onCriarContaClick: (String, String) -> Unit
) {
    var nome by remember { mutableStateOf("") }
    var senha by remember { mutableStateOf("") }

    Box(modifier = Modifier.fillMaxSize()) {

        // IMAGEM DE FUNDO
        Image(
            painter = painterResource(id = R.drawable.backgroundlogin),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        // Camada escura
        Box(modifier = Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.3f)))

        // CONTEÚDO
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            // --- LOGO ---
            Image(
                painter = painterResource(id = R.drawable.eradomux),
                contentDescription = "Logo Era do Mux",
                modifier = Modifier
                    .weight(1f)
                    .aspectRatio(1f)
                    .padding(end = 16.dp)
                    .align(Alignment.CenterVertically)
            )

            // --- FORMULÁRIO ---
            Box(
                modifier = Modifier
                    .weight(1.2f)
                    .background(CorFundoTransparente, shape = RoundedCornerShape(4.dp))
                    .padding(24.dp)
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {

                    // Campo Nome
                    Text(
                        text = "Digite seu nome:",
                        color = CorDourado,
                        fontFamily = FontFamily.Serif,
                        fontSize = 18.sp,
                        modifier = Modifier.align(Alignment.Start).padding(bottom = 4.dp)
                    )
                    CampoMedieval(
                        value = nome,
                        onValueChange = { nome = it },
                        placeholder = "Nome"
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Campo Senha
                    Text(
                        text = "Digite sua senha :",
                        color = CorDourado,
                        fontFamily = FontFamily.Serif,
                        fontSize = 18.sp,
                        modifier = Modifier.align(Alignment.Start).padding(bottom = 4.dp)
                    )
                    CampoMedieval(
                        value = senha,
                        onValueChange = { senha = it },
                        placeholder = "Senha",
                        isPassword = true
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // Botões
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        BotaoMedieval(text = "Login", onClick = { onLoginClick(nome, senha) })
                        BotaoMedieval(text = "Criar conta", onClick = { onCriarContaClick(nome, senha) })
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 800, heightDp = 400)
@Composable
fun PreviewLogin() {
    TelaLogin({_,_->}, {_,_->})
}