package com.example.fleamarket.api;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;

@AnalyzeClasses(packages = "com.example.fleamarket.api", importOptions = ImportOption.DoNotIncludeTests.class)
class ArchitectureTests {

    @ArchTest
    void layerDependencies(JavaClasses classes) {
        classes().that()
            .resideInAPackage("..controller..")
            .should()
            .onlyBeAccessed()
            .byAnyPackage("..controller..", "..config..")
            .check(classes);

        classes().that()
            .resideInAPackage("..service..")
            .should()
            .onlyBeAccessed()
            .byAnyPackage("..controller..", "..service..", "..config..")
            .check(classes);

        classes().that()
            .resideInAPackage("..repository..")
            .should()
            .onlyBeAccessed()
            .byAnyPackage("..service..", "..repository..", "..config..")
            .check(classes);

        classes().that()
            .resideInAPackage("..config..")
            .should()
            .onlyBeAccessed()
            .byAnyPackage("..config..")
            .check(classes);

    }

    @ArchTest
    void domainDependencies(JavaClasses classes) {

        classes().that()
            .resideInAPackage("..buy..")
            .should()
            .onlyBeAccessed()
            .byAnyPackage("..review..", "..buy..", "..config..")
            .check(classes);

        classes().that()
            .resideInAPackage("..review..")
            .should()
            .onlyBeAccessed()
            .byAnyPackage("..review..", "..config..")
            .check(classes);

        classes().that()
            .resideInAPackage("..shipping..")
            .should()
            .onlyBeAccessed()
            .byAnyPackage("..shipping..", "..config..")
            .check(classes);

        classes().that()
            .resideInAPackage("..sell..")
            .should()
            .onlyBeAccessed()
            .byAnyPackage("..catalog..", "..review..", "..shipping..", "..buy..", "..sell..", "..config..")
            .check(classes);

        classes().that()
            .resideInAPackage("..user..")
            .should()
            .onlyBeAccessed()
            .byAnyPackage("..shipping..", "..user..", "..review..", "..sell..", "..buy..", "..config..")
            .check(classes);
    }
}
