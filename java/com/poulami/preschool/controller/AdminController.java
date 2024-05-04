package com.poulami.preschool.controller;

import com.poulami.preschool.model.Courses;
import com.poulami.preschool.model.Person;
import com.poulami.preschool.model.PreClass;
import com.poulami.preschool.repository.CoursesRepository;
import com.poulami.preschool.repository.PersonRepository;
import com.poulami.preschool.repository.PreClassRepository;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Optional;

@Slf4j
@Controller
@RequestMapping("admin")
public class AdminController {

    @Autowired
    PreClassRepository preClassRepository;

    @Autowired
    PersonRepository personRepository;

    @Autowired
    CoursesRepository coursesRepository;

    @RequestMapping("/displayClasses")
    public ModelAndView displayClasses(Model model){
            List<PreClass> preClasses = preClassRepository.findAll();
            ModelAndView modelAndView = new ModelAndView("classes.html");
            modelAndView.addObject("preClasses",preClasses);
            modelAndView.addObject("preClass", new PreClass());
            return modelAndView;
    }

    @PostMapping("/addNewClass")
    public ModelAndView addNewClass(Model model,
                                    @ModelAttribute("preClass") PreClass preClass){
        preClassRepository.save(preClass);
        ModelAndView modelAndView = new ModelAndView("redirect:/admin/displayClasses");
        return modelAndView;
    }

    @RequestMapping("/deleteClass")
    public ModelAndView deleteClass(Model model,
                                    @RequestParam int id){
        Optional<PreClass> preClass = preClassRepository.findById(id);
        for(Person person : preClass.get().getPersons()){
            person.setPreClass(null);
            personRepository.save(person);
        }
        preClassRepository.deleteById(id);
        ModelAndView modelAndView = new ModelAndView("redirect:/admin/displayClasses");
        return modelAndView;
    }

    @GetMapping("/displayStudents")
    public ModelAndView displayStudents(Model model,
                                        @RequestParam int classId,
                                        HttpSession httpSession,
                                        @RequestParam(value = "error", required = false) String error){
        String errorMessage = null;
        ModelAndView modelAndView = new ModelAndView("students.html");
        Optional<PreClass> preClass = preClassRepository.findById(classId);
        modelAndView.addObject("preClass", preClass.get());
        modelAndView.addObject("person", new Person());
        httpSession.setAttribute("preClass", preClass.get());
        if(error != null){
            errorMessage = "Invalid Email entered!!";
            modelAndView.addObject("errorMessage", errorMessage);
        }
        return modelAndView;
    }
    @PostMapping("/addStudent")
    public ModelAndView addStudent(Model model,
                                   @ModelAttribute("person") Person person,
                                   HttpSession httpSession){
        ModelAndView modelAndView = new ModelAndView();
        PreClass preClass = (PreClass) httpSession.getAttribute("preClass");
        Person personEntity = personRepository.readByEmail(person.getEmail());
        if(personEntity == null || !(personEntity.getPersonId() > 0)){
            modelAndView.setViewName("redirect:/admin/displayStudents?classId="+preClass.getClassId()
                                    +"&error=true");
            return modelAndView;
        }
        personEntity.setPreClass(preClass);
        personRepository.save(personEntity);
        preClass.getPersons().add(personEntity);
        preClassRepository.save(preClass);
        modelAndView.setViewName("redirect:/admin/displayStudents?classId="+preClass.getClassId());
        return modelAndView;

    }

    @GetMapping("/deleteStudent")
    public ModelAndView deleteStudent(Model model,
                                      @RequestParam int personId, HttpSession session) {
        PreClass eazyClass = (PreClass) session.getAttribute("eazyClass");
        Optional<Person> person = personRepository.findById(personId);
        person.get().setPreClass(null);
        eazyClass.getPersons().remove(person.get());
        PreClass eazyClassSaved = preClassRepository.save(eazyClass);
        session.setAttribute("eazyClass",eazyClassSaved);
        ModelAndView modelAndView = new ModelAndView("redirect:/admin/displayStudents?classId="+eazyClass.getClassId());
        return modelAndView;
    }

    @GetMapping("/displayCourses")
    public ModelAndView displayCourses(Model model){
        List<Courses> courses = coursesRepository.findByOrderByName();
        ModelAndView modelAndView = new ModelAndView("courses_secure.html");
        modelAndView.addObject("courses", courses);
        model.addAttribute("course", new Courses());
        return modelAndView;
    }

    @PostMapping("/addNewCourse")
    public ModelAndView addNewCourse(Model model,
                                     @ModelAttribute("course") Courses course){
        ModelAndView modelAndView = new ModelAndView();
        coursesRepository.save(course);
        modelAndView.setViewName("redirect:/admin/displayCourses");
        return modelAndView;
    }

    @GetMapping("/viewStudents")
    public ModelAndView viewStudents(Model model,
                                     @RequestParam int id,
                                     HttpSession httpSession,
                                     @RequestParam(value = "error", required = false) String error){
        String errorMessage = null;
        ModelAndView modelAndView = new ModelAndView("course_students.html");
        Optional<Courses> courses = coursesRepository.findById(id);
        modelAndView.addObject("courses", courses.get());
        modelAndView.addObject("person",new Person());
        httpSession.setAttribute("courses",courses.get());
        if(error != null){
            errorMessage = "Invalid Email entered!!";
            modelAndView.addObject("errorMessage", errorMessage);
        }
        return modelAndView;

    }

    @PostMapping("/addStudentToCourse")
    public ModelAndView addStudentToCourse(Model model,
                                           @ModelAttribute("person") Person person,
                                           HttpSession httpSession){
        ModelAndView modelAndView = new ModelAndView();
        Courses courses = (Courses) httpSession.getAttribute("courses");
        Person personEntity = personRepository.readByEmail(person.getEmail());
        if(personEntity==null || !(personEntity.getPersonId()>0)){
            modelAndView.setViewName("redirect:/admin/viewStudents?id="+courses.getCourseId()
                                        +"&error=true");
            return modelAndView;
        }
        personEntity.getCourses().add(courses);
        courses.getPersons().add(personEntity);
        personRepository.save(personEntity);
        httpSession.setAttribute("courses",courses);
        modelAndView.setViewName("redirect:/admin/viewStudents?id="+courses.getCourseId());
        return modelAndView;
    }

    @GetMapping("/deleteStudentFromCourse")
    public ModelAndView deleteStudentFromCourse(Model model,
                                                @RequestParam int personId,
                                                HttpSession httpSession){
        Courses courses = (Courses) httpSession.getAttribute("courses");
        Optional<Person> person = personRepository.findById(personId);
        person.get().getCourses().remove(courses);
        courses.getPersons().remove(person);
        personRepository.save(person.get());
        httpSession.setAttribute("courses",courses);
        ModelAndView modelAndView = new ModelAndView("redirect:/admin/viewStudents?id="+
                                                    courses.getCourseId());
        return modelAndView;
    }
}
