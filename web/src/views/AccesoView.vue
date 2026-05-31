<template>
  <div class="ContenedorPrincipal">

    <div class="PanelIzquierdo">
      <div class="ContenidoDecorativo">
        <div class="CirculoGrande"></div>
        <div class="CirculoMediano"></div>
        <div class="TextoDecorativo">
          <span class="LetraGrande">L</span>
          <span class="LetraGrande">I</span>
          <span class="LetraGrande">B</span>
          <span class="LetraGrande">R</span>
          <span class="LetraGrande">O</span>
          <span class="LetraGrande">S</span>
        </div>
        <div class="FraseInspiradora">
          "Un libro abierto es un cerebro que habla"
        </div>
        <div class="LineaDecorativa"></div>
        <p class="DescripcionLibreria">
          Librería & Papelería Cáceres<br>
          <span class="SubtituloLibreria">Sistema de Gestión Interno</span>
        </p>
      </div>
    </div>

    <div class="PanelDerecho">
      <div class="TarjetaAcceso">

        <div class="CabeceraFormulario">
          <div class="IconoLibro">📚</div>
          <h1 class="TituloAcceso">Bienvenido</h1>
          <p class="SubtituloAcceso">Ingresa tus credenciales para continuar</p>
        </div>

        <form class="FormularioAcceso" @submit.prevent="ManejarInicioSesion">

          <div class="GrupoEntrada">
            <label class="EtiquetaCampo">Usuario</label>
            <div class="ContenedorEntrada">
              <span class="IconoCampo">
                <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                  <path d="M20 21v-2a4 4 0 0 0-4-4H8a4 4 0 0 0-4 4v2"/>
                  <circle cx="12" cy="7" r="4"/>
                </svg>
              </span>
              <input
                v-model="NombreUsuario"
                type="text"
                class="CampoEntrada"
                placeholder="Tu nombre de usuario"
                autocomplete="username"
              />
            </div>
          </div>

          <div class="GrupoEntrada">
            <label class="EtiquetaCampo">Contraseña</label>
            <div class="ContenedorEntrada">
              <span class="IconoCampo">
                <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                  <rect x="3" y="11" width="18" height="11" rx="2" ry="2"/>
                  <path d="M7 11V7a5 5 0 0 1 10 0v4"/>
                </svg>
              </span>
              <input
                v-model="Contrasena"
                :type="MostrarContrasena ? 'text' : 'password'"
                class="CampoEntrada"
                placeholder="Tu contraseña"
                autocomplete="current-password"
              />
              <button
                type="button"
                class="BotonVerContrasena"
                @click="MostrarContrasena = !MostrarContrasena"
              >
                <svg v-if="!MostrarContrasena" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                  <path d="M1 12s4-8 11-8 11 8 11 8-4 8-11 8-11-8-11-8z"/>
                  <circle cx="12" cy="12" r="3"/>
                </svg>
                <svg v-else width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                  <path d="M17.94 17.94A10.07 10.07 0 0 1 12 20c-7 0-11-8-11-8a18.45 18.45 0 0 1 5.06-5.94M9.9 4.24A9.12 9.12 0 0 1 12 4c7 0 11 8 11 8a18.5 18.5 0 0 1-2.16 3.19m-6.72-1.07a3 3 0 1 1-4.24-4.24"/>
                  <line x1="1" y1="1" x2="23" y2="23"/>
                </svg>
              </button>
            </div>
          </div>

          <div v-if="MensajeError" class="AlertaError">
            <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
              <circle cx="12" cy="12" r="10"/>
              <line x1="12" y1="8" x2="12" y2="12"/>
              <line x1="12" y1="16" x2="12.01" y2="16"/>
            </svg>
            {{ MensajeError }}
          </div>

          <button
            type="submit"
            class="BotonIngresar"
            :class="{ 'BotonCargando': EstaCargando }"
            :disabled="EstaCargando"
          >
            <span v-if="!EstaCargando">Ingresar al Sistema</span>
            <span v-else class="TextoCargando">
              <span class="PuntoCarga"></span>
              <span class="PuntoCarga"></span>
              <span class="PuntoCarga"></span>
            </span>
          </button>

          <div class="ContenedorOlvideContrasena">
            <RouterLink to="/recuperar-contrasena" class="EnlaceOlvideContrasena">
              ¿Olvidaste tu contraseña?
            </RouterLink>
          </div>

        </form>

        <div class="PieTarjeta">
          <span>© 2025 Librería Cáceres</span>
        </div>

      </div>
    </div>

  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { autenticacionApi } from '@/api/autenticacion.api.js'
import { useSesionStore } from '@/stores/sesionStore.js'

const Enrutador = useRouter()
const AlmacenSesion = useSesionStore()

const NombreUsuario = ref('')
const Contrasena = ref('')
const MostrarContrasena = ref(false)
const EstaCargando = ref(false)
const MensajeError = ref('')

const ManejarInicioSesion = async () => {
  if (!NombreUsuario.value || !Contrasena.value) {
    MensajeError.value = 'Por favor completa todos los campos'
    return
  }
  EstaCargando.value = true
  MensajeError.value = ''
  try {
    const RespuestaServidor = await autenticacionApi.iniciarSesion({
      nombreUsuario: NombreUsuario.value,
      contrasena: Contrasena.value
    })
    AlmacenSesion.IniciarSesion(RespuestaServidor)
    Enrutador.push({ name: 'inicio' })
  } catch {
    MensajeError.value = 'Usuario o contraseña incorrectos'
  } finally {
    EstaCargando.value = false
  }
}
</script>
<style>
@import url('https://fonts.googleapis.com/css2?family=Crimson+Pro:ital,wght@0,300;0,400;0,600;1,300;1,400&family=Libre+Baskerville:ital,wght@0,400;0,700;1,400&display=swap');
@import '@/style/globals.css';
</style>
