import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import path from 'path'

export default defineConfig({
  plugins: [vue()],
  resolve: {
    alias: {
      '@': path.resolve(__dirname, 'src')
    }
  },
  server: {
    port: 9528,
    host: true,
    proxy: {
      '/learn-auth': {
        target: 'http://127.0.0.1:8000',
        changeOrigin: true
      },
      '/learn-product': {
        target: 'http://127.0.0.1:8000',
        changeOrigin: true
      },
      '/learn-rbac': {
        target: 'http://127.0.0.1:8000',
        changeOrigin: true
      },
      '/learn-user': {
        target: 'http://127.0.0.1:8000',
        changeOrigin: true
      },
      '/learn-order': {
        target: 'http://127.0.0.1:8000',
        changeOrigin: true
      }
    }
  }
})
