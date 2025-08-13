package dz.irdcfb.dgdn.mf.gouv.service;

import java.io.IOException;
import java.util.List;

import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import dz.irdcfb.dgdn.mf.gouv.entities.MailArrive;

public interface MailArriveService {

	List<MailArrive> getAllMail();

	MailArrive saveMail(MailArrive mail, MultipartFile[] multipartFile, Model model) throws IOException;

	ResponseEntity<Resource> downloadMail(String fileName) throws IOException;

	// Integer max();
}
