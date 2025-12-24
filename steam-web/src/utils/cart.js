import { ref } from 'vue'

const CART_KEY = 'steam_cart_v1'

// 响应式的购物车列表
export const cartState = ref(getJSON())

function getJSON() {
    const str = localStorage.getItem(CART_KEY)
    try {
        return str ? JSON.parse(str) : []
    } catch (e) {
        return []
    }
}

// 保存数据
function save() {
    localStorage.setItem(CART_KEY, JSON.stringify(cartState.value))
}

// 添加商品
export function addToCart(game) {
    const exists = cartState.value.find(item => item.id === game.id)
    if (!exists) {
        cartState.value.push({
            id: game.id,
            title: game.title,
            price: game.price,
            coverUrl: game.coverUrl,
            addedAt: Date.now()
        })
        save()
        return true
    }
    return false
}

// 移除商品
export function removeFromCart(gameId) {
    cartState.value = cartState.value.filter(item => item.id !== gameId)
    save()
}

// 检查是否已添加
export function isInCart(gameId) {
    return cartState.value.some(item => item.id === gameId)
}

// 清空购物车
export function clearCart() {
    cartState.value = []
    save()
}