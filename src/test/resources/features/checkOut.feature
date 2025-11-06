Feature: Generar factura post check-out
  Como cliente del hotel
  Quiero poder hacer check-out cuando mi reserva finalice
  Para liberar la habitación y conocer cuanto pagué por mi estadia

# -- Caso de exito
  Scenario: Generar factura post check-out
    Given el usuario "juan.perez" existe en el sistema
    And la reserva "R-8791" con estado "confirmada" del cliente "juan.perez" con costo total "2500000.0"
    When el recepcionista marque "Check-out realizado" el estado pasa a "completada"
    Then el sistema debe generar la factura con código "F-1203" con costo total "2500000.0"
    And enviar una copia PDF al correo "juan.perez@gmail.com"

  Scenario: Intentar generar factura para una reserva ya completada
    Given la reserva "R-8791" con estado "completada" del cliente "juan.perez" con costo total "2500000.0"
    When el recepcionista intente marcar "Check-out realizado"
    Then el sistema debe mostrar "La reserva ya fue completada"
    And no debe generar una nueva factura

  Scenario: Intentar generar factura para una reserva inexistente
    Given no existe una reserva con código "R-9999"
    When el recepcionista intente marcar "Check-out realizado"
    Then el sistema debe mostrar "Reserva no encontrada"
    And no debe generarse ninguna factura