export function formatPrice(fee) {
  if (fee == null) return '0.00'
  return (fee / 100).toFixed(2)
}

export function yuanToFee(yuan) {
  if (yuan == null || yuan === '') return 0
  return Math.round(Number(yuan) * 100)
}
