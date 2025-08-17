package dz.irdcfb.dgdn.mf.gouv.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.multipart.MultipartFile;

import dz.irdcfb.dgdn.mf.gouv.entities.MailDepart;
import dz.irdcfb.dgdn.mf.gouv.util.FileUploadUtil;
import dz.irdcfb.dgdn.mf.gouv.util.ZipDirectory;

@CrossOrigin("*")
@Service
@Transactional
public class MailDepartServiceImpl implements MailDepartService {
	@Value("${fileBasePath}")
	private String fileBasePath;

	@Value("${tempDir}")
	private String tempDir;

	String pattern = "yyyy-MM-dd";
	SimpleDateFormat sdf = new SimpleDateFormat(pattern);

	@Autowired(required = true)
	dz.irdcfb.dgdn.mf.gouv.repository.MailDepartRepository mailDepartRepository;

	@Override
	public List<MailDepart> getAllMail() {
		return mailDepartRepository.findAll();
	}

	@Override
	public MailDepart saveMail(MailDepart mail, MultipartFile[] multipartFile, Model model) throws IOException {
		File directory = new File(tempDir);
		try {

			if (!directory.exists()) {
				directory.mkdir();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		Date dateDepart = mail.getDateDepart();
		sdf.format(dateDepart);
		LocalDate localDateDepart = dateDepart.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		final int yearDepart = localDateDepart.getYear();

		try {
			for (MultipartFile file : multipartFile) {
				String originalFilename = file.getOriginalFilename();
				if (originalFilename == null) {
					throw new IllegalArgumentException("File name cannot be null");
				}
				String fileName = StringUtils.cleanPath(originalFilename);
				FileUploadUtil.saveFile(tempDir, fileName, file);
				model.addAttribute("name", fileName);
			}

			String name = (String) model.getAttribute("name");
			Date date = mail.getDateDepart();
			sdf.format(date);
			LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
			final int year = localDate.getYear();
			final int month = localDate.getMonthValue();
			final int day = localDate.getDayOfMonth();
			String numero = "D" + "." + mail.getNumDepart() + "-" + day + "." + month + "." + year;// + "-" +
																									// nextNumero;
			String uploadDir = fileBasePath + numero + ".zip";
			File fileDir = new File(uploadDir);
			File fileTemp = new File(tempDir + name + ".zip");
			ZipDirectory zipDir = new ZipDirectory();
			zipDir.zipDirectory(directory, fileTemp);
			FileUtils.copyFile(fileTemp, fileDir);

			File[] listFichier = directory.listFiles();
			for (File file : listFichier) {
				file.delete();
			}

			mail.setFile(numero + ".zip");
			mail.setAnnee(yearDepart);
			mail.setStructure(mail.getStructure());
			// mail.setPath(uploadDir);
			return mailDepartRepository.save(mail);
		} catch (IOException ioe) {
			throw new IOException("Could not save the file");
		}
	}

	@Override
	public ResponseEntity<Resource> downloadMail(String fileName) throws IOException {
		Path path = Paths.get(fileBasePath + fileName);
		Resource resource = null;
		resource = new FileSystemResource(path);
		return ResponseEntity.ok().contentType(MediaType.valueOf("application/zip"))
				.contentLength(resource.contentLength())
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
				.body(resource);

	}

}
