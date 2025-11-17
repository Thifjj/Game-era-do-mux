package com.example.eradomux.ui

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import com.example.eradomux.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

// Tipos de blocos no mapa
enum class TipoBloco { CHAO, ARBUSTO, JAVALI, CASA, AGUA }

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
    val mapaAtual: List<List<TipoBloco>>
    val inicioPlayer: Pair<Int, Int>
    val direcaoInicialPlayer: Int // 0: Cima, 1: Dir, 2: Baixo, 3: Esq

    when (nivelAtual) {
        1 -> {
            mapaAtual = listOf(
                listOf(TipoBloco.CHAO, TipoBloco.CHAO, TipoBloco.ARBUSTO, TipoBloco.CHAO, TipoBloco.CHAO),
                listOf(TipoBloco.CHAO, TipoBloco.CHAO, TipoBloco.CHAO, TipoBloco.ARBUSTO, TipoBloco.CHAO),
                listOf(TipoBloco.CHAO, TipoBloco.ARBUSTO, TipoBloco.CHAO, TipoBloco.CHAO, TipoBloco.JAVALI),
                listOf(TipoBloco.CHAO, TipoBloco.CHAO, TipoBloco.CHAO, TipoBloco.CHAO, TipoBloco.CHAO),
                listOf(TipoBloco.CASA, TipoBloco.CHAO, TipoBloco.CHAO, TipoBloco.ARBUSTO, TipoBloco.CHAO)
            )
            inicioPlayer = Pair(1, 1)
            direcaoInicialPlayer = 1
        }
        2 -> {
            mapaAtual = listOf(
                listOf(TipoBloco.CASA, TipoBloco.CHAO, TipoBloco.CHAO, TipoBloco.CHAO, TipoBloco.ARBUSTO),
                listOf(TipoBloco.ARBUSTO, TipoBloco.CHAO, TipoBloco.ARBUSTO, TipoBloco.CHAO, TipoBloco.ARBUSTO),
                listOf(TipoBloco.CHAO, TipoBloco.CHAO, TipoBloco.ARBUSTO, TipoBloco.CHAO, TipoBloco.JAVALI),
                listOf(TipoBloco.CHAO, TipoBloco.ARBUSTO, TipoBloco.ARBUSTO, TipoBloco.CHAO, TipoBloco.CHAO),
                listOf(TipoBloco.CHAO, TipoBloco.CHAO, TipoBloco.CHAO, TipoBloco.CHAO, TipoBloco.CHAO)
            )
            inicioPlayer = Pair(4, 0)
            direcaoInicialPlayer = 0
        }
        3 -> {
            mapaAtual = listOf(
                listOf(TipoBloco.JAVALI, TipoBloco.CHAO, TipoBloco.CHAO, TipoBloco.ARBUSTO, TipoBloco.CASA),
                listOf(TipoBloco.ARBUSTO, TipoBloco.ARBUSTO, TipoBloco.CHAO, TipoBloco.ARBUSTO, TipoBloco.CHAO),
                listOf(TipoBloco.CHAO, TipoBloco.CHAO, TipoBloco.CHAO, TipoBloco.CHAO, TipoBloco.CHAO),
                listOf(TipoBloco.CHAO, TipoBloco.ARBUSTO, TipoBloco.ARBUSTO, TipoBloco.ARBUSTO, TipoBloco.ARBUSTO),
                listOf(TipoBloco.CHAO, TipoBloco.CHAO, TipoBloco.CHAO, TipoBloco.CHAO, TipoBloco.CHAO)
            )
            inicioPlayer = Pair(4, 4)
            direcaoInicialPlayer = 3
        }
        else -> {
            mapaAtual = listOf(listOf(TipoBloco.CHAO))
            inicioPlayer = Pair(0, 0)
            direcaoInicialPlayer = 0
        }
    }

    // --- ESTADOS DO JOGO ---
    var playerPos by remember(nivelAtual) { mutableStateOf(inicioPlayer) }
    var playerDir by remember(nivelAtual) { mutableStateOf(direcaoInicialPlayer) }
    var comandos by remember(nivelAtual) { mutableStateOf(listOf<Comando>()) }

    // --- VARIÁVEIS DE ANIMAÇÃO ---
    // Animatable permite controlar o valor float pixel a pixel
    val animRow = remember(nivelAtual) { Animatable(inicioPlayer.first.toFloat()) }
    val animCol = remember(nivelAtual) { Animatable(inicioPlayer.second.toFloat()) }

    var isRunning by remember { mutableStateOf(false) }
    var showPauseMenu by remember { mutableStateOf(false) }
    var showWinDialog by remember { mutableStateOf(false) }
    var mensagemErro by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope() // Para rodar animações de reset

    val listaAvatares = listOf(R.drawable.player, R.drawable.player, R.drawable.player, R.drawable.player)
    val imgSpriteSheet = listaAvatares.getOrElse(avatarId) { R.drawable.player }

    // Animação de rotação suave
    val rotacaoAnimada by animateFloatAsState(
        targetValue = (playerDir * 90f) - 90f,
        animationSpec = tween(durationMillis = 300) // Gira rapidinho
    )

    // --- LÓGICA DE EXECUÇÃO ---
    LaunchedEffect(isRunning) {
        if (isRunning) {
            mensagemErro = ""

            // 1. Reseta a posição lógica e VISUAL para o início
            playerPos = inicioPlayer
            playerDir = direcaoInicialPlayer
            animRow.snapTo(inicioPlayer.first.toFloat())
            animCol.snapTo(inicioPlayer.second.toFloat())

            delay(500)

            for (cmd in comandos) {
                // Tempo de espera antes de executar o próximo comando
                delay(100)

                when (cmd) {
                    Comando.FRENTE -> {
                        val (r, c) = playerPos
                        var novoR = r
                        var novoC = c
                        when (playerDir) {
                            0 -> novoR--
                            1 -> novoC++
                            2 -> novoR++
                            3 -> novoC--
                        }

                        // Verifica limites
                        if (novoR in mapaAtual.indices && novoC in mapaAtual[0].indices) {
                            val blocoAlvo = mapaAtual[novoR][novoC]
                            if (blocoAlvo != TipoBloco.ARBUSTO && blocoAlvo != TipoBloco.CASA) {
                                // --- MOVIMENTO VÁLIDO: ANIMAÇÃO ---
                                playerPos = Pair(novoR, novoC)

                                // Roda as duas animações (linha e coluna) ao mesmo tempo
                                launch {
                                    animRow.animateTo(
                                        targetValue = novoR.toFloat(),
                                        animationSpec = tween(durationMillis = 500, easing = LinearEasing)
                                    )
                                }
                                launch {
                                    animCol.animateTo(
                                        targetValue = novoC.toFloat(),
                                        animationSpec = tween(durationMillis = 500, easing = LinearEasing)
                                    )
                                }
                                // Espera a animação terminar antes do próximo comando
                                delay(500)

                                if (blocoAlvo == TipoBloco.JAVALI) {
                                    showWinDialog = true
                                    isRunning = false
                                    return@LaunchedEffect
                                }
                            } else {
                                // Bateu: Animação de "tentei ir mas voltei" (opcional) ou só erro
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
                    Comando.ESQUERDA -> {
                        playerDir = (playerDir + 3) % 4
                        delay(300) // Tempo para ver ele girando
                    }
                    Comando.DIREITA -> {
                        playerDir = (playerDir + 1) % 4
                        delay(300) // Tempo para ver ele girando
                    }
                    Comando.ATACAR -> {}
                }
            }
            isRunning = false
        }
    }

    // --- UI DA TELA ---
    Box(modifier = Modifier.fillMaxSize().background(Color(0xFF2C2C2C))) {

        Text(
            text = "Fase $nivelAtual",
            color = Color.White.copy(alpha = 0.1f),
            fontSize = 100.sp,
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
                // BOX INTERNO PARA O GRID E O BONECO
                // Usamos BoxWithConstraints para saber o tamanho exato se precisasse,
                // mas como fixamos 60dp por tile, vamos usar um Box normal alinhado ao centro
                Box(
                    modifier = Modifier.align(Alignment.Center)
                ) {
                    // 1. DESENHA O GRID (FUNDO)
                    Column {
                        mapaAtual.forEachIndexed { rowIndex, row ->
                            Row {
                                row.forEachIndexed { colIndex, bloco ->
                                    Box(
                                        modifier = Modifier
                                            .size(60.dp) // Tamanho fixo do tile
                                            .border(0.5.dp, Color.White.copy(alpha = 0.2f)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Image(painter = painterResource(id = R.drawable.grama), contentDescription = null, contentScale = ContentScale.Crop, modifier = Modifier.fillMaxSize())
                                        when (bloco) {
                                            TipoBloco.ARBUSTO -> Image(painter = painterResource(id = R.drawable.mato), contentDescription = null, modifier = Modifier.padding(4.dp))
                                            TipoBloco.JAVALI -> Image(painter = painterResource(id = R.drawable.javali), contentDescription = null, modifier = Modifier.padding(4.dp))
                                            TipoBloco.CASA -> Image(painter = painterResource(id = R.drawable.casa), contentDescription = null)
                                            else -> {}
                                        }
                                        // REMOVI O BONECO DAQUI!
                                    }
                                }
                            }
                        }
                    }

                    // 2. DESENHA O BONECO (FLUTUANTE EM CIMA DO GRID)
                    // O offset calcula: PosiçãoAnimada * TamanhoDoTile (60.dp)
                    SpriteRenderer(
                        spriteSheetId = imgSpriteSheet ,
                        direction = playerDir,
                        modifier = Modifier
                            .size(60.dp) // Tamanho do boneco igual ao tile
                            .offset(
                                x = (animCol.value * 60).dp, // Movimento Horizontal Suave
                                y = (animRow.value * 60).dp  // Movimento Vertical Suave
                            )
                            .padding(bottom = 8.dp) // Ajuste visual
                    )
                }

                // Botão Pause
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
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .padding(16.dp)
                            .background(Color.Red, RoundedCornerShape(8.dp))
                            .border(2.dp, Color.White, RoundedCornerShape(8.dp))
                            .padding(16.dp)
                    ) {
                        Text(mensagemErro, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 20.sp)
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

                Row(horizontalArrangement = Arrangement.SpaceEvenly, modifier = Modifier.fillMaxWidth()) {
                    BotaoComando(Icons.Default.KeyboardArrowUp, "Andar") { comandos = comandos + Comando.FRENTE }
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row(horizontalArrangement = Arrangement.SpaceEvenly, modifier = Modifier.fillMaxWidth()) {
                    BotaoComando(Icons.Default.RotateLeft, "Esq") { comandos = comandos + Comando.ESQUERDA }
                    BotaoComando(Icons.Default.RotateRight, "Dir") { comandos = comandos + Comando.DIREITA }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text("Sua Lógica:", color = Color.Gray, fontSize = 14.sp)

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .background(Color.Black, RoundedCornerShape(4.dp))
                        .padding(8.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    @OptIn(ExperimentalLayoutApi::class)
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
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
                                    imageVector = when (cmd) {
                                        Comando.FRENTE -> Icons.Default.KeyboardArrowUp
                                        Comando.ESQUERDA -> Icons.Default.RotateLeft
                                        Comando.DIREITA -> Icons.Default.RotateRight
                                        Comando.ATACAR -> Icons.Default.Star
                                    },
                                    contentDescription = null,
                                    tint = Color.Red,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                    Button(
                        onClick = {
                            comandos = emptyList()
                            // Reseta visualmente também
                            scope.launch {
                                playerPos = inicioPlayer
                                playerDir = direcaoInicialPlayer
                                animRow.snapTo(inicioPlayer.first.toFloat())
                                animCol.snapTo(inicioPlayer.second.toFloat())
                            }
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
                                scope.launch {
                                    playerPos = inicioPlayer
                                    playerDir = direcaoInicialPlayer
                                    animRow.snapTo(inicioPlayer.first.toFloat())
                                    animCol.snapTo(inicioPlayer.second.toFloat())
                                }
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

        if (showWinDialog) {
            AlertDialog(
                onDismissRequest = {},
                containerColor = CorDourado,
                title = { Text("VITÓRIA!", color = CorVermelhoEscuro, fontWeight = FontWeight.Bold) },
                text = { Text("Fase concluída!", color = Color.Black) },
                confirmButton = {
                    Button(onClick = { onFaseConcluida() }, colors = ButtonDefaults.buttonColors(containerColor = CorVermelhoBotao)) { Text("Próxima", color = CorDourado) }
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

@Composable
fun SpriteRenderer(
    spriteSheetId: Int,
    direction: Int,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val imageBitmap = remember(spriteSheetId) {
        val drawable = ContextCompat.getDrawable(context, spriteSheetId)
        drawable?.toBitmap()?.asImageBitmap()
    }

    if (imageBitmap != null) {
        Canvas(modifier = modifier) {
            val colunas = 6
            val linhas = 10
            val spriteW = imageBitmap.width / colunas
            val spriteH = imageBitmap.height / linhas

            var flipHorizontal = false
            val linhaParaDesenhar = when (direction) {
                0 -> 2
                1 -> 1
                2 -> 0
                3 -> {
                    flipHorizontal = true
                    1
                }
                else -> 0
            }
            val colunaParaDesenhar = 0
            val srcX = colunaParaDesenhar * spriteW
            val srcY = linhaParaDesenhar * spriteH

            withTransform({
                if (flipHorizontal) {
                    scale(scaleX = -1f, scaleY = 1f, pivot = center)
                }
            }) {
                drawImage(
                    image = imageBitmap,
                    srcOffset = IntOffset(srcX, srcY),
                    srcSize = IntSize(spriteW, spriteH),
                    dstSize = IntSize(size.width.toInt(), size.height.toInt())
                )
            }
        }
    }
}