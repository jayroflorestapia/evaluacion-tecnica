# Evaluacion Java Senior 2026

Solucion de referencia para la evaluacion, organizada como un proyecto multi-modulo:

| Modulo | Puerto | Responsabilidad |
| --- | ---: | --- |
| `api-entrada` | 8081 | Login, validacion, descifrado AES-256-GCM y envio a la API de transacciones. |
| `api-transacciones` | 8082 | Persistencia H2, referencias, cancelacion y consulta paginada. |
| `frontend` | 5173 | React: login, cifrado del secreto y registro de operaciones. |

Las APIs siguen una estructura MVC: `controller` recibe HTTP, `service` concentra reglas de negocio, `repository` usa JPA, `entity` modela las tablas y `dto` representa los contratos de entrada/salida. La API de entrada tambien incluye `client` para OpenFeign y ambas APIs separan `exception` para errores HTTP.

## Requisitos locales

- JDK 17 (la version instalada es valida).
- Maven 3.9 o superior, disponible como `mvn`.
- Node.js LTS con npm (Node 20 o 22 recomendado; evita Node 24 para este ejercicio si Vite reporta incompatibilidad).

## Arranque

Abre esta carpeta en VS Code y abre tres terminales:

```powershell
mvn -pl api-transacciones spring-boot:run
```

```powershell
mvn -pl api-entrada spring-boot:run
```

```powershell
Set-Location frontend
npm install
npm run dev
```

Visita `http://localhost:5173`. El usuario de demostracion es `angel` y la contrasena es `Password123!`.

## Endpoints para Postman

- `POST http://localhost:8081/api/auth/login`
- `POST http://localhost:8081/api/operaciones`
- `GET http://localhost:8082/api/transacciones?page=0&size=10&sortBy=id&direction=DESC`
- `PATCH http://localhost:8082/api/transacciones/cancelar`

La llave AES incluida es solo para desarrollo. En un entorno real debe recibirse por una variable de entorno o un gestor de secretos, nunca quedar en el repositorio.

## Modelo de datos

El diagrama ER, diccionario de campos y reglas del motor H2 se encuentran en [docs/modelo-er.md](docs/modelo-er.md). Los scripts `schema.sql` de cada API crean las tablas y Hibernate valida que las entidades Java coincidan con dicho esquema al arrancar.
