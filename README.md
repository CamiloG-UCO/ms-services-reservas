# Microservicio de Reservas de Hotel

Este microservicio maneja las reservas de habitaciones de hotel, implementando la historia de usuario BE-1.1 para crear nuevas reservas.

## Funcionalidades Implementadas

### Historia de Usuario BE-1.1: Crear una nueva reserva para una habitación disponible

**Scenario:** Registrar reserva en habitación disponible
- **Given:** la habitación "Premium vista al mar" del hotel "Santa Marta Resort" con estado "disponible" y capacidad "2"
- **When:** el usuario "juan.perez" ingrese fecha de inicio "2025-12-12" y fecha final "2025-12-31" y presione "Reservar"
- **Then:** antes de 5 segundos el sistema debe mostrar "Habitación reservada con éxito, código de reserva R-XXXX"
- **And:** enviar un correo de confirmación a "juan.perez@gmail.com"

## Arquitectura

### Entidades Principales
- **Hotel**: Información del hotel
- **Room**: Habitaciones disponibles
- **User**: Usuarios del sistema
- **Reservation**: Reservas realizadas

### Servicios
- **ReservationService**: Lógica de negocio para reservas
- **EmailService**: Envío de notificaciones por correo

## API Endpoints

### Reservas

#### Crear una nueva reserva
```
POST /api/v1/reservations
Authorization: Bearer <JWT_TOKEN>
Content-Type: application/json

{
  "roomId": "room_id",
  "startDate": "2025-12-12",
  "endDate": "2025-12-31"
}
```

**Respuesta exitosa (201):**
```json
{
  "id": "reservation_id",
  "reservationCode": "R-8791",
  "username": "juan.perez",
  "userEmail": "juan.perez@gmail.com",
  "roomName": "Premium vista al mar",
  "hotelName": "Santa Marta Resort",
  "startDate": "2025-12-12",
  "endDate": "2025-12-31",
  "status": "confirmada",
  "totalAmount": 4750000.0,
  "createdAt": "2025-01-01T10:00:00",
  "message": "Habitación reservada con éxito, código de reserva R-8791"
}
```

#### Obtener reservas del usuario
```
GET /api/v1/reservations
Authorization: Bearer <JWT_TOKEN>
```

#### Obtener reserva por código
```
GET /api/v1/reservations/{reservationCode}
Authorization: Bearer <JWT_TOKEN>
```

### Habitaciones

#### Obtener habitaciones disponibles
```
GET /api/v1/rooms/available
Authorization: Bearer <JWT_TOKEN>
```

#### Obtener habitación por ID
```
GET /api/v1/rooms/{roomId}
Authorization: Bearer <JWT_TOKEN>
```

## Configuración

### Variables de Entorno

```properties
# Base de datos MongoDB
MONGODB_URI=mongodb+srv://usuario:contraseña@cluster.mongodb.net/hotel

# Configuración de correo
EMAIL_USERNAME=your-email@gmail.com
EMAIL_PASSWORD=your-app-password

# JWT
JWT_SECRET=your-secret-key
```

### Configuración de Correo

Para Gmail, necesitas:
1. Habilitar autenticación de 2 factores
2. Generar una contraseña de aplicación
3. Usar esa contraseña en `EMAIL_PASSWORD`

## Validaciones Implementadas

1. **Fechas de reserva:**
   - La fecha de inicio no puede ser anterior a hoy
   - La fecha de fin debe ser posterior a la fecha de inicio
   - No puede haber conflictos con otras reservas

2. **Disponibilidad de habitación:**
   - La habitación debe existir
   - La habitación debe estar en estado "disponible"
   - No debe tener reservas conflictivas en las fechas solicitadas

3. **Usuario autenticado:**
   - Debe tener un token JWT válido
   - El usuario debe existir en el sistema

## Funcionalidades de Negocio

### Generación de Código de Reserva
- Formato: R-XXXX (donde XXXX es un número aleatorio de 4 dígitos)
- Único por reserva

### Cálculo de Precio
- Se calcula automáticamente basado en:
  - Número de noches (fecha fin - fecha inicio)
  - Precio por noche de la habitación

### Notificación por Correo
- Se envía automáticamente al confirmar la reserva
- Incluye todos los detalles de la reserva
- Manejo de errores sin afectar la creación de la reserva

## Datos de Prueba

El sistema inicializa automáticamente con:

**Hotel:**
- Nombre: "Santa Marta Resort"
- Ciudad: "Santa Marta"

**Habitación:**
- Nombre: "Premium vista al mar"
- Capacidad: 2 personas
- Estado: "disponible"
- Precio: $250,000 por noche

**Usuario:**
- Username: "juan.perez"
- Email: "juan.perez@gmail.com"

## Ejecución

```bash
# Compilar
mvn clean compile

# Ejecutar tests
mvn test

# Ejecutar aplicación
mvn spring-boot:run
```

La aplicación en `http://localhost:8080`
