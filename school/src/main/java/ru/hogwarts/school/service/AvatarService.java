package ru.hogwarts.school.service;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.hogwarts.school.model.Avatar;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.AvatarRepository;
import ru.hogwarts.school.repository.StudentRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

@Service
public class AvatarService {

    private final AvatarRepository avatarRepository;
    private final StudentRepository studentRepository;
    private final Path storageLocation;

    public AvatarService(AvatarRepository avatarRepository, 
                         StudentRepository studentRepository,
                         @Value("${avatar.storage.path:./avatars}") String storagePath) {
        this.avatarRepository = avatarRepository;
        this.studentRepository = studentRepository;
        this.storageLocation = Paths.get(storagePath).toAbsolutePath().normalize();
    }

    public Avatar uploadAvatar(Long studentId, MultipartFile file) throws IOException {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        if (file.isEmpty()) {
            throw new RuntimeException("File is empty");
        }

        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        Path targetLocation = storageLocation.resolve(fileName);
        Files.createDirectories(targetLocation.getParent());
        Files.copy(file.getInputStream(), targetLocation);

        Avatar avatar = new Avatar();
        avatar.setFilePath(targetLocation.toString());
        avatar.setFileSize(file.getSize());
        avatar.setMediaType(file.getContentType());
        avatar.setData(file.getBytes());
        avatar.setStudent(student);

        return avatarRepository.save(avatar);
    }

    public Optional<Avatar> getAvatarFromDb(Long id) {
        return avatarRepository.findById(id);
    }

    public Optional<Avatar> getAvatarFromFileSystem(Long id) {
        Optional<Avatar> avatarOpt = avatarRepository.findById(id);
        if (avatarOpt.isPresent()) {
            Avatar avatar = avatarOpt.get();
            try {
                Path path = Paths.get(avatar.getFilePath());
                if (Files.exists(path)) {
                    byte[] data = Files.readAllBytes(path);
                    avatar.setData(data);
                    return Optional.of(avatar);
                }
            } catch (IOException e) {
                throw new RuntimeException("Error reading file", e);
            }
        }
        return Optional.empty();
    }
}
