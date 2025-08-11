package dz.irdcfb.dgdn.mf.gouv.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ZipDirectory {
	public ZipDirectory() {

	}

	// La méthode utilisée pour compresser un dossier.
	public void zipDirectory(File inputDir, File outputZipFile) {
		// Créez le dossier parent pour le fichier de sortie (output file).
		outputZipFile.getParentFile().mkdirs();

		String inputDirPath = inputDir.getAbsolutePath();
		byte[] buffer = new byte[1024];

		FileOutputStream fileOs = null;
		ZipOutputStream zipOs = null;
		try {
			List<File> allFiles = this.listChildFiles(inputDir);
			// Créez l'object ZipOutputStream pour graver le fichier zip.
			fileOs = new FileOutputStream(outputZipFile);
			//
			zipOs = new ZipOutputStream(fileOs);
			for (File file : allFiles) {
				String filePath = file.getAbsolutePath();
				// System.out.println("Zipping " + filePath);
				// entryName : est un chemin relatif.
				String entryName = filePath.substring(inputDirPath.length() + 1);

				ZipEntry ze = new ZipEntry(entryName);
				// AjouteZ entry au fichier zip.
				zipOs.putNextEntry(ze);
				// Lisez les données du fichier et écrivez sur le ZipOutputStream.
				FileInputStream fileIs = new FileInputStream(filePath);
				int len;
				while ((len = fileIs.read(buffer)) > 0) {
					zipOs.write(buffer, 0, len);
				}
				fileIs.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			closeQuite(zipOs);
			closeQuite(fileOs);
		}
	}

	private void closeQuite(OutputStream out) {
		try {
			out.close();
		} catch (Exception e) {
		}
	}

	// Cette méthode renvoie la liste des fichiers,
	// y compris tous les fichiers enfants, petits-enfants, .. du répertoire
	// d'entrée.
	private List<File> listChildFiles(File dir) throws IOException {
		List<File> allFiles = new ArrayList<>();

		File[] childFiles = dir.listFiles();
		for (File file : childFiles) {
			if (file.isFile()) {
				allFiles.add(file);
			} else {
				List<File> files = this.listChildFiles(file);
				allFiles.addAll(files);
			}
		}
		return allFiles;
	}

}