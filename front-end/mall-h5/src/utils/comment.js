import { getToken } from './auth'

const SEED_COMMENTS = {
  1: [
    { id: 's1-1', nickName: '小明', score: 5, content: '手机很流畅，学习版演示商品，性价比不错。', createTime: '2026-03-10 14:20:00' },
    { id: 's1-2', nickName: '购物达人', score: 4, content: '发货快，包装完好，推荐购买。', createTime: '2026-03-08 09:15:00' }
  ],
  2: [
    { id: 's2-1', nickName: '程序员', score: 5, content: '轻薄便携，办公够用，电池续航也可以。', createTime: '2026-03-11 18:30:00' }
  ],
  3: [
    { id: 's3-1', nickName: '时尚青年', score: 5, content: '纯棉面料很舒服，尺码合适。', createTime: '2026-03-09 11:00:00' },
    { id: 's3-2', nickName: 'user1', score: 4, content: '颜色好看，洗了一次没有缩水。', createTime: '2026-03-07 20:45:00' }
  ]
}

function storageKey(spuId) {
  return `learn_comments_${spuId}`
}

function readUserComments(spuId) {
  try {
    const raw = localStorage.getItem(storageKey(spuId))
    return raw ? JSON.parse(raw) : []
  } catch {
    return []
  }
}

function writeUserComments(spuId, list) {
  localStorage.setItem(storageKey(spuId), JSON.stringify(list))
}

export function getComments(spuId) {
  const seed = SEED_COMMENTS[Number(spuId)] || []
  const user = readUserComments(spuId)
  return [...user, ...seed]
}

export function getCommentCount(spuId) {
  return getComments(spuId).length
}

export function addComment(spuId, content, score = 5) {
  if (!getToken()) {
    return { success: false, message: '请先登录' }
  }
  const text = (content || '').trim()
  if (!text) {
    return { success: false, message: '请输入评论内容' }
  }
  const list = readUserComments(spuId)
  list.unshift({
    id: `u-${Date.now()}`,
    nickName: '我',
    score: Math.min(5, Math.max(1, score)),
    content: text,
    createTime: formatNow(),
    mine: true
  })
  writeUserComments(spuId, list)
  return { success: true, message: '评论成功' }
}

function formatNow() {
  const now = new Date()
  const y = now.getFullYear()
  const m = String(now.getMonth() + 1).padStart(2, '0')
  const d = String(now.getDate()).padStart(2, '0')
  const h = String(now.getHours()).padStart(2, '0')
  const min = String(now.getMinutes()).padStart(2, '0')
  const s = String(now.getSeconds()).padStart(2, '0')
  return `${y}-${m}-${d} ${h}:${min}:${s}`
}

export function scoreText(score) {
  return '★'.repeat(score) + '☆'.repeat(5 - score)
}
