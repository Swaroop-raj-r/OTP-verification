package com.feb_otp.feb_otp.Payload;

import lombok.Data;

@Data
public class OtpVerificationRequest {
    private String phoneNumber;
    private String otp;
}
