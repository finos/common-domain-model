package cdm.base.staticdata.codelist;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.io.Resources;
import com.regnosys.rosetta.common.hashing.ReferenceResolverProcessStep;
import com.regnosys.rosetta.common.serialisation.RosettaObjectMapper;
import com.rosetta.model.lib.RosettaModelObject;
import org.isda.cdm.processor.CdmReferenceConfig;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

@Deprecated
public class JsonCodeLoader {
    static JsonCodeLoader singleton;
    private static final Map<String, RosettaModelObject > cache = new HashMap<>();
    private static ObjectMapper rosettaObjectMapper = null;


    static JsonCodeLoader getInstance() {
        if (singleton == null) singleton = new JsonCodeLoader();
        return singleton;
    }

    public CodeList loadCodelist (String domain) throws IOException {
        String filename = "reference-data/" + domain + ".json";
        CodeList obj = getObject(CodeList.class, filename);
        return obj;

    }

    public  synchronized  <T extends RosettaModelObject> T getObject(Class<T> clazz, String resourceName) throws IOException {
        if (!cache.containsKey(resourceName)) {
            RosettaModelObject object = loadObject(clazz, resourceName);
            cache.put(resourceName, object);
        }
//        logger.log("Retrieving " + resourceName + " from cache");
        return (T) cache.get(resourceName);
    }

    public ObjectMapper getMapper () {
        if (rosettaObjectMapper == null) {
            rosettaObjectMapper =  RosettaObjectMapper.getNewMinimalRosettaObjectMapper();
            long start = System.currentTimeMillis();
//            logger.log("ignore classes took "+ (System.currentTimeMillis()-start) + "ms");
        }
        return rosettaObjectMapper;
    }



    private   <T extends RosettaModelObject> T loadObject(Class<T> clazz, String resourceName) throws IOException {

        Object object = deserializeJson(clazz, resourceName);
        if (object instanceof RosettaModelObject) {
//            logger.log("processing trade");
            RosettaModelObject builder = ((RosettaModelObject) object).toBuilder();
            new ReferenceResolverProcessStep(CdmReferenceConfig.get()).runProcessStep(builder.getType(), builder);
//            logger.log("finished processing trade");
            return (T) builder.build();
        }
        return null;
    }


    private <T extends RosettaModelObject> Object deserializeJson(Class<T> clazz, String resourceName) throws IOException {
        String json = getJson(resourceName);
//        logger.log("loaded JSON for "+ resourceName);
        //return deserializeJsonGson(clazz,json);
        return deserializeJsonJackson(clazz,json);

    }

    private <T extends Object> Object deserializeJsonJackson(Class<T> clazz, String json) throws IOException {
        long start = System.currentTimeMillis();
        Object object = getMapper().readValue(json, clazz);
        long end = System.currentTimeMillis();
        long dur = end - start;
//        logger.log("read into memory using jackson, took "+ dur +"ms");
        return object;
    }


    public static String getJson(String resourceName) throws IOException {
        URL url = Resources.getResource(resourceName);
        String json = Resources.toString(url, Charset.defaultCharset());
        //logger.log(json);
        return json;
    }

}
