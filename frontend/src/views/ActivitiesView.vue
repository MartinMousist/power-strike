<template>
  <div class="container">
    <div class="page-header">
      <h1>Gestión de Actividades</h1>
      <button class="btn-primary" @click="openCreate">+ Nueva Actividad</button>
    </div>

    <div class="activities-grid">
      <div v-for="activity in activities" :key="activity.id" class="activity-card">
        <div class="card-header">
          <h3>{{ activity.name }}</h3>
          <span :class="activity.active ? 'badge-active' : 'badge-inactive'">
            {{ activity.active ? 'Activa' : 'Inactiva' }}
          </span>
        </div>
        <div class="card-body">
          <div class="info-item">📅 <span>{{ activity.day }}</span></div>
          <div class="info-item">🕐 <span>{{ activity.schedule }}</span></div>
          <div class="info-item">💰 <span>${{ activity.monthlyCost?.toLocaleString('es-AR') }}/mes</span></div>
        </div>
        <div class="card-actions">
          <button class="btn-edit" @click="openEdit(activity)">Editar</button>
          <button class="btn-delete" @click="confirmDelete(activity.id)">Eliminar</button>
        </div>
      </div>
      <div v-if="activities.length === 0" class="empty-state">
        <p>No hay actividades registradas</p>
      </div>
    </div>

    <div v-if="showModal" class="modal-overlay" @click.self="closeModal">
      <div class="modal">
        <h2>{{ editingId ? 'Editar Actividad' : 'Nueva Actividad' }}</h2>
        <form @submit.prevent="saveActivity">
          <div class="form-group">
            <label>Nombre de la actividad</label>
            <input v-model="form.name" placeholder="Ej: Boxeo, Spinning, Yoga..." required />
          </div>
          <div class="form-row">
            <div class="form-group">
              <label>Día</label>
              <select v-model="form.day" required>
                <option value="">Seleccionar día</option>
                <option v-for="d in dias" :key="d">{{ d }}</option>
              </select>
            </div>
            <div class="form-group">
              <label>Horario</label>
              <input v-model="form.schedule" placeholder="Ej: 09:00 - 10:00" required />
            </div>
          </div>
          <div class="form-group">
            <label>Cuota Mensual ($)</label>
            <input v-model.number="form.monthlyCost" type="number" step="0.01" min="0" placeholder="0.00" required />
          </div>
          <p v-if="formError" class="form-error">{{ formError }}</p>
          <div class="modal-actions">
            <button type="button" class="btn-cancel" @click="closeModal">Cancelar</button>
            <button type="submit" class="btn-primary">Guardar</button>
          </div>
        </form>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import api from '../api/axios'

const activities = ref([])
const showModal = ref(false)
const editingId = ref(null)
const formError = ref('')
const dias = ['Lunes', 'Martes', 'Miércoles', 'Jueves', 'Viernes', 'Sábado', 'Domingo']
const form = ref({ name: '', day: '', schedule: '', monthlyCost: null })

onMounted(fetchActivities)

async function fetchActivities() {
  const res = await api.get('/activities')
  activities.value = res.data
}

function openCreate() {
  editingId.value = null
  form.value = { name: '', day: '', schedule: '', monthlyCost: null }
  formError.value = ''
  showModal.value = true
}

function openEdit(activity) {
  editingId.value = activity.id
  form.value = {
    name: activity.name,
    day: activity.day,
    schedule: activity.schedule,
    monthlyCost: activity.monthlyCost
  }
  formError.value = ''
  showModal.value = true
}

function closeModal() {
  showModal.value = false
  editingId.value = null
}

async function saveActivity() {
  formError.value = ''
  try {
    if (editingId.value) {
      await api.put(`/activities/${editingId.value}`, form.value)
    } else {
      await api.post('/activities', form.value)
    }
    closeModal()
    fetchActivities()
  } catch {
    formError.value = 'Error al guardar la actividad.'
  }
}

async function confirmDelete(id) {
  if (confirm('¿Eliminar esta actividad?')) {
    await api.delete(`/activities/${id}`)
    fetchActivities()
  }
}
</script>

<style scoped>
.container { padding: 2rem; max-width: 1200px; margin: 0 auto; }
.page-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 2rem; }
h1 { color: #1a1a2e; font-size: 1.8rem; }
.btn-primary { background: #e94560; color: white; border: none; padding: 0.6rem 1.4rem; border-radius: 6px; cursor: pointer; font-weight: 600; transition: background 0.2s; }
.btn-primary:hover { background: #c73350; }
.activities-grid { display: grid; grid-template-columns: repeat(auto-fill, minmax(280px, 1fr)); gap: 1.5rem; }
.activity-card { background: white; border-radius: 10px; box-shadow: 0 2px 10px rgba(0,0,0,0.08); overflow: hidden; }
.card-header { background: #1a1a2e; padding: 1.2rem; display: flex; justify-content: space-between; align-items: center; }
.card-header h3 { color: white; font-size: 1.1rem; margin: 0; }
.badge-active { background: #00aa44; color: white; padding: 0.2rem 0.6rem; border-radius: 20px; font-size: 0.75rem; }
.badge-inactive { background: #888; color: white; padding: 0.2rem 0.6rem; border-radius: 20px; font-size: 0.75rem; }
.card-body { padding: 1.2rem; }
.info-item { display: flex; align-items: center; gap: 0.5rem; padding: 0.4rem 0; color: #555; font-size: 0.9rem; border-bottom: 1px solid #f5f5f5; }
.info-item span { font-weight: 500; }
.card-actions { padding: 1rem 1.2rem; display: flex; gap: 0.5rem; border-top: 1px solid #f0f0f0; }
.btn-edit { flex: 1; background: #0070cc; color: white; border: none; padding: 0.5rem; border-radius: 5px; cursor: pointer; font-size: 0.85rem; }
.btn-delete { flex: 1; background: #e94560; color: white; border: none; padding: 0.5rem; border-radius: 5px; cursor: pointer; font-size: 0.85rem; }
.empty-state { grid-column: 1 / -1; text-align: center; color: #aaa; padding: 3rem; font-style: italic; }
.modal-overlay { position: fixed; top: 0; left: 0; right: 0; bottom: 0; background: rgba(0,0,0,0.5); display: flex; align-items: center; justify-content: center; z-index: 1000; }
.modal { background: white; padding: 2rem; border-radius: 10px; width: 100%; max-width: 460px; box-shadow: 0 10px 40px rgba(0,0,0,0.2); }
.modal h2 { color: #1a1a2e; margin-bottom: 1.5rem; }
.form-row { display: grid; grid-template-columns: 1fr 1fr; gap: 1rem; }
.form-group { margin-bottom: 1rem; }
.form-group label { display: block; margin-bottom: 0.4rem; color: #333; font-weight: 500; font-size: 0.88rem; }
.form-group input, .form-group select { width: 100%; padding: 0.65rem; border: 1.5px solid #e0e0e0; border-radius: 6px; font-size: 0.9rem; }
.form-group input:focus, .form-group select:focus { outline: none; border-color: #e94560; }
.form-error { color: #e94560; font-size: 0.88rem; margin-bottom: 1rem; }
.modal-actions { display: flex; gap: 1rem; justify-content: flex-end; margin-top: 1.5rem; }
.btn-cancel { background: white; border: 1.5px solid #ddd; color: #666; padding: 0.6rem 1.2rem; border-radius: 6px; cursor: pointer; }
</style>
