# ChatApplication

A real-time, anonymous Android chat app inspired by stranger-pairing services. Users are randomly matched into 1-on-1 conversations over a persistent WebSocket connection, with message delivery acknowledged end-to-end and queued locally when the network drops.

Built end-to-end in **Kotlin** with **Jetpack Compose**, **Ktor WebSockets**, **Hilt**, **Room**, and **DataStore** — packaged in a clean `data / domain / presentation` Clean Architecture.

---

## Why this project

I wanted to learn how production messengers handle the hard parts that most CRUD tutorials skip:

- **Connection lifecycle** — reconnecting a WebSocket cleanly across screen rotations, backgrounding, and flaky networks.
- **At-least-once delivery** — outgoing messages are persisted, sent, ACKed by the server, and only then marked delivered. If the socket dies mid-flight, the failed-message queue retries on reconnect.
- **Random matching** — clients post a `MatchRequest`, wait for a `MatchedResponse`, then transition into a paired session.
- **Offline-first reads** — every received message lands in Room before the UI sees it, so the chat history is intact even before the socket is up.

---

## Architecture

```
┌──────────────────────── presentation ────────────────────────┐
│  Compose screens · ViewModels · Hilt-scoped UI state         │
└──────────────────────────────┬───────────────────────────────┘
                               │
┌─────────────── domain ───────┴───────────────────────────────┐
│  Repository interface · pure Kotlin models                   │
└──────────────────────────────┬───────────────────────────────┘
                               │
┌─────────────── data ─────────┴───────────────────────────────┐
│  RepositoryImpl                                              │
│    ├─ WebsocketClient (Ktor)        ← real-time channel      │
│    ├─ Room (chat_message, failed_message)                    │
│    └─ DataStore (auth token, user prefs)                     │
└──────────────────────────────────────────────────────────────┘
```

### WebSocket message protocol

The client and server exchange small JSON envelopes; the relevant request/response shapes live under `data/model/websocket/`:

| Direction | Type | Purpose |
|---|---|---|
| → server | `MatchRequest` | "Find me someone to talk to" |
| ← server | `MatchedResponse` | "You've been paired with chat-id `X`" |
| → server | `SendMessage` | Outgoing text message |
| ← server | `ReceiveMessage` | Incoming text from the paired peer |
| ↔ both | `AckRequest` / `AckResponse` | Per-message delivery confirmation |
| ← server | `SyncResponse` | Backfill of messages missed while disconnected |

If the socket drops while a `SendMessage` is in flight, the message is moved into the `failed_message` table and resent automatically on the next successful connection.

---

## Tech stack

- **Language:** Kotlin 1.9, Coroutines + Flow
- **UI:** Jetpack Compose, Material 3, Compose Navigation
- **DI:** Hilt (with `hilt-navigation-compose`)
- **Networking:** Ktor client (OkHttp engine) + content negotiation + kotlinx.serialization
- **Persistence:** Room (messages + failed-message queue), DataStore (preferences)
- **Misc:** Coil, MessageBar Compose, KSP for annotation processing

`compileSdk 35` · `minSdk 26` · `targetSdk 35` · JVM target 11

---

## Build & run

```bash
git clone https://github.com/bibekanandan892/ChatApplication.git
cd ChatApplication
./gradlew assembleDebug
```

Then either install the resulting APK on a device:

```bash
./gradlew installDebug
```

…or open the project in **Android Studio Koala or newer** and run the `app` configuration.

> The app expects a WebSocket backend that implements the protocol above. The endpoint URL is wired through Hilt in `di/AppModule.kt` — point it at your own server (or wire in your favorite chat backend) before running.

---

## Project layout

```
app/src/main/java/com/bibek/chatapplication/
├── ChatApplication.kt            # @HiltAndroidApp
├── data/
│   ├── local/
│   │   ├── appdatastore/         # AppDataStore — auth token, prefs
│   │   └── database/             # Room: ChatMessageDao, FailedMessageDao
│   ├── model/
│   │   ├── authenticate/         # Auth request/response
│   │   ├── chat/                 # ChatMessage
│   │   ├── error/                # Error, ErrorResponse
│   │   └── websocket/            # Request/response envelopes (match, ack, sync, message)
│   ├── remote/
│   │   └── WebsocketClient.kt    # Ktor WebSocket client + reconnect
│   └── repository/
│       └── RepositoryImpl.kt
├── di/
│   └── AppModule.kt              # Hilt bindings
├── domain/
│   └── repository/Repository.kt
└── presentation/
    ├── activity/                 # MainActivity, MainViewModel
    └── component/                # Reusable Compose UI components
```

---

## What I'd improve next

- Swap the JSON wire protocol for protobuf to cut bandwidth on slow networks.
- Add typing indicators and read receipts (the ACK plumbing already exists).
- Move auth into a refresh-token flow rather than a single persisted token.
- End-to-end encrypt the message payload before it hits the server.

---

## License

MIT — see [LICENSE](LICENSE).
