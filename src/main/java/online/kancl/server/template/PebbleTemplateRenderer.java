package online.kancl.server.template;

import com.mitchellbosecke.pebble.PebbleEngine;
import com.mitchellbosecke.pebble.template.PebbleTemplate;
import online.kancl.server.Controller;

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class PebbleTemplateRenderer {

    private final PebbleEngine pebbleEngine;

    public PebbleTemplateRenderer(PebbleEngine pebbleEngine) {
        this.pebbleEngine = pebbleEngine;
    }

    public String renderDefaultControllerTemplate(Controller controller, Object... contexts) {
        String templateName = getDefaultControllerTemplateName(controller);
        return renderTemplate(templateName, contexts);
    }

    /**
     * @param contexts The contexts name (key of the HashMap) is the name of the given class with the first
     *                 letter lowercase (e.g. class: LoginInfo -> contexts name: loginInfo)
     */
    public String renderTemplate(String templateName, Object... contexts) {
        try {
            StringWriter stringWriter = new StringWriter();
            PebbleTemplate compiledTemplate = pebbleEngine.getTemplate(templateName);
            compiledTemplate.evaluate(stringWriter, createContextMap(contexts));

            return stringWriter.toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Map<String, Object> createContextMap(Object[] contexts) {
        HashMap<String, Object> contextMap = new HashMap<>();

        for (Object context : contexts) {
            contextMap.put(getContextName(context), context);
        }

        return contextMap;
    }

    String getDefaultControllerTemplateName(Controller controller) {
        String className = controller.getClass().getCanonicalName();
        String controllerSuffix = Controller.class.getSimpleName();

        if (!className.endsWith(controllerSuffix))
            throw new IllegalArgumentException("Expected class " + className + " to end with " + controllerSuffix);

        String classNameWithoutSuffix = className.substring(0, className.length() - controllerSuffix.length());
        String filePath = classNameWithoutSuffix.replace('.', '/');

        return filePath + ".peb";
    }

    private String getContextName(Object context) {
        String className = context.getClass().getSimpleName();
        if (className.isEmpty()) {
            throw new IllegalArgumentException("Context can't be an anonymous class.");
        }
        String firstLowercaseLetter = className.substring(0, 1).toLowerCase(Locale.ROOT);
        return firstLowercaseLetter + className.substring(1);
    }
}
