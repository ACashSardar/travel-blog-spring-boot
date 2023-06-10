package com.akash.blog.serviceImpl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.akash.blog.service.FileService;

@Service
public class FileServiceImpl implements FileService{

	@Override
	public String uploadImage(String path, MultipartFile file) throws IOException {
		
		// filename
		String name=file.getOriginalFilename();

		String randomId=UUID.randomUUID().toString();
		
		String arr[]=name.split("\\.");
		
		String uniqueName=arr[0]+randomId+"."+arr[1];
		
		// fullpath
		String filepath=path+ File.separator+uniqueName;
		
		// create a folder if not created earlier
		File f=new File(path); // path is the folder path
		if(!f.exists()) {
			f.mkdir();
		}
		
		// file copy
		Files.copy(file.getInputStream(), Paths.get(filepath));

		return uniqueName;
	}

	@Override
	public InputStream getResource(String path, String fileName) throws FileNotFoundException {
		String fullPath=path+File.separator+fileName;
		InputStream is=new FileInputStream(fullPath);
		// db logic to return input stream
		return is;
	}

	@Override
	public boolean deleteImage(String path, String fileName) throws FileNotFoundException {
		String fullPath=path+File.separator+fileName;
		System.out.println(fullPath);
		try {
			 File file = new File(fullPath);
			 file.delete();
			 return true;
		} 
		catch (Exception e) {
			return false;
		}
	}

}
