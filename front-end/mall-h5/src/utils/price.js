export function formatPrice(fee) {
  if (fee == null) return '0.00'
  return (fee / 100).toFixed(2)
}
