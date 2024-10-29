// components/Header.tsx
import { Link } from "react-router-dom";
import LogoImg from "../../assets/logo.png";
import { MdOutlinePerson } from "react-icons/md";
import { MdOutlineShoppingCart } from "react-icons/md";

const Header = () => {
  return (
    <>
      <header className="relative flex justify-center shadow-lg z-10">
        <div className="mt-10 mb-5">
          <img src={LogoImg} alt="소상한 구독" className="w-[120px] drop-shadow-lg" />
        </div>
      </header>
      <nav className="sticky top-0 text-lg bg-white shadow-md z-10">
        <div className="flex flex-row justify-between items-center w-full h-14 px-16">
          {/* 페이지 이동 */}
          <div className="flex items-center gap-8">
            <Link to="/">홈</Link>
            <Link to="/store">매장 조회</Link>
          </div>
          {/* user 관련 */}
          <div className="flex items-center gap-8">
            <Link to="/mypage">
              <MdOutlinePerson className="w-5 h-5" />
            </Link>
            <Link to="/cart">
              <MdOutlineShoppingCart className="w-5 h-5" />
            </Link>
            <Link to="/auth">로그인/회원가입</Link>
          </div>
        </div>
      </nav>
    </>
  );
};

export default Header;
