import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/HomeView.vue'
import LoginView from '../views/LoginView.vue'
import GameDetailView from '../views/GameDetailView.vue'
import AdminGameView from '../views/AdminGameView.vue' // 原来的 PublishView 改名

const router = createRouter({
    history: createWebHistory(import.meta.env.BASE_URL),
    routes: [
        {
            path: '/',
            name: 'home',
            component: HomeView
        },
        {
            path: '/login',
            name: 'login',
            component: LoginView
        },
        {
            path: '/admin',
            name: 'admin',
            component: AdminGameView,
            // 以后这里可以加路由守卫，防止普通人直接输入网址进入
        },
        {
            path: '/game/:id', // :id 是动态参数
            name: 'game-detail',
            component: GameDetailView
        }
    ]
})

export default router