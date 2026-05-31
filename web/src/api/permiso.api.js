import InstanciaAxios from './instanciaAxios'

export const permisoApi = {
  listarTodos: async () => {
    const Respuesta = await InstanciaAxios.get('/permisos')
    return Respuesta.data
  }
}