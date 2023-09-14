import React, { useState } from 'react';
import { styled } from 'styled-components';
import axios from 'axios';
import { useAuthStore, useCartItemStore } from '../../store/store';
import toast from 'react-hot-toast';
import FalseModal from './modal/FalseModal.jsx';
import { useCartApi } from '../../api/cart';

const ModalBg = styled.div`
  position: fixed;
  inset: 0;
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1100;
  background-color: rgba(76, 76, 76, 0.5);
`;

const StyledImage = styled.img`
  width: 300px;
  height: 200px;
  object-fit: cover;
  border-radius: 8px;
  transition: transform 0.3s ease;

  &:hover {
    transform: scale(1.1);
  }
`;

const MenuItem = ({ data }) => {
  const apiUrl = process.env.REACT_APP_API_URL;
  const [isMenuModalOpen, setIsMenuModalOpen] = useState(false);
  const [isFalseModalOpen, setIsFalseModalOpen] = useState(false);
  const [isCount, setIsCount] = useState(1);
  const { isLoggedIn, accessToken } = useAuthStore((state) => state);
  const { fetchCart } = useCartApi();
  const { setCartItem, setCheckItem } = useCartItemStore((state) => state);
  const [currentData, setCurrentData] = useState(null);
  const [currentCount, setCurrentCount] = useState(1);

  const notify = () => toast.error('제품이 품절 되었습니다.');
  const notifysuccess = () => toast.success('장바구니에 추가 되었습니다.');

  const menuModalClose = () => {
    setIsMenuModalOpen(false); // 메뉴모달 닫기
  };
  const menuModalOpen = () => {
    setIsMenuModalOpen(true); // 메뉴모달 열기
  };

  const openFalseModal = (data, count) => {
    setCurrentData(data);
    setCurrentCount(count);
    setIsFalseModalOpen(true);
  };
  const closeFalseModal = () => {
    setIsFalseModalOpen(false);
  };
  // 전부 닫기
  const allClose = () => {
    setIsFalseModalOpen(false);
    setIsMenuModalOpen(false);
  };

  const addToCart = async () => {
    const cartItem = { quantity: isCount };
    try {
      const response = await axios.post(
        `${apiUrl}/api/cart/${data.id}?quantity=${cartItem.quantity}`,
        null,
        {
          headers: {
            Authorization: `Bearer ${accessToken}`,
          },
        },
      );
      const statusData = response.status;
      const exception = response.headers['Bbangbbang_exception'];
      // console.log(exception);
      if (statusData === 200) {
        notifysuccess();
        setIsMenuModalOpen(false); // 모달을 닫도록 수정
      }
    } catch (error) {
      console.log(error);
      openFalseModal(data, isCount);
    } finally {
      const newData = await fetchCart().then((res) => res.order_menus);
      setCartItem(newData);
      // 기존에 있던 checkItem에 새로운 데이터의 id를 추가
      setCheckItem([...new Set([...newData.map((item) => item.id)])]);
    }
  };

  if (!isLoggedIn || !accessToken) {
    alert('로그인이 필요합니다.');
    return null; // null을 반환하여 아무것도 렌더링되지 않도록 수정
  }

  return (
    <div className="flex p-[10px] border-b">
      <div className="xl:w-[750px]">
        <h3 className="xl:text-[25px]">{data.menu_name}</h3>
        <div className="xl:text-[15px]">{data.menu_desc}</div>
      </div>
      <div>
        <div
          onClick={() => {
            if (data.stock > 0) {
              menuModalOpen();
            } else {
              notify();
            }
          }}
          className="cursor-pointer mb-2 overflow-hidden rounded-lg"
        >
          <StyledImage src={data.img} alt="메뉴 이미지" />
        </div>
        <div className="flex xl:space-x-36">
          <div>{data.price.toLocaleString()}원</div>
          <div>남은 수량: {data.stock}</div>
        </div>
      </div>
      {isMenuModalOpen && (
        <ModalBg
          onClick={(e) => {
            if (e.target === e.currentTarget) menuModalClose();
          }}
        >
          <div className="relative bg-white w-[500px] h-[350px] p-4 rounded-lg shadow-lg">
            <button
              className="absolute top-2 right-2 p-4 text-gray-600 hover:text-gray-800"
              onClick={menuModalClose}
            >
              닫기
            </button>
            <div className="p-3">
              <h2 className="mb-1">{data.menu_name}</h2>
              <p className="w-[450px] h-[100px] py-2 px-2 mb-2 border">
                {data.menu_desc}
              </p>
              <div>메뉴 가격 : {data.price.toLocaleString()}원</div>
              <div className="py-1">남은 수량 : {data.stock}</div>
              <div className="flex py-1 border-b pb-2">
                <span className="mr-1 pt-[5px]">주문 수량 </span>
                <button
                  className="w-[32px] border rounded-lg pt-1"
                  onClick={() => {
                    if (isCount > 1) setIsCount(isCount - 1);
                  }}
                >
                  -
                </button>
                <div className="w-[32px] pt-1 text-center">{isCount}</div>
                <button
                  className="w-[32px] border rounded-lg pt-1"
                  onClick={() => {
                    if (isCount < data.stock) setIsCount(isCount + 1);
                  }}
                >
                  +
                </button>
              </div>
              <div className="flex py-2 justify-end">
                <span>총 결제 금액 :</span>
                <div className="text-right w-[70px]">
                  {(isCount * data.price).toLocaleString()}원
                </div>
              </div>
            </div>
            <button
              className="absolute right-[200px] bottom-3 cursor-pointer"
              onClick={addToCart}
            >
              장바구니에 담기
            </button>
          </div>
          {isFalseModalOpen && (
            <FalseModal
              closeFalseModal={closeFalseModal}
              dataId={currentData.id}
              quantity={currentCount}
              allClose={allClose}
            />
          )}
        </ModalBg>
      )}
    </div>
  );
};

const MenuTab = ({ menuData }) => {
  return (
    <div className="flex flex-col">
      {menuData.map((menu) => (
        <MenuItem key={menu.id} data={menu} />
      ))}
    </div>
  );
};

export default MenuTab;
