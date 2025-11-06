Narrative:
Como usuario del sistema de reservas
Quiero poder cancelar una reserva existente
Para gestionar mis reservas de manera flexible

Scenario: Cancelar reserva existente
Given la reserva "R-8791" del usuario "juan.perez" con estado "confirmada"
When el usuario presione el botón "Cancelar reserva"
Then el sistema debe actualizar el estado a "cancelada"
And mostrar el mensaje "Reserva R-8791 cancelada exitosamente"
And enviar correo de cancelación a "juan.perez@gmail.com"