package net.lsun.bbs171.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import net.lsun.bbs171.entity.CodeResult;

public class AliyunUtil {

    /**
     * 发送短信验证码
     *
     * @param phone 接收手机号
     * @param code  验证码
     * @return 结果
     */
    public static JSONObject sendCode(String phone, String code) {
        final String accessKeyId = "LTAI4G4o1KM38JH25mL4W5dm";
        final String accessKeySecret = "dcexjXxirteZjAiuVY93NZySQiHiD7";

        DefaultProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId, accessKeySecret);
        IAcsClient client = new DefaultAcsClient(profile);

        CommonRequest req = new CommonRequest();
        req.setSysMethod(MethodType.POST);
        req.setSysDomain("dysmsapi.aliyuncs.com");
        req.setSysVersion("2017-05-25");
        req.setSysAction("SendSms");
        req.putQueryParameter("RegionId", "cn-hangzhou");
        req.putQueryParameter("PhoneNumbers", phone);
        req.putQueryParameter("SignName", "初升的太阳");
        req.putQueryParameter("TemplateCode", "SMS_181211375");

        req.putQueryParameter("TemplateParam", "{\"code\":\"" + code + "\"}");

        JSONObject json = new JSONObject();

        try {
            CommonResponse res = client.getCommonResponse(req);
            CodeResult codeResult = JSON.toJavaObject(JSONObject.parseObject(res.getData()), CodeResult.class);

            if ("OK".equals(codeResult.Code)) {
                json.put("success", true);
                json.put("msg", "发送成功!");
            } else {
                json.put("success", false);
                json.put("msg", "发送失败: " + codeResult.Message);
            }

        } catch (ClientException e) {
            json.put("success", false);
            json.put("msg", "发送失败!");
            e.printStackTrace();
        }

        return json;
    }

    /**
     * 生成一个6位纯数字随机验证码
     *
     * @return code
     */
    public static String generateCode() {
        return (Math.random() + "").substring(2, 8);
    }

}
