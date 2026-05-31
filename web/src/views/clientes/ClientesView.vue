<template>
  <div class="VistaClientes">

    <div class="CabeceraVista">
      <div>
        <h1 class="TituloVista">Gestión de Clientes</h1>
        <p class="SubtituloVista">Administra la cartera de clientes</p>
      </div>
      <button class="BotonPrimario" @click="AbrirModalCrear">
        <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
          <line x1="12" y1="5" x2="12" y2="19"/>
          <line x1="5" y1="12" x2="19" y2="12"/>
        </svg>
        Nuevo Cliente
      </button>
    </div>

    <div class="FilaFiltros">
      <div class="ContenedorBusqueda">
        <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="#94a3b8" stroke-width="2">
          <circle cx="11" cy="11" r="8"/>
          <line x1="21" y1="21" x2="16.65" y2="16.65"/>
        </svg>
        <input v-model="TextoBusqueda" type="text" class="EntradaBusqueda" placeholder="Buscar cliente..." />
      </div>
    </div>

    <div class="ContenedorTabla">
      <div v-if="EstaCargando" class="EstadoCargando">
        <div class="AnimadorCarga" />
        <span>Cargando clientes...</span>
      </div>

      <table v-else class="TablaEstilos">
        <thead>
          <tr>
            <th class="EncabezadoTabla"></th>
            <th class="EncabezadoTabla">Nombre</th>
            <th class="EncabezadoTabla">Correo</th>
            <th class="EncabezadoTabla">Celular</th>
            <th class="EncabezadoTabla">Ciudad</th>
            <th class="EncabezadoTabla" style="text-align:right">Saldo</th>
            <th class="EncabezadoTabla" style="text-align:center">Acciones</th>
          </tr>
        </thead>
        <tbody>
          <tr v-if="ClientesFiltrados.length === 0">
            <td colspan="7" class="FilaVacia">No hay clientes registrados</td>
          </tr>
          <tr v-for="Cliente in ClientesFiltrados" :key="Cliente.idCliente" class="FilaTabla">
            <td class="CeldaTabla CeldaAvatar">
              <div class="AvatarTabla">
                <img v-if="Cliente.fotoPerfil" :src="`http://localhost:8080${Cliente.fotoPerfil}`" alt="foto" />
                <span v-else>{{ (Cliente.nombre || '?').charAt(0).toUpperCase() }}</span>
              </div>
            </td>
            <td class="CeldaTabla">
              <div class="NombreCompleto">{{ Cliente.nombre }} {{ Cliente.apellidoPaterno }}</div>
              <div class="ApellidoSecundario">{{ Cliente.apellidoMaterno }}</div>
            </td>
            <td class="CeldaTabla CeldaSecundaria">{{ Cliente.correoElectronico || '-' }}</td>
            <td class="CeldaTabla CeldaSecundaria">{{ Cliente.celular || '-' }}</td>
            <td class="CeldaTabla CeldaSecundaria">{{ Cliente.ciudad || '-' }}</td>
            <td class="CeldaTabla" style="text-align:right">
              <span class="InsigniaSaldo">Bs. {{ formatearSaldo(Cliente.saldo) }}</span>
            </td>
            <td class="CeldaTabla CeldaAcciones">
              <button class="BotonIcono BotonDetalle" @click="AbrirDetalle(Cliente)" title="Ver detalle">
                <svg width="15" height="15" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                  <path d="M1 12s4-8 11-8 11 8 11 8-4 8-11 8-11-8-11-8z"/>
                  <circle cx="12" cy="12" r="3"/>
                </svg>
              </button>
              <button class="BotonIcono BotonEditar" @click="AbrirModalEditar(Cliente)" title="Editar">
                <svg width="15" height="15" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                  <path d="M11 4H4a2 2 0 0 0-2 2v14a2 2 0 0 0 2 2h14a2 2 0 0 0 2-2v-7"/>
                  <path d="M18.5 2.5a2.121 2.121 0 0 1 3 3L12 15l-4 1 1-4 9.5-9.5z"/>
                </svg>
              </button>
              <button class="BotonIcono BotonEliminar" @click="ConfirmarEliminar(Cliente)" title="Eliminar">
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
        <div class="ContenedorModal" style="max-width:860px;max-height:92vh">

          <div class="CabeceraModal">
            <div class="TituloModal">{{ EsEdicion ? 'Editar Cliente' : 'Nuevo Cliente' }}</div>
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
                  <div class="GrupoEntrada">
                    <label class="EtiquetaCampo">Saldo Inicial (Bs.)</label>
                    <input v-model="Formulario.saldo" type="number" step="0.01" min="0" class="CampoEntrada" placeholder="0.00" />
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
                    <input v-model="Formulario.zona" type="text" class="CampoEntrada" placeholder="Zona" />
                  </div>
                  <div class="GrupoEntrada">
                    <label class="EtiquetaCampo">Dirección</label>
                    <input v-model="Formulario.direccion" type="text" class="CampoEntrada" placeholder="Dirección" />
                  </div>
                  <div class="GrupoEntrada GrupoEntradaCompleto">
                    <label class="EtiquetaCampo">Referencia</label>
                    <input v-model="Formulario.referencia" type="text" class="CampoEntrada" placeholder="Referencia" />
                  </div>
                </div>

              </div>
            </div>
          </div>

          <div class="PieModal">
            <button class="BotonSecundario" @click="CerrarModal">Cancelar</button>
            <button class="BotonPrimario" :disabled="EstaGuardando" @click="GuardarCliente">
              <span v-if="!EstaGuardando">{{ EsEdicion ? 'Actualizar Cliente' : 'Guardar Cliente' }}</span>
              <span v-else>Guardando...</span>
            </button>
          </div>

        </div>
      </div>
    </Teleport>

    <Teleport to="body">
      <div v-if="ModalDetalleVisible" class="OverlayModal" @click.self="ModalDetalleVisible = false">
        <div class="ContenedorModal" style="max-width:560px">

          <div class="CabeceraModal">
            <div class="TituloModal">Detalle del Cliente</div>
            <button class="BotonCerrarModal" @click="ModalDetalleVisible = false">
              <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                <line x1="18" y1="6" x2="6" y2="18"/><line x1="6" y1="6" x2="18" y2="18"/>
              </svg>
            </button>
          </div>

          <div v-if="ClienteDetalle" class="CuerpoModal">
            <div class="CabeceraPerfil">
              <div class="AvatarDetalle">
                <img v-if="ClienteDetalle.fotoPerfil" :src="`http://localhost:8080${ClienteDetalle.fotoPerfil}`" alt="foto" />
                <span v-else>{{ (ClienteDetalle.nombre || '?').charAt(0).toUpperCase() }}</span>
              </div>
              <div>
                <h2 class="NombreDetalle">{{ ClienteDetalle.nombre }} {{ ClienteDetalle.apellidoPaterno }} {{ ClienteDetalle.apellidoMaterno }}</h2>
                <div class="SaldoDestacado">Bs. {{ formatearSaldo(ClienteDetalle.saldo) }}</div>
              </div>
            </div>

            <div class="SeparadorDetalle" />

            <div class="CuadriculaDetalle">
              <div class="ItemDetalle">
                <span class="EtiquetaDetalle">Sexo</span>
                <span class="ValorDetalle">{{ ClienteDetalle.sexo === 'M' ? 'Masculino' : ClienteDetalle.sexo === 'F' ? 'Femenino' : '-' }}</span>
              </div>
              <div class="ItemDetalle">
                <span class="EtiquetaDetalle">Fecha de Nacimiento</span>
                <span class="ValorDetalle">{{ ClienteDetalle.fechaDeNacimiento || '-' }}</span>
              </div>
              <div class="ItemDetalle ItemDetalleCompleto">
                <span class="EtiquetaDetalle">Correo Electrónico</span>
                <span class="ValorDetalle">{{ ClienteDetalle.correoElectronico || '-' }}</span>
              </div>
              <div class="ItemDetalle">
                <span class="EtiquetaDetalle">Celular</span>
                <span class="ValorDetalle">{{ ClienteDetalle.celular || '-' }}</span>
              </div>
              <div class="ItemDetalle">
                <span class="EtiquetaDetalle">País</span>
                <span class="ValorDetalle">{{ ClienteDetalle.pais || '-' }}</span>
              </div>
              <div class="ItemDetalle">
                <span class="EtiquetaDetalle">Ciudad</span>
                <span class="ValorDetalle">{{ ClienteDetalle.ciudad || '-' }}</span>
              </div>
              <div class="ItemDetalle">
                <span class="EtiquetaDetalle">Zona</span>
                <span class="ValorDetalle">{{ ClienteDetalle.zona || '-' }}</span>
              </div>
              <div class="ItemDetalle ItemDetalleCompleto">
                <span class="EtiquetaDetalle">Dirección</span>
                <span class="ValorDetalle">{{ ClienteDetalle.direccion || '-' }}</span>
              </div>
              <div class="ItemDetalle ItemDetalleCompleto">
                <span class="EtiquetaDetalle">Referencia</span>
                <span class="ValorDetalle">{{ ClienteDetalle.referencia || '-' }}</span>
              </div>
              <div class="ItemDetalle">
                <span class="EtiquetaDetalle">Fecha de Registro</span>
                <span class="ValorDetalle">{{ FormatearFecha(ClienteDetalle.fechaCreacion) }}</span>
              </div>
              <div class="ItemDetalle">
                <span class="EtiquetaDetalle">Última Actualización</span>
                <span class="ValorDetalle">{{ FormatearFecha(ClienteDetalle.fechaActualizacion) }}</span>
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
              ¿Eliminar al cliente <strong>{{ ClienteAEliminar?.nombre }} {{ ClienteAEliminar?.apellidoPaterno }}</strong>?
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
import { clienteApi } from '@/api/cliente.api'
import { useNotificacion } from '@/composables/useNotificacion'

const { MostrarNotificacion } = useNotificacion()

const ListaClientes = ref([])
const EstaCargando = ref(false)
const EstaGuardando = ref(false)
const EstaEliminando = ref(false)
const ModalFormularioVisible = ref(false)
const ModalDetalleVisible = ref(false)
const ModalConfirmacionVisible = ref(false)
const EsEdicion = ref(false)
const ClienteDetalle = ref(null)
const ClienteAEliminar = ref(null)
const FotoSeleccionada = ref(null)
const VistasPreviaFoto = ref(null)
const TextoBusqueda = ref('')

const FormularioVacio = () => ({
  idCliente: null,
  nombre: '', apellidoPaterno: '', apellidoMaterno: '',
  sexo: '', fechaDeNacimiento: null, saldo: 0,
  correoElectronico: '', celular: '',
  pais: '', ciudad: '', zona: '', direccion: '', referencia: ''
})

const Formulario = ref(FormularioVacio())

const ClientesFiltrados = computed(() => {
  if (!TextoBusqueda.value) return ListaClientes.value
  const Texto = TextoBusqueda.value.toLowerCase()
  return ListaClientes.value.filter((Cliente) =>
    Cliente.nombre?.toLowerCase().includes(Texto) ||
    Cliente.apellidoPaterno?.toLowerCase().includes(Texto) ||
    Cliente.correoElectronico?.toLowerCase().includes(Texto) ||
    Cliente.celular?.includes(Texto)
  )
})

const CargarClientes = async () => {
  EstaCargando.value = true
  try {
    ListaClientes.value = await clienteApi.listarTodos()
  } catch {
    MostrarNotificacion('Error al cargar los clientes', 'error')
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

const AbrirModalEditar = (Cliente) => {
  EsEdicion.value = true
  Formulario.value = {
    idCliente: Cliente.idCliente,
    nombre: Cliente.nombre || '',
    apellidoPaterno: Cliente.apellidoPaterno || '',
    apellidoMaterno: Cliente.apellidoMaterno || '',
    sexo: Cliente.sexo || '',
    fechaDeNacimiento: Cliente.fechaDeNacimiento || null,
    saldo: Cliente.saldo || 0,
    correoElectronico: Cliente.correoElectronico || '',
    celular: Cliente.celular || '',
    pais: Cliente.pais || '',
    ciudad: Cliente.ciudad || '',
    zona: Cliente.zona || '',
    direccion: Cliente.direccion || '',
    referencia: Cliente.referencia || ''
  }
  FotoSeleccionada.value = null
  VistasPreviaFoto.value = Cliente.fotoPerfil ? `http://localhost:8080${Cliente.fotoPerfil}` : null
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

const GuardarCliente = async () => {
  if (!Formulario.value.nombre || !Formulario.value.apellidoPaterno) {
    MostrarNotificacion('El nombre y apellido paterno son requeridos', 'advertencia')
    return
  }
  EstaGuardando.value = true
  try {
    if (EsEdicion.value) {
      await clienteApi.actualizar(Formulario.value.idCliente, Formulario.value, FotoSeleccionada.value)
      MostrarNotificacion('Cliente actualizado correctamente')
    } else {
      await clienteApi.guardar(Formulario.value, FotoSeleccionada.value)
      MostrarNotificacion('Cliente registrado correctamente')
    }
    ModalFormularioVisible.value = false
    await CargarClientes()
  } catch {
    MostrarNotificacion('Error al guardar el cliente', 'error')
  } finally {
    EstaGuardando.value = false
  }
}

const AbrirDetalle = (Cliente) => {
  ClienteDetalle.value = Cliente
  ModalDetalleVisible.value = true
}

const ConfirmarEliminar = (Cliente) => {
  ClienteAEliminar.value = Cliente
  ModalConfirmacionVisible.value = true
}

const EjecutarEliminar = async () => {
  EstaEliminando.value = true
  try {
    await clienteApi.eliminar(ClienteAEliminar.value.idCliente)
    MostrarNotificacion('Cliente eliminado correctamente')
    ModalConfirmacionVisible.value = false
    await CargarClientes()
  } catch {
    MostrarNotificacion('Error al eliminar el cliente', 'error')
  } finally {
    EstaEliminando.value = false
  }
}

const formatearSaldo = (Saldo) => {
  if (Saldo == null) return '0.00'
  return parseFloat(Saldo).toFixed(2)
}

const FormatearFecha = (FechaISO) => {
  if (!FechaISO) return '-'
  return new Date(FechaISO).toLocaleString('es-ES')
}

onMounted(CargarClientes)
</script>
<style>
@import url('https://fonts.googleapis.com/css2?family=Inter:wght@300;400;500;600;700&family=Playfair+Display:wght@600;700&display=swap');
@import '@/style/globals.css';
</style>

