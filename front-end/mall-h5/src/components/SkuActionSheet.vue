<template>
  <Teleport to="body">
    <div v-if="visible" class="sheet-mask" @click.self="close">
      <div class="sheet-panel">
        <div class="sheet-header">
          <img :src="spu?.mainImgUrl" alt="" />
          <div class="meta">
            <div class="price">¥{{ formatPrice(spu?.priceFee) }}</div>
            <div class="stock">库存 {{ spu?.stock ?? 0 }} 件</div>
            <div class="selected">已选：默认规格 x{{ count }}</div>
          </div>
          <button class="close" @click="close">×</button>
        </div>

        <div class="count-row">
          <span>购买数量</span>
          <div class="stepper">
            <button :disabled="count <= 1" @click="count -= 1">-</button>
            <span>{{ count }}</span>
            <button :disabled="count >= maxCount" @click="count += 1">+</button>
          </div>
        </div>

        <button class="btn-primary confirm" :disabled="submitting" @click="confirm">
          {{ submitting ? '处理中...' : confirmText }}
        </button>
      </div>
    </div>
  </Teleport>
</template>

<script setup>
import { computed, ref, watch } from 'vue'
import { formatPrice } from '@/utils/price'

const props = defineProps({
  visible: Boolean,
  spu: Object,
  mode: {
    type: String,
    default: 'cart'
  }
})

const emit = defineEmits(['update:visible', 'confirm'])

const count = ref(1)
const submitting = ref(false)

const maxCount = computed(() => Math.max(1, props.spu?.stock || 1))

const confirmText = computed(() => (props.mode === 'buy' ? '立即购买' : '加入购物车'))

watch(() => props.visible, (val) => {
  if (val) {
    count.value = 1
    submitting.value = false
  }
})

function close() {
  emit('update:visible', false)
}

function confirm() {
  if (submitting.value || !props.spu) return
  submitting.value = true
  emit('confirm', { count: count.value, mode: props.mode })
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
  padding: 16px 16px calc(16px + env(safe-area-inset-bottom));
}
.sheet-header {
  display: flex;
  gap: 12px;
  align-items: flex-start;
  padding-bottom: 16px;
  border-bottom: 1px solid #f5f5f5;
}
.sheet-header img {
  width: 88px;
  height: 88px;
  object-fit: cover;
  border-radius: 8px;
}
.meta {
  flex: 1;
}
.price {
  color: #ee0a24;
  font-size: 20px;
  font-weight: 700;
}
.stock, .selected {
  margin-top: 6px;
  font-size: 12px;
  color: #969799;
}
.close {
  border: none;
  background: transparent;
  font-size: 24px;
  color: #969799;
  line-height: 1;
}
.count-row {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 16px 0;
  font-size: 14px;
}
.stepper {
  display: flex;
  align-items: center;
  gap: 12px;
}
.stepper button {
  width: 28px;
  height: 28px;
  border: 1px solid #ebedf0;
  background: #fff;
  border-radius: 4px;
}
.stepper button:disabled {
  opacity: 0.4;
}
.confirm {
  width: 100%;
  margin-top: 8px;
}
</style>
