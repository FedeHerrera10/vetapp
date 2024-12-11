# Sistema de Turnos para Veterinaria

## Descripción General
Este proyecto consiste en un sistema de gestión de turnos diseñado para veterinarias, que permite la administración de usuarios, mascotas, servicios, y turnos de manera eficiente y moderna. Está desarrollado con un backend en **Spring Boot** y un frontend en **React**, utilizando una arquitectura modular y escalable.

---

## Funcionalidades Clave

### 1. Gestión de Usuarios
- **Roles:**
  - Administrador
  - Veterinario
  - Cliente
- **Características:**
  - Registro de usuarios y autenticación con JWT.
  - Cada cliente puede registrar y gestionar información de sus mascotas.

### 2. Gestión de Mascotas
- CRUD de mascotas asociado a cada cliente.
- Registro de datos importantes:
  - Especie
  - Raza
  - Edad
  - Historial médico
  - Vacunas

### 3. Gestión de Servicios
- CRUD de servicios ofrecidos por la veterinaria:
  - Consulta médica
  - Vacunación
  - Baño y peluquería
  - Emergencias
- Configuración de horarios disponibles para cada servicio.

### 4. Gestión de Turnos
- Reservar turnos para una mascota específica.
- Selección de:
  - Servicio
  - Veterinario (opcional)
  - Fecha y horario disponible
- Gestión de estados del turno:
  - RESERVADO
  - CANCELADO
  - FINALIZADO
- Notificaciones por correo o SMS (opcional).

### 5. Panel Administrativo
- Herramientas para:
  - Gestión de veterinarios
  - Configuración de horarios
  - Gestión de turnos
  - Administración de clientes
- Reportes y estadísticas:
  - Servicios más solicitados
  - Historial de turnos

### 6. Historial Médico
- Registro de consultas y tratamientos pasados para cada mascota.
- Posibilidad de adjuntar:
  - Notas
  - Tratamientos
  - Recetas médicas

---
<br>

## API Restful a construir
CRUD para usuarios, mascotas, servicios y turnos.

Tablas para:
 - Usuarios: Información básica, roles.
 - Mascotas: Datos de las mascotas asociadas a un cliente.
 - Servicios: Tipos de servicios ofrecidos.
 - Turnos: Relación entre usuario, mascota, servicio y horario.

<br>

---
## Tecnologías Utilizadas

### Backend:
- **Spring Boot**:
  - Spring Security (para autenticación y autorización).
  - Spring Data JPA (para interacción con la base de datos).
  - Spring Mail (para notificaciones opcionales).


### Base de Datos:
- **MySQL** :
  - Gestión de datos relacionales.

### Despliegue:
- **Backend:** Docker .

---

## Instalación y Configuración

1. **Clonar el repositorio:**
   ```bash
   git clone <repositorio_url>
   ```

2. **Backend:**
   - Configurar las variables de entorno (como base de datos, claves JWT, etc.).
   - Construir y ejecutar la aplicación Spring Boot:
     ```bash
     ./mvnw spring-boot:run
     ```
---

