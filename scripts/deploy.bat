@echo off
REM SIRHA Backend - Deployment Script para Windows
REM Este script facilita el despliegue de la aplicación usando Docker

setlocal enabledelayedexpansion

REM Colores (limitados en batch)
set "GREEN=[INFO]"
set "YELLOW=[WARNING]"
set "RED=[ERROR]"

REM Función para verificar Docker
:check_docker
docker --version >nul 2>&1
if errorlevel 1 (
    echo %RED% Docker no está instalado. Por favor instala Docker Desktop.
    exit /b 1
)

docker-compose --version >nul 2>&1
if errorlevel 1 (
    echo %RED% Docker Compose no está instalado.
    exit /b 1
)
goto :eof

REM Función para verificar archivo .env
:check_env_file
if not exist ".env" (
    echo %YELLOW% Archivo .env no encontrado. Creando archivo de ejemplo...
    (
        echo # MongoDB Atlas Configuration
        echo SPRING_DATA_MONGODB_URI=mongodb+srv://username:password@cluster.mongodb.net/sirha_db?retryWrites=true^&w=majority
        echo.
        echo # Application Configuration
        echo SPRING_PROFILES_ACTIVE=prod
        echo SERVER_PORT=8080
        echo.
        echo # Logging Configuration
        echo LOGGING_LEVEL_ROOT=INFO
        echo LOGGING_LEVEL_EDU_DOSW_SIRHA=DEBUG
    ) > .env
    echo %YELLOW% Por favor edita el archivo .env con tu configuración de MongoDB Atlas.
    exit /b 1
)
goto :eof

REM Función para construir la imagen
:build_image
echo %GREEN% Construyendo imagen Docker...
docker-compose build --no-cache
if errorlevel 1 (
    echo %RED% Error al construir la imagen.
    exit /b 1
)
echo %GREEN% Imagen construida exitosamente.
goto :eof

REM Función para iniciar la aplicación
:start_application
echo %GREEN% Iniciando aplicación...
docker-compose up -d
if errorlevel 1 (
    echo %RED% Error al iniciar la aplicación.
    exit /b 1
)
echo %GREEN% Aplicación iniciada. Verificando estado...

REM Esperar a que la aplicación esté lista
timeout /t 10 /nobreak >nul

REM Verificar health check
curl -f http://localhost:8080/actuator/health >nul 2>&1
if errorlevel 1 (
    echo %YELLOW% La aplicación puede estar iniciando. Revisa los logs con: docker-compose logs -f
) else (
    echo %GREEN% ✅ Aplicación ejecutándose correctamente!
    echo %GREEN% 🌐 API disponible en: http://localhost:8080
    echo %GREEN% 📚 Swagger UI disponible en: http://localhost:8080/swagger-ui.html
)
goto :eof

REM Función para detener la aplicación
:stop_application
echo %GREEN% Deteniendo aplicación...
docker-compose down
echo %GREEN% Aplicación detenida.
goto :eof

REM Función para mostrar logs
:show_logs
echo %GREEN% Mostrando logs de la aplicación...
docker-compose logs -f sirha-backend
goto :eof

REM Función para mostrar estado
:show_status
echo %GREEN% Estado de los contenedores:
docker-compose ps

docker-compose ps -q sirha-backend >nul 2>&1
if not errorlevel 1 (
    echo %GREEN% Verificando health check...
    curl -f http://localhost:8080/actuator/health >nul 2>&1
    if errorlevel 1 (
        echo %YELLOW% ⚠️  Aplicación no responde al health check
    ) else (
        echo %GREEN% ✅ Aplicación saludable
    )
)
goto :eof

REM Función para limpiar
:cleanup
echo %GREEN% Limpiando contenedores e imágenes...
docker-compose down -v
docker system prune -f
echo %GREEN% Limpieza completada.
goto :eof

REM Función para mostrar ayuda
:show_help
echo SIRHA Backend - Deployment Script para Windows
echo.
echo Uso: %0 [COMANDO]
echo.
echo Comandos disponibles:
echo   build     - Construir la imagen Docker
echo   start     - Iniciar la aplicación
echo   stop      - Detener la aplicación
echo   restart   - Reiniciar la aplicación
echo   logs      - Mostrar logs de la aplicación
echo   status    - Mostrar estado de la aplicación
echo   cleanup   - Limpiar contenedores e imágenes
echo   help      - Mostrar esta ayuda
echo.
echo Ejemplos:
echo   %0 build
echo   %0 start
echo   %0 logs
goto :eof

REM Función principal
:main
if "%1"=="build" (
    call :check_docker
    if errorlevel 1 exit /b 1
    call :check_env_file
    if errorlevel 1 exit /b 1
    call :build_image
    if errorlevel 1 exit /b 1
) else if "%1"=="start" (
    call :check_docker
    if errorlevel 1 exit /b 1
    call :check_env_file
    if errorlevel 1 exit /b 1
    call :start_application
    if errorlevel 1 exit /b 1
) else if "%1"=="stop" (
    call :stop_application
) else if "%1"=="restart" (
    call :stop_application
    timeout /t 2 /nobreak >nul
    call :start_application
    if errorlevel 1 exit /b 1
) else if "%1"=="logs" (
    call :show_logs
) else if "%1"=="status" (
    call :show_status
) else if "%1"=="cleanup" (
    call :cleanup
) else (
    call :show_help
)
goto :eof

REM Ejecutar función principal
call :main %1
