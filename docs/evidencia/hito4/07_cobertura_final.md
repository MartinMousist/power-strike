# Cobertura Final — Hito 4

Generado con `mvn clean test jacoco:report`. 72 tests, 0 failures.

## Tabla por paquete (líneas)

| Paquete                    | Líneas cubiertas | % líneas | Ramas cubiertas | % ramas |
|----------------------------|-----------------|----------|-----------------|---------|
| com/powerstrike/controller | 25/25           | **100%** | —               | —       |
| com/powerstrike/service    | 41/41           | **100%** | 4/4             | **100%** |
| com/powerstrike/model      | 28/28           | **100%** | 1/146*          | —       |
| com/powerstrike/dto        | 11/12           | 91.7%    | 0/82*           | —       |
| com/powerstrike/config     | 24/93           | 25.8%    | 5/12            | 41.7%   |
| com/powerstrike/security   | 1/45            | 2.2%     | 0/14            | 0%      |
| com/powerstrike            | 1/3             | 33.3%    | —               | —       |
| **TOTAL**                  | **131/247**     | **53.0%** | **10/258**     | **3.9%** |

_(*) Las ramas de `model` y `dto` provienen de código generado por Lombok
(equals, hashCode, toString, constructores). Son ramas sintéticas impracticables
de cubrir en tests unitarios — el % de ramas del proyecto no refleja la calidad
real del testing._

## Cobertura de las clases críticas

| Clase                 | Líneas   | Ramas    | Observaciones |
|-----------------------|----------|----------|---------------|
| UserController        | 100%     | —        | Todos los endpoints cubiertos |
| ActivityController    | 100%     | —        | Todos los endpoints cubiertos |
| AttendanceController  | 100%     | —        | Todos los endpoints cubiertos |
| AuthController        | 100%     | —        | Login cubierto |
| UserService           | 100%     | 4/4 100% | Incluye rama blank password |
| ActivityService       | 100%     | —        | |
| AttendanceService     | 100%     | —        | |
| GlobalExceptionHandler| 100% líneas | 5/12  | Los 3 handlers cubiertos |

## Comparación inicial vs final

| Paquete       | Inicial | Final  | Variación |
|---------------|---------|--------|-----------|
| controller    | 8.0%    | 100%   | +92%      |
| service       | 100%    | 100%   | —         |
| model         | 96.4%   | 100%   | +3.6%     |
| config        | 25.8%   | 25.8%  | =         |
| **TOTAL**     | **28.3%** | **53.0%** | **+24.7%** |

## Objetivo de cobertura

El Plan SQA establece ≥60% sobre **código nuevo**. El código nuevo del Hito 4
(controllers, validaciones, GlobalExceptionHandler, modelos con anotaciones)
alcanza 100% de cobertura de líneas. El 53% global incluye código pre-existente
sin tests (SecurityConfig, DataInitializer, código Lombok) fuera del alcance
del hito.
