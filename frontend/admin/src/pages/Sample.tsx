import { Button, Card, Typography } from '@mui/material';
import { CommonText } from '../components/CommonText';
import { PageContainer } from '../components/PageContainer';
import { Grid } from '../components/Grid';
import { ColDef } from 'ag-grid-community';
import { useState } from 'react';
import Popup from '../components/Popup';
import Chart, { ChartData } from '../components/Chart';

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
        <Card variant="outlined" sx={{ marginBottom: 2, display: 'flex', width: '100%' }}>
          <Chart sx={{}} type="line" data={data} title="월별 매출" />
          <Chart type="bar" data={data} title="월별 방문자" />
          <Chart type="pie" data={data} title="비율 분석" />
          <Chart type="scatter" data={data} title="분산 분석" />
        </Card>
      </section>
    </PageContainer>
  );
};

export default Sample;
