<template>
  <div class="agent-page">
    <header class="agent-header">
      <div class="header-main">
        <div class="avatar">AI</div>
        <div class="header-text">
          <h1>购物助手</h1>
          <p>描述需求，帮你选品、对比、推荐</p>
        </div>
      </div>
      <button class="pref-btn" @click="goPreferences">偏好</button>
    </header>

    <div ref="chatBodyRef" class="chat-body">
      <div
        v-for="msg in messages"
        :key="msg.id"
        class="message-row"
        :class="msg.role"
      >
        <div v-if="msg.role === 'assistant'" class="msg-avatar">AI</div>

        <div class="msg-content">
          <div v-if="msg.type === 'text'" class="bubble text-bubble">
            {{ msg.content }}
          </div>

          <template v-else-if="msg.type === 'rich'">
            <div v-if="msg.content" class="bubble text-bubble">{{ msg.content }}</div>

            <div v-if="msg.products?.length" class="product-scroll">
              <AgentProductCard
                v-for="product in msg.products"
                :key="product.spuId"
                :product="product"
                :tag="getProductTag(product.spuId, msg.compare)"
                :tag-type="getProductTagType(product.spuId, msg.compare)"
              />
            </div>

            <AgentComparePanel
              v-if="msg.compare"
              class="compare-block"
              :specs="msg.compare.specs"
              :items="msg.compare.items"
              :recommendation="msg.compare.recommendation"
              :highlights="msg.compare.highlights"
            />
          </template>
        </div>
      </div>

      <div v-if="typing" class="message-row assistant">
        <div class="msg-avatar">AI</div>
        <div class="bubble typing-bubble">
          <span /><span /><span />
        </div>
      </div>
    </div>

    <div class="quick-prompts" v-if="showQuickPrompts">
      <button
        v-for="prompt in quickPrompts"
        :key="prompt"
        class="prompt-chip"
        @click="sendPrompt(prompt)"
      >
        {{ prompt }}
      </button>
    </div>

    <div class="chat-input-bar">
      <input
        v-model="inputText"
        type="text"
        placeholder="描述你想要的商品，如：2000元以内的轻薄本"
        :disabled="typing"
        @keyup.enter="handleSend"
      />
      <button class="send-btn" :disabled="!inputText.trim() || typing" @click="handleSend">
        发送
      </button>
    </div>
  </div>
</template>

<script setup>
import { nextTick, onMounted, ref } from 'vue'
import { useRouter } from 'vue-router'
import AgentProductCard from '@/components/agent/AgentProductCard.vue'
import AgentComparePanel from '@/components/agent/AgentComparePanel.vue'
import { chat } from '@/api/agent'
import { getToken } from '@/utils/auth'

const router = useRouter()
const chatBodyRef = ref(null)
const inputText = ref('')
const typing = ref(false)
const showQuickPrompts = ref(true)
let msgId = 0

const quickPrompts = [
  '2000元以内的手机',
  '轻便运动鞋推荐',
  '适合送礼的礼盒'
]

const messages = ref([
  {
    id: nextId(),
    role: 'assistant',
    type: 'text',
    content: '你好，我是购物助手。你可以告诉我预算、用途或偏好，我会为你推荐商品并做参数对比。'
  }
])

function nextId() {
  msgId += 1
  return msgId
}

function scrollToBottom() {
  nextTick(() => {
    const el = chatBodyRef.value
    if (el) el.scrollTop = el.scrollHeight
  })
}

function ensureLogin() {
  if (!getToken()) {
    router.push('/login')
    return false
  }
  return true
}

function goPreferences() {
  if (!ensureLogin()) return
  router.push('/agent/preferences')
}

function getProductTag(spuId, compare) {
  const item = compare?.items?.find(entry => entry.spuId === spuId)
  if (item?.recommended) return '推荐'
  return ''
}

function getProductTagType(spuId, compare) {
  const item = compare?.items?.find(entry => entry.spuId === spuId)
  if (item?.recommended) return 'recommend'
  return 'default'
}

function mapAssistantMessage(data) {
  const hasProducts = data.products && data.products.length > 0
  if (!hasProducts) {
    return {
      id: nextId(),
      role: 'assistant',
      type: 'text',
      content: data.reply || '暂时没有找到合适商品，请补充更多需求。'
    }
  }
  return {
    id: nextId(),
    role: 'assistant',
    type: 'rich',
    content: data.reply,
    products: data.products,
    compare: data.compare
  }
}

function sendPrompt(text) {
  inputText.value = text
  handleSend()
}

async function handleSend() {
  const text = inputText.value.trim()
  if (!text || typing.value) return
  if (!ensureLogin()) return

  showQuickPrompts.value = false
  messages.value.push({
    id: nextId(),
    role: 'user',
    type: 'text',
    content: text
  })
  inputText.value = ''
  scrollToBottom()

  typing.value = true
  scrollToBottom()

  try {
    const data = await chat({ message: text })
    messages.value.push(mapAssistantMessage(data))
  } catch (err) {
    const isTimeout = err?.message?.includes('timeout')
    messages.value.push({
      id: nextId(),
      role: 'assistant',
      type: 'text',
      content: isTimeout
        ? '助手思考时间较长，请稍等后重试。若多次超时，请检查 learn-agent 服务与 DeepSeek 网络连接。'
        : (err?.message || '助手暂时不可用，请稍后重试。')
    })
  } finally {
    typing.value = false
    scrollToBottom()
  }
}

onMounted(scrollToBottom)
</script>

<style scoped>
.agent-page {
  display: flex;
  flex-direction: column;
  height: 100vh;
  height: 100dvh;
  padding-bottom: calc(68px + env(safe-area-inset-bottom, 0px));
  background: linear-gradient(180deg, #eef4ff 0%, #f7f8fa 180px);
}

.agent-header {
  flex-shrink: 0;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 16px 16px 12px;
}

.header-main {
  display: flex;
  align-items: center;
  gap: 12px;
}

.avatar {
  width: 44px;
  height: 44px;
  border-radius: 14px;
  background: linear-gradient(135deg, #667eea, #764ba2);
  color: #fff;
  font-size: 14px;
  font-weight: 700;
  display: flex;
  align-items: center;
  justify-content: center;
  box-shadow: 0 4px 12px rgba(102, 126, 234, 0.35);
}

.header-text h1 {
  margin: 0;
  font-size: 18px;
  font-weight: 600;
  color: #323233;
}

.header-text p {
  margin: 4px 0 0;
  font-size: 12px;
  color: #969799;
}

.pref-btn {
  border: none;
  padding: 6px 12px;
  border-radius: 14px;
  background: rgba(102, 126, 234, 0.12);
  color: #667eea;
  font-size: 12px;
  font-weight: 600;
}

.chat-body {
  flex: 1;
  overflow-y: auto;
  padding: 0 12px 12px;
  -webkit-overflow-scrolling: touch;
}

.message-row {
  display: flex;
  gap: 8px;
  margin-bottom: 16px;
}

.message-row.user {
  flex-direction: row-reverse;
}

.msg-avatar {
  flex-shrink: 0;
  width: 32px;
  height: 32px;
  border-radius: 10px;
  background: linear-gradient(135deg, #667eea, #764ba2);
  color: #fff;
  font-size: 11px;
  font-weight: 700;
  display: flex;
  align-items: center;
  justify-content: center;
}

.msg-content {
  max-width: calc(100% - 44px);
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.message-row.user .msg-content {
  align-items: flex-end;
}

.bubble {
  padding: 10px 14px;
  border-radius: 16px;
  font-size: 14px;
  line-height: 1.5;
  word-break: break-word;
}

.text-bubble {
  background: #fff;
  color: #323233;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
}

.message-row.user .text-bubble {
  background: linear-gradient(135deg, #1989fa, #4fc3f7);
  color: #fff;
  border-bottom-right-radius: 4px;
}

.message-row.assistant .text-bubble {
  border-bottom-left-radius: 4px;
}

.product-scroll {
  display: flex;
  gap: 10px;
  overflow-x: auto;
  padding-bottom: 4px;
  -webkit-overflow-scrolling: touch;
}

.product-scroll::-webkit-scrollbar {
  display: none;
}

.compare-block {
  width: 100%;
  max-width: 100%;
}

.typing-bubble {
  display: flex;
  align-items: center;
  gap: 4px;
  padding: 14px 18px;
  background: #fff;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
  border-bottom-left-radius: 4px;
}

.typing-bubble span {
  width: 6px;
  height: 6px;
  border-radius: 50%;
  background: #c8c9cc;
  animation: typing 1.2s infinite ease-in-out;
}

.typing-bubble span:nth-child(2) {
  animation-delay: 0.15s;
}

.typing-bubble span:nth-child(3) {
  animation-delay: 0.3s;
}

@keyframes typing {
  0%, 80%, 100% {
    transform: scale(0.8);
    opacity: 0.5;
  }
  40% {
    transform: scale(1);
    opacity: 1;
  }
}

.quick-prompts {
  flex-shrink: 0;
  display: flex;
  gap: 8px;
  padding: 0 12px 8px;
  overflow-x: auto;
  -webkit-overflow-scrolling: touch;
}

.prompt-chip {
  flex-shrink: 0;
  border: 1px solid rgba(102, 126, 234, 0.25);
  background: rgba(255, 255, 255, 0.9);
  color: #667eea;
  border-radius: 16px;
  padding: 6px 12px;
  font-size: 12px;
  white-space: nowrap;
}

.chat-input-bar {
  flex-shrink: 0;
  display: flex;
  align-items: center;
  gap: 8px;
  margin: 0 12px 10px;
  padding: 8px 8px 8px 14px;
  background: #fff;
  border-radius: 24px;
  box-shadow: 0 2px 16px rgba(0, 0, 0, 0.08);
}

.chat-input-bar input {
  flex: 1;
  border: none;
  outline: none;
  font-size: 14px;
  background: transparent;
  min-width: 0;
}

.chat-input-bar input::placeholder {
  color: #c8c9cc;
  font-size: 13px;
}

.send-btn {
  flex-shrink: 0;
  border: none;
  border-radius: 18px;
  padding: 8px 16px;
  font-size: 13px;
  font-weight: 600;
  color: #fff;
  background: linear-gradient(135deg, #667eea, #764ba2);
}

.send-btn:disabled {
  opacity: 0.45;
}
</style>
