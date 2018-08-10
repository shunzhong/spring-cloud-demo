package com.learn.springcloud.auth.exception;

/**
 * 返回的错误码常量
 */
public class ErrorCodeConstant {

    //************************************公共参数***********************************
    /**
     * 必填参数缺失
     */
    public final static String MISSING_PARAMETER = "MissingParameter";
    /**
     * 后台发生未知错误，请稍后重试或联系客服解决
     */
    public final static String INTERNAL_ERROR = "InternalError";
    /**
     * 签名不匹配,检查签名方法是否正确
     */
    public final static String SIGNATURE_DOESNOT_MATCH = "SignatureDoesNotMatch";
    /**
     * 用户token信息不存在
     */
    public final static String NULL_TOKEN = "NullToken";
    /**
     * 上传文件大小超过限制
     */
    public final static String MAX_UPLOAD_SIZE_EXCEEDED = "MaxUploadSizeExceeded";
    /**
     * 请求方法不支持
     */
    public final static String HTTP_REQUEST_METHOD_NOTSPPORTED = "HttpRequestMethodNotSupported";
    /**
     * 资源不存在,或查询数据库的结果为空
     */
    public final static String RESOURCE_NOT_FOUND = "ResourceNotFound";
    /**
     * 验证码错误
     */
    public final static String VERIFICATION_CODE_ERROR = "VerificationCodeError";
    /**
     * 邮件发送失败
     */
    public final static String SEND_EMAIL_FAILED = "SendEmailFailed";
    /**
     * 手机验证码发送失败
     */
    public final static String SEND_PHONECODE_FAILED = "SendPhoneCodeFailed";
    /**
     * 参数数据格式不对
     */
    public final static String PARAMETER_FORMAT_NOT_SUPPORTED = "ParameterFormatNotSupported";


    //************************************用户相关***********************************
    /**
     * 用户尚未绑定邮箱
     */
    public final static String USER_UNBIND_EMAIL = "UserUnbindEmail";
    /**
     * 手机号码已存在
     */
    public final static String PHONENO_HAS_EXISTED = "PhoneNoHasExisted";
    /**
     * 邮箱已存在
     */
    public final static String EMAIL_HAS_EXISTED = "EmailHasExisted";
    /**
     * 用户身份证号已存在
     */
    public final static String IDCARD_HAS_EXISTED = "IdCardHasExisted";
    /**
     * 用户名或密码错误
     */
    public final static String USERNAME_OR_PASSWORD_INCORRECT = "UsernameOrPasswordIncorrect";
}
