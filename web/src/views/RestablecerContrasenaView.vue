<template>
  <div class="ContenedorRestablecimiento">
    <div class="TarjetaRestablecimiento">

      <div v-if="VerificandoToken" class="EstadoCarga">
        <div class="AnimacionCarga"></div>
        <p>Verificando enlace...</p>
      </div>

      <div v-else-if="TokenInvalido" class="EstadoError">
        <div class="IconoEstado">⚠️</div>
        <h2 class="TituloEstado">Enlace invalido o expirado</h2>
        <p class="DescripcionEstado">Este enlace ya fue utilizado o ha expirado. Solicita uno nuevo.</p>
        <RouterLink to="/recuperar-contrasena" class="BotonRedireccion">Solicitar nuevo enlace</RouterLink>
      </div>

      <div v-else-if="ContrasenaRestablecida" class="EstadoExito">
        <div class="IconoEstado">✅</div>
        <h2 class="TituloEstado">Contrasena restablecida</h2>
        <p class="DescripcionEstado">Tu contrasena fue actualizada correctamente. Ya puedes iniciar sesion.</p>
        <RouterLink to="/acceso" class="BotonRedireccion">Ir al inicio de sesion</RouterLink>
      </div>

      <div v-else>
        <div class="CabeceraSeccion">
          <div class="IconoSeccion">🔐</div>
          <h1 class="TituloSeccion">Nueva Contraseña</h1>
          <p class="DescripcionSeccion">Ingresa y confirma tu nueva contrasena.</p>
        </div>

        <form @submit.prevent="ManejarRestablecimiento">

          <div class="GrupoEntrada">
            <label class="EtiquetaCampo">Nueva Contraseña</label>
            <div class="ContenedorEntrada">
              <input
                v-model="NuevaContrasena"
                :type="VerNuevaContrasena ? 'text' : 'password'"
                class="CampoEntrada"
                placeholder="Minimo 6 caracteres"
              />
              <button type="button" class="BotonVer" @click="VerNuevaContrasena = !VerNuevaContrasena">
                {{ VerNuevaContrasena ? '🙈' : '👁️' }}
              </button>
            </div>
          </div>

          <div class="GrupoEntrada">
            <label class="EtiquetaCampo">Confirmar Contraseña</label>
            <div class="ContenedorEntrada">
              <input
                v-model="ConfirmarContrasena"
                :type="VerConfirmacion ? 'text' : 'password'"
                class="CampoEntrada"
                placeholder="Repite tu nueva contrasena"
              />
              <button type="button" class="BotonVer" @click="VerConfirmacion = !VerConfirmacion">
                {{ VerConfirmacion ? '🙈' : '👁️' }}
              </button>
            </div>
          </div>

          <div v-if="MensajeError" class="AlertaError">{{ MensajeError }}</div>

          <button type="submit" class="BotonAccion" :disabled="EstaCargando">
            <span v-if="!EstaCargando">Restablecer contrasena</span>
            <span v-else>Guardando...</span>
          </button>

        </form>
      </div>

    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { autenticacionApi } from '@/api/autenticacion.api.js'

const Ruta = useRoute()

const TokenUrl = ref('')
const NuevaContrasena = ref('')
const ConfirmarContrasena = ref('')
const VerNuevaContrasena = ref(false)
const VerConfirmacion = ref(false)
const EstaCargando = ref(false)
const MensajeError = ref('')
const VerificandoToken = ref(true)
const TokenInvalido = ref(false)
const ContrasenaRestablecida = ref(false)

onMounted(async () => {
  TokenUrl.value = Ruta.query.token || ''
  if (!TokenUrl.value) {
    TokenInvalido.value = true
    VerificandoToken.value = false
    return
  }
  try {
    const EsValido = await autenticacionApi.validarToken(TokenUrl.value)
    TokenInvalido.value = !EsValido
  } catch {
    TokenInvalido.value = true
  } finally {
    VerificandoToken.value = false
  }
})

const ManejarRestablecimiento = async () => {
  if (!NuevaContrasena.value || !ConfirmarContrasena.value) {
    MensajeError.value = 'Por favor completa todos los campos'
    return
  }
  if (NuevaContrasena.value.length < 6) {
    MensajeError.value = 'La contrasena debe tener al menos 6 caracteres'
    return
  }
  if (NuevaContrasena.value !== ConfirmarContrasena.value) {
    MensajeError.value = 'Las contrasenas no coinciden'
    return
  }
  EstaCargando.value = true
  MensajeError.value = ''
  try {
    await autenticacionApi.restablecerContrasena({
      token: TokenUrl.value,
      nuevaContrasena: NuevaContrasena.value
    })
    ContrasenaRestablecida.value = true
  } catch {
    MensajeError.value = 'Ocurrio un error al restablecer la contrasena'
  } finally {
    EstaCargando.value = false
  }
}
</script>
<style>
@import url('https://fonts.googleapis.com/css2?family=Crimson+Pro:ital,wght@0,300;0,400;0,600;1,300;1,400&family=Libre+Baskerville:ital,wght@0,400;0,700;1,400&display=swap');
@import '@/style/globals.css';
</style>
