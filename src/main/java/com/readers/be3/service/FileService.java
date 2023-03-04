package com.readers.be3.service;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.readers.be3.repository.image.ArticleImgRepository;
import com.readers.be3.repository.image.UserImgRepository;
import com.readers.be3.vo.FileDownloadVO;
import com.readers.be3.vo.book.InvalidInputException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FileService {
    private final ArticleImgRepository articleImgRepository;
    private final UserImgRepository userImgRepository;

    @Value("${file.image.article}") String article_img_path;
    @Value("${file.image.user}") String user_img_path;

    public FileDownloadVO downloadImgFile(String type, String uri) {
        Path folderLocation = null;

        if (type.equals("article")) {
            folderLocation = Paths.get(article_img_path);
        }
        else if (type.equals("user")) {
            folderLocation = Paths.get(user_img_path);
        }
        else {
            throw new InvalidInputException("유효하지 않은 경로입니다.");
        }
        
        String filename = "";
        if (type.equals("article")) {
            if (articleImgRepository.findByAimgUriEquals(uri)==null) {
                throw new InvalidInputException("파일이 존재하지 않습니다.");
            }
            else {
                filename = articleImgRepository.findByAimgUriEquals(uri).getAimgFilename();
            }
        }
        else if (type.equals("user")) {
            if (userImgRepository.findTopByUimgUriEquals(uri)==null) {
                throw new InvalidInputException("파일이 존재하지 않습니다.");
            }
            else {
                filename = userImgRepository.findTopByUimgUriEquals(uri).getUimgFilename();
            }
        }

        return new FileDownloadVO(folderLocation, filename);
    }
}
