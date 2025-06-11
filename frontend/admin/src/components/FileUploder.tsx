import React, { useRef, useState, DragEvent } from 'react';
import { Button, IconButton } from '@mui/material';
import { CloudUpload } from '@mui/icons-material';

interface PreviewFile {
    file: File;
    previewUrl: string;
}

const FileUploader = () => {
    const [files, setFiles] = useState<PreviewFile[]>([]);
    const fileInputRef = useRef<HTMLInputElement | null>(null);

    const handleFiles = (fileList: FileList) => {
        const newFiles = Array.from(fileList).map((file) => ({
            file,
            previewUrl: URL.createObjectURL(file),
        }));
        setFiles((prev) => [...prev, ...newFiles]);
    };

    const handleDrop = (e: DragEvent<HTMLDivElement>) => {
        e.preventDefault();
        e.stopPropagation();
        if (e.dataTransfer.files) {
            handleFiles(e.dataTransfer.files);
        }
    };

    const handleFileChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        if (e.target.files) {
            handleFiles(e.target.files);
        }
    };

    return (
        <div
            className="border-2 border-dashed border-gray-300 rounded-xl p-6 text-center space-y-4 cursor-pointer hover:border-blue-400"
            onClick={() => fileInputRef.current?.click()}
            onDragOver={(e) => e.preventDefault()}
            onDrop={handleDrop}
        >
            <CloudUpload className="mx-auto text-4xl text-gray-400" />
            <p className="text-gray-600">드래그 또는 클릭하여 파일을 업로드하세요</p>

            <input
                type="file"
                multiple
                accept="image/*"
                hidden
                ref={fileInputRef}
                onChange={handleFileChange}
            />

            {files.length > 0 && (
                <div className="grid grid-cols-3 gap-4 mt-4">
                    {files.map(({ file, previewUrl }, idx) => (
                        <div key={idx} className="relative">
                            <img src={previewUrl} alt={file.name} className="rounded-md w-full h-32 object-cover" />
                            <p className="text-xs mt-1 truncate">{file.name}</p>
                        </div>
                    ))}
                </div>
            )}
        </div>
    );
};

export default FileUploader;