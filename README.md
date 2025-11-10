# HailGames Afk Bot

<p align="center">
    🎮 Bot AFK funcional para servidores Minecraft Bedrock Edition
</p>

<p align="center">
    Anti-AFK, Auto-Auth, Suporte para contas offline/online
</p>

## 📋 Sobre

HailGames Afk Bot é um bot especializado para manter seu personagem ativo em servidores Minecraft Bedrock Edition. Perfeito para servidores Aternos e outros que exigem jogadores online.

## ⚙️ Configuração

### Pré-requisitos

1. [Download](https://nodejs.org/en/download/) e instale o Node.JS
2. Clone ou extraia este projeto

### Instalação

1. Abra o terminal/CMD na pasta do bot
2. Execute o comando: `npm install`

### Configuração

Edite o arquivo `settings.json` com suas configurações:

```json
{
  "bot-account": {
    "username": "HailGamesBot",  // Nome do bot
    "password": "",              // Deixe vazio para offline
    "type": "offline"            // "offline" ou "microsoft"
  },

  "server": {
    "ip": "FizAnal.aternos.me",  // IP do servidor
    "port": 19132,               // Porta Bedrock (padrão: 19132)
    "version": "1.21.50"         // Versão do Minecraft
  },
  
  "position": {
    "enabled": false,            // Mover para posição específica
    "x": 0,
    "y": 0,
    "z": 0
  },

  "utils": {
    "auto-auth": {
      "enabled": false,          // Auto login (se servidor tiver)
      "password": ""
    },

    "anti-afk": {
      "enabled": true,           // Anti-AFK (pular/agachar)
      "sneak": true              // Agachar enquanto pula
    },

    "chat-messages": {
      "enabled": true,           // Enviar mensagens no chat
      "repeat": true,            // Repetir mensagens
      "repeat-delay": 60,        // Delay entre mensagens (segundos)

      "messages": [
        "HailGames Afk Bot",
        "Keeping server active!",
        "Bedrock Edition Bot"
      ]
    },

    "chat-log": true,            // Mostrar chat no console
    "auto-reconnect": true,      // Reconectar se desconectar
    "auto-recconect-delay": 5000 // Delay para reconectar (ms)
  }
}
```

## 🚀 Iniciar o Bot

Execute o comando:

```bash
node index.js
```

ou

```bash
npm start
```

O bot irá:
- ✅ Conectar ao servidor
- ✅ Enviar mensagens automáticas
- ✅ Realizar ações anti-AFK
- ✅ Reconectar automaticamente se desconectar

## 🎯 Recursos

- ✅ **Anti-AFK**: Pula e agacha automaticamente
- ✅ **Mensagens no Chat**: Envia mensagens personalizadas
- ✅ **Auto-Reconexão**: Reconecta automaticamente
- ✅ **Auto-Auth**: Suporte para login automático
- ✅ **Chat Log**: Visualize mensagens do servidor
- ✅ **Suporte Bedrock**: Versões 1.16 até 1.21+
- ✅ **Servidor Web**: Interface básica na porta 8000

## 📦 Estrutura de Arquivos

```
HailGames-Afk-Bot/
├── index.js              # Código principal do bot
├── settings.json         # Configurações
├── package.json          # Dependências
├── README.md            # Documentação
└── launcher_accounts.json # Contas (opcional)
```

## 🔧 Solução de Problemas

### Bot não conecta
- Verifique se o IP e porta estão corretos
- Confirme se o servidor está online
- Para Aternos, use o endereço completo (ex: `server.aternos.me`)
- Porta padrão Bedrock: 19132

### Erro de versão
- Atualize `bedrock-protocol`: `npm update bedrock-protocol`
- Verifique a versão do servidor e ajuste em `settings.json`

### Bot desconecta
- Ative `auto-reconnect` em `settings.json`
- Ajuste o `auto-recconect-delay` se necessário

## 📝 Notas

- **Bedrock Edition**: Este bot funciona apenas com Minecraft Bedrock (Windows 10, Mobile, Console, etc.)
- **Java Edition**: Para Java Edition, use o bot original com `mineflayer`
- **Contas Offline**: Funcionam na maioria dos servidores Bedrock
- **Microsoft Account**: Configure `"type": "microsoft"` e adicione credenciais

## 📄 Licença

MIT License - Sinta-se livre para usar e modificar

## 🎮 Versões Suportadas

- Minecraft Bedrock: 1.16.x - 1.21.x
- Testado em: Aternos, Realms, Servidores Dedicados

---

**HailGames Afk Bot** - Mantendo seus servidores sempre ativos! 🚀
