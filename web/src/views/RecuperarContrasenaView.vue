<template>
  <div class="ContenedorRecuperacion">
    <div class="TarjetaRecuperacion">

      <RouterLink to="/acceso" class="BotonRegresar">
        <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
          <path d="M19 12H5M12 5l-7 7 7 7"/>
        </svg>
        Regresar
      </RouterLink>

      <div v-if="!CorreoEnviado">
        <div class="CabeceraSeccion">
          <div class="IconoSeccion">🔑</div>
          <h1 class="TituloSeccion">Recuperar Contraseña</h1>
          <p class="DescripcionSeccion">
            Ingresa el correo electronico asociado a tu cuenta y te enviaremos un enlace para restablecer tu contrasena.
          </p>
        </div>

        <form @submit.prevent="ManejarRecuperacion">
          <div class="GrupoEntrada">
            <label class="EtiquetaCampo">Correo Electronico</label>
            <div class="ContenedorEntrada">
              <span class="IconoCampo">
                <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                  <path d="M4 4h16c1.1 0 2 .9 2 2v12c0 1.1-.9 2-2 2H4c-1.1 0-2-.9-2-2V6c0-1.1.9-2 2-2z"/>
                  <polyline points="22,6 12,13 2,6"/>
                </svg>
              </span>
              <input
                v-model="CorreoElectronico"
                type="email"
                class="CampoEntrada"
                placeholder="correo@ejemplo.com"
              />
            </div>
          </div>

          <div v-if="MensajeError" class="AlertaError">{{ MensajeError }}</div>

          <button type="submit" class="BotonAccion" :disabled="EstaCargando">
            <span v-if="!EstaCargando">Enviar enlace de recuperacion</span>
            <span v-else>Enviando...</span>
          </button>
        </form>
      </div>

      <div v-else class="MensajeExito">
        <div class="IconoExito">✉️</div>
        <h2 class="TituloExito">Correo enviado</h2>
        <p class="DescripcionExito">
          Hemos enviado un enlace de recuperacion a <strong>{{ CorreoElectronico }}</strong>.
          Revisa tu bandeja de entrada y sigue las instrucciones.
        </p>
        <p class="AdvertenciaExito">El enlace expira en 1 hora.</p>
        <RouterLink to="/acceso" class="EnlaceVolver">Volver al inicio de sesion</RouterLink>
      </div>

    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { autenticacionApi } from '@/api/autenticacion.api.js'

const CorreoElectronico = ref('')
const EstaCargando = ref(false)
const MensajeError = ref('')
const CorreoEnviado = ref(false)

const ManejarRecuperacion = async () => {
  if (!CorreoElectronico.value) {
    MensajeError.value = 'Por favor ingresa tu correo electronico'
    return
  }
  EstaCargando.value = true
  MensajeError.value = ''
  try {
    await autenticacionApi.recuperarContrasena({ correoElectronico: CorreoElectronico.value })
    CorreoEnviado.value = true
  } catch {
    MensajeError.value = 'No encontramos una cuenta con ese correo electronico'
  } finally {
    EstaCargando.value = false
  }
}
</script>
<style>
@import url('https://fonts.googleapis.com/css2?family=Crimson+Pro:ital,wght@0,300;0,400;0,600;1,300;1,400&family=Libre+Baskerville:ital,wght@0,400;0,700;1,400&display=swap');
@import '@/style/globals.css';
</style>
