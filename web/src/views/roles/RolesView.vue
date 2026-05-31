<template>
  <div class="VistaRoles">

    <div class="CabeceraVista">
      <div>
        <h1 class="TituloVista">Gestión de Roles</h1>
        <p class="SubtituloVista">Administra los roles y sus accesos al sistema</p>
      </div>
      <button class="BotonPrimario" @click="AbrirModalCrear">
        <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
          <line x1="12" y1="5" x2="12" y2="19"/>
          <line x1="5" y1="12" x2="19" y2="12"/>
        </svg>
        Nuevo Rol
      </button>
    </div>

    <div class="ContenedorTabla">
      <div v-if="EstaCargando" class="EstadoCargando">
        <div class="AnimadorCarga" />
        <span>Cargando roles...</span>
      </div>

      <table v-else class="TablaEstilos">
        <thead>
          <tr>
            <th class="EncabezadoTabla">#</th>
            <th class="EncabezadoTabla">Rol</th>
            <th class="EncabezadoTabla" style="text-align:center">Acciones</th>
          </tr>
        </thead>
        <tbody>
          <tr v-if="ListaRoles.length === 0">
            <td colspan="3" class="FilaVacia">No hay roles registrados</td>
          </tr>
          <tr v-for="Rol in ListaRoles" :key="Rol.idRol" class="FilaTabla">
            <td class="CeldaTabla CeldaId">{{ Rol.idRol }}</td>
            <td class="CeldaTabla">
              <div class="NombreRol">
                <div class="InsigniaRol">
                  <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="#c4912a" stroke-width="2">
                    <path d="M12 22s8-4 8-10V5l-8-3-8 3v7c0 6 8 10 8 10z"/>
                  </svg>
                </div>
                {{ Rol.nombre }}
              </div>
            </td>
            <td class="CeldaTabla CeldaAcciones">
              <button class="BotonAccionTabla BotonEditar" @click="AbrirModalEditar(Rol)" title="Editar">
                <svg width="15" height="15" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                  <path d="M11 4H4a2 2 0 0 0-2 2v14a2 2 0 0 0 2 2h14a2 2 0 0 0 2-2v-7"/>
                  <path d="M18.5 2.5a2.121 2.121 0 0 1 3 3L12 15l-4 1 1-4 9.5-9.5z"/>
                </svg>
                Editar
              </button>
              <button class="BotonAccionTabla BotonEliminar" @click="ConfirmarEliminar(Rol)" title="Eliminar">
                <svg width="15" height="15" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                  <polyline points="3 6 5 6 21 6"/>
                  <path d="M19 6v14a2 2 0 0 1-2 2H7a2 2 0 0 1-2-2V6m3 0V4a1 1 0 0 1 1-1h4a1 1 0 0 1 1 1v2"/>
                </svg>
                Eliminar
              </button>
            </td>
          </tr>
        </tbody>
      </table>
    </div>

    <Teleport to="body">
      <div v-if="ModalVisible" class="OverlayModal" @click.self="CerrarModal">
        <div class="ContenedorModal ContenedorModalGrande">

          <div class="CabeceraModal">
            <div class="TituloModal">
              <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="#c4912a" stroke-width="2">
                <path d="M12 22s8-4 8-10V5l-8-3-8 3v7c0 6 8 10 8 10z"/>
              </svg>
              {{ EsEdicion ? 'Editar Rol' : 'Nuevo Rol' }}
            </div>
            <button class="BotonCerrarModal" @click="CerrarModal">
              <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                <line x1="18" y1="6" x2="6" y2="18"/>
                <line x1="6" y1="6" x2="18" y2="18"/>
              </svg>
            </button>
          </div>

          <div class="CuerpoModal">
            <div class="FilaModal">

              <div class="ColumnaIzquierdaModal">
                <p class="EtiquetaSeccionModal">Información del Rol</p>
                <div class="GrupoEntrada">
                  <label class="EtiquetaCampo">Nombre del Rol *</label>
                  <input
                    v-model="Formulario.nombre"
                    type="text"
                    class="CampoEntrada"
                    placeholder="Ej: ADMINISTRADOR"
                  />
                </div>
              </div>

              <div class="ColumnaDerechaModal">
                <p class="EtiquetaSeccionModal">Permisos y Accesos por Módulo</p>

                <div v-if="EstaCargandoAccesos" class="EstadoCargando">
                  <div class="AnimadorCarga" />
                </div>

                <div v-else class="ContenedorMatriz">
                  <table class="TablaMatriz">
                    <thead>
                      <tr>
                        <th class="EncabezadoMatriz EncabezadoModulo">Módulo</th>
                        <th
                          v-for="Permiso in ListaPermisos"
                          :key="Permiso.idPermiso"
                          class="EncabezadoMatriz"
                        >
                          {{ Permiso.nombre }}
                        </th>
                      </tr>
                    </thead>
                    <tbody>
                      <tr v-for="Modulo in ListaModulos" :key="Modulo.idModulo" class="FilaMatriz">
                        <td class="CeldaMatriz CeldaNombreModulo">{{ Modulo.nombre }}</td>
                        <td
                          v-for="Permiso in ListaPermisos"
                          :key="Permiso.idPermiso"
                          class="CeldaMatriz CeldaCheckbox"
                        >
                          <label class="ContenedorCheckbox">
                            <input
                              type="checkbox"
                              v-model="MatrizAccesos[Modulo.idModulo]"
                              :value="Permiso.idPermiso"
                              class="InputCheckbox"
                            />
                            <span class="CheckboxPersonalizado" />
                          </label>
                        </td>
                      </tr>
                    </tbody>
                  </table>
                </div>

              </div>
            </div>
          </div>

          <div class="PieModal">
            <button class="BotonSecundario" @click="CerrarModal">Cancelar</button>
            <button class="BotonPrimario" :disabled="EstaGuardando" @click="GuardarRol">
              <span v-if="!EstaGuardando">{{ EsEdicion ? 'Actualizar Rol' : 'Guardar Rol' }}</span>
              <span v-else>Guardando...</span>
            </button>
          </div>

        </div>
      </div>
    </Teleport>

    <Teleport to="body">
      <div v-if="ModalConfirmacionVisible" class="OverlayModal" @click.self="ModalConfirmacionVisible = false">
        <div class="ContenedorModal ContenedorModalPequeno">

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
              ¿Estás seguro de eliminar el rol <strong>{{ RolAEliminar?.nombre }}</strong>?
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
import { ref, reactive, onMounted } from 'vue'
import { rolApi } from '@/api/rol.api'
import { permisoApi } from '@/api/permiso.api'
import { moduloApi } from '@/api/modulo.api'
import { rolAccesoApi } from '@/api/rolAcceso.api'
import { useNotificacion } from '@/composables/useNotificacion'

const { MostrarNotificacion } = useNotificacion()

const ListaRoles = ref([])
const ListaPermisos = ref([])
const ListaModulos = ref([])
const EstaCargando = ref(false)
const EstaCargandoAccesos = ref(false)
const EstaGuardando = ref(false)
const EstaEliminando = ref(false)
const ModalVisible = ref(false)
const ModalConfirmacionVisible = ref(false)
const EsEdicion = ref(false)
const RolAEliminar = ref(null)

const Formulario = ref({ idRol: null, nombre: '' })
const MatrizAccesos = ref({})

const InicializarMatriz = () => {
  const NuevaMatriz = {}
  ListaModulos.value.forEach((Modulo) => {
    NuevaMatriz[Modulo.idModulo] = []
  })
  MatrizAccesos.value = NuevaMatriz
}

const CargarRoles = async () => {
  EstaCargando.value = true
  try {
    ListaRoles.value = await rolApi.listarTodos()
  } catch {
    MostrarNotificacion('Error al cargar los roles', 'error')
  } finally {
    EstaCargando.value = false
  }
}

const AbrirModalCrear = () => {
  EsEdicion.value = false
  Formulario.value = { idRol: null, nombre: '' }
  InicializarMatriz()
  ModalVisible.value = true
}

const AbrirModalEditar = async (Rol) => {
  EsEdicion.value = true
  Formulario.value = { ...Rol }
  InicializarMatriz()
  ModalVisible.value = true
  EstaCargandoAccesos.value = true
  try {
    const AccesosExistentes = await rolAccesoApi.listarPorRol(Rol.idRol)
    AccesosExistentes.forEach((Acceso) => {
      if (!MatrizAccesos.value[Acceso.idModulo]) {
        MatrizAccesos.value[Acceso.idModulo] = []
      }
      MatrizAccesos.value[Acceso.idModulo].push(Acceso.idPermiso)
    })
  } catch {
    MostrarNotificacion('Error al cargar los accesos del rol', 'error')
  } finally {
    EstaCargandoAccesos.value = false
  }
}

const CerrarModal = () => {
  ModalVisible.value = false
}

const GuardarRol = async () => {
  if (!Formulario.value.nombre.trim()) {
    MostrarNotificacion('El nombre del rol es requerido', 'advertencia')
    return
  }
  EstaGuardando.value = true
  try {
    let RolGuardado
    if (EsEdicion.value) {
      RolGuardado = await rolApi.actualizar(Formulario.value.idRol, Formulario.value)
    } else {
      RolGuardado = await rolApi.guardar(Formulario.value)
    }

    const ListaAccesoItems = []
    Object.entries(MatrizAccesos.value).forEach(([IdModulo, ListaPermisosSeleccionados]) => {
      ListaPermisosSeleccionados.forEach((IdPermiso) => {
        ListaAccesoItems.push({ idModulo: parseInt(IdModulo), idPermiso: IdPermiso })
      })
    })

    await rolAccesoApi.guardarAccesos({
      idRol: RolGuardado.idRol,
      accesos: ListaAccesoItems
    })

    MostrarNotificacion(EsEdicion.value ? 'Rol actualizado correctamente' : 'Rol creado correctamente')
    ModalVisible.value = false
    await CargarRoles()
  } catch {
    MostrarNotificacion('Error al guardar el rol', 'error')
  } finally {
    EstaGuardando.value = false
  }
}

const ConfirmarEliminar = (Rol) => {
  RolAEliminar.value = Rol
  ModalConfirmacionVisible.value = true
}

const EjecutarEliminar = async () => {
  EstaEliminando.value = true
  try {
    await rolApi.eliminar(RolAEliminar.value.idRol)
    MostrarNotificacion('Rol eliminado correctamente')
    ModalConfirmacionVisible.value = false
    await CargarRoles()
  } catch {
    MostrarNotificacion('Error al eliminar el rol', 'error')
  } finally {
    EstaEliminando.value = false
  }
}

onMounted(async () => {
  EstaCargando.value = true
  try {
    const [RolesObtenidos, PermisosObtenidos, ModulosObtenidos] = await Promise.all([
      rolApi.listarTodos(),
      permisoApi.listarTodos(),
      moduloApi.listarTodos()
    ])
    ListaRoles.value = RolesObtenidos
    ListaPermisos.value = PermisosObtenidos
    ListaModulos.value = ModulosObtenidos
  } catch {
    MostrarNotificacion('Error al cargar los datos', 'error')
  } finally {
    EstaCargando.value = false
  }
})
</script>
<style>
@import url('https://fonts.googleapis.com/css2?family=Crimson+Pro:ital,wght@0,300;0,400;0,600;1,300;1,400&family=Libre+Baskerville:ital,wght@0,400;0,700;1,400&display=swap');
@import '@/style/globals.css';
</style>
