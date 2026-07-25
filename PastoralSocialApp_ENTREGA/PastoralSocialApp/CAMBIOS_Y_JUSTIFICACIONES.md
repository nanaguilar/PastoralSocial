# Cambios realizados y justificaciones

Documento para el equipo: explica **qué se modificó**, **por qué**, y **en qué se
diferencia** de la versión original que se tenía antes.

---

## 1. Punto de partida (versión original de los compañeros)

La versión anterior tenía:

- Entidades: `Usuario`, `TipoAsistencia`, `Expediente`, `Persona`.
- Solo funcionaba **login / registro** (`LoginFrame`, `RegistroUsuarioFrame`).
- El botón de expedientes mostraba únicamente *"Módulo de expedientes próximamente"*
  → **el módulo de Caso Social no estaba implementado**.
- `Expediente.parroquia` y `Expediente.sectorFilial` eran **texto libre** (`String`).
- `Expediente` **no** tenía la colección de `Persona` (no había relación
  maestro-detalle real, aunque `Persona` sí apuntaba a `Expediente`).

Por eso, más que "modificar" pantallas existentes, se **construyó el módulo de
Caso Social** aplicando desde el inicio las correcciones pedidas, **respetando el
estilo de código original** (Swing plano: `GridLayout`, `JOptionPane`, fuente
Arial en títulos, sin librerías de estilo ni colores personalizados).

---

## 2. Cambios por archivo

### Entidades (`model/`)

| Archivo | Cambio | Justificación |
|---------|--------|---------------|
| `Expediente.java` | `parroquia` y `sectorFilial` pasan de `String` a relación `@ManyToOne` (FK a catálogo). Se agregó `@OneToMany List<Persona> personas` con `cascade=ALL, orphanRemoval=true` y métodos `agregarPersona`/`quitarPersona`. | Convierte el expediente en el **maestro** real del maestro-detalle; la cascada permite guardar el expediente y sus familiares en una sola operación sin código duplicado. |
| `Parroquia.java` *(nuevo)* | Entidad catálogo `id, nombre (único)`. | Sustituye el texto libre por un **valor estructurado y persistente**, evitando duplicados/inconsistencias. |
| `SectorFilial.java` *(nuevo)* | Entidad catálogo `id, nombre, parroquia (FK)`. | Es el **detalle** de la parroquia: cada sector pertenece a una parroquia (combos en cascada). |
| `Persona.java` | Sin cambios estructurales. | Se mantiene la compatibilidad; los campos siguen siendo los mismos, solo se controla su ingreso desde la interfaz. |

### Acceso a datos (`dao/`)

| Archivo | Cambio | Justificación |
|---------|--------|---------------|
| `ExpedienteDAO.java` *(nuevo)* | CRUD del caso social: `guardar`, `actualizar` (merge), `listar` (con `JOIN FETCH` de familiares), `buscarConDetalle`, `existeNumeroFicha`, `eliminar`. | Centraliza la persistencia maestro-detalle. `buscarConDetalle` **reutiliza** los datos existentes (los carga para no volver a pedirlos); `existeNumeroFicha` evita **duplicados**. |
| `ParroquiaDAO.java` *(nuevo)* | Listar parroquias/sectores, `sembrarSiVacio()` y CRUD de catálogo. | Alimenta los combos desde la **base de datos**; la siembra idempotente garantiza que siempre haya opciones sin quemarlas en la interfaz. |
| `UsuarioDAO.java` | Sin cambios. | Login/registro seguían funcionando; no se tocaron. |

### Interfaz (`ui/`)

| Archivo | Cambio | Justificación |
|---------|--------|---------------|
| `MenuPrincipalFrame.java` | Un único botón **"Casos Sociales"** (antes había una celda vacía en el `GridLayout`). | **Reduce la navegación**: un solo acceso al flujo, sin enlaces redundantes. |
| `CasoSocialListFrame.java` *(nuevo)* | Lista de casos para **reabrir/editar**; botones Nuevo, Abrir/Editar, Eliminar, Parroquias y sectores. | Concentra las acciones del flujo en un solo lugar y permite **reutilizar** casos existentes en vez de recrearlos. |
| `CasoSocialFormFrame.java` *(nuevo)* | Formulario **maestro-detalle**: datos del expediente + tabla de familiares; combos de parroquia/sector en cascada; ubicación única. | Implementa el patrón pedido; la ubicación se registra **una sola vez** y la heredan los familiares (sin ubicaciones repetidas). |
| `PersonaDialog.java` *(nuevo)* | Alta/edición de familiar con **combos** (sexo, parentesco, país, migración, educación, seguro). Sin campos de ubicación. | Elimina el **texto libre** en campos que deben ser seleccionables y evita duplicar la ubicación por persona. |
| `CatalogoParroquiasFrame.java` *(nuevo)* | Mantenimiento maestro-detalle de parroquias y sectores. | Permite administrar el catálogo desde la app, coherente con el patrón maestro-detalle. |
| `LoginFrame.java`, `RegistroUsuarioFrame.java` | **Sin cambios.** | Se respetó tal cual la versión de los compañeros. |

### Utilidades (`util/`)

| Archivo | Cambio | Justificación |
|---------|--------|---------------|
| `Catalogos.java` *(nuevo)* | Listas de valores controlados de `Persona` (parentesco, país, migración, educación, seguro, sexo). | Centraliza los vocabularios para que **no queden quemados y dispersos** en cada formulario y el usuario no ingrese valores arbitrarios. |
| `JPAUtil.java` | Sin cambios. | Se reutiliza tal cual. |

### Configuración y base de datos

| Archivo | Cambio | Justificación |
|---------|--------|---------------|
| `persistence.xml` | Se registran `Parroquia` y `SectorFilial`. | Necesario para que Hibernate mapee las nuevas entidades. |
| `db/migracion_maestro_detalle.sql` *(nuevo)* | Crea `parroquia`, `sector_filial` y las columnas `expediente.parroquia_id` / `sector_filial_id` (FK **nulas**), con datos semilla. | Cambio de esquema **no destructivo**: no borra columnas ni datos; las columnas viejas de texto quedan en desuso pero se conservan. |
| `lib/` + `.classpath` | Se incluye **javassist 3.29** (en vez de la 3.12) y se usan rutas **relativas**. | javassist 3.12 no lee el bytecode de Java moderno (lambdas → *invokedynamic*), causando `invalid constant type: 18`. La 3.29 sí. Las rutas relativas hacen el proyecto **portátil** entre máquinas. |

---

## 3. Diferencias clave con la versión anterior

1. **Antes:** parroquia/sector eran texto libre → **Ahora:** catálogos en BD con combos en cascada.
2. **Antes:** no existía el módulo de Caso Social → **Ahora:** formulario maestro-detalle completo (expediente + familiares) con reutilización de datos.
3. **Antes:** la ubicación podía repetirse por familiar → **Ahora:** se registra una vez en el maestro y se hereda.
4. **Antes:** el menú tenía un acceso "próximamente" y una celda vacía → **Ahora:** un solo acceso funcional "Casos Sociales".
5. **Antes:** sin catálogo de parroquias → **Ahora:** pantalla de mantenimiento de parroquias/sectores.
6. **Compatibilidad:** no se eliminó ninguna funcionalidad ni datos; los cambios de BD son aditivos.

> **Nota de estilo:** todas las pantallas nuevas usan el mismo estilo Swing plano
> de los archivos originales (mismo `GridLayout`, `JOptionPane`, Arial en títulos),
> para que el código sea consistente con el del resto del equipo.
