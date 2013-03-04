package com.navid.trafalgar.mod.windtunnel;

import com.navid.trafalgar.modapi.BuilderProvider;
import com.navid.trafalgar.model.Builder2;
import com.navid.trafalgar.model.BuilderInterface;
import java.util.Map;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.ClassPathResource;

/**
 *
 * @author alberto
 */
public class ModelRegisterer implements BuilderProvider{

    @Override
    public void registerModels(BeanFactory beanFactory) {
        XmlBeanFactory ctx = new XmlBeanFactory(new ClassPathResource("mod/windtunnel/builders-declaration.xml"), beanFactory);

        Map<String, BuilderInterface> builders = ctx.getBeansOfType(BuilderInterface.class);
        Builder2 builder2 = (Builder2) beanFactory.getBean("common.Builder");

        for (BuilderInterface currentBuilder : builders.values()) {
            builder2.registerBuilder(currentBuilder);
        }
    }

}
