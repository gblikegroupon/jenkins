package jenkins.model;

import hudson.model.FreeStyleBuild;
import hudson.model.Job;
import hudson.model.Run;
import hudson.model.FreeStyleProject;
import org.junit.Rule;
import org.junit.Test;
import org.jvnet.hudson.test.JenkinsRule;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.query.Query;

import java.util.List;

import static org.junit.Assert.*;

public class PersistenceTest {
    @Rule public JenkinsRule j = new JenkinsRule();


    /* configuration for this test has a mongotemplate defined */
    @Test
    public void testBuildMongoPersistence() throws Exception {
        Datastore ds = Jenkins.getInstance().getDatastore();
        ds.delete(ds.createQuery(Run.class));
        ds.delete(ds.createQuery(Job.class));

        FreeStyleProject job = j.createFreeStyleProject("test1");
        assertNotNull(job);
        j.buildAndAssertSuccess(job);
        FreeStyleBuild build = job.getBuildByNumber(1);
        assertNotNull(build);

        //ds.save(build.getProject());
        //ds.save(build);
        List<Run> runs = ds.find(Run.class).asList();
        List<Job> jobs = ds.find(Job.class).asList();

        assertTrue("Did not find any Runs", runs.size() > 0);
        assertTrue("Did not find any Jobs", jobs.size() > 0);
        //assertEquals("Did not set value test", 22, runs.get(0).test);
    }
}