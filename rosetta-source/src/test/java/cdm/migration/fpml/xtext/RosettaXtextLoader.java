package cdm.migration.fpml.xtext;

import java.nio.file.Path;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.xtext.resource.XtextResourceSet;

import com.google.inject.Injector;
import com.regnosys.rosetta.RosettaStandaloneSetup;
import com.regnosys.rosetta.rosetta.RosettaModel;

public class RosettaXtextLoader {
    private final Injector injector;

    public RosettaXtextLoader() {
        this.injector = new RosettaStandaloneSetup().createInjectorAndDoEMFRegistration();
    }

    public RosettaModel loadModel(Path file) {
        XtextResourceSet resourceSet = injector.getInstance(XtextResourceSet.class);
        resourceSet.setClasspathURIContext(getClass());
        Resource resource = resourceSet.getResource(URI.createFileURI(file.toAbsolutePath().toString()), true);
        if (resource == null || resource.getContents().isEmpty()) {
            return null;
        }
        Object root = resource.getContents().get(0);
        if (root instanceof RosettaModel) {
            return (RosettaModel) root;
        }
        return null;
    }
}
