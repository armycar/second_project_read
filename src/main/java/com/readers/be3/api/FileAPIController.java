package com.readers.be3.api;

import java.net.URLEncoder;
import java.nio.file.Path;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.readers.be3.service.FileService;
import com.readers.be3.utilities.ResponseMessageUtils;
import com.readers.be3.vo.book.InvalidInputException;
import com.readers.be3.vo.response.BasicResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Tag(name = "파일 다운로드", description = "이미지 파일 다운로드")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class FileAPIController {
    private final FileService fileService;

    @Operation(summary = "파일 다운로드", description = "type에 업로드된 uri 파일을 다운로드합니다.",
        responses = {
            @ApiResponse(responseCode = "200", description = ResponseMessageUtils.TRUE),
            @ApiResponse(responseCode = "400", description = ResponseMessageUtils.FALSE,
                content = @Content(schema = @Schema(implementation = BasicResponse.class)))
        })
    @GetMapping("/download/{type}/{uri}")
    public ResponseEntity<Resource> downloadImgFile(
        @Parameter(description = "업로드 된 곳(user : 유저정보, article : 게시글)", example = "user") @PathVariable String type,
        @Parameter(description = "파일 uri", example = "15411016347037151532") @PathVariable String uri,
        HttpServletRequest request
    ) throws Exception {
        String filename = fileService.downloadImgFile(type, uri).getFilename();
        Path folderLocation = fileService.downloadImgFile(type, uri).getFolderLocation();
        String[] split = filename.split("\\.");
        String ext = split[split.length - 1];
        String exportName = uri + "." + ext;
        Path targetFile = folderLocation.resolve(filename);

        Resource r = null;
        try {
            r = new UrlResource(targetFile.toUri());
        }
        catch (Exception e) {
            throw new InvalidInputException("파일 다운로드 실패");
        }

        String contentType = null;
        try {
            contentType = request.getServletContext().getMimeType(r.getFile().getAbsolutePath());
            if (contentType==null) {
                contentType = "application/octet-stream";
            }
        }
        catch (Exception e) {
            throw new InvalidInputException("파일 다운로드 실패");
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename*=\"" + URLEncoder.encode(exportName, "UTF-8") + "\"")
                .body(r);
    }
}
