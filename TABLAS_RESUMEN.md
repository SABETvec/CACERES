# Resumen de Tablas - Base de Datos Librería CACERES

## Listado Completo de 28 Tablas

### MÓDULO 1: GESTIÓN DE PERSONAS (3 tablas)
```
┌─────────────────────────────────────────┐
│ Persona (IdPersona PK)                  │
│ ├─ Nombre                               │
│ ├─ ApellidoPaterno                      │
│ ├─ ApellidoMaterno                      │
│ ├─ Sexo                                 │
│ ├─ FechaDeNacimiento                    │
│ ├─ FotoPerfil                           │
│ ├─ FechaCreacion                        │
│ └─ FechaActualizacion                   │
└─────────────────────────────────────────┘
         ↓ 1:N
    ┌─────────────────────────────────────────┐
    │ Contacto (IdContacto PK)                │
    │ ├─ Email                               │
    │ ├─ Celular                             │
    │ └─ IdPersona (FK)                      │
    └─────────────────────────────────────────┘

    ┌─────────────────────────────────────────┐
    │ Dirección (IdDireccion PK)              │
    │ ├─ Pais                                 │
    │ ├─ Ciudad                               │
    │ ├─ Zona                                 │
    │ ├─ Dirección                            │
    │ ├─ Referencia                           │
    │ └─ IdPersona (FK)                       │
    └─────────────────────────────────────────┘
```

---

### MÓDULO 2: SEGURIDAD Y AUTENTICACIÓN (6 tablas)
```
┌──────────────────────────┐      ┌──────────────────────────┐
│ Rol (IdRol PK)           │      │ Permiso (IdPermiso PK)   │
│ └─ Nombre                │      │ └─ Nombre                │
└──────────────────────────┘      └──────────────────────────┘
         ↓                                  ↓
    ┌───────────────────────────────────────────┐
    │ RolAcceso (IdRolAcceso PK)                │
    │ ├─ IdRol (FK)                             │
    │ ├─ IdModulo (FK)                          │
    │ └─ IdPermiso (FK)                         │
    └───────────────────────────────────────────┘
             ↓
    ┌──────────────────────────┐
    │ Modulo (IdModulo PK)     │
    │ ├─ Nombre                │
    │ ├─ Descripción           │
    │ ├─ Icono                 │
    │ └─ Ruta                  │
    └──────────────────────────┘

┌──────────────────────────────────────────┐
│ Usuario (IdUsuario PK)                   │
│ ├─ Username (UNIQUE)                     │
│ ├─ Password                              │
│ ├─ Activo                                │
│ ├─ IdPersona (FK)                        │
│ └─ FechaCreacion                         │
└──────────────────────────────────────────┘
    ├─ 1:N ─→ UsuarioRol ─→ Rol
    └─ 1:N ─→ TokenRecuperacion

┌──────────────────────────────────────────┐
│ UsuarioRol (IdUsuarioRol PK)             │
│ ├─ IdUsuario (FK)                        │
│ ├─ IdRol (FK)                            │
│ └─ FechaRegistro                         │
└──────────────────────────────────────────┘

┌──────────────────────────────────────────┐
│ TokenRecuperacion (IdToken PK) [NUEVA]   │
│ ├─ Token (UNIQUE)                        │
│ ├─ FechaExpiracion                       │
│ ├─ Usado                                 │
│ ├─ FechaCreacion                         │
│ └─ IdUsuario (FK)                        │
└──────────────────────────────────────────┘
```

---

### MÓDULO 3: CLIENTES Y PROVEEDORES (2 tablas)
```
┌──────────────────────────────────────────┐
│ Cliente (IdCliente PK)                   │
│ ├─ Saldo                                 │
│ ├─ FechaCreacion                         │
│ ├─ FechaActualizacion                    │
│ └─ IdPersona (FK)                        │
└──────────────────────────────────────────┘
    ├─ 1:N ─→ Venta
    └─ 1:N ─→ Pedido

┌──────────────────────────────────────────┐
│ Proveedor (IdProveedor PK)               │
│ ├─ Descripción                           │
│ ├─ FechaCreacion                         │
│ ├─ FechaActualizacion                    │
│ └─ IdPersona (FK)                        │
└──────────────────────────────────────────┘
```

---

### MÓDULO 4: PRODUCTOS Y CATÁLOGO (5 tablas)
```
┌──────────────────────────┐    ┌──────────────────────────┐
│ Categoria (IdCategoria)  │    │ Marca (IdMarca)          │
│ └─ Nombre (UNIQUE)       │    │ └─ Nombre (UNIQUE)       │
└──────────────────────────┘    └──────────────────────────┘
         ↓                               ↓
    ┌──────────────────────────────────────────┐
    │ Producto (IdProducto PK)                 │
    │ ├─ Codigo (UNIQUE)                       │
    │ ├─ Nombre                                │
    │ ├─ Descripcion                           │
    │ ├─ Precio                                │
    │ ├─ Activo                                │
    │ ├─ IdCategoria (FK)                      │
    │ ├─ IdMarca (FK)                          │
    │ ├─ FechaCreacion                         │
    │ └─ FechaActualizacion                    │
    └──────────────────────────────────────────┘
              ↓
    ┌──────────────────────────────────────────┐
    │ ProductoUnidad (IdProductoUnidad PK)     │
    │ ├─ IdProducto (FK)                       │
    │ ├─ IdUnidad (FK)                         │
    │ ├─ Equivalencia                          │
    │ ├─ Precio                                │
    │ └─ FechaCreacion                         │
    └──────────────────────────────────────────┘
         ↓
    ┌──────────────────────────┐
    │ UnidadMedida (IdUnidad)  │
    │ ├─ Nombre (UNIQUE)       │
    │ └─ Abreviatura           │
    └──────────────────────────┘
```

---

### MÓDULO 5: INVENTARIO (2 tablas)
```
┌──────────────────────────────────────────┐
│ Inventario (IdInventario PK)             │
│ ├─ Stock                                 │
│ ├─ StockMinimo                           │
│ ├─ StockMaximo                           │
│ ├─ FechaActualizacion                    │
│ └─ IdProductoUnidad (FK)                 │
└──────────────────────────────────────────┘
    ↓ 1:N
┌──────────────────────────────────────────┐
│ MovimientoInventario (IdMovimiento PK)   │
│ ├─ Tipo                                  │
│ ├─ Cantidad                              │
│ ├─ StockAnterior                         │
│ ├─ StockPosterior                        │
│ ├─ Motivo                                │
│ ├─ Fecha                                 │
│ ├─ IdProductoUnidad (FK)                 │
│ └─ IdUsuario (FK)                        │
└──────────────────────────────────────────┘
```

---

### MÓDULO 6: SERVICIOS (1 tabla)
```
┌──────────────────────────────────────────┐
│ Servicio (IdServicio PK)                 │
│ ├─ Nombre (UNIQUE)                       │
│ ├─ Descripción                           │
│ ├─ Precio                                │
│ ├─ Activo                                │
│ └─ FechaCreacion                         │
└──────────────────────────────────────────┘
```

---

### MÓDULO 7: VENTAS (2 tablas)
```
┌──────────────────────────────────────────┐
│ Venta (IdVenta PK)                       │
│ ├─ Fecha                                 │
│ ├─ Total                                 │
│ ├─ Estado                                │
│ ├─ Observación                           │
│ ├─ IdCliente (FK)                        │
│ └─ IdUsuario (FK)                        │
└──────────────────────────────────────────┘
    ↓ 1:N
┌──────────────────────────────────────────┐
│ VentaDetalle (IdDetalle PK)              │
│ ├─ Cantidad                              │
│ ├─ Precio                                │
│ ├─ Subtotal                              │
│ ├─ Descuento                             │
│ ├─ IdVenta (FK)                          │
│ └─ IdProductoUnidad (FK)                 │
└──────────────────────────────────────────┘
```

---

### MÓDULO 8: PEDIDOS (3 tablas)
```
┌──────────────────────────────────────────┐
│ Pedido (IdPedido PK)                     │
│ ├─ Fecha                                 │
│ ├─ FechaEntregaEstimada                  │
│ ├─ Estado                                │
│ ├─ Observación                           │
│ ├─ IdUsuario (FK)                        │
│ └─ IdCliente (FK)                        │
└──────────────────────────────────────────┘
    ↓ 1:N
┌──────────────────────────────────────────┐
│ PedidoDetalle (IdDetalle PK)             │
│ ├─ Tipo                                  │
│ ├─ Cantidad                              │
│ ├─ Precio                                │
│ ├─ IdPedido (FK)                         │
│ ├─ IdProductoUnidad (FK)                 │
│ └─ IdServicio (FK)                       │
└──────────────────────────────────────────┘
    ↓ 1:1
┌──────────────────────────────────────────┐
│ PedidoDetallePersonalizado (IdPersonal)  │
│ ├─ IdDetalle (FK)                        │
│ ├─ TipoTrabajo                           │
│ ├─ ColorTapaFrontal                      │
│ ├─ ColorTapaPosterior                    │
│ ├─ CantidadHojas                         │
│ ├─ Tamaño                                │
│ └─ Observación                           │
└──────────────────────────────────────────┘
```

---

### MÓDULO 9: CONFIGURACIÓN Y REPORTES (3 tablas)
```
┌──────────────────────────────────────────┐
│ TipoCambio (IdTipoCambio PK)             │
│ ├─ Valor                                 │
│ ├─ Moneda                                │
│ └─ Fecha                                 │
└──────────────────────────────────────────┘

┌──────────────────────────────────────────┐
│ Reporte (IdReporte PK)                   │
│ ├─ Tipo                                  │
│ ├─ FechaInicio                           │
│ ├─ FechaFin                              │
│ ├─ FechaGenerado                         │
│ ├─ Datos                                 │
│ └─ IdUsuario (FK)                        │
└──────────────────────────────────────────┘

┌──────────────────────────────────────────┐
│ NotificacionVenta (IdNotificacion PK)    │
│ ├─ IdVenta (FK)                          │
│ ├─ IdPedido (FK)                         │
│ ├─ Medio                                 │
│ ├─ Estado                                │
│ ├─ Mensaje                               │
│ ├─ FechaEnvio                            │
│ ├─ FechaCreacion                         │
│ ├─ IdCliente (FK)                        │
│ └─ IdUsuario (FK)                        │
└──────────────────────────────────────────┘
```

---

## Estadísticas

| Categoría | Cantidad |
|-----------|----------|
| **Tablas Totales** | **28** |
| Tablas Base | 3 |
| Tablas Seguridad | 6 |
| Tablas Negocio | 19 |
| Tablas Sistema | 3 (Reportes, TipoCambio, Notificaciones) |

---

## Campos Comunes en Todas las Tablas

### Campos de Auditoría (Recomendado en tablas principales)
- `FechaCreacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP`
- `FechaActualizacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP`

### Convenciones
- **Primary Keys**: `Id<NombreTabla> SERIAL PRIMARY KEY`
- **Foreign Keys**: `Id<NombraTabla> INT` con `FOREIGN KEY`
- **Campos Textuales**: `VARCHAR` para datos cortos, `TEXT` para largos
- **Campos Numéricos**: `INT` para enteros, `NUMERIC(12,2)` para dinero
- **Campos Booleanos**: `BOOLEAN DEFAULT FALSE/TRUE`

---

## Índices Creados

### Por Búsqueda
- `idx_usuario_username` - Búsqueda rápida de usuarios
- `idx_producto_codigo` - Búsqueda de productos

### Por Relación (FK)
- `idx_usuario_persona` - Relación Usuario-Persona
- `idx_producto_categoria` - Relación Producto-Categoría
- Y más para joins frecuentes

### Por Filtrado Temporal
- `idx_venta_fecha` - Reportes por fecha
- `idx_pedido_fecha` - Filtros de pedidos
- `idx_movimiento_fecha` - Auditoría de movimientos

---

## Notas Importantes

1. **TokenRecuperacion es NUEVA** - Necesaria para la funcionalidad de "Olvidé mi contraseña"
2. **RolAcceso reemplaza RolPermiso** - Sistema más flexible de permisos por módulo
3. **Modulo es NUEVA** - Define los módulos del sistema dinámicamente
4. **ON DELETE CASCADE** - Limpia automáticamente datos relacionados
5. **UNIQUE constraints** - Evita duplicados en campos clave

---

## Próximos Pasos

✓ Ejecutar `database_complete.sql`  
✓ Verificar creación de tablas con `\dt`  
✓ Insertar datos iniciales (ya incluidos)  
✓ Crear usuario específico para la aplicación  
✓ Configurar backups automáticos  

