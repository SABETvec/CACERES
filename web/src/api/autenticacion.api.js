import axios from 'axios'

const UrlBase = 'http://localhost:8080/api/autenticacion'

export const autenticacionApi = {

  iniciarSesion: async (DatosLogin) => {
    try {
      const Respuesta = await axios.post(
        `${UrlBase}/IniciarSesion`,
        DatosLogin
      )
      return Respuesta.data
    } catch (error) {
      throw new Error('Credenciales incorrectas')
    }
  },

  recuperarContrasena: async (DatosRecuperacion) => {
    try {
      const Respuesta = await axios.post(
        `${UrlBase}/RecuperarContrasena`,
        DatosRecuperacion
      )
      return Respuesta.data
    } catch (error) {
      throw new Error('Correo no encontrado')
    }
  },

  validarToken: async (TokenRecibido) => {
    try {
      const Respuesta = await axios.get(
        `${UrlBase}/ValidarToken`,
        {
          params: {
            token: TokenRecibido
          }
        }
      )
      return Respuesta.data
    } catch (error) {
      throw new Error('Token invalido')
    }
  },

  restablecerContrasena: async (DatosRestablecimiento) => {
    try {
      const Respuesta = await axios.post(
        `${UrlBase}/RestablecerContrasena`,
        DatosRestablecimiento
      )
      return Respuesta.data
    } catch (error) {
      throw new Error('Error al restablecer la contrasena')
    }
  }

}