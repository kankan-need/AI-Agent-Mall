import { createRouter, createWebHistory } from 'vue-router'
import Home from '@/views/Home.vue'
import Category from '@/views/Category.vue'
import Detail from '@/views/Detail.vue'
import Cart from '@/views/Cart.vue'
import Login from '@/views/Login.vue'
import My from '@/views/My.vue'
import Register from '@/views/Register.vue'
import ProfileEdit from '@/views/ProfileEdit.vue'
import AddressList from '@/views/AddressList.vue'
import AddressEdit from '@/views/AddressEdit.vue'
import OrderConfirm from '@/views/OrderConfirm.vue'
import OrderPay from '@/views/OrderPay.vue'
import OrderList from '@/views/OrderList.vue'
import OrderDetail from '@/views/OrderDetail.vue'

const router = createRouter({
  history: createWebHistory(),
  routes: [
    { path: '/', component: Home },
    { path: '/category', component: Category },
    { path: '/detail', component: Detail },
    { path: '/cart', component: Cart },
    { path: '/login', component: Login },
    { path: '/my', component: My },
    { path: '/register', component: Register },
    { path: '/profile/edit', component: ProfileEdit },
    { path: '/address', component: AddressList },
    { path: '/address/edit', component: AddressEdit },
    { path: '/order/confirm', component: OrderConfirm },
    { path: '/order/pay', component: OrderPay },
    { path: '/order/list', component: OrderList },
    { path: '/order/detail', component: OrderDetail }
  ]
})

export default router
