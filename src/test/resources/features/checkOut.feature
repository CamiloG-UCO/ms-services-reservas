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

