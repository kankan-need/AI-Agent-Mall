<template>
  <Teleport to="body">
    <div v-if="visible" class="sheet-mask" @click.self="close">
      <div class="sheet-panel">
        <div class="title">分享到</div>
        <div class="options">
          <button class="option" @click="copyLink">
            <span class="icon link">链</span>
            <span>复制链接</span>
          </button>
          <button class="option" @click="mockShare('微信好友')">
            <span class="icon wechat">微</span>
            <span>微信好友</span>
          </button>
          <button class="option" @click="mockShare('朋友圈')">
            <span class="icon moments">圈</span>
            <span>朋友圈</span>
          </button>
        </div>
        <button class="cancel" @click="close">取消</button>
      </div>
    </div>
  </Teleport>
</template>

<script setup>
const props = defineProps({
  visible: Boolean,
  shareUrl: {
    type: String,
    default: ''
  },
  shareTitle: {
    type: String,
    default: ''
  }
})

const emit = defineEmits(['update:visible'])

function close() {
  emit('update:visible', false)
}

async function copyLink() {
  const text = props.shareUrl || window.location.href
  try {
    await navigator.clipboard.writeText(text)
    alert('链接已复制')
  } catch {
    alert(`请手动复制：${text}`)
  }
  close()
}

function mockShare(channel) {
  alert(`学习版演示：已模拟分享到${channel}`)
  close()
}
</script>

<style scoped>
.sheet-mask {
  position: fixed;
  inset: 0;
  background: rgba(0, 0, 0, 0.45);
  z-index: 1000;
  display: flex;
  align-items: flex-end;
}
.sheet-panel {
  width: 100%;
  background: #fff;
  border-radius: 16px 16px 0 0;
  padding: 20px 16px calc(16px + env(safe-area-inset-bottom));
}
.title {
  text-align: center;
  font-size: 15px;
  font-weight: 600;
  margin-bottom: 20px;
}
.options {
  display: flex;
  justify-content: space-around;
  margin-bottom: 20px;
}
.option {
  border: none;
  background: transparent;
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 8px;
  font-size: 12px;
  color: #646566;
}
.icon {
  width: 48px;
  height: 48px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #fff;
  font-size: 16px;
  font-weight: 700;
}
.icon.link { background: #1989fa; }
.icon.wechat { background: #07c160; }
.icon.moments { background: #ff976a; }
.cancel {
  width: 100%;
  border: none;
  background: #f7f8fa;
  border-radius: 20px;
  padding: 12px;
  font-size: 15px;
}
</style>
