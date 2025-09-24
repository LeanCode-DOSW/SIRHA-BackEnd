# SIRHA - Sistema de Reasignación de Horarios Académicos

Este proyecto tiene como objetivo desarrollar un sistema de software denominado **SIRHA** para la Escuela Colombiana de Ingeniería, siguiendo buenas prácticas de ingeniería de software y metodologías ágiles.
---
## 🧪 Estado del Proyecto

🔧 Proyecto en fase de planeación. Actualmente en construcción de estructura base (scaffolding), diseño técnico y documentación inicial.

---

## 🚀 Tecnologías Utilizadas

- ☕ Java 17+
- 🌱 Spring Boot
- 📦 Maven (gestión de dependencias y compilación)
- 🔍 Swagger UI (documentación de la API)
- 🧪 JaCoCo (análisis de cobertura de pruebas)
- 🧠 SonarQube (análisis estático de código)
- 🛠️ Jira (gestión ágil de tareas)
- 💬 Slack (comunicación en equipo)

---

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
