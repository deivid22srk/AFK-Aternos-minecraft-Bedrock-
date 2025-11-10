# 🎮 HailGames Afk Bot - Android App

Bot AFK profissional para Minecraft Bedrock Edition com interface Material You.

## ✨ Recursos

- ✅ **Material You (Material 3)** - Design moderno e adaptável
- ✅ **Jetpack Compose** - Interface declarativa
- ✅ **Foreground Service** - Roda em background 24/7
- ✅ **Anti-AFK** - Pula e agacha automaticamente
- ✅ **Mensagens Automáticas** - Envia mensagens personalizadas
- ✅ **CloudburstMC Protocol** - Biblioteca Bedrock profissional
- ✅ **Configurações Completas** - Personalize tudo
- ✅ **Auto-Reconexão** - Reconecta automaticamente

## 📱 Requisitos

- Android 7.0 (API 24) ou superior
- Conexão à internet
- Servidor Minecraft Bedrock

## 🚀 Como Usar

### Opção 1: Baixar APK pronto

1. Vá em [Releases](../../releases)
2. Baixe `HailGames-Bot-Release.apk`
3. Instale no Android
4. Abra e configure!

### Opção 2: Compilar com GitHub Actions

1. Faça fork deste repositório
2. Vá em **Actions** → **Android CI**
3. Clique em **Run workflow**
4. Aguarde compilação
5. Baixe o APK em **Artifacts**

### Opção 3: Compilar localmente

```bash
# Clone o repositório
git clone https://github.com/seu-usuario/HailGamesBot-Android.git
cd HailGamesBot-Android

# Compile
./gradlew assembleDebug

# APK estará em: app/build/outputs/apk/debug/app-debug.apk
```

## ⚙️ Configuração

1. Abra o app
2. Clique no ícone de **Configurações** (⚙️)
3. Configure:
   - **Servidor**: IP e porta do servidor
   - **Nome do Bot**: Nome que aparecerá no servidor
   - **Anti-AFK**: Ative para pular/agachar automaticamente
   - **Mensagens**: Configure mensagens automáticas
4. Clique em **SALVAR CONFIGURAÇÕES**
5. Volte e clique em **INICIAR BOT**

## 📋 Funcionalidades Detalhadas

### Anti-AFK
- Pula a cada 3 segundos
- Opção de agachar automaticamente
- Evita kick por inatividade

### Mensagens Automáticas
- Múltiplas mensagens (uma por linha)
- Delay configurável entre mensagens
- Rotação automática

### Foreground Service
- Roda em background
- Notificação persistente
- WakeLock para evitar suspensão

### Interface
- Material You dinâmico
- Dark/Light mode automático
- Animações fluidas
- Status em tempo real

## 🛠️ Tecnologias

- **Kotlin** - Linguagem principal
- **Jetpack Compose** - UI moderna
- **Material 3** - Material You
- **CloudburstMC Protocol** - Protocolo Bedrock
- **Netty** - Networking assíncrono
- **Coroutines** - Programação assíncrona
- **Foreground Service** - Background execution

## 📦 Estrutura do Projeto

```
app/
├── src/main/
│   ├── java/com/hailgames/bot/
│   │   ├── MainActivity.kt
│   │   ├── service/
│   │   │   └── MinecraftBotService.kt
│   │   ├── ui/
│   │   │   ├── screen/
│   │   │   │   ├── MainScreen.kt
│   │   │   │   └── SettingsScreen.kt
│   │   │   └── theme/
│   │   │       ├── Theme.kt
│   │   │       ├── Color.kt
│   │   │       └── Type.kt
│   │   └── viewmodel/
│   │       └── BotViewModel.kt
│   ├── AndroidManifest.xml
│   └── res/
└── build.gradle.kts
```

## 🔧 Solução de Problemas

### Bot não conecta
- Verifique IP e porta do servidor
- Certifique-se que o servidor está online
- Verifique conexão à internet

### App fecha sozinho
- Desative otimizações de bateria para o app
- Permita execução em segundo plano
- Configurações → Apps → HailGames Bot → Bateria → Sem restrições

### Mensagens não enviam
- Verifique se mensagens estão ativadas
- Verifique delay (não muito baixo)
- Certifique-se que bot está conectado

## 📄 Licença

MIT License - Use livremente!

## 🤝 Contribuir

1. Fork o projeto
2. Crie uma branch (`git checkout -b feature/nova-feature`)
3. Commit suas mudanças (`git commit -am 'Add nova feature'`)
4. Push para a branch (`git push origin feature/nova-feature`)
5. Abra um Pull Request

## 🎯 Roadmap

- [ ] Suporte para múltiplos servidores
- [ ] Histórico de conexões
- [ ] Estatísticas de uptime
- [ ] Widget na tela inicial
- [ ] Backup/restore de configurações
- [ ] Temas personalizados

## 📞 Suporte

Problemas? Abra uma [issue](../../issues)!

---

**HailGames Afk Bot** - Bot profissional para Minecraft Bedrock! 🎮✨
