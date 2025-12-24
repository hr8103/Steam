<script setup>
import { ref, computed, onMounted } from 'vue'
import { cartState, removeFromCart } from '@/utils/cart'
import { useRouter } from 'vue-router'
import request from '@/utils/request'

const router = useRouter()
const recommendGames = ref([]) // 推荐游戏列表

// 计算总价
const totalPrice = computed(() => {
  return cartState.value.reduce((sum, item) => sum + Number(item.price), 0).toFixed(2)
})

const handleRemove = (id) => {
  removeFromCart(id)
}

// 获取推荐游戏 (模拟 Steam 底部推荐)
const fetchRecommendations = async () => {
  try {
    const res = await request.get('/api/game/list')
    const all = Array.isArray(res) ? res : (res.data || [])
    // 随机打乱并取前 4 个
    recommendGames.value = all.sort(() => 0.5 - Math.random()).slice(0, 4)
  } catch (e) {
    console.error(e)
  }
}

// 模拟跳转支付
const handlePayment = () => {
  alert("正在跳转至第三方支付平台...")
  // 这里后续接支付接口
}

onMounted(fetchRecommendations)
</script>

<template>
  <div class="cart-page-steam">
    <div class="content-width">
      <div class="steam-breadcrumbs">
        <span @click="router.push('/')">所有产品</span> &gt; 您的购物车
      </div>

      <h2 class="page-title">您的购物车</h2>

      <div class="cart-layout">
        <div class="cart-main">

          <div v-if="cartState.length > 0" class="cart-list">
            <div class="cart-item" v-for="item in cartState" :key="item.id">
              <div class="item-visual" @click="router.push(`/game/${item.id}`)">
                <img :src="item.coverUrl" alt="">
              </div>
              <div class="item-content">
                <div class="item-name" @click="router.push(`/game/${item.id}`)">{{ item.title }}</div>
                <div class="item-actions">
                  <span class="remove-btn" @click="handleRemove(item.id)">移除</span>
                </div>
              </div>
              <div class="item-price">
                $ {{ Number(item.price).toFixed(2) }}
              </div>
            </div>
          </div>

          <div v-else class="empty-state">
            <div class="msg">您的购物车中没有商品。</div>
            <div class="sub-msg">点击下方按钮浏览商城。</div>
            <button class="btn-continue" @click="router.push('/')">继续购物</button>
          </div>

          <div class="recommend-section" v-if="recommendGames.length > 0">
            <div class="rec-title">为您推荐</div>
            <div class="rec-grid">
              <div class="rec-card" v-for="game in recommendGames" :key="game.id" @click="router.push(`/game/${game.id}`)">
                <div class="rec-img"><img :src="game.coverUrl"></div>
                <div class="rec-info">
                  <div class="rec-name" :title="game.title">{{ game.title }}</div>
                  <div class="rec-price">$ {{ game.price }}</div>
                </div>
              </div>
            </div>
          </div>
        </div>

        <div class="cart-sidebar" v-if="cartState.length > 0">
          <div class="checkout-panel">
            <div class="summary-row">
              <div class="label">预计总额</div>
              <div class="val">$ {{ totalPrice }}</div>
            </div>
            <div class="tax-note">销售税将在结账时计算</div>

            <button class="btn-checkout-steam" @click="handlePayment">
              跳转至支付
            </button>
          </div>

          <button class="btn-continue-shopping" @click="router.push('/')">
            继续购物
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
/* 全局容器：Steam 新版深蓝背景 */
.cart-page-steam {
  background-color: #1b2838;
  min-height: 100vh;
  color: #c7d5e0;
  font-family: "Motiva Sans", Arial, sans-serif;
  padding-bottom: 80px;
}
.content-width { width: 940px; margin: 0 auto; padding-top: 24px; }

/* 面包屑 */
.steam-breadcrumbs { color: #8f98a0; font-size: 12px; margin-bottom: 15px; }
.steam-breadcrumbs span { cursor: pointer; }
.steam-breadcrumbs span:hover { color: #fff; }

/* 标题 */
.page-title {
  font-family: "Motiva Sans", sans-serif;
  font-weight: 300;
  font-size: 26px;
  text-transform: uppercase;
  letter-spacing: 2px;
  color: #fff;
  margin-bottom: 25px;
}

/* 布局：左右分栏 */
.cart-layout { display: flex; gap: 20px; align-items: flex-start; }
.cart-main { flex: 1; }
.cart-sidebar { width: 308px; flex-shrink: 0; }

/* 购物车列表项 (新版样式) */
.cart-list { background: rgba(0,0,0,0.2); border-radius: 4px; overflow: hidden; }
.cart-item {
  display: flex;
  background-color: #16202d; /* 卡片深色 */
  margin: 1px 0; /* 细微间隔 */
  padding: 16px;
  align-items: center;
  position: relative;
}
.item-visual {
  width: 120px;
  height: 56px;
  margin-right: 16px;
  cursor: pointer;
  background: #000;
}
.item-visual img { width: 100%; height: 100%; object-fit: cover; }

.item-content { flex: 1; display: flex; flex-direction: column; justify-content: center; }
.item-name {
  font-size: 16px;
  color: #c7d5e0;
  margin-bottom: 4px;
  cursor: pointer;
}
.item-name:hover { color: #fff; }

.item-actions { display: flex; gap: 10px; font-size: 12px; }
.remove-btn { color: #56707f; text-decoration: underline; cursor: pointer; }
.remove-btn:hover { color: #66c0f4; }

.item-price { font-size: 15px; color: #c7d5e0; text-align: right; min-width: 80px; }

/* 右侧结算面板 */
.checkout-panel {
  background-color: #16202d; /* 这里的背景色通常和列表项一致 */
  padding: 20px;
  border-radius: 4px;
  position: relative;
  margin-bottom: 15px;
}
.summary-row { display: flex; justify-content: space-between; margin-bottom: 8px; font-size: 14px; }
.summary-row .val { font-weight: bold; font-size: 16px; color: #fff; }
.tax-note { color: #626366; font-size: 11px; margin-bottom: 20px; font-style: italic; }

.btn-checkout-steam {
  width: 100%;
  background: linear-gradient(90deg, #75b022 0%, #588a1b 100%);
  border: none;
  padding: 15px;
  border-radius: 2px;
  color: #d2efa9;
  font-size: 16px;
  font-weight: 500;
  cursor: pointer;
  text-shadow: 1px 1px 0 rgba(0,0,0,0.3);
  transition: filter 0.2s;
}
.btn-checkout-steam:hover {
  background: linear-gradient(90deg, #8ed629 0%, #6aa621 100%);
  color: #fff;
}

.btn-continue-shopping {
  width: 100%;
  background: transparent;
  border: 1px solid rgba(255,255,255,0.1);
  color: #c7d5e0;
  padding: 10px;
  border-radius: 2px;
  cursor: pointer;
}
.btn-continue-shopping:hover { background: rgba(255,255,255,0.05); color: #fff; }

/* 空状态 */
.empty-state { text-align: center; padding: 60px 0; background: rgba(0,0,0,0.2); border-radius: 4px; }
.empty-state .msg { font-size: 18px; color: #fff; margin-bottom: 10px; }
.empty-state .sub-msg { color: #8f98a0; font-size: 13px; margin-bottom: 20px; }
.btn-continue { background: #66c0f4; border:none; color: #1b2838; padding: 10px 24px; font-weight: bold; border-radius: 2px; cursor: pointer; }

/* 底部推荐 */
.recommend-section { margin-top: 50px; }
.rec-title { font-size: 14px; color: #fff; border-bottom: 1px solid #3c4450; padding-bottom: 5px; margin-bottom: 15px; text-transform: uppercase; }
.rec-grid { display: grid; grid-template-columns: repeat(4, 1fr); gap: 15px; }
.rec-card { background: #16202d; cursor: pointer; transition: 0.2s; box-shadow: 0 4px 8px rgba(0,0,0,0.3); }
.rec-card:hover { transform: translateY(-3px); background: #2a3f5a; }
.rec-img { height: 80px; overflow: hidden; position: relative; }
.rec-img img { width: 100%; height: 100%; object-fit: cover; }
.rec-info { padding: 10px; }
.rec-name { font-size: 12px; color: #c7d5e0; white-space: nowrap; overflow: hidden; text-overflow: ellipsis; margin-bottom: 5px; }
.rec-price { color: #66c0f4; font-size: 12px; }
</style>