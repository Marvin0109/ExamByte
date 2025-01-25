#!/bin/bash

# Sicherstellen, dass die TO_DO.md-Datei vorhanden ist
if [ ! -f TO_DO.md ]; then
  echo "TO_DO.md file not found!"
  exit 1
fi

# Liest die TO_DO.md und zählt die erledigten Aufgaben
total_tasks=$(grep -o '\[ \]' TO_DO.md | wc -l)
completed_tasks=$(grep -o '\[x\]' TO_DO.md | wc -l)

# Verhindern einer Division durch null (falls keine Aufgaben vorhanden sind)
if [ "$total_tasks" -eq 0 ]; then
  progress=0
else
  # Berechnet den Fortschritt
  progress=$(( 100 * completed_tasks / total_tasks ))
fi

# Aktualisiert die TO_DO.md-Datei mit dem neuen Fortschritt
sed -i "s/Fertiggestellt: [0-9]*%/Fertiggestellt: $progress%/" TO_DO.md

# Ausgabetest zur Verifikation (optional)
echo "Updated progress: $progress%"