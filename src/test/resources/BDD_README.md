# Pruebas BDD (Behavior Driven Development) - Historia de Usuario BE-1.1

## Descripción

Este proyecto implementa pruebas BDD para la historia de usuario BE-1.1: "Crear una nueva reserva para una habitación disponible" utilizando **Cucumber** y **Mockito** para evitar dependencias de base de datos.

## Estructura de Pruebas

### 1. Feature File (Gherkin)
**Ubicación:** `src/test/resources/features/reservation.feature`

Contiene los escenarios escritos en lenguaje natural (español) usando la sintaxis Gherkin:

- **Escenario principal:** Registrar reserva en habitación disponible
- **Escenarios de error:** 
  - Habitación no disponible
  - Fechas inválidas
  - Conflicto de fechas

### 2. Step Definitions
**Ubicación:** `src/test/java/co/edu/hotel/reservaservice/bdd/ReservationStepDefinitions.java`

Implementa los pasos definidos en el feature file usando anotaciones en español:
- `@Dado` (Given)
- `@Cuando` (When) 
- `@Entonces` (Then)
- `@Y` (And)

### 3. Unit Tests con Mockito
**Ubicación:** `src/test/java/co/edu/hotel/reservaservice/bdd/ReservationBddTest.java`

Pruebas unitarias que implementan los mismos escenarios BDD usando:
- **@Mock** para simular repositorios y servicios
- **@InjectMocks** para inyectar mocks en el servicio bajo prueba
- **Mockito** para configurar comportamientos esperados

## Tecnologías Utilizadas

### Dependencias BDD
```xml
<!-- Cucumber BDD Testing -->
<dependency>
    <groupId>io.cucumber</groupId>
    <artifactId>cucumber-java</artifactId>
    <version>7.18.0</version>
    <scope>test</scope>
</dependency>
<dependency>
    <groupId>io.cucumber</groupId>
    <artifactId>cucumber-spring</artifactId>
    <version>7.18.0</version>
    <scope>test</scope>
</dependency>

<!-- Mockito for mocking -->
<dependency>
    <groupId>org.mockito</groupId>
    <artifactId>mockito-core</artifactId>
    <scope>test</scope>
</dependency>
```

## Escenarios de Prueba

### Escenario Exitoso
**Given:** Habitación "Premium vista al mar" disponible en "Santa Marta Resort"  
**When:** Usuario "juan.perez" reserva del 2025-12-12 al 2025-12-31  
**Then:** Sistema confirma reserva en menos de 5 segundos y envía email  

**Validaciones:**
- Tiempo de respuesta < 5 segundos
- Código de reserva generado (formato R-XXXX)
- Cálculo correcto del precio total
- Envío de email de confirmación
- Estado "confirmada"

### Escenarios de Error

1. **Habitación no disponible**
   - Error: "Habitación no disponible o no encontrada"

2. **Fechas inválidas**
   - Error: "La fecha de inicio no puede ser anterior a hoy"

3. **Conflicto de fechas**
   - Error: "La habitación no está disponible para las fechas seleccionadas"

## Mocking Strategy

### Repositorios Mockeados
- `ReservationRepository`: Simula operaciones de base de datos de reservas
- `RoomRepository`: Simula búsqueda de habitaciones
- `UserRepository`: Simula búsqueda de usuarios
- `EmailService`: Simula envío de correos

### Configuración de Mocks
```java
// Habitación disponible
when(roomRepository.findByIdAndStatus("room-1", "disponible"))
    .thenReturn(Optional.of(testRoom));

// Usuario existente
when(userRepository.findByUsername("juan.perez"))
    .thenReturn(Optional.of(testUser));

// Sin conflictos de reserva
when(reservationRepository.findConflictingReservations(anyString(), any(LocalDate.class), any(LocalDate.class)))
    .thenReturn(Collections.emptyList());
```

## Ejecución de Pruebas

### Ejecutar pruebas BDD específicas
```bash
# Ejecutar solo las pruebas BDD
./mvnw test -Dtest=ReservationBddTest
```

### Resultados Esperados
```
Tests run: 3, Failures: 0, Errors: 0, Skipped: 0
BUILD SUCCESS
```

## Beneficios de esta Implementación

### 1. **Sin Dependencias de BD**
- Uso de Mockito elimina necesidad de base de datos real
- Pruebas más rápidas y confiables
- Aislamiento completo de componentes

### 2. **BDD Completo**
- Escenarios en lenguaje natural (Gherkin)
- Trazabilidad directa con requisitos de negocio
- Fácil comprensión para stakeholders no técnicos

### 3. **Cobertura Integral**
- Casos exitosos y de error
- Validación de rendimiento (< 5 segundos)
- Verificación de efectos secundarios (email)

### 4. **Mantenibilidad**
- Separación clara entre definición y implementación
- Reutilización de step definitions
- Fácil extensión para nuevos escenarios

## Próximos Pasos

1. **Integración con CI/CD**: Configurar ejecución automática en pipeline
2. **Reportes**: Generar reportes HTML de Cucumber
3. **Más Escenarios**: Agregar casos edge adicionales
4. **Performance**: Agregar métricas de rendimiento detalladas
