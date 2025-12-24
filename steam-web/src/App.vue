<script setup>
import { ref, onMounted, computed } from 'vue'
import { RouterView, RouterLink, useRouter } from 'vue-router'
import { Search } from '@element-plus/icons-vue' // 移除 ShoppingCart 图标，使用文字或自定义样式
import { cartState } from '@/utils/cart'

const router = useRouter()
const user = ref(null)
const searchKeyword = ref('')

// 实时计算购物车数量
const cartCount = computed(() => cartState.value.length)

onMounted(() => {
  const userStr = localStorage.getItem('user')
  if (userStr) {
    try {
      user.value = JSON.parse(userStr)
    } catch (e) {
      localStorage.removeItem('user')
    }
  }
})

const handleLogout = () => {
  localStorage.removeItem('user')
  user.value = null
  router.push('/login')
  setTimeout(() => location.reload(), 100)
}

const doSearch = () => {
  const query = searchKeyword.value.trim() ? { q: searchKeyword.value.trim() } : {}
  router.push({ path: '/search', query: query })
}
</script>

<template>
  <div class="steam-container">
    <div class="global-header">
      <div class="content-width global-nav">
        <div class="global-actions">
          <el-button type="info" link size="small" class="global-btn">安装 Steam</el-button>
          <template v-if="!user">
            <el-button type="info" link size="small" class="global-btn" @click="router.push('/login')">登录</el-button>
            <span class="sep">|</span>
            <el-button type="info" link size="small" class="global-btn">语言</el-button>
          </template>
          <template v-else>
            <span class="user-welcome">欢迎回来, {{ user.nickname || user.username }}</span>
            <el-button type="info" link size="small" class="global-btn" @click="handleLogout">注销</el-button>
          </template>
        </div>
      </div>
    </div>

    <header class="steam-header">
      <div class="content-width header-inner">
        <div class="logo-wrapper" @click="router.push('/')">
          <img src="https://store.cloudflare.steamstatic.com/public/shared/images/header/logo_steam.svg?t=962016" class="steam-logo-img" alt="Steam Logo" />
        </div>

        <nav class="nav-links">
          <RouterLink to="/" class="nav-item">商店</RouterLink>
          <a href="#" class="nav-item">社区</a>
          <a href="#" class="nav-item">关于</a>
          <a href="#" class="nav-item">客服</a>
          <RouterLink v-if="user && user.role === 1" to="/admin" class="nav-item admin-link">后台管理</RouterLink>
        </nav>

        <div class="header-actions">
          <div class="cart-btn-wrapper" @click="router.push('/cart')">
            <span class="cart-label">购物车</span>
            <div class="cart-count-box">
              <span class="count-num">{{ cartCount }}</span>
            </div>
          </div>

          <div class="search-box">
            <input type="text" placeholder="搜索" v-model="searchKeyword" @keyup.enter="doSearch" />
            <button class="search-btn" @click="doSearch">
              <el-icon><Search /></el-icon>
            </button>
          </div>
        </div>
      </div>
    </header>

    <main class="main-content">
      <RouterView />
    </main>

    <footer class="steam-footer">
      <div class="content-width">
        <div class="footer-text">
          <p>© 2025 Valve Corporation. 保留所有权利。</p>
          <div class="footer-links">
            <a href="#">隐私政策</a> | <a href="#">法律信息</a> | <a href="#">Steam 订户协议</a> | <a href="#">退款</a>
          </div>
        </div>
      </div>
    </footer>
  </div>
</template>

<style>
@import url('https://fonts.googleapis.com/css2?family=Motiva+Sans:wght@300;400;700&display=swap');

:root { --steam-bg-dark: #171a21; --steam-bg-main: #1b2838; --steam-text-color: #c6d4df; --steam-accent: #66c0f4; }
body { margin: 0; padding: 0; background-color: var(--steam-bg-main); color: var(--steam-text-color); font-family: "Motiva Sans", Arial, sans-serif; }
.steam-container { min-height: 100vh; display: flex; flex-direction: column; }
.global-header { background: #171a21; height: 34px; display: flex; align-items: center; }
.global-nav { display: flex; justify-content: flex-end; width: 940px; margin: 0 auto; }
.global-btn span { color: #b8b6b4 !important; font-size: 11px; }
.global-btn:hover span { color: #fff !important; }
.sep { color: #b8b6b4; margin: 0 5px; font-size: 11px; }
.user-welcome { color: #b8b6b4; font-size: 12px; margin-right: 15px; }
.steam-header { background: #171a21; height: 104px; box-shadow: 0 0 10px rgba(0,0,0,0.4); position: relative; z-index: 100; }
.content-width { width: 940px; margin: 0 auto; }
.header-inner { height: 100%; display: flex; align-items: center; }
.logo-wrapper { margin-right: 40px; cursor: pointer; }
.steam-logo-img { width: 176px; height: 44px; transition: filter 0.2s; }
.steam-logo-img:hover { filter: brightness(1.2); }
.nav-links { display: flex; height: 100%; align-items: center; }
.nav-item { text-decoration: none; color: #b8b6b4; font-size: 14px; font-weight: bold; text-transform: uppercase; padding: 10px 15px; transition: color 0.3s; letter-spacing: 0.05em; display: block; }
.nav-item:hover { color: var(--steam-accent); }
.router-link-active { color: #1a9fff; border-bottom: 3px solid #1a9fff; padding-bottom: 7px; }
.admin-link { color: #ff4c4c !important; text-shadow: 0 0 5px rgba(255, 76, 76, 0.3); }

.header-actions { margin-left: auto; display: flex; align-items: center; gap: 10px; }

/* ★ 购物车按钮样式 (新版蓝色风格) ★ */
.cart-btn-wrapper {
  /* 使用 Steam 新版常用的深蓝渐变 */
  background: linear-gradient(to bottom, #212c42 5%, #2a475e 95%);
  padding: 6px 12px;
  border-radius: 2px;
  cursor: pointer;
  display: flex;
  align-items: center;
  height: 28px;
  transition: all 0.2s;
  margin-right: 5px;
  box-shadow: 0 1px 2px rgba(0,0,0,0.3);
}
.cart-btn-wrapper:hover {
  background: linear-gradient(to bottom, #2a475e 5%, #212c42 95%);
  filter: brightness(1.2);
}
.cart-label {
  color: #66c0f4; /* 蓝色文字 */
  font-size: 11px;
  margin-right: 8px;
  font-weight: bold;
  text-transform: uppercase;
}
.cart-count-box {
  background: #000;
  padding: 1px 6px;
  border-radius: 2px;
  min-width: 15px;
  text-align: center;
}
.count-num {
  color: #fff;
  font-size: 12px;
  font-weight: bold;
  font-family: Arial, sans-serif;
}

/* 搜索框 (保持原有风格) */
.search-box { background-color: #316282; border-radius: 3px; padding: 3px; display: flex; align-items: center; border: 1px solid rgba(0,0,0,0); transition: border 0.3s, background-color 0.3s; }
.search-box:hover { border: 1px solid #66c0f4; background-color: #417a9b; }
.search-box input { background: transparent; border: none; color: #fff; padding: 5px 10px; font-style: italic; font-size: 14px; outline: none; width: 150px; text-shadow: 1px 1px 0 rgba(0,0,0,0.3); }
.search-box input::placeholder { color: #0e1c25; }
.search-btn { background: #66c0f4; border: none; width: 30px; height: 25px; border-radius: 2px; cursor: pointer; display: flex; align-items: center; justify-content: center; color: #000; }
.search-btn:hover { background: #fff; }

.main-content { flex: 1; }
.steam-footer { background-color: #171a21; padding: 40px 0; text-align: center; font-size: 12px; color: #8f98a0; margin-top: 0; border-top: 1px solid #363c44; }
.footer-text { padding-top: 20px; }
.footer-links { margin-top: 10px; }
.footer-links a { color: #c6d4df; text-decoration: none; margin: 0 5px; }
.footer-links a:hover { color: #fff; }
</style>