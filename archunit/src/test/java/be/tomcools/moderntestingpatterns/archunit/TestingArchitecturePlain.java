package be.tomcools.moderntestingpatterns.archunit;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.methods;
import static com.tngtech.archunit.library.DependencyRules.NO_CLASSES_SHOULD_DEPEND_UPPER_PACKAGES;
import static com.tngtech.archunit.library.freeze.FreezingArchRule.freeze;

// Using ArchUnit
public class TestingArchitecturePlain {

    private final JavaClasses classes = new ClassFileImporter().importPackages("be.tomcools");

    @Test
    public void allResourceClassesShouldUseRestControllerAnnotation() {
        classes().that().haveSimpleNameEndingWith("Resource")
                .should()
                .beAnnotatedWith(RestController.class)
                .andShould()
                .notBeAnnotatedWith(Controller.class)
                .check(classes);
    }

    @Test
    public void allBeanProviderClassesShouldBeInConfigurationPackage() {
        freeze(methods().that().areAnnotatedWith(Bean.class)
                .should().beDeclaredInClassesThat()
                .resideInAPackage("be.tomcools.moderntestingpatterns.app.configuration"))
                .check(classes);
    }

    @Test
    public void noClassesShouldDependOnClassesOfHigherLevelPackage() {
        NO_CLASSES_SHOULD_DEPEND_UPPER_PACKAGES.check(classes);
    }
}
