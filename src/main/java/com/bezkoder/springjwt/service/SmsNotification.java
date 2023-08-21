package com.bezkoder.springjwt.service;


import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import org.apache.http.auth.AUTH;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class SmsNotification {

    @Value("${auth.sid}")
    private String authSid;

    @Value("${auth.key}")
    private String authKey;

    @Value("${auth.msg.serv.sid}")
    private String servId;

    public String sendSMS(String phoneNumber, String msgBody) {
        System.out.println(phoneNumber+" ========================== "+msgBody+" =================== "+authSid+" =================="+ authKey+" ============== "+servId);
        if (phoneNumber.length()==13) phoneNumber+="+";
        else if (phoneNumber.length()==14) phoneNumber=phoneNumber;
        else return "Phone Number Invalid";
        Twilio.init(authSid, authKey);
        Message message = Message.creator(new com.twilio.type.PhoneNumber(phoneNumber),servId, msgBody).create();

        System.out.println("========================== The RESPONSE FROM THE MESSAGE SENT =====================================");
        System.out.println("The RESPONSE FROM THE MESSAGE SENT "+message);
        return message.getSid()+"-"+message.getStatus();
    }

    public static void main(String[] args) {
        System.out.println("+2349098163085".length());
    }
}



