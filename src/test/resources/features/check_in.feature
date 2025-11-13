Feature: Check-in de huéspedes
    Scenario: Realizar check-in exitoso
      Given la reserva "R-8791" del cliente "juan.perez" con fecha de inicio "2025-12-12"
      When el recepcionista "carlos.gomez" marque "Check-in realizado"
      Then el sistema debe cambiar el estado de la habitación a "ocupada"
      And registrar la fecha y hora del check-in en la base de datos