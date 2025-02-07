/**
 * Das Paket enthält die Controller-Klassen der Webanwendung, die für das Routing und die Verarbeitung von HTTP-Anfragen zuständig sind.
 * <p>
 * Die Controller-Klassen:
 * <ul>
 *     <li>Reagieren auf verschiedene HTTP-Methoden (z. B. GET, POST, PUT, DELETE) und URL-Muster.</li>
 *     <li>Stellen die Schnittstelle zwischen der Web-Schicht (Frontend) und der Domänen-Schicht der Anwendung dar.</li>
 *     <li>Empfangen Eingaben vom Benutzer und leiten diese an die entsprechenden Services weiter.</li>
 *     <li>Geben Ergebnisse in der passenden Form (z. B. als View oder JSON) zurück.</li>
 *     <li>Führen Validierungen, Fehlerbehandlungen und Umwandlungen von Daten (z. B. von DTOs) durch.</li>
 * </ul>
 * </p>
 *
 * @see org.springframework.web.bind.annotation.RestController
 * @see org.springframework.web.bind.annotation.RequestMapping
 * @see org.springframework.web.bind.annotation.GetMapping
 * @see org.springframework.web.bind.annotation.PostMapping
 */
package exambyte.web.controllers;
