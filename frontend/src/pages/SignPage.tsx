import { useState } from "react";
import { useNavigate } from "react-router-dom";
import LogoImg from "../assets/logo.png";
import SignInBox from "../components/specific/Sign/SignInBox";
import SignUpBox from "../components/specific/Sign/SignUpBox";
import BrushBorder from "../components/common/BrushBorder";
import Person1 from "../assets/people/person1.png";
import Brush from "../assets/brush.png";

const SignPage = () => {
  const navigate = useNavigate();
  const [clickedLogin, setClickedLogin] = useState<boolean>(true);

  const goToLanding = () => {
    navigate("/");
  };

  return (
    <div className="h-screen flex justify-center items-center">
      <BrushBorder
        className="w-96 h-full max-h-[500px] flex justify-end items-center pr-20"
        borderColor="#333333"
        strokeWidth={4}
      >
        <div className="absolute top-[100px] right-[250px] w-[150px]">
          <img src={Person1} alt="로그인 캐릭터" className="object-cover" />
        </div>
        <div className="absolute bottom-[60px] left-[215px] w-[100px]">
          <img src={Brush} alt="뭇" className="object-cover" />
        </div>
        <img
          className="w-60 mb-10 cursor-pointer transform duration-300 hover:scale-[1.05]"
          src={LogoImg}
          alt="소상한 구독"
          onClick={goToLanding}
        />
      </BrushBorder>

      <div className="w-20"></div>
      <div className="w-[450px] flex flex-col items-center">
        <div className="flex w-full justify-center mb-10">
          <div
            onClick={() => setClickedLogin(true)}
            className={`w-1/2 h-10 border-2 rounded flex justify-center items-center rounded-l-full ${
              clickedLogin
                ? "border-main-800 bg-main-600 text-white"
                : "border-inherit"
            } cursor-pointer`}
          >
            로그인
          </div>
          <div
            onClick={() => setClickedLogin(false)}
            className={`w-1/2 h-10 border-2 rounded flex justify-center items-center rounded-r-full ${
              !clickedLogin
                ? "border-main-800 bg-main-600 text-white"
                : "border-inherit"
            } cursor-pointer`}
          >
            회원가입
          </div>
        </div>

        {clickedLogin ? <SignInBox /> : <SignUpBox />}
      </div>
    </div>
  );
};

export default SignPage;
