<template>
  <el-card>
    <template #header>
      <div class="header">
        <span>{{ isEdit ? '编辑商品' : '新增商品' }}</span>
        <el-button @click="router.back()">返回</el-button>
      </div>
    </template>

    <el-form :model="form" label-width="100px" class="form">
      <el-form-item label="商品名称" required>
        <el-input v-model="form.name" />
      </el-form-item>
      <el-form-item label="分类" required>
        <el-select v-model="form.categoryId" style="width: 100%">
          <el-option
            v-for="item in categories"
            :key="item.categoryId"
            :value="item.categoryId"
            :label="item.name"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="卖点">
        <el-input v-model="form.sellingPoint" />
      </el-form-item>
      <el-form-item label="主图URL">
        <el-input v-model="form.mainImgUrl" placeholder="https://..." />
      </el-form-item>
      <el-form-item label="售价(元)" required>
        <el-input-number v-model="priceYuan" :min="0" :precision="2" :step="1" />
      </el-form-item>
      <el-form-item label="市场价(元)">
        <el-input-number v-model="marketPriceYuan" :min="0" :precision="2" :step="1" />
      </el-form-item>
      <el-form-item label="库存" required>
        <el-input-number v-model="form.stock" :min="0" />
      </el-form-item>
      <el-form-item label="排序">
        <el-input-number v-model="form.seq" :min="0" />
      </el-form-item>
      <el-form-item label="状态">
        <el-radio-group v-model="form.status">
          <el-radio :value="1">上架</el-radio>
          <el-radio :value="0">下架</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item label="详情">
        <el-input v-model="form.detail" type="textarea" :rows="6" placeholder="支持 HTML" />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" :loading="saving" @click="handleSave">保存</el-button>
      </el-form-item>
    </el-form>
  </el-card>
</template>

<script setup>
import { computed, onMounted, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { listCategories } from '@/api/category'
import { getSpu, saveSpu, updateSpu } from '@/api/spu'
import { yuanToFee } from '@/utils/price'

const route = useRoute()
const router = useRouter()
const saving = ref(false)
const categories = ref([])
const form = ref({
  spuId: null,
  categoryId: null,
  name: '',
  sellingPoint: '',
  mainImgUrl: '',
  priceFee: 0,
  marketPriceFee: 0,
  stock: 0,
  seq: 0,
  status: 0,
  detail: ''
})

const isEdit = computed(() => !!form.value.spuId)
const priceYuan = computed({
  get: () => (form.value.priceFee || 0) / 100,
  set: val => { form.value.priceFee = yuanToFee(val) }
})
const marketPriceYuan = computed({
  get: () => (form.value.marketPriceFee || 0) / 100,
  set: val => { form.value.marketPriceFee = yuanToFee(val) }
})

async function loadCategories() {
  categories.value = await listCategories()
}

async function loadSpu() {
  const spuId = route.query.spuId
  if (!spuId) return
  const data = await getSpu(spuId)
  form.value = { ...data }
}

async function handleSave() {
  saving.value = true
  try {
    if (isEdit.value) {
      await updateSpu(form.value)
    } else {
      await saveSpu(form.value)
    }
    ElMessage.success('保存成功')
    router.push('/product/spu/index')
  } finally {
    saving.value = false
  }
}

onMounted(async () => {
  await loadCategories()
  await loadSpu()
})
</script>

<style scoped>
.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
.form {
  max-width: 720px;
}
</style>
