import { Box, Button, Card, Typography } from '@mui/material';
import { CommonText } from '../components/CommonText';
import { PageContainer } from '../components/PageContainer';
import { Grid } from '../components/Grid';
import { ColDef } from 'ag-grid-community';
import { useState } from 'react';
import Popup from '../components/Popup';
import Chart, { ChartData } from '../components/Chart';
import GenericSelect, { GenericSelectOption } from '../components/GenericSelect';
import MultiSelect, { MultiSelectOption } from '../components/MultiSelect';

const Sample = () => {
  interface User {
    id: number;
    name: string;
    age: number;
    email: string;
    country: string;
  }

  const initialUsers: User[] = [
    { id: 1, name: '홍길동', age: 30, email: 'hong@example.com', country: 'Korea' },
    { id: 2, name: 'Jane Doe', age: 25, email: 'jane@sample.com', country: 'USA' },
    { id: 3, name: '김철수', age: 28, email: 'kim@demo.com', country: 'Korea' },
    { id: 4, name: 'John Smith', age: 35, email: 'john@site.com', country: 'Canada' },
    { id: 5, name: 'Alice Brown', age: 22, email: 'alice@brown.com', country: 'UK' },
    { id: 6, name: '박영희', age: 27, email: 'park@younghee.com', country: 'Korea' },
    { id: 7, name: 'Michael Johnson', age: 40, email: 'michael@johnson.com', country: 'USA' },
    { id: 8, name: '이민호', age: 33, email: 'lee@minho.com', country: 'Korea' },
    { id: 9, name: 'Sophia Lee', age: 29, email: 'sophia@lee.com', country: 'Australia' },
    { id: 10, name: 'David Kim', age: 31, email: 'david@kim.com', country: 'Canada' },
    { id: 11, name: 'Emma Wilson', age: 26, email: 'emma@wilson.com', country: 'UK' },
    { id: 12, name: '최지훈', age: 24, email: 'choi@jihoon.com', country: 'Korea' },
    { id: 13, name: 'Olivia Martinez', age: 34, email: 'olivia@martinez.com', country: 'Spain' },
    { id: 14, name: '김영수', age: 37, email: 'kim@youngsoo.com', country: 'Korea' },
  ];

  const columnDefs: ColDef[] = [
    { field: 'id', headerName: 'ID', width: 90 },
    { field: 'name', headerName: '이름', flex: 1 },
    { field: 'age', headerName: '나이', filter: 'agNumberColumnFilter', width: 100 },
    { field: 'email', headerName: '이메일', flex: 1 },
    { field: 'country', headerName: '국가', flex: 1 },
  ];

  const data: ChartData[] = [
    { x: 'Jan', y: 400 },
    { x: 'Feb', y: 600 },
  ];

  const [open, setOpen] = useState(false);

  //Select
  type Fruit = 'apple' | 'banana' | 'cherry';
  type Color = 'red' | 'green' | 'blue';
  type Location = 'home' | 'office' | 'school' | 'park' | 'mall';
  type Country =
    | 'Korea'
    | 'USA'
    | 'Canada'
    | 'UK'
    | 'Australia'
    | 'Spain'
    | 'Japan'
    | 'China'
    | 'Germany'
    | 'France';
  type Gender = 'male' | 'female' | 'other';
  type Food = 'pizza' | 'burger' | 'sushi' | 'pasta' | 'salad';

  const fruitOptions: GenericSelectOption<Fruit>[] = [
    { label: '사과', value: 'apple' },
    { label: '바나나', value: 'banana' },
    { label: '체리', value: 'cherry' },
  ];

  const colorOptions: GenericSelectOption<Color>[] = [
    { label: '빨강', value: 'red' },
    { label: '초록', value: 'green' },
    { label: '파랑', value: 'blue', disabled: false },
  ];

  const locationOptions: GenericSelectOption<Location>[] = [
    { label: '집', value: 'home' },
    { label: '사무실', value: 'office' },
    { label: '학교', value: 'school' },
    { label: '공원', value: 'park' },
    { label: '쇼핑몰', value: 'mall' },
  ];

  const countryOptions: MultiSelectOption<Country>[] = [
    { label: '한국', value: 'Korea' },
    { label: '미국', value: 'USA' },
    { label: '캐나다', value: 'Canada' },
    { label: '영국', value: 'UK' },
    { label: '호주', value: 'Australia' },
    { label: '스페인', value: 'Spain' },
    { label: '일본', value: 'Japan' },
    { label: '중국', value: 'China' },
    { label: '독일', value: 'Germany' },
    { label: '프랑스', value: 'France' },
  ];

  const genderOptions: MultiSelectOption<Gender>[] = [
    { label: '남성', value: 'male' },
    { label: '여성', value: 'female' },
    { label: '기타', value: 'other' },
  ];

  const foodOptions: MultiSelectOption<Food>[] = [
    { label: '피자', value: 'pizza' },
    { label: '버거', value: 'burger' },
    { label: '스시', value: 'sushi' },
    { label: '파스타', value: 'pasta' },
    { label: '샐러드', value: 'salad' },
  ];

  const [selectedLocations, setSelectedLocations] = useState<Location | ''>('');
  const [selectedCountrys, setSelectedCountrys] = useState<Country[]>([]);
  const [selectedFruits, setSelectedFruits] = useState<Fruit | ''>('');
  const [selectedColors, setSelectedColors] = useState<Color | ''>('');
  const [selectedGenders, setSelectedGenders] = useState<Gender[]>([]);
  const [selectedFoods, setSelectedFoods] = useState<Food[]>([]);
  const [error, setError] = useState('');

  const handleSubmit = () => {
    if (!selectedFruits || !selectedColors) {
      setError('모두 선택해야 합니다.');
      return;
    }
    setError('');
    console.log('선택된 과일:', selectedFruits);
    console.log('선택된 색상:', selectedColors);
    // 이곳에 실제 제출 로직을 넣으면 됩니다.
  };

  return (
    <PageContainer>
      <CommonText variantType="title"> Sample</CommonText>
      <section>
        {/* CommonText */}
        <CommonText variantType="subtitle"> Text</CommonText>
        <Card variant="outlined" sx={{ marginBottom: 2 }}>
          <CommonText variantType="title"> title</CommonText>
          <CommonText variantType="subtitle"> subtitle</CommonText>
          <CommonText variantType="body"> body</CommonText>
          <CommonText variantType="caption"> caption</CommonText>
        </Card>
        {/* Select */}
        <CommonText variantType="subtitle" sx={{ display: 'flex' }}>
          Select
        </CommonText>
        <Card variant="outlined" sx={{ marginBottom: 2, width: '100%' }}>
          <CommonText variantType="body" sx={{ marginBottom: 2 }}>
            GenericSelect
          </CommonText>
          <div className="my-2 flex">
            <GenericSelect
              sx={{ maxWidth: 200 }}
              id="fruit-select"
              label="위치 선택"
              options={locationOptions}
              value={selectedLocations}
              onChange={(value) => setSelectedLocations(value)}
              fullWidth={true}
            />
            <p className="mx-4"></p>
            <GenericSelect
              sx={{ maxWidth: 200 }}
              id="fruit-select"
              label="과일 선택"
              options={fruitOptions}
              value={selectedFruits}
              onChange={(value) => setSelectedFruits(value as Fruit | '')}
              errorMessage={!selectedFruits ? '과일을 선택해주세요.' : undefined}
              fullWidth={true}
            />
            <p className="mx-4"></p>
            <GenericSelect
              sx={{ maxWidth: 200 }}
              id="color-select"
              label="색상 선택"
              options={colorOptions}
              value={selectedColors}
              onChange={(value) => setSelectedColors(value)}
              errorMessage={selectedColors.length === 0 ? '색상을 선택해주세요.' : undefined}
              variant="filled"
              fullWidth={true}
            />

            {error && (
              <Typography variant="body2" color="error" textAlign="center">
                {error}
              </Typography>
            )}

            {/* <Button
              sx={{ maxWidth: '120px' }}
              size="small"
              variant="contained"
              color="primary"
              onClick={handleSubmit}
            >
              제출하기
            </Button> */}
          </div>

          <CommonText variantType="body" sx={{ marginBottom: 2 }}>
            MultSelect
          </CommonText>
          <div className="flex">
            <MultiSelect
              sx={{ width: 200 }}
              id="country-multiple-select"
              label="나라 선택 (다중)"
              options={countryOptions}
              value={selectedCountrys}
              onChange={(value) => setSelectedCountrys((value as Country[]) || [])}
              errorMessage={selectedCountrys.length === 0 ? '하나 이상 선택해주세요.' : undefined}
            />

            {/* <Typography variant="body1">
              선택된 과일: {selectedCountrys.length > 0 ? selectedCountrys.join(', ') : '없음'}
            </Typography> */}

            {/* <Button
              variant="contained"
              color="primary"
              onClick={() => alert(`선택된 과일: ${selectedCountrys.join(', ')}`)}
              disabled={selectedCountrys.length === 0}
            >
              제출
            </Button> */}
            <p className="mx-4"></p>
            <MultiSelect
              displayMode="checkmarks"
              sx={{ width: 200 }}
              id="gender-multiple-select"
              label="성별 선택 (다중)"
              options={genderOptions}
              value={selectedGenders}
              onChange={(value) => setSelectedGenders((value as Gender[]) || [])}
            // errorMessage={selectedFruits.length === 0 ? '하나 이상 선택해주세요.' : undefined}
            />
            <p className="mx-4"></p>
            <MultiSelect
              displayMode="chip"
              sx={{ width: 400 }}
              id="food-multiple-select"
              label="음식 선택 (다중)"
              options={foodOptions}
              value={selectedFoods}
              onChange={(value) => setSelectedFoods((value as Food[]) || [])}
            />
          </div>
        </Card>

        {/* Grid */}
        <CommonText variantType="subtitle"> Grid</CommonText>
        <Card variant="outlined" sx={{ marginBottom: 2 }}>
          <Grid className="w-[100%]" columnDefs={columnDefs} rowData={initialUsers}></Grid>
        </Card>

        {/* Popup */}
        <CommonText variantType="subtitle"> Popup</CommonText>
        <Card variant="outlined" sx={{ marginBottom: 2 }}>
          <Button variant="contained" color="primary" onClick={() => setOpen(true)}>
            open
          </Button>
          <Popup
            open={open}
            onClose={() => setOpen(false)}
            title="팝업 제목"
            content="여기에 팝업 내용을 작성하세요. 예시로 안내 메시지를 보여줍니다."
            confirmText="확인"
            cancelText="취소"
            onConfirm={() => console.log('확인 버튼 클릭')}
          />
        </Card>
        {/* Chart */}
        <CommonText variantType="subtitle" sx={{ display: 'flex' }}>
          Chart
        </CommonText>
        {/* <Card variant="outlined" sx={{ marginBottom: 2, display: 'flex', width: '100%' }}>
          <Chart sx={{}} type="line" data={data} title="월별 매출" />
          <Chart type="bar" data={data} title="월별 방문자" />
          <Chart type="pie" data={data} title="비율 분석" />
          <Chart type="scatter" data={data} title="분산 분석" />
        </Card> */}
      </section>
    </PageContainer>
  );
};

export default Sample;
