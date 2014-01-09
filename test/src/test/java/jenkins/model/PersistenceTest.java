package jenkins.model;

import hudson.model.FreeStyleBuild;
import hudson.model.Run;
import jenkins.model.repositories.RunRepository;
import hudson.model.FreeStyleProject;
import org.junit.Rule;
import org.junit.Test;
import org.jvnet.hudson.test.JenkinsRule;
import org.jvnet.hudson.test.recipes.LocalData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.List;

import static org.junit.Assert.*;

public class PersistenceTest {
    @Rule public JenkinsRule j = new JenkinsRule();

    private RunRepository repository;

    /* configuration for this test has a mongotemplate defined */
    @Test
    public void testBuildMongoPersistence() throws Exception {
        ApplicationContext ctx = new ClassPathXmlApplicationContext(new String[] {"spring.xml"});
        repository = (RunRepository) ctx.getBean(RunRepository.class);

        FreeStyleProject job = j.createFreeStyleProject("test1");
        assertNotNull(job);
        j.buildAndAssertSuccess(job);
        FreeStyleBuild build = job.getBuildByNumber(1);
        assertNotNull(build);
        repository.save(build);

        List<Run> runs = repository.findAll();
        assertTrue("Did not find any Runs", runs.size() > 0);
    }
}
