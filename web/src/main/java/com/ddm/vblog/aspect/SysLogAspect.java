package com.ddm.vblog.aspect;

import com.alibaba.fastjson.JSON;
import com.ddm.vblog.annotation.SysLog;
import com.ddm.vblog.base.BaseController;
import com.ddm.vblog.entity.Log;
import com.ddm.vblog.entity.User;
import com.ddm.vblog.exception.BaseException;
import com.ddm.vblog.service.LogService;
import org.apache.shiro.SecurityUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * @Description
 * @Date:2019/2/18 10:55
 * @Author ddm
 **/
@Aspect
@Component
public class SysLogAspect extends BaseController {

    private static final String LOGIN = "login";

    @Resource
    private LogService logService;

    @Pointcut("@annotation(com.ddm.vblog.annotation.SysLog)")
    public void sysLog(){}

    @Around("sysLog()")
    private Object sysLogAround(ProceedingJoinPoint joinPoint){
        SysLog sysLog = ((MethodSignature)joinPoint.getSignature()).getMethod().getAnnotation(SysLog.class);
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request;
        try {
            request = requestAttributes.getRequest();
        } catch (NullPointerException e){
            throw new BaseException("请求异常!");
        }
        ArrayList<Object> objects = new ArrayList<>(Arrays.asList(joinPoint.getArgs()));
        for (int i = 0; i < objects.size(); i++) {
            if(objects.get(i) instanceof HttpServletResponse || objects.get(i) instanceof  HttpServletRequest){
                objects.remove(i);
            }
        }
        //封装log实体信息
        Log log = new Log();
        log.setParams(JSON.toJSONString(objects));
        //开始记录请求时间
        long startTime = System.currentTimeMillis();
        Object obj = null;
        try {
            obj = joinPoint.proceed(joinPoint.getArgs());
        } catch(BaseException e){
            throw e;
        } catch (Throwable e){
            e.printStackTrace();
            throw new BaseException("系统异常!");
        }

        log.setCreateTime(LocalDateTime.now());
        log.setUrl(request.getRequestURI());
        if (request.getHeader("X-Real-IP") == null) {
            log.setIp(request.getRemoteAddr());

        } else{
            log.setIp(request.getHeader("X-Real-IP"));
        }
        log.setIp(request.getRemoteAddr());
        log.setMethod(joinPoint.getSignature().getName());
        log.setOperation(sysLog.value());

        //当前请求的用户信息
        User user = (User)SecurityUtils.getSubject().getPrincipal();
        if(user != null){
            log.setUserId(user.getAccount());
            log.setNickname(user.getNickname());
        }
        log.setTime((double)(System.currentTimeMillis() - startTime) / 1000);
        if(LOGIN.equals(joinPoint.getSignature().getName())){
            Object[] args = joinPoint.getArgs();
            user = (User)args[0];
            user = getUser(user.getAccount());
            if(user != null){
                log.setUserId(user.getAccount());
                log.setNickname(user.getNickname());
            } else{
                log.setNickname("游客!");
            }
        }
        try {
            logService.save(log);
        } catch (Exception e){
            e.printStackTrace();
            return error("系统日志保存异常!");
        }
        return obj;
    }
}
