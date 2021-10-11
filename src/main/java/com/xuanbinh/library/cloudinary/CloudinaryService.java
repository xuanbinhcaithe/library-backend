package com.xuanbinh.library.cloudinary;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

@Service
public class CloudinaryService {

    Cloudinary cloudinary;
    private Map<String, String> valueMap = new HashMap<>();

    public CloudinaryService() {
        ResourceBundle rb = ResourceBundle.getBundle("config");
        String cloud_name = rb.getString("cloud_name");
        String api_key = rb.getString("api_key");
        String api_secret = rb.getString("api_secret");
        valueMap.put("cloud_name", cloud_name);
        valueMap.put("api_key", api_key);
        valueMap.put("api_secret", api_secret);
        cloudinary = new Cloudinary(valueMap);
    }

    public Map upload(MultipartFile multipartFile) throws IOException {
        File file = convert(multipartFile);
        Map result = cloudinary.uploader().upload(file, ObjectUtils.emptyMap());
        file.delete();
        return result;
    }

    public Map delete(String id) throws IOException {
        Map result = cloudinary.uploader().destroy(id, ObjectUtils.emptyMap());
        return result;
    }

    private File convert(MultipartFile multipartFile) throws IOException {
        File file = new File(multipartFile.getOriginalFilename());
        FileOutputStream fo = new FileOutputStream(file);
        fo.write(multipartFile.getBytes());
        fo.close();
        return file;
    }

}
