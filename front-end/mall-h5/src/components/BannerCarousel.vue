<template>
  <div class="banner-carousel card" @mouseenter="pause" @mouseleave="resume">
    <div class="track" :style="{ transform: `translateX(-${current * 100}%)` }">
      <div
        v-for="(item, index) in banners"
        :key="index"
        class="slide"
        @click="handleClick(item)"
      >
        <img v-if="item.imgUrl" :src="item.imgUrl" :alt="item.title" />
        <div v-else class="slide-bg" :style="{ background: item.bg }">
          <div class="slide-title">{{ item.title }}</div>
          <div class="slide-desc">{{ item.desc }}</div>
        </div>
      </div>
    </div>
    <div class="dots">
      <span
        v-for="(_, index) in banners"
        :key="index"
        :class="{ active: current === index }"
        @click.stop="goTo(index)"
      />
    </div>
  </div>
</template>

<script setup>
import { onMounted, onUnmounted, ref } from 'vue'
import { useRouter } from 'vue-router'
import { getToken } from '@/utils/auth'

const props = defineProps({
  banners: {
    type: Array,
    default: () => []
  },
  interval: {
    type: Number,
    default: 3500
  }
})

const router = useRouter()
const current = ref(0)
let timer = null

function next() {
  if (!props.banners.length) return
  current.value = (current.value + 1) % props.banners.length
}

function goTo(index) {
  current.value = index
}

function start() {
  stop()
  if (props.banners.length <= 1) return
  timer = setInterval(next, props.interval)
}

function stop() {
  if (timer) {
    clearInterval(timer)
    timer = null
  }
}

function pause() {
  stop()
}

function resume() {
  start()
}

function handleClick(item) {
  if (!item.link) return
  if (item.needLogin && !getToken()) {
    router.push('/login')
    return
  }
  router.push(item.link)
}

onMounted(start)
onUnmounted(stop)
</script>

<style scoped>
.banner-carousel {
  position: relative;
  margin: 0 12px 12px;
  overflow: hidden;
  border-radius: 8px;
}
.track {
  display: flex;
  transition: transform 0.4s ease;
}
.slide {
  flex: 0 0 100%;
  height: 140px;
  cursor: pointer;
}
.slide img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  display: block;
}
.slide-bg {
  width: 100%;
  height: 100%;
  display: flex;
  flex-direction: column;
  justify-content: center;
  padding: 0 24px;
  color: #fff;
}
.slide-title {
  font-size: 20px;
  font-weight: 700;
}
.slide-desc {
  margin-top: 8px;
  font-size: 13px;
  opacity: 0.9;
}
.dots {
  position: absolute;
  left: 0;
  right: 0;
  bottom: 10px;
  display: flex;
  justify-content: center;
  gap: 6px;
}
.dots span {
  width: 6px;
  height: 6px;
  border-radius: 50%;
  background: rgba(255, 255, 255, 0.5);
  cursor: pointer;
  transition: all 0.2s;
}
.dots span.active {
  width: 16px;
  border-radius: 3px;
  background: #fff;
}
</style>
