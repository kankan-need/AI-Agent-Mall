import { getToken } from '@/utils/auth'

const STORAGE_PREFIX = 'learn_mall_h5_agent_chat'

function getStorageKey() {
  const token = getToken()
  if (!token) return null
  return `${STORAGE_PREFIX}_${token.slice(-32)}`
}

export function loadAgentChatState() {
  const key = getStorageKey()
  if (!key) return null
  try {
    const raw = localStorage.getItem(key)
    if (!raw) return null
    const data = JSON.parse(raw)
    if (!Array.isArray(data.messages) || !data.messages.length) return null
    return data
  } catch {
    return null
  }
}

export function saveAgentChatState(messages, nextMsgId) {
  const key = getStorageKey()
  if (!key) return
  localStorage.setItem(key, JSON.stringify({ messages, nextMsgId }))
}

export function clearAgentChatState() {
  const key = getStorageKey()
  if (key) localStorage.removeItem(key)
}

export function clearAllAgentChatState() {
  const prefix = `${STORAGE_PREFIX}_`
  const removeKeys = []
  for (let i = 0; i < localStorage.length; i += 1) {
    const key = localStorage.key(i)
    if (key && key.startsWith(prefix)) {
      removeKeys.push(key)
    }
  }
  removeKeys.forEach(key => localStorage.removeItem(key))
}
