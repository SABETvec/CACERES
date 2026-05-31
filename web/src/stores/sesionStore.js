import { defineStore } from 'pinia'
import { ref, computed } from 'vue'

export const useSesionStore = defineStore('sesion', () => {
  const TokenJwt = ref(localStorage.getItem('tokenJwt') || null)
  const DatosUsuario = ref(JSON.parse(localStorage.getItem('datosUsuario') || 'null'))

  const EstaAutenticado = computed(() => !!TokenJwt.value)

  const IniciarSesion = (DatosRespuesta) => {
    TokenJwt.value = DatosRespuesta.tokenJwt
    DatosUsuario.value = {
      NombreUsuario: DatosRespuesta.nombreUsuario,
      Nombre: DatosRespuesta.nombre,
      ApellidoPaterno: DatosRespuesta.apellidoPaterno,
      FotoPerfil: DatosRespuesta.fotoPerfil
    }
    localStorage.setItem('tokenJwt', DatosRespuesta.tokenJwt)
    localStorage.setItem('datosUsuario', JSON.stringify(DatosUsuario.value))
  }

  const CerrarSesion = () => {
    TokenJwt.value = null
    DatosUsuario.value = null
    localStorage.removeItem('tokenJwt')
    localStorage.removeItem('datosUsuario')
  }

  return { TokenJwt, DatosUsuario, EstaAutenticado, IniciarSesion, CerrarSesion }
})