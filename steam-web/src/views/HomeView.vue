<script setup>
import { ref, onMounted } from 'vue'
import request from '@/utils/request'
import { useRouter } from 'vue-router'

const router = useRouter()
const gameList = ref([])      // 所有游戏
const carouselList = ref([])  // 轮播图专用数据 (取前5个)

// 获取数据
const fetchGames = async () => {
  try {
    const data = await request.get('/api/game/list')
    gameList.value = data
    // 截取前 5 个作为轮播图展示
    if (data.length > 0) {
      carouselList.value = data.slice(0, 5)
    }
  } catch (error) {
    console.error("获取游戏失败", error)
  }
}

// 跳转详情页
const goToDetail = (id) => {
  router.push(`/game/${id}`)
}

onMounted(() => {
  fetchGames()
})
</script>

<template>
  <div class="home-container">

    <div class="background-glow"></div>

    <div class="content-width">

      <div class="store-nav">
        <span class="nav-btn">您的商店</span>
        <span class="nav-btn">新鲜推荐</span>
        <span class="nav-btn">类别</span>
        <span class="nav-btn">点数商店</span>
        <span class="nav-btn">新闻</span>
        <span class="nav-btn">实验室</span>
      </div>

      <h2 class="section-title">精选和推荐</h2>

      <div class="carousel-wrapper" v-if="carouselList.length > 0">
        <el-carousel :interval="4000" type="card" height="350px" indicator-position="outside">
          <el-carousel-item v-for="item in carouselList" :key="item.id">
            <div class="carousel-card" @click="goToDetail(item.id)">
              <img :src="item.coverUrl" class="carousel-img" />
              <div class="carousel-info">
                <h3>{{ item.title }}</h3>
                <div class="carousel-tags">
                  <span class="tag">现已上架</span>
                  <span class="tag" v-if="item.tags">{{ item.tags.split(',')[0] }}</span>
                </div>
                <div class="carousel-price">
                  <span v-if="item.price > 0">$ {{ item.price }}</span>
                  <span v-else>免费开玩</span>
                </div>
              </div>
            </div>
          </el-carousel-item>
        </el-carousel>
      </div>

      <h2 class="section-title">特别优惠</h2>

      <div v-if="gameList.length === 0" class="empty-tip">
        正在加载数据，或请运行爬虫脚本填充数据库...
      </div>

      <div class="game-grid">
        <div class="game-card" v-for="game in gameList" :key="game.id" @click="goToDetail(game.id)">
          <div class="card-img-wrapper">
            <img :src="game.coverUrl" class="card-img" />
          </div>

          <div class="card-info">
            <div class="game-title">{{ game.title }}</div>
            <div class="game-dev">{{ game.developer }}</div>

            <div class="price-row">
              <span class="discount" v-if="game.price > 0">-20%</span>
              <div class="price-box">
                <span class="old-price" v-if="game.price > 0">$ {{ (game.price * 1.2).toFixed(0) }}</span>
                <span class="final-price">
                    {{ game.price > 0 ? '$ ' + game.price : '免费' }}
                </span>
              </div>
            </div>
          </div>
        </div>
      </div>

    </div>
  </div>
</template>

<style scoped>
/* 容器与背景 */
.home-container {
  position: relative;
  min-height: 100vh;
  padding-bottom: 50px;
}
.background-glow {
  position: absolute;
  top: 0; left: 0; width: 100%; height: 600px;
  background: radial-gradient(circle at center top, #1b2838 0%, #171a21 100%);
  z-index: -1;
}

.content-width {
  width: 940px;
  margin: 0 auto;
}

/* 导航条 */
.store-nav {
  display: flex;
  gap: 10px;
  background: rgba(62, 126, 167, 0.2);
  padding: 5px;
  margin-top: 20px;
  margin-bottom: 30px;
  border-radius: 3px;
  box-shadow: 0 0 5px rgba(0,0,0,0.5);
}
.nav-btn {
  color: #e5e5e5;
  font-size: 13px;
  padding: 5px 10px;
  cursor: pointer;
  border-radius: 2px;
  transition: 0.2s;
}
.nav-btn:hover {
  background: linear-gradient( to bottom, #e3eaef 5%, #c7d5e0 95%);
  color: #333;
}

.section-title {
  color: #fff;
  font-size: 14px;
  margin-bottom: 10px;
  letter-spacing: 1px;
  text-transform: uppercase;
  margin-top: 40px;
}

/* 轮播图样式 */
.carousel-wrapper {
  margin-bottom: 50px;
}
.carousel-card {
  width: 100%;
  height: 100%;
  position: relative;
  border-radius: 4px;
  overflow: hidden;
  cursor: pointer;
  box-shadow: 0 10px 20px rgba(0,0,0,0.5);
}
.carousel-img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.3s;
}
.carousel-card:hover .carousel-img {
  transform: scale(1.05);
}
.carousel-info {
  position: absolute;
  bottom: 0; left: 0; width: 100%;
  background: linear-gradient(to top, rgba(0,0,0,0.9), transparent);
  padding: 20px;
  color: #fff;
}
.carousel-info h3 { margin: 0 0 5px 0; font-size: 20px; text-shadow: 0 2px 4px #000; }
.carousel-tags .tag {
  background: rgba(255,255,255,0.2);
  padding: 2px 5px;
  font-size: 11px;
  margin-right: 5px;
  border-radius: 2px;
}
.carousel-price {
  font-size: 16px;
  color: #66c0f4;
  margin-top: 5px;
  font-weight: bold;
}

/* 游戏网格列表 */
.game-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr); /* 一行3个 */
  gap: 15px;
}
.empty-tip {
  color: #8f98a0; text-align: center; padding: 40px;
}

.game-card {
  background: #16202d;
  cursor: pointer;
  transition: transform 0.2s, background 0.2s;
  box-shadow: 0 0 10px rgba(0,0,0,0.3);
  position: relative;
}
.game-card:hover {
  transform: scale(1.03);
  background: #1f2c3d;
  z-index: 10;
  box-shadow: 0 5px 15px rgba(0,0,0,0.5);
}

.card-img-wrapper {
  height: 160px;
  overflow: hidden;
  position: relative;
}
.card-img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.card-info {
  padding: 10px;
}
.game-title {
  color: #c7d5e0;
  font-size: 14px;
  margin-bottom: 3px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}
.game-dev {
  color: #556772;
  font-size: 11px;
}

/* 价格区域 */
.price-row {
  display: flex;
  align-items: center;
  margin-top: 10px;
}
.discount {
  background: #4c6b22;
  color: #a4d007;
  padding: 1px 4px;
  font-size: 12px;
  margin-right: 5px;
}
.price-box {
  display: flex;
  flex-direction: column;
  align-items: flex-end;
  margin-left: auto;
}
.old-price {
  color: #738895;
  font-size: 11px;
  text-decoration: line-through;
}
.final-price {
  color: #acdbf5;
  font-size: 14px;
}

/* 调整 Element Carousel 样式 */
:deep(.el-carousel__mask) {
  background-color: #000;
  opacity: 0.6;
}
</style>