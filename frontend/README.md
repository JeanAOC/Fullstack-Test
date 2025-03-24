# Frontend (Angular 19)

Aplicación web para la gestión de posts y usuarios.

## Requisitos Previos
- Node.js 18+
- Angular CLI 19.1.5

## Instalación
Ejecuta:
```bash
npm install
```

## Servidor de Desarrollo
Ejecuta:
```bash
ng serve
```
Abre http://localhost:4200 en tu navegador.

## Generar Componentes
Para crear un nuevo componente:
```bash
ng generate component nombre-del-componente
```
## Estructura del Proyecto
**Módulos:**

- **AuthModule**: Maneja la autenticación (login y registro).

- **AdminModule**: Panel de administración para gestionar usuarios y posts.

- **PublicModule**: Sección pública para ver posts.

**Servicios:**

- **AuthService**: Maneja la autenticación y el almacenamiento del token JWT.

- **PostService**: Maneja las operaciones CRUD de posts.

**Componentes:**

- **LoginComponent**: Formulario de inicio de sesión.

- **RegisterComponent**: Formulario de registro.

- **PostListComponent**: Lista de posts públicos.