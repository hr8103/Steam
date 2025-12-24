import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/HomeView.vue'
import LoginView from '../views/LoginView.vue'
import GameDetailView from '../views/GameDetailView.vue'
import AdminGameView from '../views/AdminGameView.vue'
import SearchResultsView from '../views/SearchResultsView.vue' // 引入新页面

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
        },
        {
            path: '/game/:id',
            name: 'game-detail',
            component: GameDetailView
        },
        // ★ 新增搜索页路由
        {
            path: '/search',
            name: 'search',
            component: SearchResultsView
        }
    ]
})

export default router