package ru.hogwarts.school.service;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Student;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Collection;

@Service
public class StudentService {
    HashMap<Long, Student> students = new HashMap<>();
    private long count = 0;

    public Student createStudent(Student student) {
        student.setId(count++);
        students.put(student.getId(), student);
        return student;
    }

    public Student readStudent(Long id) {
        return students.get(id);
    }

    public Student updateStudent(Student student) {
        if (!students.containsKey(student.getId())) {
            return null;
        }
        students.put(student.getId(), student);
        return student;
    }

    public Student deleteStudent(Long id) {
        return students.remove(id);
    }

    public Collection<Student> findByAge(int age) {
        ArrayList<Student> result = new ArrayList<>();
            for (Student student : students.values()) {
                if (student.getAge() == age) {
                    result.add(student);
                }
            }
        return result;
    }

}
