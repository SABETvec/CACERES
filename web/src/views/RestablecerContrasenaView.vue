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
</style>
<style scoped>

.ContenedorRestablecimiento {
  min-height: 100vh;
  background-color: #0f0e0b;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 40px 24px;
}

.TarjetaRestablecimiento {
  width: 100%;
  max-width: 440px;
  background-color: #1a1710;
  border: 1px solid #3d3320;
  border-radius: 12px;
  padding: 40px;
}

.EstadoCarga, .EstadoError, .EstadoExito {
  text-align: center;
  padding: 20px 0;
}

.AnimacionCarga {
  width: 40px;
  height: 40px;
  border: 3px solid #3d3320;
  border-top-color: #c4912a;
  border-radius: 50%;
  animation: Girar 0.8s linear infinite;
  margin: 0 auto 16px;
}

@keyframes Girar { to { transform: rotate(360deg); } }

.IconoEstado { font-size: 48px; margin-bottom: 16px; display: block; }

.TituloEstado {
  font-family: 'Libre Baskerville', serif;
  font-size: 22px;
  color: #f5f0e8;
  margin-bottom: 12px;
}

.DescripcionEstado {
  font-family: 'Crimson Pro', serif;
  font-size: 16px;
  color: #8a7d6b;
  margin-bottom: 24px;
  line-height: 1.6;
}

.BotonRedireccion {
  display: inline-block;
  padding: 12px 24px;
  background: linear-gradient(135deg, #c4912a 0%, #a87520 100%);
  border-radius: 6px;
  color: #0f0e0b;
  font-family: 'Libre Baskerville', serif;
  font-size: 14px;
  font-weight: 700;
  text-decoration: none;
  transition: transform 0.2s;
}

.BotonRedireccion:hover { transform: translateY(-2px); }

.CabeceraSeccion { text-align: center; margin-bottom: 32px; }
.IconoSeccion { font-size: 40px; margin-bottom: 16px; display: block; }

.TituloSeccion {
  font-family: 'Libre Baskerville', serif;
  font-size: 26px;
  color: #f5f0e8;
  margin-bottom: 8px;
}

.DescripcionSeccion {
  font-family: 'Crimson Pro', serif;
  font-size: 16px;
  color: #8a7d6b;
  font-style: italic;
}

.GrupoEntrada { margin-bottom: 20px; display: flex; flex-direction: column; gap: 8px; }

.EtiquetaCampo {
  font-family: 'Crimson Pro', serif;
  font-size: 13px;
  color: #c4912a;
  letter-spacing: 2px;
  text-transform: uppercase;
}

.ContenedorEntrada { position: relative; display: flex; align-items: center; }

.CampoEntrada {
  width: 100%;
  padding: 14px 48px 14px 16px;
  background-color: #0f0e0b;
  border: 1px solid #3d3320;
  border-radius: 6px;
  color: #f5f0e8;
  font-family: 'Crimson Pro', serif;
  font-size: 16px;
  outline: none;
  transition: border-color 0.3s;
}

.CampoEntrada::placeholder { color: #4a3f2e; }
.CampoEntrada:focus { border-color: #c4912a; }

.BotonVer {
  position: absolute;
  right: 12px;
  background: none;
  border: none;
  cursor: pointer;
  font-size: 16px;
  padding: 4px;
}

.AlertaError {
  padding: 12px 16px;
  background-color: rgba(220, 60, 60, 0.1);
  border: 1px solid rgba(220, 60, 60, 0.3);
  border-radius: 6px;
  color: #e87c7c;
  font-family: 'Crimson Pro', serif;
  font-size: 15px;
  margin-bottom: 16px;
}

.BotonAccion {
  width: 100%;
  padding: 16px;
  background: linear-gradient(135deg, #c4912a 0%, #a87520 100%);
  border: none;
  border-radius: 6px;
  color: #0f0e0b;
  font-family: 'Libre Baskerville', serif;
  font-size: 15px;
  font-weight: 700;
  cursor: pointer;
  transition: transform 0.2s, box-shadow 0.2s, opacity 0.2s;
}

.BotonAccion:hover:not(:disabled) {
  transform: translateY(-2px);
  box-shadow: 0 8px 24px rgba(196, 145, 42, 0.3);
}

.BotonAccion:disabled { opacity: 0.7; cursor: not-allowed; }
</style>