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

    public SmsService() {
        this.messageService = NurigoApp.INSTANCE.initialize("NCSVFBRLUOMLNG1K", "OOHFM3UHYYFSZPAZKJITB1FW5SAHNVTA", "https://api.coolsms.co.kr");
    }
    public String sendSms(String phoneNum, String numStr) {
        Message message = new Message();
        // 발신번호 및 수신번호는 반드시 01012345678 형태로 입력되어야 합니다.

        message.setFrom("01026384275");
        message.setTo(phoneNum);
        message.setText("[당근마켓] 안증번호["+numStr+"]");

        SingleMessageSentResponse response = this.messageService.sendOne(new SingleMessageSendingRequest(message));
        System.out.println(response);

        return numStr;

    }
}
