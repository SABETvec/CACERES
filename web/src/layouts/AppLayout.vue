<template>
  <div class="LayoutPrincipal">

    <div
      v-if="SidebarVisible"
      class="OverlayMovil"
      @click="SidebarVisible = false"
    />

    <aside class="BarraLateral" :class="{ 'BarraLateral--Visible': SidebarVisible }">

      <div class="MarcaApp">
        <div class="IconoMarca">
          <svg width="28" height="28" viewBox="0 0 24 24" fill="none" stroke="#c4912a" stroke-width="1.5">
            <path d="M4 19.5A2.5 2.5 0 0 1 6.5 17H20"/>
            <path d="M6.5 2H20v20H6.5A2.5 2.5 0 0 1 4 19.5v-15A2.5 2.5 0 0 1 6.5 2z"/>
          </svg>
        </div>
        <div class="TextoMarca">
          <span class="NombreMarca">Librería Cáceres</span>
          <span class="SubtituloMarca">Sistema de Gestión</span>
        </div>
      </div>

      <div class="SeparadorNav" />

      <nav class="NavegacionPrincipal">

        <RouterLink to="/inicio" class="EnlaceNav" active-class="EnlaceNav--Activo" @click="SidebarVisible = false">
          <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.8">
            <path d="M3 9l9-7 9 7v11a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2z"/>
            <polyline points="9 22 9 12 15 12 15 22"/>
          </svg>
          Inicio
        </RouterLink>

        <div class="EtiquetaSeccion">ADMINISTRACIÓN</div>

        <RouterLink to="/roles" class="EnlaceNav" active-class="EnlaceNav--Activo" @click="SidebarVisible = false">
          <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.8">
            <path d="M12 22s8-4 8-10V5l-8-3-8 3v7c0 6 8 10 8 10z"/>
          </svg>
          Roles
        </RouterLink>

        <RouterLink to="/usuarios" class="EnlaceNav" active-class="EnlaceNav--Activo" @click="SidebarVisible = false">
          <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.8">
            <path d="M17 21v-2a4 4 0 0 0-4-4H5a4 4 0 0 0-4 4v2"/>
            <circle cx="9" cy="7" r="4"/>
            <path d="M23 21v-2a4 4 0 0 0-3-3.87"/>
            <path d="M16 3.13a4 4 0 0 1 0 7.75"/>
          </svg>
          Usuarios
        </RouterLink>

        <div class="EtiquetaSeccion">COMERCIAL</div>

<RouterLink to="/clientes" class="EnlaceNav" active-class="EnlaceNav--Activo" @click="SidebarVisible = false">
  <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.8">
    <path d="M17 21v-2a4 4 0 0 0-4-4H5a4 4 0 0 0-4 4v2"/>
    <circle cx="9" cy="7" r="4"/>
    <path d="M23 21v-2a4 4 0 0 0-3-3.87"/>
    <path d="M16 3.13a4 4 0 0 1 0 7.75"/>
  </svg>
  Clientes
</RouterLink>

<RouterLink to="/proveedores" class="EnlaceNav" active-class="EnlaceNav--Activo" @click="SidebarVisible = false">
  <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.8">
    <rect x="1" y="3" width="15" height="13"/>
    <polygon points="16 8 20 8 23 11 23 16 16 16 16 8"/>
    <circle cx="5.5" cy="18.5" r="2.5"/>
    <circle cx="18.5" cy="18.5" r="2.5"/>
  </svg>
  Proveedores
</RouterLink>

      </nav>

      <div class="InfoUsuarioNav">
        <div class="AvatarNavUsuario">
          <img
            v-if="AlmacenSesion.DatosUsuario?.FotoPerfil"
            :src="`http://localhost:8080${AlmacenSesion.DatosUsuario.FotoPerfil}`"
            alt="foto"
          />
          <span v-else>{{ InicialesUsuario }}</span>
        </div>
        <div class="DatosNavUsuario">
          <span class="NombreNavUsuario">{{ AlmacenSesion.DatosUsuario?.Nombre }}</span>
          <span class="UsernameNav">@{{ AlmacenSesion.DatosUsuario?.NombreUsuario }}</span>
        </div>
        <button class="BotonSalirNav" @click="ManejarCierreSesion" title="Cerrar sesion">
          <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.8">
            <path d="M9 21H5a2 2 0 0 1-2-2V5a2 2 0 0 1 2-2h4"/>
            <polyline points="16 17 21 12 16 7"/>
            <line x1="21" y1="12" x2="9" y2="12"/>
          </svg>
        </button>
      </div>

    </aside>

    <div class="AreaContenido">

      <header class="CabeceraMovil">
        <button class="BotonMenuMovil" @click="SidebarVisible = true">
          <svg width="22" height="22" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
            <line x1="3" y1="12" x2="21" y2="12"/>
            <line x1="3" y1="6" x2="21" y2="6"/>
            <line x1="3" y1="18" x2="21" y2="18"/>
          </svg>
        </button>
        <span class="TituloMovil">Librería Cáceres</span>
      </header>

      <div class="ContenedorNotificaciones">
        <TransitionGroup name="FadeNotificacion">
          <div
            v-for="Notificacion in ListaNotificaciones"
            :key="Notificacion.identificador"
            class="TarjetaNotificacion"
            :class="`TarjetaNotificacion--${Notificacion.tipo}`"
          >
            <svg v-if="Notificacion.tipo === 'exito'" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
              <polyline points="20 6 9 17 4 12"/>
            </svg>
            <svg v-else width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
              <circle cx="12" cy="12" r="10"/>
              <line x1="12" y1="8" x2="12" y2="12"/>
              <line x1="12" y1="16" x2="12.01" y2="16"/>
            </svg>
            {{ Notificacion.mensaje }}
          </div>
        </TransitionGroup>
      </div>

      <RouterView />

    </div>

  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { useRouter } from 'vue-router'
import { useSesionStore } from '@/stores/sesionStore'
import { useNotificacion } from '@/composables/useNotificacion'

const Enrutador = useRouter()
const AlmacenSesion = useSesionStore()
const { ListaNotificaciones } = useNotificacion()

const SidebarVisible = ref(false)

const InicialesUsuario = computed(() => {
  const NombreCompleto = AlmacenSesion.DatosUsuario?.Nombre || ''
  return NombreCompleto.charAt(0).toUpperCase()
})

const ManejarCierreSesion = () => {
  AlmacenSesion.CerrarSesion()
  Enrutador.push({ name: 'acceso' })
}
</script>
<style>
@import url('https://fonts.googleapis.com/css2?family=Crimson+Pro:ital,wght@0,300;0,400;0,600;1,300;1,400&family=Libre+Baskerville:ital,wght@0,400;0,700;1,400&display=swap');
</style>
<style scoped>

.LayoutPrincipal {
  display: flex;
  min-height: 100vh;
  background-color: #0f0e0b;
}

.OverlayMovil {
  position: fixed;
  inset: 0;
  background-color: rgba(0, 0, 0, 0.6);
  z-index: 40;
}

.BarraLateral {
  width: 260px;
  min-height: 100vh;
  background-color: #12100c;
  border-right: 1px solid #2d2410;
  display: flex;
  flex-direction: column;
  position: fixed;
  top: 0;
  left: 0;
  bottom: 0;
  z-index: 50;
  transition: transform 0.3s ease;
}

.MarcaApp {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 24px 20px;
}

.IconoMarca {
  width: 42px;
  height: 42px;
  background-color: #1f1a10;
  border: 1px solid #3d2e0a;
  border-radius: 8px;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
}

.NombreMarca {
  display: block;
  font-family: 'Libre Baskerville', serif;
  font-size: 15px;
  font-weight: 700;
  color: #f5f0e8;
  line-height: 1.2;
}

.SubtituloMarca {
  display: block;
  font-family: 'Crimson Pro', serif;
  font-size: 12px;
  color: #8a7d6b;
  letter-spacing: 1px;
  text-transform: uppercase;
}

.SeparadorNav {
  height: 1px;
  background: linear-gradient(to right, transparent, #2d2410, transparent);
  margin: 0 20px;
}

.EtiquetaSeccion {
  font-family: 'Crimson Pro', serif;
  font-size: 11px;
  color: #5a4e3c;
  letter-spacing: 2px;
  text-transform: uppercase;
  padding: 16px 20px 8px;
}

.NavegacionPrincipal {
  flex: 1;
  padding: 12px 12px;
  display: flex;
  flex-direction: column;
  gap: 2px;
}

.EnlaceNav {
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 10px 12px;
  border-radius: 6px;
  color: #8a7d6b;
  text-decoration: none;
  font-family: 'Crimson Pro', serif;
  font-size: 16px;
  transition: background-color 0.2s, color 0.2s;
}

.EnlaceNav:hover {
  background-color: #1f1a10;
  color: #f5f0e8;
}

.EnlaceNav--Activo {
  background-color: #1f1a10;
  color: #c4912a;
  border-left: 2px solid #c4912a;
}

.InfoUsuarioNav {
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 16px 16px;
  border-top: 1px solid #2d2410;
  margin-top: auto;
}

.AvatarNavUsuario {
  width: 36px;
  height: 36px;
  border-radius: 50%;
  background-color: #2d2410;
  border: 1px solid #c4912a;
  display: flex;
  align-items: center;
  justify-content: center;
  overflow: hidden;
  flex-shrink: 0;
  font-family: 'Libre Baskerville', serif;
  font-size: 14px;
  font-weight: 700;
  color: #c4912a;
}

.AvatarNavUsuario img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.DatosNavUsuario {
  flex: 1;
  min-width: 0;
}

.NombreNavUsuario {
  display: block;
  font-family: 'Crimson Pro', serif;
  font-size: 14px;
  color: #f5f0e8;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.UsernameNav {
  display: block;
  font-family: 'Crimson Pro', serif;
  font-size: 12px;
  color: #5a4e3c;
}

.BotonSalirNav {
  background: none;
  border: none;
  color: #5a4e3c;
  cursor: pointer;
  padding: 6px;
  border-radius: 4px;
  display: flex;
  align-items: center;
  transition: color 0.2s, background-color 0.2s;
  flex-shrink: 0;
}

.BotonSalirNav:hover {
  color: #e87c7c;
  background-color: rgba(220, 60, 60, 0.1);
}

.AreaContenido {
  flex: 1;
  margin-left: 260px;
  min-height: 100vh;
  display: flex;
  flex-direction: column;
  position: relative;
}

.CabeceraMovil {
  display: none;
  align-items: center;
  gap: 16px;
  padding: 16px 20px;
  background-color: #12100c;
  border-bottom: 1px solid #2d2410;
}

.BotonMenuMovil {
  background: none;
  border: none;
  color: #f5f0e8;
  cursor: pointer;
  padding: 4px;
  display: flex;
  align-items: center;
}

.TituloMovil {
  font-family: 'Libre Baskerville', serif;
  font-size: 16px;
  color: #f5f0e8;
}

.ContenedorNotificaciones {
  position: fixed;
  top: 20px;
  right: 20px;
  z-index: 100;
  display: flex;
  flex-direction: column;
  gap: 8px;
  max-width: 360px;
}

.TarjetaNotificacion {
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 14px 18px;
  border-radius: 8px;
  font-family: 'Crimson Pro', serif;
  font-size: 15px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.4);
}

.TarjetaNotificacion--exito {
  background-color: #1a2e1a;
  border: 1px solid #2d6a2d;
  color: #7bc97b;
}

.TarjetaNotificacion--error {
  background-color: #2e1a1a;
  border: 1px solid #6a2d2d;
  color: #e87c7c;
}

.TarjetaNotificacion--advertencia {
  background-color: #2e2a1a;
  border: 1px solid #6a5a1a;
  color: #d4b44a;
}

.FadeNotificacion-enter-active,
.FadeNotificacion-leave-active {
  transition: all 0.3s ease;
}

.FadeNotificacion-enter-from {
  opacity: 0;
  transform: translateX(20px);
}

.FadeNotificacion-leave-to {
  opacity: 0;
  transform: translateX(20px);
}

@media (max-width: 768px) {
  .BarraLateral {
    transform: translateX(-100%);
  }
  .BarraLateral--Visible {
    transform: translateX(0);
  }
  .AreaContenido {
    margin-left: 0;
  }
  .CabeceraMovil {
    display: flex;
  }
}
</style>