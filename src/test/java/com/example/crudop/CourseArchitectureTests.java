package com.example.crudop;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.domain.JavaModifier;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.fields;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

@SpringBootTest
public class CourseArchitectureTests {

    private final JavaClasses importedClasses = new ClassFileImporter()
            .importPackages("com.example.crudop");

    @Test
    void controllersShouldBeAnnotated() {
        classes().that().resideInAPackage("..controller..")
                .should().beAnnotatedWith(RestController.class)
                .check(importedClasses);
    }

    @Test
    void controllersShouldHaveProperName() {
        classes().that().resideInAPackage("..controller..")
                .should().haveSimpleNameEndingWith("Controller")
                .check(importedClasses);
    }

    @Test
    void servicesShouldBeAnnotated() {
        classes().that().resideInAPackage("..service..")
                .should().beAnnotatedWith(Service.class)
                .check(importedClasses);
    }

    @Test
    void servicesShouldHaveProperName() {
        classes().that().resideInAPackage("..service..")
                .should().haveSimpleNameEndingWith("Service")
                .check(importedClasses);
    }

    @Test
    void repositoriesShouldBeInterfaces() {
        classes().that().resideInAPackage("..repository..")
                .should().beInterfaces()
                .check(importedClasses);
    }

    @Test
    void repositoriesShouldBeAnnotated() {
        classes().that().resideInAPackage("..repository..")
                .should().beAnnotatedWith(Repository.class)
                .check(importedClasses);
    }

    @Test
    void controllersShouldNotDependOnControllers() {
        noClasses().that().resideInAPackage("..controller..")
                .should().dependOnClassesThat().resideInAPackage("..controller..")
                .check(importedClasses);
    }

    @Test
    void servicesShouldNotDependOnControllers() {
        noClasses().that().resideInAPackage("..service..")
                .should().dependOnClassesThat().resideInAPackage("..controller..")
                .check(importedClasses);
    }

    @Test
    void repositoriesShouldNotDependOnServices() {
        noClasses().that().resideInAPackage("..repository..")
                .should().dependOnClassesThat().resideInAPackage("..service..")
                .check(importedClasses);
    }

    @Test
    void controllersDependencies() {
        classes().that().resideInAPackage("..controller..")
                .should().onlyDependOnClassesThat()
                .resideInAnyPackage("..service..", "..model..", "..request..", "java..", "javax..",
                        "org.springframework..", "lombok..")
                .check(importedClasses);
    }

    @Test
    void servicesDependencies() {
        classes().that().resideInAPackage("..service..")
                .should().onlyDependOnClassesThat()
                .resideInAnyPackage("..service..", "..repository..", "..model..", "..request..", "java..",
                        "javax..", "org.springframework..", "jakarta.annotation..", "lombok..")
                .check(importedClasses);
    }

    @Test
    void repositoriesDependencies() {
        classes().that().resideInAPackage("..repository..")
                .should().onlyDependOnClassesThat()
                .resideInAnyPackage("..model..", "java..", "javax..", "org.springframework..",
                        "org.springframework.data..", "lombok..")
                .check(importedClasses);
    }

    @Test
    void controllersShouldNotHaveAutowiredFields() {
        noClasses().that().resideInAPackage("..controller..")
                .should().beAnnotatedWith(org.springframework.beans.factory.annotation.Autowired.class)
                .check(importedClasses);
    }

    @Test
    void servicesShouldNotHaveAutowiredFields() {
        noClasses().that().resideInAPackage("..service..")
                .should().beAnnotatedWith(org.springframework.beans.factory.annotation.Autowired.class)
                .check(importedClasses);
    }

    @Test
    void repositoriesShouldNotDependOnControllers() {
        noClasses().that().resideInAPackage("..repository..")
                .should().dependOnClassesThat().resideInAPackage("..controller..")
                .check(importedClasses);
    }

    @Test
    void modelsShouldBeInModelPackage() {
        classes().that().haveSimpleNameEndingWith("Course")
                .should().resideInAPackage("..model..")
                .check(importedClasses);
    }

    @Test
    void modelClassesShouldHaveProperNames() {
        classes().that().resideInAPackage("..model..")
                .should().haveSimpleNameEndingWith("Course")
                .check(importedClasses);
    }

    @Test
    void servicesFieldsShouldNotBePublic() {
        fields().that().areDeclaredInClassesThat().resideInAPackage("..service..")
                .should().notHaveModifier(JavaModifier.PUBLIC)
                .check(importedClasses);
    }

    @Test
    void servicesShouldNotDependOnDto() {
        noClasses().that().resideInAPackage("..service..")
                .should().dependOnClassesThat().resideInAPackage("..dto..")
                .check(importedClasses);
    }

    @Test
    void controllersShouldNotDependOnRepository() {
        noClasses().that().resideInAPackage("..controller..")
                .should().dependOnClassesThat().resideInAPackage("..repository..")
                .check(importedClasses);
    }
}
