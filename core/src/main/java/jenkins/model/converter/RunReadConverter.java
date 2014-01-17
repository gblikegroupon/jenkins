package jenkins.model.converter;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import hudson.model.Project;
import hudson.model.Run;
import jenkins.model.Jenkins;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.mongodb.core.convert.MongoConverter;

/**
 * Created by gblike on 1/15/14.
 */

public class RunReadConverter implements Converter<DBObject, Run> {
    
    @Override
    public Run convert(DBObject source) {
        String projectName = (String) source.get("projectName");

        // Find project with this name
        // TODO robustify this
        Project temp = null;
        for(Project project : Jenkins.getInstance().getProjects()) {
            if(projectName.equals(project.getName())) {
                temp = project;
            }
        }

        this.
        MongoConverter c = new MongoConverter();
        c.getConversionService();

        return null;
    }
}
