package ru.hogwarts.controller.mvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.minidev.json.JSONObject;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.hogwarts.controller.FacultyController;
import ru.hogwarts.entity.Faculty;
import ru.hogwarts.entity.Student;
import ru.hogwarts.exception.FacultyNotFoundException;
import ru.hogwarts.repository.FacultyRepository;
import ru.hogwarts.repository.StudentRepository;
import ru.hogwarts.service.FacultyService;
import ru.hogwarts.service.StudentService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(FacultyController.class)
class FacultyControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private FacultyRepository facultyRepository;
    @MockBean
    private StudentRepository studentRepository;
    @SpyBean
    private FacultyService facultyService;
    @SpyBean
    private StudentService studentService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @DisplayName("Создание факультета")
    void createFaculty() throws Exception {
        long id = 1;
        String name = "Гриффиндор";
        String color = "красный";

        Faculty faculty = new Faculty();
        faculty.setName(name);
        faculty.setId(id);
        faculty.setColor(color);

        JSONObject facultyObject = new JSONObject();
        facultyObject.put("name", name);
        facultyObject.put("id", id);
        facultyObject.put("color", color);

        when(facultyRepository.save(any(Faculty.class))).thenReturn(faculty);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/faculty")
                        .content(facultyObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(name))
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.color").value(color));
        verify(facultyRepository, only()).save(any());
    }

    @Test
    @DisplayName("Редактирование факультета")
    void editFaculty() throws Exception {
        long id = 1L;
        String nameNew = "Гриффиндор";
        String colorNew = "красный";
        Faculty oldFaculty = new Faculty();
        oldFaculty.setId(id);
        oldFaculty.setName("Слизарин");
        oldFaculty.setColor("зелёный");

        Faculty newFaculty = new Faculty();
        newFaculty.setName(nameNew);
        newFaculty.setColor(colorNew);
        newFaculty.setId(id);

        JSONObject newFacultyTest = new JSONObject();
        newFacultyTest.put("name", nameNew);
        newFacultyTest.put("id", id);
        newFacultyTest.put("color", colorNew);

        when(facultyRepository.findById(id)).thenReturn(Optional.of(oldFaculty));
        when(facultyRepository.save(any())).thenReturn(newFaculty);

        mockMvc.perform(MockMvcRequestBuilders
                .put("/faculty/{id}", id)
                .content(newFacultyTest.toString())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));
        verify(facultyRepository, times(1)).save(any());
    }

    @Test
    @DisplayName("Редактировани факультета отсутвующего в базе")
    void updateFacultyNegativeTest() throws Exception {
        long id = 2L;
        String nameNew = "Гриффиндор";
        String colorNew = "красный";
        Faculty oldFaculty = new Faculty();
        oldFaculty.setId(id);
        oldFaculty.setName("Слизарин");
        oldFaculty.setColor("зелёный");

        Faculty newFaculty = new Faculty();
        newFaculty.setName(nameNew);
        newFaculty.setColor(colorNew);
        newFaculty.setId(id);

        JSONObject newFacultyTest = new JSONObject();
        newFacultyTest.put("name", nameNew);
        newFacultyTest.put("id", id);
        newFacultyTest.put("color", colorNew);

        when(facultyRepository.findById(id)).thenThrow(FacultyNotFoundException.class);
        when(facultyRepository.save(any())).thenReturn(newFaculty);
        mockMvc.perform(MockMvcRequestBuilders
                        .put("/faculty/{id}", id)
                        .content(newFacultyTest.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(result -> assertInstanceOf(FacultyNotFoundException.class, result.getResolvedException()));
    }

    @Test
    @DisplayName("Запрос факультета")
    void findFaculty() throws Exception {
        long id = 1;
        String name = "Гриффиндор";
        String color = "красный";

        Faculty faculty = new Faculty();
        faculty.setName(name);
        faculty.setId(id);
        faculty.setColor(color);

        JSONObject facultyObject = new JSONObject();
        facultyObject.put("id", id);
        facultyObject.put("name", name);
        facultyObject.put("color", color);

        when(facultyRepository.findById(any())).thenReturn(Optional.of(faculty));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/faculty/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(name))
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.color").value(color));
        verify(facultyRepository, only()).findById(any());

    }

    @Test
    @DisplayName("Запрос факультета отсутвующего в базе")
    void getFacultyNegativeTest() throws Exception {
        long id = 1;
        when(facultyRepository.findById(id)).thenThrow(FacultyNotFoundException.class);
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/faculty/{id}", id))
                .andExpect(result -> assertInstanceOf(FacultyNotFoundException.class, result.getResolvedException()));
    }

    @Test
    @DisplayName("Удаление факультета")
    void deleteFacultyTest() throws Exception {

        long id = 1L;
        Faculty oldFaculty = new Faculty();
        oldFaculty.setId(id);
        oldFaculty.setName("Гриффиндор");
        oldFaculty.setColor("красный");
        when(facultyRepository.save(any(Faculty.class))).thenReturn(oldFaculty);
        when(facultyRepository.findById(any(Long.class))).thenReturn(Optional.of(oldFaculty));
        when(facultyRepository.existsById(any())).thenReturn(true);

        mockMvc.perform(MockMvcRequestBuilders
                .delete("/faculty/{id}", id));
        verify(facultyRepository, times(0)).deleteById(id);
    }

    @Test
    @DisplayName("Удаление факультета отсутствующего в базе")
    void deleteFacultyNegativeTest() throws Exception {
        long id = 1;
        when(facultyRepository.existsById(id)).thenThrow(FacultyNotFoundException.class);
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/faculty/{id}", id))
                .andExpect(result -> assertInstanceOf(FacultyNotFoundException.class, result.getResolvedException()));
    }

    @Test
    @DisplayName("Вывод факультетов по названию и цвету")
    void findFacultiesByColorOrName() throws Exception {
        long id1 = 1L;
        long id2 = 2L;
        long id3 = 3L;
        String name = "Гриффиндор";
        String color = "красный";
        String nameOrColor = "красный";

        Faculty faculty1 = new Faculty();
        faculty1.setName(name);
        faculty1.setId(id1);
        faculty1.setColor(color);

        Faculty faculty2 = new Faculty();
        faculty2.setName(name);
        faculty2.setId(id2);
        faculty2.setColor(color);

        Faculty faculty3 = new Faculty();
        faculty3.setName(name);
        faculty3.setId(id3);
        faculty3.setColor(color);

        List<Faculty> facultyList = new ArrayList<>();
        facultyList.add(faculty1);
        facultyList.add(faculty2);
        facultyList.add(faculty3);
        when(facultyRepository.findAllByColorIgnoreCaseOrNameIgnoreCase(any(), any())).thenReturn(facultyList);

        System.out.println(facultyList);
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/faculty?nameOrColor=" + nameOrColor)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].color").value(color))
                .andExpect(jsonPath("$[1].color").value(color))
                .andExpect(jsonPath("$[2].color").value(color));

        verify(facultyRepository, only()).findAllByColorIgnoreCaseOrNameIgnoreCase(any(), any());
    }

    @Test
    @DisplayName("Вывод факультетов по цвету")
    void findFacultiesByColor() throws Exception {
        //data
        long id1 = 1L;
        long id2 = 2L;
        long id3 = 3L;
        String name1 = "Гриффиндор";
        String name2 = "Слизарин";
        String name3 = "Пуффендуй";
        String color = "жёлтый";

        Faculty faculty1 = new Faculty();
        faculty1.setName(name1);
        faculty1.setId(id1);
        faculty1.setColor(color);

        Faculty faculty2 = new Faculty();
        faculty2.setName(name2);
        faculty2.setId(id2);
        faculty2.setColor(color);

        Faculty faculty3 = new Faculty();
        faculty3.setName(name3);
        faculty3.setId(id3);
        faculty3.setColor(color);

        List<Faculty> facultyList = new ArrayList<>();
        facultyList.add(faculty1);
        facultyList.add(faculty2);
        facultyList.add(faculty3);
        when(facultyRepository.findAllByColor(any())).thenReturn(facultyList);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/faculty?color=" + color)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].color").value(color))
                .andExpect(jsonPath("$[1].color").value(color))
                .andExpect(jsonPath("$[2].color").value(color));

        verify(facultyRepository, only()).findAllByColor(any());
    }

    @Test
    @DisplayName("Вывод всех студентов с факультета")
    void findStudentsByFacultyId() throws Exception {

        long id = 1L;
        Faculty faculty = new Faculty();
        faculty.setId(id);
        faculty.setName("Гриффиндор");
        faculty.setColor("красный");

        Student student1 = new Student();
        student1.setId(1L);
        student1.setName("Гермиона Гренджер");
        student1.setAge(12);
        student1.setFaculty(faculty);

        Student student2 = new Student();
        student2.setId(2L);
        student2.setName("Гарри Поттерр");
        student2.setAge(11);
        student2.setFaculty(faculty);

        List<Student> studentList = new ArrayList<>();
        studentList.add(student1);
        studentList.add(student2);

        when(studentRepository.findAllByFaculty_Id(any(Long.class))).thenReturn(studentList);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/faculty/" + id + "/students"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
        verify(studentRepository, times(1)).findAllByFaculty_Id(id);
    }
}