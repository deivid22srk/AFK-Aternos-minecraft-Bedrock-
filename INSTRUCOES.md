# 📱 Instruções Completas - HailGames Bot Android

## 🚀 Opção 1: Compilar com GitHub Actions (MAIS FÁCIL!)

### Passo 1: Subir para GitHub

```bash
# No seu computador, dentro da pasta do projeto:
git init
git add .
git commit -m "Initial commit - HailGames Bot"
git branch -M main
git remote add origin https://github.com/SEU_USUARIO/HailGames-Bot.git
git push -u origin main
```

### Passo 2: Compilar Automaticamente

1. Vá no seu repositório no GitHub
2. Clique em **Actions** (aba superior)
3. Clique no workflow **"Android CI"**
4. Clique em **"Run workflow"** → **"Run workflow"**
5. Aguarde ~5-10 minutos
6. Quando terminar, clique no workflow executado
7. Role até embaixo, em **Artifacts**
8. Baixe **"HailGames-Bot-Release"**
9. Extraia o ZIP e instale o APK no Android

**Pronto! APK compilado sem precisar instalar nada!** ✅

---

## 🛠️ Opção 2: Compilar Localmente (Android Studio)

### Passo 1: Instalar Android Studio

1. Baixe: https://developer.android.com/studio
2. Instale normalmente
3. Abra e aguarde download de SDKs

### Passo 2: Abrir Projeto

1. Android Studio → **Open**
2. Selecione pasta `HailGamesBot-Android`
3. Aguarde sync do Gradle (primeira vez demora!)

### Passo 3: Compilar

**Debug (para testar):**
```
Build → Build Bundle(s) / APK(s) → Build APK(s)
```

**Release (versão final):**
```
Build → Generate Signed Bundle / APK
→ APK
→ Create new... (crie keystore)
→ Finish
```

APK estará em: `app/build/outputs/apk/`

---

## 💻 Opção 3: Compilar por Linha de Comando

### Requisitos:
- Java JDK 17
- Android SDK

### Windows:

```cmd
cd HailGamesBot-Android
gradlew.bat assembleDebug
```

### Mac/Linux:

```bash
cd HailGamesBot-Android
chmod +x gradlew
./gradlew assembleDebug
```

APK: `app/build/outputs/apk/debug/app-debug.apk`

---

## 📦 Após Compilar

### Instalar no Android:

1. **Ative "Fontes Desconhecidas":**
   - Configurações → Segurança
   - Ative "Instalar apps desconhecidos"

2. **Transfira APK para o celular:**
   - USB, Bluetooth, ou Drive

3. **Instale:**
   - Abra o APK no celular
   - Clique "Instalar"

4. **Configure:**
   - Abra app
   - Clique em ⚙️ (configurações)
   - Configure servidor/bot
   - Salve e inicie!

---

## ⚠️ Arquivos Importantes Faltando

O projeto está completo, mas você precisa adicionar:

### 1. Ícone do App

Crie/baixe um ícone e adicione em:
```
app/src/main/res/
├── mipmap-mdpi/ic_launcher.png (48x48)
├── mipmap-hdpi/ic_launcher.png (72x72)
├── mipmap-xhdpi/ic_launcher.png (96x96)
├── mipmap-xxhdpi/ic_launcher.png (144x144)
└── mipmap-xxxhdpi/ic_launcher.png (192x192)
```

**Gerar ícones facilmente:**
- https://icon.kitchen/ (grátis)
- Faça upload de imagem
- Download e substitua

### 2. Gradle Wrapper JAR

Se compilação der erro "gradle-wrapper.jar not found":

```bash
# Gerar wrapper completo:
gradle wrapper
```

Ou baixe de outro projeto Android e copie:
```
gradle/wrapper/gradle-wrapper.jar
```

---

## 🔧 Solução de Problemas

### "SDK location not found"

Crie arquivo `local.properties`:
```properties
sdk.dir=/caminho/para/seu/Android/Sdk
```

Windows: `C\:\\Users\\SeuUsuario\\AppData\\Local\\Android\\Sdk`
Mac: `/Users/SeuUsuario/Library/Android/sdk`
Linux: `/home/usuario/Android/Sdk`

### "Java version" erro

Certifique-se de ter Java JDK 17:
```bash
java -version
```

Se não tiver, instale: https://adoptium.net/

### Gradle sync failed

1. File → Invalidate Caches → Invalidate and Restart
2. Ou delete pasta `.gradle` e sync novamente

### CloudburstMC não baixa

Adicione em `settings.gradle.kts`:
```kotlin
maven { url = uri("https://repo.opencollab.dev/maven-snapshots/") }
```

(Já está no projeto!)

---

## 📊 Estrutura Final

```
HailGamesBot-Android/
├── app/
│   ├── src/main/
│   │   ├── java/com/hailgames/bot/
│   │   │   ├── MainActivity.kt
│   │   │   ├── service/MinecraftBotService.kt
│   │   │   ├── ui/screen/
│   │   │   ├── ui/theme/
│   │   │   └── viewmodel/
│   │   ├── res/
│   │   │   ├── values/
│   │   │   ├── xml/
│   │   │   └── mipmap-*/  ← ADICIONE ÍCONES AQUI
│   │   └── AndroidManifest.xml
│   ├── build.gradle.kts
│   └── proguard-rules.pro
├── gradle/
│   └── wrapper/
│       ├── gradle-wrapper.properties
│       └── gradle-wrapper.jar  ← Gere com 'gradle wrapper'
├── .github/workflows/build.yml
├── build.gradle.kts
├── settings.gradle.kts
├── gradle.properties
├── gradlew
├── gradlew.bat  ← Crie para Windows
├── .gitignore
└── README.md
```

---

## ✅ Checklist Antes de Compilar

- [ ] Java JDK 17 instalado
- [ ] Android Studio instalado (se for usar)
- [ ] Gradle wrapper gerado (`gradle wrapper`)
- [ ] Ícones adicionados em `mipmap-*`
- [ ] `local.properties` criado (se necessário)
- [ ] Conexão internet (para download de dependências)

---

## 🎯 Compilação Rápida (Resumo)

### GitHub Actions (Recomendado):
```bash
git init && git add . && git commit -m "Init"
# Suba para GitHub
# Actions → Run workflow → Baixe APK
```

### Local:
```bash
./gradlew assembleRelease
# APK em: app/build/outputs/apk/release/
```

---

## 💡 Dicas

### Reduzir tamanho do APK:

Em `build.gradle.kts`, adicione:
```kotlin
android {
    buildTypes {
        release {
            isMinifyEnabled = true  // ← Já está ativado!
            isShrinkResources = true  // ← Adicione isso
        }
    }
}
```

### Debug mais rápido:

```bash
./gradlew installDebug  # Instala direto no celular conectado
```

### Ver logs do app:

```bash
adb logcat | grep HailGames
```

---

## 📞 Suporte

Problemas na compilação?

1. Verifique Java version: `java -version`
2. Limpe projeto: `./gradlew clean`
3. Delete `.gradle/` e tente novamente
4. Use GitHub Actions (mais fácil!)

---

**HailGames Afk Bot** - Projeto Android completo! 🚀📱
