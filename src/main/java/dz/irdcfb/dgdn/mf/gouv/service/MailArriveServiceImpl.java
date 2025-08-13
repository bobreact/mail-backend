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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import dz.irdcfb.dgdn.mf.gouv.entities.MailArrive;
import dz.irdcfb.dgdn.mf.gouv.repository.MailArriveRepository;
import dz.irdcfb.dgdn.mf.gouv.util.FileUploadUtil;
import dz.irdcfb.dgdn.mf.gouv.util.ZipDirectory;

@Service
@Transactional
public class MailArriveServiceImpl implements MailArriveService {

	@Value("${fileBasePath}")
	private String fileBasePath;

	@Value("${tempDir}")
	private String tempDir;

	String pattern = "yyyy-MM-dd";
	SimpleDateFormat sdf = new SimpleDateFormat(pattern);

	MailArriveRepository mailArriveRepository;

	public MailArriveServiceImpl(MailArriveRepository mailArriveRepository) {
		super();
		this.mailArriveRepository = mailArriveRepository;
	}

	@Override
	public List<MailArrive> getAllMail() {
		return mailArriveRepository.findByStructure(1);
	}

	@Override
	public MailArrive saveMail(MailArrive mail, MultipartFile[] multipartFile, Model model) throws IOException {

		File directory = new File(tempDir);
		if (!directory.exists()) {
			directory.mkdir();
		}
		Date dateArrivee = mail.getDateArrivee();
		sdf.format(dateArrivee);
		LocalDate localDateArrivee = dateArrivee.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		final int yearArrivee = localDateArrivee.getYear();
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
			Date date = mail.getDateDepartExp();
			sdf.format(date);
			LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
			final int year = localDate.getYear();
			final int month = localDate.getMonthValue();
			final int day = localDate.getDayOfMonth();
			String numero = mail.getNumDepartExp() + "-" + day + "." + month + "." + year;// + "-" + nextNumero;
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
			mail.setAnnee(yearArrivee);
			mail.setStructure(mail.getStructure());
			// mail.setPath(uploadDir);
			return mailArriveRepository.save(mail);
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

	/*
	 * @Override public Integer max() { // TODO Auto-generated method stub return
	 * mailArriveRepository.max(); }
	 */

}
