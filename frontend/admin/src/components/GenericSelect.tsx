import React from 'react';
import {
  FormControl,
  InputLabel,
  Select,
  MenuItem,
  FormHelperText,
  SelectChangeEvent,
} from '@mui/material';

/**
 * 범용 Select 컴포넌트 props
 */
export interface GenericSelectOption<T> {
  label: string;
  value: T;
  disabled?: boolean;
}

export interface GenericSelectProps<T> {
  /** Select 컴포넌트의 고유 id 및 label 연동 */
  id: string;
  /** 옵션 배열 */
  options: GenericSelectOption<T>[];
  /** 현재 선택된 값 */
  value: T | '';
  /** 값 변경 시 호출되는 콜백 */
  onChange: (value: T) => void;
  /** Select의 표시 레이블 */
  label: string;
  /** 에러 메시지 */
  errorMessage?: string;
  /** 전체 너비 사용 여부 */
  fullWidth?: boolean;
  /** Select 크기 variant (standard, outlined, filled) */
  variant?: 'standard' | 'outlined' | 'filled';
  /** 추가 스타일링 */
  sx?: any;
  /** Select 비활성화 */
  disabled?: boolean;
  /** 다중 선택 여부 */
  multiple?: boolean;
  /** 컴포넌트 크기 */
  size?: 'small' | 'medium';
}

/**
 * 제네릭 기반의 범용 Select 컴포넌트
 */
export function GenericSelect<T extends string | number>({
  id,
  options,
  value,
  onChange,
  label,
  errorMessage,
  fullWidth = false,
  variant = 'outlined',
  sx,
  disabled = false,
  multiple = false,
  size = 'medium',
}: GenericSelectProps<T>) {
  const handleChange = (event: SelectChangeEvent<unknown>) => {
    const val = event.target.value as T;
    onChange(val);
  };

  return (
    <FormControl
      variant={variant}
      fullWidth={fullWidth}
      error={Boolean(errorMessage)}
      sx={sx}
      disabled={disabled}
      size={size}
    >
      <InputLabel id={`${id}-label`}>{label}</InputLabel>
      <Select
        labelId={`${id}-label`}
        id={id}
        multiple={multiple}
        value={value}
        label={label}
        onChange={handleChange}
      >
        <MenuItem value="">
          <em>None</em>
        </MenuItem>
        {options.map((opt) => (
          <MenuItem key={`item-${opt.value}`} value={opt.value} disabled={opt.disabled}>
            {opt.label}
          </MenuItem>
        ))}
      </Select>
      {errorMessage && <FormHelperText>{errorMessage}</FormHelperText>}
    </FormControl>
  );
}

export default GenericSelect;
