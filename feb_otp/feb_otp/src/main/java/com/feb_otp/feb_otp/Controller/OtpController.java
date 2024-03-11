package com.feb_otp.feb_otp.Controller;

import com.feb_otp.feb_otp.Payload.OtpRequest;
import com.feb_otp.feb_otp.Payload.OtpVerificationRequest;
import com.feb_otp.feb_otp.Service.TwilioSmsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Random;

@RestController
@RequestMapping("/api/otp")
public class OtpController {

    private final TwilioSmsService twilioSmsService;

    @Autowired
    public OtpController(TwilioSmsService twilioSmsService) {
        this.twilioSmsService = twilioSmsService;
    }

    @PostMapping("/send")
    public ResponseEntity<String> sendOtp(@RequestBody OtpRequest otpRequest) {
        // Generate random 6-digit OTP
        String otp = String.format("%06d", new Random().nextInt(1000000));

        // Send OTP via Twilio
        twilioSmsService.sendSms(otpRequest.getPhoneNumber(), "Your OTP is: " + otp);

        // Store the OTP for verification
        twilioSmsService.storeOtp(otpRequest.getPhoneNumber(), otp);

        return new ResponseEntity<>("OTP sent successfully!", HttpStatus.OK);
    }

    @PostMapping("/verify")
    public ResponseEntity<String> verifyOtp(@RequestBody OtpVerificationRequest otpVerificationRequest) {
        // Verify the OTP
        if (twilioSmsService.verifyOtp(otpVerificationRequest.getPhoneNumber(), otpVerificationRequest.getOtp())) {
            return new ResponseEntity<>("OTP verified successfully!", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Invalid OTP. Please try again.", HttpStatus.OK);
        }
    }
}
