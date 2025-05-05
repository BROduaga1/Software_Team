package com.example.gymcrm.controller.logging;

import com.example.gymcrm.util.TransactionIdGenerator;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class LogTransactionAspect {

    private final TransactionIdGenerator transactionIdGenerator;

    public LogTransactionAspect(TransactionIdGenerator transactionIdGenerator) {
        this.transactionIdGenerator = transactionIdGenerator;
    }

    @Around("@annotation(com.example.gymcrm.controller.logging.LogTransaction)")
    public Object logTransaction(ProceedingJoinPoint joinPoint) throws Throwable {
        String transactionId = transactionIdGenerator.generateTransactionId();
        Object[] args = joinPoint.getArgs();
        LOGGER.info("Method {} called with args: {} and transaction id: {}", joinPoint.getSignature(), args, transactionId);

        Object result = joinPoint.proceed();

        LOGGER.info("Method {} returned: {} with transaction id: {}", joinPoint.getSignature(), result, transactionId);
        return result;
    }
}
