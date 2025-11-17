package com.example.eradomux

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.google.firebase.firestore.FirebaseFirestore

// Importamos nossas telas (Considerando que você renomeou tirando o "Medieval")
import com.example.eradomux.ui.TelaLogin
import com.example.eradomux.ui.TelaCadastro
import com.example.eradomux.ui.TelaMenu
import com.example.eradomux.ui.TelaEstatisticas
import com.example.eradomux.ui.TelaConquistas
import com.example.eradomux.ui.TelaTutorial
import com.example.eradomux.ui.TelaSelecaoAvatar
import com.example.eradomux.ui.TelaSelecaoFases
import com.example.eradomux.ui.TelaJogo
import com.example.eradomux.ui.TelaPerfil
import com.example.eradomux.ui.TelaSobre


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val db = FirebaseFirestore.getInstance()

        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // Estado de Navegação
                    var telaAtual by remember { mutableStateOf("login") }

                    // Dados do Usuário Logado
                    var nomeUsuarioLogado by remember { mutableStateOf("") }
                    var tipoUsuarioLogado by remember { mutableStateOf("") }
                    var avatarUsuarioLogado by remember { mutableStateOf(0) }
                    var senhaUsuarioLogado by remember { mutableStateOf("") } // Precisamos guardar a senha atual
                    var avatarEdicaoPerfil by remember { mutableStateOf(0) }
                    // Dados de Cadastro
                    var avatarSelecionadoNoCadastro by remember { mutableStateOf(0) }

                    // --- NOVA VARIÁVEL: Qual fase o jogador escolheu? ---
                    var faseSelecionada by remember { mutableStateOf(1) }

                    when (telaAtual) {
                        "login" -> {
                            TelaLogin(
                                onLoginClick = { nome, senha ->
                                    fazerLogin(db, nome, senha) { sucesso, nomeRetornado, tipoRetornado, avatarRetornado ->
                                        if (sucesso) {
                                            nomeUsuarioLogado = nomeRetornado
                                            tipoUsuarioLogado = tipoRetornado
                                            avatarUsuarioLogado = avatarRetornado
                                            senhaUsuarioLogado = senha
                                            telaAtual = "menu"
                                        }
                                    }
                                },
                                onCriarContaClick = { _, _ -> telaAtual = "cadastro" }
                            )
                        }

                        "cadastro" -> {
                            TelaCadastro(
                                onVoltarClick = { telaAtual = "login" },
                                onCadastrarClick = { nome, senha, tipo ->
                                    criarConta(db, nome, senha, tipo, avatarSelecionadoNoCadastro) { sucesso ->
                                        if (sucesso) telaAtual = "login"
                                    }
                                },
                                onEscolherAvatarClick = { telaAtual = "selecao_avatar" },
                                currentAvatarId = avatarSelecionadoNoCadastro
                            )
                        }

                        "selecao_avatar" -> {
                            TelaSelecaoAvatar(
                                onVoltarClick = { telaAtual = "cadastro" },
                                onAvatarSelecionado = { avatarIdEscolhido ->
                                    avatarSelecionadoNoCadastro = avatarIdEscolhido
                                    telaAtual = "cadastro"
                                }
                            )
                        }

                        "menu" -> {
                            TelaMenu(
                                nomeJogador = nomeUsuarioLogado,
                                tipoJogador = tipoUsuarioLogado,
                                avatarId = avatarUsuarioLogado,
                                onNavegar = { destino ->
                                    when (destino) {
                                        "Jogar" -> telaAtual = "selecao_fases"
                                        "Tutorial" -> telaAtual = "tutorial"
                                        "Estatísticas" -> telaAtual = "estatisticas"
                                        "Perfil" -> {
                                            avatarEdicaoPerfil = avatarUsuarioLogado
                                            telaAtual = "perfil"
                                        }
                                        "Sobre" -> telaAtual = "sobre"
                                    }
                                },
                                onSairClick = {
                                    nomeUsuarioLogado = ""
                                    tipoUsuarioLogado = ""
                                    avatarUsuarioLogado = 0
                                    telaAtual = "login"
                                }
                            )
                        }

                        // --- AQUI ESTAVA O ERRO 1: Não salvava qual fase clicou ---
                        "selecao_fases" -> {
                            TelaSelecaoFases(
                                onVoltarClick = { telaAtual = "menu" },
                                onFaseClick = { numeroFase ->
                                    faseSelecionada = numeroFase // <--- GUARDA O NÚMERO DA FASE
                                    Toast.makeText(this, "Carregando Fase $numeroFase...", Toast.LENGTH_SHORT).show()
                                    telaAtual = "jogo"
                                }
                            )
                        }

                        // --- AQUI ESTAVA O ERRO 2: O nível estava fixo em 1 ---
                        "jogo" -> {
                            TelaJogo(
                                nivelAtual = faseSelecionada, // <--- USA A VARIÁVEL, NÃO O NÚMERO 1
                                avatarId = avatarUsuarioLogado,
                                onVoltarMenu = { telaAtual = "menu" },
                                onFaseConcluida = {
                                    // Salva que passou da fase selecionada
                                    salvarProgressoFase(db, nomeUsuarioLogado, faseSelecionada) {
                                        telaAtual = "selecao_fases" // Volta pro mapa
                                    }
                                }
                            )
                        }

                        "estatisticas" -> {
                            TelaEstatisticas(
                                nomeJogador = nomeUsuarioLogado,
                                avatarId = avatarUsuarioLogado,
                                onVoltarClick = { telaAtual = "menu" },
                                onConquistasClick = { telaAtual = "conquistas" }
                            )
                        }

                        "conquistas" -> {
                            TelaConquistas(
                                nomeJogador = nomeUsuarioLogado,
                                avatarId = avatarUsuarioLogado,
                                onVoltarClick = { telaAtual = "estatisticas" }
                            )
                        }

                        "tutorial" -> {
                            TelaTutorial(
                                nomeJogador = nomeUsuarioLogado,
                                avatarId = avatarUsuarioLogado,
                                onVoltarClick = { telaAtual = "menu" }
                            )
                        }

                        // --- TELA DE PERFIL ---
                        "perfil" -> {
                            TelaPerfil(
                                nome = nomeUsuarioLogado,
                                tipo = tipoUsuarioLogado,
                                senhaAtual = senhaUsuarioLogado,
                                avatarId = avatarEdicaoPerfil, // Mostra o que está sendo editado
                                onVoltarClick = {
                                    // Se cancelar, volta pro menu e descarta mudanças
                                    telaAtual = "menu"
                                },
                                onTrocarAvatarClick = {
                                    telaAtual = "selecao_avatar_perfil" // Rota específica para saber voltar pro perfil
                                },
                                onSalvarClick = { novaSenha ->
                                    // Chama a função para gravar no banco
                                    atualizarPerfil(db, nomeUsuarioLogado, novaSenha, avatarEdicaoPerfil) {
                                        // Se der certo:
                                        senhaUsuarioLogado = novaSenha
                                        avatarUsuarioLogado = avatarEdicaoPerfil
                                        telaAtual = "menu"
                                    }
                                }
                            )
                        }

                        "selecao_avatar_perfil" -> {
                            TelaSelecaoAvatar(
                                onVoltarClick = { telaAtual = "perfil" },
                                onAvatarSelecionado = { novoId ->
                                    avatarEdicaoPerfil = novoId // Atualiza o temporário
                                    telaAtual = "perfil" // Volta para a tela de perfil
                                }
                            )
                        }

                        "sobre" -> {
                            TelaSobre(
                                onVoltarClick = {
                                    telaAtual = "menu"
                                }
                            )
                        }


                    }
                }
            }
        }
    }

    // --- LÓGICA DE LOGIN (CORRIGIDA: Agora retorna 4 valores, incluindo avatar) ---
    private fun fazerLogin(
        db: FirebaseFirestore,
        nome: String,
        senha: String,
        onResult: (Boolean, String, String, Int) -> Unit // <--- Adicionado Int para o Avatar
    ) {
        if (nome.isEmpty() || senha.isEmpty()) {
            Toast.makeText(this, "Preencha todos os campos!", Toast.LENGTH_SHORT).show()
            return
        }
        db.collection("usuarios")
            .whereEqualTo("nome", nome)
            .whereEqualTo("senha", senha)
            .get()
            .addOnSuccessListener { documents ->
                if (!documents.isEmpty) {
                    val doc = documents.documents[0]
                    val tipo = doc.getString("classe") ?: "Aluno"
                    val nomeReal = doc.getString("nome") ?: nome
                    // Busca o avatarId do banco (se não tiver, usa 0)
                    val avatarId = doc.getLong("avatarId")?.toInt() ?: 0

                    Toast.makeText(this, "Login realizado!", Toast.LENGTH_SHORT).show()
                    // Retorna os 4 dados
                    onResult(true, nomeReal, tipo, avatarId)
                } else {
                    Toast.makeText(this, "Dados incorretos.", Toast.LENGTH_SHORT).show()
                    onResult(false, "", "", 0)
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "Erro de conexão.", Toast.LENGTH_SHORT).show()
                onResult(false, "", "", 0)
            }
    }

    // --- LÓGICA DE CRIAR CONTA ---
    private fun criarConta(
        db: FirebaseFirestore,
        nome: String,
        senha: String,
        tipo: String,
        avatarId: Int,
        onResult: (Boolean) -> Unit
    ) {
        if (nome.isEmpty() || senha.isEmpty()) {
            Toast.makeText(this, "Preencha todos os campos!", Toast.LENGTH_SHORT).show()
            onResult(false)
            return
        }

        db.collection("usuarios")
            .whereEqualTo("nome", nome)
            .get()
            .addOnSuccessListener { documents ->
                if (!documents.isEmpty) {
                    // --- CORREÇÃO AQUI: ADICIONE O AVISO ---
                    Toast.makeText(this, "Usuário já existe! Escolha outro nome.", Toast.LENGTH_SHORT).show()
                    onResult(false) // Avisa que falhou
                } else {
                    // ... (o resto do código de salvar está perfeito) ...
                    val novoUsuario = hashMapOf(
                        "nome" to nome,
                        "senha" to senha,
                        "nivel" to 1,
                        "classe" to tipo,
                        "avatarId" to avatarId,
                        "ouro" to 0 // Adicionei o ouro que estava faltando no seu ultimo snippet, mas é opcional
                    )

                    db.collection("usuarios")
                        .add(novoUsuario)
                        .addOnSuccessListener {
                            Toast.makeText(this, "Conta criada! Faça login.", Toast.LENGTH_LONG).show()
                            onResult(true)
                        }
                        .addOnFailureListener {
                            Toast.makeText(this, "Erro ao criar.", Toast.LENGTH_SHORT).show()
                            onResult(false)
                        }
                }
            }
    }

    private fun salvarProgressoFase(
        db: FirebaseFirestore,
        nomeUsuario: String,
        faseConcluida: Int,
        onSuccess: () -> Unit
    ) {
        // 1. Busca o usuário pelo nome
        db.collection("usuarios")
            .whereEqualTo("nome", nomeUsuario)
            .get()
            .addOnSuccessListener { documents ->
                if (!documents.isEmpty) {
                    val docId = documents.documents[0].id
                    val nivelAtual = documents.documents[0].getLong("nivel")?.toInt() ?: 1

                    // Só atualiza se a fase concluída for maior ou igual ao nível atual (para liberar o próximo)
                    if (faseConcluida >= nivelAtual) {
                        db.collection("usuarios").document(docId)
                            .update("nivel", faseConcluida + 1) // Libera o próximo nível
                            .addOnSuccessListener {
                                Toast.makeText(this, "Progresso Salvo!", Toast.LENGTH_SHORT).show()
                                onSuccess()
                            }
                    } else {
                        // Se já tinha passado dessa fase, só volta
                        onSuccess()
                    }
                }
            }
    }

    // --- LÓGICA PARA ATUALIZAR PERFIL ---
    private fun atualizarPerfil(
        db: FirebaseFirestore,
        nomeUsuario: String,
        novaSenha: String,
        novoAvatarId: Int,
        onSuccess: () -> Unit
    ) {
        db.collection("usuarios")
            .whereEqualTo("nome", nomeUsuario)
            .get()
            .addOnSuccessListener { documents ->
                if (!documents.isEmpty) {
                    val docId = documents.documents[0].id

                    // Atualiza os campos
                    db.collection("usuarios").document(docId)
                        .update(
                            mapOf(
                                "senha" to novaSenha,
                                "avatarId" to novoAvatarId
                            )
                        )
                        .addOnSuccessListener {
                            Toast.makeText(this, "Perfil atualizado com sucesso!", Toast.LENGTH_SHORT).show()
                            onSuccess()
                        }
                        .addOnFailureListener {
                            Toast.makeText(this, "Erro ao atualizar.", Toast.LENGTH_SHORT).show()
                        }
                }
            }
    }
}