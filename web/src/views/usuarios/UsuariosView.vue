<template>
  <div class="VistaUsuarios">

    <div class="CabeceraVista">
      <div>
        <h1 class="TituloVista">Gestión de Usuarios</h1>
        <p class="SubtituloVista">Administra los usuarios del sistema</p>
      </div>
      <button class="BotonPrimario" @click="AbrirModalCrear">
        <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
          <line x1="12" y1="5" x2="12" y2="19"/>
          <line x1="5" y1="12" x2="19" y2="12"/>
        </svg>
        Nuevo Usuario
      </button>
    </div>

    <div class="FilaFiltros">
      <div class="ContenedorBusqueda">
        <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="#5a4e3c" stroke-width="2">
          <circle cx="11" cy="11" r="8"/>
          <line x1="21" y1="21" x2="16.65" y2="16.65"/>
        </svg>
        <input
          v-model="TextoBusqueda"
          type="text"
          class="EntradaBusqueda"
          placeholder="Buscar usuario..."
        />
      </div>

      <select v-model="FiltroRolSeleccionado" class="SelectorFiltro">
        <option value="">Todos los roles</option>
        <option v-for="Rol in ListaRoles" :key="Rol.idRol" :value="Rol.idRol">{{ Rol.nombre }}</option>
      </select>

      <select v-model="FiltroEstadoSeleccionado" class="SelectorFiltro">
        <option value="">Todos los estados</option>
        <option value="true">Activos</option>
        <option value="false">Inactivos</option>
      </select>
    </div>

    <div class="ContenedorTabla">
      <div v-if="EstaCargando" class="EstadoCargando">
        <div class="AnimadorCarga" />
        <span>Cargando usuarios...</span>
      </div>

      <table v-else class="TablaEstilos">
        <thead>
          <tr>
            <th class="EncabezadoTabla"></th>
            <th class="EncabezadoTabla">Usuario</th>
            <th class="EncabezadoTabla">Nombre</th>
            <th class="EncabezadoTabla">Correo</th>
            <th class="EncabezadoTabla">Celular</th>
            <th class="EncabezadoTabla">Rol</th>
            <th class="EncabezadoTabla" style="text-align:center">Estado</th>
            <th class="EncabezadoTabla" style="text-align:center">Acciones</th>
          </tr>
        </thead>
        <tbody>
          <tr v-if="UsuariosFiltrados.length === 0">
            <td colspan="8" class="FilaVacia">No hay usuarios registrados</td>
          </tr>
          <tr v-for="Usuario in UsuariosFiltrados" :key="Usuario.idUsuario" class="FilaTabla">
            <td class="CeldaTabla CeldaAvatar">
              <div class="AvatarTabla">
                <img
                  v-if="Usuario.fotoPerfil"
                  :src="`http://localhost:8080${Usuario.fotoPerfil}`"
                  alt="foto"
                />
                <span v-else>{{ (Usuario.nombre || '?').charAt(0).toUpperCase() }}</span>
              </div>
            </td>
            <td class="CeldaTabla CeldaUsername">{{ Usuario.nombreUsuario }}</td>
            <td class="CeldaTabla">{{ Usuario.nombre }} {{ Usuario.apellidoPaterno }}</td>
            <td class="CeldaTabla CeldaTextoSecundario">{{ Usuario.correoElectronico }}</td>
            <td class="CeldaTabla CeldaTextoSecundario">{{ Usuario.celular }}</td>
            <td class="CeldaTabla">
              <span class="InsigniaRol">{{ Usuario.nombreRol }}</span>
            </td>
            <td class="CeldaTabla" style="text-align:center">
              <span class="InsigniaEstado" :class="Usuario.activo ? 'InsigniaEstado--Activo' : 'InsigniaEstado--Inactivo'">
                {{ Usuario.activo ? 'Activo' : 'Inactivo' }}
              </span>
            </td>
            <td class="CeldaTabla CeldaAcciones">
              <button class="BotonIcono BotonDetalle" @click="AbrirDetalle(Usuario)" title="Ver detalle">
                <svg width="15" height="15" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                  <path d="M1 12s4-8 11-8 11 8 11 8-4 8-11 8-11-8-11-8z"/>
                  <circle cx="12" cy="12" r="3"/>
                </svg>
              </button>
              <button class="BotonIcono BotonEditar" @click="AbrirModalEditar(Usuario)" title="Editar">
                <svg width="15" height="15" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                  <path d="M11 4H4a2 2 0 0 0-2 2v14a2 2 0 0 0 2 2h14a2 2 0 0 0 2-2v-7"/>
                  <path d="M18.5 2.5a2.121 2.121 0 0 1 3 3L12 15l-4 1 1-4 9.5-9.5z"/>
                </svg>
              </button>
              <button
                class="BotonIcono"
                :class="Usuario.activo ? 'BotonDesactivar' : 'BotonActivar'"
                @click="CambiarEstadoUsuario(Usuario)"
                :title="Usuario.activo ? 'Desactivar' : 'Activar'"
              >
                <svg v-if="Usuario.activo" width="15" height="15" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                  <circle cx="12" cy="12" r="10"/>
                  <line x1="4.93" y1="4.93" x2="19.07" y2="19.07"/>
                </svg>
                <svg v-else width="15" height="15" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                  <polyline points="20 6 9 17 4 12"/>
                </svg>
              </button>
              <button class="BotonIcono BotonEliminar" @click="ConfirmarEliminar(Usuario)" title="Eliminar">
                <svg width="15" height="15" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                  <polyline points="3 6 5 6 21 6"/>
                  <path d="M19 6v14a2 2 0 0 1-2 2H7a2 2 0 0 1-2-2V6m3 0V4a1 1 0 0 1 1-1h4a1 1 0 0 1 1 1v2"/>
                </svg>
              </button>
            </td>
          </tr>
        </tbody>
      </table>
    </div>

    <Teleport to="body">
      <div v-if="ModalFormularioVisible" class="OverlayModal" @click.self="CerrarModal">
        <div class="ContenedorModal ContenedorModalGrande" style="max-height:92vh">

          <div class="CabeceraModal">
            <div class="TituloModal">
              <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="#c4912a" stroke-width="2">
                <path d="M20 21v-2a4 4 0 0 0-4-4H8a4 4 0 0 0-4 4v2"/>
                <circle cx="12" cy="7" r="4"/>
              </svg>
              {{ EsEdicion ? 'Editar Usuario' : 'Nuevo Usuario' }}
            </div>
            <button class="BotonCerrarModal" @click="CerrarModal">
              <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                <line x1="18" y1="6" x2="6" y2="18"/>
                <line x1="6" y1="6" x2="18" y2="18"/>
              </svg>
            </button>
          </div>

          <div class="CuerpoModal" style="overflow-y:auto;max-height:calc(92vh - 130px)">
            <div class="FilaModalUsuario">

              <div class="ColumnaFotoModal">
                <p class="EtiquetaSeccionModal">Foto de Perfil</p>
                <div class="ContenedorFoto">
                  <div class="VistasPreviaFoto">
                    <img v-if="VistasPreviaFoto" :src="VistasPreviaFoto" alt="vista previa" />
                    <span v-else class="PlaceholderFoto">
                      <svg width="36" height="36" viewBox="0 0 24 24" fill="none" stroke="#3d3320" stroke-width="1.5">
                        <path d="M20 21v-2a4 4 0 0 0-4-4H8a4 4 0 0 0-4 4v2"/>
                        <circle cx="12" cy="7" r="4"/>
                      </svg>
                    </span>
                  </div>
                  <label class="BotonSeleccionarFoto">
                    <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                      <path d="M23 19a2 2 0 0 1-2 2H3a2 2 0 0 1-2-2V8a2 2 0 0 1 2-2h4l2-3h6l2 3h4a2 2 0 0 1 2 2z"/>
                      <circle cx="12" cy="13" r="4"/>
                    </svg>
                    Seleccionar foto
                    <input type="file" accept=".jpg,.jpeg,.png" style="display:none" @change="ManejarSeleccionFoto" />
                  </label>
                </div>
              </div>

              <div class="ColumnaDatosModal">

                <p class="EtiquetaSeccionModal">Datos Personales</p>
                <div class="CuadriculaFormulario">
                  <div class="GrupoEntrada">
                    <label class="EtiquetaCampo">Nombre *</label>
                    <input v-model="Formulario.nombre" type="text" class="CampoEntrada" placeholder="Nombre" />
                  </div>
                  <div class="GrupoEntrada">
                    <label class="EtiquetaCampo">Apellido Paterno *</label>
                    <input v-model="Formulario.apellidoPaterno" type="text" class="CampoEntrada" placeholder="Apellido Paterno" />
                  </div>
                  <div class="GrupoEntrada">
                    <label class="EtiquetaCampo">Apellido Materno</label>
                    <input v-model="Formulario.apellidoMaterno" type="text" class="CampoEntrada" placeholder="Apellido Materno" />
                  </div>
                  <div class="GrupoEntrada">
                    <label class="EtiquetaCampo">Sexo</label>
                    <select v-model="Formulario.sexo" class="CampoEntrada">
                      <option value="">Seleccionar</option>
                      <option value="M">Masculino</option>
                      <option value="F">Femenino</option>
                    </select>
                  </div>
                  <div class="GrupoEntrada">
                    <label class="EtiquetaCampo">Fecha de Nacimiento</label>
                    <input v-model="Formulario.fechaDeNacimiento" type="date" class="CampoEntrada" />
                  </div>
                </div>

                <div class="SeparadorSeccion" />

                <p class="EtiquetaSeccionModal">Contacto</p>
                <div class="CuadriculaFormulario">
                  <div class="GrupoEntrada">
                    <label class="EtiquetaCampo">Correo Electrónico</label>
                    <input v-model="Formulario.correoElectronico" type="email" class="CampoEntrada" placeholder="correo@ejemplo.com" />
                  </div>
                  <div class="GrupoEntrada">
                    <label class="EtiquetaCampo">Celular</label>
                    <input v-model="Formulario.celular" type="text" class="CampoEntrada" placeholder="Número de celular" />
                  </div>
                </div>

                <div class="SeparadorSeccion" />

                <p class="EtiquetaSeccionModal">Dirección</p>
                <div class="CuadriculaFormulario">
                  <div class="GrupoEntrada">
                    <label class="EtiquetaCampo">País</label>
                    <input v-model="Formulario.pais" type="text" class="CampoEntrada" placeholder="País" />
                  </div>
                  <div class="GrupoEntrada">
                    <label class="EtiquetaCampo">Ciudad</label>
                    <input v-model="Formulario.ciudad" type="text" class="CampoEntrada" placeholder="Ciudad" />
                  </div>
                  <div class="GrupoEntrada">
                    <label class="EtiquetaCampo">Zona</label>
                    <input v-model="Formulario.zona" type="text" class="CampoEntrada" placeholder="Zona o barrio" />
                  </div>
                  <div class="GrupoEntrada">
                    <label class="EtiquetaCampo">Dirección</label>
                    <input v-model="Formulario.direccion" type="text" class="CampoEntrada" placeholder="Dirección exacta" />
                  </div>
                  <div class="GrupoEntrada GrupoEntradaCompleto">
                    <label class="EtiquetaCampo">Referencia</label>
                    <input v-model="Formulario.referencia" type="text" class="CampoEntrada" placeholder="Referencia de ubicación" />
                  </div>
                </div>

                <div class="SeparadorSeccion" />

                <p class="EtiquetaSeccionModal">Acceso al Sistema</p>
                <div class="CuadriculaFormulario">
                  <div class="GrupoEntrada GrupoEntradaContrasena">
                    <label class="EtiquetaCampo">
                      {{ EsEdicion ? 'Nueva Contraseña (dejar vacío para no cambiar)' : 'Contraseña *' }}
                    </label>
                    <div class="ContenedorEntradaContrasena">
                      <input
                        v-model="Formulario.contrasena"
                        :type="MostrarContrasena ? 'text' : 'password'"
                        class="CampoEntrada"
                        :placeholder="EsEdicion ? 'Nueva contraseña (opcional)' : 'Contraseña'"
                      />
                      <button type="button" class="BotonVerContrasena" @click="MostrarContrasena = !MostrarContrasena">
                        <svg v-if="!MostrarContrasena" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                          <path d="M1 12s4-8 11-8 11 8 11 8-4 8-11 8-11-8-11-8z"/><circle cx="12" cy="12" r="3"/>
                        </svg>
                        <svg v-else width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                          <path d="M17.94 17.94A10.07 10.07 0 0 1 12 20c-7 0-11-8-11-8a18.45 18.45 0 0 1 5.06-5.94M9.9 4.24A9.12 9.12 0 0 1 12 4c7 0 11 8 11 8a18.5 18.5 0 0 1-2.16 3.19m-6.72-1.07a3 3 0 1 1-4.24-4.24"/>
                          <line x1="1" y1="1" x2="23" y2="23"/>
                        </svg>
                      </button>
                    </div>
                  </div>
                  <div class="GrupoEntrada">
                    <label class="EtiquetaCampo">Rol *</label>
                    <select v-model="Formulario.idRol" class="CampoEntrada">
                      <option value="">Seleccionar rol</option>
                      <option v-for="Rol in ListaRoles" :key="Rol.idRol" :value="Rol.idRol">{{ Rol.nombre }}</option>
                    </select>
                  </div>
                </div>

              </div>
            </div>
          </div>

          <div class="PieModal">
            <button class="BotonSecundario" @click="CerrarModal">Cancelar</button>
            <button class="BotonPrimario" :disabled="EstaGuardando" @click="GuardarUsuario">
              <span v-if="!EstaGuardando">{{ EsEdicion ? 'Actualizar Usuario' : 'Guardar Usuario' }}</span>
              <span v-else>Guardando...</span>
            </button>
          </div>

        </div>
      </div>
    </Teleport>

    <Teleport to="body">
      <div v-if="ModalDetalleVisible" class="OverlayModal" @click.self="ModalDetalleVisible = false">
        <div class="ContenedorModal" style="max-width:580px">

          <div class="CabeceraModal">
            <div class="TituloModal">
              <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="#c4912a" stroke-width="2">
                <circle cx="12" cy="12" r="10"/>
                <line x1="12" y1="16" x2="12" y2="12"/>
                <line x1="12" y1="8" x2="12.01" y2="8"/>
              </svg>
              Detalle del Usuario
            </div>
            <button class="BotonCerrarModal" @click="ModalDetalleVisible = false">
              <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                <line x1="18" y1="6" x2="6" y2="18"/>
                <line x1="6" y1="6" x2="18" y2="18"/>
              </svg>
            </button>
          </div>

          <div v-if="UsuarioDetalle" class="CuerpoModal">

            <div class="CabeceraPerfil">
              <div class="AvatarDetalle">
                <img v-if="UsuarioDetalle.fotoPerfil" :src="`http://localhost:8080${UsuarioDetalle.fotoPerfil}`" alt="foto" />
                <span v-else>{{ (UsuarioDetalle.nombre || '?').charAt(0).toUpperCase() }}</span>
              </div>
              <div class="InfoDetallePerfil">
                <h2 class="NombreDetalle">
                  {{ UsuarioDetalle.nombre }} {{ UsuarioDetalle.apellidoPaterno }} {{ UsuarioDetalle.apellidoMaterno }}
                </h2>
                <p class="UsernameDetalle">@{{ UsuarioDetalle.nombreUsuario }}</p>
                <span class="InsigniaEstado" :class="UsuarioDetalle.activo ? 'InsigniaEstado--Activo' : 'InsigniaEstado--Inactivo'">
                  {{ UsuarioDetalle.activo ? 'Activo' : 'Inactivo' }}
                </span>
              </div>
            </div>

            <div class="SeparadorDetalle" />

            <div class="CuadriculaDetalle">
              <div class="ItemDetalle">
                <span class="EtiquetaDetalle">Sexo</span>
                <span class="ValorDetalle">{{ UsuarioDetalle.sexo === 'M' ? 'Masculino' : UsuarioDetalle.sexo === 'F' ? 'Femenino' : '-' }}</span>
              </div>
              <div class="ItemDetalle">
                <span class="EtiquetaDetalle">Fecha de Nacimiento</span>
                <span class="ValorDetalle">{{ UsuarioDetalle.fechaDeNacimiento || '-' }}</span>
              </div>
              <div class="ItemDetalle">
                <span class="EtiquetaDetalle">Rol</span>
                <span class="ValorDetalle">{{ UsuarioDetalle.nombreRol || '-' }}</span>
              </div>
            </div>

            <div class="SeparadorDetalle" />

            <div class="CuadriculaDetalle">
              <div class="ItemDetalle ItemDetalleCompleto">
                <span class="EtiquetaDetalle">Correo Electrónico</span>
                <span class="ValorDetalle">{{ UsuarioDetalle.correoElectronico || '-' }}</span>
              </div>
              <div class="ItemDetalle">
                <span class="EtiquetaDetalle">Celular</span>
                <span class="ValorDetalle">{{ UsuarioDetalle.celular || '-' }}</span>
              </div>
            </div>

            <div class="SeparadorDetalle" />

            <div class="CuadriculaDetalle">
              <div class="ItemDetalle">
                <span class="EtiquetaDetalle">País</span>
                <span class="ValorDetalle">{{ UsuarioDetalle.pais || '-' }}</span>
              </div>
              <div class="ItemDetalle">
                <span class="EtiquetaDetalle">Ciudad</span>
                <span class="ValorDetalle">{{ UsuarioDetalle.ciudad || '-' }}</span>
              </div>
              <div class="ItemDetalle">
                <span class="EtiquetaDetalle">Zona</span>
                <span class="ValorDetalle">{{ UsuarioDetalle.zona || '-' }}</span>
              </div>
              <div class="ItemDetalle ItemDetalleCompleto">
                <span class="EtiquetaDetalle">Dirección</span>
                <span class="ValorDetalle">{{ UsuarioDetalle.direccion || '-' }}</span>
              </div>
              <div class="ItemDetalle ItemDetalleCompleto">
                <span class="EtiquetaDetalle">Referencia</span>
                <span class="ValorDetalle">{{ UsuarioDetalle.referencia || '-' }}</span>
              </div>
            </div>

            <div class="SeparadorDetalle" />

            <div class="CuadriculaDetalle">
              <div class="ItemDetalle">
                <span class="EtiquetaDetalle">Fecha de Creación</span>
                <span class="ValorDetalle">{{ FormatearFecha(UsuarioDetalle.fechaCreacion) }}</span>
              </div>
              <div class="ItemDetalle">
                <span class="EtiquetaDetalle">Última Actualización</span>
                <span class="ValorDetalle">{{ FormatearFecha(UsuarioDetalle.fechaActualizacion) }}</span>
              </div>
            </div>

          </div>

        </div>
      </div>
    </Teleport>

    <Teleport to="body">
      <div v-if="ModalConfirmacionVisible" class="OverlayModal" @click.self="ModalConfirmacionVisible = false">
        <div class="ContenedorModal" style="max-width:440px">

          <div class="CabeceraModal">
            <div class="TituloModal TituloModalPeligro">
              <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="#e87c7c" stroke-width="2">
                <circle cx="12" cy="12" r="10"/>
                <line x1="12" y1="8" x2="12" y2="12"/>
                <line x1="12" y1="16" x2="12.01" y2="16"/>
              </svg>
              Confirmar Eliminación
            </div>
          </div>

          <div class="CuerpoModal">
            <p class="TextoConfirmacion">
              ¿Estás seguro de eliminar al usuario
              <strong>{{ UsuarioAEliminar?.nombreUsuario }}</strong>?
              Se eliminarán todos sus datos. Esta acción no se puede deshacer.
            </p>
          </div>

          <div class="PieModal">
            <button class="BotonSecundario" @click="ModalConfirmacionVisible = false">Cancelar</button>
            <button class="BotonEliminarModal" :disabled="EstaEliminando" @click="EjecutarEliminar">
              <span v-if="!EstaEliminando">Eliminar</span>
              <span v-else>Eliminando...</span>
            </button>
          </div>

        </div>
      </div>
    </Teleport>

  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { usuarioApi } from '@/api/usuario.api'
import { rolApi } from '@/api/rol.api'
import { useNotificacion } from '@/composables/useNotificacion'

const { MostrarNotificacion } = useNotificacion()

const ListaUsuarios = ref([])
const ListaRoles = ref([])
const EstaCargando = ref(false)
const EstaGuardando = ref(false)
const EstaEliminando = ref(false)
const ModalFormularioVisible = ref(false)
const ModalDetalleVisible = ref(false)
const ModalConfirmacionVisible = ref(false)
const EsEdicion = ref(false)
const UsuarioDetalle = ref(null)
const UsuarioAEliminar = ref(null)
const FotoSeleccionada = ref(null)
const VistasPreviaFoto = ref(null)
const MostrarContrasena = ref(false)
const TextoBusqueda = ref('')
const FiltroRolSeleccionado = ref('')
const FiltroEstadoSeleccionado = ref('')

const FormularioVacio = () => ({
  idUsuario: null,
  nombre: '',
  apellidoPaterno: '',
  apellidoMaterno: '',
  sexo: '',
  fechaDeNacimiento: null,
  correoElectronico: '',
  celular: '',
  pais: '',
  ciudad: '',
  zona: '',
  direccion: '',
  referencia: '',
  contrasena: '',
  idRol: ''
})

const Formulario = ref(FormularioVacio())

const UsuariosFiltrados = computed(() => {
  let Resultado = ListaUsuarios.value

  if (TextoBusqueda.value) {
    const TextoMinusculas = TextoBusqueda.value.toLowerCase()
    Resultado = Resultado.filter((Usuario) =>
      Usuario.nombreUsuario?.toLowerCase().includes(TextoMinusculas) ||
      Usuario.nombre?.toLowerCase().includes(TextoMinusculas) ||
      Usuario.apellidoPaterno?.toLowerCase().includes(TextoMinusculas) ||
      Usuario.correoElectronico?.toLowerCase().includes(TextoMinusculas)
    )
  }

  if (FiltroRolSeleccionado.value) {
    Resultado = Resultado.filter(
      (Usuario) => Usuario.idRol === parseInt(FiltroRolSeleccionado.value)
    )
  }

  if (FiltroEstadoSeleccionado.value !== '') {
    const EstadoBuscado = FiltroEstadoSeleccionado.value === 'true'
    Resultado = Resultado.filter((Usuario) => Usuario.activo === EstadoBuscado)
  }

  return Resultado
})

const CargarUsuarios = async () => {
  EstaCargando.value = true
  try {
    ListaUsuarios.value = await usuarioApi.listarTodos()
  } catch {
    MostrarNotificacion('Error al cargar los usuarios', 'error')
  } finally {
    EstaCargando.value = false
  }
}

const AbrirModalCrear = () => {
  EsEdicion.value = false
  Formulario.value = FormularioVacio()
  FotoSeleccionada.value = null
  VistasPreviaFoto.value = null
  MostrarContrasena.value = false
  ModalFormularioVisible.value = true
}

const AbrirModalEditar = (Usuario) => {
  EsEdicion.value = true
  Formulario.value = {
    idUsuario: Usuario.idUsuario,
    nombre: Usuario.nombre || '',
    apellidoPaterno: Usuario.apellidoPaterno || '',
    apellidoMaterno: Usuario.apellidoMaterno || '',
    sexo: Usuario.sexo || '',
    fechaDeNacimiento: Usuario.fechaDeNacimiento || null,
    correoElectronico: Usuario.correoElectronico || '',
    celular: Usuario.celular || '',
    pais: Usuario.pais || '',
    ciudad: Usuario.ciudad || '',
    zona: Usuario.zona || '',
    direccion: Usuario.direccion || '',
    referencia: Usuario.referencia || '',
    contrasena: '',
    idRol: Usuario.idRol || ''
  }
  FotoSeleccionada.value = null
  VistasPreviaFoto.value = Usuario.fotoPerfil ? `http://localhost:8080${Usuario.fotoPerfil}` : null
  MostrarContrasena.value = false
  ModalFormularioVisible.value = true
}

const CerrarModal = () => {
  ModalFormularioVisible.value = false
}

const ManejarSeleccionFoto = (Evento) => {
  const Archivo = Evento.target.files[0]
  if (Archivo) {
    FotoSeleccionada.value = Archivo
    const LectorArchivo = new FileReader()
    LectorArchivo.onload = (EventoLector) => {
      VistasPreviaFoto.value = EventoLector.target.result
    }
    LectorArchivo.readAsDataURL(Archivo)
  }
}

const GuardarUsuario = async () => {
  if (!Formulario.value.nombre || !Formulario.value.apellidoPaterno) {
    MostrarNotificacion('El nombre y apellido paterno son requeridos', 'advertencia')
    return
  }
  if (!EsEdicion.value && !Formulario.value.contrasena) {
    MostrarNotificacion('La contraseña es requerida para nuevos usuarios', 'advertencia')
    return
  }
  if (!Formulario.value.idRol) {
    MostrarNotificacion('Debes asignar un rol al usuario', 'advertencia')
    return
  }

  EstaGuardando.value = true
  try {
    const DatosEnvio = { ...Formulario.value }
    if (!DatosEnvio.contrasena) delete DatosEnvio.contrasena

    if (EsEdicion.value) {
      await usuarioApi.actualizar(Formulario.value.idUsuario, DatosEnvio, FotoSeleccionada.value)
      MostrarNotificacion('Usuario actualizado correctamente')
    } else {
      await usuarioApi.guardar(DatosEnvio, FotoSeleccionada.value)
      MostrarNotificacion('Usuario creado correctamente')
    }
    ModalFormularioVisible.value = false
    await CargarUsuarios()
  } catch {
    MostrarNotificacion('Error al guardar el usuario', 'error')
  } finally {
    EstaGuardando.value = false
  }
}

const AbrirDetalle = (Usuario) => {
  UsuarioDetalle.value = Usuario
  ModalDetalleVisible.value = true
}

const CambiarEstadoUsuario = async (Usuario) => {
  try {
    await usuarioApi.cambiarEstado(Usuario.idUsuario)
    MostrarNotificacion(`Usuario ${Usuario.activo ? 'desactivado' : 'activado'} correctamente`)
    await CargarUsuarios()
  } catch {
    MostrarNotificacion('Error al cambiar el estado del usuario', 'error')
  }
}

const ConfirmarEliminar = (Usuario) => {
  UsuarioAEliminar.value = Usuario
  ModalConfirmacionVisible.value = true
}

const EjecutarEliminar = async () => {
  EstaEliminando.value = true
  try {
    await usuarioApi.eliminar(UsuarioAEliminar.value.idUsuario)
    MostrarNotificacion('Usuario eliminado correctamente')
    ModalConfirmacionVisible.value = false
    await CargarUsuarios()
  } catch {
    MostrarNotificacion('Error al eliminar el usuario', 'error')
  } finally {
    EstaEliminando.value = false
  }
}

const FormatearFecha = (FechaISO) => {
  if (!FechaISO) return '-'
  return new Date(FechaISO).toLocaleString('es-ES')
}

onMounted(async () => {
  const [UsuariosObtenidos, RolesObtenidos] = await Promise.all([
    usuarioApi.listarTodos(),
    rolApi.listarTodos()
  ])
  ListaUsuarios.value = UsuariosObtenidos
  ListaRoles.value = RolesObtenidos
})
</script>
<style>
@import '@/style/globals.css';
</style>
<style scoped>

.VistaUsuarios { 
  padding: 32px;
  min-height: 100vh;
  background-color: var(--bg-light);
  font-family: 'Inter', sans-serif;
}

/* Estilos específicos para Usuarios que no están en globals */
.SelectorFiltro {
  padding: 10px 14px;
  background-color: var(--color-white);
  border: 1px solid #e2e8f0;
  border-radius: 6px;
  color: var(--color-gray-dark);
  font-family: 'Inter', sans-serif;
  font-size: 14px;
  outline: none;
  cursor: pointer;
  min-width: 160px;
}

.SelectorFiltro option { background-color: var(--color-white); }

.CeldaUsername { font-weight: 600; color: var(--color-primary-light); }

.FilaModalUsuario {
  display: flex;
  gap: 28px;
}

.ContenedorEntradaContrasena .CampoEntrada { padding-right: 44px; }

@media (max-width: 768px) {
  .VistaUsuarios { padding: 20px; }
  .FilaModalUsuario { flex-direction: column; }
  .ColumnaFotoModal { width: 100%; }
  .CuadriculaFormulario { grid-template-columns: 1fr; }
  .GrupoEntradaContrasena { grid-column: auto; }
}
</style>
