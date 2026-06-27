import { createRouter, createWebHistory } from 'vue-router'
import Home from '@/views/Home.vue'
import Detail from '@/views/Detail.vue'
import Cart from '@/views/Cart.vue'
import Login from '@/views/Login.vue'
import My from '@/views/My.vue'
import Register from '@/views/Register.vue'
import ProfileEdit from '@/views/ProfileEdit.vue'
import AddressList from '@/views/AddressList.vue'
import AddressEdit from '@/views/AddressEdit.vue'

const router = createRouter({
  history: createWebHistory(),
  routes: [
    { path: '/', component: Home },
    { path: '/detail', component: Detail },
    { path: '/cart', component: Cart },
    { path: '/login', component: Login },
    { path: '/my', component: My },
    { path: '/register', component: Register },
    { path: '/profile/edit', component: ProfileEdit },
    { path: '/address', component: AddressList },
    { path: '/address/edit', component: AddressEdit }
  ]
})

export default router
