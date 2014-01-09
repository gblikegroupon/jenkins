package jenkins.model.repositories;

import hudson.model.Run;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Created by gblike on 1/7/14.
 */
public interface RunRepository extends MongoRepository<Run, String>{
    public Run findById(String id);
}
