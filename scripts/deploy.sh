#!/bin/bash

# SIRHA Backend - Deployment Script
# Este script facilita el despliegue de la aplicación usando Docker

set -e

# Colores para output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Función para imprimir mensajes
print_message() {
    echo -e "${GREEN}[INFO]${NC} $1"
}

print_warning() {
    echo -e "${YELLOW}[WARNING]${NC} $1"
}

print_error() {
    echo -e "${RED}[ERROR]${NC} $1"
}

# Verificar que Docker esté instalado
check_docker() {
    if ! command -v docker &> /dev/null; then
        print_error "Docker no está instalado. Por favor instala Docker Desktop."
        exit 1
    fi
    
    if ! command -v docker-compose &> /dev/null; then
        print_error "Docker Compose no está instalado."
        exit 1
    fi
}

# Verificar archivo .env
check_env_file() {
    if [ ! -f ".env" ]; then
        print_warning "Archivo .env no encontrado. Creando archivo de ejemplo..."
        cat > .env << EOF
# MongoDB Atlas Configuration
SPRING_DATA_MONGODB_URI=mongodb+srv://username:password@cluster.mongodb.net/sirha_db?retryWrites=true&w=majority

# Application Configuration
SPRING_PROFILES_ACTIVE=prod
SERVER_PORT=8080

# Logging Configuration
LOGGING_LEVEL_ROOT=INFO
LOGGING_LEVEL_EDU_DOSW_SIRHA=DEBUG
EOF
        print_warning "Por favor edita el archivo .env con tu configuración de MongoDB Atlas."
        exit 1
    fi
}

# Función para construir la imagen
build_image() {
    print_message "Construyendo imagen Docker..."
    docker-compose build --no-cache
    print_message "Imagen construida exitosamente."
}

# Función para ejecutar la aplicación
start_application() {
    print_message "Iniciando aplicación..."
    docker-compose up -d
    print_message "Aplicación iniciada. Verificando estado..."
    
    # Esperar a que la aplicación esté lista
    sleep 10
    
    # Verificar health check
    if curl -f http://localhost:8080/actuator/health &> /dev/null; then
        print_message "✅ Aplicación ejecutándose correctamente!"
        print_message "🌐 API disponible en: http://localhost:8080"
        print_message "📚 Swagger UI disponible en: http://localhost:8080/swagger-ui.html"
    else
        print_warning "La aplicación puede estar iniciando. Revisa los logs con: docker-compose logs -f"
    fi
}

# Función para detener la aplicación
stop_application() {
    print_message "Deteniendo aplicación..."
    docker-compose down
    print_message "Aplicación detenida."
}

# Función para mostrar logs
show_logs() {
    print_message "Mostrando logs de la aplicación..."
    docker-compose logs -f sirha-backend
}

# Función para mostrar estado
show_status() {
    print_message "Estado de los contenedores:"
    docker-compose ps
    
    if [ "$(docker-compose ps -q sirha-backend)" ]; then
        print_message "Verificando health check..."
        if curl -f http://localhost:8080/actuator/health &> /dev/null; then
            print_message "✅ Aplicación saludable"
        else
            print_warning "⚠️  Aplicación no responde al health check"
        fi
    fi
}

# Función para limpiar
cleanup() {
    print_message "Limpiando contenedores e imágenes..."
    docker-compose down -v
    docker system prune -f
    print_message "Limpieza completada."
}

# Función para mostrar ayuda
show_help() {
    echo "SIRHA Backend - Deployment Script"
    echo ""
    echo "Uso: $0 [COMANDO]"
    echo ""
    echo "Comandos disponibles:"
    echo "  build     - Construir la imagen Docker"
    echo "  start     - Iniciar la aplicación"
    echo "  stop      - Detener la aplicación"
    echo "  restart   - Reiniciar la aplicación"
    echo "  logs      - Mostrar logs de la aplicación"
    echo "  status    - Mostrar estado de la aplicación"
    echo "  cleanup   - Limpiar contenedores e imágenes"
    echo "  help      - Mostrar esta ayuda"
    echo ""
    echo "Ejemplos:"
    echo "  $0 build"
    echo "  $0 start"
    echo "  $0 logs"
}

# Función principal
main() {
    case "${1:-help}" in
        "build")
            check_docker
            check_env_file
            build_image
            ;;
        "start")
            check_docker
            check_env_file
            start_application
            ;;
        "stop")
            stop_application
            ;;
        "restart")
            stop_application
            sleep 2
            start_application
            ;;
        "logs")
            show_logs
            ;;
        "status")
            show_status
            ;;
        "cleanup")
            cleanup
            ;;
        "help"|*)
            show_help
            ;;
    esac
}

# Ejecutar función principal
main "$@"
