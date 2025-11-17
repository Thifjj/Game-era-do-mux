package com.example.eradomux.ui

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.RotateLeft
import androidx.compose.material.icons.filled.RotateRight
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.eradomux.R
import com.example.eradomux.ui.*
import kotlinx.coroutines.delay
import androidx.compose.foundation.layout.FlowRow // Para quebrar a linha
import androidx.compose.foundation.rememberScrollState // Para lembrar a rolagem
import androidx.compose.foundation.verticalScroll

// Tipos de blocos no mapa
enum class TipoBloco { CHAO, ARBUSTO, JAVALI, CASA, AGUA } // Adicionei AGUA se quiser usar depois

// Tipos de Comandos
enum class Comando { FRENTE, ESQUERDA, DIREITA, ATACAR }

@Composable
fun TelaJogo(
    nivelAtual: Int,
    avatarId: Int,
    onFaseConcluida: () -> Unit,
    onVoltarMenu: () -> Unit
) {
    // --- CONFIGURAÇÃO DAS FASES ---

    // Variáveis para guardar o mapa e posição da fase escolhida
    val mapaAtual: List<List<TipoBloco>>
    val inicioPlayer: Pair<Int, Int>
    val direcaoInicialPlayer: Int // 0: Cima, 1: Dir, 2: Baixo, 3: Esq

    when (nivelAtual) {
        1 -> {
            // FASE 1: Reta Simples com obstáculo
            mapaAtual = listOf(
                listOf(TipoBloco.CHAO, TipoBloco.CHAO, TipoBloco.ARBUSTO, TipoBloco.CHAO, TipoBloco.CHAO),
                listOf(TipoBloco.CHAO, TipoBloco.CHAO, TipoBloco.CHAO, TipoBloco.ARBUSTO, TipoBloco.CHAO),
                listOf(TipoBloco.CHAO, TipoBloco.ARBUSTO, TipoBloco.CHAO, TipoBloco.CHAO, TipoBloco.JAVALI), // Objetivo
                listOf(TipoBloco.CHAO, TipoBloco.CHAO, TipoBloco.CHAO, TipoBloco.CHAO, TipoBloco.CHAO),
                listOf(TipoBloco.CASA, TipoBloco.CHAO, TipoBloco.CHAO, TipoBloco.ARBUSTO, TipoBloco.CHAO)
            )
            inicioPlayer = Pair(1, 1) // Começa (1,1)
            direcaoInicialPlayer = 1 // Olhando pra Direita
        }
        2 -> {
            // FASE 2: Desvio (Precisa virar)
            // O Javali está protegido por arbustos, tem que dar a volta
            mapaAtual = listOf(
                listOf(TipoBloco.CASA, TipoBloco.CHAO, TipoBloco.CHAO, TipoBloco.CHAO, TipoBloco.ARBUSTO),
                listOf(TipoBloco.ARBUSTO, TipoBloco.CHAO, TipoBloco.ARBUSTO, TipoBloco.CHAO, TipoBloco.ARBUSTO),
                listOf(TipoBloco.CHAO, TipoBloco.CHAO, TipoBloco.ARBUSTO, TipoBloco.CHAO, TipoBloco.JAVALI), // Objetivo
                listOf(TipoBloco.CHAO, TipoBloco.ARBUSTO, TipoBloco.ARBUSTO, TipoBloco.CHAO, TipoBloco.CHAO),
                listOf(TipoBloco.CHAO, TipoBloco.CHAO, TipoBloco.CHAO, TipoBloco.CHAO, TipoBloco.CHAO)
            )
            inicioPlayer = Pair(4, 0) // Começa lá em baixo na esquerda
            direcaoInicialPlayer = 0 // Olhando pra Cima
        }
        3 -> {
            // FASE 3: O Labirinto em "S"
            mapaAtual = listOf(
                listOf(TipoBloco.JAVALI, TipoBloco.CHAO, TipoBloco.CHAO, TipoBloco.ARBUSTO, TipoBloco.CASA), // Objetivo (0,0)
                listOf(TipoBloco.ARBUSTO, TipoBloco.ARBUSTO, TipoBloco.CHAO, TipoBloco.ARBUSTO, TipoBloco.CHAO),
                listOf(TipoBloco.CHAO, TipoBloco.CHAO, TipoBloco.CHAO, TipoBloco.CHAO, TipoBloco.CHAO),
                listOf(TipoBloco.CHAO, TipoBloco.ARBUSTO, TipoBloco.ARBUSTO, TipoBloco.ARBUSTO, TipoBloco.ARBUSTO),
                listOf(TipoBloco.CHAO, TipoBloco.CHAO, TipoBloco.CHAO, TipoBloco.CHAO, TipoBloco.CHAO)
            )
            inicioPlayer = Pair(4, 4) // Começa no canto inferior direito
            direcaoInicialPlayer = 3 // Olhando pra Esquerda
        }
        else -> {
            // Fallback se vier um nível que não existe (repete a 1)
            mapaAtual = listOf(
                listOf(TipoBloco.CHAO, TipoBloco.JAVALI),
                listOf(TipoBloco.CHAO, TipoBloco.CHAO)
            )
            inicioPlayer = Pair(1, 0)
            direcaoInicialPlayer = 0
        }
    }

    // --- ESTADOS DO JOGO ---
    // "remember(nivelAtual)" garante que reseta se mudar de fase
    var playerPos by remember(nivelAtual) { mutableStateOf(inicioPlayer) }
    var playerDir by remember(nivelAtual) { mutableStateOf(direcaoInicialPlayer) }
    var comandos by remember(nivelAtual) { mutableStateOf(listOf<Comando>()) }

    var isRunning by remember { mutableStateOf(false) }
    var showPauseMenu by remember { mutableStateOf(false) }
    var showWinDialog by remember { mutableStateOf(false) }
    var mensagemErro by remember { mutableStateOf("") }

    // Imagem do Avatar
    val listaAvatares = listOf(R.drawable.imgperfil, R.drawable.imgperfil2, R.drawable.imgperfil3, R.drawable.imgperfil4)
    val imgAvatar = listaAvatares.getOrElse(avatarId) { R.drawable.imgperfil }

    // Animação de rotação
    val rotacaoAnimada by animateFloatAsState(targetValue = (playerDir * 90f) - 90f)

    // --- LÓGICA DE EXECUÇÃO ---
    LaunchedEffect(isRunning) {
        if (isRunning) {
            mensagemErro = ""
            // Reseta posição para o início da fase atual antes de rodar
            playerPos = inicioPlayer
            playerDir = direcaoInicialPlayer
            delay(500)

            for (cmd in comandos) {
                delay(600)
                when (cmd) {
                    Comando.FRENTE -> {
                        val (r, c) = playerPos
                        var novoR = r
                        var novoC = c
                        when (playerDir) {
                            0 -> novoR-- // Cima
                            1 -> novoC++ // Dir
                            2 -> novoR++ // Baixo
                            3 -> novoC-- // Esq
                        }
                        // Verifica limites
                        if (novoR in mapaAtual.indices && novoC in mapaAtual[0].indices) {
                            val blocoAlvo = mapaAtual[novoR][novoC]

                            // Colisão
                            if (blocoAlvo != TipoBloco.ARBUSTO && blocoAlvo != TipoBloco.CASA) {
                                playerPos = Pair(novoR, novoC)

                                // Verifica Vitória
                                if (blocoAlvo == TipoBloco.JAVALI) {
                                    showWinDialog = true
                                    isRunning = false
                                    return@LaunchedEffect
                                }
                            } else {
                                mensagemErro = "Bateu no obstáculo!"
                                isRunning = false
                                return@LaunchedEffect
                            }
                        } else {
                            mensagemErro = "Saiu do mapa!"
                            isRunning = false
                            return@LaunchedEffect
                        }
                    }
                    Comando.ESQUERDA -> playerDir = (playerDir + 3) % 4
                    Comando.DIREITA -> playerDir = (playerDir + 1) % 4
                    Comando.ATACAR -> { /* Implementar futuramente */ }
                }
            }
            isRunning = false
        }
    }

    // --- UI DA TELA ---
    Box(modifier = Modifier.fillMaxSize().background(Color(0xFF2C2C2C))) {

        // Título da Fase (Visualização rápida)
        Text(
            text = "Fase $nivelAtual",
            color = Color.White.copy(alpha = 0.5f),
            fontSize = 80.sp,
            fontFamily = FontFamily.Serif,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.Center)
        )

        Row(modifier = Modifier.fillMaxSize()) {

            // === ÁREA DO MAPA (ESQUERDA) ===
            Box(
                modifier = Modifier
                    .weight(0.7f)
                    .fillMaxHeight()
                    .padding(16.dp)
                    .border(4.dp, CorDourado, RoundedCornerShape(8.dp))
                    .background(Color(0xFF228B22))
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Renderiza o Grid Dinâmico
                    mapaAtual.forEachIndexed { rowIndex, row ->
                        Row {
                            row.forEachIndexed { colIndex, bloco ->
                                Box(
                                    modifier = Modifier
                                        .size(60.dp)
                                        .border(0.5.dp, Color.White.copy(alpha = 0.3f)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    // 1. Chão
                                    Image(
                                        painter = painterResource(id = R.drawable.grama),
                                        contentDescription = null,
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier.fillMaxSize()
                                    )

                                    // 2. Objetos
                                    when (bloco) {
                                        TipoBloco.ARBUSTO -> Image(painter = painterResource(id = R.drawable.mato), contentDescription = null, modifier = Modifier.padding(4.dp))
                                        TipoBloco.JAVALI -> Image(painter = painterResource(id = R.drawable.javali), contentDescription = null, modifier = Modifier.padding(4.dp))
                                        TipoBloco.CASA -> Image(painter = painterResource(id = R.drawable.casa), contentDescription = null)
                                        else -> {}
                                    }

                                    // 3. Jogador
                                    if (rowIndex == playerPos.first && colIndex == playerPos.second) {
                                        Image(
                                            painter = painterResource(id = imgAvatar),
                                            contentDescription = "Player",
                                            modifier = Modifier
                                                .fillMaxSize()
                                                .rotate(rotacaoAnimada)
                                                .padding(2.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                // Menu Pause
                Box(modifier = Modifier.align(Alignment.TopEnd).padding(8.dp)) {
                    Button(
                        onClick = { showPauseMenu = true },
                        colors = ButtonDefaults.buttonColors(containerColor = CorVermelhoBotao)
                    ) {
                        Icon(Icons.Default.Menu, contentDescription = "Pause", tint = CorDourado)
                    }
                }

                // Mensagem de Erro
                if (mensagemErro.isNotEmpty()) {
                    Box(modifier = Modifier.align(Alignment.BottomCenter).padding(16.dp).background(Color.Red.copy(alpha = 0.8f), RoundedCornerShape(4.dp)).padding(8.dp)) {
                        Text(mensagemErro, color = Color.White, fontWeight = FontWeight.Bold)
                    }
                }
            }

            // === ÁREA DE COMANDOS (DIREITA) ===
            Column(
                modifier = Modifier
                    .weight(0.3f)
                    .fillMaxHeight()
                    .background(Color(0xFF1A1A1A))
                    .padding(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Comandos", color = CorDourado, fontSize = 20.sp, fontFamily = FontFamily.Serif, modifier = Modifier.padding(vertical = 8.dp))

                // Botões
                Row(horizontalArrangement = Arrangement.SpaceEvenly, modifier = Modifier.fillMaxWidth()) {
                    BotaoComando(Icons.Default.KeyboardArrowUp, "Andar") { comandos = comandos + Comando.FRENTE }
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row(horizontalArrangement = Arrangement.SpaceEvenly, modifier = Modifier.fillMaxWidth()) {
                    BotaoComando(Icons.Default.RotateLeft, "Esq") { comandos = comandos + Comando.ESQUERDA }
                    BotaoComando(Icons.Default.RotateRight, "Dir") { comandos = comandos + Comando.DIREITA }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Lista de Comandos (O "Código" que o usuário criou)
                Text("Sua Lógica:", color = Color.Gray, fontSize = 14.sp)

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .background(Color.Black, RoundedCornerShape(4.dp))
                        .padding(8.dp)
                        // ADICIONADO: Permite rolar para baixo se tiver muitas linhas
                        .verticalScroll(rememberScrollState())
                ) {
                    // ADICIONADO: FlowRow faz os itens quebrarem para a próxima linha
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        // FlowRow não tem 'itemsIndexed', então usamos o forEachIndexed padrão do Kotlin
                        comandos.forEachIndexed { index, cmd ->
                            Box(
                                modifier = Modifier
                                    .background(Color.DarkGray, RoundedCornerShape(4.dp))
                                    .clickable {
                                        val novaLista = comandos.toMutableList()
                                        novaLista.removeAt(index)
                                        comandos = novaLista
                                    }
                                    .padding(4.dp)
                            ) {
                                Icon(
                                    imageVector = when(cmd) {
                                        Comando.FRENTE -> Icons.Default.KeyboardArrowUp
                                        Comando.ESQUERDA -> Icons.Default.RotateLeft
                                        Comando.DIREITA -> Icons.Default.RotateRight
                                        Comando.ATACAR -> Icons.Default.Star
                                    },
                                    contentDescription = "Remover",
                                    tint = Color.Red,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))

                // Controles Play/Clear
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                    Button(
                        onClick = {
                            comandos = emptyList()
                            playerPos = inicioPlayer // Reseta pra posição original da fase
                            playerDir = direcaoInicialPlayer
                            mensagemErro = ""
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)
                    ) {
                        Icon(Icons.Default.Refresh, contentDescription = "Limpar")
                    }

                    Button(
                        onClick = { isRunning = true },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF006400)),
                        enabled = !isRunning && comandos.isNotEmpty()
                    ) {
                        Icon(Icons.Default.PlayArrow, contentDescription = "Executar", tint = Color.White)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("RODAR")
                    }
                }
            }
        }

        // --- PAUSE ---
        if (showPauseMenu) {
            AlertDialog(
                onDismissRequest = { showPauseMenu = false },
                containerColor = CorVermelhoEscuro,
                title = { Text("Pausado", color = CorDourado) },
                text = {
                    Column {
                        Button(onClick = { showPauseMenu = false }, colors = ButtonDefaults.buttonColors(containerColor = CorVermelhoBotao), modifier = Modifier.fillMaxWidth()) { Text("Continuar", color = CorDourado) }
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(
                            onClick = {
                                comandos = emptyList()
                                playerPos = inicioPlayer
                                playerDir = direcaoInicialPlayer
                                showPauseMenu = false
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = CorVermelhoBotao), modifier = Modifier.fillMaxWidth()
                        ) { Text("Reiniciar Fase", color = CorDourado) }
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(onClick = { onVoltarMenu() }, colors = ButtonDefaults.buttonColors(containerColor = Color.Black), modifier = Modifier.fillMaxWidth()) { Text("Sair", color = Color.Red) }
                    }
                },
                confirmButton = {}
            )
        }

        // --- VITÓRIA ---
        if (showWinDialog) {
            AlertDialog(
                onDismissRequest = {},
                containerColor = CorDourado,
                title = { Text("VITÓRIA!", color = CorVermelhoEscuro, fontWeight = FontWeight.Bold) },
                text = { Text("Fase $nivelAtual concluída!", color = Color.Black) },
                confirmButton = {
                    Button(
                        onClick = { onFaseConcluida() }, // Salva e sai
                        colors = ButtonDefaults.buttonColors(containerColor = CorVermelhoBotao)
                    ) {
                        Text("Próxima", color = CorDourado)
                    }
                }
            )
        }
    }
}

@Composable
fun BotaoComando(icon: ImageVector, label: String, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(containerColor = CorVermelhoBotao),
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier.size(width = 70.dp, height = 60.dp)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(icon, contentDescription = null, tint = CorDourado)
            Text(label, fontSize = 10.sp, color = CorDourado)
        }
    }
}