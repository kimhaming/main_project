const storeData = [
  {
    id: 4,
    store_name: '플러터베이커리',
    region_name: '마포구',
    rating: 4.5,
    is_favorite: true,
    img: 'https://source.unsplash.com/random/?Baguette',
    last_modified_at: '2022-09-05T14:20:30',
    created_at: '2022-09-05T14:20:30',
  },
  {
    id: 5,
    store_name: '도우넛팩토리',
    region_name: '중구',
    rating: 4.2,
    is_favorite: false,
    img: 'https://source.unsplash.com/random/?Croissant',
    last_modified_at: '2022-09-01T09:15:18',
    created_at: '2022-08-20T19:55:02',
  },
  {
    id: 6,
    store_name: '그린티오븐',
    region_name: '강남구',
    rating: 4.8,
    is_favorite: true,
    img: 'https://source.unsplash.com/random/?Sourdough',
    last_modified_at: '2022-09-10T08:35:27',
    created_at: '2022-09-10T08:35:27',
  },
  {
    id: 7,
    store_name: '롤리폴리베이크',
    region_name: '영등포구',
    rating: 4.3,
    is_favorite: false,
    img: 'https://source.unsplash.com/random/?Brioche',
    last_modified_at: '2022-09-02T16:45:55',
    created_at: '2022-08-25T10:12:14',
  },
  {
    id: 8,
    store_name: '베이크웍스',
    region_name: '서대문구',
    rating: 4.6,
    is_favorite: true,
    img: 'https://source.unsplash.com/random/?Ciabatta',
    last_modified_at: '2022-09-07T13:28:40',
    created_at: '2022-09-07T13:28:40',
  },
  {
    id: 9,
    store_name: '샌드위치브라더스',
    region_name: '용산구',
    rating: 4.0,
    is_favorite: false,
    img: 'https://source.unsplash.com/random/?Pita',
    last_modified_at: '2022-09-03T11:55:21',
    created_at: '2022-08-28T15:20:30',
  },
  {
    id: 10,
    store_name: '크로와상파크',
    region_name: '광진구',
    rating: 4.7,
    is_favorite: true,
    img: 'https://source.unsplash.com/random/?Focaccia',
    last_modified_at: '2022-09-09T20:10:08',
    created_at: '2022-09-09T20:10:08',
  },
  {
    id: 11,
    store_name: '베이킹블루',
    region_name: '성동구',
    rating: 4.4,
    is_favorite: false,
    img: 'https://source.unsplash.com/random/?Multigrain',
    last_modified_at: '2022-09-06T12:47:30',
    created_at: '2022-09-01T09:30:15',
  },
  {
    id: 12,
    store_name: '피터팬베이커리',
    region_name: '강동구',
    rating: 4.9,
    is_favorite: true,
    img: 'https://source.unsplash.com/random/?Rye',
    last_modified_at: '2022-09-12T09:18:06',
    created_at: '2022-09-12T09:18:06',
  },
  {
    id: 13,
    store_name: '롤케이크',
    region_name: '성북구',
    rating: 4.3,
    is_favorite: false,
    img: 'https://source.unsplash.com/random/?Whole Wheat',
    last_modified_at: '2022-09-04T15:30:50',
    created_at: '2022-08-30T08:15:42',
  },
  {
    id: 14,
    store_name: '슈가힐',
    region_name: '동작구',
    rating: 4.6,
    is_favorite: true,
    img: 'https://source.unsplash.com/random/?White Bread',
    last_modified_at: '2022-09-08T18:27:22',
    created_at: '2022-09-02T11:09:59',
  },
  {
    id: 15,
    store_name: '바게트마스터',
    region_name: '서초구',
    rating: 4.2,
    is_favorite: false,
    img: 'https://source.unsplash.com/random/?Pumpernickel',
    last_modified_at: '2022-09-03T14:40:12',
    created_at: '2022-08-30T17:55:30',
  },
  {
    id: 16,
    store_name: '크림딜라이트',
    region_name: '강남구',
    rating: 4.8,
    is_favorite: true,
    img: 'https://source.unsplash.com/random/?English Muffin',
    last_modified_at: '2022-09-10T09:20:35',
    created_at: '2022-09-01T13:18:27',
  },
  {
    id: 17,
    store_name: '피치블루베리',
    region_name: '마포구',
    rating: 4.5,
    is_favorite: false,
    img: 'https://source.unsplash.com/random/?Bagel',
    last_modified_at: '2022-09-05T16:15:08',
    created_at: '2022-09-02T20:37:14',
  },
  {
    id: 18,
    store_name: '베이컨메이커',
    region_name: '중구',
    rating: 4.0,
    is_favorite: false,
    img: 'https://source.unsplash.com/random/?Naan',
    last_modified_at: '2022-09-07T11:45:29',
    created_at: '2022-08-31T09:55:21',
  },
  {
    id: 19,
    store_name: '모카휘낭시에',
    region_name: '용산구',
    rating: 4.7,
    is_favorite: true,
    img: 'https://source.unsplash.com/random/?Challah',
    last_modified_at: '2022-09-06T22:10:14',
    created_at: '2022-08-29T14:26:50',
  },
  {
    id: 20,
    store_name: '롤링베이커리',
    region_name: '송파구',
    rating: 4.4,
    is_favorite: true,
    img: 'https://source.unsplash.com/random/?Panettone',
    last_modified_at: '2022-09-09T17:33:40',
    created_at: '2022-08-28T19:47:03',
  },
  {
    id: 21,
    store_name: '그린티오션',
    region_name: '강서구',
    rating: 4.6,
    is_favorite: false,
    img: 'https://source.unsplash.com/random/?Pretzel',
    last_modified_at: '2022-09-02T14:55:19',
    created_at: '2022-09-01T08:05:16',
  },
  {
    id: 22,
    store_name: '프레시베이크',
    region_name: '관악구',
    rating: 4.3,
    is_favorite: false,
    img: 'https://source.unsplash.com/random/?Muffin',
    last_modified_at: '2022-09-03T19:20:50',
    created_at: '2022-08-30T12:40:22',
  },
  {
    id: 23,
    store_name: '호밀빵마스터',
    region_name: '노원구',
    rating: 4.1,
    is_favorite: true,
    img: 'https://source.unsplash.com/random/?Donut',
    last_modified_at: '2022-09-05T10:10:30',
    created_at: '2022-08-31T18:15:11',
  },
];

export default storeData;
