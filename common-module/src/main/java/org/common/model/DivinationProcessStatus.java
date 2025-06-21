package org.common.model;

public enum DivinationProcessStatus {
    Started,
    Pending,
    PaymentAccepted,
    FailedIntegrationWithChatGPT,
    FinishedWithWrongPaymentStatus,
    Finished
}
