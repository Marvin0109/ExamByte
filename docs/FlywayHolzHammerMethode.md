# Flyway macht Probleme?

## Sollte nicht in Produktion verwendet werden, aber in der Entwicklung etwas vertretbar
1. Stoppen Sie den Container, falls dieser noch läuft
2. Löschen Sie den Ordner, den Sie als Volume eingebunden haben (z.B: data) 
3. Räumen sie gestoppte Container ab (`docker container prune`)

*"Ist natürlich die Holzhammermethode, die in Produktion so nicht benutzt werden sollte, aber in der Entwicklung ist sowas vertretbar."*
*"Und für die Zukunft: Keine Flywayskripte ändern!"*
~ Jens Bendisposto, März 2025