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
- Maven 3.9.9 mediante el wrapper incluido: `./mvnw.cmd` en Windows. No necesitas instalar `mvn` ni agregarlo al PATH; el wrapper descarga Maven la primera vez.
- Node.js con npm: 20.19+ de la rama 20, o 22.12+ (tambien funciona la version 24 instalada).
- Conexion a Internet para la primera descarga de Maven y dependencias.

## Arranque

### Desde VS Code (Windows)

1. Abre la carpeta completa `evaluacion tecnica` con **Archivo > Abrir carpeta**.
2. Presiona **Ctrl+Shift+B** y ejecuta **Proyecto: iniciar todo**. Tambien esta en **Terminal > Ejecutar tarea**.
3. La tarea prepara las dependencias y abre las dos APIs y el frontend en terminales de VS Code. Espera a que aparezca la URL del frontend.
4. Abre `http://localhost:5173`.

Para detener los servicios usa **Terminal > Finalizar tarea** para cada servicio, o Ctrl+C en cada terminal. Cuando vuelvas a abrir VS Code, repite Ctrl+Shift+B. No inicies una segunda copia si los puertos 8081, 8082 o 5173 siguen ocupados.

### Desde la terminal de PowerShell

Desde la raiz del proyecto, prepara las dependencias una vez (y despues de cambios en Java):

```powershell
.\preparar.cmd
```

Abre tres terminales en la raiz del proyecto:

```powershell
.\mvnw.cmd -pl api-transacciones spring-boot:run
```

```powershell
.\mvnw.cmd -pl api-entrada spring-boot:run
```

```powershell
npm.cmd --prefix frontend run dev
```

Visita `http://localhost:5173`. El usuario de demostracion es `angel` y la contrasena es `Password123!`.

### Si un comando falla

- **`mvn` no se reconoce:** usa `.\mvnw.cmd` desde la raiz. Las dependencias se guardan en `.tools/m2-repository`.
- **`npm.ps1` no se puede ejecutar:** usa `npm.cmd`; no es necesario cambiar la politica de ejecucion de PowerShell.
- **No encuentra `package.json`:** ejecuta `npm.cmd --prefix frontend run dev` desde la raiz, o entra a `frontend` antes de ejecutar `npm.cmd run dev`.
- **`vite` no se reconoce o cambiaste `package-lock.json`:** detiene el frontend y ejecuta `npm.cmd --prefix frontend ci`; despues vuelve a iniciarlo.
- **Puerto ocupado:** detiene la instancia anterior antes de iniciar otra. El frontend usa siempre el puerto 5173 para coincidir con CORS de las APIs.
- **Java no se reconoce:** instala un JDK 17 y reinicia VS Code para que detecte el PATH actualizado. Si defines `JAVA_HOME`, debe apuntar a la carpeta del JDK, sin `bin`.

Las tareas usan `cmd.exe` y `npm.cmd` explicitamente; tu terminal personal puede seguir usando PowerShell. Las versiones del frontend estan fijadas en `package.json` y `package-lock.json`.

## Endpoints para Postman

- `POST http://localhost:8081/api/auth/login`
- `POST http://localhost:8081/api/operaciones`
- `GET http://localhost:8082/api/transacciones?page=0&size=10&sortBy=id&direction=DESC`
- `PATCH http://localhost:8082/api/transacciones/cancelar`

La llave AES incluida es solo para desarrollo. En un entorno real debe recibirse por una variable de entorno o un gestor de secretos, nunca quedar en el repositorio.

## Modelo de datos

El diagrama ER, diccionario de campos y reglas del motor H2 se encuentran en [docs/modelo-er.md](docs/modelo-er.md). Los scripts `schema.sql` de cada API crean las tablas y Hibernate valida que las entidades Java coincidan con dicho esquema al arrancar.
