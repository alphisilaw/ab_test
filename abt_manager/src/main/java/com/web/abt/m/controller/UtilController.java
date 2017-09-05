package com.web.abt.m.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.zhenai.modules.photo.bean.FileData;
import com.zhenai.modules.photo.utils.UploadPhotoTOServerUtil;

@Controller
public class UtilController extends BaseController {


	@RequestMapping(method = RequestMethod.POST, value = "uploadImg")
	@ResponseBody
	public String uploadImg(MultipartFile picFile) {
		String picUrl = "";
		try {
			UploadPhotoTOServerUtil u = new UploadPhotoTOServerUtil();
			FileData fileData = new FileData();
			String original = picFile.getOriginalFilename();
			byte[] fileDatas = picFile.getBytes();
			fileData.setFileData(fileDatas);
			fileData.setFilename(original);
			String fileName = u.savePhoto("/photo/activity/", fileData);
			picUrl = "http://photo.zhenai.com/photo/activity/" + fileName;
		} catch (Exception e) {
			
		}
		return picUrl;
	}
}
