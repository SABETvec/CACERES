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
</style>
<style scoped>

.ContenedorRecuperacion {
  min-height: 100vh;
  background-color: #0f0e0b;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 40px 24px;
}

.TarjetaRecuperacion {
  width: 100%;
  max-width: 440px;
  background-color: #1a1710;
  border: 1px solid #3d3320;
  border-radius: 12px;
  padding: 40px;
}

.BotonRegresar {
  display: inline-flex;
  align-items: center;
  gap: 8px;
  color: #8a7d6b;
  font-family: 'Crimson Pro', serif;
  font-size: 15px;
  text-decoration: none;
  margin-bottom: 32px;
  transition: color 0.2s;
}

.BotonRegresar:hover { color: #c4912a; }

.CabeceraSeccion { text-align: center; margin-bottom: 32px; }

.IconoSeccion { font-size: 40px; margin-bottom: 16px; display: block; }

.TituloSeccion {
  font-family: 'Libre Baskerville', serif;
  font-size: 26px;
  color: #f5f0e8;
  margin-bottom: 12px;
}

.DescripcionSeccion {
  font-family: 'Crimson Pro', serif;
  font-size: 16px;
  color: #8a7d6b;
  line-height: 1.6;
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

.IconoCampo {
  position: absolute;
  left: 14px;
  color: #5a4e3c;
  display: flex;
  align-items: center;
  pointer-events: none;
}

.CampoEntrada {
  width: 100%;
  padding: 14px 48px;
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

.MensajeExito { text-align: center; }
.IconoExito { font-size: 48px; margin-bottom: 16px; display: block; }

.TituloExito {
  font-family: 'Libre Baskerville', serif;
  font-size: 22px;
  color: #f5f0e8;
  margin-bottom: 12px;
}

.DescripcionExito {
  font-family: 'Crimson Pro', serif;
  font-size: 16px;
  color: #8a7d6b;
  line-height: 1.6;
  margin-bottom: 12px;
}

.DescripcionExito strong { color: #c4912a; }

.AdvertenciaExito {
  font-family: 'Crimson Pro', serif;
  font-size: 14px;
  color: #5a4e3c;
  margin-bottom: 24px;
  font-style: italic;
}

.EnlaceVolver {
  display: inline-block;
  padding: 12px 24px;
  border: 1px solid #c4912a;
  border-radius: 6px;
  color: #c4912a;
  font-family: 'Crimson Pro', serif;
  font-size: 15px;
  text-decoration: none;
  transition: background-color 0.2s;
}

.EnlaceVolver:hover { background-color: rgba(196, 145, 42, 0.1); }
</style>