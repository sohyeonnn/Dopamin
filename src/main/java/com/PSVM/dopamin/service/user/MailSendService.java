package com.PSVM.dopamin.service.user;

import javax.mail.MessagingException;

public interface MailSendService {
    void makeRandomNumber();

    //이메일 보낼 양식
    String joinEmail(String email) throws MessagingException;

    //이메일 전송 메소드
    void mailSend(String setFrom, String toMail, String title, String content) throws MessagingException;
}
