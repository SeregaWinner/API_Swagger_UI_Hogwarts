package ru.hogwarts.controller.mvc;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import ru.hogwarts.controller.StudentController;
import ru.hogwarts.entity.Faculty;
import ru.hogwarts.entity.Student;

import ru.hogwarts.exception.FacultyNotFoundException;
import ru.hogwarts.exception.StudentNotFoundException;
import ru.hogwarts.repository.FacultyRepository;
import ru.hogwarts.repository.StudentRepository;
import ru.hogwarts.service.AvatarService;

import ru.hogwarts.service.StudentService;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(StudentController.class)
public class StudentControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private StudentRepository studentRepository;
    @MockBean
    private FacultyRepository facultyRepository;
    @MockBean
    private AvatarService avatarService;
    @SpyBean
    private StudentService studentService;

    @Test
    @DisplayName("Создать студента")
    void createStudent() throws Exception {
        Student student = new Student(null, "Гарри Поттер", 11);
        when(studentRepository.save(any())).thenReturn(student);
        mockMvc.perform(post("/student")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(student.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(student.getName()))
                .andExpect(jsonPath("$.age").value(student.getAge()));
    }

    @Test
    @DisplayName("Запрос информации по студенту")
    void getStudentTest() throws Exception {
        long id = 1L;
        Student student1 = new Student();
        student1.setId(id);
        student1.setAge(11);
        student1.setName("Гарри Поттер");
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student1));
        mockMvc.perform(get("/student/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(student1.getName()))
                .andExpect(jsonPath("$.age").value(student1.getAge()));

    }

    @Test
    @DisplayName("Попытка найти студента отсутствующего в базе")
    void getStudentNegative() throws Exception {
        long id = 1;
        when(studentRepository.findById(id)).thenThrow(StudentNotFoundException.class);
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/student/{id}", id))
                .andExpect(result -> assertInstanceOf(StudentNotFoundException.class, result.getResolvedException()));
    }

    @Test
    @DisplayName("Запрос студента по возрасту")
    void getStudentAgeTest() throws Exception {

        Student student1 = new Student();
        student1.setId(1L);
        student1.setName("Гарри Поттер");
        student1.setAge(11);

        Student student2 = new Student();
        student1.setId(2L);
        student1.setName("Рон Узли");
        student1.setAge(11);

        when(studentRepository.findAllByAge(10)).thenReturn(Arrays.asList(student1, student2));
        mockMvc.perform(get("/student?age=10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    @DisplayName("Найти студентов в диапазоне min/max возраста")
    void findStudentByAgeBetweenTest() throws Exception {
        Student student1 = new Student();
        student1.setId(1L);
        student1.setName("Гарри Поттер");
        student1.setAge(11);

        Student student2 = new Student();
        student1.setId(2L);
        student1.setName("Седрик Дигори");
        student1.setAge(14);

        when(studentRepository.findByAgeBetween(10, 20)).thenReturn(Arrays.asList(student1, student2));
        mockMvc.perform(get("/student/minAge_maxAge?minAge=10&&maxAge=20"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));

    }

    @Test
    @DisplayName("Заменить студента по id")
    void updateStudent() throws Exception {
        long id = 1L;
        Faculty faculty = new Faculty();
        faculty.setId(id);
        faculty.setName("Гриффиндор");
        faculty.setColor("красный");

        Student student1 = new Student();
        student1.setId(id);
        student1.setName("Гарри Поттер");
        student1.setAge(11);
        student1.setFaculty(faculty);

        Student student2 = new Student();
        student2.setId(id);
        student2.setName("Седрик Дигори");
        student2.setAge(14);
        student2.setFaculty(faculty);

        when(studentRepository.findById(id)).thenReturn(Optional.of(student1));

        mockMvc.perform(put("/student" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(student2.toString()))
                .andExpect(status().isOk());
        verify(studentRepository, times(1)).save(any());

    }

    @Test
    @DisplayName("Редактировать студента по id проверка исключений StudentNotFoundException")
    void updateStudentNegativeTest1() throws Exception {
        long id = -1L;
        Faculty faculty = new Faculty();
        faculty.setName("Гриффиндор");
        faculty.setColor("красный");
        faculty.setId(id);

        Student student1 = new Student();
        student1.setId(id);
        student1.setAge(10);
        student1.setName("Гарри Поттер");
        student1.setFaculty(faculty);

        Student student2 = new Student();
        student2.setId(id);
        student2.setAge(14);
        student2.setName("Седрик Дигори");
        student2.setFaculty(faculty);
        when(studentRepository.findById(id)).thenThrow(StudentNotFoundException.class);

        mockMvc.perform(put("/student/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(student2.toString()))
                .andExpect(result -> assertInstanceOf(StudentNotFoundException.class, result.getResolvedException()));
    }

    @Test
    @DisplayName("Редактировать студента по id проверка исключений FacultyNotFoundException")
    void updateStudentNegativeTest2() throws Exception {
        long id = 1L;
        Faculty faculty = new Faculty();
        faculty.setName("Гриффиндор");
        faculty.setColor("красный");
        faculty.setId(id);

        Student student1 = new Student();
        student1.setId(id);
        student1.setName("Гарри Поттер");
        student1.setAge(11);
        student1.setFaculty(faculty);

        Student student2 = new Student();
        student2.setId(id);
        student2.setName("Седрик Дигори");
        student2.setAge(14);
        student2.setFaculty(faculty);
        when(studentRepository.findById(id)).thenReturn(Optional.of(student1));
        when(facultyRepository.findById(id)).thenThrow(FacultyNotFoundException.class);

        mockMvc.perform(put("/student/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(student2.toString()))
                .andExpect(result -> assertInstanceOf(FacultyNotFoundException.class, result.getResolvedException()));
    }

    @Test
    @DisplayName("Удалить студента")
    void deleteStudentTest() throws Exception {
        long id = 1L;
        Student student1 = new Student();
        student1.setId(id);
        student1.setName("Гарри Поттер");
        student1.setAge(11);


        when(studentRepository.existsById(any())).thenReturn(true);
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student1));

        mockMvc.perform(delete("/student/" + id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        verify(studentRepository, times(0)).deleteById(any());

    }

    @Test
    @DisplayName("Попытка удалить студента по id отсутствующего в базе")
    void deleteStudentNegative() throws Exception {
        long id = 1;
        when(studentRepository.existsById(id)).thenThrow(StudentNotFoundException.class);
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/student/{id}", id))
                .andExpect(result -> assertInstanceOf(StudentNotFoundException.class, result.getResolvedException()));
    }

    @Test
    @DisplayName("Найти факультет по id студента")
    void findStudentsFaculty() throws Exception {
        //data
        long id = 1L;
        String name = "Гриффиндор";
        String color = "красный";
        Faculty faculty = new Faculty();
        faculty.setColor(color);
        faculty.setName(name);
        faculty.setId(id);
        Student student1 = new Student();
        student1.setId(id);
        student1.setName("Гарри Поттер");
        student1.setAge(11);
        student1.setFaculty(faculty);

        when(studentRepository.existsById(any())).thenReturn(true);
        when(studentRepository.findById(id)).thenReturn(Optional.of(student1));
        //test, check
        mockMvc.perform(get("/student/" + id + "/faculty")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(name))
                .andExpect(jsonPath("$.color").value(color));
    }

}