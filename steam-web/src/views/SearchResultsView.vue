<script setup>
import { ref, onMounted, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import request from '@/utils/request'
import { Search } from '@element-plus/icons-vue' // 用于空状态图标

const route = useRoute()
const router = useRouter()

const loading = ref(false)
const gameList = ref([])
const keyword = ref(route.query.q || '')
const sortBy = ref('relevance') // 默认排序

// 排序选项
const sortOptions = [
  { value: 'relevance', label: '相关性' },
  { value: 'released_desc', label: '发行日期' },
  { value: 'price_asc', label: '价格：从低到高' },
  { value: 'price_desc', label: '价格：从高到低' }
]

// 执行搜索
const doSearch = async () => {
  loading.value = true
  try {
    // 构建参数
    const params = {
      sort: sortBy.value
    }
    if (keyword.value) {
      params.q = keyword.value
    }

    // 调用后端新接口
    const res = await request.get('/api/game/search', { params })
    gameList.value = res
  } catch (error) {
    console.error("搜索失败", error)
    gameList.value = []
  } finally {
    loading.value = false
  }
}

// 跳转详情
const goToDetail = (id) => {
  router.push(`/game/${id}`)
}

// 监听路由查询参数变化（例如在搜索页再次搜索）
watch(() => route.query.q, (newVal) => {
  keyword.value = newVal || ''
  doSearch()
})

// 监听排序变化
watch(sortBy, () => {
  doSearch()
})

onMounted(() => {
  doSearch()
})
</script>

<template>
  <div class="search-container">
    <div class="content-width">

      <div class="search-header">
        <h2 class="result-title" v-if="keyword">
          搜索结果：正在显示 "{{ keyword }}" 的匹配项
        </h2>
        <h2 class="result-title" v-else>
          所有产品
        </h2>

        <div class="sort-box">
          <span class="sort-label">排序依据</span>
          <el-select v-model="sortBy" class="steam-select" popper-class="steam-select-popper">
            <el-option
                v-for="item in sortOptions"
                :key="item.value"
                :label="item.label"
                :value="item.value"
            />
          </el-select>
        </div>
      </div>

      <div class="result-list-area" v-loading="loading" element-loading-background="rgba(0,0,0,0.2)">

        <div v-if="!loading && gameList.length === 0" class="no-results">
          <div class="no-results-icon"><el-icon><Search /></el-icon></div>
          <h3>0 个匹配的搜索结果。</h3>
          <p>您的搜索项没有返回任何结果。这就好像我们在寻找传说中的“半条命 3”一样……</p>
          <p>或者是您的输入有误？请尝试使用不同的关键词。</p>
        </div>

        <div class="search-result-row" v-for="game in gameList" :key="game.id" @click="goToDetail(game.id)">
          <div class="col-img">
            <img :src="game.coverUrl" alt="">
          </div>
          <div class="col-info">
            <div class="title">{{ game.title }}</div>
            <div class="details">
              <span class="platform">Windows</span>
              <span class="date">{{ game.releaseDate }}</span>
            </div>
          </div>
          <div class="col-price">
            <div class="price-box">
              ¥ {{ game.price }}
            </div>
          </div>
        </div>

      </div>

    </div>
  </div>
</template>

<style scoped>
.search-container {
  background: #1b2838; /* Steam 主背景 */
  min-height: 100vh;
  color: #c6d4df;
  padding: 30px 0;
}
.content-width { width: 940px; margin: 0 auto; }

/* 头部 */
.search-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  border-bottom: 1px solid #3c4450;
  padding-bottom: 15px;
  margin-bottom: 20px;
}
.result-title {
  font-size: 22px;
  color: #fff;
  font-weight: normal;
  margin: 0;
}

/* 排序控件 */
.sort-box {
  display: flex;
  align-items: center;
  background: rgba( 0, 0, 0, 0.2 );
  padding: 5px 10px;
  border-radius: 3px;
}
.sort-label {
  font-size: 12px;
  color: #556772;
  margin-right: 10px;
  text-transform: uppercase;
}
/* 深度定制 Element Select 以匹配 Steam 風格 */
:deep(.steam-select .el-input__wrapper) {
  background-color: transparent !important;
  box-shadow: none !important;
  padding: 0;
}
:deep(.steam-select .el-input__inner) {
  color: #66c0f4 !important;
  font-size: 12px;
  text-align: right;
  border: none;
  height: 24px;
}
:deep(.steam-select .el-select__caret) {
  color: #66c0f4;
}

/* 列表區域 */
.result-list-area {
  min-height: 300px;
}

/* 空狀態 */
.no-results {
  background: rgba(0,0,0,0.2);
  padding: 40px;
  text-align: center;
  border-radius: 4px;
  margin-top: 20px;
}
.no-results-icon { font-size: 48px; color: #3c4450; margin-bottom: 20px; }
.no-results h3 { color: #fff; font-size: 18px; margin-bottom: 10px; }
.no-results p { color: #8f98a0; font-size: 14px; }

/* 搜索結果行 (仿原版橫向佈局) */
.search-result-row {
  display: flex;
  background: rgba(0,0,0,0.2);
  margin-bottom: 5px;
  height: 69px; /* 原版高度 */
  cursor: pointer;
  transition: background 0.2s;
}
.search-result-row:hover {
  background: #2a3f5a; /* 高亮色 */
}

.col-img { width: 184px; flex-shrink: 0; }
.col-img img { width: 100%; height: 100%; object-fit: cover; }

.col-info { flex: 1; padding: 10px 15px; display: flex; flex-direction: column; justify-content: space-between; }
.col-info .title { font-size: 16px; color: #c7d5e0; }
.col-info .details { font-size: 12px; color: #556772; display: flex; gap: 10px;}

.col-price { width: 150px; display: flex; align-items: center; justify-content: flex-end; padding-right: 20px; }
.price-box { color: #c7d5e0; font-size: 14px; }

</style>

<style>
.steam-select-popper.el-popper {
  background: #3d4450 !important;
  border: 1px solid #1b2838 !important;
}
.steam-select-popper .el-select-dropdown__item {
  color: #c6d4df !important;
  font-size: 12px;
}
.steam-select-popper .el-select-dropdown__item.hover,
.steam-select-popper .el-select-dropdown__item.selected {
  background: #66c0f4 !important;
  color: #fff !important;
}
.steam-select-popper .el-popper__arrow::before {
  background: #3d4450 !important;
  border-color: #1b2838 !important;
}
</style>