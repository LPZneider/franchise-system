package com.nequi.franchise.franchise.common.logging;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
public class LoggingAspect {

    private static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);

    // Casos de uso (application/usecase)
    @Pointcut("within(com.nequi.franchise.franchise.application.usecase..*)")
    public void useCaseLayer() {}

    // Handlers (entrypoint/rest/handler)
    @Pointcut("within(com.nequi.franchise.franchise.entrypoint.rest.handler..*)")
    public void handlerLayer() {}

    // Repositorios (domain/repository y infrastructure/persistence/impl)
    @Pointcut("within(com.nequi.franchise.franchise.domain.repository..*) || within(com.nequi.franchise.franchise.infrastructure.persistence.impl..*)")
    public void repositoryLayer() {}

    // Log entrada y salida de métodos de casos de uso
    @Around("useCaseLayer()")
    public Object logUseCase(ProceedingJoinPoint joinPoint) throws Throwable {
        logger.info("[USECASE] Entrando a: {} con argumentos: {}", joinPoint.getSignature(), Arrays.toString(joinPoint.getArgs()));
        try {
            Object result = joinPoint.proceed();
            logger.info("[USECASE] Saliendo de: {} con resultado: {}", joinPoint.getSignature(), result);
            return result;
        } catch (Throwable ex) {
            logger.error("[USECASE] Excepción en: {}: {}", joinPoint.getSignature(), ex.getMessage(), ex);
            throw ex;
        }
    }

    // Log entrada y salida de métodos de handlers
    @Around("handlerLayer()")
    public Object logHandler(ProceedingJoinPoint joinPoint) throws Throwable {
        logger.info("[HANDLER] Entrando a: {} con argumentos: {}", joinPoint.getSignature(), Arrays.toString(joinPoint.getArgs()));
        try {
            Object result = joinPoint.proceed();
            logger.info("[HANDLER] Saliendo de: {} con resultado: {}", joinPoint.getSignature(), result);
            return result;
        } catch (Throwable ex) {
            logger.error("[HANDLER] Excepción en: {}: {}", joinPoint.getSignature(), ex.getMessage(), ex);
            throw ex;
        }
    }

    // Log entrada y salida de métodos de repositorios
    @Around("repositoryLayer()")
    public Object logRepository(ProceedingJoinPoint joinPoint) throws Throwable {
        logger.debug("[REPOSITORY] Entrando a: {} con argumentos: {}", joinPoint.getSignature(), Arrays.toString(joinPoint.getArgs()));
        try {
            Object result = joinPoint.proceed();
            logger.debug("[REPOSITORY] Saliendo de: {} con resultado: {}", joinPoint.getSignature(), result);
            return result;
        } catch (Throwable ex) {
            logger.error("[REPOSITORY] Excepción en: {}: {}", joinPoint.getSignature(), ex.getMessage(), ex);
            throw ex;
        }
    }
}

