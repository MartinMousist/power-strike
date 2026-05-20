# Evidencia de cobertura JaCoCo

**Fecha de ejecución:** 20/05/2026  
**Comando ejecutado:** `cd backend && mvn verify`  
**Versión JaCoCo:** 0.8.11  
**Reporte HTML:** `backend/target/site/jacoco/index.html`

---

## CSV completo (jacoco.csv)

```csv
GROUP,PACKAGE,CLASS,INSTRUCTION_MISSED,INSTRUCTION_COVERED,BRANCH_MISSED,BRANCH_COVERED,LINE_MISSED,LINE_COVERED,COMPLEXITY_MISSED,COMPLEXITY_COVERED,METHOD_MISSED,METHOD_COVERED
powerstrike,com.powerstrike.service,ActivityService,0,64,0,0,0,14,0,7,0,7
powerstrike,com.powerstrike.service,AttendanceService,0,52,0,0,0,10,0,5,0,5
powerstrike,com.powerstrike.service,UserService,0,88,1,3,0,17,1,8,0,7
powerstrike,com.powerstrike.config,SecurityConfig,153,0,0,0,26,0,9,0,9,0
powerstrike,com.powerstrike.config,AppConfig,7,0,0,0,2,0,2,0,2,0
powerstrike,com.powerstrike.config,DataInitializer,160,0,4,0,41,0,4,0,2,0
powerstrike,com.powerstrike.dto,AttendanceRequest,98,17,22,0,0,3,15,5,4,5
powerstrike,com.powerstrike.dto,LoginResponse,207,0,38,0,6,0,32,0,13,0
powerstrike,com.powerstrike.dto,LoginRequest,115,0,22,0,3,0,20,0,9,0
powerstrike,com.powerstrike.security,JwtUtil,75,0,4,0,18,0,9,0,7,0
powerstrike,com.powerstrike.security,UserDetailsServiceImpl,39,0,0,0,8,0,3,0,3,0
powerstrike,com.powerstrike.security,JwtAuthFilter,75,0,10,0,19,0,7,0,2,0
powerstrike,com.powerstrike.controller,AttendanceController,23,0,0,0,4,0,4,0,4,0
powerstrike,com.powerstrike.controller,UserController,37,0,0,0,7,0,6,0,6,0
powerstrike,com.powerstrike.controller,ActivityController,37,0,0,0,7,0,6,0,6,0
powerstrike,com.powerstrike.controller,AuthController,57,0,0,0,7,0,2,0,2,0
powerstrike,com.powerstrike,PowerStrikeApplication,8,0,0,0,3,0,2,0,2,0
powerstrike,com.powerstrike.model,User,255,77,57,1,1,10,34,15,5,15
powerstrike,com.powerstrike.model,Activity,227,62,50,0,1,9,31,12,6,12
powerstrike,com.powerstrike.model,Attendance,170,42,38,0,0,7,24,9,5,9
```

---

## Cobertura del paquete service (código nuevo — umbral Plan SQA: ≥ 60%)

| Clase | Instrucciones cubiertas | Instrucciones perdidas | % cobertura |
|---|---|---|---|
| ActivityService | 64 | 0 | **100%** |
| AttendanceService | 52 | 0 | **100%** |
| UserService | 88 | 1 | **99%** |

> UserService tiene 1 instrucción perdida correspondiente a la rama `password.isBlank()` — cubierta con 3/4 ramas (75% de branches).

---

## Cobertura global del proyecto

| Paquete | Estado |
|---|---|
| `com.powerstrike.service` | 100% — código nuevo, cubierto en Hito 3 |
| `com.powerstrike.controller` | 0% — MVP preexistente, se cubre en Hito 4 |
| `com.powerstrike.security` | 0% — MVP preexistente, se cubre en Hito 4 |
| `com.powerstrike.config` | 0% — configuración de infraestructura |
| **Global (instrucciones)** | **~18%** |

> El umbral de 60% del Plan SQA aplica a **código nuevo**. Los services implementados en este hito alcanzan 100%.

---

> El reporte HTML completo está disponible en `backend/target/site/jacoco/index.html`. Se recomienda abrirlo en el navegador para ver el detalle línea por línea.
