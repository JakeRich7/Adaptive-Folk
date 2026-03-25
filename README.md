## The Dream
I have always wanted to see 'smart' NPCs that use AI to become dynamic entities that respond realistically and with memory. This is my first attempt at making that dream a reality.

Important Note: AI has massive limitations and dangers that I am acutely aware of, but having AI-powered NPCs is something that I believe can create unique, immersive, and happy experiences for players. That has been a powerful enough motivation for me to continue creating this mod.
___

## Overview

Kweebec are transformed from static NPCs into AI-powered interactive characters.

Each Kweebec:
- Has a unique name
- Responds in-character using locally generated AI dialogue
- Remembers past conversations

The result is a more immersive world where Kweebec feel more aware, reactive, and alive.

For Example:
- I asked a Kweebec who Harry Potter was and it did not know
- I taught the Kweebec who Harry Potter was
- I asked the Kweebec about Harry Potter and it was able to tell me about him

---

## Features

### NPC Identity
- Name assigned to NPC on spawn from a curated list of 'plant-people-like' names

### AI-powered Dialogue
- AI-powered conversations with any Kweebec via in-game chat
- Fully local via Ollama backend (Open-source software)
  - No costs
  - No limits
  - No internet required
- Low-latency (typically under 2 seconds)

### Persistent Memory System
- NPC conversation history stored in JSONL documents (Path to AI memory stored here: `(world)/mods/Adaptive_Folk_Kweebecs)`
  - Summarized by 'Summarizer AI Agent' to give context to 'Kweebec AI Agent' to use
- NPCs can reference previous interactions with multiple players

### Graceful Fallback Mode
- If AI is not installed (Ollama + any LLM) Kweebec still retain their new name and respond using simple fallback responses.
  
---

## Architecture
1. Player speaks via in-game chat
2. Summarizer AI Agent reads the NPC’s JSONL file
3. Summarizer AI Agent passes memory context to Kweebec AI Agent
4. Kweebec AI Agent responds in-character via in-game chat OR responds with simple fallback response if LLM is not available

---

## Requirements

### Full Experience (Names, AI-Powered Kweebec Responses, Conversation Memory)
- Place .jar in Hytale 'Mods' folder
- Install and run Ollama Open-source software (https://ollama.com/download)
- Use any CLI (e.g. powershell or command line) to pull a model for Ollama to use `ollama pull llama3.2:3b`
  - (Optional) Substitute `llama3.2:3b` with ANY available model (https://ollama.com/library)
- Launch Hytale, select Adaptive Folk mod, and speak to any Kweebec!

### Minimal Experience (Names, Basic Kweebec Responses)
- Place .jar in Hytale 'Mods' folder
- Launch Hytale, select Adaptive Folk mod, and speak to any Kweebec!

---

## Additional Information Regarding LLMs
### Recommended list of LLMs:
- `llama3.2:3b` (My recommendation AND my preferred model while testing on my RTX 3060 Ti)
- `llama3.2:1b` (My recommendation for computers with less power than an RTX 3060 Ti)
- `gemma3:4b`
- `gemma3:1b`
- `qwen2.5:3b`
- `qwen2.5:7b`
- `mistral:7b`
- `gemma3:12b`

### Important Notes about local LLM use:
- Any LLM from the Ollama library above will work IF your hardware supports it. I found my RTX 3060 Ti (8gb VRAM) to run very well with the `llama3.2:3b` model listed above. VRAM will likely be your bottleneck here
- Guardrails vary from model to model! I found `llama3.2:3b` to be a very good model for safe responses

---

## Design Principles
- Immersion first (names + smart and in-character responses + memory)
- Local AI only (privacy + zero cost + performance)
- Graceful degradation (always functional)
- Minimal latency

---

## Special Thanks:
- Ollama
- https://britakee-studios.gitbook.io/hytale-modding-documentation
- https://hytalemodding.dev/en/docs
- https://hytale.limetta.dev/
- Scheletro