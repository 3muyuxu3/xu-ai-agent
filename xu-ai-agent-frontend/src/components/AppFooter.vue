<template>
  <footer class="app-footer">
    <div class="footer-content">
      <div class="footer-section">
        <div class="footer-logo">
          <h3>木羽AI超级智能体应用平台</h3>
        </div>
        <div class="footer-links">
          <a href="#">《用户协议》</a>
          <a href="#">《隐私政策》</a>
        </div>
      </div>
      
      <div class="footer-section">
        <h4>友情链接</h4>
        <div class="footer-links">
          <a href="https://github.com/3muyuxu3" target="_blank">个人介绍</a>
          <a href="https://github.com/3muyuxu3" target="_blank">个人主页</a>
          <a href="#" target="_blank">友情赞助</a>
        </div>
      </div>
      
      <div class="footer-section">
        <h4>联系我们</h4>
        <div class="footer-links">
          <a href="#">邮箱:2274623566@qq.com</a>
          <a href="#">站长：栩</a>
        </div>
      </div>
    </div>
    
        <div class="footer-bottom">
      <p>© {{ currentYear }} 木羽AI超级智能体应用平台 - 让AI为你服务</p>
    </div>
  </footer>
</template>

<script setup>
import { computed } from 'vue'

// 计算当前年份
const currentYear = computed(() => new Date().getFullYear())
</script>

<style scoped>
.app-footer {
  background-color: rgba(255, 255, 255, 0.85);
  backdrop-filter: blur(10px);
  padding: 40px 0 20px;
  color: #666;
  border-top: 1px solid rgba(255, 182, 193, 0.3);
  width: 100%;
  margin-top: auto;
  position: relative;
  z-index: 2;
  box-shadow: 0 -2px 10px rgba(0, 0, 0, 0.05);
}

.footer-content {
  max-width: 1200px;
  margin: 0 auto;
  display: flex;
  flex-wrap: wrap;
  justify-content: space-between;
  padding: 0 20px;
}

.footer-section {
  flex: 1;
  min-width: 200px;
  margin-bottom: 30px;
  padding: 0 15px;
}

.footer-logo h3 {
  font-size: 1.25rem;
  margin-bottom: 15px;
  color: #ff69b4;
  position: relative;
  display: inline-block;
}

.footer-logo h3:after {
  content: '';
  position: absolute;
  bottom: -5px;
  left: 0;
  width: 50px;
  height: 2px;
  background: linear-gradient(90deg, #ff69b4, transparent);
}

.footer-section h4 {
  font-size: 1rem;
  margin-bottom: 15px;
  color: #ff69b4;
  position: relative;
  display: inline-block;
}

.footer-section h4:after {
  content: '';
  position: absolute;
  bottom: -5px;
  left: 0;
  width: 30px;
  height: 2px;
  background: linear-gradient(90deg, #ff69b4, transparent);
}

.footer-links {
  display: flex;
  flex-direction: column;
}

.footer-links a {
  margin-bottom: 10px;
  color: #666;
  text-decoration: none;
  transition: all 0.3s ease;
  position: relative;
  padding-left: 12px;
}

.footer-links a:before {
  content: '•';
  position: absolute;
  left: 0;
  color: #ffb6c1;
  opacity: 0.7;
}

.footer-links a:hover {
  color: #ff69b4;
  padding-left: 15px;
}

.footer-links a:hover:before {
  color: #ff69b4;
  opacity: 1;
}

.qrcode {
  display: flex;
  align-items: center;
}

.qrcode-container {
  text-align: center;
}

.qrcode-placeholder {
  width: 90px;
  height: 90px;
  background-color: #f5f5f5;
  margin: 0 auto 10px;
  border-radius: 4px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 2rem;
}

.qrcode-placeholder:after {
  content: '🤖';
}

.footer-bottom {
  text-align: center;
  padding-top: 20px;
  margin-top: 20px;
  border-top: 1px solid rgba(255, 182, 193, 0.2);
  color: #999;
  font-size: 0.9rem;
  position: relative;
}

.footer-bottom:before {
  content: '';
  position: absolute;
  top: -10px;
  left: 50%;
  transform: translateX(-50%);
  width: 50px;
  height: 2px;
  background: linear-gradient(90deg, transparent, #ffb6c1, transparent);
}

.footer-bottom p {
  margin: 0;
  padding: 0;
  font-size: 0.85rem;
  color: #888;
  letter-spacing: 0.5px;
}

.footer-heart {
  position: absolute;
  top: -25px;
  left: 50%;
  transform: translateX(-50%);
  font-size: 1.2rem;
  color: #ff69b4;
  animation: heartbeat 1.5s ease-in-out infinite;
  text-shadow: 0 0 8px rgba(255, 105, 180, 0.5);
}

@keyframes heartbeat {
  0% {
    transform: translateX(-50%) scale(1);
  }
  50% {
    transform: translateX(-50%) scale(1.2);
  }
  100% {
    transform: translateX(-50%) scale(1);
  }
}

/* 响应式设计 */
@media (max-width: 768px) {
  .footer-content {
    flex-direction: column;
  }
  
  .footer-section {
    width: 100%;
    margin-bottom: 20px;
    padding: 0;
  }
}

@media (max-width: 480px) {
  .app-footer {
    padding: 30px 0 15px;
  }
  
  .footer-section h4 {
    font-size: 0.95rem;
  }
  
  .footer-links a {
    font-size: 0.9rem;
  }
  
  .qrcode-placeholder {
    width: 80px;
    height: 80px;
  }
}
</style> 