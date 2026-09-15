<div align="center">

<img src="app/src/main/ic_launcher-playstore.png" alt="PetMatch" width="160">

PetMatch

Aplicación Android para la consulta y administración de mascotas

Cliente móvil nativo conectado a servicios web PHP para gestionar usuarios, mascotas, contenido multimedia y reportes.

<br>

<img src="https://img.shields.io/badge/Android-API_28%2B-3DDC84?style=flat-square&logo=android&logoColor=white" alt="Android API 28+">
<img src="https://img.shields.io/badge/Java-11-ED8B00?style=flat-square&logo=openjdk&logoColor=white" alt="Java 11">
<img src="https://img.shields.io/badge/Gradle-8.11.1-02303A?style=flat-square&logo=gradle&logoColor=white" alt="Gradle 8.11.1">
<img src="https://img.shields.io/badge/Android_Studio-3DDC84?style=flat-square&logo=androidstudio&logoColor=white" alt="Android Studio">
<img src="https://img.shields.io/badge/API-PHP-777BB4?style=flat-square&logo=php&logoColor=white" alt="API PHP">

<br><br>

Descripción ·
Funcionalidades ·
Arquitectura ·
Instalación ·
Servicios ·
Estado

</div>

📖 Descripción

PetMatch es una aplicación Android orientada al registro, consulta y administración de información sobre mascotas. Se comunica por Internet con servicios PHP para autenticar usuarios, verificar registros por correo, consultar información y cargar imágenes o videos.

Después del inicio de sesión, la aplicación dirige a cada persona hacia una interfaz diferente según su nivel de acceso.

Aplicación

Configuración

Plataforma

Android nativo

Lenguaje

Java 11

Android mínimo

Android 9 — API 28

SDK de compilación

API 35

Perfiles

Administrador, operador y usuario

Backend

Servicios web PHP externos

Alcance actual: el código permite registrar, buscar, visualizar y administrar mascotas. No se encontró un flujo implementado para adopciones, emparejamiento, mensajería o seguimiento de solicitudes.

✨ Funcionalidades

Acceso y registro

Inicio de sesión con nombre de usuario y contraseña.

Registro de nuevos usuarios con validación de datos.

Generación, envío, verificación y reenvío de códigos por correo.

Corrección del correo antes de completar el registro.

Sesión local mediante SharedPreferences.

Redirección automática según el nivel de acceso.

Cierre de sesión y limpieza de los datos locales.

Funciones por perfil

Perfil

Funciones principales

Usuario

Buscar mascotas, consultar información general, abrir fichas detalladas, visualizar imágenes y reproducir videos. También puede consultar su perfil y la sección «Acerca de nosotros».

Operador

Registrar, buscar, actualizar y eliminar mascotas. Puede seleccionar y cargar imágenes y videos, además de consultar el reporte de mascotas.

Administrador

Gestionar mascotas y operadores, modificar o eliminar cuentas de operadores y consultar reportes de mascotas y usuarios.

Información registrada de una mascota

Nombre · Especie · Género · Raza · Edad · Descripción · Estado · Fecha de ingreso · Imagen · Video · Responsable

🧰 Stack tecnológico

Tecnología

Función dentro del proyecto

Java 11

Actividades, fragmentos, adaptadores y lógica de la aplicación

XML + Material Components

Construcción de pantallas y navegación

View Binding

Acceso a los componentes definidos en XML

Volley 1.2.1

Peticiones GET/POST y procesamiento de JSON

Glide 4.16.0

Descarga y presentación de imágenes remotas

WebView

Reproducción de videos y presentación de reportes

SharedPreferences

Conservación de la sesión y datos del usuario

PHP

Servicios externos de autenticación, CRUD y archivos

Gradle 8.11.1

Dependencias, compilación y generación del APK

Android Gradle Plugin 8.10.1

Configuración del módulo Android

🏗️ Arquitectura

El repositorio contiene únicamente el cliente Android. La aplicación utiliza actividades como contenedores, fragmentos para cada módulo, adaptadores para las listas y servicios PHP externos para la persistencia.

flowchart LR
    U["Usuario"] --> APP["Aplicación Android"]
    APP --> NET["Volley y HTTP"]
    NET --> API["Servicios PHP"]
    API --> DATA["JSON, archivos y reportes"]
    DATA --> APP

Flujo de acceso

flowchart TD
    LOGIN["Inicio de sesión"] --> ROLE{"Nivel recibido"}
    ROLE -->|admin| ADMIN["Gestión completa"]
    ROLE -->|operador| OP["Gestión de mascotas"]
    ROLE -->|usuario| USER["Consulta de mascotas"]

Organización del código

app/src/main/
├── AndroidManifest.xml
├── java/com/example/proyectoinvestigacion/
│   ├── MainActivity*.java
│   ├── *Adapter*.java
│   ├── URL.java
│   ├── vistas/
│   └── vistasusuarios/
└── res/
    ├── drawable/
    ├── layout/
    ├── menu/
    └── values/

🔄 Datos intercambiados

El backend y el esquema de la base de datos no están incluidos. Los siguientes campos fueron identificados en las solicitudes y respuestas del cliente:

Recurso

Campos utilizados

Usuario

idUsuario, nombreUsuario, nombre, apellido, correo, nivel

Mascota

idMascota, nombre, especie, genero, raza, edad, descripcion, estado, rutaImagen, rutaVideo, fechaIngreso, registradoPor

Especie

nombre

Verificación

Correo electrónico y código temporal

Algunos nombres cambian entre respuestas, por ejemplo idMascota e idmascota. Un backend compatible debe respetar el formato esperado por cada pantalla.

🚀 Instalación

Requisitos

Android Studio.

JDK 17 para ejecutar Android Gradle Plugin 8.10.1.

Android SDK 35.

Dispositivo o emulador con Android 9 — API 28 o superior.

Conexión a Internet.

Backend PHP compatible y disponible.

El código fuente utiliza compatibilidad Java 11, pero Gradle debe ejecutarse con JDK 17 debido a la versión del complemento de Android.

<details>
<summary><strong>Ver instalación paso a paso</strong></summary>

1. Clonar el repositorio

git clone https://github.com/Juanmanzan/Pet_Match_app.git
cd Pet_Match_app

2. Abrir el proyecto

Iniciar Android Studio.

Seleccionar Open.

Elegir la carpeta Pet_Match_app.

Esperar la sincronización de Gradle.

Confirmar que Gradle utiliza JDK 17.

3. Verificar el backend

La dirección base de varios servicios se encuentra en:

app/src/main/java/com/example/proyectoinvestigacion/URL.java

También existen direcciones completas en:

vistas/modificarmascotasFragment.java;

vistas/modificarOperadoresFragment.java;

vistasusuarios/inicioUsuarioFragment.java.

Si cambia el servidor, deben actualizarse esas direcciones junto con URL.java.

4. Ejecutar

Iniciar un emulador o conectar un dispositivo con depuración USB.

Seleccionar el módulo app.

Presionar Run app.

</details>

Compilación por consola

Sistema

Comando

Windows

.\gradlew.bat assembleDebug

Linux/macOS

bash gradlew assembleDebug

El APK se genera normalmente en:

app/build/outputs/apk/debug/app-debug.apk

El backend PHP y su base de datos no forman parte del repositorio. Sin ellos, las operaciones de inicio de sesión, registro, consulta y administración no estarán disponibles.

🌐 Servicios web

<details>
<summary><strong>Ver endpoints consumidos por la aplicación</strong></summary>

Grupo

Servicio

Método

Propósito

Acceso

validar_usuarios.php

POST

Validar credenciales y obtener el nivel

Acceso

generarcodigo.php

POST

Enviar el código de verificación

Acceso

verificar_codigo.php

POST

Comprobar el código recibido

Acceso

registrar_usuario.php

POST

Registrar al usuario

Mascotas

listar_inicio_mascotas.php

GET

Mostrar y buscar mascotas

Mascotas

detalles_mascota.php

GET

Consultar la ficha completa

Mascotas

listar_especie.php

GET

Obtener las especies

Mascotas

insertar_mascota.php

POST

Registrar una mascota

Mascotas

listar_mascotas.php

GET

Buscar mascotas para administrarlas

Mascotas

actualizar_mascota.php

POST

Modificar una mascota

Mascotas

eliminar_mascota.php

POST

Eliminar una mascota

Archivos

subir_imagen.php

POST

Cargar una imagen

Archivos

subir_video.php

POST

Cargar un video

Operadores

insertar_operador.php

POST

Registrar un operador

Operadores

listar_operador.php

GET

Buscar operadores

Operadores

actualizar_operador.php

POST

Modificar un operador

Operadores

eliminar_operador.php

POST

Eliminar un operador

Reportes

reporte_mascota.php

WebView

Mostrar el reporte de mascotas

Reportes

reporte_usuarios.php

WebView

Mostrar el reporte de usuarios

Las imágenes y los videos se transfieren mediante solicitudes multipart/form-data.

</details>

🔐 Seguridad

Uso de HTTPS en la dirección configurada actualmente.

Verificación del correo mediante código antes del registro.

Validación local de campos obligatorios y formato del correo.

Separación de interfaces según el nivel recibido.

Almacenamiento de la sesión dentro de las preferencias privadas de la aplicación.

Limpieza de la sesión al cerrarla.

Confirmación previa para operaciones de eliminación.

La autenticación y la autorización definitivas deben validarse en el servidor. Los controles de la aplicación móvil no deben considerarse por sí solos una barrera de seguridad.

