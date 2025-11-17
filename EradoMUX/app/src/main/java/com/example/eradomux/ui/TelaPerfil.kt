package com.example.eradomux.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.eradomux.R
import com.example.eradomux.ui.BotaoMedieval
import com.example.eradomux.ui.CampoMedieval
import com.example.eradomux.ui.*

@Composable
fun TelaPerfil(
    nome: String,
    tipo: String,
    senhaAtual: String, // Recebe a senha atual para preencher o campo
    avatarId: Int, // ID do avatar temporário (que pode ter sido mudado na seleção)
    onVoltarClick: () -> Unit,
    onTrocarAvatarClick: () -> Unit,
    onSalvarClick: (String) -> Unit // Retorna a nova senha (o avatar já está no state da Main)
) {
    // Estado local para a senha (editável)
    var senhaEditavel by remember { mutableStateOf(senhaAtual) }

    val scrollState = rememberScrollState()

    // Lógica para pegar a imagem correta baseada no ID
    val listaAvatares = listOf(
        R.drawable.imgperfil,
        R.drawable.imgperfil2,
        R.drawable.imgperfil3,
        R.drawable.imgperfil4
    )
    val imagemAvatar = listaAvatares.getOrElse(avatarId) { R.drawable.imgperfil }

    Box(modifier = Modifier.fillMaxSize()) {
        // 1. Fundo
        Image(
            painter = painterResource(id = R.drawable.backgroundlogin),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        Box(modifier = Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.3f)))

        // 2. Conteúdo
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            // --- ESQUERDA: LOGO (Igual ao Cadastro) ---
            Image(
                painter = painterResource(id = R.drawable.eradomux),
                contentDescription = "Logo",
                modifier = Modifier
                    .weight(0.8f)
                    .aspectRatio(1f)
                    .padding(end = 16.dp)
                    .align(Alignment.CenterVertically)
            )

            // --- DIREITA: FORMULÁRIO DE PERFIL ---
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
                        text = "Perfil do Guerreiro",
                        color = CorDourado,
                        fontFamily = FontFamily.Serif,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    // --- SEÇÃO DO AVATAR (CLICÁVEL) ---
                    Text("Avatar (Toque para alterar):", color = CorDourado, fontSize = 12.sp, fontFamily = FontFamily.Serif)
                    Spacer(modifier = Modifier.height(4.dp))

                    Box(
                        contentAlignment = Alignment.BottomEnd,
                        modifier = Modifier
                            .size(100.dp)
                            .clickable { onTrocarAvatarClick() } // Vai para tela de seleção
                    ) {
                        Image(
                            painter = painterResource(id = imagemAvatar),
                            contentDescription = "Avatar Atual",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(RoundedCornerShape(8.dp))
                                .border(2.dp, CorDourado, RoundedCornerShape(8.dp))
                        )
                        // Ícone de lápis pequeno para indicar edição
                        Box(
                            modifier = Modifier
                                .background(CorVermelhoBotao, CircleShape)
                                .padding(4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "Editar",
                                tint = CorDourado,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // --- CAMPOS DE TEXTO ---

                    // NOME (Apenas Leitura)
                    InfoApenasLeitura(titulo = "Nome:", valor = nome)

                    Spacer(modifier = Modifier.height(8.dp))

                    // TIPO (Apenas Leitura)
                    InfoApenasLeitura(titulo = "Classe:", valor = tipo)

                    Spacer(modifier = Modifier.height(12.dp))

                    // SENHA (Editável)
                    Text(
                        text = "Alterar Senha:",
                        color = CorDourado,
                        fontFamily = FontFamily.Serif,
                        fontSize = 14.sp,
                        modifier = Modifier.align(Alignment.Start).padding(bottom = 2.dp)
                    )
                    CampoMedieval(
                        value = senhaEditavel,
                        onValueChange = { senhaEditavel = it },
                        placeholder = "Nova Senha",
                        isPassword = true
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // --- BOTÕES ---
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        TextButton(onClick = onVoltarClick) {
                            Text("Cancelar", color = Color.Gray, fontFamily = FontFamily.Serif)
                        }

                        BotaoMedieval(text = "Salvar Alterações", onClick = {
                            onSalvarClick(senhaEditavel)
                        })
                    }
                }
            }
        }
    }
}

// Componente visual simples para campos que não mudam
@Composable
fun InfoApenasLeitura(titulo: String, valor: String) {
    Column(horizontalAlignment = Alignment.Start, modifier = Modifier.fillMaxWidth()) {
        Text(text = titulo, color = Color.Gray, fontSize = 12.sp, fontFamily = FontFamily.Serif)
        Text(
            text = valor,
            color = Color.White,
            fontSize = 16.sp,
            fontFamily = FontFamily.Serif,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(start = 4.dp)
        )
        Spacer(modifier = Modifier.height(4.dp))
        androidx.compose.material3.HorizontalDivider(color = Color.Gray.copy(alpha = 0.3f))
    }
}