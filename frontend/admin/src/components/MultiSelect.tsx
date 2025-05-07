import React from 'react';
import {
  FormControl,
  InputLabel,
  Select,
  MenuItem,
  Chip,
  Box,
  FormHelperText,
  SelectChangeEvent,
  Checkbox,
  ListItemText,
} from '@mui/material';
import type { SxProps } from '@mui/system';

export interface MultiSelectOption<T> {
  label: string;
  value: T;
  disabled?: boolean;
}

export interface MultiSelectProps<T extends string | number> {
  /** Select 고유 ID */
  id: string;
  /** 옵션 리스트 */
  options: MultiSelectOption<T>[];
  /** 선택된 값 배열 */
  value: T[];
  /** 값 변경 핸들러 */
  onChange: (value: T[]) => void;
  /** 라벨 텍스트 */
  label: string;
  /** 에러 메시지 */
  errorMessage?: string;
  /** fullWidth 여부 */
  fullWidth?: boolean;
  /** variant 타입 */
  variant?: 'standard' | 'outlined' | 'filled';
  /** 컴포넌트 비활성화 */
  disabled?: boolean;
  /** 추가 스타일링 */
  sx?: SxProps;
  /** 표시 모드: 기본(default), 체크박스(checkbox), 칩(chip) */
  displayMode?: DisplayMode;
}
export type DisplayMode = 'default' | 'checkmarks' | 'chip';

export function MultiSelect<T extends string | number>({
  id,
  options,
  value,
  onChange,
  label,
  errorMessage,
  fullWidth = false,
  variant = 'outlined',
  disabled = false,
  sx,
  displayMode = 'default',
}: MultiSelectProps<T>) {
  const handleChange = (event: SelectChangeEvent<T[]>) => {
    const selected = event.target.value as T[];
    onChange(selected);
  };

  const renderValue =
    displayMode === 'chip'
      ? (selected: React.ReactNode) => (
          <Box sx={{ display: 'flex', flexWrap: 'wrap', gap: 0.5 }}>
            {(selected as T[]).map((val) => {
              const opt = options.find((o) => o.value === val);
              return <Chip key={val} label={opt?.label ?? val} />;
            })}
          </Box>
        )
      : (selected: T[]) => selected.join(', ');

  return (
    <FormControl
      variant={variant}
      fullWidth={fullWidth}
      error={Boolean(errorMessage)}
      disabled={disabled}
      sx={sx}
    >
      <InputLabel id={`${id}-label`}>{label}</InputLabel>
      <Select
        labelId={`${id}-label`}
        id={id}
        multiple
        value={value}
        label={label}
        onChange={handleChange}
        renderValue={renderValue}
      >
        {options.map((opt) => {
          const isSelected = value.includes(opt.value);
          return (
            <MenuItem key={opt.value} value={opt.value} disabled={opt.disabled}>
              {displayMode === 'checkmarks' && <Checkbox checked={isSelected} />}
              <ListItemText primary={opt.label} />
            </MenuItem>
          );
        })}
      </Select>
      {errorMessage && <FormHelperText>{errorMessage}</FormHelperText>}
    </FormControl>
  );
}

export default MultiSelect;
