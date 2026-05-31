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
<style scoped>

.ContenedorPrincipal {
  display: flex;
  min-height: 100vh;
  background-color: var(--bg-light);
}

.PanelIzquierdo {
  flex: 1;
  background: linear-gradient(135deg, #f0f6ff 0%, #e0eeff 40%, #f0f6ff 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  position: relative;
  overflow: hidden;
  border-right: 1px solid #cbd5e1;
}

.PanelIzquierdo::before {
  content: '';
  position: absolute;
  top: 0; left: 0; right: 0; bottom: 0;
  background-image: radial-gradient(circle at 20% 50%, rgba(196, 145, 42, 0.08) 0%, transparent 50%),
                    radial-gradient(circle at 80% 20%, rgba(196, 145, 42, 0.05) 0%, transparent 40%);
}

.ContenidoDecorativo {
  position: relative;
  z-index: 1;
  text-align: center;
  padding: 40px;
}

.CirculoGrande {
  position: absolute;
  width: 400px;
  height: 400px;
  border: 1px solid rgba(196, 145, 42, 0.1);
  border-radius: 50%;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  animation: GirarLento 30s linear infinite;
}

.CirculoMediano {
  position: absolute;
  width: 260px;
  height: 260px;
  border: 1px solid rgba(196, 145, 42, 0.15);
  border-radius: 50%;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  animation: GirarLento 20s linear infinite reverse;
}

@keyframes GirarLento {
  from { transform: translate(-50%, -50%) rotate(0deg); }
  to { transform: translate(-50%, -50%) rotate(360deg); }
}

.TextoDecorativo {
  display: flex;
  gap: 8px;
  justify-content: center;
  margin-bottom: 30px;
}

.LetraGrande {
  font-family: 'Libre Baskerville', serif;
  font-size: 52px;
  font-weight: 700;
  color: var(--color-primary-light);
  opacity: 0.15;
  letter-spacing: 4px;
  display: inline-block;
  animation: AparecerLetra 0.8s ease forwards;
}

.LetraGrande:nth-child(1) { animation-delay: 0.1s; }
.LetraGrande:nth-child(2) { animation-delay: 0.2s; }
.LetraGrande:nth-child(3) { animation-delay: 0.3s; }
.LetraGrande:nth-child(4) { animation-delay: 0.4s; }
.LetraGrande:nth-child(5) { animation-delay: 0.5s; }
.LetraGrande:nth-child(6) { animation-delay: 0.6s; }

@keyframes AparecerLetra {
  from { opacity: 0; transform: translateY(20px); }
  to { opacity: 0.15; transform: translateY(0); }
}

.FraseInspiradora {
  font-family: 'Crimson Pro', serif;
  font-style: italic;
  font-size: 20px;
  color: var(--color-primary-light);
  opacity: 0.7;
  line-height: 1.6;
  max-width: 320px;
  margin: 0 auto 24px;
}

.LineaDecorativa {
  width: 60px;
  height: 1px;
  background: linear-gradient(to right, transparent, var(--color-primary-light), transparent);
  margin: 24px auto;
}

.DescripcionLibreria {
  font-family: 'Libre Baskerville', serif;
  font-size: 18px;
  color: var(--color-gray-dark);
  opacity: 0.6;
  line-height: 1.8;
}

.SubtituloLibreria {
  font-family: 'Crimson Pro', serif;
  font-size: 14px;
  color: var(--color-primary-light);
  letter-spacing: 2px;
  text-transform: uppercase;
}

.PanelDerecho {
  width: 480px;
  background-color: var(--color-white);
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 40px;
}

.TarjetaAcceso {
  width: 100%;
  max-width: 380px;
}

.CabeceraFormulario {
  text-align: center;
  margin-bottom: 40px;
}

.IconoLibro {
  font-size: 48px;
  margin-bottom: 16px;
  display: block;
  animation: FlotarIcono 3s ease-in-out infinite;
}

@keyframes FlotarIcono {
  0%, 100% { transform: translateY(0); }
  50% { transform: translateY(-6px); }
}

.TituloAcceso {
  font-family: 'Libre Baskerville', serif;
  font-size: 32px;
  font-weight: 700;
  color: var(--color-gray-dark);
  margin-bottom: 8px;
  letter-spacing: -0.5px;
}

.SubtituloAcceso {
  font-family: 'Crimson Pro', serif;
  font-size: 16px;
  color: #8a7d6b;
  font-style: italic;
}

.FormularioAcceso {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.GrupoEntrada {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.EtiquetaCampo {
  font-family: 'Crimson Pro', serif;
  font-size: 13px;
  color: var(--color-primary-light);
  letter-spacing: 2px;
  text-transform: uppercase;
}

.ContenedorEntrada {
  position: relative;
  display: flex;
  align-items: center;
}

.IconoCampo {
  position: absolute;
  left: 14px;
  color: #5a4e3c;
  display: flex;
  align-items: center;
  pointer-events: none;
  z-index: 1;
}

.CampoEntrada {
  width: 100%;
  padding: 14px 48px;
  background-color: var(--bg-white);
  border: 1px solid var(--color-gray-medium);
  border-radius: 6px;
  color: var(--color-gray-dark);
  font-family: 'Crimson Pro', serif;
  font-size: 16px;
  outline: none;
  transition: border-color 0.3s, background-color 0.3s;
}

.CampoEntrada::placeholder {
  color: var(--color-gray-text);
}

.CampoEntrada:focus {
  border-color: var(--color-primary-light);
  background-color: #1f1c12;
}

.BotonVerContrasena {
  position: absolute;
  right: 14px;
  background: none;
  border: none;
  color: #5a4e3c;
  cursor: pointer;
  display: flex;
  align-items: center;
  padding: 4px;
  transition: color 0.2s;
}

.BotonVerContrasena:hover {
  color: var(--color-primary-light);
}

.AlertaError {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 12px 16px;
  background-color: rgba(220, 60, 60, 0.1);
  border: 1px solid rgba(220, 60, 60, 0.3);
  border-radius: 6px;
  color: #e87c7c;
  font-family: 'Crimson Pro', serif;
  font-size: 15px;
}

.BotonIngresar {
  padding: 16px;
  background: linear-gradient(135deg, var(--color-primary-light) 0%, var(--color-primary-dark) 100%);
  border: none;
  border-radius: 6px;
  color: var(--color-white);
  font-family: 'Libre Baskerville', serif;
  font-size: 15px;
  font-weight: 700;
  letter-spacing: 1px;
  cursor: pointer;
  transition: transform 0.2s, box-shadow 0.2s, opacity 0.2s;
  margin-top: 4px;
}

.BotonIngresar:hover:not(:disabled) {
  transform: translateY(-2px);
  box-shadow: 0 8px 24px rgba(196, 145, 42, 0.3);
}

.BotonIngresar:active:not(:disabled) {
  transform: translateY(0);
}

.BotonCargando {
  opacity: 0.7;
  cursor: not-allowed;
}

.TextoCargando {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 6px;
}

.PuntoCarga {
  width: 8px;
  height: 8px;
  background-color: var(--color-white);
  border-radius: 50%;
  animation: PulsarPunto 1.2s ease-in-out infinite;
}

.PuntoCarga:nth-child(2) { animation-delay: 0.2s; }
.PuntoCarga:nth-child(3) { animation-delay: 0.4s; }

@keyframes PulsarPunto {
  0%, 80%, 100% { transform: scale(0.6); opacity: 0.4; }
  40% { transform: scale(1); opacity: 1; }
}

.ContenedorOlvideContrasena {
  text-align: center;
}

.EnlaceOlvideContrasena {
  font-family: 'Crimson Pro', serif;
  font-size: 15px;
  color: #8a7d6b;
  text-decoration: none;
  font-style: italic;
  transition: color 0.2s;
}

.EnlaceOlvideContrasena:hover {
  color: var(--color-primary-light);
}

.PieTarjeta {
  text-align: center;
  margin-top: 40px;
  font-family: 'Crimson Pro', serif;
  font-size: 13px;
  color: var(--color-gray-medium);
  letter-spacing: 1px;
}

@media (max-width: 768px) {
  .PanelIzquierdo { display: none; }
  .PanelDerecho { width: 100%; padding: 24px; }
}
</style>
