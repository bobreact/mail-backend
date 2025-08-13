package dz.irdcfb.dgdn.mf.gouv.service;

import java.io.IOException;
import java.util.List;

import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import dz.irdcfb.dgdn.mf.gouv.entities.MailDepart;

public interface MailDepartService {

	List<MailDepart> getAllMail();

	MailDepart saveMail(MailDepart mail, MultipartFile[] multipartFile, Model model) throws IOException;

	ResponseEntity<Resource> downloadMail(String fileName) throws IOException;

}
