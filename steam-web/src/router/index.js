import { createRouter, createWebHistory } from 'vue-router'

// 引入所有页面组件
import HomeView from '../views/HomeView.vue'
import LoginView from '../views/LoginView.vue'
import AdminGameView from '../views/AdminGameView.vue'
import GameDetailView from '../views/GameDetailView.vue'
import DeveloperView from '../views/DeveloperView.vue'
import CategoryView from '../views/CategoryView.vue'
import SearchResultsView from '../views/SearchResultsView.vue' // 新增搜索页

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
            component: AdminGameView
        },
        {
            path: '/game/:id',
            name: 'game-detail',
            component: GameDetailView
        },
        {
            path: '/developer/:name',
            name: 'developer',
            component: DeveloperView
        },
        {
            path: '/category/:name',
            name: 'category',
            component: CategoryView
        },
        // ★ 新增：搜索结果页路由
        {
            path: '/search',
            name: 'search',
            component: SearchResultsView
        }
    ]
})

export default router