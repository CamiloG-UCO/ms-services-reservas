Feature: Cancelar una reserva confirmada
  Como cliente del hotel
  Quiero cancelar una reserva confirmada
  Para liberar la habitación y evitar cargos innecesarios

  # --- Escenario principal (caso exitoso)
  Scenario: Cancelar una reserva confirmada existente
    Given existe una reserva con código "R-8791" para el usuario "juan.perez" con estado "confirmada"
    And el correo del usuario es "juan.perez@gmail.com"
    When el usuario presione el botón "Cancelar reserva"
    Then el sistema debe eliminar la reserva con código "R-8791"
    And mostrar el mensaje "Reserva R-8791 cancelada exitosamente"
    And enviar correo de cancelación a "juan.perez@gmail.com"

  # --- Escenario alternativo: reserva inexistente
  Scenario: Cancelar una reserva que no existe
    Given no existe una reserva con código "R-9999" para el usuario "juan.perez"
    And el correo del usuario es "juan.perez@gmail.com"
    When el usuario presione el botón "Cancelar reserva"
    Then el sistema debe mostrar el mensaje de error "No existe una reserva con el código R-9999"
    And no debe enviarse ningún correo


  # --- Escenario de error técnico
  Scenario: Fallo durante la cancelación (error interno)
    Given existe una reserva con código "R-5555" para el usuario "maria.gomez" con estado "confirmada"
    And el correo del usuario es "maria.gomez@gmail.com"
    And el servicio de base de datos presenta un error temporal
    When el usuario presione el botón "Cancelar reserva"
    Then el sistema debe mostrar el mensaje "No fue posible cancelar la reserva en este momento. Intente más tarde."
    And no debe eliminar la reserva

# --- Escenario de validación de correo antes de cancelar
  Scenario: No se puede cancelar la reserva hasta validar el correo del usuario
    Given existe una reserva con código "R-8791" para el usuario "juan.perez" con estado "confirmada"
    And el correo del usuario no encontrado
    When el usuario presione el botón "Cancelar reserva"
    Then el sistema debe mostrar el mensaje "No se puede cancelar la reserva hasta que valide la información de su correo electrónico"
    And no debe eliminar la reserva
    And no debe enviarse ningún correo de cancelación
