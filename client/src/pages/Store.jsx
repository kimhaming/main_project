import ShopInfo from '../components/store/ShopInfo.jsx';
import { useParams } from 'react-router-dom';
import { useEffect, useState, useRef } from 'react';
import MenuTab from '../components/store/MenuTab.jsx';
import StoreReviewTab from '../components/store/StoreReviewTab.jsx';
import axios from 'axios';
import LoadingSpinner from '../components/Loading.jsx';

const Store = () => {
  const { id } = useParams();
  const [storeData, setStoreData] = useState(null);
  const [reviewData, setReviewData] = useState([]);
  const menuRef = useRef(null);
  const reviewRef = useRef(null);
  const apiUrl = process.env.REACT_APP_API_URL;

  //스크롤 위치에 따른 상태 추가
  const [isMenuTabActive, setIsMenuTabActive] = useState(false);
  const [isReviewTabActive, setIsReviewTabActive] = useState(false);

  // handleScroll 함수 정의
  const handleScroll = () => {
    const scrollY = window.scrollY;
    const stickyTabHeight = 43;

    // null 체크 추가
    if (menuRef.current && reviewRef.current) {
      const menuTabOffset = menuRef.current.offsetTop - stickyTabHeight;
      const reviewTabOffset = reviewRef.current.offsetTop - stickyTabHeight;

      if (scrollY >= menuTabOffset && scrollY < reviewTabOffset) {
        setIsMenuTabActive(true);
        setIsReviewTabActive(false);
      } else if (scrollY >= reviewTabOffset) {
        setIsMenuTabActive(false);
        setIsReviewTabActive(true);
      } else {
        setIsMenuTabActive(false);
        setIsReviewTabActive(false);
      }
    }
  };

  useEffect(() => {
    // 상점 정보
    axios
      .get(`${apiUrl}/api/stores/${id}`)
      .then((res) => {
        setStoreData(res.data.store);
        console.log(res.data.store);
      })
      .catch((err) => {
        console.error(err);
      });
    // 리뷰 정보
    axios
      .get(`${apiUrl}/api/stores/${id}/reviews?page=1&size=10`)
      .then((res) => {
        setReviewData(res.data.reviews);
        console.log(res.data.reviews);
      })
      .catch((err) => {
        console.error(err);
      });

    // 스크롤 이벤트 리스너 추가
    window.addEventListener('scroll', handleScroll);

    return () => {
      window.removeEventListener('scroll', handleScroll);
    };
  }, [apiUrl, id]);

  if (!storeData) {
    return <LoadingSpinner />;
  }

  const scrollTo = (ref) => {
    if (ref.current) {
      const stickyTabHeight = 43; // 스티키 탭의 높이
      const targetOffset = ref.current.offsetTop - stickyTabHeight;
      window.scrollTo({ top: targetOffset, behavior: 'smooth' });
    }
  };

  return (
    <div className="flex flex-col relative">
      <ShopInfo store={storeData} />
      <ul className="flex justify-center text-center w-[1070px] mx-auto mb-1 sticky top-[65px] bg-white z-10">
        <li
          className={`w-full hover:bg-[#ccc] py-3 border-r ${
            isMenuTabActive ? 'bg-[#ccc]' : 'border-b'
          }`}
        >
          <button
            className="block w-full cursor-pointer"
            onClick={() => scrollTo(menuRef)}
          >
            메뉴 ({storeData.menus.length})
          </button>
        </li>
        <li
          className={`w-full hover:bg-[#ccc] py-3 ${
            isReviewTabActive ? 'bg-[#ccc]' : 'border-b'
          }`}
        >
          <button
            className="block w-full cursor-pointer"
            onClick={() => scrollTo(reviewRef)}
          >
            리뷰 ({reviewData.length})
          </button>
        </li>
      </ul>
      <div className="flex flex-col mx-auto">
        <span className="mt-[30px] mb-[10px] text-4xl ">매장 메뉴</span>
        <div ref={menuRef}></div>
        <MenuTab menuData={storeData.menus} />
        <span className="mt-[30px] mb-[10px] text-4xl ">매장 리뷰</span>
        <div ref={reviewRef}></div>
        <StoreReviewTab reviewData={reviewData} />
      </div>
    </div>
  );
};

export default Store;
