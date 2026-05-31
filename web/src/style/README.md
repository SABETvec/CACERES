# Sistema de Estilos Consolidados

## Estructura

Este directorio contiene todos los estilos globales y consolidados del proyecto.

### `globals.css`

Archivo principal que contiene:

#### Variables de Color (Paleta Azul Celeste)
- `--color-primary-dark`: #0369a1 (Azul oscuro)
- `--color-primary-light`: #0ea5e9 (Azul celeste)
- `--color-white`: #ffffff
- `--color-gray-light`: #f0f6ff (Fondo claro)
- `--color-gray-medium`: #cbd5e1
- `--color-gray-text`: #64748b
- `--color-gray-dark`: #0f172a (Texto oscuro)
- `--color-error`: #dc3c3c (Rojo error)
- `--color-error-light`: #e87c7c (Rojo error claro)

#### Componentes Consolidados

**Botones:**
- `.BotonPrimario` - Botón principal con gradiente azul
- `.BotonSecundario` - Botón secundario con borde
- `.BotonIcono` - Botón de icono simple
- `.BotonEliminarModal` - Botón de eliminación

**Tablas:**
- `.ContenedorTabla` - Contenedor principal
- `.TablaEstilos` - Tabla base
- `.EncabezadoTabla` - Encabezado
- `.FilaTabla`, `.CeldaTabla` - Filas y celdas
- `.AvatarTabla`, `.AvatarDetalle` - Avatares

**Insignias:**
- `.InsigniaSaldo` - Para mostrar saldos
- `.InsigniaEspecialidad` - Para especialidades
- `.InsigniaRol` - Para roles
- `.InsigniaEstado` - Para estados (activo/inactivo)

**Formularios:**
- `.GrupoEntrada` - Grupo de campo
- `.EtiquetaCampo` - Etiqueta de campo
- `.CampoEntrada` - Campo de entrada
- `.ContenedorBusqueda`, `.EntradaBusqueda` - Búsqueda
- `.SelectorFiltro` - Selector/dropdown

**Modales:**
- `.OverlayModal` - Overlay background
- `.ContenedorModal` - Modal container
- `.CabeceraModal`, `.CuerpoModal`, `.PieModal` - Secciones del modal
- `.TituloModal` - Título del modal
- `.BotonCerrarModal` - Botón cerrar

**Detalles:**
- `.CabeceraPerfil` - Cabecera con avatar y nombre
- `.NombreDetalle`, `.UsernameDetalle` - Textos de detalle
- `.CuadriculaDetalle`, `.ItemDetalle` - Grid de detalles

**Estados:**
- `.EstadoCargando` - Estado de carga
- `.AlertaError` - Alerta de error
- `.TextoConfirmacion` - Texto de confirmación

## Paleta de Colores

El proyecto ahora utiliza una **paleta azul celeste y blanco** consistente:

- Azul celeste oscuro (#0369a1) - Botones principales, textos destacados
- Azul celeste claro (#0ea5e9) - Estados hover, acentos
- Blanco (#ffffff) - Fondos principales
- Gris claro (#f0f6ff) - Fondo secundario
- Gris oscuro (#0f172a) - Textos principales

## Uso

Todas las vistas Vue importan automáticamente `globals.css`:

```vue
<style>
@import '@/style/globals.css';
</style>

<style scoped>
/* Estilos específicos de la vista */
</style>
```

## Ventajas

1. **Consolidación**: Un solo archivo CSS base para todo el proyecto
2. **Consistencia**: Colores y estilos únicos en toda la aplicación
3. **Reutilización**: Las clases se pueden usar en múltiples vistas sin duplicación
4. **Mantenibilidad**: Cambios globales se hacen en un solo lugar
5. **Rendimiento**: Menos CSS duplicado, menos bytes transferidos
