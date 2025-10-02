package com.fs.ecommerce_multivendor.entity;

import com.fs.ecommerce_multivendor.enums.Paymentstatus;
import lombok.*;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PaymentDetails {

    private String paymentId;
    private String razorpayPaymentLinkId;
    private String razorpayPaymentLinkReferenceId;
    private String razorpayPaymentLinkStatus;
    private String razorpayPaymentIdZWSP;
    private Paymentstatus status;

}
