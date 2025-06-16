import { useCallback, useContext, useMemo } from 'react';
import { AgGridReact } from 'ag-grid-react';
import type { ColDef, GridReadyEvent } from 'ag-grid-community';
import { AllCommunityModule, ModuleRegistry } from 'ag-grid-community';
import { themeAlpine, colorSchemeDarkBlue, RowSelectionOptions } from 'ag-grid-community';
import { ColorModeContext } from '../theme/ThemeContext';

ModuleRegistry.registerModules([AllCommunityModule]);

interface AgGridComponentProps {
  columnDefs: ColDef[];
  rowData: any[];
  className: any;
  height?: number | string;
  onCellClicked?: (event: any) => void;
  onSelectionChanged?: (event: any) => void;
  rowSelectionMode?: 'singleRow' | 'multiRow';
}

export function Grid({
  columnDefs,
  rowData,
  className,
  height = 400,
  onCellClicked,
  onSelectionChanged,
  rowSelectionMode = 'multiRow',
}: AgGridComponentProps) {
  const onGridReady = useCallback((params: GridReadyEvent) => {
    // 컬럼 너비를 컨테이너에 맞춰 자동 조정
    params.api.sizeColumnsToFit();
  }, []);
  const { mode } = useContext(ColorModeContext);
  const themeDarkBlue = themeAlpine.withPart(colorSchemeDarkBlue);
  const gridTheme = (mode === 'light' ? themeAlpine : themeDarkBlue).withParams({
    accentColor: mode ? '#90caf9' : 'red',
  });

  const rowSelection = useMemo<RowSelectionOptions | 'single' | 'multiple'>(() => {
    return {
      mode: rowSelectionMode,
    };
  }, [rowSelectionMode]);

  return (
    <div style={{ width: '100%', height }}>
      <div> </div>
      <AgGridReact
        className={className}
        theme={gridTheme}
        columnDefs={columnDefs}
        rowData={rowData}
        defaultColDef={{
          sortable: true,
          filter: true,
          resizable: true,
        }}
        onGridReady={onGridReady}
        onCellClicked={onCellClicked}
        rowSelection={rowSelection}
        onSelectionChanged={onSelectionChanged}
      />
    </div>
  );
}
