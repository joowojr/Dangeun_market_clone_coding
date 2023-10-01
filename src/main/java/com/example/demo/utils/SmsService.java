package com.example.demo.utils;

import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.request.SingleMessageSendingRequest;
import net.nurigo.sdk.message.response.SingleMessageSentResponse;
import net.nurigo.sdk.message.service.DefaultMessageService;
import org.springframework.stereotype.Service;

@Service
public class SmsService {
    final DefaultMessageService messageService;
    private final static String COOL_SMS_API_KEY = "NCSVFBRLUOMLNG1K";
    private final static String COOL_SMS_API_SECRET_KEY = "OOHFM3UHYYFSZPAZKJITB1FW5SAHNVTA";
    private final static String COOL_SMS_URL = "https://api.coolsms.co.kr";

    public SmsService() {
        this.messageService = NurigoApp.INSTANCE.initialize(COOL_SMS_API_KEY, COOL_SMS_API_SECRET_KEY, COOL_SMS_URL);
    }
    public String sendSms(String phoneNum, String certNum) {
        Message message = new Message();
        // 발신번호 및 수신번호는 반드시 01012345678 형태로 입력되어야 합니다.

        message.setFrom("01026384275");
        message.setTo(phoneNum);
        message.setText("[당근마켓] 안증번호["+certNum+"]");

        SingleMessageSentResponse response = this.messageService.sendOne(new SingleMessageSendingRequest(message));
        System.out.println(response);

        return certNum;

    }
}
