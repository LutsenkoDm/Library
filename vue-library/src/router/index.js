import Vue from 'vue'
import VueRouter from 'vue-router'

Vue.use(VueRouter)

const routes = [
  {
    path: '/',
    component: () => import('../views/Journal')
  },
  {
    path: '/bookTypes',
    component: () => import('../views/BookTypes')
  },
  {
    path: '/books',
    component: () => import('../views/Books')
  },
  {
    path: '/clients',
    component: () => import('../views/Clients')
  },
  {
    path: '/journal',
    component: () => import('../views/Journal')
  },
]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})


export default router
