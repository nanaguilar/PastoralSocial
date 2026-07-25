# Pastoral Social App — Guía de ejecución

Aplicación de escritorio (Java + Swing) para la gestión de **Casos Sociales**
(expedientes) y sus **familiares**, con persistencia en **MySQL** vía
**JPA / Hibernate**.

---

## 1. Requisitos

| Herramienta | Versión recomendada |
|-------------|---------------------|
| JDK         | 8 o superior (probado con JDK 24) |
| Eclipse IDE | cualquier versión reciente |
| MySQL       | 8.x (con MySQL Workbench para administrar) |

> Las librerías de Hibernate, javassist y el conector MySQL **ya vienen incluidas**
> en la carpeta `lib/`, así que no hay que descargar nada más.

---

## 2. Crear la base de datos

1. Abrí **MySQL Workbench** y conectate a tu servidor local.
2. Abrí y ejecutá el script:  **`db/base de datos.sql`**
   - Crea la base `pastoral_social`, sus tablas y el usuario inicial.
3. (Opcional) Ejecutá **`db/migracion_maestro_detalle.sql`** si querés crear de una
   vez los catálogos de parroquias/sectores. **No es obligatorio**: la aplicación
   crea esas tablas automáticamente al primer arranque y las siembra sola.

---

## 3. Configurar tu usuario de MySQL

Abrí el archivo:

```
src/main/resources/META-INF/persistence.xml
```

y cambiá estas dos líneas por **tus** credenciales de MySQL (las mismas de Workbench):

```xml
<property name="hibernate.connection.username" value="root"/>
<property name="hibernate.connection.password" value="root"/>
```

> Viene con `root` / `root` como valor por defecto. Cada integrante pone la suya.

---

## 4. Importar el proyecto en Eclipse

1. **File → Import… → General → Existing Projects into Workspace → Next**
2. **Select root directory** → elegí la carpeta `PastoralSocialApp` (esta).
3. Verificá que aparezca marcado el proyecto **PastoralSocialApp** → **Finish**.
4. Las librerías se enlazan solas (rutas relativas a `lib/`). En el árbol,
   bajo **Referenced Libraries**, deberías ver los 10 `.jar` (incluido
   `javassist-3.29.2-GA.jar`).

Si Eclipse no compilara solo: **Project → Clean… → PastoralSocialApp → Clean**.

---

## 5. Ejecutar

1. Expandí `src/main/java → ui`.
2. Clic derecho en **`LoginFrame.java` → Run As → Java Application**.
3. Iniciá sesión con el usuario que trae la base:
   - **Usuario:** `admin`
   - **Contraseña:** `admin123`
4. En el menú → **Casos Sociales**.

---

## 6. Cómo usar el módulo de Casos Sociales

- **Nuevo caso:** completás los datos del expediente (maestro). La **parroquia** y
  el **sector** se eligen de una lista (combos en cascada: al elegir parroquia se
  filtran sus sectores). La **dirección/ubicación se registra una sola vez** aquí.
- **Familiares (detalle):** con *Agregar familiar* abrís un formulario donde el
  sexo, parentesco, país, migración, educación y seguro son **listas
  seleccionables** (no texto libre). Los familiares **heredan la ubicación** del
  expediente, no se repite por persona.
- **Abrir / Editar:** reabrís un caso existente y se cargan todos sus datos y
  familiares (no se piden de nuevo).
- **Parroquias y sectores:** botón para administrar el catálogo (alta/edición/baja).

---

## 7. Problemas frecuentes

| Síntoma | Causa / Solución |
|---------|------------------|
| `Access denied for user 'root'` | La clave del `persistence.xml` no coincide con tu MySQL. Corregila (paso 3). |
| `Unknown database 'pastoral_social'` | No ejecutaste `db/base de datos.sql` (paso 2). |
| `Communications link failure` | MySQL no está encendido. |
| `invalid constant type: 18` (javassist) | Falta usar `javassist-3.29.2-GA.jar`. Ya viene en `lib/`; hacé **Clean** y verificá que en *Referenced Libraries* NO esté una javassist vieja. |
| `SLF4J: Failed to load class ... StaticLoggerBinder` | **Inofensivo** (solo avisa que no hay logger). No afecta nada. |

---

## 8. Estructura del proyecto

```
PastoralSocialApp/
├── .classpath / .project        (configuración Eclipse, rutas relativas)
├── lib/                         (Hibernate, javassist 3.29, conector MySQL)
├── db/
│   ├── base de datos.sql        (esquema + usuario admin)
│   └── migracion_maestro_detalle.sql  (catálogos parroquia/sector, opcional)
├── src/main/java/
│   ├── model/   (entidades JPA)
│   ├── dao/     (acceso a datos)
│   ├── ui/      (pantallas Swing)
│   └── util/    (JPAUtil, catálogos)
└── src/main/resources/META-INF/persistence.xml
```
