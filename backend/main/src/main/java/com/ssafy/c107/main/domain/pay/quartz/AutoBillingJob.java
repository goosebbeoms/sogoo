//package com.ssafy.c107.main.domain.pay.quartz;
//
//import com.ssafy.c107.main.domain.pay.dto.AutoBillingDto;
//import com.ssafy.c107.main.domain.pay.service.TossPaymentsService;
//import com.ssafy.c107.main.domain.subscribe.entity.MemberSubscribe;
//import com.ssafy.c107.main.domain.subscribe.repository.MemberSubscribeRepository;
//import lombok.RequiredArgsConstructor;
//import org.quartz.Job;
//import org.quartz.JobExecutionContext;
//import org.quartz.JobExecutionException;
//import org.springframework.stereotype.Component;
//
//@Component
//@RequiredArgsConstructor
//public class AutoBillingJob implements Job {
//
//    private final TossPaymentsService tossPaymentsService;
//    private final MemberSubscribeRepository memberSubscribeRepository;
//    private final QuartzConfig quartzConfig;
//
//    @Override
//    public void execute(JobExecutionContext context) throws JobExecutionException {
//
//        Long subscriptionId = context.getJobDetail().getJobDataMap().getLong("subscriptionId");
//        MemberSubscribe ms = memberSubscribeRepository.findById(subscriptionId)
//                .orElseThrow(() -> new RuntimeException("Subscription not found: " + subscriptionId));
//
//        // 자동 결제 수행
//        boolean billingSuccess = tossPaymentsService.executeAutoBilling(
//                ms.getMember().getId(),
//                new AutoBillingDto(
//                        ms.getMember().getBillingKey(),
//                        ms.getMember().getUuid(),
//                        ms.getSubscribe().getPrice(),
//                        ms.getSubscribe().getName(),
//                        ms.getMember().getName(),
//                        ms.getMember().getEmail()
//                )
//        );
//
//        if (billingSuccess) {
//            // 결제 성공 시, 결제 내역 저장 및 구독 완료 처리
//            ms.completePayment();
//            memberSubscribeRepository.save(ms);
//
//            // 다음 결제 일자를 기준으로 스케줄링 재설정
//            quartzConfig.scheduleAutoBillingJob(subscriptionId, ms.getEndDate());
//
//        } else {
//            throw new JobExecutionException("Failed to process auto billing for ms: " + subscriptionId);
//        }
//    }
//}
//
