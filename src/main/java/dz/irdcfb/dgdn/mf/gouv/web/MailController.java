package dz.irdcfb.dgdn.mf.gouv.web;

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
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import dz.irdcfb.dgdn.mf.gouv.dao.MailArriveRepository;
import dz.irdcfb.dgdn.mf.gouv.dto.MailResponseDTO;
import dz.irdcfb.dgdn.mf.gouv.entities.MailArrive;
import dz.irdcfb.dgdn.mf.gouv.security.payload.response.MessageResponse;
import dz.irdcfb.dgdn.mf.gouv.service.MailArriveService;


@CrossOrigin("*")
@Controller
@RestController
@RequestMapping("/api/mail")

@Component
public class MailController {

	private MailArriveService mailArriveService;

	public MailController(MailArriveService mailArriveService) {
		super();
		this.mailArriveService = mailArriveService;
	}

	@Autowired
	MailArriveRepository mailArriveRepository;

	String pattern = "yyyy-MM-dd";
	SimpleDateFormat sdf = new SimpleDateFormat(pattern);
	
	
	
	@GetMapping
	// @PreAuthorize("hasAuthority('ROLE_ADMIN')")
	@PreAuthorize("hasRole('USER')")
	public List<MailArrive> getAllMails() {
		return mailArriveService.getAllMail();
	}

	@PostMapping(value = "/upload", consumes = { MediaType.ALL_VALUE })
	@PreAuthorize("hasAuthority('ROLE_USER')")
	public ResponseEntity<?> saveMail(@RequestPart("mail") @Valid MailArrive mail,
			@RequestPart("file") MultipartFile[] multipartFile, Model model) throws IOException {

		/*
		 * if
		 * (mailArriveRepository.existsByNumArriveAndNumDepartExpAndDateDepartExp(mail.
		 * getNumArrive(), mail.getNumDepartExp(), mail.getDateDepartExp())) { return
		 * ResponseEntity.badRequest().body(new
		 * MessageResponse("Error: Mail already exists!")); }
		 */
		Date dateArrivee = mail.getDateArrivee();
		sdf.format(dateArrivee);
		//LocalDate localDateArrivee = dateArrivee.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();		
       // final int yearArrivee = localDateArrivee.getYear();
		if (mailArriveRepository.existsByNumArriveAndAnnee(mail.getNumArrive(),
		dateArrivee.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().getYear())) {
			return ResponseEntity.badRequest().body(new MessageResponse("Error: Mail already exists!"));
		}

		mailArriveService.saveMail(mail, multipartFile, model);
		return ResponseEntity.ok(new MailResponseDTO("Success !"));

	}

	@GetMapping("/download/{fileName}")
	@PreAuthorize("hasAuthority('ROLE_ADMIN')")
	public ResponseEntity<Resource> downloadFile(@PathVariable String fileName) throws IOException {
		try {
			return mailArriveService.downloadMail(fileName);
		} catch (RuntimeException exc) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Folder not found", exc);
		}
	}
}