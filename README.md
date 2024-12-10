# DBW-Project
1. Schritt: Aktivieren personal access tokens
2. Das Programm sollte als Projekt in Docker Compose per
** docker login --username {2023-wise-2} --password {gldt--j1yJ4RAudzZQAywSxyS} registry.git.hhu.de
** docker compose up --build --force-recreate
im Projektverzeichnis unter Unix gestartet werden können (im API-Template konfigurierbar).

Folglich muss auch die CLI-Anwendung von Docker Compose auf Ihrem System installiert sein. Dafür empfiehlt sich Docker Desktop.
Die Anwendung muss per HTTP auf Port 8080 lauschen (im API-Template enthalten), auf port 8090 für swagger UI requests.
