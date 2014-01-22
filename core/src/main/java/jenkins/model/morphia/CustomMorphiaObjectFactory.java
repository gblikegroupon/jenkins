package jenkins.model.morphia;

import org.mongodb.morphia.mapping.MappingException;
import org.objenesis.Objenesis;
import org.objenesis.ObjenesisStd;
import java.lang.reflect.Constructor;

import org.mongodb.morphia.mapping.DefaultCreator;

/*
 * Credit Johan Haleby
 * http://www.jayway.com/2012/02/28/configure-morphia-to-work-without-a-default-constructor/
 */

public class CustomMorphiaObjectFactory extends DefaultCreator {
    Objenesis objenesis;
    public CustomMorphiaObjectFactory(){
        this.objenesis = new ObjenesisStd();
    }


    @Override
    public Object createInstance(Class clazz) {
        try {
            final Constructor constructor = getNoArgsConstructor(clazz);
            if(constructor != null) {
                return constructor.newInstance();
            }
            try {
                return objenesis.newInstance(clazz);
            } catch (Exception e) {
                throw new MappingException("Failed to instantiate " + clazz.getName(), e);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Constructor getNoArgsConstructor(final Class ctorType) {
        try {
            Constructor ctor = ctorType.getDeclaredConstructor();
            ctor.setAccessible(true);
            return ctor;
        } catch (NoSuchMethodException e) {
            return null;
        }
    }
}