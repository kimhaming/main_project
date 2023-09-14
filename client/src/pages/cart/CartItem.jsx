import { styled } from 'styled-components';
import { Link } from 'react-router-dom';
import CheckBox from '../../components/cart/Checkbox.jsx';
import { useState } from 'react';
import DeleteModal from './SubmitModal.jsx';
import { ReactComponent as Delete } from '../../assets/images/closebutton.svg';
import { useCartItemStore } from '../../store/store.js';
import { useCartApi } from '../../api/cart.js';
import { toast } from 'react-hot-toast';

const ItemCard = styled.li`
  display: flex;
  width: 100%;
  position: relative;
  align-items: center;
  padding: 20px 0;
`;

const ItemImg = styled(Link)`
  display: block;
  width: 80px;
  height: 78px;
  margin-right: 20px;
  // 여기에 Menu 이미지가 오면 될듯?
  background: ${(props) =>
    props.img ||
    `url('https://img-cf.kurly.com/shop/data/goods/1653036991865l0.jpeg')`};
  background-size: cover;
  background-position: center center;
  background-repeat: no-repeat;
`;

const ButtonBox = styled.div`
  display: inline-flex;
  align-items: center;
  width: 88px;
  border: 1px solid rgb(221, 223, 225);
  border-radius: 3px;
  button {
    width: 28px;
    height: 28px;
    border: none;
    background: none;
    font-size: 20px;
    &:disabled {
      color: rgb(156, 163, 175);
    }
  }
  div {
    display: inline-flex;
    font-size: 14px;
    font-weight: 400;
    width: 31px;
    height: 28px;
    line-height: 28px;
    justify-content: center;
  }
`;

const PriceBox = styled.div`
  display: flex;
  flex-direction: column;
  width: 127px;
  text-align: right;
`;

const CartItem = ({ menuName, quantity, price, onChange, checked, id }) => {
  //-, +버튼으로 quantity를 조절하는 함수
  const [amount, setAmount] = useState(quantity);
  const { setCartItem, storeId, setCheckItem } = useCartItemStore();
  const { deleteCart, updateCart, fetchCart, getStock } = useCartApi();

  const quantityUp = async () => {
    const stock = await getStock(storeId, id);
    const updatedAmount = amount + 1;
    console.dir(`현재 재고 : ${stock}`);

    // updatedAmount가 stock보다 크면 경고 메시지를 표시하고 함수를 종료
    if (updatedAmount > stock) {
      toast.error('재고가 부족합니다.', {
        id: 'stock',
        duration: 3000,
      });
      return;
    }
    setAmount(updatedAmount);
    updateQuantity(id, updatedAmount);
  };
  const quantityDown = () => {
    if (amount > 1) {
      const updatedAmount = amount - 1;
      setAmount(updatedAmount);
      updateQuantity(id, updatedAmount);
    }
  };

  const updateQuantity = async (itemId, updatedQuantity) => {
    await updateCart(itemId, updatedQuantity);
  };

  const [isModalOpen, setIsModalOpen] = useState(false); // 모달 상태 추가

  const openModal = () => {
    setIsModalOpen(true);
  };
  const closeModal = () => {
    setIsModalOpen(false);
  };

  const handleRemoveClick = () => {
    openModal();
  };

  const handleDelete = async () => {
    try {
      await deleteCart([id]);
      // 삭제된 내역 업데이트
      const newData = await fetchCart().then((res) => res.order_menus);
      setCartItem(newData);
      setCheckItem(newData.map((item) => item.id));
      console.log(newData);
    } catch (error) {
      console.error('에러임', error);
    } finally {
      closeModal();
    }
  };

  return (
    <ItemCard>
      <CheckBox onChange={onChange} checked={checked} />
      <ItemImg to={`/stores/${storeId}`} />
      <div className="flex-1">
        <Link to={`/stores/${storeId}`}>
          <p>{menuName}</p>
        </Link>
      </div>
      <ButtonBox>
        <button
          type="button"
          aria-label="수량내리기"
          onClick={quantityDown}
          disabled={amount === 1}
        >
          -
        </button>
        <div>{amount}</div>
        <button type="button" aria-label="수량올리기" onClick={quantityUp}>
          +
        </button>
      </ButtonBox>
      <PriceBox>
        <span
          aria-label="판매 가격"
          data-testid="product-price"
          className="font-bold"
        >
          {(price * amount).toLocaleString()}원
        </span>
      </PriceBox>
      <button
        type="button"
        aria-label="삭제하기"
        className="pl-4"
        onClick={handleRemoveClick}
      >
        <Delete />
      </button>
      {isModalOpen && (
        <DeleteModal
          onClose={() => closeModal()}
          onSubmit={handleDelete}
          message={'정말 삭제하시겠습니까?'}
          cancelLabel={'취소'}
          submitLabel={'삭제'}
        />
      )}
    </ItemCard>
  );
};

export default CartItem;
