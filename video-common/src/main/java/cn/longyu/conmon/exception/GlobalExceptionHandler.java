package cn.longyu.conmon.exception;


import cn.longyu.conmon.result.Result;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

//全局异常处理器 处理整个Contoller层的异常
//当有多个异常处理器时候，会根据就近原则 谁匹配的更精确，就执行谁
@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Result error(Exception e){
        e.printStackTrace();
        return Result.fail();
    }
    @ExceptionHandler(VideoException.class)
    @ResponseBody
    public Result error(VideoException e){
        e.printStackTrace();
        return Result.fail(e.getCode(), e.getMessage());
    }
}
