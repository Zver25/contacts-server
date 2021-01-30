package developer.contacts.aop;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

@Aspect
@Component
public class LoggingAspect {

    private static final Logger LOGGER = LogManager.getLogger(LoggingAspect.class);

    @Pointcut("@annotation(org.springframework.web.bind.annotation.GetMapping)")
    public void getRequest() {
    }

    @Pointcut("@annotation(org.springframework.web.bind.annotation.PostMapping)")
    public void postRequest() {
    }

    @Pointcut("@annotation(org.springframework.web.bind.annotation.PutMapping)")
    public void putRequest() {
    }

    @Pointcut("@annotation(org.springframework.web.bind.annotation.DeleteMapping)")
    public void deleteRequest() {
    }

    @Pointcut("@annotation(org.springframework.messaging.handler.annotation.MessageMapping)")
    public void messageMapping() {
    }

    @Pointcut("@annotation(org.springframework.messaging.simp.annotation.SubscribeMapping)")
    public void subscribeMapping() {
    }

    @Pointcut("messageMapping() || subscribeMapping()")
    public void mappings() {
    }

    @Pointcut("getRequest() || postRequest() || putRequest() || deleteRequest() || mappings()")
    public void allRequest() {
    }

    @Pointcut("execution(public * *(..))")
    protected void atExecution() {
    }

    @Before("allRequest() && atExecution()")
    public void logBefore(JoinPoint joinPoint) {
        LOGGER.info("Execute {}.{}()", joinPoint.getSignature().getDeclaringTypeName(), joinPoint.getSignature().getName());
        LOGGER.info("Arguments: {}", Arrays.toString(joinPoint.getArgs()));
    }

    @Before("allRequest() && atExecution() && args(.., request)")
    public void logBeforeWithRequest(HttpServletRequest request) {
        if (request != null) {
            LOGGER.info("sessionId: {}", request.getSession().getId());
        }
    }

    @AfterThrowing(pointcut = "allRequest() && atExecution()", throwing = "exception")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable exception) {
        LOGGER.error("An exception has been thrown in {}.{}()",
                joinPoint.getSignature().getDeclaringTypeName(),
                joinPoint.getSignature().getName()
        );
        LOGGER.error("Cause : " + exception.getCause());
    }
}
