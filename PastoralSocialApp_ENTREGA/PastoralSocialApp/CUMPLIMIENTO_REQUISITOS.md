# Cumplimiento de la instrucción inicial del proyecto

Estado de cada requisito solicitado por el equipo.

**Leyenda:** ✅ Cumplido &nbsp;·&nbsp; ➖ No aplicaba / sin cambios necesarios

---

## Requisitos funcionales

| # | Requisito | Estado | Dónde / Cómo se cumple |
|---|-----------|:------:|------------------------|
| 1 | **Maestro-detalle** aplicado donde corresponda, de forma consistente, cuidando integridad y sin duplicar datos | ✅ | `Expediente` ↔ `Persona` (`@OneToMany` con cascada y `orphanRemoval`) y `Parroquia` ↔ `SectorFilial`. UI: `CasoSocialFormFrame` y `CatalogoParroquiasFrame`. |
| 2 | **Parroquia** y demás campos de selección: sustituir texto libre por opciones seleccionables provenientes de datos persistentes; reutilizar catálogo o crearlo | ✅ | Nuevas tablas/entidades `Parroquia` y `SectorFilial` (BD) con combos en cascada; campos de `Persona` con combos desde `Catalogos.java`. Sin ingreso libre. |
| 3 | **Caso Social**: no volver a pedir datos ya registrados; reutilizar/mostrar; gestionar familiares y ubicaciones por relaciones, no por duplicación | ✅ | `ExpedienteDAO.buscarConDetalle` recarga expediente + familiares; la ubicación vive en el maestro y la heredan los familiares (`Persona` no tiene ubicación propia). |
| 4 | **Reducir enlaces/navegación**; limitar al apartado de Caso Social; evitar enlaces redundantes | ✅ | `MenuPrincipalFrame` con un único botón "Casos Sociales" (se quitó la celda vacía); acciones concentradas en `CasoSocialListFrame`. |
| 5 | **Mejoras visuales** incrementales, sin rediseño ni cambiar la identidad visual | ✅ | Agrupación con `TitledBorder` estándar y buen espaciado, manteniendo el Swing plano original (Arial, `GridLayout`, `JOptionPane`). |
| 6 | **Base de datos y compatibilidad**: revisar esquema, no borrar datos ni romper relaciones; explicar cambios de esquema | ✅ | Cambios **aditivos y no destructivos** (`migracion_maestro_detalle.sql`): nuevas tablas + FK nulas; columnas viejas conservadas. |

---

## Proceso de trabajo solicitado

| # | Paso | Estado | Nota |
|---|------|:------:|------|
| 1 | Analizar proyecto y base de datos | ✅ | Se revisó toda la arquitectura antes de modificar. |
| 2 | Identificar archivos/componentes por corrección | ✅ | Ver `CAMBIOS_Y_JUSTIFICACIONES.md`. |
| 3 | Describir problemas y plan | ✅ | Documentado. |
| 4 | Implementar de forma incremental | ✅ | Por capas: entidades → DAO → UI. |
| 5 | Sin errores de compilación | ✅ | Compila limpio (24 clases, 0 errores). |
| 6 | Relaciones entre entidades funcionando | ✅ | Maestro-detalle probado; login/consulta verificados contra la BD. |
| 7 | Sin duplicación de datos | ✅ | Catálogos + validación de número de ficha único. |
| 8 | Campos seleccionables sin ingreso libre | ✅ | Combos no editables en parroquia/sector y campos de `Persona`. |
| 9 | Reutilización de datos del Caso Social | ✅ | Carga de expediente + familiares al editar. |
| 10 | Navegación más simple y organizada | ✅ | Un solo acceso; acciones agrupadas. |
| 11 | Revisión general de la interfaz | ✅ | Pantallas consistentes con el estilo original. |

---

## Restricciones respetadas

| Restricción | Estado | Nota |
|-------------|:------:|------|
| No eliminar funcionalidades sin justificar | ✅ | Nada se eliminó; solo se agregó. |
| No cambiar tecnologías/frameworks | ✅ | Sigue siendo Java + Swing + JPA/Hibernate + MySQL. |
| No hacer cambios innecesarios fuera de alcance | ✅ | Solo el módulo pedido; login/registro intactos. |
| No duplicar lógica ni código | ✅ | DAO y catálogos reutilizados; helpers compartidos. |
| Reutilizar componentes/entidades/catálogos | ✅ | Se reutilizó `TipoAsistencia`, `JPAUtil`, patrones existentes. |
| Buenas prácticas y separación de responsabilidades | ✅ | Capas model / dao / ui / util bien separadas. |
| Indicar si un cambio afecta datos existentes | ✅ | Se documentó que los cambios de BD son no destructivos. |

---

## Puntos que requieren revisión manual del equipo

- Configurar en `persistence.xml` el **usuario/clave de MySQL** de cada integrante.
- Ajustar los **nombres reales** de las parroquias/sectores (los sembrados son de ejemplo).
- Las contraseñas de usuario se guardan en **texto plano** (igual que la versión
  original); si se requiere, se podría agregar encriptación más adelante.
