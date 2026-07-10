package com.example.voting.adapters;

import static com.tngtech.archunit.library.Architectures.layeredArchitecture;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

@AnalyzeClasses(
        packages = "com.example.voting",
        importOptions = ImportOption.DoNotIncludeTests.class)
class ArchitectureTest {

    private static final String DOMAIN_LAYER = "com.example.voting.core..";
    private static final String APPLICATION_LAYER = "com.example.voting.usecases..";
    private static final String ADAPTERS_LAYER = "com.example.voting.adapters..";

    private static final String WEB_ADAPTER = "com.example.voting.adapters.web..";
    private static final String PERSISTENCE_ADAPTER = "com.example.voting.adapters.persistence..";

    private static final String INBOUND_PORTS = "com.example.voting.usecases.api..";
    private static final String OUTBOUND_PORTS = "com.example.voting.usecases.spi..";

    private static final String SPRING_FRAMEWORK = "org.springframework..";
    private static final String JAKARTA_FRAMEWORK = "jakarta..";
    private static final String HIBERNATE_FRAMEWORK = "org.hibernate..";

    @ArchTest
    static final ArchRule domain_should_not_depend_on_spring =
            noClasses()
                    .that().resideInAPackage(DOMAIN_LAYER)
                    .should().dependOnClassesThat().resideInAnyPackage(
                            SPRING_FRAMEWORK, JAKARTA_FRAMEWORK, HIBERNATE_FRAMEWORK)
                    .as("Domain must remain free of Spring and persistence frameworks");

    @ArchTest
    static final ArchRule domain_should_not_depend_on_adapters =
            noClasses()
                    .that().resideInAPackage(DOMAIN_LAYER)
                    .should().dependOnClassesThat().resideInAPackage(ADAPTERS_LAYER)
                    .as("Domain must not depend on any adapter");

    @ArchTest
    static final ArchRule domain_should_not_depend_on_application =
            noClasses()
                    .that().resideInAPackage(DOMAIN_LAYER)
                    .should().dependOnClassesThat().resideInAPackage(APPLICATION_LAYER)
                    .as("Domain is the innermost layer and depends on nothing outward");

    @ArchTest
    static final ArchRule application_should_not_depend_on_adapters =
            noClasses()
                    .that().resideInAPackage(APPLICATION_LAYER)
                    .should().dependOnClassesThat().resideInAPackage(ADAPTERS_LAYER)
                    .as("Application must not depend on any adapter");

    @ArchTest
    static final ArchRule application_should_not_depend_on_spring =
            noClasses()
                    .that().resideInAPackage(APPLICATION_LAYER)
                    .should().dependOnClassesThat().resideInAnyPackage(
                            SPRING_FRAMEWORK, JAKARTA_FRAMEWORK, HIBERNATE_FRAMEWORK)
                    .as("Application must remain free of Spring and persistence frameworks");

    @ArchTest
    static final ArchRule adapters_should_be_independent =
            noClasses()
                    .that().resideInAPackage(WEB_ADAPTER)
                    .should().dependOnClassesThat().resideInAPackage(PERSISTENCE_ADAPTER)
                    .as("Web adapter must not depend on the persistence adapter");

    @ArchTest
    static final ArchRule persistence_adapter_should_not_depend_on_web =
            noClasses()
                    .that().resideInAPackage(PERSISTENCE_ADAPTER)
                    .should().dependOnClassesThat().resideInAPackage(WEB_ADAPTER)
                    .as("Persistence adapter must not depend on the web adapter");

    @ArchTest
    static final ArchRule ports_should_be_interfaces =
            classes()
                    .that().resideInAnyPackage(INBOUND_PORTS, OUTBOUND_PORTS)
                    .and().haveSimpleNameEndingWith("Port")
                    .should().beInterfaces()
                    .as("Inbound and outbound ports must be interfaces");

    @ArchTest
    static final ArchRule hexagonal_layers_should_respect_inward_dependencies =
            layeredArchitecture()
                    .consideringOnlyDependenciesInLayers()
                    .layer("Domain").definedBy(DOMAIN_LAYER)
                    .layer("Application").definedBy(APPLICATION_LAYER)
                    .layer("Adapters").definedBy(ADAPTERS_LAYER)
                    .whereLayer("Adapters").mayNotBeAccessedByAnyLayer()
                    .whereLayer("Application").mayOnlyBeAccessedByLayers("Adapters")
                    .whereLayer("Domain").mayOnlyBeAccessedByLayers("Application", "Adapters")
                    .as("Dependencies must point inward: adapters -> application -> domain");
}
