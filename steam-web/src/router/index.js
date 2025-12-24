import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/HomeView.vue'
import LoginView from '../views/LoginView.vue'
import GameDetailView from '../views/GameDetailView.vue'
import AdminGameView from '../views/AdminGameView.vue'
import SearchResultsView from '../views/SearchResultsView.vue'
// 1. 引入缺少的组件
import DeveloperView from '../views/DeveloperView.vue'
import CategoryView from '../views/CategoryView.vue'

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
        {
            path: '/search',
            name: 'search',
            component: SearchResultsView
        },
        // 2. 添加开发商页面路由 (对应 GameDetailView 中的 /developer/:name)
        {
            path: '/developer/:name',
            name: 'developer',
            component: DeveloperView
        },
        // 3. 添加分类标签页面路由 (对应 GameDetailView 中的 /category/:name)
        {
            path: '/category/:name',
            name: 'category',
            component: CategoryView
        }
    ]
})

export default router