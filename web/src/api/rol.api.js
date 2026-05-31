import InstanciaAxios from './instanciaAxios'

export const rolApi = {
  listarTodos: async () => {
    const Respuesta = await InstanciaAxios.get('/roles')
    return Respuesta.data
  },
  guardar: async (RolDto) => {
    const Respuesta = await InstanciaAxios.post('/roles', RolDto)
    return Respuesta.data
  },
  actualizar: async (Identificador, RolDto) => {
    const Respuesta = await InstanciaAxios.put(`/roles/${Identificador}`, RolDto)
    return Respuesta.data
  },
  eliminar: async (Identificador) => {
    await InstanciaAxios.delete(`/roles/${Identificador}`)
  }
}