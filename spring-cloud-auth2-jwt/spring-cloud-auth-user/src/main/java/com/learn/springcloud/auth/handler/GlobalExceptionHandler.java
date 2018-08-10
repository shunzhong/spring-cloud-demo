package com.learn.springcloud.auth.handler;


import com.learn.springcloud.auth.exception.*;
import com.learn.springcloud.auth.signature.exception.SignatureException;
import com.learn.springcloud.auth.util.Result;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class GlobalExceptionHandler {

    private final static Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * 签名异常全局处理
     *
     * @param se
     * @return
     */
    @ExceptionHandler(SignatureException.class)
    @ResponseBody
    public Object handleSignatureException(SignatureException se) {
        //result.setException(ExceptionUtils.getStackTrace(se));
        LOGGER.warn(se.getMessage(), se);
        return Result.codeDesc(ErrorCodeConstant.SIGNATURE_DOESNOT_MATCH, se.getMessage());
    }


    /**
     * 服务器内部错误
     *
     * @param ne
     */
    @ExceptionHandler(SystemErrorException.class)
    @ResponseBody
    public Result systemErrorException(SystemErrorException ne) {
        LOGGER.warn(ne.getMessage(), ne);
        Result result = new Result();
        result.setResCode(ErrorCodeConstant.INTERNAL_ERROR);
        result.setResDesc(ne.getMessage());
        result.setException(ExceptionUtils.getStackTrace(ne));
        return result;
    }

    /**
     * 验证码校验不通过
     *
     * @param ne
     */
    @ExceptionHandler(VerificationCodeErrorException.class)
    @ResponseBody
    public Result verificationCodeErrorException(VerificationCodeErrorException ne) {
        return Result.codeDesc(ErrorCodeConstant.VERIFICATION_CODE_ERROR, "验证码错误");
    }


    /**
     * 资源不存,或查询数据库的结果为空
     *
     * @param ne
     * @return
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseBody
    public Result handleResourceNotFoundException(ResourceNotFoundException ne) {
        //LOGGER.warn(ne.getMessage(), ne);
        return Result.codeDesc(ErrorCodeConstant.RESOURCE_NOT_FOUND, ne.getMessage());
    }


    /**
     * 请求参数为空
     *
     * @param ne
     * @return
     */
    @ExceptionHandler(BadRequestException.class)
    @ResponseBody
    public Result handleBadRequestException(BadRequestException ne) {
        return Result.codeDesc(ErrorCodeConstant.MISSING_PARAMETER, ne.getMessage());
    }

    /**
     * token为空的异常
     *
     * @param ne
     * @return
     */
    @ExceptionHandler(NullTokenException.class)
    //@ResponseStatus(value = HttpStatus.UNAUTHORIZED)
    @ResponseBody
    public Result nullTokenException(NullTokenException ne) {
        return Result.codeDesc(ErrorCodeConstant.NULL_TOKEN, "用户token信息为空");
    }


    /**
     * 文件上传大小超过限制异常
     * 大小配置在spring-mvc-servlet.xml中定义了
     *
     * @param e
     * @return
     */
    @ExceptionHandler(org.springframework.web.multipart.MaxUploadSizeExceededException.class)
    @ResponseBody
    public Object fileSizeException(org.springframework.web.multipart.MaxUploadSizeExceededException e) {
        LOGGER.warn(e.getMessage(), e);
        return Result.codeDesc(ErrorCodeConstant.MAX_UPLOAD_SIZE_EXCEEDED, "文件大小超过5M");
    }

    /**
     * http请求方法不支持异常，post  get put
     *
     * @param e
     * @return
     */
    @ExceptionHandler(org.springframework.web.HttpRequestMethodNotSupportedException.class)
    @ResponseBody
    public Object requestMethodNotSupported(org.springframework.web.HttpRequestMethodNotSupportedException e) {
        return Result.codeDesc(ErrorCodeConstant.HTTP_REQUEST_METHOD_NOTSPPORTED, "http请求的方法类型不支持");
    }

    /**
     * 参数数据格式不对
     *
     * @param ne
     * @return
     */
    @ExceptionHandler(ParameterFormatNotSupportedException.class)
    @ResponseBody
    public Result handleResourceConflictException(ParameterFormatNotSupportedException ne) {
        //LOGGER.warn(ne.getMessage(), ne);
        return Result.codeDesc(ErrorCodeConstant.PARAMETER_FORMAT_NOT_SUPPORTED, ne.getMessage());
    }

    /**
     * 其他异常处理
     *
     * @param ne
     * @return
     */
    @ExceptionHandler(Exception.class)
    //@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public Result handleOthersException(Exception ne) {
        LOGGER.warn(ne.getMessage(), ne);
        return new Result(HttpStatus.INTERNAL_SERVER_ERROR.value(), ne.getMessage(), ExceptionUtils.getStackTrace(ne));
    }


    /**
     * 手机号已被使用，用户已存在
     *
     * @param ne
     * @return
     */
    @ExceptionHandler(PhoneNoHasExistedException.class)
    @ResponseBody
    public Result handlePhoneNoHasExistedException(PhoneNoHasExistedException ne) {
        //LOGGER.warn(ne.getMessage(), ne);
        return Result.codeDesc(ErrorCodeConstant.PHONENO_HAS_EXISTED, ne.getMessage());
    }

    /**
     * 邮箱账号已被使用
     *
     * @param ne
     * @return
     */
    @ExceptionHandler(EmailHasExistedException.class)
    @ResponseBody
    public Result handleEmailHasExistedException(EmailHasExistedException ne) {
        //LOGGER.warn(ne.getMessage(), ne);
        return Result.codeDesc(ErrorCodeConstant.EMAIL_HAS_EXISTED, ne.getMessage());
    }

    /**
     * 邮件发送失败
     *
     * @param ne
     * @return
     */
    @ExceptionHandler(SendEmailFailedException.class)
    @ResponseBody
    public Result handleSendEmailFailedException(EmailHasExistedException ne) {
        LOGGER.error(ne.getMessage(), ne);
        return Result.codeDesc(ErrorCodeConstant.SEND_EMAIL_FAILED, ne.getMessage());
    }

    /**
     * 手机验证码发送失败
     *
     * @param ne
     * @return
     */
    @ExceptionHandler(SendPhoneCodeFailedException.class)
    @ResponseBody
    public Result handleSendPhoneCodeFailedException(SendPhoneCodeFailedException ne) {
        LOGGER.error(ne.getMessage(), ne);
        return Result.codeDesc(ErrorCodeConstant.SEND_PHONECODE_FAILED, ne.getMessage());
    }
}
