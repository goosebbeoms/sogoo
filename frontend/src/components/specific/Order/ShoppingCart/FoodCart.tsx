import { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import {
  ListItem,
  ListItemText,
  IconButton,
  Checkbox,
  Button,
} from "@mui/material";
import { Remove, Add, Close } from "@mui/icons-material";
import useRootStore from "../../../../stores";
import { HiOutlineSparkles } from "react-icons/hi2";

const FoodCart = () => {
  const {
    foodList,
    deleteSelectedList,
    changeFoodCount,
    setSelectedId,
    selectedId,
  } = useRootStore();
  const [checked, setChecked] = useState<number[]>([]);

  useEffect(() => {
    if (selectedId) {
      setSelectedId(null);
    }
  }, []);
  const handleChecked = (id: number) => {
    if (checked.includes(id)) {
      const updatedChecked = checked.filter((foodId) => foodId !== id);
      setChecked(updatedChecked);
      return;
    }
    setChecked((prev) => [...prev, id]);
  };

  const navigate = useNavigate();

  /**
   * 단일 반찬 구매 페이지 이동
   * */
  const goToOrder = () => {
    setSelectedId(checked);
    navigate(`/orders/form`, {
      state: { isSubscription: false, accessRoute: location.pathname },
    });
  };
  if (!foodList) {
    return (
      <div className="flex flex-col gap-4 justify-center items-center h-32 w-full rounded-b-3xl bg-white">
        <HiOutlineSparkles className="w-10 h-10 text-gray-400" />
        <p className="text-md text-center">담긴 반찬 상품이 없습니다.</p>
      </div>
    );
  }

  return (
    <div className="w-full rounded-b-3xl bg-white flex flex-col gap-8 pt-3">
      {foodList &&
        foodList.map((item) => (
          <ListItem key={item.id} className="flex items-center py-3">
            <Checkbox
              onChange={() => handleChecked(item.id)}
              checked={checked.includes(item.id)}
            />
            <img
              src={item.image}
              alt={item.name}
              className="w-16 h-16 rounded-lg mr-3"
            />
            <ListItemText
              primary={item.name}
              secondary={
                <span className="text-lg font-bold">
                  {item.price.toLocaleString()}원
                </span>
              }
            />
            <div className="flex items-center">
              <IconButton
                onClick={() => changeFoodCount(item.id, -1)}
                disabled={item.count === 1}
              >
                <Remove />
              </IconButton>
              <span>{item.count}</span>
              <IconButton onClick={() => changeFoodCount(item.id, 1)}>
                <Add />
              </IconButton>
            </div>
            <IconButton onClick={() => deleteSelectedList([item.id])}>
              <Close />
            </IconButton>
          </ListItem>
        ))}
      <Button size="large" onClick={goToOrder}>
        구매하기
      </Button>
    </div>
  );
};

export default FoodCart;
