import { createRouter, createWebHistory } from 'vue-router'
import Home from '@/views/Home.vue'
import Detail from '@/views/Detail.vue'
import Cart from '@/views/Cart.vue'
import Login from '@/views/Login.vue'

const router = createRouter({
  history: createWebHistory(),
  routes: [
    { path: '/', component: Home },
    { path: '/detail', component: Detail },
    { path: '/cart', component: Cart },
    { path: '/login', component: Login }
  ]
})

export default router
