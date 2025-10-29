# SIRHA Backend - Despliegue en Kubernetes

Este documento describe cómo desplegar la aplicación SIRHA Backend en un cluster de Kubernetes.

## Prerrequisitos

### Herramientas Necesarias
- **kubectl**: Cliente de línea de comandos para Kubernetes
- **Docker**: Para construir las imágenes
- **Cluster de Kubernetes**: Kubernetes activos de Docker desktop

## Despliegue en Kubernetes

### Despliegue Manual

#### Opción 1: Aplicar todos los archivos individualmente
```bash
# 1. Crear namespace y cambiar contexto
kubectl apply -f k8s/namespace.yaml
kubectl config set-context --current --namespace=sirha


# 2. Aplicar ConfigMap
kubectl apply -f k8s/configmap.yaml

# 3. Aplicar Secret
kubectl apply -f k8s/secret.yaml

# 4. Desplegar aplicación
kubectl apply -f k8s/deployment.yaml

# 5. Crear servicio
kubectl apply -f k8s/service.yaml

# 6. Configurar Ingress
kubectl apply -f k8s/ingress.yaml
```

#### Opción 2: Aplicar todos los archivos de una vez
```bash
# Aplicar todos los manifiestos
kubectl apply -f k8s/
```

## Arquitectura del Despliegue

### Componentes Kubernetes

#### 1. **Namespace** (`k8s/namespace.yaml`)
- Aislamiento de recursos
- Namespace: `sirha`

#### 2. **ConfigMap** (`k8s/configmap.yaml`)
- Variables de entorno no sensibles
- Configuración de la aplicación
- Logging y Swagger

#### 3. **Secret** (`k8s/secret.yaml`)
- Variables sensibles (MongoDB URI)
- Datos encriptados en base64

#### 4. **Deployment** (`k8s/deployment.yaml`)
- 2 réplicas por defecto
- Health checks configurados
- Recursos limitados
- Variables de entorno desde ConfigMap y Secret

#### 5. **Service** (`k8s/service.yaml`)
- Exposición interna del puerto 8080
- Tipo: ClusterIP

#### 6. **Ingress** (`k8s/ingress.yaml`)
- Exposición externa
- Configuración de CORS
- Timeouts y límites

## 🔧 Configuración

### Variables de Entorno

#### ConfigMap (No Sensibles)
```yaml
SPRING_PROFILES_ACTIVE: "prod"
SERVER_PORT: "8080"
LOGGING_LEVEL_ROOT: "INFO"
```

#### Secret (Sensibles)
```yaml
SPRING_DATA_MONGODB_URI: <base64-encoded>
SPRING_DATA_MONGODB_DATABASE: <base64-encoded>
```

### Health Checks
- **Startup Probe**: `/actuator/health` (30 intentos, 10s intervalo)
- **Readiness Probe**: `/actuator/health` (3 intentos, 5s intervalo)
- **Liveness Probe**: `/actuator/health` (3 intentos, 10s intervalo)

### Recursos
```yaml
requests:
  cpu: "100m"
  memory: "256Mi"
limits:
  cpu: "500m"
  memory: "512Mi"
```

## Acceso a la Aplicación

### URLs Disponibles
- **API Base**: `http://localhost:8080`
- **Swagger UI**: `http://localhost:8080/swagger-ui.html`
- **Health Check**: `http://localhost:8080/actuator/health`
- **API Docs**: `http://localhost:8080/v3/api-docs`

### Port Forward
```bash
# Acceso local
kubectl port-forward service/sirha-backend-service 8080:80 -n sirha
```

## Monitoreo y Logs

### Ver Estado del Despliegue
```bash
# Pods
kubectl get pods -n sirha -l app=sirha-backend

# Services
kubectl get services -n sirha

# Ingress
kubectl get ingress -n sirha

# Deployment
kubectl get deployment sirha-backend -n sirha
```

### Ver Logs
```bash
# Logs en tiempo real
kubectl logs -f deployment/sirha-backend -n sirha

# Logs de un pod específico
kubectl logs <pod-name> -n sirha
```

## Actualizaciones

### Actualizar Imagen
```bash
# Cambiar imagen
kubectl set image deployment/sirha-backend sirha-backend=ghcr.io/tu-usuario/sirha-backend:latest -n sirha

# Verificar rollout
kubectl rollout status deployment/sirha-backend -n sirha

# Rollback si es necesario
kubectl rollout undo deployment/sirha-backend -n sirha
```

## Limpieza

### Eliminar Recursos
```bash
# Eliminar todo
kubectl delete -f k8s/
```

## Seguridad

### Mejores Prácticas
-  Usar Secrets para datos sensibles
-  ConfigMap para configuración no sensible
-  Namespace para aislamiento
-  Resource limits configurados
-  Health checks implementados
-  Usuario no-root en contenedor

### Rotar Secrets
```bash
# Actualizar secret
kubectl create secret generic sirha-backend-secrets \
  --from-literal=SPRING_DATA_MONGODB_URI='nueva-uri' \
  -n sirha --dry-run=client -o yaml | kubectl apply -f -

# Reiniciar deployment
kubectl rollout restart deployment/sirha-backend -n sirha
```

# CI/CD con GitHub Actions

1. **Construye** la imagen Docker
2. **Sube** la imagen al GitHub Container Registry
3. **Despliega** en Kubernetes
4. **Escanea** vulnerabilidades de seguridad

### Configuración Requerida
- `KUBE_CONFIG`: Configuración del cluster (base64)
- `GITHUB_TOKEN`: Token para el registry

### Workflow
- **Push a `develop`**: Despliega en staging
- **Push a `main`**: Despliega en production
- **Pull Request**: Solo construye y escanea