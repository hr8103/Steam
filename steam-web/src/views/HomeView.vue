<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import request from '@/utils/request'

const router = useRouter()
const gameList = ref([])
const loading = ref(true)

// 获取所有游戏
const fetchGames = async () => {
  try {
    const res = await request.get('/api/game/list')
    // 兼容处理：无论后端返回 {data:[]} 还是 [] 都能拿到
    gameList.value = Array.isArray(res) ? res : (res.data || [])
  } catch (e) {
    console.error(e)
  } finally {
    loading.value = false
  }
}

const goToDetail = (id) => {
  router.push(`/game/${id}`)
}

onMounted(fetchGames)
</script>

<template>
  <div class="home-container">

    <div class="hero-banner-gif">
      <img src="/winter.GIF" alt="Winter Sale" class="gif-bg" />

      <div class="gif-overlay-gradient"></div>
    </div>

    <div class="content-width">

      <div class="store-capsule-bar">
        <div class="capsule active">精选和推荐</div>
        <div class="capsule">特别优惠</div>
        <div class="capsule">按类别浏览</div>
        <div class="capsule">点数商店</div>
      </div>

      <div class="section-title">浏览所有游戏</div>

      <div class="game-grid" v-loading="loading" element-loading-background="rgba(0,0,0,0)">
        <div
            class="game-card"
            v-for="game in gameList"
            :key="game.id"
            @click="goToDetail(game.id)"
        >
          <div class="card-img">
            <img :src="game.coverUrl" loading="lazy" />
          </div>

          <div class="card-info">
            <div class="title" :title="game.title">{{ game.title }}</div>
            <div class="tags" v-if="game.tags">
              {{ game.tags.split(',').slice(0, 2).join(', ') }}
            </div>

            <div class="price-row">
              <div class="discount-badge">-20%</div>
              <div class="price-box">
                <div class="original-price">¥ {{ (game.price * 1.25).toFixed(2) }}</div>
                <div class="final-price">¥ {{ game.price }}</div>
              </div>
            </div>
          </div>
        </div>
      </div>

      <div v-if="!loading && gameList.length === 0" class="empty-list">
        暂无上架游戏，请去后台添加数据。
      </div>

    </div>
  </div>
</template>

<style scoped>
.home-container {
  background-color: #1b2838;
  min-height: 100vh;
  color: #c7d5e0;
  font-family: "Motiva Sans", Arial, sans-serif;
  padding-bottom: 80px;
}

/* ★ 修改：GIF 背景区域样式 ★ */
.hero-banner-gif {
  position: relative;
  width: 100%;
  height: 450px; /* 您可以根据 GIF 的比例调整这个高度 */
  overflow: hidden;
  margin-bottom: 30px;
  background-color: #000;
  box-shadow: 0 10px 20px rgba(0,0,0,0.5);
  border-bottom: 1px solid #66c0f4; /* 加一条蓝色底边，更像活动页 */
}

.gif-bg {
  width: 100%;
  height: 100%;
  object-fit: cover; /* 关键：让 GIF 铺满整个区域，裁剪多余部分 */
  object-position: center center;
  display: block;
}

/* 底部渐变遮罩 */
.gif-overlay-gradient {
  position: absolute;
  bottom: 0;
  left: 0;
  width: 100%;
  height: 100px; /* 渐变高度 */
  /* 从透明过渡到网页背景色，实现无缝衔接 */
  background: linear-gradient(to bottom, rgba(27,40,56,0) 0%, #1b2838 100%);
  pointer-events: none;
}

/* 下方内容样式保持不变 */
.content-width { width: 940px; margin: 0 auto; position: relative; z-index: 2; }

.store-capsule-bar {
  display: flex;
  gap: 10px;
  margin-bottom: 30px;
  background: rgba(0,0,0,0.2);
  padding: 5px;
  border-radius: 4px;
}
.capsule {
  padding: 8px 16px;
  background: #212c42;
  border-radius: 2px;
  font-size: 13px;
  color: #fff;
  cursor: pointer;
  transition: 0.2s;
}
.capsule:hover, .capsule.active {
  background: linear-gradient(90deg, #06BFFF 0%, #2D73FF 100%);
  color: #fff;
  text-shadow: 1px 1px 2px rgba(0,0,0,0.5);
}

.section-title {
  font-size: 18px;
  color: #fff;
  text-transform: uppercase;
  margin-bottom: 15px;
  letter-spacing: 1px;
}

/* 游戏卡片网格 */
.game-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr); /* 3列布局 */
  gap: 20px;
}

/* 单个卡片样式 (Steam 风格) */
.game-card {
  background: #16202d;
  transition: transform 0.2s, box-shadow 0.2s;
  cursor: pointer;
  position: relative;
  overflow: hidden;
  box-shadow: 0 4px 10px rgba(0,0,0,0.3);
}
.game-card:hover {
  transform: translateY(-5px) scale(1.02);
  box-shadow: 0 10px 20px rgba(0,0,0,0.5);
  background: #2a475e; /* 悬停变亮蓝 */
}

.card-img {
  width: 100%;
  height: 160px;
  overflow: hidden;
}
.card-img img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: opacity 0.2s;
}
.game-card:hover .card-img img {
  opacity: 0.8;
}

.card-info {
  padding: 12px 15px;
}

.title {
  font-size: 16px;
  color: #c7d5e0;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  margin-bottom: 5px;
}
.game-card:hover .title { color: #fff; }

.tags {
  font-size: 11px;
  color: #556772;
  margin-bottom: 10px;
}

/* 价格行 */
.price-row {
  display: flex;
  align-items: center;
  justify-content: flex-end; /* 价格靠右 */
  gap: 8px;
}

.discount-badge {
  background: #4c6b22;
  color: #a4d007;
  padding: 2px 6px;
  font-size: 14px;
  font-weight: bold;
}

.price-box {
  text-align: right;
  line-height: 1.2;
}
.original-price {
  color: #738895;
  font-size: 11px;
  text-decoration: line-through;
}
.final-price {
  color: #fff;
  font-size: 15px;
}

.empty-list {
  text-align: center;
  padding: 50px;
  color: #8f98a0;
  font-size: 16px;
}
</style>