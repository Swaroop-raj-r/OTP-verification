package com.feb_otp.feb_otp.Service;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;

import java.util.HashMap;
import java.util.Map;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class TwilioSmsService {
    private final String accountSid;
    private final String authToken;
    private final String phoneNumber;

    // Store the OTPs for verification
    private final Map<String, String> otpStorage = new HashMap<>();

    public TwilioSmsService(@Value("${twilio.accountSid}") String accountSid,
                            @Value("${twilio.authToken}") String authToken,
                            @Value("${twilio.phoneNumber}") String phoneNumber) {
        this.accountSid = accountSid;
        this.authToken = authToken;
        this.phoneNumber = phoneNumber;
        Twilio.init(accountSid, authToken);
    }

    public void sendSms(String to, String message) {
        // Send SMS logic using Twilio
        Message.creator(
                new com.twilio.type.PhoneNumber(to),
                new com.twilio.type.PhoneNumber(phoneNumber),
                message).create();
    }

    public void storeOtp(String phoneNumber, String otp) {
        // Store the OTP associated with the phone number
        otpStorage.put(phoneNumber, otp);
    }

    public boolean verifyOtp(String phoneNumber, String enteredOtp) {
        // Retrieve the stored OTP for the provided phone number
        String storedOtp = otpStorage.get(phoneNumber);

        // Compare the stored OTP with the entered OTP
        return storedOtp != null && storedOtp.equals(enteredOtp);
    }
}
