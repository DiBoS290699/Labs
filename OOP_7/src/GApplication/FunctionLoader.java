package GApplication;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

public class FunctionLoader extends ClassLoader {

    public FunctionLoader() {}

    public Object loadFunction(String path, String className)
            throws NoSuchMethodException, IllegalAccessException, InvocationTargetException,
            InstantiationException, IOException {
        File file = new File(path);

        BufferedInputStream stream = new BufferedInputStream(new FileInputStream(file));
        byte[] bytes = new byte[(int) file.length()];
        stream.read(bytes);
        Class defineClass = defineClass(className, bytes, 0, bytes.length);
        return defineClass.getConstructor().newInstance();
    }
}
