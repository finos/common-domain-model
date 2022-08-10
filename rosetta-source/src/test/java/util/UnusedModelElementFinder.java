package util;

import com.google.common.collect.Lists;
import com.regnosys.rosetta.common.util.ClassPathUtils;
import com.regnosys.rosetta.rosetta.*;
import com.regnosys.rosetta.rosetta.simple.Attribute;
import com.regnosys.rosetta.rosetta.simple.Data;
import com.regnosys.rosetta.rosetta.simple.Function;
import com.regnosys.rosetta.transgest.ModelLoaderImpl;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;
import java.util.*;

/**
 * Util needs to determine if types are used or not
 */
public class UnusedModelElementFinder {

    private static final Logger LOGGER = LoggerFactory.getLogger(UnusedModelElementFinder.class);
    private Set<String> listOfTypes = new HashSet<>();
    private Set<String> listOfUsedTypes = new HashSet<>();
    private Set<String> listOfOrphanedTypes = new HashSet<>();
    private static List<RosettaModel> models;

    public UnusedModelElementFinder(ModelLoaderImpl modelLoader) {
        models = modelLoader.models();
    }

    public static void main(String[] args) throws IOException {
        new UnusedModelElementFinder(new ModelLoaderImpl(ClassPathUtils.findRosettaFilePaths().stream().map(ClassPathUtils::toUrl).toArray(URL[]::new))).run();
    }

    public void run() throws IOException {

        generateTypesList();

        LOGGER.trace("{} Types found in Model ", listOfTypes.size());
        LOGGER.trace("{} Types are used within Model", listOfUsedTypes.size());
        listOfOrphanedTypes.addAll(listOfTypes);
        listOfOrphanedTypes.removeAll(listOfUsedTypes);

        LOGGER.trace("out of which {} are now orphaned types as listed below:", listOfOrphanedTypes.size());

        // Arrays.stream(listOfOrphanedTypes.toArray()).sorted()
        //   .forEach(System.out::println);

    }

    private String getQualifiedName(RosettaType type) {
        return type.getModel().getName() + "." + type.getName();
    }

    private void generateTypesList() {

        for (RosettaModel model : models) {
            LOGGER.trace("Processing namespace {}, containing {} model elements", model.getName(), model.getElements().size());

            model.getElements().stream()
                    .filter(Data.class::isInstance)
                    .map(Data.class::cast)
                    .forEach(dataType -> {
                        LOGGER.trace(" Processing data type: {}", getQualifiedName(dataType));
                        listOfTypes.add(getQualifiedName(dataType));
                        TreeIterator<EObject> eObjectTreeIterator = dataType.eAllContents();
                        updateUsedTypes(eObjectTreeIterator);
                    });

            model.getElements().stream()
                    .filter(RosettaEnumeration.class::isInstance)
                    .map(RosettaEnumeration.class::cast)
                    .forEach(enumeration -> {
                        LOGGER.trace("Processing enumeration type {}", getQualifiedName(enumeration));
                        listOfTypes.add(getQualifiedName(enumeration));
                    });

            model.getElements().stream()
                    .filter(Function.class::isInstance)
                    .map(Function.class::cast)
                    .forEach(function -> {
                        LOGGER.trace(" Processing function types {}.{}", function.getModel().getName(), function.getName());
                        // listOfTypes.add((function.getModel().getName().concat(".")).concat(function.getName()));

                        TreeIterator<EObject> eObjectTreeIterator = function.eAllContents();
                        updateUsedTypes(eObjectTreeIterator);
                    });
        }

    }

    private void updateUsedTypes(TreeIterator<EObject> eObjectTreeIterator) {
        ArrayList<EObject> attributes = Lists.newArrayList(eObjectTreeIterator);

        attributes.stream()
                .filter(Attribute.class::isInstance)
                .map(Attribute.class::cast)
                .forEach(attribute -> listOfUsedTypes.add(getQualifiedName(attribute.getType())));

    }

    public Set<String> getListOfTypes() {
        return listOfTypes;
    }

    public Set<String> getListOfUsedTypes() {
        return listOfUsedTypes;
    }

    public Set<String> getListOfOrphanedTypes() {
        return listOfOrphanedTypes;
    }
}