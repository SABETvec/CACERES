import InstanciaAxios from './instanciaAxios'

export const rolAccesoApi = {
  listarPorRol: async (IdRol) => {
    const Respuesta = await InstanciaAxios.get(`/rol-accesos/rol/${IdRol}`)
    return Respuesta.data
  },
  guardarAccesos: async (GuardarAccesosDto) => {
    await InstanciaAxios.post('/rol-accesos', GuardarAccesosDto)
  }
}