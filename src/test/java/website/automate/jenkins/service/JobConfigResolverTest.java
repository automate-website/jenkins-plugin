package website.automate.jenkins.service;

import static java.util.Collections.singletonMap;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class JobConfigResolverTest {

  private static final String RESOLUTION = "640x480x16";

  private static final String BOX_ID = "ubuntu14/firefox";

  private static final String TIMEOUT_STR = "5.0";

  private static final Double TIMEOUT = 5.0;

  private JobConfigResolver jobConfigResolver = JobConfigResolver.getInstance();

  @Test
  public void resolutionIsResolved() {
    assertThat(jobConfigResolver.resolve(singletonMap("website.automate.resolution", RESOLUTION))
        .getResolution(), is(RESOLUTION));
  }

  @Test
  public void timeoutIsResolved() {
    assertThat(jobConfigResolver.resolve(singletonMap("website.automate.timeout", TIMEOUT_STR))
        .getTimeout(), is(TIMEOUT));
  }

  @Test
  public void boxIdIsResolved() {
    assertThat(jobConfigResolver.resolve(singletonMap("website.automate.box.id", BOX_ID)).getBoxId(),
        is(BOX_ID));
  }

  @Test
  public void contextParameterAreResolved() {
    assertThat(jobConfigResolver.resolve(singletonMap("website.automate.context.x", "y"))
        .getContext().get("x"), is("y"));
  }
}
