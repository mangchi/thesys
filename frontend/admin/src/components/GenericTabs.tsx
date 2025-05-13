import React, { useState } from 'react';
import { Tabs, Tab, Box, SxProps, Theme } from '@mui/material';

interface GenericTabsProps {
  /** 탭 배열: label과 해당 패널에 보여줄 content */
  tabs: Array<{ label: string; content: React.ReactNode }>;
  /** 가로(row)인지 세로(column)인지 */
  orientation?: 'horizontal' | 'vertical';
  /** MUI Tabs의 variant (e.g. "scrollable", "fullWidth") */
  variant?: 'standard' | 'scrollable' | 'fullWidth';
  /** 스타일 오버라이드 */
  sx?: SxProps<Theme>;
}

export function GenericTabs({
  tabs,
  orientation = 'horizontal',
  variant = 'standard',
  sx,
}: GenericTabsProps) {
  const [value, setValue] = useState(0);
  const handleChange = (_: React.SyntheticEvent, newValue: number) => {
    setValue(newValue);
  };

  return (
    <Box sx={{ display: orientation === 'vertical' ? 'flex' : 'block', ...sx }}>
      <Tabs
        orientation={orientation}
        variant={variant}
        value={value}
        onChange={handleChange}
        sx={orientation === 'vertical' ? { borderRight: 1, borderColor: 'divider' } : {}}
        aria-label="generic tabs"
      >
        {tabs.map((tab, idx) => (
          <Tab
            key={idx}
            label={tab.label}
            id={`generic-tab-${idx}`}
            aria-controls={`generic-tabpanel-${idx}`}
          />
        ))}
      </Tabs>
      {tabs.map((tab, idx) => (
        <div
          key={idx}
          role="tabpanel"
          hidden={value !== idx}
          id={`generic-tabpanel-${idx}`}
          aria-labelledby={`generic-tab-${idx}`}
          style={{ width: '100%' }}
        >
          {value === idx && <Box sx={{ p: 2 }}>{tab.content}</Box>}
        </div>
      ))}
    </Box>
  );
}
