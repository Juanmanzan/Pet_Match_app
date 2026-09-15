<div align="center">

<img src="app/src/main/ic_launcher-playstore.png" alt="Logotipo de PetMatch" width="220">

PetMatch

Aplicación Android para la consulta y administración de mascotas

Aplicación móvil nativa desarrollada en Java que permite registrar usuarios, consultar mascotas y administrar operadores, contenido multimedia y reportes mediante servicios web PHP.







</div>

Descripción

PetMatch es una aplicación Android orientada a la publicación, consulta y administración de información sobre mascotas. La aplicación se comunica por Internet con un conjunto de servicios PHP para autenticar usuarios, verificar registros por correo, consultar datos y cargar imágenes o videos.

El sistema diferencia tres niveles de acceso: administrador, operador y usuario. Después del inicio de sesión, cada persona es dirigida automáticamente a la interfaz correspondiente a su nivel.

El nombre del proyecto hace referencia a PetMatch, pero el código disponible se concentra actualmente en el registro, búsqueda, visualización y administración de mascotas. No se encontró un flujo implementado para solicitudes de adopción, emparejamiento entre mascotas, mensajería o seguimiento de adopciones.

Funcionalidades

Acceso y registro

Pantalla inicial y acceso al formulario de inicio de sesión.

Autenticación mediante nombre de usuario y contraseña.

Registro de nuevos usuarios.

Validación de datos obligatorios y formato del correo electrónico.

Generación y envío de un código de verificación por correo.

Comprobación del código antes de completar el registro.

Reenvío del código y posibilidad de corregir el correo.

Conservación de la sesión mediante SharedPreferences.

Redirección automática según el nivel de acceso.

Cierre de sesión y eliminación de los datos locales de la sesión.

Usuario

Consulta del listado de mascotas disponibles.

Búsqueda de mascotas mediante texto.

Visualización de nombre, raza, edad, género e imagen.

Acceso a una ficha con información detallada de la mascota.

Reproducción del video asociado dentro de un WebView.

Consulta de los datos del perfil autenticado.

Sección «Acerca de nosotros» y créditos del proyecto.

Operador

Registro de mascotas con:

nombre;

especie;

género;

raza;

edad;

descripción;

estado;

fecha de ingreso;

imagen;

video;

responsable del registro.

Selección de imágenes y videos desde el dispositivo.

Carga de archivos multimedia al servidor mediante solicitudes multipart/form-data.

Búsqueda y listado de mascotas registradas.

Actualización de datos, imagen y video.

Eliminación de mascotas con confirmación previa.

Consulta del reporte de mascotas dentro de un WebView.

Administrador

Acceso a todas las opciones de gestión de mascotas.

Registro de nuevos operadores.

Búsqueda y consulta de operadores.

Actualización de nombres, apellidos, correo, usuario y contraseña de operadores.

Eliminación de operadores con confirmación.

Consulta de reportes de mascotas.

Consulta de reportes de usuarios.

Navegación mediante barra inferior entre los módulos administrativos.

Tecnologías utilizadas

Área

Tecnología

Uso en el proyecto

Plataforma

Android nativo

Ejecución de la aplicación móvil

Lenguaje

Java 11

Actividades, fragmentos, adaptadores y lógica de negocio

SDK

Compile/Target SDK 35

Compilación y compatibilidad con Android 15

Compatibilidad

Min SDK 28

Android 9 o superior

Interfaz

XML, Material Components y ConstraintLayout

Construcción de pantallas y navegación

Enlace de vistas

View Binding

Acceso seguro a los componentes XML

Red

Volley 1.2.1

Solicitudes GET/POST y procesamiento de JSON

Imágenes

Glide 4.16.0

Descarga y presentación de imágenes remotas

Contenido web

WebView

Videos y reportes generados por el servidor

Sesión local

SharedPreferences

Datos del usuario, nivel y estado de autenticación

Backend consumido

Servicios PHP

Autenticación, registros, CRUD, archivos y reportes

Construcción

Gradle 8.11.1 y AGP 8.10.1

Gestión de dependencias y generación del APK

Arquitectura general

El repositorio contiene el cliente Android. La aplicación organiza sus pantallas mediante actividades y fragmentos, utiliza adaptadores para presentar listas y delega la persistencia principal a servicios web PHP externos.

app/src/main/
├── AndroidManifest.xml        # Permisos y actividades
├── java/.../
│   ├── MainActivity*.java     # Inicio, autenticación y contenedores de navegación
│   ├── *Adapter*.java         # Listas de mascotas y operadores
│   ├── URL.java               # Dirección base de algunos servicios
│   ├── vistas/                # Módulos de administrador y operador
│   └── vistasusuarios/        # Inicio, detalle, perfil y créditos
└── res/
    ├── layout/                # Interfaces XML
    ├── menu/                  # Menús de navegación
    ├── drawable/              # Imágenes e iconos
    └── values/                # Colores, textos y temas

Flujo general de comunicación:

flowchart TD
    A[Aplicación Android] --> B[Volley o HttpURLConnection]
    B --> C[Servicios PHP]
    C --> D[Datos JSON o texto]
    C --> E[Imágenes, videos y reportes]
    D --> A
    E --> A

Flujo de acceso por nivel:

flowchart TD
    A[Inicio de sesión] --> B{Nivel recibido}
    B -->|admin| C[Gestión completa]
    B -->|operador| D[Gestión de mascotas]
    B -->|usuario| E[Consulta de mascotas]

Datos manejados por el cliente

El código del backend y el esquema de su base de datos no están incluidos en este repositorio. No obstante, el cliente permite identificar los siguientes datos intercambiados con la API:

Recurso

Campos utilizados por la aplicación

Usuario

idUsuario, nombreUsuario, nombre, apellido, correo, nivel

Mascota

idMascota, nombre, especie, genero, raza, edad, descripcion, estado, rutaImagen, rutaVideo, fechaIngreso, registradoPor

Especie

nombre

Verificación

correo y código temporal

Los nombres exactos de algunos campos cambian entre respuestas, por ejemplo idMascota e idmascota. Cualquier backend compatible debe respetar los nombres que espera cada pantalla.

Servicios web consumidos

Servicio

Método observado

Propósito

validar_usuarios.php

POST

Validar credenciales y devolver el nivel del usuario

generarcodigo.php

POST

Generar y enviar el código de verificación

verificar_codigo.php

POST

Comprobar el código recibido

registrar_usuario.php

POST

Completar el registro del usuario

listar_inicio_mascotas.php

GET

Presentar y buscar mascotas para el usuario

detalles_mascota.php

GET

Consultar la ficha completa de una mascota

listar_especie.php

GET

Obtener las especies disponibles

insertar_mascota.php

POST

Registrar una mascota

listar_mascotas.php

GET

Buscar mascotas para su administración

actualizar_mascota.php

POST

Modificar los datos de una mascota

eliminar_mascota.php

POST

Eliminar una mascota

subir_imagen.php

POST multipart

Almacenar una imagen en el servidor

subir_video.php

POST multipart

Almacenar un video en el servidor

insertar_operador.php

POST

Registrar un operador

listar_operador.php

GET

Buscar operadores

actualizar_operador.php

POST

Modificar un operador

eliminar_operador.php

POST

Eliminar un operador

reporte_mascota.php

WebView

Mostrar el reporte de mascotas

reporte_usuarios.php

WebView

Mostrar el reporte de usuarios

Requisitos

Android Studio con soporte para proyectos Java.

JDK 17 para ejecutar Android Gradle Plugin 8.10.1.

Android SDK 35 instalado.

Dispositivo físico o emulador con Android 9 (API 28) o superior.

Conexión a Internet.

Acceso a un backend PHP compatible con los servicios enumerados.

Aunque el código fuente se compila con compatibilidad Java 11, la versión del Android Gradle Plugin utilizada requiere ejecutar Gradle con JDK 17.

Instalación y ejecución

1. Clonar el repositorio

git clone https://github.com/Juanmanzan/Pet_Match_app.git
cd Pet_Match_app

2. Abrir el proyecto

Iniciar Android Studio.

Seleccionar Open.

Elegir la carpeta Pet_Match_app.

Esperar a que finalice la sincronización de Gradle.

Confirmar que el proyecto utiliza JDK 17 en la configuración de Gradle.

3. Comprobar la conexión con el backend

La dirección principal se encuentra en:

app/src/main/java/com/example/proyectoinvestigacion/URL.java

Algunas pantallas también conservan direcciones completas directamente en el código:

vistas/modificarmascotasFragment.java;

vistas/modificarOperadoresFragment.java;

vistasusuarios/inicioUsuarioFragment.java.

Antes de ejecutar la aplicación se debe confirmar que el servidor y todos los scripts PHP se encuentran disponibles. Si se utiliza otro servidor, hay que actualizar tanto URL.java como las direcciones completas anteriores.

El backend PHP y su base de datos no forman parte de este repositorio. Sin esos servicios, las pantallas locales pueden abrirse, pero el inicio de sesión, registro, consultas, cargas y modificaciones no funcionarán.

4. Ejecutar en Android Studio

Crear o iniciar un emulador con API 28 o superior, o conectar un dispositivo con depuración USB.

Seleccionar el módulo app.

Presionar Run app.

La aplicación solicita únicamente el permiso de Internet en el manifiesto. La selección de imágenes y videos utiliza el selector de documentos del sistema.

Compilación por consola

En Windows:

.\gradlew.bat assembleDebug

En Linux o macOS:

bash gradlew assembleDebug

El APK de depuración se genera normalmente en:

app/build/outputs/apk/debug/app-debug.apk

Para generar una versión de publicación:

.\gradlew.bat assembleRelease

La configuración actual no incluye una firma de producción. Para distribuir la aplicación se debe crear un keystore y configurar la firma de la variante release.

Seguridad implementada

Uso de HTTPS en la dirección actual de los servicios.

Verificación del correo mediante un código antes del registro.

Validación local de campos obligatorios y formato del correo.

Separación visual y funcional según el nivel recibido desde el servidor.

Persistencia del estado de inicio de sesión en almacenamiento privado de la aplicación.

Limpieza de los datos de sesión al cerrar sesión.

Confirmaciones antes de eliminar mascotas u operadores.

La autenticación y autorización definitivas deben validarse siempre en el servidor. El cliente móvil no debe considerarse una barrera de seguridad porque sus controles pueden ser modificados o evitados.





