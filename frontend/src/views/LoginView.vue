<template>
  <div class="login-container">
    <div class="login-card">
      <div class="login-header">
        <h1>⚡ Power Strike</h1>
        <p>Sistema de Gestión de Gimnasio</p>
      </div>
      <form @submit.prevent="handleLogin">
        <div class="form-group">
          <label>Email</label>
          <input v-model="email" type="email" placeholder="admin@powerstrike.com" required />
        </div>
        <div class="form-group">
          <label>Contraseña</label>
          <input v-model="password" type="password" placeholder="••••••••" required />
        </div>
        <button type="submit" :disabled="loading">
          {{ loading ? 'Ingresando...' : 'Iniciar Sesión' }}
        </button>
        <p v-if="error" class="error">{{ error }}</p>
      </form>
      <div class="login-hint">
        <small>Demo: admin@powerstrike.com / admin123</small>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '../store/auth'
import api from '../api/axios'

const router = useRouter()
const authStore = useAuthStore()

const email = ref('')
const password = ref('')
const loading = ref(false)
const error = ref('')

async function handleLogin() {
  loading.value = true
  error.value = ''
  try {
    const response = await api.post('/auth/login', {
      email: email.value,
      password: password.value
    })
    authStore.setAuth(response.data.token, {
      email: response.data.email,
      name: response.data.name,
      role: response.data.role
    })
    router.push('/dashboard')
  } catch {
    error.value = 'Email o contraseña incorrectos'
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.login-container {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #1a1a2e 0%, #16213e 50%, #0f3460 100%);
}
.login-card {
  background: white;
  padding: 2.5rem;
  border-radius: 12px;
  width: 100%;
  max-width: 420px;
  box-shadow: 0 20px 60px rgba(0,0,0,0.4);
}
.login-header { text-align: center; margin-bottom: 2rem; }
.login-header h1 { color: #e94560; font-size: 1.8rem; margin-bottom: 0.4rem; }
.login-header p { color: #888; font-size: 0.9rem; }
.form-group { margin-bottom: 1.2rem; }
label { display: block; margin-bottom: 0.4rem; color: #333; font-weight: 500; font-size: 0.9rem; }
input {
  width: 100%;
  padding: 0.75rem 1rem;
  border: 1.5px solid #e0e0e0;
  border-radius: 6px;
  font-size: 0.95rem;
  transition: border-color 0.2s;
}
input:focus { outline: none; border-color: #e94560; }
button {
  width: 100%;
  padding: 0.85rem;
  background: #e94560;
  color: white;
  border: none;
  border-radius: 6px;
  font-size: 1rem;
  font-weight: 600;
  cursor: pointer;
  margin-top: 0.5rem;
  transition: background 0.2s;
}
button:hover:not(:disabled) { background: #c73350; }
button:disabled { background: #ccc; cursor: not-allowed; }
.error { color: #e94560; margin-top: 0.8rem; text-align: center; font-size: 0.9rem; }
.login-hint { text-align: center; margin-top: 1.5rem; color: #aaa; }
</style>
