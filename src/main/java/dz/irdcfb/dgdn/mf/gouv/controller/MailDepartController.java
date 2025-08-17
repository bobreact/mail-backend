package dz.irdcfb.dgdn.mf.gouv.controller;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import dz.irdcfb.dgdn.mf.gouv.dto.MailResponseDTO;
import dz.irdcfb.dgdn.mf.gouv.entities.MailDepart;
import dz.irdcfb.dgdn.mf.gouv.repository.MailDepartRepository;
import dz.irdcfb.dgdn.mf.gouv.security.payload.response.MessageResponse;
import dz.irdcfb.dgdn.mf.gouv.service.MailDepartService;

@CrossOrigin("*")
@Controller
@RestController
@RequestMapping("/api/mail/depart")

@Component
public class MailDepartController {

	private MailDepartService mailDepartService;

	public MailDepartController(MailDepartService mailDepartService) {
		super();
		this.mailDepartService = mailDepartService;
	}

	@Autowired(required = true)
	MailDepartRepository mailDepartRepository;

	String pattern = "yyyy-MM-dd";
	SimpleDateFormat sdf = new SimpleDateFormat(pattern);

	@GetMapping
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<List<MailDepart>> searchMail(@RequestParam Integer structure) {
		List<MailDepart> mails = mailDepartRepository.findByStructure(structure);
		return ResponseEntity.ok(mails);
	}

	@PostMapping(value = "/upload", consumes = { MediaType.ALL_VALUE })
	@PreAuthorize("hasAuthority('ROLE_USER')")
	public ResponseEntity<?> saveMail(@RequestPart("mail") @Valid MailDepart mail,
			@RequestPart("file") MultipartFile[] multipartFile, Model model) throws IOException {

		/*
		 * if (mailDepartRepository.existsByNumDepartAndDateDepart(mail.getNumDepart(),
		 * mail.getDateDepart())) { return ResponseEntity.badRequest().body(new
		 * MessageResponse("Error: Mail already exists!")); }
		 */
		Date dateDepart = mail.getDateDepart();
		sdf.format(dateDepart);
		if (mailDepartRepository.existsByNumDepartAndAnneeAndStructure(mail.getNumDepart(),
				dateDepart.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().getYear(), mail.getStructure())) {
			return ResponseEntity.badRequest().body(new MessageResponse("Error: Mail already exists!"));
		}
		mailDepartService.saveMail(mail, multipartFile, model);
		return ResponseEntity.ok(new MailResponseDTO("Success !"));

	}

	@GetMapping("/download/{fileName}")
	@PreAuthorize("hasAuthority('ROLE_ADMIN')")
	public ResponseEntity<Resource> downloadFile(@PathVariable String fileName) throws IOException {
		try {
			return mailDepartService.downloadMail(fileName);
		} catch (RuntimeException exc) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Folder not found", exc);
		}
	}
}