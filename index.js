const bedrock = require('bedrock-protocol');
const express = require('express');
const config = require('./settings.json');

const app = express();

app.get('/', (req, res) => {
  res.send('HailGames Afk Bot is running');
});

app.listen(8000, () => {
  console.log('Server started on port 8000');
});

function createBot() {
   console.log('\x1b[36m[HailGames] Starting bot connection...\x1b[0m');
   
   const client = bedrock.createClient({
      host: config.server.ip,
      port: config.server.port,
      username: config['bot-account']['username'],
      offline: config['bot-account']['type'] === 'offline',
      version: config.server.version,
      profilesFolder: './profiles',
      skipPing: false
   });

   let isSpawned = false;
   let messageIndex = 0;
   let messageTimer = null;
   let antiAFKTimer = null;
   let jumpState = false;

   client.on('join', () => {
      console.log('\x1b[33m[HailGames Afk Bot] Bot joined the server!\x1b[0m');
      isSpawned = true;

      if (config.utils['auto-auth'].enabled) {
         console.log('[INFO] Started auto-auth module');
         const password = config.utils['auto-auth'].password;
         
         setTimeout(() => {
            client.queue('text', {
               type: 'chat',
               needs_translation: false,
               source_name: config['bot-account']['username'],
               message: `/register ${password} ${password}`,
               xuid: '',
               platform_chat_id: ''
            });
            console.log(`[Auth] Sent /register command.`);
         }, 2000);

         setTimeout(() => {
            client.queue('text', {
               type: 'chat',
               needs_translation: false,
               source_name: config['bot-account']['username'],
               message: `/login ${password}`,
               xuid: '',
               platform_chat_id: ''
            });
            console.log(`[Auth] Sent /login command.`);
         }, 3000);
      }

      if (config.utils['chat-messages'].enabled) {
         console.log('[INFO] Started chat-messages module');
         const messages = config.utils['chat-messages']['messages'];

         if (config.utils['chat-messages'].repeat) {
            const delay = config.utils['chat-messages']['repeat-delay'];
            
            messageTimer = setInterval(() => {
               if (isSpawned && client) {
                  client.queue('text', {
                     type: 'chat',
                     needs_translation: false,
                     source_name: config['bot-account']['username'],
                     message: messages[messageIndex],
                     xuid: '',
                     platform_chat_id: ''
                  });
                  
                  console.log(`[Chat] Sent: ${messages[messageIndex]}`);
                  
                  messageIndex = (messageIndex + 1) % messages.length;
               }
            }, delay * 1000);
         } else {
            setTimeout(() => {
               messages.forEach((msg, index) => {
                  setTimeout(() => {
                     if (isSpawned && client) {
                        client.queue('text', {
                           type: 'chat',
                           needs_translation: false,
                           source_name: config['bot-account']['username'],
                           message: msg,
                           xuid: '',
                           platform_chat_id: ''
                        });
                        console.log(`[Chat] Sent: ${msg}`);
                     }
                  }, index * 1000);
               });
            }, 2000);
         }
      }

      if (config.utils['anti-afk'].enabled) {
         console.log('[INFO] Started anti-afk module');
         
         antiAFKTimer = setInterval(() => {
            if (isSpawned && client) {
               jumpState = !jumpState;
               
               client.queue('player_action', {
                  action: jumpState ? 'jump' : 'start_swimming',
                  action_type: 0
               });

               if (config.utils['anti-afk'].sneak) {
                  client.queue('player_action', {
                     action: jumpState ? 'start_sneak' : 'stop_sneak',
                     action_type: 0
                  });
               }
            }
         }, 3000);
      }

      if (config.position.enabled) {
         console.log(
            `\x1b[32m[HailGames Afk Bot] Moving to target location (${config.position.x}, ${config.position.y}, ${config.position.z})\x1b[0m`
         );
      }
   });

   client.on('text', (packet) => {
      if (config.utils['chat-log']) {
         try {
            const message = packet.message || '';
            console.log(`[ChatLog] ${message}`);
         } catch (err) {
            console.log(`[ChatLog] Received text packet`);
         }
      }
   });

   client.on('disconnect', (packet) => {
      isSpawned = false;
      
      if (messageTimer) clearInterval(messageTimer);
      if (antiAFKTimer) clearInterval(antiAFKTimer);
      
      const reason = packet.message || 'Unknown reason';
      console.log(
         '\x1b[33m',
         `[HailGames Afk Bot] Disconnected from server. Reason: ${reason}`,
         '\x1b[0m'
      );

      if (config.utils['auto-reconnect']) {
         console.log(`[INFO] Reconnecting in ${config.utils['auto-recconect-delay'] / 1000} seconds...`);
         setTimeout(() => {
            createBot();
         }, config.utils['auto-recconect-delay']);
      }
   });

   client.on('error', (err) => {
      console.log(`\x1b[31m[ERROR] ${err.message}\x1b[0m`);
      
      if (config.utils['auto-reconnect']) {
         console.log(`[INFO] Reconnecting in ${config.utils['auto-recconect-delay'] / 1000} seconds...`);
         setTimeout(() => {
            createBot();
         }, config.utils['auto-recconect-delay']);
      }
   });

   client.on('spawn', () => {
      console.log('\x1b[32m[HailGames Afk Bot] Bot spawned in the world!\x1b[0m');
   });

   client.on('kick', (reason) => {
      isSpawned = false;
      
      if (messageTimer) clearInterval(messageTimer);
      if (antiAFKTimer) clearInterval(antiAFKTimer);
      
      console.log(
         '\x1b[33m',
         `[HailGames Afk Bot] Bot was kicked from the server. Reason: ${reason}`,
         '\x1b[0m'
      );
   });
}

createBot();
