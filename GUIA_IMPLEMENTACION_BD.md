# Guía Completa de Implementación - Base de Datos Librería CACERES

## 1. PREPARACIÓN INICIAL

### Requisitos
- PostgreSQL 12+ instalado
- Acceso de administrador a la base de datos
- Script `database_complete.sql`
- Usuario `postgres` con contraseña configurada

### Verificar PostgreSQL
```bash
psql --version
psql -U postgres -h localhost -c "SELECT version();"
```

---

## 2. CREAR LA BASE DE DATOS

### Opción A: Desde línea de comandos
```bash
# Crear base de datos vacía
createdb -U postgres -O postgres caceres

# Verificar que se creó
psql -U postgres -l | grep caceres
```

### Opción B: Desde pgAdmin
1. Conectar a PostgreSQL
2. Clic derecho en "Databases"
3. Create → Database
4. Nombre: `caceres`
5. Owner: `postgres`
6. Click "Save"

---

## 3. EJECUTAR EL SCRIPT COMPLETO

### Opción A: Línea de comandos (Recomendado)
```bash
# Cambiar a directorio del proyecto
cd /vercel/share/v0-project

# Ejecutar script
psql -U postgres -d caceres -f database_complete.sql

# Resultado esperado:
# CREATE TABLE
# CREATE TABLE
# ... (múltiples líneas)
# CREATE INDEX
# INSERT 0 1
# INSERT 0 4
# ... (inserciones de datos iniciales)
```

### Opción B: Desde pgAdmin
1. Conectar a la base de datos `caceres`
2. Tools → Query Tool
3. Abrir archivo `database_complete.sql`
4. Click en "Execute" (rayo ⚡)
5. Ver resultados en Messages

### Opción C: Con Docker
```bash
# Si PostgreSQL está en un contenedor
docker exec -i nombre_contenedor psql -U postgres -d caceres < database_complete.sql

# Ejemplo:
docker exec -i postgres_caceres psql -U postgres -d caceres < database_complete.sql
```

---

## 4. VERIFICACIÓN DE INSTALACIÓN

### 4.1 Verificar tablas creadas
```bash
psql -U postgres -d caceres -c "\dt"
```

**Resultado esperado: 28 tablas**
```
                 List of relations
 Schema |                  Name                  | Type  | Owner
--------+----------------------------------------+-------+----------
 public | categoria                              | table | postgres
 public | cliente                                | table | postgres
 public | contacto                               | table | postgres
 public | direccion                              | table | postgres
 ... (más tablas)
 public | usuariorol                             | table | postgres
(28 rows)
```

### 4.2 Verificar estructura de una tabla
```bash
psql -U postgres -d caceres -c "\d usuario"
```

**Resultado esperado:**
```
                                    Table "public.usuario"
     Column      |            Type             | Collation | Nullable |      Default
-----------------+-----------------------------+-----------+----------+------------------
 idusuario       | integer                     |           | not null | nextval('usuario_id...'::regclass)
 username        | character varying(50)       |           | not null |
 password        | text                        |           | not null |
 activo          | boolean                     |           |          | true
 fechacreacion   | timestamp without time zone |           |          | CURRENT_TIMESTAMP
 idpersona       | integer                     |           | not null |
Indexes:
    "usuario_pkey" PRIMARY KEY, btree (idusuario)
    "usuario_username_key" UNIQUE, btree (username)
    "idx_usuario_persona" btree (idpersona)
    "idx_usuario_username" btree (username)
Foreign-key constraints:
    "usuario_idpersona_fkey" FOREIGN KEY (idpersona) REFERENCES persona(idpersona)
Referenced by:
    TABLE "tokenrecuperacion" CONSTRAINT "tokenrecuperacion_idusuario_fkey" FOREIGN KEY (idusuario) REFERENCES usuario(idusuario)
```

### 4.3 Contar registros por tabla
```bash
psql -U postgres -d caceres << EOF
SELECT 'Rol' as tabla, COUNT(*) FROM rol
UNION ALL SELECT 'Permiso', COUNT(*) FROM permiso
UNION ALL SELECT 'Modulo', COUNT(*) FROM modulo
UNION ALL SELECT 'Servicio', COUNT(*) FROM servicio
UNION ALL SELECT 'Categoria', COUNT(*) FROM categoria
UNION ALL SELECT 'UnidadMedida', COUNT(*) FROM unidadmedida
UNION ALL SELECT 'TipoCambio', COUNT(*) FROM tipocambio
ORDER BY 1;
EOF
```

**Resultado esperado:**
```
      tabla       | count
------------------+-------
 Categoria        |     4
 Modulo           |    10
 Permiso          |     6
 Rol              |     4
 Servicio         |     3
 TipoCambio       |     2
 UnidadMedida     |     4
(7 rows)
```

### 4.4 Verificar índices
```bash
psql -U postgres -d caceres -c "\di"
```

---

## 5. CREAR USUARIO ESPECÍFICO PARA LA APLICACIÓN

### IMPORTANTE: NO usar usuario `postgres` en producción

```bash
psql -U postgres -d caceres << EOF

-- Crear usuario
CREATE USER app_libreria WITH PASSWORD 'tu_contrasena_segura';

-- Otorgar permisos
GRANT CONNECT ON DATABASE caceres TO app_libreria;
GRANT USAGE ON SCHEMA public TO app_libreria;
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO app_libreria;
GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA public TO app_libreria;

-- Verificar
\du

EOF
```

### Configurar en application.properties (Java)
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/caceres
spring.datasource.username=app_libreria
spring.datasource.password=tu_contrasena_segura
spring.datasource.driver-class-name=org.postgresql.Driver

# Hibernate
spring.jpa.hibernate.ddl-auto=validate
spring.jpa.show-sql=false
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
```

---

## 6. INSERTAR DATOS INICIALES ADICIONALES

El script ya incluye datos básicos. Para agregar más:

### Agregar usuario administrativo
```sql
-- 1. Crear persona
INSERT INTO persona (nombre, apellidopaterno, apellidomaterno, sexo, fechadenacimiento)
VALUES ('Admin', 'Sistema', 'Librería', 'M', '1990-01-01');

-- 2. Obtener IdPersona (verificar con SELECT)
SELECT idpersona FROM persona WHERE nombre = 'Admin' AND apellidopaterno = 'Sistema';
-- Resultado: 1 (o el ID que obtengas)

-- 3. Crear usuario (reemplaza 1 con el IdPersona)
INSERT INTO usuario (username, password, activo, idpersona)
VALUES ('admin', 'bcrypt_encoded_password_aqui', true, 1);

-- 4. Asignar rol Administrador (IdUsuario=1, IdRol=1)
INSERT INTO usuariorol (idusuario, idrol)
VALUES (1, 1);

-- 5. Asignar todos los accesos (Admin tiene acceso a todo)
INSERT INTO rolacceso (idrol, idmodulo, idpermiso)
SELECT 1, m.idmodulo, p.idpermiso 
FROM modulo m, permiso p;
```

### Agregar productos de ejemplo
```sql
INSERT INTO producto (codigo, nombre, descripcion, precio, idcategoria, idmarca)
VALUES 
  ('LIBRO001', 'Libro Java Avanzado', 'Programación en Java', 150.00, 1, NULL),
  ('CUAN001', 'Cuaderno A4 100 hojas', 'Cuaderno de tapa dura', 25.00, 2, NULL),
  ('UTIL001', 'Lapicero BIC azul', 'Bolígrafo de escritura', 5.00, 3, NULL);

-- Agregar unidades a los productos
INSERT INTO productounidad (idproducto, idunidad, equivalencia, precio)
VALUES 
  (1, 1, 1, 150.00),
  (2, 1, 1, 25.00),
  (3, 1, 1, 5.00);

-- Agregar inventario
INSERT INTO inventario (stock, stockminimo, idproductounidad)
VALUES 
  (100, 10, 1),
  (200, 20, 2),
  (500, 50, 3);
```

---

## 7. TESTING Y VALIDACIÓN

### Test de Conexión
```bash
psql -U app_libreria -d caceres -h localhost -c "SELECT COUNT(*) FROM usuario;"
```

### Test de Integridad Referencial
```sql
-- Intentar insertar usuario sin persona (debe fallar)
INSERT INTO usuario (username, password, activo, idpersona) 
VALUES ('testuser', 'password', true, 9999);

-- Error esperado:
-- ERROR: insert or update on table "usuario" violates foreign key constraint
```

### Test de Cascada
```sql
-- Crear persona y usuario
INSERT INTO persona (nombre, apellidopaterno) VALUES ('Test', 'User');
-- Obtener ID (ej: 100)
INSERT INTO usuario (username, password, activo, idpersona) VALUES ('test', 'pass', true, 100);

-- Eliminar persona (debe eliminar usuario también)
DELETE FROM persona WHERE idpersona = 100;

-- Verificar que el usuario se eliminó
SELECT COUNT(*) FROM usuario WHERE idpersona = 100;
-- Resultado: 0
```

---

## 8. BACKUPS Y RESTAURACIÓN

### Crear Backup
```bash
# Backup completo
pg_dump -U postgres -d caceres -f backup_caceres.sql

# Backup solo datos
pg_dump -U postgres -d caceres -a -f backup_datos_caceres.sql

# Backup solo estructura
pg_dump -U postgres -d caceres -s -f backup_estructura_caceres.sql

# Con compresión
pg_dump -U postgres -d caceres -Fc -f backup_caceres.dump
```

### Restaurar desde Backup
```bash
# Desde archivo SQL
psql -U postgres -d caceres -f backup_caceres.sql

# Desde archivo dump
pg_restore -U postgres -d caceres backup_caceres.dump
```

### Restauración Automatizada (Cron)
```bash
# Agregar a crontab
0 2 * * * pg_dump -U postgres -d caceres -Fc -f /backups/caceres_$(date +\%Y\%m\%d).dump
```

---

## 9. OPTIMIZACIÓN Y MANTENIMIENTO

### Estadísticas de la Base de Datos
```bash
# Analizar la base de datos
psql -U postgres -d caceres -c "ANALYZE;"

# Vacuum (limpieza)
psql -U postgres -d caceres -c "VACUUM ANALYZE;"

# Full vacuum (más agresivo)
psql -U postgres -d caceres -c "VACUUM FULL ANALYZE;"
```

### Monitoreo
```sql
-- Ver tamaño de tablas
SELECT schemaname, tablename, pg_size_pretty(pg_total_relation_size(schemaname||'.'||tablename))
FROM pg_tables
WHERE schemaname = 'public'
ORDER BY pg_total_relation_size(schemaname||'.'||tablename) DESC;

-- Ver conexiones activas
SELECT count(*) FROM pg_stat_activity WHERE datname = 'caceres';

-- Ver consultas lentas
SELECT query, calls, mean_exec_time 
FROM pg_stat_statements 
ORDER BY mean_exec_time DESC 
LIMIT 10;
```

---

## 10. TROUBLESHOOTING

### Error: "La base de datos no existe"
```bash
createdb -U postgres caceres
```

### Error: "Usuario no tiene permisos"
```bash
psql -U postgres -d caceres -c "GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO app_libreria;"
```

### Error: "No se puede conectar a PostgreSQL"
```bash
# Verificar que PostgreSQL está corriendo
sudo systemctl status postgresql

# Si no:
sudo systemctl start postgresql

# En Windows:
net start PostgreSQL-x64-14
```

### Error: "Puerto 5432 en uso"
```bash
# En Linux
sudo lsof -i :5432
sudo kill -9 <PID>

# En Windows
netstat -ano | findstr :5432
taskkill /PID <PID> /F
```

---

## 11. CONFIGURACIÓN EN APPLICATION.PROPERTIES

Para tu proyecto Spring Boot Java:

```properties
# ============================================
# DATABASE CONFIGURATION
# ============================================
spring.datasource.url=jdbc:postgresql://localhost:5432/caceres
spring.datasource.username=app_libreria
spring.datasource.password=tu_contrasena_segura
spring.datasource.driver-class-name=org.postgresql.Driver

# ============================================
# HIBERNATE JPA CONFIGURATION
# ============================================
spring.jpa.database-platform=org.hibernate.dialect.PostgreSQL10Dialect
spring.jpa.hibernate.ddl-auto=validate
spring.jpa.show-sql=false
spring.jpa.properties.hibernate.format_sql=false
spring.jpa.properties.hibernate.use_sql_comments=true

# Connection pool
spring.datasource.hikari.maximum-pool-size=10
spring.datasource.hikari.minimum-idle=5
spring.datasource.hikari.idle-timeout=600000
spring.datasource.hikari.max-lifetime=1800000

# ============================================
# LOGGING
# ============================================
logging.level.org.hibernate.SQL=DEBUG
logging.level.org.hibernate.type.descriptor.sql.BasicBinder=TRACE
```

---

## 12. CHECKLIST FINAL

- [ ] PostgreSQL instalado y corriendo
- [ ] Base de datos `caceres` creada
- [ ] Script `database_complete.sql` ejecutado sin errores
- [ ] 28 tablas creadas correctamente
- [ ] Datos iniciales insertados
- [ ] Usuario `app_libreria` creado con permisos
- [ ] Conexión de prueba exitosa
- [ ] Backup inicial creado
- [ ] application.properties configurado
- [ ] Proyecto Java conecta sin errores
- [ ] Tests de integridad pasados

---

## 13. PRÓXIMOS PASOS

1. **Cargar datos maestros:**
   - Categorías adicionales
   - Marcas de productos
   - Servicios especiales

2. **Configurar seguridad:**
   - SSL/TLS para PostgreSQL
   - Usuarios con permisos limitados
   - Auditoría de cambios

3. **Implementar vistas útiles:**
   - ProductosBajoStock
   - ReportesVentasMensuales
   - InventarioActual

4. **Monitores y alertas:**
   - Stock mínimo
   - Ventas diarias
   - Pedidos pendientes

---

## Documentación Adicional

- Archivo: `database_complete.sql` - Script SQL completo
- Archivo: `ANALISIS_BASE_DATOS.md` - Análisis detallado
- Archivo: `TABLAS_RESUMEN.md` - Resumen visual de tablas

---

**¡Base de datos lista para producción!** ✓
