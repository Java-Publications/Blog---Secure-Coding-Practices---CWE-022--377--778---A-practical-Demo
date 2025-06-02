package com.svenruppert.filemanager;

import com.svenruppert.dependencies.core.logger.HasLogger;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import com.vaadin.flow.router.Route;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

/**
 * The main view contains a text field for getting the user name and a button
 * that shows a greeting message on a new line.
 */
@Route("")
public class MainView
    extends VerticalLayout
    implements HasLogger {

//  private static final Logger LOGGER = Logger.getLogger(MainView.class.getName());

  private static final String UPLOADS = "uploads";
  private Div divUpload = new Div();
  private VerticalLayout fileList = new VerticalLayout();

  public MainView() {
    divUpload.setWidth("100%");
    fileList.setWidth("100%");
    setDivUploadV001();
//    setDivUploadV002();
//    setDivUploadV003();
//    setDivUploadV004();
//    setDivUploadV005();
    updateFileList();
    add(divUpload);
    add(fileList);
  }

  private void updateFileList() {
    fileList.removeAll();
    File folder = new File(UPLOADS);
    File[] listOfFiles = folder.listFiles();
    if (listOfFiles != null) {
      for (File file : listOfFiles) {
        if (file.isFile()) {
          Anchor downloadLink = new Anchor("/" + UPLOADS + "/" + file.getName(), file.getName());
          downloadLink.getElement().setAttribute("download", true);
          downloadLink.add(new Button(new Icon(VaadinIcon.DOWNLOAD_ALT)));
          fileList.add(downloadLink);
          logger().info("Datei {} zur Download-Liste hinzugefügt " , file.getName());
        }
      }
    } else {
      logger().warn("Verzeichnis 'uploads' konnte nicht gelesen werden oder ist leer.");
    }
  }

  private void setDivUploadV005() {
    MemoryBuffer buffer = new MemoryBuffer();
    Upload upload = new Upload(buffer);
    upload.setMaxFiles(1);
    upload.addSucceededListener(event -> {
      String fileName = sanitizeFileName(event.getFileName());
      Path targetPath = Paths.get("uploads").resolve(fileName).normalize();

      // Verhindern, dass der Pfad außerhalb des Upload-Verzeichnisses liegt
      if (!targetPath.startsWith(Paths.get("uploads").toAbsolutePath())) {
        Notification.show("Ungültiger Dateipfad! Upload abgebrochen.");
        logger().warn("Ungültiger Dateipfad-Versuch: {} " ,targetPath);
        return;
      }

      try (InputStream inputStream = buffer.getInputStream()) {
        // Erstellen einer sicheren temporären Datei
        Path tempFile = Files.createTempFile("upload_", "tmp");
        Files.copy(inputStream, tempFile, StandardCopyOption.REPLACE_EXISTING);

        // Verschieben der temporären Datei in das Zielverzeichnis
        Files.createDirectories(targetPath.getParent());
        Files.move(tempFile, targetPath, StandardCopyOption.REPLACE_EXISTING);
        Notification.show("Datei " + fileName + " erfolgreich hochgeladen!");
        logger().info("Datei {} erfolgreich hochgeladen nach {} ", fileName, targetPath);
        updateFileList();
      } catch (IOException e) {
        Notification.show("Fehler beim Hochladen der Datei");
        logger().warn("Fehler beim Hochladen der Datei {}: {} ", fileName, e.getMessage());
      }
    });
  }

  private void setDivUploadV004() {
    MemoryBuffer buffer = new MemoryBuffer();
    Upload upload = new Upload(buffer);
    upload.setMaxFiles(1);
    upload.addSucceededListener(event -> {
      String fileName = sanitizeFileName(event.getFileName());
      Path targetPath = Paths.get(UPLOADS).resolve(fileName).normalize();

      // Verhindern, dass der Pfad außerhalb des Upload-Verzeichnisses liegt
      if (!targetPath.startsWith(Paths.get(UPLOADS).toAbsolutePath())) {
        Notification.show("Ungültiger Dateipfad! Upload abgebrochen.");
        return;
      }

      try (InputStream inputStream = buffer.getInputStream()) {
        // Erstellen einer sicheren temporären Datei
        Path tempFile = Files.createTempFile("upload_", "tmp");
        Files.copy(inputStream, tempFile, StandardCopyOption.REPLACE_EXISTING);

        // Verschieben der temporären Datei in das Zielverzeichnis
        Files.createDirectories(targetPath.getParent());
        Files.move(tempFile, targetPath, StandardCopyOption.REPLACE_EXISTING);
        Notification.show("Datei " + fileName + " erfolgreich hochgeladen!");
        updateFileList();
      } catch (IOException e) {
        Notification.show("Fehler beim Hochladen der Datei");
      }
    });
  }

  private String sanitizeFileName(String fileName) {
    return fileName.replaceAll("[^a-zA-Z0-9._-]", "_");
  }

  private void setDivUploadV003() {
    MemoryBuffer buffer = new MemoryBuffer();
    Upload upload = new Upload(buffer);
    upload.setMaxFiles(1);
    upload.addSucceededListener(event -> {
      String fileName = sanitizeFileName(event.getFileName());
      Path targetPath = Paths.get(UPLOADS).resolve(fileName).normalize();

      // Verhindern, dass der Pfad außerhalb des Upload-Verzeichnisses liegt
      if (!targetPath.startsWith(Paths.get(UPLOADS).toAbsolutePath())) {
        Notification.show("Ungültiger Dateipfad! Upload abgebrochen.");
        return;
      }

      try (InputStream inputStream = buffer.getInputStream()) {
        Files.createDirectories(targetPath.getParent());
        Files.copy(inputStream, targetPath, StandardCopyOption.REPLACE_EXISTING);
        Notification.show("Datei " + fileName + " erfolgreich hochgeladen!");
        updateFileList();
        ;
      } catch (IOException e) {
        Notification.show("Fehler beim Hochladen der Datei");
      }
    });
  }

  private void setDivUploadV002() {
    MemoryBuffer buffer = new MemoryBuffer();
    Upload upload = new Upload(buffer);
    upload.setMaxFiles(1);
    upload.addSucceededListener(event -> {
      String fileName = event.getFileName();
      Path targetPath = Paths.get(UPLOADS).resolve(fileName);
      try (InputStream inputStream = buffer.getInputStream()) {
        Files.createDirectories(targetPath.getParent());
        Files.copy(inputStream, targetPath, StandardCopyOption.REPLACE_EXISTING);
        Notification.show("Datei " + fileName + " erfolgreich hochgeladen!");
        updateFileList();
      } catch (IOException e) {
        Notification.show("Fehler beim Hochladen der Datei");
      }
    });
  }

  private void setDivUploadV001() {
    MemoryBuffer buffer = new MemoryBuffer();
    Upload upload = new Upload(buffer);
    upload.setMaxFiles(1);
    upload.addSucceededListener(event -> {
      String fileName = event.getFileName();
      try (InputStream inputStream = buffer.getInputStream()) {
        File targetFile = new File(UPLOADS + "/" + fileName);
        var mkdirs = targetFile.getParentFile().mkdirs();
        try (FileOutputStream outputStream = new FileOutputStream(targetFile)) {
          inputStream.transferTo(outputStream);
        }
        updateFileList();
        Notification.show("Datei " + fileName + " erfolgreich hochgeladen!");
      } catch (IOException e) {
        Notification.show("Fehler beim Hochladen der Datei");
      }
    });
    divUpload.add(upload);
  }

}