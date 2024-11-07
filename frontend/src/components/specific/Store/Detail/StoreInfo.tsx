import MenuSelect from "./MenuSelect";
import { useGetStoreDetail } from "../../../../queries/queries";
import { useParams } from "react-router-dom";

interface StoreInfo {
  storeId: number;
  name: string;
  description: string;
  img: string;
}

const StoreInfo = () => {
  const { id } = useParams();
  const info = useGetStoreDetail(Number(id));
  // console.log("info", info);
  if (!info) {
    return <div className="grid grid-cols-2 h-[500px] gap-x-6"></div>;
  }
  return (
    <div className="grid grid-cols-2 h-[500px] gap-x-6">
      <img src={info.img} className="inline-block h-[400px] w-full" />
      <div className="flex flex-col gap-y-3 py-4">
        <p className="text-2xl font-bold">{info.name}</p>
        <p className="text-slate-500 text-base">{info.description}</p>
        <div className="flex">
          <p className="basis-1/6 py-3">상품 선택</p>
          <MenuSelect />
        </div>
      </div>
    </div>
  );
};

export default StoreInfo;
