import InstanciaAxios from './instanciaAxios'

export const moduloApi = {
  listarTodos: async () => {
    const Respuesta = await InstanciaAxios.get('/modulos')
    return Respuesta.data
  }
}