package jenkins.model;

import hudson.model.FreeStyleBuild;
import hudson.model.Run;
import hudson.model.FreeStyleProject;
import jenkins.model.morphia.CustomMorphiaObjectFactory;
import org.junit.Rule;
import org.junit.Test;
import org.jvnet.hudson.test.JenkinsRule;
import org.jvnet.hudson.test.recipes.LocalData;
import org.mongodb.morphia.Datastore;
import java.util.List;

import static org.junit.Assert.*;

public class PersistenceTest {
  @Rule public JenkinsRule j = new JenkinsRule();


  /* configuration for this test has a mongotemplate defined */
  @Test
    @LocalData
    public void testBuildMongoPersistence() throws Exception {
      Datastore ds = Jenkins.getInstance().getDatastore();

      ds.delete(ds.createQuery(Run.class));

      FreeStyleProject job = j.createFreeStyleProject("test1");
      assertNotNull(job);
      j.buildAndAssertSuccess(job);
      FreeStyleBuild build = job.getBuildByNumber(1);
      assertNotNull(build);

      List<Run> runs = ds.find(Run.class).asList();

      //TODO check that the Run and Job found are the same ones we put in
      assertTrue("Did not find any Runs", runs.size() > 0);
      Run run = runs.get(0);
      assertNotNull(run.getParent());
    }
  @Test
    @LocalData
    public void testJenkinsMongoConfiguration() throws Exception {
      // Data config in
      assertEquals("localhost", Jenkins.getInstance().getDatastore().getMongo().getAddress().getHost());
      assertEquals("meganame", Jenkins.getInstance().getDatastore().getDB().getName());
      assertEquals(1337, Jenkins.getInstance().getDatastore().getMongo().getAddress().getPort());
    }
}

