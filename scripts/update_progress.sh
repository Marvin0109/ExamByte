#!/bin/bash

# TO_DO.md-Datei und README.md-Datei einlesen
todo_file="TO_DO.md"
readme_file="README.md"

# Anzahl der Aufgaben insgesamt (Zeilen mit " - [ ]" oder " - [x]" zählen)
total_tasks=$(grep -c " - \[" "$todo_file")

# Anzahl der erledigten Aufgaben (Zeilen mit " - [x]" zählen)
completed_tasks=$(grep -c " - \[x\]" "$todo_file")

# Prozentualer Fortschritt
if [ "$total_tasks" -gt 0 ]; then
  progress=$(( 100 * completed_tasks / total_tasks ))
else
  progress=0
fi

# Fortschritt in der README.md aktualisieren
# Escape das Prozentzeichen für die URL im Sed-Befehl
sed -i "s|![Fortschritt](https://img.shields.io/badge/Fortschritt-[0-9]*%25-brightgreen)|![Fortschritt](https://img.shields.io/badge/Fortschritt-${progress}\%25-brightgreen)|" "$readme_file"

# Fortschritt in der TO_DO.md aktualisieren (Fortschrittsbild ersetzen)
sed -i "s|**Status:** ![Fortschritt](https://img.shields.io/badge/Fortschritt-[0-9]*%25-brightgreen)|**Status:** ![Fortschritt](https://img.shields.io/badge/Fortschritt-${progress}\%25-brightgreen)|" "$todo_file"
