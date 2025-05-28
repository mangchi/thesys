import { Box, Button, Divider, Stack, TextField } from '@mui/material';
import { CommonText } from '../components/CommonText';
import { Grid } from '../components/Grid';
import { PageContainer } from '../components/PageContainer';
import { ColDef } from 'ag-grid-community';
import { useState } from 'react';
import GenericSelect from '../components/GenericSelect';

interface FAQ {
  question: string;
  answer: string;
  date: string; // ISO format date string
  type: '일반' | '계정' | '보안' | '기타';
}

const initialFAQs: FAQ[] = [
  {
    question: '이 서비스는 무엇인가요?',
    answer: '이 서비스는 사용자들이 자주 묻는 질문에 대한 답변을 제공합니다.',
    date: '2025-05-01',
    type: '일반',
  },
  {
    question: '어떻게 계정을 생성하나요?',
    answer: '계정을 생성하려면 회원가입 페이지로 이동하여 필요한 정보를 입력하세요.',
    date: '2025-05-02',
    type: '계정',
  },
  {
    question: '비밀번호를 잊어버렸어요. 어떻게 해야 하나요?',
    answer: '로그인 페이지에서 "비밀번호 찾기" 링크를 클릭하여 지침을 따르세요.',
    date: '2025-05-03',
    type: '보안',
  },
  {
    question: '이 서비스는 무료인가요?',
    answer: '네, 이 서비스는 무료로 제공됩니다.',
    date: '2025-05-04',
    type: '기타',
  },
];

const columnDefs: ColDef[] = [
  { field: 'type', headerName: '질문 유형', flex: 1 },
  { field: 'question', headerName: '질문', flex: 1 },
  { field: 'answer', headerName: '답변', flex: 1 },
  { field: 'date', headerName: '작성일', flex: 1 },
];

const faqtypeOptions = [
  { label: '일반', value: '일반' },
  { label: '계정', value: '계정' },
  { label: '보안', value: '보안' },
  { label: '기타', value: '기타' },
];

const FAQ = () => {
  const [query, setQuery] = useState('');
  const [selectedType, setSelectedType] = useState<string>('');

  const handleSearch = (e: React.FormEvent) => {
    e.preventDefault(); // 폼 제출 시 리로드 방지
  };

  return (
    <PageContainer>
      <CommonText variantType="title">FAQ</CommonText>

      {/* 검색 폼 */}
      <Box
        component="form"
        onSubmit={handleSearch}
        sx={{ border: '1px solid #ccc', p: 1, mt: 2, mb: 2, width: '100%' }}
      >
        <Stack direction="row" spacing={2}>
          <GenericSelect
            sx={{ width: 200 }}
            size="small"
            id="faq-type-select"
            label="질문 유형"
            options={faqtypeOptions}
            value={selectedType}
            onChange={(value) => setSelectedType(value as string)}
          />
          <TextField
            // sx={{ width: 900 }}
            label="질문"
            variant="outlined"
            size="small"
            value={query}
            onChange={(e) => setQuery(e.target.value)}
            fullWidth={true}
          />

          <Button type="submit" variant="contained" sx={{ whiteSpace: 'nowrap' }}>
            검색
          </Button>
        </Stack>
      </Box>
      <Grid className="w-[100%]" columnDefs={columnDefs} rowData={initialFAQs}></Grid>
    </PageContainer>
  );
};

export default FAQ;
