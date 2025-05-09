# Backend (Spring Boot 3.2.6)

API REST para la aplicación Fullstack Test.

## Requisitos Previos
- Java 17
- SQL Server
- Maven

## Configuración
1. Base de datos:
   - Crea una base de datos en SQL Server llamada **"fullstack_db"**.
   - Usuario: "sa", Contraseña: "24092002". (Se pueden cambiar estos datos en el archivo pom.xml)
   - Asegúrate de que SQL Server esté en ejecución en localhost:1433.

**Para cambiar los datos en pom.xml se debe modificar lo siguiente:**
```bash
<configuration>
   <url>jdbc:sqlserver://localhost:1433;databaseName=fullstack_db;encrypt=false;trustServerCertificate=true</url>
   <user>sa</user>
   <password>24092002</password>

</configuration>
```

2. Migraciones de Flyway:

   Ejecuta en la terminal:
   ```bash
   mvn flyway:migrate
   ```
3. Ejecutar la aplicación:
   ```bash
   mvn spring-boot:run
   ```
## Pruebas Unitarias
   Ejecuta:
   ```bash
   mvn test
   ```

### 4. **Consideraciones adicionales**
- **Colección de Postman**: Existen dos tipos de colecciones en Postman: **Collections** y **Environments**. Estas colecciones permiten realizar las pruebas correspondientes de la API

### **Pasos para importar las colecciones**:
1. El archivo `FullStack Test API.postman_collection.json` debe importarse en la sección Collections de Postman.
2. El archivo `Local Dev.postman_environment.json` debe importarse en la sección Environments de Postman.
3. Para seleccionar el entorno en Postman, ubique la opción en la parte superior derecha donde aparece **No environment**, haga clic y seleccione **Local Dev**. Esto permitirá utilizar variables como {{baseUrl}} en las solicitudes.

