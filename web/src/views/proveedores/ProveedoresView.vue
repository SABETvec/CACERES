<template>
  <div class="VistaProveedores">

    <div class="CabeceraVista">
      <div>
        <h1 class="TituloVista">Gestión de Proveedores</h1>
        <p class="SubtituloVista">Administra la cartera de proveedores</p>
      </div>
      <button class="BotonPrimario" @click="AbrirModalCrear">
        <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
          <line x1="12" y1="5" x2="12" y2="19"/>
          <line x1="5" y1="12" x2="19" y2="12"/>
        </svg>
        Nuevo Proveedor
      </button>
    </div>

    <div class="FilaFiltros">
      <div class="ContenedorBusqueda">
        <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="#94a3b8" stroke-width="2">
          <circle cx="11" cy="11" r="8"/>
          <line x1="21" y1="21" x2="16.65" y2="16.65"/>
        </svg>
        <input v-model="TextoBusqueda" type="text" class="EntradaBusqueda" placeholder="Buscar proveedor..." />
      </div>
    </div>

    <div class="ContenedorTabla">
      <div v-if="EstaCargando" class="EstadoCargando">
        <div class="AnimadorCarga" />
        <span>Cargando proveedores...</span>
      </div>

      <table v-else class="TablaEstilos">
        <thead>
          <tr>
            <th class="EncabezadoTabla"></th>
            <th class="EncabezadoTabla">Nombre</th>
            <th class="EncabezadoTabla">Correo</th>
            <th class="EncabezadoTabla">Celular</th>
            <th class="EncabezadoTabla">Especialidad</th>
            <th class="EncabezadoTabla" style="text-align:center">Acciones</th>
          </tr>
        </thead>
        <tbody>
          <tr v-if="ProveedoresFiltrados.length === 0">
            <td colspan="6" class="FilaVacia">No hay proveedores registrados</td>
          </tr>
          <tr v-for="Proveedor in ProveedoresFiltrados" :key="Proveedor.idProveedor" class="FilaTabla">
            <td class="CeldaTabla CeldaAvatar">
              <div class="AvatarTabla">
                <img v-if="Proveedor.fotoPerfil" :src="`http://localhost:8080${Proveedor.fotoPerfil}`" alt="foto" />
                <span v-else>{{ (Proveedor.nombre || '?').charAt(0).toUpperCase() }}</span>
              </div>
            </td>
            <td class="CeldaTabla">
              <div class="NombreCompleto">{{ Proveedor.nombre }} {{ Proveedor.apellidoPaterno }}</div>
              <div class="ApellidoSecundario">{{ Proveedor.apellidoMaterno }}</div>
            </td>
            <td class="CeldaTabla CeldaSecundaria">{{ Proveedor.correoElectronico || '-' }}</td>
            <td class="CeldaTabla CeldaSecundaria">{{ Proveedor.celular || '-' }}</td>
            <td class="CeldaTabla">
              <span v-if="Proveedor.descripcion" class="InsigniaEspecialidad">{{ Proveedor.descripcion }}</span>
              <span v-else class="CeldaSecundaria">-</span>
            </td>
            <td class="CeldaTabla CeldaAcciones">
              <button class="BotonIcono BotonDetalle" @click="AbrirDetalle(Proveedor)" title="Ver detalle">
                <svg width="15" height="15" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                  <path d="M1 12s4-8 11-8 11 8 11 8-4 8-11 8-11-8-11-8z"/>
                  <circle cx="12" cy="12" r="3"/>
                </svg>
              </button>
              <button class="BotonIcono BotonEditar" @click="AbrirModalEditar(Proveedor)" title="Editar">
                <svg width="15" height="15" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                  <path d="M11 4H4a2 2 0 0 0-2 2v14a2 2 0 0 0 2 2h14a2 2 0 0 0 2-2v-7"/>
                  <path d="M18.5 2.5a2.121 2.121 0 0 1 3 3L12 15l-4 1 1-4 9.5-9.5z"/>
                </svg>
              </button>
              <button class="BotonIcono BotonEliminar" @click="ConfirmarEliminar(Proveedor)" title="Eliminar">
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
        <div class="ContenedorModal" style="max-width:760px;max-height:92vh">

          <div class="CabeceraModal">
            <div class="TituloModal">{{ EsEdicion ? 'Editar Proveedor' : 'Nuevo Proveedor' }}</div>
            <button class="BotonCerrarModal" @click="CerrarModal">
              <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                <line x1="18" y1="6" x2="6" y2="18"/><line x1="6" y1="6" x2="18" y2="18"/>
              </svg>
            </button>
          </div>

          <div class="CuerpoModal" style="overflow-y:auto;max-height:calc(92vh - 130px)">
            <div class="FilaModalFormulario">

              <div class="ColumnaFotoModal">
                <p class="EtiquetaSeccionModal">Foto</p>
                <div class="ContenedorFoto">
                  <div class="VistasPreviaFoto">
                    <img v-if="VistasPreviaFoto" :src="VistasPreviaFoto" alt="vista previa" />
                    <span v-else class="PlaceholderFoto">
                      <svg width="32" height="32" viewBox="0 0 24 24" fill="none" stroke="#94a3b8" stroke-width="1.5">
                        <path d="M20 21v-2a4 4 0 0 0-4-4H8a4 4 0 0 0-4 4v2"/>
                        <circle cx="12" cy="7" r="4"/>
                      </svg>
                    </span>
                  </div>
                  <label class="BotonSeleccionarFoto">
                    <svg width="13" height="13" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
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
                  <div class="GrupoEntrada GrupoEntradaCompleto">
                    <label class="EtiquetaCampo">Especialidad / Descripción</label>
                    <input v-model="Formulario.descripcion" type="text" class="CampoEntrada" placeholder="Ej: Papelería, Útiles escolares, Impresión..." />
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
                    <input v-model="Formulario.celular" type="text" class="CampoEntrada" placeholder="Celular" />
                  </div>
                </div>

              </div>
            </div>
          </div>

          <div class="PieModal">
            <button class="BotonSecundario" @click="CerrarModal">Cancelar</button>
            <button class="BotonPrimario" :disabled="EstaGuardando" @click="GuardarProveedor">
              <span v-if="!EstaGuardando">{{ EsEdicion ? 'Actualizar Proveedor' : 'Guardar Proveedor' }}</span>
              <span v-else>Guardando...</span>
            </button>
          </div>

        </div>
      </div>
    </Teleport>

    <Teleport to="body">
      <div v-if="ModalDetalleVisible" class="OverlayModal" @click.self="ModalDetalleVisible = false">
        <div class="ContenedorModal" style="max-width:520px">
          <div class="CabeceraModal">
            <div class="TituloModal">Detalle del Proveedor</div>
            <button class="BotonCerrarModal" @click="ModalDetalleVisible = false">
              <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                <line x1="18" y1="6" x2="6" y2="18"/><line x1="6" y1="6" x2="18" y2="18"/>
              </svg>
            </button>
          </div>

          <div v-if="ProveedorDetalle" class="CuerpoModal">
            <div class="CabeceraPerfil">
              <div class="AvatarDetalle">
                <img v-if="ProveedorDetalle.fotoPerfil" :src="`http://localhost:8080${ProveedorDetalle.fotoPerfil}`" alt="foto" />
                <span v-else>{{ (ProveedorDetalle.nombre || '?').charAt(0).toUpperCase() }}</span>
              </div>
              <div>
                <h2 class="NombreDetalle">{{ ProveedorDetalle.nombre }} {{ ProveedorDetalle.apellidoPaterno }} {{ ProveedorDetalle.apellidoMaterno }}</h2>
                <div v-if="ProveedorDetalle.descripcion" class="InsigniaEspecialidad" style="margin-top:6px">{{ ProveedorDetalle.descripcion }}</div>
              </div>
            </div>

            <div class="SeparadorDetalle" />

            <div class="CuadriculaDetalle">
              <div class="ItemDetalle">
                <span class="EtiquetaDetalle">Sexo</span>
                <span class="ValorDetalle">{{ ProveedorDetalle.sexo === 'M' ? 'Masculino' : ProveedorDetalle.sexo === 'F' ? 'Femenino' : '-' }}</span>
              </div>
              <div class="ItemDetalle">
                <span class="EtiquetaDetalle">Fecha de Nacimiento</span>
                <span class="ValorDetalle">{{ ProveedorDetalle.fechaDeNacimiento || '-' }}</span>
              </div>
              <div class="ItemDetalle ItemDetalleCompleto">
                <span class="EtiquetaDetalle">Correo Electrónico</span>
                <span class="ValorDetalle">{{ ProveedorDetalle.correoElectronico || '-' }}</span>
              </div>
              <div class="ItemDetalle">
                <span class="EtiquetaDetalle">Celular</span>
                <span class="ValorDetalle">{{ ProveedorDetalle.celular || '-' }}</span>
              </div>
              <div class="ItemDetalle">
                <span class="EtiquetaDetalle">Fecha de Registro</span>
                <span class="ValorDetalle">{{ FormatearFecha(ProveedorDetalle.fechaCreacion) }}</span>
              </div>
              <div class="ItemDetalle">
                <span class="EtiquetaDetalle">Última Actualización</span>
                <span class="ValorDetalle">{{ FormatearFecha(ProveedorDetalle.fechaActualizacion) }}</span>
              </div>
            </div>
          </div>
        </div>
      </div>
    </Teleport>

    <Teleport to="body">
      <div v-if="ModalConfirmacionVisible" class="OverlayModal" @click.self="ModalConfirmacionVisible = false">
        <div class="ContenedorModal" style="max-width:420px">
          <div class="CabeceraModal">
            <div class="TituloModal TituloModalPeligro">Confirmar Eliminación</div>
          </div>
          <div class="CuerpoModal">
            <p class="TextoConfirmacion">
              ¿Eliminar al proveedor <strong>{{ ProveedorAEliminar?.nombre }} {{ ProveedorAEliminar?.apellidoPaterno }}</strong>?
              Esta acción no se puede deshacer.
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
import { proveedorApi } from '@/api/proveedor.api'
import { useNotificacion } from '@/composables/useNotificacion'

const { MostrarNotificacion } = useNotificacion()

const ListaProveedores = ref([])
const EstaCargando = ref(false)
const EstaGuardando = ref(false)
const EstaEliminando = ref(false)
const ModalFormularioVisible = ref(false)
const ModalDetalleVisible = ref(false)
const ModalConfirmacionVisible = ref(false)
const EsEdicion = ref(false)
const ProveedorDetalle = ref(null)
const ProveedorAEliminar = ref(null)
const FotoSeleccionada = ref(null)
const VistasPreviaFoto = ref(null)
const TextoBusqueda = ref('')

const FormularioVacio = () => ({
  idProveedor: null,
  nombre: '', apellidoPaterno: '', apellidoMaterno: '',
  sexo: '', fechaDeNacimiento: null, descripcion: '',
  correoElectronico: '', celular: ''
})

const Formulario = ref(FormularioVacio())

const ProveedoresFiltrados = computed(() => {
  if (!TextoBusqueda.value) return ListaProveedores.value
  const Texto = TextoBusqueda.value.toLowerCase()
  return ListaProveedores.value.filter((Proveedor) =>
    Proveedor.nombre?.toLowerCase().includes(Texto) ||
    Proveedor.apellidoPaterno?.toLowerCase().includes(Texto) ||
    Proveedor.correoElectronico?.toLowerCase().includes(Texto) ||
    Proveedor.descripcion?.toLowerCase().includes(Texto)
  )
})

const CargarProveedores = async () => {
  EstaCargando.value = true
  try {
    ListaProveedores.value = await proveedorApi.listarTodos()
  } catch {
    MostrarNotificacion('Error al cargar los proveedores', 'error')
  } finally {
    EstaCargando.value = false
  }
}

const AbrirModalCrear = () => {
  EsEdicion.value = false
  Formulario.value = FormularioVacio()
  FotoSeleccionada.value = null
  VistasPreviaFoto.value = null
  ModalFormularioVisible.value = true
}

const AbrirModalEditar = (Proveedor) => {
  EsEdicion.value = true
  Formulario.value = {
    idProveedor: Proveedor.idProveedor,
    nombre: Proveedor.nombre || '',
    apellidoPaterno: Proveedor.apellidoPaterno || '',
    apellidoMaterno: Proveedor.apellidoMaterno || '',
    sexo: Proveedor.sexo || '',
    fechaDeNacimiento: Proveedor.fechaDeNacimiento || null,
    descripcion: Proveedor.descripcion || '',
    correoElectronico: Proveedor.correoElectronico || '',
    celular: Proveedor.celular || ''
  }
  FotoSeleccionada.value = null
  VistasPreviaFoto.value = Proveedor.fotoPerfil ? `http://localhost:8080${Proveedor.fotoPerfil}` : null
  ModalFormularioVisible.value = true
}

const CerrarModal = () => { ModalFormularioVisible.value = false }

const ManejarSeleccionFoto = (Evento) => {
  const Archivo = Evento.target.files[0]
  if (Archivo) {
    FotoSeleccionada.value = Archivo
    const LectorArchivo = new FileReader()
    LectorArchivo.onload = (E) => { VistasPreviaFoto.value = E.target.result }
    LectorArchivo.readAsDataURL(Archivo)
  }
}

const GuardarProveedor = async () => {
  if (!Formulario.value.nombre || !Formulario.value.apellidoPaterno) {
    MostrarNotificacion('El nombre y apellido paterno son requeridos', 'advertencia')
    return
  }
  EstaGuardando.value = true
  try {
    if (EsEdicion.value) {
      await proveedorApi.actualizar(Formulario.value.idProveedor, Formulario.value, FotoSeleccionada.value)
      MostrarNotificacion('Proveedor actualizado correctamente')
    } else {
      await proveedorApi.guardar(Formulario.value, FotoSeleccionada.value)
      MostrarNotificacion('Proveedor registrado correctamente')
    }
    ModalFormularioVisible.value = false
    await CargarProveedores()
  } catch {
    MostrarNotificacion('Error al guardar el proveedor', 'error')
  } finally {
    EstaGuardando.value = false
  }
}

const AbrirDetalle = (Proveedor) => {
  ProveedorDetalle.value = Proveedor
  ModalDetalleVisible.value = true
}

const ConfirmarEliminar = (Proveedor) => {
  ProveedorAEliminar.value = Proveedor
  ModalConfirmacionVisible.value = true
}

const EjecutarEliminar = async () => {
  EstaEliminando.value = true
  try {
    await proveedorApi.eliminar(ProveedorAEliminar.value.idProveedor)
    MostrarNotificacion('Proveedor eliminado correctamente')
    ModalConfirmacionVisible.value = false
    await CargarProveedores()
  } catch {
    MostrarNotificacion('Error al eliminar el proveedor', 'error')
  } finally {
    EstaEliminando.value = false
  }
}

const FormatearFecha = (FechaISO) => {
  if (!FechaISO) return '-'
  return new Date(FechaISO).toLocaleString('es-ES')
}

onMounted(CargarProveedores)
</script>
<style>
@import '@/style/globals.css';
</style>
<style scoped>

.VistaProveedores {
  padding: 32px;
  min-height: 100vh;
  background-color: #f0f6ff;
  font-family: 'Inter', sans-serif;
}

.CabeceraVista {
  display: flex;
  align-items: flex-start;
  justify-content: space-between;
  margin-bottom: 24px;
  gap: 16px;
}

.TituloVista {
  font-family: 'Playfair Display', serif;
  font-size: 26px;
  font-weight: 700;
  color: #0f172a;
  margin-bottom: 4px;
}

.SubtituloVista { font-size: 14px; color: #64748b; }

.BotonPrimario {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 10px 20px;
  background: linear-gradient(135deg, #0369a1, #0ea5e9);
  border: none;
  border-radius: 8px;
  color: #ffffff;
  font-family: 'Inter', sans-serif;
  font-size: 14px;
  font-weight: 600;
  cursor: pointer;
  transition: transform 0.2s, box-shadow 0.2s, opacity 0.2s;
  white-space: nowrap;
  flex-shrink: 0;
  box-shadow: 0 4px 14px rgba(3, 105, 161, 0.35);
}

.BotonPrimario:hover:not(:disabled) {
  transform: translateY(-1px);
  box-shadow: 0 6px 20px rgba(3, 105, 161, 0.45);
}

.BotonPrimario:disabled { opacity: 0.6; cursor: not-allowed; }

.BotonSecundario {
  padding: 10px 20px;
  background: transparent;
  border: 1px solid #cbd5e1;
  border-radius: 8px;
  color: #64748b;
  font-family: 'Inter', sans-serif;
  font-size: 14px;
  cursor: pointer;
  transition: border-color 0.2s, color 0.2s;
}

.BotonSecundario:hover { border-color: #0ea5e9; color: #0ea5e9; }

.FilaFiltros { display: flex; gap: 12px; margin-bottom: 20px; }

.ContenedorBusqueda {
  display: flex;
  align-items: center;
  gap: 10px;
  background-color: #ffffff;
  border: 1px solid #e2e8f0;
  border-radius: 8px;
  padding: 0 14px;
  flex: 1;
  max-width: 380px;
  box-shadow: 0 1px 4px rgba(0,0,0,0.06);
}

.EntradaBusqueda {
  flex: 1;
  background: transparent;
  border: none;
  outline: none;
  padding: 11px 0;
  color: #0f172a;
  font-family: 'Inter', sans-serif;
  font-size: 14px;
}

.EntradaBusqueda::placeholder { color: #94a3b8; }

.ContenedorTabla {
  background-color: #ffffff;
  border: 1px solid #e2e8f0;
  border-radius: 12px;
  overflow: hidden;
  box-shadow: 0 2px 12px rgba(0,0,0,0.06);
}

.TablaEstilos { width: 100%; border-collapse: collapse; }

.EncabezadoTabla {
  padding: 14px 16px;
  background-color: #f0f9ff;
  font-family: 'Inter', sans-serif;
  font-size: 11px;
  font-weight: 600;
  letter-spacing: 1px;
  text-transform: uppercase;
  color: #0369a1;
  text-align: left;
  border-bottom: 1px solid #bae6fd;
}

.FilaTabla {
  border-bottom: 1px solid #f1f5f9;
  transition: background-color 0.15s;
}

.FilaTabla:hover { background-color: #f0f9ff; }
.FilaTabla:last-child { border-bottom: none; }

.CeldaTabla {
  padding: 14px 16px;
  font-family: 'Inter', sans-serif;
  font-size: 14px;
  color: #1e293b;
}

.CeldaAvatar { width: 56px; }

.AvatarTabla {
  width: 38px;
  height: 38px;
  border-radius: 50%;
  background: linear-gradient(135deg, #e0f2fe, #bae6fd);
  border: 2px solid #7dd3fc;
  display: flex;
  align-items: center;
  justify-content: center;
  overflow: hidden;
  font-family: 'Inter', sans-serif;
  font-size: 14px;
  font-weight: 700;
  color: #0369a1;
}

.AvatarTabla img { width: 100%; height: 100%; object-fit: cover; }

.NombreCompleto { font-weight: 600; color: #0f172a; }
.ApellidoSecundario { font-size: 12px; color: #94a3b8; margin-top: 2px; }
.CeldaSecundaria { color: #64748b; }

.InsigniaEspecialidad {
  display: inline-block;
  padding: 4px 10px;
  background-color: #f0f9ff;
  border: 1px solid #bae6fd;
  border-radius: 20px;
  font-size: 13px;
  font-weight: 500;
  color: #0369a1;
}

.CeldaAcciones { text-align: center; white-space: nowrap; }

.BotonIcono {
  width: 32px;
  height: 32px;
  display: inline-flex;
  align-items: center;
  justify-content: center;
  border: 1px solid transparent;
  border-radius: 6px;
  cursor: pointer;
  transition: all 0.2s;
  margin: 0 2px;
}

.BotonDetalle { background-color: #f0f9ff; border-color: #bae6fd; color: #0369a1; }
.BotonDetalle:hover { background-color: #e0f2fe; }
.BotonEditar { background-color: #fefce8; border-color: #fde68a; color: #d97706; }
.BotonEditar:hover { background-color: #fef9c3; }
.BotonEliminar { background-color: #fef2f2; border-color: #fecaca; color: #dc2626; }
.BotonEliminar:hover { background-color: #fee2e2; }

.FilaVacia { padding: 40px; text-align: center; font-size: 15px; color: #94a3b8; font-style: italic; }

.EstadoCargando {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 12px;
  padding: 40px;
  font-size: 15px;
  color: #64748b;
}

.AnimadorCarga {
  width: 24px;
  height: 24px;
  border: 2px solid #e2e8f0;
  border-top-color: #0ea5e9;
  border-radius: 50%;
  animation: Girar 0.8s linear infinite;
}

@keyframes Girar { to { transform: rotate(360deg); } }

.OverlayModal {
  position: fixed;
  inset: 0;
  background-color: rgba(15, 23, 42, 0.5);
  z-index: 200;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 20px;
}

.ContenedorModal {
  background-color: #ffffff;
  border: 1px solid #e2e8f0;
  border-radius: 16px;
  width: 100%;
  max-height: 90vh;
  display: flex;
  flex-direction: column;
  box-shadow: 0 20px 60px rgba(15, 23, 42, 0.15);
}

.CabeceraModal {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 20px 24px;
  border-bottom: 1px solid #f1f5f9;
  position: sticky;
  top: 0;
  background-color: #ffffff;
  z-index: 1;
}

.TituloModal {
  font-family: 'Playfair Display', serif;
  font-size: 18px;
  font-weight: 700;
  color: #0f172a;
}

.TituloModalPeligro { color: #dc2626; }

.BotonCerrarModal {
  background: none;
  border: none;
  color: #94a3b8;
  cursor: pointer;
  padding: 4px;
  display: flex;
  align-items: center;
  border-radius: 4px;
  transition: all 0.2s;
}

.BotonCerrarModal:hover { color: #0f172a; background-color: #f1f5f9; }

.CuerpoModal { padding: 24px; }

.PieModal {
  display: flex;
  align-items: center;
  justify-content: flex-end;
  gap: 12px;
  padding: 16px 24px;
  border-top: 1px solid #f1f5f9;
  position: sticky;
  bottom: 0;
  background-color: #ffffff;
}

.FilaModalFormulario { display: flex; gap: 28px; }
.ColumnaFotoModal { width: 160px; flex-shrink: 0; }
.ColumnaDatosModal { flex: 1; min-width: 0; }

.EtiquetaSeccionModal {
  font-family: 'Inter', sans-serif;
  font-size: 12px;
  font-weight: 700;
  color: #0369a1;
  letter-spacing: 1px;
  text-transform: uppercase;
  margin-bottom: 14px;
  padding-bottom: 8px;
  border-bottom: 2px solid #bae6fd;
}

.ContenedorFoto { display: flex; flex-direction: column; align-items: center; gap: 12px; }

.VistasPreviaFoto {
  width: 110px;
  height: 110px;
  border-radius: 50%;
  background-color: #f0f9ff;
  border: 2px dashed #7dd3fc;
  overflow: hidden;
  display: flex;
  align-items: center;
  justify-content: center;
}

.VistasPreviaFoto img { width: 100%; height: 100%; object-fit: cover; }
.PlaceholderFoto { display: flex; align-items: center; justify-content: center; }

.BotonSeleccionarFoto {
  display: flex;
  align-items: center;
  gap: 6px;
  padding: 7px 12px;
  background: transparent;
  border: 1px solid #bae6fd;
  border-radius: 6px;
  color: #0369a1;
  font-family: 'Inter', sans-serif;
  font-size: 12px;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.2s;
  white-space: nowrap;
}

.BotonSeleccionarFoto:hover { background-color: #f0f9ff; }

.CuadriculaFormulario {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 14px;
  margin-bottom: 4px;
}

.GrupoEntrada { display: flex; flex-direction: column; gap: 6px; }
.GrupoEntradaCompleto { grid-column: 1 / -1; }

.EtiquetaCampo {
  font-family: 'Inter', sans-serif;
  font-size: 11px;
  font-weight: 600;
  color: #64748b;
  letter-spacing: 0.5px;
  text-transform: uppercase;
}

.CampoEntrada {
  padding: 10px 12px;
  background-color: #f8fafc;
  border: 1px solid #e2e8f0;
  border-radius: 8px;
  color: #0f172a;
  font-family: 'Inter', sans-serif;
  font-size: 14px;
  outline: none;
  transition: border-color 0.2s, background-color 0.2s;
  width: 100%;
}

.CampoEntrada::placeholder { color: #94a3b8; }
.CampoEntrada:focus { border-color: #0ea5e9; background-color: #ffffff; }
.CampoEntrada option { background-color: #ffffff; }

.SeparadorSeccion { height: 1px; background-color: #f1f5f9; margin: 18px 0; }

.CabeceraPerfil { display: flex; align-items: center; gap: 16px; margin-bottom: 20px; }

.AvatarDetalle {
  width: 64px;
  height: 64px;
  border-radius: 50%;
  background: linear-gradient(135deg, #e0f2fe, #bae6fd);
  border: 2px solid #7dd3fc;
  display: flex;
  align-items: center;
  justify-content: center;
  overflow: hidden;
  font-family: 'Inter', sans-serif;
  font-size: 22px;
  font-weight: 700;
  color: #0369a1;
  flex-shrink: 0;
}

.AvatarDetalle img { width: 100%; height: 100%; object-fit: cover; }

.NombreDetalle {
  font-family: 'Playfair Display', serif;
  font-size: 18px;
  color: #0f172a;
  margin-bottom: 4px;
}

.SeparadorDetalle { height: 1px; background-color: #f1f5f9; margin: 16px 0; }

.CuadriculaDetalle { display: grid; grid-template-columns: 1fr 1fr; gap: 16px; }
.ItemDetalle { display: flex; flex-direction: column; gap: 4px; }
.ItemDetalleCompleto { grid-column: 1 / -1; }

.EtiquetaDetalle {
  font-family: 'Inter', sans-serif;
  font-size: 10px;
  font-weight: 700;
  color: #94a3b8;
  letter-spacing: 1px;
  text-transform: uppercase;
}

.ValorDetalle { font-family: 'Inter', sans-serif; font-size: 14px; color: #1e293b; }

.TextoConfirmacion { font-family: 'Inter', sans-serif; font-size: 15px; color: #64748b; line-height: 1.6; }
.TextoConfirmacion strong { color: #0f172a; }

.BotonEliminarModal {
  padding: 10px 20px;
  background: linear-gradient(135deg, #dc2626, #b91c1c);
  border: none;
  border-radius: 8px;
  color: #ffffff;
  font-family: 'Inter', sans-serif;
  font-size: 14px;
  font-weight: 600;
  cursor: pointer;
  transition: opacity 0.2s, transform 0.2s;
}

.BotonEliminarModal:hover:not(:disabled) { transform: translateY(-1px); }
.BotonEliminarModal:disabled { opacity: 0.6; cursor: not-allowed; }

@media (max-width: 768px) {
  .VistaProveedores { padding: 20px; }
  .FilaModalFormulario { flex-direction: column; }
  .ColumnaFotoModal { width: 100%; }
  .CuadriculaFormulario { grid-template-columns: 1fr; }
}
</style>
