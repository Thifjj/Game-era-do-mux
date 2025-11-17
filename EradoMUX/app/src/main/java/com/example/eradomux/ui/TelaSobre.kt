package com.example.eradomux.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
fun TelaSobre(
    onVoltarClick: () -> Unit
) {
    val desenvolvedores = listOf(
        "Thiago Fernandes Jacques",
        "André Herzfeld",
        "Rafael Mehrle Kraemer",
        "Eduardo Reichert"
    )

    Box(modifier = Modifier.fillMaxSize()) {

        // 1. FUNDO
        Image(
            painter = painterResource(id = R.drawable.fundomenu),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        Box(modifier = Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.4f)))

        // LOGO PARCEIRO (PADRÃO MENU) ---
        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd),
            contentAlignment = Alignment.Center
        ) {
            // 1. A IMAGEM DO PAPEL
            Image(
                painter = painterResource(id = R.drawable.retangulo_papel),
                contentDescription = null,
                contentScale = ContentScale.FillBounds,
                modifier = Modifier
                    .width(220.dp)
                    .height(80.dp)
            )

            // 2. O LOGO DA UNIVALI
            Image(
                painter = painterResource(id = R.drawable.logounivali),
                contentDescription = "Univali",
                modifier = Modifier
                    .height(70.dp)
                    .width(220.dp),
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

            // --- COLUNA DA ESQUERDA (LOGO DO JOGO) ---
            Column(
                modifier = Modifier
                    .weight(0.35f)
                    .fillMaxHeight(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.eradomux),
                    contentDescription = "Logo Era do Mux",
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .aspectRatio(1f)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Versão 1.0",
                    color = Color.Gray,
                    fontFamily = FontFamily.Serif,
                    fontSize = 12.sp
                )
            }

            // --- COLUNA DA DIREITA (TEXTO E CRÉDITOS) ---
            Box(
                modifier = Modifier
                    .weight(0.65f)
                    .fillMaxHeight(0.85f) // Altura controlada
                    .background(Color.Black.copy(alpha = 0.6f)) // Fundo escuro translúcido
                    .border(1.dp, CorDourado.copy(alpha = 0.5f))
                    .padding(24.dp)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState()) // Permite rolar se o texto for grande
                ) {
                    // Título do Projeto
                    Text(
                        text = "Sobre o Projeto",
                        color = CorDourado,
                        fontSize = 24.sp,
                        fontFamily = FontFamily.Serif,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    // Descrição Acadêmica
                    Text(
                        text = "Este jogo foi desenvolvido como parte da disciplina de Programação de Dispositivos Móveis na UNIVALI.",
                        color = Color.White.copy(alpha = 0.9f),
                        fontFamily = FontFamily.Serif,
                        fontSize = 16.sp,
                        textAlign = TextAlign.Center,
                        lineHeight = 24.sp
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "2º Semestre de 2025",
                        color = CorDourado,
                        fontFamily = FontFamily.Serif,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(24.dp))
                    HorizontalDivider(color = CorDourado.copy(alpha = 0.3f), thickness = 1.dp)
                    Spacer(modifier = Modifier.height(24.dp))

                    // Lista de Desenvolvedores
                    Text(
                        text = "Desenvolvedores:",
                        color = CorDourado,
                        fontSize = 20.sp,
                        fontFamily = FontFamily.Serif,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )

                    desenvolvedores.forEach { dev ->
                        Text(
                            text = dev,
                            color = Color.White,
                            fontFamily = FontFamily.Serif,
                            fontSize = 18.sp,
                            modifier = Modifier.padding(vertical = 4.dp)
                        )
                    }
                }
            }
        }

        // 3. BOTÃO VOLTAR (Topo Esquerdo)
        Box(modifier = Modifier.align(Alignment.TopStart).padding(16.dp)) {
            BotaoMedieval(text = "Voltar", onClick = onVoltarClick)
        }
    }
}