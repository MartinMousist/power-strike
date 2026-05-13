<template>
  <div class="container">
    <div class="page-header">
      <h1>Gestión de Usuarios</h1>
      <button class="btn-primary" @click="openCreate">+ Nuevo Usuario</button>
    </div>

    <div class="table-container">
      <table>
        <thead>
          <tr>
            <th>#</th>
            <th>Nombre</th>
            <th>Email</th>
            <th>DNI</th>
            <th>Rol</th>
            <th>Estado</th>
            <th>Acciones</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="user in users" :key="user.id">
            <td>{{ user.id }}</td>
            <td>{{ user.name }}</td>
            <td>{{ user.email }}</td>
            <td>{{ user.dni }}</td>
            <td><span :class="'badge badge-' + user.role.toLowerCase()">{{ roleLabel(user.role) }}</span></td>
            <td><span :class="user.active ? 'active' : 'inactive'">{{ user.active ? 'Activo' : 'Inactivo' }}</span></td>
            <td class="actions">
              <button class="btn-edit" @click="openEdit(user)">Editar</button>
              <button class="btn-delete" @click="confirmDelete(user.id)">Eliminar</button>
            </td>
          </tr>
          <tr v-if="users.length === 0">
            <td colspan="7" class="empty">No hay usuarios registrados</td>
          </tr>
        </tbody>
      </table>
    </div>

    <div v-if="showModal" class="modal-overlay" @click.self="closeModal">
      <div class="modal">
        <h2>{{ editingId ? 'Editar Usuario' : 'Nuevo Usuario' }}</h2>
        <form @submit.prevent="saveUser">
          <div class="form-row">
            <div class="form-group">
              <label>Nombre completo</label>
              <input v-model="form.name" placeholder="Ej: Juan Pérez" required />
            </div>
            <div class="form-group">
              <label>DNI</label>
              <input v-model="form.dni" placeholder="Ej: 12345678" required />
            </div>
          </div>
          <div class="form-group">
            <label>Email</label>
            <input v-model="form.email" type="email" placeholder="juan@email.com" required />
          </div>
          <div class="form-group">
            <label>Contraseña {{ editingId ? '(dejar vacío para no cambiar)' : '' }}</label>
            <input v-model="form.password" type="password" :required="!editingId" placeholder="••••••••" />
          </div>
          <div class="form-group">
            <label>Rol</label>
            <select v-model="form.role" required>
              <option value="ADMIN">Administrador</option>
              <option value="TRAINER">Entrenador</option>
              <option value="CLIENT">Cliente</option>
            </select>
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

const users = ref([])
const showModal = ref(false)
const editingId = ref(null)
const formError = ref('')
const form = ref({ name: '', email: '', dni: '', password: '', role: 'CLIENT' })

onMounted(fetchUsers)

async function fetchUsers() {
  const res = await api.get('/users')
  users.value = res.data
}

function openCreate() {
  editingId.value = null
  form.value = { name: '', email: '', dni: '', password: '', role: 'CLIENT' }
  formError.value = ''
  showModal.value = true
}

function openEdit(user) {
  editingId.value = user.id
  form.value = { name: user.name, email: user.email, dni: user.dni, password: '', role: user.role }
  formError.value = ''
  showModal.value = true
}

function closeModal() {
  showModal.value = false
  editingId.value = null
}

async function saveUser() {
  formError.value = ''
  try {
    if (editingId.value) {
      await api.put(`/users/${editingId.value}`, form.value)
    } else {
      await api.post('/users', form.value)
    }
    closeModal()
    fetchUsers()
  } catch {
    formError.value = 'Error al guardar. Verificá los datos.'
  }
}

async function confirmDelete(id) {
  if (confirm('¿Eliminar este usuario? Esta acción no se puede deshacer.')) {
    await api.delete(`/users/${id}`)
    fetchUsers()
  }
}

function roleLabel(role) {
  const labels = { ADMIN: 'Admin', TRAINER: 'Entrenador', CLIENT: 'Cliente' }
  return labels[role] || role
}
</script>

<style scoped>
.container { padding: 2rem; max-width: 1200px; margin: 0 auto; }
.page-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 2rem; }
h1 { color: #1a1a2e; font-size: 1.8rem; }
.btn-primary { background: #e94560; color: white; border: none; padding: 0.6rem 1.4rem; border-radius: 6px; cursor: pointer; font-weight: 600; transition: background 0.2s; }
.btn-primary:hover { background: #c73350; }
.table-container { background: white; border-radius: 10px; box-shadow: 0 2px 10px rgba(0,0,0,0.08); overflow: hidden; }
table { width: 100%; border-collapse: collapse; }
th { background: #1a1a2e; color: white; padding: 0.9rem 1rem; text-align: left; font-size: 0.85rem; }
td { padding: 0.85rem 1rem; border-bottom: 1px solid #f0f0f0; font-size: 0.9rem; }
.badge { padding: 0.25rem 0.7rem; border-radius: 20px; font-size: 0.78rem; font-weight: 600; }
.badge-admin { background: #ffe0e6; color: #e94560; }
.badge-trainer { background: #e0f0ff; color: #0070cc; }
.badge-client { background: #e0ffe0; color: #00aa44; }
.active { color: #00aa44; font-weight: 600; }
.inactive { color: #aaa; }
.actions { display: flex; gap: 0.5rem; }
.btn-edit { background: #0070cc; color: white; border: none; padding: 0.3rem 0.7rem; border-radius: 4px; cursor: pointer; font-size: 0.82rem; }
.btn-delete { background: #e94560; color: white; border: none; padding: 0.3rem 0.7rem; border-radius: 4px; cursor: pointer; font-size: 0.82rem; }
.empty { text-align: center; color: #aaa; font-style: italic; padding: 2rem; }
.modal-overlay { position: fixed; top: 0; left: 0; right: 0; bottom: 0; background: rgba(0,0,0,0.5); display: flex; align-items: center; justify-content: center; z-index: 1000; }
.modal { background: white; padding: 2rem; border-radius: 10px; width: 100%; max-width: 500px; box-shadow: 0 10px 40px rgba(0,0,0,0.2); }
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
