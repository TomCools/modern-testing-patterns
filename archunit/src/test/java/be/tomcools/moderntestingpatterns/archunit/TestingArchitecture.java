package be.tomcools.moderntestingpatterns.archunit;

import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.methods;
import static com.tngtech.archunit.library.DependencyRules.NO_CLASSES_SHOULD_DEPEND_UPPER_PACKAGES;
import static com.tngtech.archunit.library.freeze.FreezingArchRule.freeze;

// Using ArchUnit
@AnalyzeClasses(packages = "be.tomcools")
public class TestingArchitecture {

    @ArchTest
    static final ArchRule no_accesses_to_upper_package = NO_CLASSES_SHOULD_DEPEND_UPPER_PACKAGES;

    @ArchTest
    static ArchRule resource_classes_should_use_restcontroller_annotation =
            classes().that().haveSimpleNameEndingWith("Resource")
                    .should()
                    .beAnnotatedWith(RestController.class)
                    .andShould()
                    .notBeAnnotatedWith(Controller.class);
    @ArchTest
    static ArchRule bean_providers_should_be_in_configuration_ackage =
            freeze(methods().that().areAnnotatedWith(Bean.class)
                    .should().beDeclaredInClassesThat()
                    .resideInAPackage("be.tomcools.moderntestingpatterns.app.configuration"));

}
