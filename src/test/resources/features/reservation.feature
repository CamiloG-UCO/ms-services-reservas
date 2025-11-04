# language: es
Característica: Crear una nueva reserva para una habitación disponible
  Como usuario del sistema de reservas de hotel
  Quiero poder reservar una habitación disponible
  Para asegurar mi estadía en el hotel

  Esquema del escenario: Registrar reserva en habitación disponible
    Dado que la habitación "<nombre_habitacion>" del hotel "<nombre_hotel>" con estado "<estado>" y capacidad "<capacidad>"
    Y que el usuario "<usuario>" existe en el sistema
    Cuando el usuario "<usuario>" ingrese fecha de inicio "<fecha_inicio>" y fecha final "<fecha_final>" y presione "Reservar"
    Entonces antes de 5 segundos el sistema debe mostrar "<mensaje_exito>"
    Y enviar un correo de confirmación a "<email>"

    Ejemplos:
      | nombre_habitacion     | nombre_hotel      | estado     | capacidad | usuario    | fecha_inicio | fecha_final | mensaje_exito                                      | email                |
      | Premium vista al mar  | Santa Marta Resort| disponible | 2         | juan.perez | 2025-12-12   | 2025-12-31  | Habitación reservada con éxito, código de reserva | juan.perez@gmail.com |

  Escenario: Intentar reservar habitación no disponible
    Dado que la habitación "Premium vista al mar" del hotel "Santa Marta Resort" con estado "ocupada" y capacidad "2"
    Y que el usuario "juan.perez" existe en el sistema
    Cuando el usuario "juan.perez" ingrese fecha de inicio "2025-12-12" y fecha final "2025-12-31" y presione "Reservar"
    Entonces el sistema debe mostrar el error "Habitación no disponible o no encontrada"

  Escenario: Intentar reservar con fechas inválidas
    Dado que la habitación "Premium vista al mar" del hotel "Santa Marta Resort" con estado "disponible" y capacidad "2"
    Y que el usuario "juan.perez" existe en el sistema
    Cuando el usuario "juan.perez" ingrese fecha de inicio "2023-12-12" y fecha final "2025-12-31" y presione "Reservar"
    Entonces el sistema debe mostrar el error "La fecha de inicio no puede ser anterior a hoy"

  Escenario: Intentar reservar habitación con conflicto de fechas
    Dado que la habitación "Premium vista al mar" del hotel "Santa Marta Resort" con estado "disponible" y capacidad "2"
    Y que el usuario "juan.perez" existe en el sistema
    Y que existe una reserva conflictiva para las fechas "2025-12-15" a "2025-12-20"
    Cuando el usuario "juan.perez" ingrese fecha de inicio "2025-12-12" y fecha final "2025-12-31" y presione "Reservar"
    Entonces el sistema debe mostrar el error "La habitación no está disponible para las fechas seleccionadas"
