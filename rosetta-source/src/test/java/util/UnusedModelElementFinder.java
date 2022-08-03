package util;

import com.regnosys.rosetta.RosettaStandaloneSetup;
import com.regnosys.rosetta.common.util.ClassPathUtils;
import com.regnosys.rosetta.rosetta.*;
import com.regnosys.rosetta.rosetta.simple.Data;
import com.regnosys.rosetta.rosetta.simple.Function;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.xtext.resource.XtextResourceSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

/**
 * TODO: Util needs to determine if types are used or not
 */
public class UnusedModelElementFinder {

    private static final Logger LOGGER = LoggerFactory.getLogger(UnusedModelElementFinder.class);

    public void run() throws IOException {
        LOGGER.debug("run");

        List<Path> expandedModelPaths = ClassPathUtils
                .findPathsFromClassPath(Arrays.asList("model", "cdm/rosetta"),
                        ".*\\.rosetta",
                        Optional.empty(),
                        this.getClass().getClassLoader());

        List<RosettaRootElement> rootElements = loadRosettaRootElements(expandedModelPaths);
        Set<RosettaModel> models = rootElements.stream().map(RosettaRootElement::getModel).collect(Collectors.toSet());

        for (RosettaModel model : models) {
            LOGGER.info("Processing namespace {}, containing {} model elements", model.getName(), model.getElements().size());

            model.getElements().stream()
                    .filter(Data.class::isInstance)
                    .map(Data.class::cast)
                    .forEach(dataType -> {
                        LOGGER.info("  Processing data type {}", getQualifiedName(dataType));
                        dataType.getAttributes().stream()
                                .map(RosettaTyped::getType)
                                .filter(t -> !RosettaBuiltinType.class.isInstance(t))
                                .forEach(attributeType -> {
                                    LOGGER.info("    Attribute type {}", getQualifiedName(attributeType));
                                });
                    });

            model.getElements().stream()
                    .filter(RosettaEnumeration.class::isInstance)
                    .map(RosettaEnumeration.class::cast)
                    .forEach(enumeration -> {
                        LOGGER.info("  Processing enumeration {}", getQualifiedName(enumeration));
                    });

            model.getElements().stream()
                    .filter(Function.class::isInstance)
                    .map(Function.class::cast)
                    .forEach(function -> {
                        LOGGER.info("  Processing function {}.{}", function.getModel().getName(), function.getName());
                    });
        }
    }

    private String getQualifiedName(RosettaType type) {
        return type.getModel().getName() + "." + type.getName();
    }

    private List<RosettaRootElement> loadRosettaRootElements(List<Path> expandedModelPaths) {
        LOGGER.debug("loadRosettaRootElements");

        RosettaStandaloneSetup.doSetup();
        ResourceSet resourceSet = createResourceSet(expandedModelPaths);

        List<RosettaRootElement> rootElements = resourceSet.getResources()
                .stream()
                .map(Resource::getContents)
                .flatMap(Collection::stream)
                .map(RosettaModel.class::cast)
                .filter(Objects::nonNull)
                .map(RosettaModel::getElements)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());

        LOGGER.info("Found {} root elements", rootElements.size());

        return rootElements;
    }

    private XtextResourceSet createResourceSet(List<Path> expandedModelPaths) {
        LOGGER.debug("createResourceSet");

        XtextResourceSet resourceSet = new XtextResourceSet();
        expandedModelPaths.forEach(f -> resourceSet.getResource(URI.createURI(f.toUri().toString(), true), true));
        return resourceSet;
    }

    public static void main(String[] args) throws IOException {
        new UnusedModelElementFinder().run();
    }
}