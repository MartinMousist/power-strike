<template>
  <div class="container">
    <div class="page-header">
      <h1>Historial de Asistencia</h1>
      <button class="btn-primary" @click="showModal = true">+ Registrar Asistencia</button>
    </div>

    <div class="filter-bar">
      <select v-model="filterUserId" @change="applyFilter">
        <option value="">Todos los usuarios</option>
        <option v-for="u in users" :key="u.id" :value="u.id">{{ u.name }}</option>
      </select>
    </div>

    <div class="table-container">
      <table>
        <thead>
          <tr>
            <th>#</th>
            <th>Usuario</th>
            <th>DNI</th>
            <th>Fecha y Hora</th>
            <th>Notas</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="att in filteredAttendances" :key="att.id">
            <td>{{ att.id }}</td>
            <td>{{ att.user?.name }}</td>
            <td>{{ att.user?.dni }}</td>
            <td>{{ formatDate(att.attendanceDate) }}</td>
            <td>{{ att.notes || '-' }}</td>
          </tr>
          <tr v-if="filteredAttendances.length === 0">
            <td colspan="5" class="empty">No hay asistencias registradas</td>
          </tr>
        </tbody>
      </table>
    </div>

    <div v-if="showModal" class="modal-overlay" @click.self="closeModal">
      <div class="modal">
        <h2>Registrar Asistencia</h2>
        <form @submit.prevent="saveAttendance">
          <div class="form-group">
            <label>Usuario</label>
            <select v-model="form.userId" required>
              <option value="">Seleccionar usuario</option>
              <option v-for="u in users" :key="u.id" :value="u.id">
                {{ u.name }} — DNI: {{ u.dni }}
              </option>
            </select>
          </div>
          <div class="form-group">
            <label>Notas (opcional)</label>
            <input v-model="form.notes" placeholder="Observaciones..." />
          </div>
          <div class="date-display">
            <span>📅 Fecha y hora: <strong>{{ currentDateTime }}</strong></span>
          </div>
          <p v-if="formError" class="form-error">{{ formError }}</p>
          <div class="modal-actions">
            <button type="button" class="btn-cancel" @click="closeModal">Cancelar</button>
            <button type="submit" class="btn-primary">Registrar</button>
          </div>
        </form>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import api from '../api/axios'

const attendances = ref([])
const users = ref([])
const showModal = ref(false)
const filterUserId = ref('')
const formError = ref('')
const form = ref({ userId: '', notes: '' })

const currentDateTime = computed(() =>
  new Date().toLocaleString('es-AR')
)

const filteredAttendances = computed(() => {
  if (!filterUserId.value) return attendances.value
  return attendances.value.filter(a => a.user?.id === Number(filterUserId.value))
})

// REQ-F05 — Registrar y mostrar historial de asistencia de usuarios al gimnasio
onMounted(async () => {
  const [attRes, usersRes] = await Promise.all([
    api.get('/attendances'),
    api.get('/users')
  ])
  attendances.value = attRes.data
  users.value = usersRes.data
})

function closeModal() {
  showModal.value = false
  form.value = { userId: '', notes: '' }
  formError.value = ''
}

// REQ-F05 — Registro de asistencia de un usuario
async function saveAttendance() {
  formError.value = ''
  if (!form.value.userId) {
    formError.value = 'Seleccioná un usuario'
    return
  }
  try {
    await api.post('/attendances', {
      userId: form.value.userId,
      notes: form.value.notes
    })
    closeModal()
    const res = await api.get('/attendances')
    attendances.value = res.data
  } catch {
    formError.value = 'Error al registrar la asistencia.'
  }
}

function formatDate(dateStr) {
  return new Date(dateStr).toLocaleString('es-AR', {
    dateStyle: 'medium',
    timeStyle: 'short'
  })
}
</script>

<style scoped>
.container { padding: 2rem; max-width: 1200px; margin: 0 auto; }
.page-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 1.5rem; }
h1 { color: #1a1a2e; font-size: 1.8rem; }
.btn-primary { background: #e94560; color: white; border: none; padding: 0.6rem 1.4rem; border-radius: 6px; cursor: pointer; font-weight: 600; transition: background 0.2s; }
.btn-primary:hover { background: #c73350; }
.filter-bar { margin-bottom: 1.5rem; }
.filter-bar select { padding: 0.6rem 1rem; border: 1.5px solid #e0e0e0; border-radius: 6px; font-size: 0.9rem; min-width: 220px; }
.table-container { background: white; border-radius: 10px; box-shadow: 0 2px 10px rgba(0,0,0,0.08); overflow: hidden; }
table { width: 100%; border-collapse: collapse; }
th { background: #1a1a2e; color: white; padding: 0.9rem 1rem; text-align: left; font-size: 0.85rem; }
td { padding: 0.9rem 1rem; border-bottom: 1px solid #f0f0f0; font-size: 0.9rem; }
.empty { text-align: center; color: #aaa; font-style: italic; padding: 2rem; }
.modal-overlay { position: fixed; top: 0; left: 0; right: 0; bottom: 0; background: rgba(0,0,0,0.5); display: flex; align-items: center; justify-content: center; z-index: 1000; }
.modal { background: white; padding: 2rem; border-radius: 10px; width: 100%; max-width: 440px; box-shadow: 0 10px 40px rgba(0,0,0,0.2); }
.modal h2 { color: #1a1a2e; margin-bottom: 1.5rem; }
.form-group { margin-bottom: 1rem; }
.form-group label { display: block; margin-bottom: 0.4rem; color: #333; font-weight: 500; font-size: 0.88rem; }
.form-group input, .form-group select { width: 100%; padding: 0.65rem; border: 1.5px solid #e0e0e0; border-radius: 6px; font-size: 0.9rem; }
.form-group input:focus, .form-group select:focus { outline: none; border-color: #e94560; }
.date-display { background: #f8f8f8; padding: 0.8rem 1rem; border-radius: 6px; font-size: 0.88rem; color: #555; margin-bottom: 1rem; }
.form-error { color: #e94560; font-size: 0.88rem; margin-bottom: 1rem; }
.modal-actions { display: flex; gap: 1rem; justify-content: flex-end; margin-top: 1.5rem; }
.btn-cancel { background: white; border: 1.5px solid #ddd; color: #666; padding: 0.6rem 1.2rem; border-radius: 6px; cursor: pointer; }
</style>
