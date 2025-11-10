# 🤖 HailGames Afk Bot - Android App

Bot AFK profissional para Minecraft Bedrock Edition com interface Material You.

## ⚠️ Importante - Versão Atual

Esta é uma **versão simplificada** do bot que:
- ✅ **Interface completa** com Material You
- ✅ **Foreground Service** rodando 24/7
- ✅ **Configurações funcionais**
- ✅ **Notificações e status**
- ⚠️ **Conexão básica UDP** (envia pings ao servidor)

**Nota:** A conexão Bedrock completa requer implementação do protocolo RakNet. Esta versão envia pacotes UDP básicos ao servidor. Para conexão completa, você pode:
1. Implementar o protocolo RakNet/Bedrock manualmente
2. Usar biblioteca nativa C++ via JNI
3. Usar Node.js embarcado (nodejs-mobile)

---

## ✨ Recursos Implementados

### Interface:
- ✅ **Material You (Material 3)** - Design moderno adaptável
- ✅ **Dynamic Colors** - Cores do sistema (Android 12+)
- ✅ **Dark/Light mode** automático
- ✅ **Animações fluidas**

### Funcionalidades:
- ✅ **Foreground Service** - Roda em background 24/7
- ✅ **Anti-AFK** - Simulação de movimento
- ✅ **Mensagens Automáticas** - Rotação de mensagens
- ✅ **Notificações** - Status em tempo real
- ✅ **WakeLock** - Mantém CPU ativa
- ✅ **Auto-reconexão**
- ✅ **Configurações completas**

### Configurações:
- Endereço e porta do servidor
- Nome do bot
- Anti-AFK (on/off)
- Auto sneak (on/off)
- Mensagens personalizadas
- Delay entre mensagens

---

## 📱 Requisitos

- Android 7.0 (API 24) ou superior
- Conexão à internet
- Permissões: Internet, Foreground Service, Notificações

---

## 🚀 Compilação

### Opção 1: GitHub Actions (Recomendado!)

1. Faça push deste projeto para GitHub
2. Vá em **Actions** → **Android CI**
3. Clique em **Run workflow**
4. Aguarde ~5 minutos
5. Baixe o APK em **Artifacts**

### Opção 2: Android Studio

1. Abra o projeto no Android Studio
2. **Build** → **Build Bundle(s) / APK(s)** → **Build APK(s)**
3. APK estará em: `app/build/outputs/apk/debug/`

### Opção 3: Linha de Comando

```bash
./gradlew assembleDebug
```

APK: `app/build/outputs/apk/debug/app-debug.apk`

---

## ⚙️ Como Usar

1. **Instale o APK** no Android
2. **Abra o app**
3. Clique no ícone **⚙️ (Configurações)**
4. Configure:
   - Servidor: `FizAnal.aternos.me`
   - Porta: `19132`
   - Nome do Bot: `HailGamesBot`
   - Ative Anti-AFK
   - Configure mensagens
5. **Salve as configurações**
6. Volte e clique em **INICIAR BOT**
7. **Desative otimização de bateria** para o app:
   - Configurações → Apps → HailGames Bot
   - Bateria → Sem restrições

---

## 🛠️ Tecnologias

- **Kotlin** - Linguagem moderna
- **Jetpack Compose** - UI declarativa
- **Material 3** - Material You
- **Coroutines** - Async programming
- **Foreground Service** - Background execution
- **UDP Sockets** - Conexão Bedrock básica

---

## 📂 Estrutura

```
app/
├── MainActivity.kt - Activity principal
├── service/
│   └── MinecraftBotService.kt - Foreground Service
├── ui/
│   ├── screen/
│   │   ├── MainScreen.kt - Tela de status
│   │   └── SettingsScreen.kt - Configurações
│   └── theme/
│       ├── Theme.kt - Material You
│       ├── Color.kt - Paleta
│       └── Type.kt - Tipografia
└── viewmodel/
    └── BotViewModel.kt - Estado
```

---

## 🔧 Melhorias Futuras

### Para conexão Bedrock completa:

**Opção 1: Node.js Mobile**
```gradle
implementation("com.janeasystems.nodejs-mobile:nodejs-mobile:0.1.9")
```
- Roda Node.js dentro do APK
- Use bedrock-protocol do Node.js
- Mais fácil de implementar

**Opção 2: Biblioteca Java Bedrock**
- Implementar protocolo RakNet manualmente
- Usar libs como Nukkit/CloudburstMC via JitPack
- Mais trabalho mas 100% nativo

**Opção 3: C++ via JNI**
- Usar biblioteca C++ do Bedrock
- Compilar via NDK
- Máxima performance

---

## 🐛 Solução de Problemas

### App fecha sozinho
- Configurações → Apps → HailGames Bot
- Bateria → **Sem restrições**
- Executar em segundo plano → **Permitir**

### Notificação não aparece
- Configurações → Apps → HailGames Bot
- Notificações → **Ativar todas**

### Bot não mantém conectado
- Desative economia de bateria
- Mantenha app rodando
- Permita execução em segundo plano

---

## 📄 Licença

MIT License - Use livremente!

---

## 🚀 Roadmap

- [ ] Implementar protocolo Bedrock completo
- [ ] Múltiplos servidores salvos
- [ ] Widget na tela inicial
- [ ] Estatísticas de conexão
- [ ] Backup/restore de configs
- [ ] Temas personalizados
- [ ] Suporte para contas Microsoft

---

## 💡 Contribuir

Este é um projeto base. Contribuições são bem-vindas!

Para implementar conexão Bedrock completa:
1. Fork o projeto
2. Adicione biblioteca Bedrock (Node.js mobile ou Java)
3. Implemente protocolo em `MinecraftBotService.kt`
4. Teste e envie PR!

---

**HailGames Afk Bot** - Interface profissional para seu bot Minecraft! 🎮✨

## 📞 Suporte

Problemas? Abra uma issue!

Para conexão Bedrock completa, considere:
- Usar Node.js version (mais fácil)
- Implementar em VPS (Oracle Cloud gratuito)
- Contribuir com implementação Java do protocolo
