package ru.hogwarts.service;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import ru.hogwarts.entity.Avatar;
import ru.hogwarts.entity.Student;
import ru.hogwarts.exception.AvatarProcessingException;
import ru.hogwarts.exception.StudentNotFoundException;
import ru.hogwarts.repository.AvatarRepository;
import ru.hogwarts.repository.StudentRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;


@Service
@Transactional
public class AvatarService {
    private final StudentRepository studentRepository;
    private final AvatarRepository avatarRepository;
    private final Path path;
    private static final Logger logger = LoggerFactory.getLogger(AvatarService.class);

    public AvatarService(AvatarRepository avatarRepository, StudentRepository studentRepository,
                         @Value("${application.avatars-dir-name}") String avatarsDirName) {
        this.avatarRepository = avatarRepository;
        this.studentRepository = studentRepository;
        path = Paths.get(avatarsDirName);
    }

    public void uploadAvatar(MultipartFile multipartFile, long studentId) {
        logger.info("Was invoked method for \"uploadAvatar\"");
        try {
            byte[] data = multipartFile.getBytes();
            String extension = StringUtils.getFilenameExtension(multipartFile.getOriginalFilename());
            Path avatarPath = path.resolve(UUID.randomUUID() + "." + extension);
//            Files.write(avatarPath, data);* проблема тут!!!
            Student student = studentRepository.findById(studentId)
                    .orElseThrow(() -> {
                        logger.error("There is not student with id = " + studentId);
                        return new StudentNotFoundException(studentId);
                    });
            Avatar avatar = avatarRepository.findByStudent_Id(studentId)
                    .orElseGet(Avatar::new);
            avatar.setStudent(student);
            avatar.setFileSize(data.length);
            avatar.setData(data);
            avatar.setMediaType(multipartFile.getContentType());
            avatar.setFilePath(avatarPath.toString());
            avatarRepository.save(avatar);
        } catch (IOException e) {
            logger.error("Blowout exception \"AvatarProcessingException\"");
            throw new AvatarProcessingException();
        }

    }

    public Pair<byte[], String> getAvatarFromDb(long studentId) {
        logger.info("Was invoked method for \"getAvatarFromDb\"");
        Avatar avatar = avatarRepository.findByStudent_Id(studentId)
                .orElseThrow(() -> {
                    logger.error("There is not student with id = " + studentId);
                    return new StudentNotFoundException(studentId);
                });
        return Pair.of(avatar.getData(), avatar.getMediaType());
    }

    public Pair<byte[], String> getAvatarFromFs(long studentId) {
        logger.info("Was invoked method for \"getAvatarFromFs\"");
        try {
            Avatar avatar = avatarRepository.findByStudent_Id(studentId)
                    .orElseThrow(() -> {
                        logger.error("There is not student with id = " + studentId);
                        return new StudentNotFoundException(studentId);
                    });
            return Pair.of(Files.readAllBytes(Paths.get(avatar.getFilePath())), avatar.getMediaType());
        } catch (IOException e) {
            logger.error("Blowout exception \"AvatarProcessingException\"");
            throw new AvatarProcessingException();
        }


    }

    public List<Avatar> getAllAvatarsForPage(Integer pageNumber, Integer pageSize) {
        logger.info("Was invoked method for \"getAllAvatarsForPage\"");
        PageRequest pageRequest = PageRequest.of(pageNumber - 1, pageSize);
        return avatarRepository.findAll(pageRequest).getContent();
    }
}
