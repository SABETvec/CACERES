import InstanciaAxios from './instanciaAxios'

export const usuarioApi = {
  listarTodos: async () => {
    const Respuesta = await InstanciaAxios.get('/usuarios')
    return Respuesta.data
  },
  guardar: async (UsuarioDto, FotoPerfil) => {
    const DatosFormulario = new FormData()
    DatosFormulario.append('usuario', new Blob([JSON.stringify(UsuarioDto)], { type: 'application/json' }))
    if (FotoPerfil) DatosFormulario.append('foto', FotoPerfil)
    const Respuesta = await InstanciaAxios.post('/usuarios', DatosFormulario)
    return Respuesta.data
  },
  actualizar: async (Identificador, UsuarioDto, FotoPerfil) => {
    const DatosFormulario = new FormData()
    DatosFormulario.append('usuario', new Blob([JSON.stringify(UsuarioDto)], { type: 'application/json' }))
    if (FotoPerfil) DatosFormulario.append('foto', FotoPerfil)
    const Respuesta = await InstanciaAxios.put(`/usuarios/${Identificador}`, DatosFormulario)
    return Respuesta.data
  },
  eliminar: async (Identificador) => {
    await InstanciaAxios.delete(`/usuarios/${Identificador}`)
  },
  cambiarEstado: async (Identificador) => {
    await InstanciaAxios.patch(`/usuarios/${Identificador}/estado`)
  }
}