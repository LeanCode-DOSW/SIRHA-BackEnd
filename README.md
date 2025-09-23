# SIRHA - Sistema de Reasignación de Horarios Académicos

Este proyecto tiene como objetivo desarrollar un sistema de software denominado **SIRHA** para la Escuela Colombiana de Ingeniería, siguiendo buenas prácticas de ingeniería de software y metodologías ágiles.
---

## Tecnologías Utilizadas

- ☕ Java 17+
- 🌱 Spring Boot
- 📦 Maven (gestión de dependencias y compilación)
- 🔍 Swagger UI (documentación de la API)
- 🧪 JaCoCo (análisis de cobertura de pruebas)
- 🧠 SonarQube (análisis estático de código)
- 🛠️ Jira (gestión ágil de tareas)
- 💬 Slack (comunicación en equipo)

---

## 📊 Estado del Proyecto

🔧 Proyecto en fase de planeación. Actualmente en construcción de estructura base (scaffolding), diseño técnico y documentación inicial.

- ✅ **API REST básica** - Implementada
- ✅ **Modelo de dominio** - Implementado
- ✅ **Integración MongoDB** - Configurada
- ✅ **Documentación Swagger** - Configurada
- ✅ **Docker/Docker Compose** - Configurado


SIRHA Backend es una API REST desarrollada con Spring Boot que gestiona el sistema de información de recursos humanos y administrativos. El sistema permite la gestión de estudiantes, grupos, solicitudes académicas y procesos de autenticación.

## 📁 Estructura del Proyecto Planeada (Scaffolding MVC)

El proyecto se desarrollará bajo el patrón **MVC (Modelo - Vista - Controlador)**. Esta será la estructura básica:

```plaintext
sirha-backend/
|── docs/
|    |── uml/
|    |── imagenes/
├── src/
│   ├── main/
│   │   ├── java/edu/dosw/sirha/
│   │   │   ├── controller/      # Controladores REST (endpoints)
│   │   │   ├── service/         # Lógica del negocio
│   │   │   ├── model/           # Entidades y modelos de datos
│   │   │   ├── repository/      # Acceso a base de datos (JPA)
│   │   │   └── SirhaApplication.java
│   │   └── resources/
│   │       ├── application.properties
│   │       └── ...
│   └── test/                    # Pruebas unitarias e integración
|── pom.xml                    # Archivo de configuración de Maven
|── .gitignore
|── README.md                  # Readme donde están las evidencias de pruebas y ejecutables

src/
├── main/
│   ├── java/edu/dosw/sirha/SIRHA_BackEnd/
│   │   ├── SirhaBackEndApplication.java       # Clase principal Spring Boot
│   │   ├── config/                            # Configuraciones
│   │   │   ├── SecurityConfig.java           # Configuración de seguridad
│   │   │   └── SwaggerConfig.java            # Configuración de documentación API
│   │   ├── controller/                        # Controladores REST
│   │   │   ├── AuthController.java           # Endpoints de autenticación
│   │   │   ├── StudentController.java        # Gestión de estudiantes
│   │   │   ├── GroupController.java          # Gestión de grupos
│   │   │   └── RequestController.java        # Gestión de solicitudes
│   │   ├── domain/                           # Lógica de negocio
│   │   │   ├── model/                        # Entidades del dominio
│   │   │   └── port/                         # Interfaces (puertos)
│   │   ├── dto/                              # Data Transfer Objects
│   │   ├── service/                          # Interfaces de servicios
│   │   │   └── impl/                         # Implementaciones de servicios
│   │   ├── repository/                       # Acceso a datos
│   │   │   └── mongo/                        # Repositorios MongoDB
│   │   └── util/                             # Utilidades
│   └── resources/
│       └── application.properties            # Configuración de la aplicación
└── test/                                     # Tests unitarios e integración
```


## Comandos
compilar ->  mvn clean compile/verify
ejecutar ->  mvn exec:java -Dexec.mainClass="eci.edu.dosw.proyecto.SIRHA_BackEnd.SirhaBackEndApplication"
ejecutar ->  mvn spring-boot:run
pruebas  ->  mvn test
swagger  -> http://localhost:8080/swagger-ui/index.html

