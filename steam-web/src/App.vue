<script setup>
import { ref, onMounted } from 'vue'
import { RouterView, RouterLink, useRouter } from 'vue-router'
import { Search } from '@element-plus/icons-vue'

const router = useRouter()
const user = ref(null)
const searchKeyword = ref('') // 绑定搜索框输入

// 页面加载时检查登录状态
onMounted(() => {
  const userStr = localStorage.getItem('user')
  if (userStr) {
    try {
      user.value = JSON.parse(userStr)
    } catch (e) {
      console.error('用户数据解析失败', e)
      localStorage.removeItem('user')
    }
  }
})

// 注销逻辑
const handleLogout = () => {
  localStorage.removeItem('user')
  user.value = null
  router.push('/login')
  setTimeout(() => location.reload(), 100)
}

// ★ 核心：执行搜索跳转
const doSearch = () => {
  // 无论有没有关键词，都跳转搜索页（空关键词显示"所有产品"）
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
          <img
              src="https://store.cloudflare.steamstatic.com/public/shared/images/header/logo_steam.svg?t=962016"
              class="steam-logo-img"
              alt="Steam Logo"
          />
        </div>

        <nav class="nav-links">
          <RouterLink to="/" class="nav-item">商店</RouterLink>
          <a href="#" class="nav-item">社区</a>
          <a href="#" class="nav-item">关于</a>
          <a href="#" class="nav-item">客服</a>

          <RouterLink v-if="user && user.role === 1" to="/admin" class="nav-item admin-link">
            后台管理
          </RouterLink>
        </nav>

        <div class="header-actions">
          <div class="search-box">
            <input
                type="text"
                v-model="searchKeyword"
                placeholder="搜索"
                @keyup.enter="doSearch"
            />
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
          <p>© 2025 Valve Corporation. 保留所有权利。所有商标均为其在美国及其它国家/地区的各自持有人的财产。</p>
          <div class="footer-links">
            <a href="#">隐私政策</a> | <a href="#">法律信息</a> | <a href="#">Steam 订户协议</a> | <a href="#">退款</a>
          </div>
        </div>
      </div>
    </footer>
  </div>
</template>

<style>
/* 引入官方字体感 */
@import url('https://fonts.googleapis.com/css2?family=Motiva+Sans:wght@300;400;700&display=swap');

:root {
  --steam-bg-dark: #171a21;
  --steam-bg-main: #1b2838;
  --steam-text-color: #c6d4df;
  --steam-accent: #66c0f4; /* Steam 蓝 */
}

body {
  margin: 0;
  padding: 0;
  background-color: var(--steam-bg-main);
  color: var(--steam-text-color);
  font-family: "Motiva Sans", Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
}

.steam-container {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
}

/* 顶部 Global 小条 */
.global-header {
  background: #171a21;
  height: 34px;
  display: flex;
  align-items: center;
}
.global-nav {
  display: flex;
  justify-content: flex-end;
  width: 940px; /* 如果你已经改为1200px宽版，这里可以改成 1200px */
  margin: 0 auto;
}
.global-btn span {
  color: #b8b6b4 !important;
  font-size: 11px;
}
.global-btn:hover span {
  color: #fff !important;
}
.sep {
  color: #b8b6b4;
  margin: 0 5px;
  font-size: 11px;
}
.user-welcome {
  color: #b8b6b4;
  font-size: 12px;
  margin-right: 15px;
}

/* 主 Header */
.steam-header {
  background: #171a21;
  height: 104px;
  box-shadow: 0 0 10px rgba(0,0,0,0.4);
  position: relative;
  z-index: 100;
}

.content-width {
  width: 940px; /* 根據你的需求，如果是寬版頁面這里可以用 1200px，暫時保持兼容 */
  margin: 0 auto;
}
/* 寬版詳情頁會自己用 .container-1200，這裡保持首頁的默認寬度 */

.header-inner {
  height: 100%;
  display: flex;
  align-items: center;
}

.logo-wrapper {
  margin-right: 40px;
  cursor: pointer;
}

.steam-logo-img {
  width: 176px;
  height: 44px;
  transition: filter 0.2s;
}
.steam-logo-img:hover {
  filter: brightness(1.2);
}

/* 导航链接 */
.nav-links {
  display: flex;
  height: 100%;
  align-items: center;
}

.nav-item {
  text-decoration: none;
  color: #b8b6b4;
  font-size: 14px;
  font-weight: bold;
  text-transform: uppercase;
  padding: 10px 15px;
  transition: color 0.3s;
  letter-spacing: 0.05em;
  display: block;
}

.nav-item:hover {
  color: var(--steam-accent);
}

.router-link-active {
  color: #1a9fff;
  border-bottom: 3px solid #1a9fff;
  padding-bottom: 7px;
}

/* 管理员链接特别样式 */
.admin-link {
  color: #ff4c4c !important;
  text-shadow: 0 0 5px rgba(255, 76, 76, 0.3);
}

/* 搜索框 */
.header-actions {
  margin-left: auto;
}
.search-box {
  background-color: #316282;
  border-radius: 3px;
  padding: 3px;
  display: flex;
  align-items: center;
  border: 1px solid rgba(0,0,0,0);
  transition: border 0.3s, background-color 0.3s;
}
.search-box:hover {
  border: 1px solid #66c0f4;
  background-color: #3b6e8c;
}
.search-box input {
  background: transparent;
  border: none;
  color: #fff;
  padding: 5px 10px;
  font-style: italic;
  font-size: 14px;
  outline: none;
  width: 150px;
  text-shadow: 1px 1px 0 rgba(0,0,0,0.3);
}
.search-box input::placeholder {
  color: #0e1c25;
  font-style: italic;
}
.search-btn {
  background: #66c0f4;
  border: none;
  width: 30px;
  height: 25px;
  border-radius: 2px;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #000;
}
.search-btn:hover {
  background: #fff;
}

.main-content {
  flex: 1;
}

/* 页脚 */
.steam-footer {
  background-color: #171a21;
  padding: 40px 0;
  text-align: center;
  font-size: 12px;
  color: #8f98a0;
  margin-top: 0; /* 让页面组件自己控制底部间距 */
  border-top: 1px solid #363c44;
}
.footer-text {
  padding-top: 20px;
}
.footer-links {
  margin-top: 10px;
}
.footer-links a {
  color: #c6d4df;
  text-decoration: none;
  margin: 0 5px;
}
.footer-links a:hover {
  color: #fff;
}
</style>