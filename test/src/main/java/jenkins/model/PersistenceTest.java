package jenkins.model;

import hudson.model.FreeStyleBuild;
import hudson.model.Run;
import hudson.model.FreeStyleProject;
import jenkins.model.morphia.CustomMorphiaObjectFactory;
import org.junit.Rule;
import org.junit.Test;
import org.jvnet.hudson.test.JenkinsRule;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
import org.mongodb.morphia.mapping.Mapper;


import java.util.List;

import static org.junit.Assert.*;

public class PersistenceTest {
    @Rule public JenkinsRule j = new JenkinsRule();


    /* configuration for this test has a mongotemplate defined */
    @Test
    public void testBuildMongoPersistence() throws Exception {


        FreeStyleProject job = j.createFreeStyleProject("test1");
        assertNotNull(job);
        j.buildAndAssertSuccess(job);
        FreeStyleBuild build = job.getBuildByNumber(1);
        assertNotNull(build);

        Morphia morphia = new Morphia();
        Mapper mapper = morphia.getMapper();
        mapper.getOptions().objectFactory = new CustomMorphiaObjectFactory();

        Datastore ds = morphia.createDatastore("test");
        ds.save(build.getProject());
        ds.save(build);
        List<Run> runs = ds.find(Run.class).asList();

        assertTrue("Did not find any Runs", runs.size() > 0);
        //assertEquals("Did not set value test", 22, runs.get(0).test);
    }
}