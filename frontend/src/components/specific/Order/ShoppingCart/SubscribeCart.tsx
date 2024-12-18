import { useNavigate } from "react-router-dom";
import { ListItem, ListItemText, IconButton, Button } from "@mui/material";
import { Close } from "@mui/icons-material";
import useRootStore from "../../../../stores";
import { HiOutlineShoppingBag } from "react-icons/hi2";

const SubscribeCart = () => {
  const { subscribe, setSubscribe } = useRootStore();

  const deleteSubscribe = () => {
    setSubscribe(null);
  };
  const navigate = useNavigate();
  /**
   * 구독 구매 페이지 이동
   * */
  const goToOrder = () => {
    navigate(`/orders/form`, {
      state: { isSubscription: true, accessRoute: location.pathname },
    });
  };

  if (!subscribe) {
    return (
      <div className="flex flex-col gap-4 justify-center items-center h-32 w-full rounded-b-3xl bg-white">
        <HiOutlineShoppingBag className="w-10 h-10 text-gray-400" />
        <p className="text-md text-center">담긴 구독 상품이 없습니다.</p>
      </div>
    );
  }

  return (
    <div className="w-full rounded-b-3xl bg-white flex flex-col gap-4 pt-3">
      {/* 주문 상품 */}
      {subscribe && (
        <ListItem key={subscribe.id} className="flex items-center py-3 px-2">
          {" "}
          <img
            src={subscribe.image}
            alt={subscribe.name}
            className="w-16 h-16 rounded-lg mr-3"
          />
          <ListItemText
            primary={subscribe.name}
            secondary={
              <>
                <span className="text-lg font-bold">
                  {subscribe.price.toLocaleString()}원
                </span>
                {subscribe.beforePrice && (
                  <span className="line-through text-sm text-gray-500 ml-2">
                    {subscribe.beforePrice.toLocaleString()}원
                  </span>
                )}
              </>
            }
          />
          <IconButton onClick={deleteSubscribe}>
            <Close />
          </IconButton>
        </ListItem>
      )}
      <Button size="large" onClick={goToOrder}>
        구독하기
      </Button>
    </div>
  );
};

export default SubscribeCart;
