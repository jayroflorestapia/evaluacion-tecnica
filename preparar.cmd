@echo off
setlocal
cd /d "%~dp0"
where java.exe >nul 2>&1
if errorlevel 1 (
  echo ERROR: Instala un JDK 17 y vuelve a abrir VS Code.
  exit /b 1
)
where npm.cmd >nul 2>&1
if errorlevel 1 (
  echo ERROR: Instala Node.js con npm y vuelve a abrir VS Code.
  exit /b 1
)
echo Preparando y compilando las APIs con Maven...
call "%~dp0mvnw.cmd" -B compile
if errorlevel 1 exit /b 1
if not exist "%~dp0frontend\node_modules\.package-lock.json" (
  echo Instalando dependencias del frontend desde package-lock.json...
  call npm.cmd --prefix "%~dp0frontend" ci
  if errorlevel 1 exit /b 1
)
echo Dependencias listas. Puedes iniciar las APIs y el frontend.
