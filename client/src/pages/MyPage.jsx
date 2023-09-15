import { useEffect, useState } from 'react';
import Button from '../assets/buttons/Button.jsx';
import { styled } from 'styled-components';
import Reviews from '../components/myPage/Reviews.jsx';
import Orders from '../components/myPage/Orders.jsx';
import Favorites from '../components/myPage/Favorites.jsx';
import axios from 'axios';
import { useAuthStore } from '../store/store.js';

import { Link, useNavigate } from 'react-router-dom';
import EditProfile from '../components/myPage/EditProfile.jsx';
import formatDate from '../utils/formatDate.js';
import ImageUploadModal from '../components/myPage/ImageUploadModal.jsx';
import InfiniteScroll from 'react-infinite-scroll-component';
import LoadingSpinner from '../components/Loading.jsx';

const TabContainer = styled.ul`
  display: flex;
  justify-content: space-between;
  text-align: center;
  border-bottom: 2px solid #ccc;

  > li {
    display: flex;
    border-right: 2px solid #ccc;
    height: 3rem;
    align-items: center;
    cursor: pointer;

    &.active {
      background-color: #ddd;
    }

    &:hover {
      background-color: #ccc;
    }
  }

  > li > a {
    display: block;
    width: 100%;
  }

  > li:last-child {
    border-right: none;
  }
`;

const MyPage = () => {
  const [currentTab, setCurrentTab] = useState('리뷰 관리');
  const { isLoggedIn, accessToken } = useAuthStore((state) => state);
  const [member, setMember] = useState([]);

  const [reviewCount, setReviewCount] = useState(0);
  const [orderCount, setOrderCount] = useState(0);
  const [favoriteCount, setFavoriteCount] = useState(0);

  const [isEditProfileModalOpen, setEditProfileModalOpen] = useState(false);
  const [isImageModalOpen, setImageModalOpen] = useState(false);

  const [page, setPage] = useState(1);
  const [data, setData] = useState([]);

  const navigate = useNavigate();

  useEffect(() => {
    if (!isLoggedIn || !accessToken) {
      navigate('/');
    }
  }, [accessToken, isLoggedIn, navigate]);

  // 데이터 가져오기
  useEffect(() => {
    // 기존 탭 데이터 초기화
    setData([]);
    setPage(1);

    // 각 탭 갯수 가져오기
    const fetchInitialData = async () => {
      const tabs = ['리뷰 관리', '주문 내역', '즐겨찾기'];
      for (const tab of tabs) {
        let apiUrl = '';
        if (tab === '리뷰 관리') {
          apiUrl = `${process.env.REACT_APP_API_URL}/api/reviews`;
        }
        if (tab === '주문 내역') {
          apiUrl = `${process.env.REACT_APP_API_URL}/api/members/orders`;
        }
        if (tab === '즐겨찾기') {
          apiUrl = `${process.env.REACT_APP_API_URL}/api/members/favorites`;
        }

        try {
          const response = await axios.get(apiUrl, {
            headers: {
              Authorization: `Bearer ${accessToken}`,
            },
            params: {
              page: 1,
              size: 1, // 최소한의 데이터만 가져옴
            },
          });

          const total_elements = response.data.pageInfo.total_elements;
          if (tab === '리뷰 관리') {
            setReviewCount(total_elements);
          }
          if (tab === '주문 내역') {
            setOrderCount(total_elements);
          }
          if (tab === '즐겨찾기') {
            setFavoriteCount(total_elements);
          }
        } catch (error) {
          console.error(`[${tab}] 데이터를 가져오는데 실패함: `, error);
        }
      }
    };

    // 회원 정보 가져오기
    const getMember = async () => {
      try {
        const response = await axios.get(
          `${process.env.REACT_APP_API_URL}/api/member`,
          {
            headers: {
              Authorization: `Bearer ${accessToken}`,
            },
          },
        );
        setMember(response.data);
      } catch (error) {
        console.error(error);
      }
    };

    // 데이터 가져오기
    const fetchData = async () => {
      // URL 초기화
      let apiUrl = '';
      // 데이터 키 초기화
      let dataKey = '';

      // 현재 탭에 따라 URL 및 데이터 키 변경
      if (currentTab === '리뷰 관리') {
        apiUrl = `${process.env.REACT_APP_API_URL}/api/reviews`;
        dataKey = 'reviews';
      }
      if (currentTab === '주문 내역') {
        apiUrl = `${process.env.REACT_APP_API_URL}/api/members/orders`;
        dataKey = 'orders';
      }
      if (currentTab === '즐겨찾기') {
        apiUrl = `${process.env.REACT_APP_API_URL}/api/members/favorites`;
        dataKey = 'stores';
      }

      // 데이터 가져오기
      try {
        const response = await axios.get(apiUrl, {
          headers: {
            Authorization: `Bearer ${accessToken}`,
          },
          params: {
            page: page,
            size: 5,
          },
        });

        // 데이터가 있으면 기존 데이터에 추가
        setData((prevData) => [...prevData, ...response.data[dataKey]]);

        // 각 요소 수량 설정
        if (currentTab === '리뷰 관리') {
          setReviewCount(response.data.pageInfo.total_elements);
        }
        if (currentTab === '주문 내역') {
          setOrderCount(response.data.pageInfo.total_elements);
        }
        if (currentTab === '즐겨찾기') {
          setFavoriteCount(response.data.pageInfo.total_elements);
        }
      } catch (error) {
        console.error('갯수 데이터 에러', error);
      }
    };

    // 로그인 상태이고 accessToken이 있으면 데이터 가져오기
    if (isLoggedIn && accessToken) {
      fetchInitialData();
      getMember();
      fetchData();
    } else {
      // 로그인 상태가 아니면 메인 페이지로 이동
      alert('로그인이 필요합니다.');
      navigate('/');
    }
  }, [accessToken, currentTab, isLoggedIn, navigate, page]);

  // 각 탭에 따라 렌더링할 컴포넌트 변경
  const renderDataComponent = () => {
    if (currentTab === '리뷰 관리') {
      return <Reviews />;
    } else if (currentTab === '주문 내역') {
      return <Orders />;
    } else if (currentTab === '즐겨찾기') {
      return <Favorites />;
    }
  };

  // 모달 관련 함수
  const openEditProfileModal = () => {
    setEditProfileModalOpen(true);
  };
  const closeEditProfileModal = () => {
    setEditProfileModalOpen(false);
  };
  const openImageModal = () => {
    setImageModalOpen(true);
  };
  const closeImageModal = () => {
    setImageModalOpen(false);
  };

  // 해시값 인식 후 초기 렌더링 시 해시 값에 따라 탭 변경
  useEffect(() => {
    const hash = window.location.hash;
    if (hash === '#review') setCurrentTab('리뷰 관리');
    if (hash === '#order') setCurrentTab('주문 내역');
    if (hash === '#favorite') setCurrentTab('즐겨찾기');
  }, []);

  return (
    <div className="max-w-screen-lg mx-auto p-10">
      <div className="flex gap-5">
        <img
          src={member.img}
          alt="유저 이미지"
          className="flex justify-center items-center border-2 w-28 rounded-full"
        ></img>
        <div className="flex flex-col justify-center gap-2">
          <h1 className="">{member.nickname}</h1>
          <div>가입일: {formatDate(member.createdAt)}</div>
          <div className="flex gap-2">
            <Button className="" onClick={openImageModal}>
              이미지 변경
            </Button>
            <Button className="" onClick={openEditProfileModal}>
              프로필 수정
            </Button>
          </div>
        </div>
      </div>
      <TabContainer className="my-5">
        {/* 리뷰 관리 탭 */}
        <li className={`w-full ${currentTab === '리뷰 관리' ? 'active' : ''}`}>
          <Link to="#review" onClick={() => setCurrentTab('리뷰 관리')}>
            리뷰 관리 ({reviewCount})
          </Link>
        </li>
        {/* 주문 내역 탭 */}
        <li className={`w-full ${currentTab === '주문 내역' ? 'active' : ''}`}>
          <Link to="#order" onClick={() => setCurrentTab('주문 내역')}>
            주문 내역 ({orderCount})
          </Link>
        </li>
        {/* 즐겨찾기 탭 */}
        <li className={`w-full ${currentTab === '즐겨찾기' ? 'active' : ''}`}>
          <Link to="#favorite" onClick={() => setCurrentTab('즐겨찾기')}>
            즐겨찾기 ({favoriteCount})
          </Link>
        </li>
      </TabContainer>
      <div className="flex justify-center">{renderDataComponent()}</div>
      {isEditProfileModalOpen && (
        <EditProfile onClose={closeEditProfileModal} />
      )}
      {isImageModalOpen && <ImageUploadModal onClose={closeImageModal} />}
    </div>
  );
};

export default MyPage;
