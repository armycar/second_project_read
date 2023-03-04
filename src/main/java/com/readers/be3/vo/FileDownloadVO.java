package com.readers.be3.vo;

import java.nio.file.Path;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FileDownloadVO {
    Path folderLocation; 
    String filename;
}
