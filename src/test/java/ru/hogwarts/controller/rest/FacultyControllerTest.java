package ru.hogwarts.controller.rest;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.hogwarts.controller.FacultyController;
import ru.hogwarts.entity.Faculty;
import ru.hogwarts.entity.Student;
import ru.hogwarts.repository.FacultyRepository;
import ru.hogwarts.repository.StudentRepository;

import java.util.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class FacultyControllerTest {
    @LocalServerPort
    private int port;

    @Autowired
    private FacultyController facultyController;

    @Autowired
    private FacultyRepository facultyRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private TestRestTemplate restTemplate;

    Faculty faculty1 = new Faculty(1L, "Гриффиндор", "красный");
    Faculty faculty2 = new Faculty(2L, "Слизорен", "зеленый");

    @BeforeEach
    void init() {
        facultyRepository.save(faculty1);
        facultyRepository.save(faculty2);
    }

    @AfterEach
    void clear() {
        studentRepository.deleteAll();
        facultyRepository.deleteAll();
    }

    private String buildUrl(String uri) {
        return "http://localhost:%d%s".formatted(port, uri);
    }

    @Test
    void contextLoads() throws Exception {
        assertThat(facultyController).isNotNull();
    }

    @Test
    @DisplayName("Запрос факультета")
    void findFacultyByIdTest() {
        Optional<Faculty> expected = facultyRepository.findById(faculty1.getId());
        ResponseEntity<Faculty> response = restTemplate.getForEntity(buildUrl("/faculty/" + faculty1.getId()), Faculty.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Faculty faculty = response.getBody();
        assertThat(faculty).isNotNull();
        assertThat(response.getBody()).usingRecursiveComparison().ignoringFields("id").isEqualTo(faculty);
        assertThat(faculty.getId()).isNotNull();
        assertThat(faculty.getName()).isEqualTo(faculty1.getName());
        assertThat(faculty.getColor()).isEqualTo(faculty1.getColor());

    }


    @Test
    @DisplayName("Создание факультета")
    void createFacultyTest() {
        Faculty faculty = new Faculty();
        faculty.setName("Пуфендуй");
        faculty.setColor("желтый");
        Faculty actual = this.restTemplate.postForObject(buildUrl("/faculty"), faculty, Faculty.class);
        assertThat(actual.getId()).isNotNull();
        assertThat(actual.getName()).isEqualTo("Пуфендуй");
        assertThat(actual.getColor()).isEqualTo("желтый");
    }

    @Test
    @DisplayName("Редактирование факультета")
    void editFacultyTest() {
        String name = "Пуфендуй";
        String color = "желтый";
        Optional<Faculty> expected = facultyRepository.findById(faculty2.getId());
        faculty2.setName(name);
        faculty2.setColor(color);
        System.out.println(expected);
        restTemplate.put(buildUrl("/faculty/" + faculty2.getId()), faculty2);
        System.out.println(faculty2);
        Faculty actual = facultyRepository.findById(faculty2.getId()).orElseThrow();
        System.out.println(actual);
        assertThat(actual).isNotEqualTo(expected);
        assertThat(actual.getName()).isEqualTo(name);
        assertThat(actual.getColor()).isEqualTo(color);
    }

    @Test
    @DisplayName("Удаление факультета")
    void deleteFacultyTest() {
        Optional<Faculty> actual = facultyRepository.findById(faculty2.getId());
        ResponseEntity<Faculty> response = restTemplate.exchange
                (buildUrl("/faculty") + faculty2.getId(), HttpMethod.DELETE, null, Faculty.class);
        Faculty s = restTemplate.getForObject(buildUrl("/faculty") +
                faculty2.getId(), Faculty.class);
        assertThat(s.getId()).isEqualTo(null);
        assertThat(s.getName()).isEqualTo(null);
        assertThat(s.getColor()).isEqualTo(null);
    }

    @Test
    @DisplayName("Вывод факультетов по цвету")
    void findFacultiesByColorTest() {
        Faculty faculty = new Faculty();
        faculty.setName("Пуфендуй");
        faculty.setColor("желтый");
        List<Faculty> expected = facultyRepository.findAllByColor("желтый");
        ResponseEntity<Faculty[]> response = restTemplate
                .exchange(buildUrl("/faculty?color=желтый"), HttpMethod.GET, null, Faculty[].class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        List<Faculty> fromDb = facultyRepository.findAllByColor("желтый");
        assertThat(fromDb).isEqualTo(expected);
    }


    @Test
    @DisplayName("Вывод факультетов по цвету и названию")
    void findFacultiesByColorOrNameTest() {
        Faculty faculty = new Faculty();
        faculty.setName("Пуфендуй");
        faculty.setColor("красный");
        faculty = facultyRepository.save(faculty);
        ResponseEntity<Collection> response = restTemplate.exchange(
                buildUrl("/faculty?colorOrName=красный&Пуфендуй"),
                HttpMethod.GET,
                null,
                Collection.class,
                "красный"
        );
        Collection<Faculty> faculties = response.getBody();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(faculties.contains(faculty));
    }


    @Test
    @DisplayName("Вывод всех студентов с факультета")
    void findStudentsByFacultyIdTest() {
        Long facultyId = 1L;
        Student student1 = new Student();
        student1.setName("Гермиона Гренджер");
        student1.setAge(12);
        student1.setFaculty(faculty1);
        student1 = studentRepository.save(student1);
        Student student2 = new Student();
        student2.setName("Гарри Поттерр");
        student2.setAge(11);
        student2.setFaculty(faculty1);
        student2 = studentRepository.save(student2);
        List<Student> expected = new ArrayList<>();
        expected.add(student1);
        expected.add(student2);
        ResponseEntity<List<Student>> response = restTemplate.exchange(
                buildUrl("/faculty/" + facultyId + "/students"),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {
                },
                facultyId);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        List<Student> actual = response.getBody();
        assertThat(actual).isEqualTo(expected);
        assertThat(response).isNotNull();
    }
}