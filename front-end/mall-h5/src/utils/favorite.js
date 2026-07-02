import { getToken } from './auth'

function userKey() {
  const token = getToken()
  return token ? `learn_favorites_${token.slice(-20)}` : 'learn_favorites_guest'
}

function readList() {
  try {
    const raw = localStorage.getItem(userKey())
    return raw ? JSON.parse(raw) : []
  } catch {
    return []
  }
}

function writeList(list) {
  localStorage.setItem(userKey(), JSON.stringify(list))
}

export function getFavorites() {
  return readList()
}

export function isFavorite(spuId) {
  return readList().some(item => item.spuId === Number(spuId))
}

export function toggleFavorite(spu) {
  const list = readList()
  const id = Number(spu.spuId)
  const index = list.findIndex(item => item.spuId === id)
  if (index >= 0) {
    list.splice(index, 1)
    writeList(list)
    return { collected: false }
  }
  list.unshift({
    spuId: id,
    name: spu.name,
    mainImgUrl: spu.mainImgUrl,
    priceFee: spu.priceFee,
    collectTime: new Date().toISOString()
  })
  writeList(list)
  return { collected: true }
}

export function removeFavorite(spuId) {
  const list = readList().filter(item => item.spuId !== Number(spuId))
  writeList(list)
}
