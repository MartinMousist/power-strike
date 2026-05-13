<template>
  <div class="container">
    <div class="page-header">
      <div>
        <h1>Dashboard</h1>
        <p class="subtitle">Bienvenido, <strong>{{ user?.name }}</strong></p>
      </div>
    </div>

    <div class="stats-grid">
      <div class="stat-card">
        <div class="stat-icon">👥</div>
        <div class="stat-info">
          <h3>Total Usuarios</h3>
          <span class="stat-number">{{ stats.users }}</span>
        </div>
      </div>
      <div class="stat-card">
        <div class="stat-icon">🏋️</div>
        <div class="stat-info">
          <h3>Actividades</h3>
          <span class="stat-number">{{ stats.activities }}</span>
        </div>
      </div>
      <div class="stat-card">
        <div class="stat-icon">📋</div>
        <div class="stat-info">
          <h3>Asistencias Hoy</h3>
          <span class="stat-number">{{ stats.todayAttendances }}</span>
        </div>
      </div>
      <div class="stat-card">
        <div class="stat-icon">📅</div>
        <div class="stat-info">
          <h3>Total Asistencias</h3>
          <span class="stat-number">{{ stats.totalAttendances }}</span>
        </div>
      </div>
    </div>

    <div class="recent-section">
      <h2>Últimas Asistencias</h2>
      <div class="table-container">
        <table>
          <thead>
            <tr>
              <th>Usuario</th>
              <th>Fecha y Hora</th>
              <th>Notas</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="att in recentAttendances" :key="att.id">
              <td>{{ att.user?.name }}</td>
              <td>{{ formatDate(att.attendanceDate) }}</td>
              <td>{{ att.notes || '-' }}</td>
            </tr>
            <tr v-if="recentAttendances.length === 0">
              <td colspan="3" class="empty">Sin asistencias registradas</td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useAuthStore } from '../store/auth'
import api from '../api/axios'

const authStore = useAuthStore()
const user = computed(() => authStore.user)

const stats = ref({ users: 0, activities: 0, todayAttendances: 0, totalAttendances: 0 })
const recentAttendances = ref([])

onMounted(async () => {
  try {
    const [usersRes, activitiesRes, attendancesRes] = await Promise.all([
      api.get('/users'),
      api.get('/activities'),
      api.get('/attendances')
    ])
    const today = new Date().toDateString()
    stats.value.users = usersRes.data.length
    stats.value.activities = activitiesRes.data.length
    stats.value.totalAttendances = attendancesRes.data.length
    stats.value.todayAttendances = attendancesRes.data.filter(a =>
      new Date(a.attendanceDate).toDateString() === today
    ).length
    recentAttendances.value = attendancesRes.data.slice(0, 5)
  } catch (e) {
    console.error(e)
  }
})

function formatDate(dateStr) {
  return new Date(dateStr).toLocaleString('es-AR')
}
</script>

<style scoped>
.container { padding: 2rem; max-width: 1200px; margin: 0 auto; }
.page-header { margin-bottom: 2rem; }
h1 { color: #1a1a2e; font-size: 1.8rem; }
.subtitle { color: #666; margin-top: 0.3rem; }
.stats-grid { display: grid; grid-template-columns: repeat(4, 1fr); gap: 1.5rem; margin-bottom: 2.5rem; }
.stat-card {
  background: white;
  padding: 1.5rem;
  border-radius: 10px;
  box-shadow: 0 2px 10px rgba(0,0,0,0.08);
  display: flex;
  align-items: center;
  gap: 1rem;
  border-left: 4px solid #e94560;
}
.stat-icon { font-size: 2rem; }
.stat-info h3 { color: #888; font-size: 0.8rem; text-transform: uppercase; letter-spacing: 0.5px; margin-bottom: 0.3rem; }
.stat-number { font-size: 2rem; font-weight: 700; color: #1a1a2e; }
.recent-section h2 { color: #1a1a2e; margin-bottom: 1rem; font-size: 1.2rem; }
.table-container { background: white; border-radius: 10px; box-shadow: 0 2px 10px rgba(0,0,0,0.08); overflow: hidden; }
table { width: 100%; border-collapse: collapse; }
th { background: #1a1a2e; color: white; padding: 0.9rem 1rem; text-align: left; font-size: 0.85rem; }
td { padding: 0.9rem 1rem; border-bottom: 1px solid #f0f0f0; font-size: 0.9rem; }
.empty { text-align: center; color: #aaa; font-style: italic; }
@media (max-width: 768px) { .stats-grid { grid-template-columns: repeat(2, 1fr); } }
</style>
