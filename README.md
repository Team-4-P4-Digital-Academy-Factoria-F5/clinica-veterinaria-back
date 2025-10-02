# 🐾 Clínica Veterinaria Margarita - Backend

Sistema de gestión backend para clínica veterinaria especializada en gatos y perros.

## 📋 Descripción

API REST desarrollada con Spring Boot para gestionar clientes, pacientes veterinarios, citas, tratamientos. Incluye sistema de autenticación, área de cliente con citas y mascotas y área de administradora para la veterinaria.

## 🛠️ Tecnologías

- **Java 17+**
- **Spring Boot 3.x**
  - Spring Data JPA
  - Spring Security
  - Spring Validation
  - Spring Mail
  - Spring Scheduler
- **MySQL**
- **BasicAuth**
- **Maven**
- **Postman/Swagger** (Documentación)

## 🗃️ Modelo de Datos

### Diagrama entidad/relación

```mermaid
erDiagram
    ROLE {
        BIGINT id_role PK
        VARCHAR name
    }
    USER {
        BIGINT id_user PK
        VARCHAR email
        VARCHAR password
    }
    PROFILE {
        BIGINT id_profile PK
        VARCHAR dni
        VARCHAR name
        VARCHAR first_surname
        VARCHAR second_surname
        VARCHAR phone_number
        BIGINT user_id FK
    }
    PATIENT {
        BIGINT id_patient PK
        VARCHAR identification_number
        VARCHAR name
        VARCHAR image
        INT age
        VARCHAR family
        VARCHAR breed
        VARCHAR sex
        BIGINT tutor_user_id FK
    }
    APPOINTMENT {
        BIGINT id_appointment PK
        DATETIME appointment_datetime
        BOOLEAN type
        TEXT reason
        VARCHAR status
        BIGINT patient_id FK
        BIGINT user_id FK
    }
    TREATMENT {
        BIGINT id_treatment PK
        BIGINT patient_id FK
        TEXT description
        DATETIME treatment_date
    }
    ROLES_USERS {
        BIGINT user_id FK
        BIGINT role_id FK
    }
    %% Relaciones
    USER ||--o{ ROLES_USERS : has_roles
    ROLE ||--o{ ROLES_USERS : role_of
    USER ||--|| PROFILE : has_profile
    PROFILE ||--|| USER : belongs_to
    USER ||--o{ PATIENT : tutors
    PATIENT ||--|| USER : has_tutor
    USER ||--o{ APPOINTMENT : books
    PATIENT ||--o{ APPOINTMENT : has_appointments
    PATIENT ||--o{ TREATMENT : has_treatments
```

### Diagrama de clases

```mermaid
classDiagram
    %% ENTIDADES
    class UserEntity {
        -id_user : Long <<PK>>
        -email : String
        -password : String
        -roles : Set~RoleEntity~
        -profile : ProfileEntity
        -patients : Set~PatientEntity~
    }
    class ProfileEntity {
        -id_profile : Long <<PK>>
        -dni : String
        -name : String
        -firstSurname : String
        -secondSurname : String
        -phoneNumber : String
        -user : UserEntity <<FK>>
    }
    class PatientEntity {
        -id_patient : Long <<PK>>
        -identificationNumber : String
        -name : String
        -image : String
        -age : int
        -family : String
        -breed : String
        -sex : String
        -tutor : UserEntity <<FK>>
        -appointments : Set~AppointmentEntity~
    }
    class AppointmentEntity {
        -id_appointment : Long <<PK>>
        -appointmentDatetime : LocalDateTime
        -type : Boolean
        -reason : String
        -status : AppointmentStatus
        -patient : PatientEntity <<FK>>
        -user : UserEntity <<FK>>
    }
    class TreatmentEntity {
        -id_treatment : Long <<PK>>
        -name : String
        -description : String
        -treatmentDate : LocalDateTime
        -patient : PatientEntity <<FK>>
    }
    class RoleEntity {
        -id_role : Long <<PK>>
        -name : String
        -users : Set~UserEntity~
    }
    %% RELACIONES
    UserEntity "1" -- "1" ProfileEntity : tiene >
    UserEntity "1" -- "many" PatientEntity : es_tutor_de >
    UserEntity "1" -- "many" AppointmentEntity : solicita >
    UserEntity "many" -- "many" RoleEntity : posee >
    PatientEntity "many" -- "1" UserEntity : pertenece_a >
    PatientEntity "1" -- "many" AppointmentEntity : tiene >
    PatientEntity "1" -- "many" TreatmentEntity : recibe >
    AppointmentEntity "many" -- "1" PatientEntity : es_para >
    AppointmentEntity "many" -- "1" UserEntity : atendida_por >
    TreatmentEntity "many" -- "1" PatientEntity : pertenece_a >
```

## 🔐 Autenticación y Seguridad

### Sistema BasicAuth
- Encriptación de emails y contraseñas
- Roles: `ADMIN` (Margarita) y `USER` (clientes)

### Endpoints Públicos
```
POST /api/v1/register  - Registro de clientes
GET  /api/v1/login     - Inicio de sesión
```

## 📡 API Endpoints

### Postman

<img width="216" height="393" alt="Captura de pantalla 2025-10-01 a las 18 15 58" src="https://github.com/user-attachments/assets/54e3f8ec-439e-45b7-8ea6-9eedd49cab9a" />
<img width="216" height="393" alt="Captura de pantalla 2025-10-01 a las 18 16 08" src="https://github.com/user-attachments/assets/5b020378-d436-4c63-92d6-9bd73389a46c" />


## ⚙️ Configuración

### application.properties

```properties
spring.application.name=clinica-veterinaria-back

api-endpoint=/api/v1

spring.profiles.active=devmysql

spring.docker.compose.enabled=true

# Configuración de Email - Gmail
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=programacionplaceholder@gmail.com
spring.mail.password=awpu xzix puhy fbzc
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true
```

## 🤖 Tareas Automáticas (Scheduler)

### 1. Actualización de Estado de Citas
```java
@Scheduled(cron = "0 0 0 * * *") // Se ejecuta a las 12:00 AM (medianoche) todos los días
@Transactional
public void markOverduePendingAppointmentsAsPassed()
```
- **Frecuencia**: Diariamente a las 00:00
- **Función**: Cambia citas con estado `PENDIENTE` a `PASADA` si la fecha/hora ya pasó y no fueron marcadas como `ATTENDED`

### 2. Limpieza de Citas Antiguas
```java
@Scheduled(cron = "0 30 0 1 * *") // Se ejecuta a las 12:30 AM el día 1 de cada mes
@Transactional
public void deleteOldPassedAppointments()
```
- **Frecuencia**: Mensual
- **Función**: Elimina automáticamente citas con estado `PASADA` que tienen más de 3 meses de antigüedad

## 📧 Sistema de Notificaciones

### Email de Confirmación de Cita
Se envía automáticamente al crear una nueva cita e incluye:
- Nombre del paciente (mascota)
- Fecha y hora de la cita
- Motivo de la consulta

## 🚦 Validaciones de Negocio

### Límite de Citas Diarias
- **Restricción**: Máximo 10 citas por día
- **Validación**: En tiempo real al crear nueva cita
- **Respuesta**: HTTP 400 Bad Request si se excede el límite

```java
public void validateDailyLimit(LocalDate date) {
    long count = appointmentRepository.countByDate(date);
    if (count >= 10) {
        throw new MaxAppointmentsExceededException(
            "El límite de " + DAILY_APPOINTMENT_LIMIT + " citas diarias ya está completo para el día " + requestedDatetime.toLocalDate()
        );
    }
}
```

## 🚀 Instalación y Ejecución

### Requisitos Previos
- Java JDK 17 o superior
- Maven 3.6+
- MySQL 8.0+ o PostgreSQL
- Git

### Pasos de Instalación

1. **Clonar el repositorio**
```bash
git clone https://github.com/tu-usuario/clinica-veterinaria-backend.git
cd clinica-veterinaria-backend
```

2. **Crear la base de datos**
```sql
CREATE DATABASE clinica_veterinaria;
```

3. **Configurar application.properties**
```bash
# Editar src/main/resources/application.properties
# Configurar credenciales de base de datos y email
```

4. **Compilar el proyecto**
```bash
mvn clean install
```

5. **Ejecutar la aplicación**
```bash
mvn spring-boot:run
```

6. **Verificar que funciona**
```
La API estará disponible en: http://localhost:8080
Documentación Swagger: http://localhost:8080/swagger-ui.html
```

## 🐳 Docker

Se utiliza Docker para la base de datos.

### Ejecutar con Docker
```bash

# Ejecutar con docker-compose
docker compose up

```

## 🧪 Testing

Cobertura de testing

<img width="359" height="686" alt="Captura de pantalla 2025-10-01 a las 17 57 04" src="https://github.com/user-attachments/assets/735164a8-925f-425e-b7c3-fd2c3958ecee" />
<img width="359" height="371" alt="Captura de pantalla 2025-10-01 a las 17 57 23" src="https://github.com/user-attachments/assets/498770ff-9415-4392-bb0c-dd9909be37b2" />



## 📝 Licencia

Este proyecto ha sido desarrollado como ejercicio educativo.

## 👥 Autoría

Desarrollado por Yolanda Alfonso, Lin Carbajales, Iván Lorenzo, Fernanda Marcos, Milca Ponce.


---

**¡Gracias por usar el sistema de gestión de Clínica Veterinaria Margarita! 🐾**
