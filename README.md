# 🏫 API Sistema de Inscripciones - E.E.T. N° 3100

Esta es la API RESTful desarrollada en Spring Boot para la gestión académica y de inscripciones de la E.E.T. N° 3100. Este documento sirve como guía oficial para el equipo de Frontend (Flutter) sobre cómo comunicarse con el servidor.

---

## 🛡️ Medidas de Seguridad Generales

Esta API implementa altos estándares de seguridad para proteger los datos de los alumnos e institución:

1. **Autenticación por JWT (JSON Web Token):** A excepción del Login y Registro, TODAS las rutas requieren que se envíe el token JWT en las cabeceras (Headers) de la petición HTTP.
    * **Key:** `Authorization`
    * **Value:** `Bearer <tu_token_aqui>`

2. **Control de Accesos por Roles (RBAC):**
    * `TUTOR`: Rol por defecto. Solo puede gestionar a sus propios hijos y solicitudes.
    * `SECRETARIO`: Puede gestionar cursos, aprobar/rechazar inscripciones y enviar notificaciones.
    * `ADMINISTRADOR`: Tiene acceso total, incluyendo el cambio de roles del personal.

3. **Protección IDOR (Insecure Direct Object Reference):**
   El backend verifica matemáticamente el árbol genealógico. Un `TUTOR` **jamás** podrá acceder, ver, ni modificar los datos o inscripciones de un `Student` que no esté a su cargo, aunque intente adivinar el ID en la URL. El servidor le devolverá un error de seguridad instantáneo.

4. **Formato de Fechas:** Todas las fechas enviadas en los JSON deben respetar el formato estricto: `YYYY-MM-DD` (Ejemplo: `"2010-05-15"`).

---

## 🌐 Endpoints (Rutas de la API)

**URL Base de la API:** `http://localhost:8080/api/v1`

---

### 1. 🔐 Autenticación (`/auth`)

Rutas públicas para el ingreso al sistema. No requieren token.

#### Registro de Usuario

* **Ruta:** `POST /auth/register`
* **Acceso:** Público
* **Recibe (JSON):**

```json
{
  "email": "padre@gmail.com",
  "password": "Password123!",
  "dni": 30123456,
  "cuil": "20301234567",
  "firstName": "Juan",
  "lastName": "Perez",
  "userPhone": "3874123456",
  "userAddress": "Av. San Martin 123",
  "dateOfBirth": "1980-05-15",
  "ocupation": "Comerciante",
  "relationship": "Padre"
}
```

* **Devuelve:** `201 Created` - Mensaje de éxito.

---

#### Iniciar Sesión (Login)

* **Ruta:** `POST /auth/login`
* **Acceso:** Público
* **Recibe (JSON):**

```json
{
  "emailOrCuil": "padre@gmail.com",
  "password": "Password123!"
}
```

* **Devuelve:** `200 OK` - Devuelve el Token que se usará en el resto de la app.

```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJwYWRyZUBnbWF...",
  "email": "padre@gmail.com",
  "firstName": "Juan",
  "lastName": "Perez",
  "roles": ["TUTOR"]
}
```

---

### 2. 👨‍👩‍👧 Usuarios y Perfil (`/users`)

#### Mi Perfil

* **Ruta:** `GET /users/me`
* **Acceso:** Cualquier usuario autenticado (`TUTOR`, `SECRETARIO`, `ADMINISTRADOR`)
* **Devuelve:** `200 OK` con los datos personales del usuario logueado.

---

#### Ver Personal Administrativo

* **Ruta:** `GET /users/staff`
* **Acceso:** `TUTOR`, `SECRETARIO`, `ADMINISTRADOR`
* **Descripción:** Permite a los padres ver la lista de secretarios o directivos para contacto.

---

#### Cambiar Rol de Usuario

* **Ruta:** `PUT /users/{userId}/role`
* **Acceso:** `SOLO ADMINISTRADOR`
* **Recibe (JSON):**

```json
{
  "newRole": "SECRETARIO"
}
```

---

### 3. 🎒 Gestión de Alumnos (`/students`)

#### Registrar un Hijo/Alumno a Cargo

* **Ruta:** `POST /students`
* **Acceso:** `TUTOR` (Se asigna automáticamente al tutor que envía la petición)
* **Recibe (JSON):**

```json
{
  "studentDni": 50123456,
  "studentCuil": "20501234567",
  "firstName": "Lucas",
  "lastName": "Perez",
  "studentEmail": "lucas@gmail.com",
  "studentPhone": "3874112233",
  "birthdate": "2010-08-20",
  "birthplace": "Salta Capital",
  "address": "Av. San Martin 123"
}
```

---

#### Mis Alumnos a Cargo

* **Ruta:** `GET /students/me`
* **Acceso:** `TUTOR`
* **Devuelve:** `200 OK` - Lista de todos los alumnos asociados al tutor logueado.

---

#### Actualizar Datos del Alumno

* **Ruta:** `PUT /students/{studentId}`
* **Acceso:** `TUTOR` (Solo si es dueño de ese `studentId`)
* **Recibe:** Mismo JSON que en el `POST`.

---

### 4. 📚 Cursos y Oferta Académica (`/courses`)

#### Crear un Curso

* **Ruta:** `POST /courses/create`
* **Acceso:** `SECRETARIO`, `ADMINISTRADOR`
* **Recibe (JSON):**

```json
{
  "year": 2026,
  "studyYear": 1,
  "division": 3,
  "advancedCycle": false,
  "shift": "Mañana",
  "speciality": "Ciclo Básico",
  "maxCapacity": 35
}
```

---

#### Ver Cursos Disponibles

* **Ruta:** `GET /courses/available?year=2026`
* **Acceso:** `TUTOR` (Cualquier autenticado)
* **Devuelve:** `200 OK` - Lista de cursos del año solicitado indicando `availablePlaces` y la demanda `pendingRequests`.

---

#### Clonar Cursos al Nuevo Año

* **Ruta:** `POST /courses/clone/{oldYear}`
* **Acceso:** `SECRETARIO`, `ADMINISTRADOR`
* **Descripción:** Clona toda la estructura académica del año viejo al año en curso.

---

### 5. 📝 Trámites de Inscripción (`/registrations`)

#### Enviar Solicitud de Inscripción

* **Ruta:** `POST /registrations`
* **Acceso:** `TUTOR`
* **Recibe (JSON):**

```json
{
  "studentId": 1,
  "idCourse": 1,
  "institutionOfOrigin": "Escuela N° 1234",
  "photoAuthorization": true,
  "relationship": "Padre",
  "healthProblem": false,
  "inclusionStudent": false,
  "contactImpediment": false,
  "legalProceedings": false
}
```

* **Devuelve:** `201 Created` - Inscripción en estado `PENDING`.

---

#### Mis Trámites (Bandeja del Tutor)

* **Ruta:** `GET /registrations/me`
* **Acceso:** `TUTOR`
* **Devuelve:** `200 OK` - Lista de los estados de inscripción de sus hijos.

---

#### Bandeja de Entrada de Secretaría

* **Ruta:** `GET /registrations/pending`
* **Acceso:** `SECRETARIO`, `ADMINISTRADOR`
* **Devuelve:** `200 OK` - Lista de todas las inscripciones esperando aprobación en la escuela.

---

#### Acciones de la Secretaría (Botones en Flutter)

* **Aprobar:** `PUT /registrations/{id}/approve`
* **Rechazar:** `PUT /registrations/{id}/reject`
* **Proponer Reasignación:** `PUT /registrations/{id}/propose-reassignment?newCourseId={id}`

> Todos estos requieren rol `SECRETARIO` o `ADMINISTRADOR`.

---

#### Respuesta del Tutor a una Reasignación

* **Ruta:** `PUT /registrations/{id}/reply-reassignment?accepted=true`
* **Acceso:** `TUTOR` (Dueño del alumno)

---

#### Recuperar Última Inscripción (Para Autocompletar Formularios en Flutter)

* **Ruta:** `GET /registrations/student/{studentId}/latest`
* **Acceso:** `TUTOR` (Dueño del alumno)
* **Devuelve:** `200 OK` - Los datos de la inscripción del año pasado para precargar en pantalla.

---

### 6. 🔔 Sistema de Notificaciones (`/notifications`)

#### Enviar Aviso a un Tutor

* **Ruta:** `POST /notifications/create`
* **Acceso:** `SECRETARIO`, `ADMINISTRADOR`
* **Recibe (JSON):**

```json
{
  "userNotified": 2,
  "content": "Por favor acérquese a la institución con la fotocopia del DNI."
}
```

---

#### Ver Mis Notificaciones No Leídas

* **Ruta:** `GET /notifications/unread`
* **Acceso:** `TUTOR` (Cualquier autenticado)
* **Devuelve:** `200 OK` - Lista de notificaciones del usuario.

---

#### Marcar Notificación como Leída

* **Ruta:** `PUT /notifications/{idUserNotification}/read`
* **Acceso:** `TUTOR` (Dueño de la notificación)

---

#### Contador de Notificaciones (Campanita)

* **Ruta:** `GET /notifications/unread/count`
* **Acceso:** Cualquier autenticado
* **Devuelve:** `200 OK` - Un número entero indicando las alertas pendientes.

---

*Desarrollado para la E.E.T. N° 3100 — Salta*