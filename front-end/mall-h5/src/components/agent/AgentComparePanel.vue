<template>
  <div class="compare-panel">
    <div class="compare-table-wrap">
      <table class="compare-table">
        <thead>
          <tr>
            <th class="spec-col">参数</th>
            <th v-for="item in items" :key="item.spuId" :class="{ recommended: item.recommended }">
              <span class="col-name">{{ item.name }}</span>
              <span v-if="item.recommended" class="best-badge">推荐</span>
            </th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="spec in specs" :key="spec.key">
            <td class="spec-col">{{ spec.label }}</td>
            <td
              v-for="item in items"
              :key="`${item.spuId}-${spec.key}`"
              :class="{ recommended: item.recommended, highlight: isHighlight(item, spec.key) }"
            >
              {{ item.values[spec.key] ?? '-' }}
            </td>
          </tr>
        </tbody>
      </table>
    </div>
    <div v-if="recommendation" class="recommendation">
      <span class="rec-icon">✦</span>
      <p>{{ recommendation }}</p>
    </div>
  </div>
</template>

<script setup>
const props = defineProps({
  specs: {
    type: Array,
    default: () => []
  },
  items: {
    type: Array,
    default: () => []
  },
  recommendation: {
    type: String,
    default: ''
  },
  highlights: {
    type: Object,
    default: () => ({})
  }
})

function isHighlight(item, specKey) {
  const keys = props.highlights[item.spuId]
  return keys && keys.includes(specKey)
}
</script>

<style scoped>
.compare-panel {
  background: #fff;
  border-radius: 12px;
  overflow: hidden;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.06);
}

.compare-table-wrap {
  overflow-x: auto;
  -webkit-overflow-scrolling: touch;
}

.compare-table {
  width: 100%;
  min-width: 320px;
  border-collapse: collapse;
  font-size: 12px;
}

.compare-table th,
.compare-table td {
  padding: 10px 8px;
  text-align: center;
  border-bottom: 1px solid #f2f3f5;
  vertical-align: middle;
}

.compare-table th {
  background: #fafbfc;
  color: #646566;
  font-weight: 500;
}

.spec-col {
  text-align: left !important;
  color: #969799;
  font-weight: 500;
  min-width: 56px;
  position: sticky;
  left: 0;
  background: #fff;
  z-index: 1;
}

.compare-table thead .spec-col {
  background: #fafbfc;
}

.col-name {
  display: block;
  font-size: 12px;
  color: #323233;
  line-height: 1.3;
  max-width: 88px;
  margin: 0 auto;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.best-badge {
  display: inline-block;
  margin-top: 4px;
  padding: 1px 6px;
  border-radius: 4px;
  background: linear-gradient(135deg, #1989fa, #4fc3f7);
  color: #fff;
  font-size: 10px;
  font-weight: 600;
}

.compare-table td.recommended,
.compare-table th.recommended {
  background: rgba(25, 137, 250, 0.04);
}

.compare-table td.highlight {
  color: #1989fa;
  font-weight: 600;
}

.recommendation {
  display: flex;
  gap: 8px;
  padding: 12px;
  background: linear-gradient(135deg, rgba(102, 126, 234, 0.08), rgba(118, 75, 162, 0.08));
  border-top: 1px solid #f2f3f5;
}

.rec-icon {
  flex-shrink: 0;
  width: 20px;
  height: 20px;
  border-radius: 50%;
  background: linear-gradient(135deg, #667eea, #764ba2);
  color: #fff;
  font-size: 10px;
  line-height: 20px;
  text-align: center;
}

.recommendation p {
  margin: 0;
  font-size: 13px;
  line-height: 1.5;
  color: #323233;
}
</style>
