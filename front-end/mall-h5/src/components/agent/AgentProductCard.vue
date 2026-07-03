<template>
  <div class="product-card" @click="goDetail">
    <img :src="product.mainImgUrl" :alt="product.name" />
    <div class="body">
      <div class="name">{{ product.name }}</div>
      <div v-if="product.sellingPoint" class="point">{{ product.sellingPoint }}</div>
      <div class="footer">
        <span class="price">¥{{ formatPrice(product.priceFee) }}</span>
        <span v-if="tag" class="tag" :class="tagType">{{ tag }}</span>
      </div>
    </div>
  </div>
</template>

<script setup>
import { useRouter } from 'vue-router'
import { formatPrice } from '@/utils/price'

const props = defineProps({
  product: {
    type: Object,
    required: true
  },
  tag: {
    type: String,
    default: ''
  },
  tagType: {
    type: String,
    default: 'default'
  }
})

const router = useRouter()

function goDetail() {
  router.push({ path: '/detail', query: { spuId: props.product.spuId } })
}
</script>

<style scoped>
.product-card {
  flex: 0 0 148px;
  background: #fff;
  border-radius: 12px;
  overflow: hidden;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.06);
  cursor: pointer;
  transition: transform 0.2s ease, box-shadow 0.2s ease;
}

.product-card:active {
  transform: scale(0.98);
  box-shadow: 0 1px 6px rgba(0, 0, 0, 0.05);
}

.product-card img {
  width: 100%;
  height: 100px;
  object-fit: cover;
  display: block;
}

.body {
  padding: 8px;
}

.name {
  font-size: 13px;
  line-height: 1.35;
  height: 36px;
  overflow: hidden;
  color: #323233;
  font-weight: 500;
}

.point {
  margin-top: 4px;
  font-size: 11px;
  color: #969799;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.footer {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 6px;
  margin-top: 6px;
}

.price {
  color: #ee0a24;
  font-size: 14px;
  font-weight: 600;
}

.tag {
  flex-shrink: 0;
  padding: 2px 6px;
  border-radius: 4px;
  font-size: 10px;
  font-weight: 600;
}

.tag.default {
  background: #f2f3f5;
  color: #646566;
}

.tag.recommend {
  background: linear-gradient(135deg, rgba(25, 137, 250, 0.12), rgba(79, 195, 247, 0.18));
  color: #1989fa;
}

.tag.value {
  background: rgba(238, 10, 36, 0.08);
  color: #ee0a24;
}
</style>
