package com.pdq.service;

import com.pdq.dto.request.EditImageRequest;
import com.pdq.dto.request.MergeImageRequest;
import com.pdq.dto.request.SearchImageRequest;
import com.pdq.dto.response.EditImageResponse;
import com.pdq.dto.response.MergeImageResponse;
import com.pdq.dto.response.SearchImageResponse;
import com.pdq.dto.response.UploadImageResponse;
import com.pdq.entity.ImageFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

// 文件服务接口
public interface FileService {

    // 上传图片：先保存到系统，再同步到OSS，并落库files表
    UploadImageResponse uploadImage(MultipartFile file, String authorization);

    // 查询当前用户上传的图片列表
    List<ImageFile> myImages(String authorization);

    // 搜索当前用户上传的图片
    List<SearchImageResponse> search(String authorization, SearchImageRequest searchImageRequest);

    // 编辑用户上传的图片
    EditImageResponse edit(String authorization, EditImageRequest editImageRequest);

    // 合并用户上传的图片
    MergeImageResponse merge(String authorization, MergeImageRequest mergeImageRequest);
}

