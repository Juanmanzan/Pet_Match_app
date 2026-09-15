<div align="center">

<img src="app/src/main/ic_launcher-playstore.png" alt="PetMatch" width="160">

# PetMatch
**Aplicación Android para la consulta y administración de mascotas**  
*Cliente móvil nativo conectado a servicios web PHP para gestionar usuarios, mascotas, contenido multimedia y reportes.*

<br>

![Android](https://img.shields.io/badge/Android-API_28%2B-3DDC84?style=flat-square&logo=android&logoColor=white)
![Java](https://img.shields.io/badge/Java-11-ED8B00?style=flat-square&logo=openjdk&logoColor=white)
![Gradle](https://img.shields.io/badge/Gradle-8.11.1-02303A?style=flat-square&logo=gradle&logoColor=white)
![Android Studio](https://img.shields.io/badge/Android_Studio-3DDC84?style=flat-square&logo=androidstudio&logoColor=white)
![API PHP](https://img.shields.io/badge/API-PHP-777BB4?style=flat-square&logo=php&logoColor=white)

<br>

[Descripción](#-descripción) •
[Funcionalidades](#-funcionalidades) •
[Stack Tecnológico](#-stack-tecnológico) •
[Arquitectura](#-arquitectura) •
[Datos Intercambiados](#-datos-intercambiados) •
[Instalación](#-instalación) •
[Servicios Web](#-servicios-web) •
[Seguridad](#-seguridad)

</div>

---

## 📖 Descripción

**PetMatch** es una aplicación Android orientada al registro, consulta y administración de información sobre mascotas. Se comunica por Internet con servicios PHP para autenticar usuarios, verificar registros por correo, consultar información y cargar imágenes o videos.

Después del inicio de sesión, la aplicación dirige a cada persona hacia una interfaz diferente según su nivel de acceso.

| Parámetro | Detalle |
| :--- | :--- |
| **Plataforma** | Android nativo |
| **Lenguaje** | Java 11 |
| **Android Mínimo** | Android 9 — API 28 |
| **SDK Compilación** | API 35 |
| **Perfiles** | Administrador, Operador y Usuario |
| **Backend** | Servicios web PHP externos |

> **Alcance actual:** El código permite registrar, buscar, visualizar y administrar mascotas. No se encontró un flujo implementado para adopciones, emparejamiento, mensajería o seguimiento de solicitudes.

---

## ✨ Funcionalidades

### Acceso y Registro
* Inicio de sesión con nombre de usuario y contraseña.
* Registro de nuevos usuarios con validación de datos.
* Generación, envío, verificación y reenvío de códigos por correo.
* Corrección del correo antes de completar el registro.
* Sesión local mediante `SharedPreferences`.
* Redirección automática según el nivel de acceso.
* Cierre de sesión y limpieza de datos locales.

### Funciones por Perfil
| Perfil | Funciones Principales |
| :--- | :--- |
| **Usuario** | Buscar mascotas, consultar información general, abrir fichas detalladas, visualizar imágenes y reproducir videos. También puede consultar su perfil y la sección "Acerca de nosotros". |
| **Operador** | Registrar, buscar, actualizar y eliminar mascotas. Puede seleccionar y cargar imágenes y videos, además de consultar el reporte de mascotas. |
| **Administrador** | Gestionar mascotas y operadores, modificar o eliminar cuentas de operadores y consultar reportes de mascotas y usuarios. |

### Información Registrada de una Mascota
`Nombre` • `Especie` • `Género` • `Raza` • `Edad` • `Descripción` • `Estado` • `Fecha de Ingreso` • `Imagen` • `Video` • `Responsable`

---

## 🧰 Stack Tecnológico

| Tecnología | Función dentro del Proyecto |
| :--- | :--- |
| **Java 11** | Actividades, fragmentos, adaptadores y lógica de la aplicación |
| **XML + Material Components** | Construcción de pantallas y navegación |
| **View Binding** | Acceso directo a componentes definidos en XML |
| **Volley 1.2.1** | Peticiones HTTP (GET/POST) y procesamiento JSON |
| **Glide 4.16.0** | Descarga y visualización de imágenes remotas |
| **WebView** | Reproducción de videos y visualización de reportes |
| **SharedPreferences** | Almacenamiento local de sesión y datos de usuario |
| **PHP** | Servicios web backend para autenticación, CRUD y archivos |
| **Gradle 8.11.1** | Gestión de dependencias y proceso de compilación |
| **Android Gradle Plugin 8.10.1** | Configuración del módulo Android |

---

## 🏗️ Arquitectura

El repositorio contiene únicamente el cliente Android. La aplicación utiliza actividades como contenedores, fragmentos para cada módulo, adaptadores para las listas y servicios PHP externos para la persistencia.

```mermaid
flowchart LR
    U["Usuario"] --> APP["Aplicación Android"]
    APP --> NET["Volley / HTTP"]
    NET --> API["Servicios PHP"]
    API --> DATA["JSON, Archivos y Reportes"]
    DATA --> APP
