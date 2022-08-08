package util;

import com.regnosys.rosetta.common.util.ClassPathUtils;
import com.regnosys.rosetta.rosetta.*;
import com.regnosys.rosetta.rosetta.simple.Attribute;
import com.regnosys.rosetta.rosetta.simple.Data;
import com.regnosys.rosetta.rosetta.simple.Function;
import com.regnosys.rosetta.transgest.ModelLoaderImpl;
import org.eclipse.emf.common.util.EList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;
import java.util.*;

/**
 * TODO: Util needs to determine if types are used or not
 */
public class UnusedModelElementFinder {

    private static final Logger LOGGER = LoggerFactory.getLogger(UnusedModelElementFinder.class);
    private static final Set<String> listOfTypes = new HashSet<>();
    private static final Set<String>  listOfUsedTypes = new HashSet<>();
    private static final Set<String> listOfOrphanedTypes = new HashSet<>();

    private static List<RosettaModel> models;

    public static void main(String[] args) throws IOException {
        new UnusedModelElementFinder().run();
    }
    public void run() throws IOException {

        ModelLoaderImpl modelLoader = new ModelLoaderImpl(ClassPathUtils.findRosettaFilePaths().stream().map(ClassPathUtils::toUrl).toArray(URL[]::new));

        models = modelLoader.models();
        generateTypesList();

        LOGGER.info("{} Types found in Model ", listOfTypes.size());
        LOGGER.info("{} Types are used within Model", listOfUsedTypes.size());
        listOfOrphanedTypes.addAll(listOfTypes);
        listOfOrphanedTypes.removeAll(listOfUsedTypes);

        LOGGER.info("out of which {} are now orphaned types as listed below:", listOfOrphanedTypes.size());

        // LOGGER.info("orphaned Type: {}", orphanedTypes);
      //  Arrays.stream(listOfOrphanedTypes.toArray()).sorted()
       //        .forEach(System.out::println);

    }

    private String getQualifiedName(RosettaType type) {
        return type.getModel().getName() + "." + type.getName();
    }

    private void generateTypesList() {

        for (RosettaModel model : models) {
           // LOGGER.info("Processing namespace {}, containing {} model elements", model.getName(), model.getElements().size());

            model.getElements().stream()
                    .filter(Data.class::isInstance)
                    .map(Data.class::cast)
                    .forEach(dataType -> {
                        //LOGGER.info(" Processing data type: {}", getQualifiedName(dataType));
                       listOfTypes.add(getQualifiedName(dataType));
                        updatelistOfUsedTypes(dataType.getAttributes());
                    });

            model.getElements().stream()
                    .filter(RosettaEnumeration.class::isInstance)
                    .map(RosettaEnumeration.class::cast)
                    .forEach(enumeration -> {
                     //  LOGGER.info("Processing enumeration type {}", getQualifiedName(enumeration));
                        listOfTypes.add(getQualifiedName(enumeration));
                    });

            model.getElements().stream()
                    .filter(Function.class::isInstance)
                    .map(Function.class::cast)
                    .forEach(this::filterFunction);
        }

    }

    private void updatelistOfUsedTypes(EList<Attribute> attributes) {
        attributes.stream()
        .map(RosettaTyped::getType)
                .filter(t -> !(t instanceof RosettaBuiltinType))
                .forEach(attributeType -> {
                    // LOGGER.info(" Processing attribute type: {}", getQualifiedName(attributeType));
                   listOfUsedTypes.add(getQualifiedName(attributeType));
                });

    }

    private void filterFunction(Function function) {

       // LOGGER.info(" Processing function types {}.{}", function.getModel().getName(), function.getName());
        listOfTypes.add((function.getModel().getName().concat(".")).concat(function.getName()));

        updatelistOfUsedTypes(function.getInputs());
        if (function.getOutput() != null) {
            //   LOGGER.info("Processing output types used within function {}", getQualifiedName(function.getOutput().getType()));
            listOfUsedTypes.add(getQualifiedName(function.getOutput().getType()));
        }
        function.getShortcuts()
                .forEach(shortCut -> {
                    if (shortCut.eContainer() != null && shortCut.eContainer().eContainer() != null) {
                        Function subFunction = (Function) shortCut.eContainer();
                        //LOGGER.info(" Processing used function types {}.{}", subFunction.getModel().getName(), subFunction.getName());

                        listOfUsedTypes.add((subFunction.getModel().getName().concat(".")).concat(subFunction.getName()));
                        //filterFunction(subFunction);
                    }

                });

    }
}