<template>
  <el-card>
    <template #header>
      <div class="header">
        <span>分类管理</span>
        <el-button type="primary" @click="openDialog()">新增分类</el-button>
      </div>
    </template>
    <el-table :data="tableData" row-key="categoryId" border default-expand-all>
      <el-table-column prop="name" label="分类名称" />
      <el-table-column prop="level" label="层级" width="80" />
      <el-table-column prop="seq" label="排序" width="80" />
      <el-table-column label="状态" width="100">
        <template #default="{ row }">
          <el-tag :type="row.status === 1 ? 'success' : 'info'">
            {{ row.status === 1 ? '启用' : '禁用' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="200">
        <template #default="{ row }">
          <el-button link type="primary" @click="openDialog(row)">编辑</el-button>
          <el-button link type="danger" @click="handleDelete(row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog v-model="dialogVisible" :title="form.categoryId ? '编辑分类' : '新增分类'" width="480px">
      <el-form :model="form" label-width="90px">
        <el-form-item label="分类名称">
          <el-input v-model="form.name" />
        </el-form-item>
        <el-form-item label="父分类">
          <el-select v-model="form.parentId" style="width: 100%">
            <el-option :value="0" label="顶级分类" />
            <el-option
              v-for="item in flatCategories"
              :key="item.categoryId"
              :value="item.categoryId"
              :label="item.name"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="排序">
          <el-input-number v-model="form.seq" :min="0" />
        </el-form-item>
        <el-form-item label="状态">
          <el-radio-group v-model="form.status">
            <el-radio :value="1">启用</el-radio>
            <el-radio :value="0">禁用</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="saving" @click="handleSave">保存</el-button>
      </template>
    </el-dialog>
  </el-card>
</template>

<script setup>
import { onMounted, ref } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { deleteCategory, listCategories, saveCategory, updateCategory } from '@/api/category'
import { treeDataTranslate } from '@/utils'

const tableData = ref([])
const flatCategories = ref([])
const dialogVisible = ref(false)
const saving = ref(false)
const form = ref({
  categoryId: null,
  name: '',
  parentId: 0,
  seq: 0,
  status: 1
})

async function loadData() {
  const list = await listCategories()
  flatCategories.value = list
  tableData.value = treeDataTranslate(list, 'categoryId', 'parentId')
}

function openDialog(row) {
  if (row) {
    form.value = { ...row }
  } else {
    form.value = { categoryId: null, name: '', parentId: 0, seq: 0, status: 1 }
  }
  dialogVisible.value = true
}

async function handleSave() {
  saving.value = true
  try {
    if (form.value.categoryId) {
      await updateCategory(form.value)
    } else {
      await saveCategory(form.value)
    }
    ElMessage.success('保存成功')
    dialogVisible.value = false
    await loadData()
  } finally {
    saving.value = false
  }
}

async function handleDelete(row) {
  await ElMessageBox.confirm(`确认删除分类「${row.name}」？`, '提示', { type: 'warning' })
  await deleteCategory(row.categoryId)
  ElMessage.success('删除成功')
  await loadData()
}

onMounted(loadData)
</script>

<style scoped>
.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style>
