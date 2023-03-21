package dgdn.mf.gouv.dz.web;

import java.io.IOException;
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

import dgdn.mf.gouv.dz.dao.MailArriveRepository;
import dgdn.mf.gouv.dz.dto.MailResponseDTO;
import dgdn.mf.gouv.dz.entities.MailArrive;
import dgdn.mf.gouv.dz.security.payload.response.MessageResponse;
import dgdn.mf.gouv.dz.service.MailArriveService;

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

		if (mailArriveRepository.existsByNumArriveAndNumDepartExpAndDateDepartExp(mail.getNumArrive(),
				mail.getNumDepartExp(), mail.getDateDepartExp())) {
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