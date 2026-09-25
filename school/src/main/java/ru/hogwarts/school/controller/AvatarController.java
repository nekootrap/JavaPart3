package ru.hogwarts.school.controller;

import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.hogwarts.school.model.Avatar;
import ru.hogwarts.school.repository.AvatarRepository;
import ru.hogwarts.school.service.AvatarService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.IOException;

@RestController
@RequestMapping("/avatar")
public class AvatarController {

    private final AvatarService avatarService;
    private final AvatarRepository avatarRepository;

    public AvatarController(AvatarService avatarService, AvatarRepository avatarRepository) {
        this.avatarService = avatarService;
        this.avatarRepository = avatarRepository;
    }

    @GetMapping
    public Page<Avatar> getAllAvatars(@RequestParam int page, @RequestParam int size) {
        Pageable pageable = PageRequest.of(page, size);
        return avatarRepository.findAll(pageable);
    }

    @PostMapping(value = "/upload/{studentId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Avatar uploadAvatar(@PathVariable Long studentId, @RequestParam("file") MultipartFile file) throws IOException {
        return avatarService.uploadAvatar(studentId, file);
    }

    @GetMapping("/from-db/{id}")
    public ResponseEntity<byte[]> getAvatarFromDb(@PathVariable Long id) {
        Avatar avatar = avatarService.getAvatarFromDb(id)
                .orElseThrow(() -> new RuntimeException("Avatar not found in DB"));
        
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(avatar.getMediaType()))
                .body(avatar.getData());
    }

    @GetMapping("/from-file/{id}")
    public ResponseEntity<byte[]> getAvatarFromFile(@PathVariable Long id) {
        Avatar avatar = avatarService.getAvatarFromFileSystem(id)
                .orElseThrow(() -> new RuntimeException("Avatar not found in File System"));

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(avatar.getMediaType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + avatar.getFilePath() + "\"")
                .body(avatar.getData());
    }
}
