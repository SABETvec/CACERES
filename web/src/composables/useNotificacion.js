import { ref } from 'vue'

const ListaNotificaciones = ref([])

export const useNotificacion = () => {
  const MostrarNotificacion = (Mensaje, Tipo = 'exito') => {
    const IdentificadorNotificacion = Date.now()
    ListaNotificaciones.value.push({
      identificador: IdentificadorNotificacion,
      mensaje: Mensaje,
      tipo: Tipo
    })
    setTimeout(() => {
      ListaNotificaciones.value = ListaNotificaciones.value.filter(
        (Notificacion) => Notificacion.identificador !== IdentificadorNotificacion
      )
    }, 3500)
  }

  return { ListaNotificaciones, MostrarNotificacion }
}