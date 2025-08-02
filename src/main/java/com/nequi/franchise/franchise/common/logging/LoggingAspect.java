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

    // Use cases (application/usecase)
    @Pointcut("within(com.nequi.franchise.franchise.application.usecase..*)")
    public void useCaseLayer() {}

    // Handlers (entrypoint/rest/handler)
    @Pointcut("within(com.nequi.franchise.franchise.entrypoint.rest.handler..*)")
    public void handlerLayer() {}

    // Repositories (domain/repository and infrastructure/persistence/impl)
    @Pointcut("within(com.nequi.franchise.franchise.domain.repository..*) || within(com.nequi.franchise.franchise.infrastructure.persistence.impl..*)")
    public void repositoryLayer() {}

    // Log entry and exit of use case methods
    @Around("useCaseLayer()")
    public Object logUseCase(ProceedingJoinPoint joinPoint) throws Throwable {
        logger.info("[USECASE] Entering: {} with arguments: {}", joinPoint.getSignature(), Arrays.toString(joinPoint.getArgs()));
        try {
            Object result = joinPoint.proceed();
            logger.info("[USECASE] Exiting: {} with result: {}", joinPoint.getSignature(), result);
            return result;
        } catch (Throwable ex) {
            logger.error("[USECASE] Exception in: {}: {}", joinPoint.getSignature(), ex.getMessage(), ex);
            throw ex;
        }
    }

    // Log entry and exit of handler methods
    @Around("handlerLayer()")
    public Object logHandler(ProceedingJoinPoint joinPoint) throws Throwable {
        logger.info("[HANDLER] Entering: {} with arguments: {}", joinPoint.getSignature(), Arrays.toString(joinPoint.getArgs()));
        try {
            Object result = joinPoint.proceed();
            logger.info("[HANDLER] Exiting: {} with result: {}", joinPoint.getSignature(), result);
            return result;
        } catch (Throwable ex) {
            logger.error("[HANDLER] Exception in: {}: {}", joinPoint.getSignature(), ex.getMessage(), ex);
            throw ex;
        }
    }

    // Log entry and exit of repository methods
    @Around("repositoryLayer()")
    public Object logRepository(ProceedingJoinPoint joinPoint) throws Throwable {
        logger.debug("[REPOSITORY] Entering: {} with arguments: {}", joinPoint.getSignature(), Arrays.toString(joinPoint.getArgs()));
        try {
            Object result = joinPoint.proceed();
            logger.debug("[REPOSITORY] Exiting: {} with result: {}", joinPoint.getSignature(), result);
            return result;
        } catch (Throwable ex) {
            logger.error("[REPOSITORY] Exception in: {}: {}", joinPoint.getSignature(), ex.getMessage(), ex);
            throw ex;
        }
    }
}
