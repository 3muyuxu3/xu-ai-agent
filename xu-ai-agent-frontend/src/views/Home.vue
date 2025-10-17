<template>
  <div class="home-container">
    <div class="header">
      <div class="glitch-wrapper">
        <h1 class="glitch-title">木羽AI超级智能体</h1>
      </div>
      <p class="subtitle">/ 探索AI的无限可能 /</p>
      <div class="cyber-line"></div>
    </div>
    
    <div class="apps-container">
      <div class="app-card" @click="navigateTo('/love-master')">
        <div class="card-glow"></div>
        <div class="app-icon love-icon">❤️</div>
        <div class="app-info">
          <div class="app-title">AI恋爱大师</div>
          <div class="app-desc">智能情感顾问，帮你解答恋爱烦恼</div>
        </div>
        <div class="app-button">
          <span class="btn-text">立即体验</span>
          <span class="btn-icon">→</span>
        </div>
      </div>
      
      <div class="app-card" @click="navigateTo('/super-agent')">
        <div class="card-glow"></div>
        <div class="app-icon robot-icon">🤖</div>
        <div class="app-info">
          <div class="app-title">AI超级智能体</div>
          <div class="app-desc">全能型AI助手，解决各类专业问题</div>
        </div>
        <div class="app-button">
          <span class="btn-text">立即体验</span>
          <span class="btn-icon">→</span>
        </div>
      </div>
    </div>
    
    <!-- 粒子效果容器 -->
    <div class="particle-container" ref="particleContainer"></div>
    
    <div class="cyber-circles">
      <div class="circle circle-1"></div>
      <div class="circle circle-2"></div>
      <div class="circle circle-3"></div>
    </div>
    
    <AppFooter />
  </div>
</template>

<script setup>
import { ref, onMounted, onBeforeUnmount } from 'vue'
import { useRouter } from 'vue-router'
import { useHead } from '@vueuse/head'
import AppFooter from '../components/AppFooter.vue'

// 设置页面标题和元数据
useHead({
  title: '木羽AI超级智能体应用平台 - 首页',
  meta: [
    {
      name: 'description',
      content: '木羽AI超级智能体应用平台提供AI恋爱大师和AI超级智能体服务，满足您的各种AI对话需求'
    },
    {
      name: 'keywords',
      content: 'AI智能体,AI应用,AI恋爱大师,AI助手,智能对话,木羽,AI超级智能体,首页'
    }
  ]
})

const router = useRouter()
const particleContainer = ref(null)
let particles = []
let animationId = null
let mouseX = 0
let mouseY = 0
let isMouseMoving = false
let mouseMoveTimeout = null

// 粒子类
class Particle {
  constructor(container) {
    this.container = container
    this.x = Math.random() * container.offsetWidth
    this.y = Math.random() * container.offsetHeight
    this.size = Math.random() * 5 + 1
    this.speedX = Math.random() * 1 - 0.5
    this.speedY = Math.random() * 1 - 0.5
    this.originalSize = this.size
    
    // 彩色粒子
    const colors = [
      `rgba(255, 182, 193, ${Math.random() * 0.5 + 0.1})`, // 粉色
      `rgba(173, 216, 230, ${Math.random() * 0.5 + 0.1})`, // 浅蓝色
      `rgba(147, 112, 219, ${Math.random() * 0.5 + 0.1})`, // 紫色
      `rgba(255, 105, 180, ${Math.random() * 0.5 + 0.1})`, // 热粉色
      `rgba(255, 165, 0, ${Math.random() * 0.5 + 0.1})`,    // 橙色
      `rgba(152, 251, 152, ${Math.random() * 0.5 + 0.1})`   // 浅绿色
    ]
    this.color = colors[Math.floor(Math.random() * colors.length)]
  }
  
  update(mouseX, mouseY, isMouseMoving) {
    this.x += this.speedX
    this.y += this.speedY
    
    // 如果鼠标在移动，粒子会受到鼠标位置的影响
    if (isMouseMoving) {
      const dx = mouseX - this.x
      const dy = mouseY - this.y
      const distance = Math.sqrt(dx * dx + dy * dy)
      
      // 当鼠标靠近粒子时，粒子会稍微避开或被吸引
      if (distance < 100) {
        const force = (100 - distance) / 100
        this.speedX += (dx / distance) * force * 0.05
        this.speedY += (dy / distance) * force * 0.05
        
        // 鼠标靠近时粒子会稍微变大
        this.size = this.originalSize + force * 2
      } else {
        // 恢复原始大小
        this.size = this.originalSize
      }
    } else {
      // 恢复原始大小
      this.size = this.originalSize
    }
    
    // 限制速度
    this.speedX = Math.max(Math.min(this.speedX, 1), -1)
    this.speedY = Math.max(Math.min(this.speedY, 1), -1)
    
    // 边界检测
    if (this.x > this.container.offsetWidth || this.x < 0) {
      this.speedX = -this.speedX
    }
    if (this.y > this.container.offsetHeight || this.y < 0) {
      this.speedY = -this.speedY
    }
  }
  
  draw(ctx) {
    ctx.fillStyle = this.color
    ctx.beginPath()
    ctx.arc(this.x, this.y, this.size, 0, Math.PI * 2)
    ctx.fill()
  }
}

// 初始化粒子
const initParticles = () => {
  if (!particleContainer.value) return
  
  const canvas = document.createElement('canvas')
  const ctx = canvas.getContext('2d')
  canvas.width = particleContainer.value.offsetWidth
  canvas.height = particleContainer.value.offsetHeight
  particleContainer.value.appendChild(canvas)
  
  // 创建粒子
  particles = []
  const particleCount = Math.min(Math.floor((canvas.width * canvas.height) / 5000), 150)
  for (let i = 0; i < particleCount; i++) {
    particles.push(new Particle(particleContainer.value))
  }
  
  // 动画循环
  const animate = () => {
    ctx.clearRect(0, 0, canvas.width, canvas.height)
    
    // 更新和绘制粒子
    for (let i = 0; i < particles.length; i++) {
      particles[i].update(mouseX, mouseY, isMouseMoving)
      particles[i].draw(ctx)
    }
    
    // 连接相邻粒子
    for (let i = 0; i < particles.length; i++) {
      for (let j = i + 1; j < particles.length; j++) {
        const dx = particles[i].x - particles[j].x
        const dy = particles[i].y - particles[j].y
        const distance = Math.sqrt(dx * dx + dy * dy)
        
        if (distance < 100) {
          ctx.beginPath()
          // 根据粒子颜色确定连接线颜色
          const color1 = particles[i].color.split(',')[0].split('(')[1];
          const color2 = particles[j].color.split(',')[0].split('(')[1];
          const avgColor = Math.floor((parseInt(color1) + parseInt(color2)) / 2);
          
          // 使用粉色系的连接线
          ctx.strokeStyle = `rgba(255, 182, 193, ${0.2 * (1 - distance / 100)})`
          ctx.lineWidth = 0.5
          ctx.moveTo(particles[i].x, particles[i].y)
          ctx.lineTo(particles[j].x, particles[j].y)
          ctx.stroke()
        }
      }
    }
    
    animationId = requestAnimationFrame(animate)
  }
  
  animate()
}

// 鼠标移动事件
const handleMouseMove = (e) => {
  if (particleContainer.value) {
    const rect = particleContainer.value.getBoundingClientRect()
    mouseX = e.clientX - rect.left
    mouseY = e.clientY - rect.top
    
    // 标记鼠标正在移动
    isMouseMoving = true
    
    // 清除之前的超时
    if (mouseMoveTimeout) {
      clearTimeout(mouseMoveTimeout)
    }
    
    // 1秒后标记为鼠标停止移动
    mouseMoveTimeout = setTimeout(() => {
      isMouseMoving = false
    }, 1000)
  }
}

// 窗口大小调整
const handleResize = () => {
  if (animationId) {
    cancelAnimationFrame(animationId)
  }
  if (particleContainer.value) {
    particleContainer.value.innerHTML = ''
    initParticles()
  }
}

const navigateTo = (path) => {
  router.push(path)
}

// 组件挂载时初始化粒子
onMounted(() => {
  initParticles()
  window.addEventListener('resize', handleResize)
  if (particleContainer.value) {
    particleContainer.value.addEventListener('mousemove', handleMouseMove)
  }
})

// 组件销毁前清理
onBeforeUnmount(() => {
  if (animationId) {
    cancelAnimationFrame(animationId)
  }
  window.removeEventListener('resize', handleResize)
  if (particleContainer.value) {
    particleContainer.value.removeEventListener('mousemove', handleMouseMove)
  }
  if (mouseMoveTimeout) {
    clearTimeout(mouseMoveTimeout)
  }
})
</script>

<style scoped>
@import url('https://fonts.googleapis.com/css2?family=Orbitron:wght@400;500;700&display=swap');

/* 全局样式变量 */
:root {
  --neon-blue: #00f0ff;
  --neon-purple: #9000ff;
  --neon-pink: #ff00d4;
  --cyber-black: #0a0a12;
  --cyber-dark: #111122;
  --cyber-light: #edf7ff;
  --pink-light: #fff0f5; /* 更粉的背景色 */
  --pink-dark: #ffc0cb;  /* 粉色 */
  --pink-gradient-start: #ffccd5;
  --pink-gradient-end: #f8c6d0;
}

.home-container {
  display: flex;
  flex-direction: column;
  min-height: 100vh;
  /* 浅粉色背景 */
  background: var(--pink-light);
  position: relative;
  overflow: hidden;
}

/* 粒子效果容器 */
.particle-container {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  z-index: 1;
  overflow: hidden;
  pointer-events: none;
}

/* 赛博朋克风格标题 */
.header {
  padding: 70px 20px 50px;
  text-align: center;
  background-color: transparent;
  position: relative;
  z-index: 2;
}

.glitch-wrapper {
  position: relative;
  display: inline-block;
  margin-bottom: 20px;
}

.glitch-title {
  font-family: 'Orbitron', sans-serif;
  font-size: 3.2rem;
  font-weight: 700;
  color: var(--cyber-light);
  text-shadow: 
    0 0 5px rgba(0, 240, 255, 0.7),
    0 0 10px rgba(0, 240, 255, 0.5),
    0 0 20px rgba(0, 240, 255, 0.3);
  letter-spacing: 2px;
  position: relative;
  animation: glitch 3s infinite;
}

.glitch-title::before,
.glitch-title::after {
  content: '木羽AI超级智能体';
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  opacity: 0.8;
}

.glitch-title::before {
  color: var(--neon-pink);
  z-index: -1;
  animation: glitch-anim 2s infinite;
}

.glitch-title::after {
  color: var(--neon-blue);
  z-index: -2;
  animation: glitch-anim-2 3s infinite;
}

.subtitle {
  font-family: 'Orbitron', sans-serif;
  font-size: 1.2rem;
  color: #9000ff; /* 紫色 */
  max-width: 600px;
  margin: 0 auto 20px;
  letter-spacing: 3px;
  text-transform: uppercase;
  text-shadow: 0 0 10px rgba(144, 0, 255, 0.5);
}

.cyber-line {
  height: 2px;
  width: 80%;
  max-width: 600px;
  margin: 0 auto;
  background: linear-gradient(90deg, transparent, var(--neon-blue), transparent);
  position: relative;
}

.cyber-line::before,
.cyber-line::after {
  content: '';
  position: absolute;
  top: 50%;
  width: 10px;
  height: 10px;
  background-color: var(--neon-blue);
  border-radius: 50%;
  transform: translateY(-50%);
  box-shadow: 0 0 10px 2px var(--neon-blue);
}

.cyber-line::before {
  left: 20%;
}

.cyber-line::after {
  right: 20%;
}

.apps-container {
  display: flex;
  flex-wrap: wrap;
  justify-content: center;
  gap: 50px;
  width: 100%;
  margin: 60px auto;
  padding: 0 20px;
  flex: 1;
  position: relative;
  z-index: 2;
}

.app-card {
  width: 340px;
  background-color: rgba(17, 23, 41, 0.7);
  backdrop-filter: blur(10px);
  border-radius: 16px;
  box-shadow: 
    0 8px 32px rgba(0, 240, 255, 0.2),
    inset 0 0 0 1px rgba(255, 255, 255, 0.1);
  padding: 30px;
  cursor: pointer;
  transition: all 0.4s cubic-bezier(0.175, 0.885, 0.32, 1.275);
  display: flex;
  flex-direction: column;
  align-items: center;
  position: relative;
  overflow: hidden;
}

.card-glow {
  position: absolute;
  top: -50%;
  left: -50%;
  width: 200%;
  height: 200%;
  background: radial-gradient(
    circle at center,
    rgba(var(--neon-blue-rgb), 0.1) 0%,
    transparent 70%
  );
  opacity: 0;
  transition: opacity 0.5s;
  pointer-events: none;
}

.app-card:hover {
  transform: translateY(-15px) scale(1.03);
  box-shadow: 
    0 15px 50px rgba(0, 240, 255, 0.3),
    inset 0 0 0 1px rgba(0, 240, 255, 0.5);
}

.app-card:hover .card-glow {
  opacity: 1;
}

.app-icon {
  font-size: 4rem;
  margin-bottom: 25px;
  width: 90px;
  height: 90px;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 50%;
  position: relative;
  z-index: 1;
}

.love-icon {
  background: linear-gradient(135deg, #ff007a, #ff5722);
  box-shadow: 0 0 20px rgba(255, 0, 122, 0.5);
}

.robot-icon {
  background: linear-gradient(135deg, #00b2ff, #4f56ff);
  box-shadow: 0 0 20px rgba(0, 178, 255, 0.5);
}

.app-info {
  text-align: center;
  margin-bottom: 30px;
  width: 100%;
}

.app-title {
  font-family: 'Orbitron', sans-serif;
  font-size: 1.6rem;
  font-weight: bold;
  color: white;
  margin-bottom: 12px;
  text-shadow: 0 0 10px rgba(0, 240, 255, 0.5);
}

.app-desc {
  font-size: 1rem;
  color: rgba(255, 255, 255, 0.7);
  line-height: 1.6;
}

.app-button {
  background: linear-gradient(90deg, #0088ff, #00b2ff);
  color: white;
  padding: 12px 28px;
  border-radius: 30px;
  font-weight: 500;
  transition: all 0.3s;
  margin-top: auto;
  display: flex;
  align-items: center;
  position: relative;
  overflow: hidden;
  border: 1px solid rgba(0, 240, 255, 0.3);
}

.app-button::before {
  content: '';
  position: absolute;
  top: 0;
  left: -100%;
  width: 100%;
  height: 100%;
  background: linear-gradient(90deg, transparent, rgba(255, 255, 255, 0.2), transparent);
  transition: left 0.7s;
}

.app-button:hover {
  box-shadow: 0 0 15px rgba(0, 178, 255, 0.7);
  transform: scale(1.05);
}

.app-button:hover::before {
  left: 100%;
}

.btn-text {
  margin-right: 8px;
  letter-spacing: 1px;
}

.btn-icon {
  font-size: 1.2rem;
  transition: transform 0.3s;
}

.app-button:hover .btn-icon {
  transform: translateX(4px);
}

/* 背景圆圈动画 */
.cyber-circles {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  overflow: hidden;
  z-index: 1;
}

.circle {
  position: absolute;
  border-radius: 50%;
  opacity: 0.1;
}

.circle-1 {
  width: 300px;
  height: 300px;
  top: -100px;
  right: -100px;
  background: linear-gradient(135deg, var(--pink-light), var(--pink-dark));
  animation: float 15s infinite alternate;
  opacity: 0.2;
}

.circle-2 {
  width: 500px;
  height: 500px;
  bottom: -200px;
  left: -200px;
  background: linear-gradient(135deg, var(--pink-dark), var(--pink-light));
  animation: float 20s infinite alternate-reverse;
  opacity: 0.2;
}

.circle-3 {
  width: 200px;
  height: 200px;
  top: 40%;
  right: 15%;
  background: linear-gradient(135deg, var(--pink-dark), var(--pink-gradient-start));
  animation: float 12s infinite alternate;
  opacity: 0.2;
}

/* 动画效果 */
@keyframes float {
  0% {
    transform: translate(0, 0) rotate(0deg);
  }
  100% {
    transform: translate(50px, 50px) rotate(10deg);
  }
}

/* 背景流动动画 */
@keyframes gradientFlow {
  0% {
    background-position: 0% 50%;
  }
  50% {
    background-position: 100% 50%;
  }
  100% {
    background-position: 0% 50%;
  }
}

@keyframes glitch {
  0% {
    text-shadow: 
      0 0 5px rgba(0, 240, 255, 0.7),
      0 0 10px rgba(0, 240, 255, 0.5);
  }
  50% {
    text-shadow: 
      0 0 5px rgba(0, 240, 255, 0.7),
      0 0 10px rgba(0, 240, 255, 0.5),
      0 0 20px rgba(0, 240, 255, 0.3);
  }
  100% {
    text-shadow: 
      0 0 5px rgba(0, 240, 255, 0.7),
      0 0 10px rgba(0, 240, 255, 0.5);
  }
}

@keyframes glitch-anim {
  0%, 100% {
    transform: translate(0);
  }
  20% {
    transform: translate(-5px, 5px);
  }
  40% {
    transform: translate(-5px, -5px);
  }
  60% {
    transform: translate(5px, 5px);
  }
  80% {
    transform: translate(5px, -5px);
  }
}

@keyframes glitch-anim-2 {
  0%, 100% {
    transform: translate(0);
  }
  20% {
    transform: translate(3px, -3px);
  }
  40% {
    transform: translate(3px, 3px);
  }
  60% {
    transform: translate(-3px, -3px);
  }
  80% {
    transform: translate(-3px, 3px);
  }
}

/* 响应式设计 */
@media (max-width: 768px) {
  .glitch-title {
    font-size: 2.5rem;
  }
  
  .subtitle {
    font-size: 1rem;
  }
  
  .apps-container {
    gap: 30px;
    margin: 40px auto;
  }
  
  .app-card {
    width: 100%;
    max-width: 420px;
    padding: 25px;
  }
  
  .app-icon {
    font-size: 3.5rem;
    width: 80px;
    height: 80px;
  }
}

@media (max-width: 480px) {
  .header {
    padding: 50px 15px 40px;
  }
  
  .glitch-title {
    font-size: 2rem;
  }
  
  .subtitle {
    font-size: 0.9rem;
    letter-spacing: 2px;
  }
  
  .apps-container {
    margin: 30px auto;
    padding: 0 15px;
  }
  
  .app-card {
    padding: 20px;
  }
  
  .app-icon {
    font-size: 3rem;
    margin-bottom: 20px;
    width: 70px;
    height: 70px;
  }
  
  .app-title {
    font-size: 1.4rem;
  }
  
  .app-desc {
    font-size: 0.9rem;
  }
  
  .circle-1, .circle-2, .circle-3 {
    opacity: 0.1;
  }
}
</style>