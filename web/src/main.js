import { createApp } from 'vue'
import { createPinia } from 'pinia'
import App from './App.vue'
import Enrutador from './router'

const AplicacionVue = createApp(App)
AplicacionVue.use(createPinia())
AplicacionVue.use(Enrutador)
AplicacionVue.mount('#app')