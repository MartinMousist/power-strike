<template>
  <nav class="navbar">
    <div class="navbar-brand">
      <span class="logo">⚡ Power Strike</span>
    </div>
    <div class="navbar-links">
      <router-link to="/dashboard">Dashboard</router-link>
      <router-link to="/users">Usuarios</router-link>
      <router-link to="/activities">Actividades</router-link>
      <router-link to="/attendance">Asistencia</router-link>
    </div>
    <div class="navbar-user">
      <span class="user-name">{{ user?.name }}</span>
      <span class="user-role">{{ user?.role }}</span>
      <button @click="logout">Salir</button>
    </div>
  </nav>
</template>

<script setup>
import { computed } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '../store/auth'

const router = useRouter()
const authStore = useAuthStore()
const user = computed(() => authStore.user)

function logout() {
  authStore.logout()
  router.push('/login')
}
</script>

<style scoped>
.navbar {
  background: #1a1a2e;
  color: white;
  padding: 0 2rem;
  height: 60px;
  display: flex;
  align-items: center;
  gap: 2rem;
  box-shadow: 0 2px 10px rgba(0,0,0,0.3);
}
.logo { font-size: 1.2rem; font-weight: 700; color: #e94560; white-space: nowrap; }
.navbar-links { display: flex; gap: 0.5rem; flex: 1; }
.navbar-links a {
  color: #aaa;
  text-decoration: none;
  padding: 0.4rem 1rem;
  border-radius: 4px;
  font-size: 0.9rem;
  transition: all 0.2s;
}
.navbar-links a:hover { color: white; background: rgba(255,255,255,0.1); }
.navbar-links a.router-link-active { background: #e94560; color: white; }
.navbar-user { display: flex; align-items: center; gap: 0.8rem; }
.user-name { color: white; font-size: 0.9rem; }
.user-role {
  background: #16213e;
  color: #aaa;
  padding: 0.2rem 0.6rem;
  border-radius: 20px;
  font-size: 0.75rem;
}
.navbar-user button {
  background: transparent;
  border: 1px solid #e94560;
  color: #e94560;
  padding: 0.3rem 0.8rem;
  border-radius: 4px;
  cursor: pointer;
  font-size: 0.85rem;
  transition: all 0.2s;
}
.navbar-user button:hover { background: #e94560; color: white; }
</style>
