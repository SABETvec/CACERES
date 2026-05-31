import axios from 'axios'
import router from '@/router'

const InstanciaAxios = axios.create({
  baseURL: 'http://localhost:8080/api'
})

InstanciaAxios.interceptors.request.use((ConfiguracionPeticion) => {
  const TokenGuardado = localStorage.getItem('tokenJwt')
  if (TokenGuardado) {
    ConfiguracionPeticion.headers.Authorization = `Bearer ${TokenGuardado}`
  }
  return ConfiguracionPeticion
})

InstanciaAxios.interceptors.response.use(
  (RespuestaExitosa) => RespuestaExitosa,
  (ErrorRespuesta) => {
    if (ErrorRespuesta.response?.status === 401) {
      localStorage.removeItem('tokenJwt')
      localStorage.removeItem('datosUsuario')
      router.push({ name: 'acceso' })
    }
    return Promise.reject(ErrorRespuesta)
  }
)

export default InstanciaAxios