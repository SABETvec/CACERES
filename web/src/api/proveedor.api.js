import InstanciaAxios from './instanciaAxios'

export const proveedorApi = {
  listarTodos: async () => {
    const Respuesta = await InstanciaAxios.get('/proveedores')
    return Respuesta.data
  },
  guardar: async (ProveedorDto, FotoPerfil) => {
    const DatosFormulario = new FormData()
    DatosFormulario.append('proveedor', new Blob([JSON.stringify(ProveedorDto)], { type: 'application/json' }))
    if (FotoPerfil) DatosFormulario.append('foto', FotoPerfil)
    const Respuesta = await InstanciaAxios.post('/proveedores', DatosFormulario)
    return Respuesta.data
  },
  actualizar: async (Identificador, ProveedorDto, FotoPerfil) => {
    const DatosFormulario = new FormData()
    DatosFormulario.append('proveedor', new Blob([JSON.stringify(ProveedorDto)], { type: 'application/json' }))
    if (FotoPerfil) DatosFormulario.append('foto', FotoPerfil)
    const Respuesta = await InstanciaAxios.put(`/proveedores/${Identificador}`, DatosFormulario)
    return Respuesta.data
  },
  eliminar: async (Identificador) => {
    await InstanciaAxios.delete(`/proveedores/${Identificador}`)
  }
}