<script setup>
import { ref, onMounted, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import request from '@/utils/request'

const route = useRoute()
const router = useRouter()
const game = ref({})
const screenshots = ref([])
const activeImage = ref('')

// Steam 原版图标 SVG (保持不变)
const steamIcons = {
  '单人': 'M9 11c-1.66 0-3-1.34-3-3s1.34-3 3-3 3 1.34 3 3-1.34 3-3 3zm0 2c-2.33 0-7 1.17-7 3.5V19h14v-2.5c0-2.33-4.67-3.5-7-3.5z',
  '多人': 'M16 11c1.66 0 2.99-1.34 2.99-3S17.66 5 16 5c-1.66 0-3 1.34-3 3s1.34 3 3 3zm-8 0c1.66 0 2.99-1.34 2.99-3S9.66 5 8 5C6.34 5 5 6.34 5 8s1.34 3 3 3zm0 2c-2.33 0-7 1.17-7 3.5V19h14v-2.5c0-2.33-4.67-3.5-7-3.5zm8 0c-.29 0-.62.02-.97.05 1.16.84 1.97 1.97 1.97 3.45V19h6v-2.5c0-2.33-4.67-3.5-7-3.5z',
  '在线玩家对战': 'M16 11c1.66 0 2.99-1.34 2.99-3S17.66 5 16 5c-1.66 0-3 1.34-3 3s1.34 3 3 3zm-8 0c1.66 0 2.99-1.34 2.99-3S9.66 5 8 5C6.34 5 5 6.34 5 8s1.34 3 3 3zm0 2c-2.33 0-7 1.17-7 3.5V19h14v-2.5c0-2.33-4.67-3.5-7-3.5zm8 0c-.29 0-.62.02-.97.05 1.16.84 1.97 1.97 1.97 3.45V19h6v-2.5c0-2.33-4.67-3.5-7-3.5z',
  'Steam 成就': 'M17 12h-2V9h2v3zm-4 0h-2V9h2v3zm-4 0H7V9h2v3zm10-7h-4V3H9v2H5c-1.1 0-2 .9-2 2v12c0 1.1.9 2 2 2h14c1.1 0 2-.9 2-2V7c0-1.1-.9-2-2-2z',
  'Steam 云': 'M19.35 10.04C18.67 6.59 15.64 4 12 4 9.11 4 6.6 5.64 5.35 8.04 2.34 8.36 0 10.91 0 14c0 3.31 2.69 6 6 6h13c2.76 0 5-2.24 5-5 0-2.64-2.05-4.78-4.65-4.96z',
  'Steam 集换式卡牌': 'M20 4H4c-1.1 0-2 .9-2 2v12c0 1.1.9 2 2 2h16c1.1 0 2-.9 2-2V6c0-1.1-.9-2-2-2zm0 14H4V6h16v12zM6 10h2v2H6zm0 4h2v2H6zm4-4h8v2h-8zm0 4h5v2h-5z',
  '完全支持控制器': 'M21 9H3c-1.1 0-2 .9-2 2v3c0 1.1.9 2 2 2h18c1.1 0 2-.9 2-2v-3c0-1.1-.9-2-2-2zm-9 6c-.55 0-1-.45-1-1s.45-1 1-1 1 .45 1 1-.45 1-1 1zm4 0h-2v-2h2v2zm2-3h-2v-2h2v2z',
  '部分支持控制器': 'M21 9H3c-1.1 0-2 .9-2 2v3c0 1.1.9 2 2 2h18c1.1 0 2-.9 2-2v-3c0-1.1-.9-2-2-2zm-9 6c-.55 0-1-.45-1-1s.45-1 1-1 1 .45 1 1-.45 1-1 1zm4 0h-2v-2h2v2zm2-3h-2v-2h2v2z',
  '家庭共享': 'M15 8c0-1.42-.5-2.73-1.33-3.76.42-.14.86-.24 1.33-.24 2.21 0 4 1.79 4 4s-1.79 4-4 4c-.43 0-.84-.09-1.23-.21-.03-.01-.06-.02-.08-.03C14.5 10.73 15 9.42 15 8zm-8 8c0-2.21 1.79-4 4-4s4 1.79 4 4-1.79 4-4 4-4-1.79-4-4-4 1.79-4 4zM4 8c0 2.21 1.79 4 4 4s4-1.79 4-4-1.79-4-4-4 1.79-4 4z',
  '应用内购买': 'M11 9h2V6h3V4h-3V1h-2v3H8v2h3v3zm-4 9c-1.1 0-1.99.9-1.99 2S5.9 22 7 22s2-.9 2-2-.9-2-2-2zm10 0c-1.1 0-1.99.9-1.99 2s.89 2 1.99 2 2-.9 2-2-.9-2-2-2zm-9.83-3.25l.03-.12.9-1.63h7.45c.75 0 1.41-.41 1.75-1.03l3.86-7.01L19.42 4h-.01l-1.1 2-2.76 5H8.53l-.13-.27L6.16 6l-.95-2-.94-2H1v2h2l3.6 7.59-1.35 2.45c-.16.28-.25.61-.25.96 0 1.1.9 2 2 2h12v-2H7.42c-.13 0-.25-.11-.25-.25z',
  'default': 'M12 2C6.48 2 2 6.48 2 12s4.48 10 10 10 10-4.48 10-10S17.52 2 12 2zm1 15h-2v-6h2v6zm0-8h-2V7h2v2z'
}

const getIconPath = (featName) => {
  for (const key in steamIcons) {
    if (featName.includes(key)) return steamIcons[key]
  }
  return steamIcons['default']
}

const parsedLanguages = computed(() => {
  if (!game.value.supportedLanguages) return []
  return game.value.supportedLanguages.replace(/<br>/g, '').split(', ').map(s => ({
    name: s.replace('<strong>*</strong>', '').trim(),
    audio: s.includes('*')
  }))
})

const fetchDetail = async () => {
  // 这里暂时复用 List 接口演示，实际应使用 getById
  const all = await request.get('/api/game/list')
  const found = all.find(g => g.id == route.params.id)
  if (found) {
    game.value = found
    try {
      if(found.screenshots) {
        const imgs = JSON.parse(found.screenshots)
        screenshots.value = imgs
      }
      activeImage.value = screenshots.value.length > 0 ? screenshots.value[0] : found.coverUrl
    } catch(e) {
      console.error("图片解析失败", e)
      activeImage.value = found.coverUrl
    }
  }
}

const goToDev = (name) => router.push(`/developer/${name}`)
const goToCategory = (tag) => router.push(`/category/${tag}`)

onMounted(fetchDetail)
</script>

<template>
  <div class="detail-wrapper-2025">
    <div class="bg-blur" :style="{ backgroundImage: `url(${game.coverUrl})` }"></div>

    <div class="container-1200">
      <div class="top-nav">
        <div class="crumbs">所有游戏 > {{ game.tags ? game.tags.split(',')[0] : '游戏' }} > {{ game.title }}</div>
        <h2 class="title">{{ game.title }}</h2>
      </div>

      <div class="highlight-box-wide">
        <div class="media-player-wide">
          <div class="screen-wide">
            <img :src="activeImage" class="full-media" />
          </div>
          <div class="thumbs-wide">
            <div
                v-for="(img, i) in screenshots" :key="i"
                class="thumb-item"
                :class="{ active: activeImage === img }"
                @mouseover="activeImage = img"
            >
              <img :src="img" />
            </div>
          </div>
        </div>

        <div class="info-sidebar-wide">
          <img :src="game.coverUrl" class="header-img-wide" />
          <div class="desc-wide" :title="game.shortDescription">{{ game.shortDescription }}</div>

          <div class="meta-table">
            <div class="row"><span class="label">发行日期:</span> <span class="val">{{ game.releaseDate }}</span></div>
            <div class="row"><span class="label">开发商:</span> <span class="val link" @click="goToDev(game.developer)">{{ game.developer }}</span></div>
            <div class="row"><span class="label">发行商:</span> <span class="val link" @click="goToDev(game.developer)">{{ game.developer }}</span></div>
          </div>

          <div class="tags-cloud">
            <span class="tag-title">该产品的热门用户自定义标签：</span>
            <div>
              <span class="tag" v-for="t in (game.tags ? game.tags.split(',').slice(0,5) : [])" :key="t" @click="goToCategory(t)">
                {{ t }}
              </span>
              <span class="tag plus">+</span>
            </div>
          </div>
        </div>
      </div>

      <div class="buy-area-wide">
        <h1>购买 {{ game.title }}</h1>
        <div class="buy-btn-row">
          <div class="price">¥ {{ game.price }}</div>
          <button class="btn-green">添加至购物车</button>
        </div>
      </div>

      <div class="content-split-wide">
        <div class="left-main-wide">
          <div class="section-title">关于这款游戏</div>
          <div class="line"></div>
          <div class="html-content-wide" v-html="game.aboutTheGame"></div>
        </div>

        <div class="right-sidebar-wide">
          <div class="block features" v-if="game.gameFeatures">
            <div class="feat-row" v-for="feat in game.gameFeatures.split(',')" :key="feat">
              <div class="icon-box">
                <svg viewBox="0 0 24 24" width="16" height="16" fill="#66c0f4">
                  <path :d="getIconPath(feat)"></path>
                </svg>
              </div>
              <div class="feat-text">{{ feat }}</div>
            </div>
          </div>

          <div class="block langs">
            <div class="block-head">语言</div>
            <table class="lang-tbl">
              <thead><tr><th></th><th>界面</th><th>完全音频</th><th>字幕</th></tr></thead>
              <tbody>
              <tr v-for="l in parsedLanguages" :key="l.name">
                <td class="lname">{{ l.name }}</td>
                <td>✔</td>
                <td>{{ l.audio ? '✔' : '' }}</td>
                <td>✔</td>
              </tr>
              </tbody>
            </table>
            <div class="more-link">查看所有支持语言</div>
          </div>

          <div class="block meta" v-if="game.metacriticScore > 0">
            <div class="meta-score">{{ game.metacriticScore }}</div>
            <div class="meta-logo">
              <div class="meta-title">metacritic</div>
              <a :href="game.metacriticUrl" target="_blank" class="meta-link">阅读游戏评测</a>
            </div>
          </div>

          <div class="block links">
            <div class="link-row"><a :href="game.website" target="_blank">访问网站</a></div>
            <div class="link-row"><a :href="`https://steamcommunity.com/app/${game.steamId}`" target="_blank">查看更新记录</a></div>
            <div class="link-row"><a href="#">阅读相关新闻</a></div>
            <div class="link-row"><a href="#">查看讨论</a></div>
          </div>

        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.detail-wrapper-2025 { background: #1b2838; min-height: 100vh; color: #c6d4df; font-family: "Motiva Sans", Arial, sans-serif; position: relative; }
.bg-blur { position: absolute; width: 100%; height: 600px; background-position: center top; background-size: cover; opacity: 0.15; mask-image: linear-gradient(to bottom, #000, transparent); z-index: 0; }
.container-1200 { width: 1200px; margin: 0 auto; position: relative; z-index: 1; padding-bottom: 50px; }

.top-nav { padding: 25px 0; }
.crumbs { font-size: 13px; color: #8f98a0; margin-bottom: 8px; }
.title { font-size: 32px; color: #fff; font-weight: normal; margin: 0; text-shadow: 0 0 5px rgba(0,0,0,0.5); }

.highlight-box-wide { display: flex; justify-content: space-between; margin-bottom: 40px; }
.media-player-wide { width: 800px; }
.screen-wide { width: 800px; height: 450px; background: #000; display: flex; align-items: center; justify-content: center; overflow: hidden; position: relative; box-shadow: 0 0 20px rgba(0,0,0,0.6); }
.full-media { width: 100%; height: 100%; object-fit: contain; }

.thumbs-wide { display: flex; gap: 6px; margin-top: 6px; overflow-x: auto; padding-bottom: 5px; }
.thumb-item { width: 155px; height: 87px; background: #000; opacity: 0.6; cursor: pointer; position: relative; flex-shrink: 0; transition: 0.2s; }
.thumb-item.active, .thumb-item:hover { opacity: 1; border: 2px solid #fff; }
.thumb-item img { width: 100%; height: 100%; object-fit: cover; }

.info-sidebar-wide { width: 370px; font-size: 14px; }
.header-img-wide { width: 370px; height: 173px; object-fit: cover; margin-bottom: 15px; box-shadow: 0 0 10px rgba(0,0,0,0.5); border-radius: 2px; }
.desc-wide { color: #c6d4df; margin-bottom: 15px; line-height: 1.6; height: 85px; overflow: hidden; display: -webkit-box; -webkit-line-clamp: 4; -webkit-box-orient: vertical; }
.row { display: flex; margin-bottom: 5px; }
.label { width: 100px; color: #556772; text-transform: uppercase; font-size: 11px; padding-top: 3px;}
.val { color: #66c0f4; }
.link { text-decoration: underline; cursor: pointer; }
.link:hover { color: #fff; }
.tag-title { display: block; margin-bottom: 8px; font-size: 12px; color: #556772; }
.tag { background: #223a4c; color: #67c1f5; padding: 4px 8px; margin: 0 4px 4px 0; display: inline-block; font-size: 12px; cursor: pointer; border-radius: 2px;}
.tag:hover { background: #67c1f5; color: #fff; }

.buy-area-wide {
  background: linear-gradient(to right, rgba(0,0,0,0.6), rgba(0,0,0,0.3));
  padding: 25px; border-radius: 4px; margin-bottom: 40px; position: relative;
  border: 1px solid rgba(255,255,255,0.05);
}
.buy-area-wide h1 { margin: 0 0 10px 0; font-size: 26px; color: #fff; font-weight: normal; }
.buy-btn-row { background: #000; padding: 2px; position: absolute; right: 25px; top: 25px; border-radius: 2px; display: flex; align-items: center; }
.price { padding: 0 20px; font-size: 16px; color: #c6d4df; }
.btn-green { background: linear-gradient(to bottom, #75b022 5%, #588a1b 95%); border: 0; color: #d2efa9; padding: 0 25px; cursor: pointer; height: 40px; font-size: 16px; text-shadow: 1px 1px 0 rgba(0,0,0,0.3); }
.btn-green:hover { background: linear-gradient(to bottom, #8ed629 5%, #6aa621 95%); color: #fff; }

.content-split-wide { display: flex; justify-content: space-between; }
.left-main-wide { width: 800px; }
.right-sidebar-wide { width: 370px; }
.section-title { color: #fff; font-size: 16px; text-transform: uppercase; letter-spacing: 1px; }
.line { height: 1px; background: linear-gradient(to right, #363c44, #1b2838); margin: 8px 0 20px 0; }

/* ★★★ 核心修复：强制图片适应容器宽度 ★★★ */
:deep(.html-content-wide) {
  overflow-wrap: break-word; /* 防止长文本溢出 */
}
:deep(.html-content-wide img) {
  max-width: 100% !important; /* 强制最大宽度为父容器宽 (800px) */
  height: auto !important;    /* 高度自动，保持比例 */
  display: block;
  margin: 15px auto;
  border-radius: 2px;
}
:deep(.html-content-wide h2) { color: #fff; font-size: 18px; border-bottom: 1px solid #363c44; margin-top: 30px; padding-bottom: 8px; text-transform: uppercase; }

.block { background: rgba(0,0,0,0.2); padding: 18px; margin-bottom: 20px; font-size: 13px; }
.feat-row { display: flex; align-items: center; gap: 10px; padding: 6px 10px; background: #1f2f42; margin-bottom: 3px; border-radius: 2px; color: #66c0f4; }
.lang-tbl { width: 100%; border-collapse: collapse; margin-bottom: 8px; }
.lang-tbl th { text-align: left; font-weight: normal; color: #556772; border-bottom: 1px solid #3c4450; padding: 6px; }
.lang-tbl td { border-bottom: 1px solid #1b2838; padding: 6px; color: #8f98a0; }
.lname { color: #c6d4df; }

.meta { display: flex; align-items: center; gap: 12px; }
.meta-score { background: #6c3; color: #fff; font-size: 28px; font-weight: bold; padding: 6px 12px; border-radius: 4px; }
.meta-title { font-weight: bold; color: #fff; text-transform: uppercase; font-size: 14px; }
.links a { color: #66c0f4; text-decoration: none; display: block; padding: 8px 12px; background: rgba(0,0,0,0.2); border-radius: 2px; margin-bottom: 6px; transition: 0.2s; }
.links a:hover { background: #66c0f4; color: #fff; }
</style>