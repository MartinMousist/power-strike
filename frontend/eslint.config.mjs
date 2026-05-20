import pluginVue from 'eslint-plugin-vue'

export default [
  ...pluginVue.configs['flat/recommended'],
  {
    rules: {
      // Navbar.vue y App.vue son nombres de un solo segmento; se permiten como
      // excepción porque son componentes estructurales del proyecto, no de dominio.
      'vue/multi-word-component-names': ['warn', {
        ignores: ['App', 'Navbar']
      }]
    }
  }
]
