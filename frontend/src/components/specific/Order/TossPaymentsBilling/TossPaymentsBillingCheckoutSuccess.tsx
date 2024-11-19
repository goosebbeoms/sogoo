import { useMutation } from "@tanstack/react-query";
import { useEffect, useState } from "react";
import { useNavigate, useSearchParams } from "react-router-dom";
import sogoo from "../../../../services/sogoo";
import useRootStore from "../../../../stores";
import formatters from "../../../../utils/formatters";

const TossPaymentsBillingCheckoutSuccess = () => {
  const { memberInfo, setSubscribe } = useRootStore();
  const [isConfirmed, setIsConfirmed] = useState(false);
  const navigate = useNavigate();
  const [searchParams] = useSearchParams();

  const orderId = searchParams.get("orderId");
  // const orderName = searchParams.get("orderName");
  const amount = Number(searchParams.get("amount"));
  const customerKey = searchParams.get("customerKey")!;
  const authKey = searchParams.get("authKey")!;
  const subscribeId = Number(searchParams.get("subscribeId")!);

  useEffect(() => {
    if (!isConfirmed) {
      confirmPayment();
    } else {
      setSubscribe(null);
    }
  }, [isConfirmed]);

  const { mutate: requestSubscribePayment } = useMutation({
    mutationFn: sogoo.requestSubscribePayment,
    onSuccess: () => {
      setIsConfirmed(true);
      setSubscribe(null);
    },
  });

  const confirmPayment = async () => {
    try {
      if (!orderId || !subscribeId || !customerKey || !authKey) {
        throw new Error("Invalid payment data");
      }

      const data: SubscribePaymentsConfirmRequest = {
        orderId,
        subscribeId,
        customerKey,
        authKey,
      };
      console.log("data", data);

      requestSubscribePayment(data);
    } catch (error) {
      console.error("Payment confirmation failed:", error);
      // 에러 발생 시 원래 페이지로 돌아가기
      // if (currentPath) {
      //   navigate(decodeURIComponent(currentPath), {
      //     replace: true,
      //     state: { paymentError: true },
      //   });
      // }
    }
  };

  const goToMypage = () => {
    navigate("/mypage");
  };

  return (
    <div className="flex flex-col items-center p-6 w-full overflow-auto">
      {isConfirmed ? (
        <div
          className="hidden flex-col items-center w-full max-w-[540px]"
          style={{
            display: "flex",
          }}
        >
          <img
            src="https://static.toss.im/illusts/check-blue-spot-ending-frame.png"
            className="w-[120px] h-[120px]"
          />
          <h2 className="mt-8 text-[#191f28] font-bold text-2xl">
            결제를 완료했어요
          </h2>
          <div className="w-full my-[60px] flex flex-col gap-4 text-xl">
            <div className="flex justify-between">
              <span className="font-semibold text-[#333d48] text-base">
                주문번호
              </span>
              <span
                id="orderId"
                className="font-medium text-[#4e5968] text-base pl-4 break-words text-right"
              >
                {orderId}
              </span>
            </div>
            <div className="flex justify-between">
              <span className="font-semibold text-[#333d48] text-base">
                받는 분
              </span>
              <span
                id="amount"
                className="font-medium text-[#4e5968] text-base pl-4 break-words text-right"
              >
                {memberInfo?.name}
              </span>
            </div>
            <div className="flex justify-between">
              <span className="font-semibold text-[#333d48] text-base">
                휴대폰
              </span>
              <span
                id="amount"
                className="font-medium text-[#4e5968] text-base pl-4 break-words text-right"
              >
                {formatters.formatPhoneNumber(memberInfo?.phoneNumber)}
              </span>
            </div>
            {/* <div className="flex justify-between">
              <span className="font-semibold text-[#333d48] text-base">
                구매 상품명
              </span>
              <span
                id="amount"
                className="font-medium text-[#4e5968] text-base pl-4 break-words text-right"
              >
                {orderName}
              </span>
            </div> */}
            <div className="flex justify-between">
              <span className="font-semibold text-[#333d48] text-base">
                결제 금액
              </span>
              <span
                id="amount"
                className="font-medium text-[#4e5968] text-base pl-4 break-words text-right"
              >
                {formatters.formatToCurrency(amount)}
              </span>
            </div>
          </div>
          <button
            className="w-full px-[22px] py-[11px] rounded-lg text-[#3282f6] bg-[#f9fcff]"
            onClick={goToMypage}
          >
            마이페이지
          </button>
        </div>
      ) : (
        <div className="flex flex-col justify-between items-center mt-[72px] w-full h-[400px] max-w-[540px]">
          <div className="flex flex-col items-center">
            <img
              src="https://static.toss.im/lotties/loading-spot-apng.png"
              className="w-[120px] h-[120px]"
            />
            <h2 className="mt-8 text-[#191f28] font-bold text-2xl text-center">
              구독 결제 요청까지 성공했어요.
            </h2>
            <h4 className="mt-2 text-[#4e5968] font-medium text-base text-center">
              구독 결제 승인 요청 중이에요. 잠시만 기다려 주세요.
            </h4>
          </div>
        </div>
      )}
    </div>
  );
};

export default TossPaymentsBillingCheckoutSuccess;