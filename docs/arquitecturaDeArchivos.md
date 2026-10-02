# Arquitectura de archivos

## Ubicación y alcance

El proyecto está en `repo/MyApp`. Este documento está en `repo/docs/arquitecturaDeArchivos.md`.

La arquitectura separa infraestructura técnica (`core`), funcionalidades y conceptos de negocio (`features`) y composición de la aplicación. `user` es una feature independiente; no se utiliza una carpeta `business`.

El árbol siguiente corresponde a `MyApp/shared/src/commonMain/kotlin/com/pad/multiplatformapp/`. Es una organización prevista: los nombres ilustran responsabilidades y no implican que todos los archivos existan. Inicialmente son paquetes dentro del módulo `shared`, no módulos Gradle independientes.

## Estructura propuesta

```text
com/pad/multiplatformapp/
├── App.kt
├── di/
│   └── AppContainer.kt
├── core/
│   ├── network/
│   ├── database/
│   └── ui/
│       ├── components/            # Header, AppLogo, AppName…
│       ├── theme/
│       └── adaptive/              # AppWindowSize…
├── features/
│   ├── user/
│   │   ├── domain/
│   │   │   ├── model/
│   │   │   │   └── User.kt
│   │   │   ├── repository/
│   │   │   │   └── UserRepository.kt
│   │   │   └── usecase/
│   │   └── data/
│   │       ├── remote/            # UserApi, UserDto…
│   │       ├── local/             # UserDao, UserEntity…
│   │       ├── mapper/
│   │       └── repository/
│   │           └── UserRepositoryImpl.kt
│   ├── home/
│   │   └── presentation/
│   │       ├── HomeScreen.kt
│   │       ├── HomeLayout.kt
│   │       └── components/
│   │           ├── TodayWorkoutSection.kt
│   │           ├── ProgressCard.kt
│   │           ├── FriendsSection.kt
│   │           ├── RecomendationSection.kt
│   │           └── ActionCard.kt
│   ├── training/
│   │   ├── domain/
│   │   │   ├── model/
│   │   │   │   ├── Exercise.kt
│   │   │   │   └── TrainingRoutine.kt
│   │   │   ├── repository/
│   │   │   └── usecase/
│   │   ├── data/
│   │   │   ├── remote/
│   │   │   ├── local/
│   │   │   ├── mapper/
│   │   │   └── repository/
│   │   └── presentation/
│   │       ├── TrainingScreen.kt
│   │       ├── RoutineDetailScreen.kt
│   │       └── components/
│   ├── scanner/
│   └── profile/
└── ui/
    ├── MainScaffold.kt
    └── navigation/
        ├── AppScreen.kt
        └── AppNavigation.kt
```

`scanner` y `profile` seguirán la misma organización según las capas que necesiten. Se añadirá `auth` cuando se implemente la autenticación.

## Responsabilidades principales

| Ubicación | Responsabilidad |
| --- | --- |
| `core` | Herramientas transversales y técnicas: red, persistencia y UI reutilizable. No conoce conceptos de negocio como usuario, ejercicio o rutina. |
| `features` | Funcionalidades y conceptos de negocio, como `user`, con las capas que necesiten. Pueden ofrecer modelos y contratos de dominio a otras features. |
| `di` | Construcción y conexión de dependencias mediante `AppContainer`. |
| `ui` global | Estructura general de la aplicación y navegación entre pantallas. |
| `App.kt` | Punto de entrada y composición de la aplicación. |

Que algo sea compartido no significa que sea técnico. `User` es un concepto de negocio y pertenece a `features/user/domain/model`, no a `core`.

El cliente HTTP pertenece a `core/network`, pero `UserApi`, que conoce endpoints y datos de usuarios, pertenece a `features/user/data/remote`. De forma equivalente, la infraestructura general de persistencia pertenece a `core/database`, mientras que los modelos y el acceso específicos de usuarios pertenecen a `features/user/data/local`.

## Capas internas

Dentro de cada feature:

- `domain/model`: modelos de negocio.
- `domain/repository`: contratos de acceso a datos.
- `domain/usecase`: operaciones y reglas de negocio.
- `data/remote`: fuentes remotas y DTO del servidor.
- `data/local`: fuentes locales y entidades de persistencia.
- `data/mapper`: conversiones entre modelos cuando sean necesarias.
- `data/repository`: implementaciones de los contratos de repositorio.
- `presentation`: pantallas, layouts, componentes, estado de UI y su gestión. En esta propuesta se encuentra en las features.

No es obligatorio crear todas las capas ni carpetas vacías. Una feature que solo presenta información y utiliza operaciones compartidas puede tener únicamente `presentation`.

Los modelos cumplen propósitos distintos: `User` representa negocio, `UserDto` el formato del servidor y `ProfileUiState` el estado de una pantalla. Solo se crean las separaciones que sean necesarias.

## Dependencias

- El dominio no depende de Compose, del cliente HTTP, de la BD ni de implementaciones de la capa de datos.
- `data` implementa los contratos de `domain` y utiliza las herramientas técnicas de `core`.
- `presentation` consume modelos y operaciones de dominio.
- `di` proporciona las implementaciones a quienes las necesitan.
- Una feature puede consumir modelos, contratos de repositorio y casos de uso que otra feature exponga como su API de dominio. No accede a sus implementaciones de `data`, DTO, entidades locales ni estado de presentación.
- Las dependencias entre features deben ser explícitas y sin ciclos. Por ejemplo, `profile` y `auth` pueden depender del dominio de `user`, pero `user` no depende de ellas.
- `core` no depende del negocio ni de las features. La navegación global conecta las pantallas mediante callbacks.

Una feature no duplica un repositorio por utilizarlo: por ejemplo, `profile` puede consumir `UserRepository` de `features/user/domain` sin crear otro repositorio equivalente en su propia capa `data`.

## Ubicación de los modelos y casos de uso

`User` se declara en `features/user/domain/model/User.kt`. La feature `user` es responsable del modelo, las reglas y el acceso a los datos de usuarios, y expone los contratos necesarios a otras features. Autenticarse, gestionar la sesión y editar el perfil son funcionalidades que utilizan al usuario, pero no son propietarias de todo su significado.

`Exercise` comienza en `features/training/domain/model/Exercise.kt`. Si otra feature necesita ese concepto, puede consumir la API de dominio expuesta por `training`. Si el catálogo de ejercicios adquiere una responsabilidad independiente, puede extraerse a una feature `exercise`, con su propio dominio y datos. No se crea una carpeta de negocio compartido por el mero hecho de reutilizar un modelo.

Compartir un modelo base no elimina el dominio propio de una feature. Por ejemplo:

```text
features/training/domain/model/Exercise.kt
features/training/domain/model/RoutineExercise.kt
features/training/domain/model/TrainingRoutine.kt
features/training/domain/usecase/CompleteWorkoutUseCase.kt
```

`Exercise` puede describir un ejercicio del catálogo; `RoutineExercise` referencia ese ejercicio y añade su configuración dentro de una rutina, como series y repeticiones.

Los casos de uso pertenecen a la feature responsable de la operación. Cuando otras features los necesiten, pueden formar parte de su API de dominio. No se duplican modelos ni repositorios por cada consumidor, ni se extraen conceptos solo porque podrían compartirse en el futuro.

## Features iniciales

| Feature | Responsabilidad |
| --- | --- |
| `user` | Modelo, reglas y acceso a datos de usuarios, disponibles mediante contratos de dominio para otras features. |
| `home` | Resumen de información y accesos a funcionalidades. |
| `training` | Rutinas, detalle de ejercicios y desarrollo del entrenamiento. |
| `scanner` | Flujo de escaneo y presentación de resultados. |
| `profile` | Consulta y edición del perfil. |

Una feature no necesita tener una pantalla propia: `user` puede comenzar con `domain` y `data`. `profile` mantiene el flujo de consulta y edición del perfil y consume los contratos de `user`; `auth` mantiene la autenticación y la sesión. No se duplica la gestión de datos del usuario entre estas features.

Una feature puede tener varias pantallas. Por ahora, las rutinas forman parte de `training`, sin crear una feature `trainingRoutine` con responsabilidades solapadas.

`auth` se incorporará al implementar registro e inicio de sesión. `social` y `progress` podrán extraerse si los retos, amistades, estadísticas u objetivos adquieren funcionalidad propia. Una tarjeta o sección visual no exige por sí sola una feature independiente.

## Presentación y layouts

Se conserva la organización actual de Home dentro de `features/home/presentation`:

- `HomeScreen.kt`: recibe el estado y los callbacks, selecciona la variante de layout y le pasa la información.
- `HomeLayout.kt`: contiene `HomeCompactLayout`, `HomeMediumLayout` y `HomeExtendedLayout`, con la distribución visual de las secciones.
- `components/`: tarjetas y secciones propias de Home, reutilizables entre sus variantes de layout.

El estado compartido entre variantes debe mantenerse por encima de los layouts. Por ejemplo, `durationMinutes` no debe tener una copia independiente en cada variante.

Si el archivo de layouts crece, puede dividirse en:

```text
presentation/
├── HomeScreen.kt
├── layouts/
│   ├── HomeCompactLayout.kt
│   ├── HomeMediumLayout.kt
│   └── HomeExtendedLayout.kt
└── components/
```

`Header` puede vivir en `core/ui/components` si es común a varias features. Su distribución interna pertenece al componente; la pantalla decide su posición y márgenes externos mediante `Modifier`. Si aparece siempre en todas las pantallas, puede colocarse una sola vez en el contenedor global.

`MainScaffold` y la navegación pertenecen a la composición global, no a Home.

## Ejemplo: Home abre el detalle de una rutina

1. Home muestra una tarjeta con el resumen de una rutina.
2. La tarjeta ejecuta un callback y Home comunica `onOpenRoutine(routineId)`.
3. La navegación global abre la pantalla de detalle de `training`, pasando el identificador y gestionando la vuelta.
4. La pantalla de detalle obtiene la información mediante su repositorio.

La tarjeta de resumen puede permanecer en Home aunque muestre información sobre entrenamiento. Si varias pantallas necesitan exactamente el mismo componente visual, se evalúa su extracción.

Si Home y Training necesitan acceder a los mismos datos de rutinas, Home consume los modelos y contratos de dominio expuestos por `training`. La implementación del repositorio permanece en `training/data` y `di` proporciona la instancia necesaria. Training no depende de Home. Las pantallas y sus layouts permanecen en cada feature.

## Evolución

Se empieza con paquetes dentro de `shared`. Separar las features o sus contratos de dominio en módulos Gradle independientes es una decisión posterior, cuando resulte útil controlar esas dependencias mediante el sistema de compilación.

Se mantiene la nomenclatura en inglés (`features`, `user`, `domain`, etc.) por coherencia con el código existente.


