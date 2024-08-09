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
import org.springframework.http.*;
import ru.hogwarts.controller.StudentController;
import ru.hogwarts.entity.Faculty;
import ru.hogwarts.entity.Student;
import ru.hogwarts.repository.FacultyRepository;
import ru.hogwarts.repository.StudentRepository;

import java.util.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class StudentControllerTest {
    @LocalServerPort
    private int port;

    @Autowired
    private StudentController studentController;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private FacultyRepository facultyRepository;

    @Autowired
    private TestRestTemplate restTemplate;

    Faculty faculty1 = new Faculty(1L, "Гриффиндор", "красный");
    Faculty faculty2 = new Faculty(2L, "Слизарин", "зеленый");

    Student student1 = new Student(1L, "Гарри Поттер", 11);
    Student student2 = new Student(2L, "Рон Узли", 12);
    Student student3 = new Student(3L, "Драго Малфой", 11);
    Student student4 = new Student(4L, "Седрик Дигори", 14);

    @BeforeEach
    void init() {
        facultyRepository.save(faculty1);
        facultyRepository.save(faculty2);
        studentRepository.save(student1);
        studentRepository.save(student2);
        studentRepository.save(student3);
        studentRepository.save(student4);
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
    void contextLoads()  {
        assertThat(studentController).isNotNull();
    }

    @Test
    @DisplayName("Запрос информации по студенту")
    void getStudentTest() {
        assertThat(this.restTemplate.getForObject(buildUrl("/student"), String.class))
                .isNotNull();
    }

    @Test
    @DisplayName("Добавляет студента без факультета")
    void createStudentWithoutFacultyTest()  {
        Student student = new Student();
        student.setName("Гермиона Гренджер");
        student.setAge(12);
        createStudentPositive(student);
    }

    @Test
    @DisplayName("Добавляет студента с факультетом")
    void createStudentWithFacultyTest()  {
        Student student = new Student();
        student.setName("Гермиона Гренджер");
        student.setAge(12);
        student.setFaculty(facultyRepository.findAll().get(1));
        createStudentPositive(student);
    }

    @Test
    @DisplayName("Ошибка добавления студента на несуществующий факультет")
    void createStudentWithoutFacultyNegativeTest()  {
        Student student = new Student();
        student.setName("Гермиона Гренджер");
        student.setAge(12);
        Faculty faculty = new Faculty();
        faculty.setId(3L);
        faculty.setName("Феникс");
        faculty.setColor("белый");
        student.setFaculty(faculty);
        ResponseEntity<String> response = restTemplate.postForEntity(buildUrl("/student"), student, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isEqualTo("Факультет с id = %d не найден!".formatted(3));
    }

    @Test
    @DisplayName("Обнавление данных студента")
    public void updateStudentTest()  {
        int age = new Random().nextInt(1, 100);
        String name = "Джини Узли";
        Optional<Student> expected = studentRepository.findById(student4.getId());
        student4.setName(name);
        student4.setAge(age);
        restTemplate.put(buildUrl("/student/" + student4.getId()), student4);
        Student actual = studentRepository.findById(student4.getId()).get();
        assertThat(actual).isNotEqualTo(expected);
        assertThat(actual.getName()).isEqualTo(name);
        assertThat(actual.getAge()).isEqualTo(age);
    }


    @Test
    @DisplayName("Удаление студента")
    public void deleteStudentTest()  {
        Optional<Student> actual = studentRepository.findById(student4.getId());
        ResponseEntity<String> response = restTemplate.exchange
                (buildUrl("/student/") + student4.getId(), HttpMethod.DELETE, null, String.class);
        Student s = restTemplate.getForObject(buildUrl("/student") +
                student4.getId(), Student.class);
        assertThat(s.getId()).isEqualTo(null);
        assertThat(s.getName()).isEqualTo(null);
        assertThat(s.getAge()).isEqualTo(0);
    }


    @Test
    @DisplayName("Фильтрация студентов по возрасту")
    void findStudentsForAgeTest() {
        ResponseEntity<Student[]> response = restTemplate
                .exchange(buildUrl("/student?age=11"), HttpMethod.GET, null, Student[].class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertEquals(response.getBody().length, 2);
    }

    @Test
    @DisplayName("Список студентов от мин до макс возраста")
    void filterByAgeRangeTest() {
        List<Student> expected = studentRepository.findByAgeBetween(10, 14);
        ResponseEntity<List<Student>> response = restTemplate.exchange(buildUrl("/student/minAge_maxAge?minAge=10&maxAge=14"),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {
                });
        List<Student> actual = response.getBody();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(actual).isEqualTo(expected);
        assertThat(response).isNotNull();

    }

    private void createStudentPositive(Student student) {
        ResponseEntity<Student> response = restTemplate.postForEntity(buildUrl("/student"), student, Student.class);
        Student created = response.getBody();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(created).isNotNull();
        assertThat(created).usingRecursiveComparison().ignoringFields("id").isEqualTo(student);
        assertThat(created.getId()).isNotNull();
        Optional<Student> fromDb = studentRepository.findById(created.getId());
        assertThat(fromDb).isPresent();
        assertThat(fromDb.get()).usingRecursiveComparison().isEqualTo(created);


    }

}





