import InstanciaAxios from './instanciaAxios'

export const clienteApi = {
  listarTodos: async () => {
    const Respuesta = await InstanciaAxios.get('/clientes')
    return Respuesta.data
  },
  guardar: async (ClienteDto, FotoPerfil) => {
    const DatosFormulario = new FormData()
    DatosFormulario.append('cliente', new Blob([JSON.stringify(ClienteDto)], { type: 'application/json' }))
    if (FotoPerfil) DatosFormulario.append('foto', FotoPerfil)
    const Respuesta = await InstanciaAxios.post('/clientes', DatosFormulario)
    return Respuesta.data
  },
  actualizar: async (Identificador, ClienteDto, FotoPerfil) => {
    const DatosFormulario = new FormData()
    DatosFormulario.append('cliente', new Blob([JSON.stringify(ClienteDto)], { type: 'application/json' }))
    if (FotoPerfil) DatosFormulario.append('foto', FotoPerfil)
    const Respuesta = await InstanciaAxios.put(`/clientes/${Identificador}`, DatosFormulario)
    return Respuesta.data
  },
  eliminar: async (Identificador) => {
    await InstanciaAxios.delete(`/clientes/${Identificador}`)
  }
}