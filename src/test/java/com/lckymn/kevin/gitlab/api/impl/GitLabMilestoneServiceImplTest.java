/**
 *
 */
package com.lckymn.kevin.gitlab.api.impl;

import static com.lckymn.kevin.gitlab.api.GitLabApiUtil.*;
import static org.elixirian.kommonlee.util.collect.Lists.*;
import static org.fest.assertions.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;

import org.elixirian.jsonstatham.core.JsonStatham;
import org.elixirian.jsonstatham.core.reflect.ReflectionJsonStathams;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.github.kevinsawicki.http.HttpRequest;
import com.lckymn.kevin.gitlab.api.GitLabMilestoneService;
import com.lckymn.kevin.gitlab.json.GitLabMilestone;
import com.lckymn.kevin.http.HttpRequestForJsonSource;

/**
 * @author Lee, SeongHyun (Kevin)
 * @version 0.0.1 (2013-09-01)
 */
public class GitLabMilestoneServiceImplTest
{
  private JsonStatham jsonStatham;

  /**
   * @throws java.lang.Exception
   */
  @BeforeClass
  public static void setUpBeforeClass() throws Exception
  {
  }

  /**
   * @throws java.lang.Exception
   */
  @AfterClass
  public static void tearDownAfterClass() throws Exception
  {
  }

  /**
   * @throws java.lang.Exception
   */
  @Before
  public void setUp() throws Exception
  {
    this.jsonStatham = ReflectionJsonStathams.newReflectionJsonStathamInAction();
  }

  /**
   * @throws java.lang.Exception
   */
  @After
  public void tearDown() throws Exception
  {
  }

  @Test
  public final void testGitLabMilestoneServiceImpl() throws Exception
  {
    /* given */
    final HttpRequestForJsonSource expectedHttpRequestForJsonSource = mock(HttpRequestForJsonSource.class);
    final JsonStatham expectedJsonStatham = mock(JsonStatham.class);
    final String url = "test-value";
    final String expectedUrl = buildApiUrlForProjects(url);

    /* when */
    final GitLabMilestoneServiceImpl actual =
      new GitLabMilestoneServiceImpl(expectedHttpRequestForJsonSource, expectedJsonStatham, url);

    /* then */
    assertThat(actual.getHttpRequestForJsonSource()).isEqualTo(expectedHttpRequestForJsonSource);
    assertThat(actual.getJsonStatham()).isEqualTo(expectedJsonStatham);
    assertThat(actual.getUrl()).isEqualTo(expectedUrl);
  }

  @Test
  public final void testGetAllGitLabMilestones() throws Exception
  {
    /* given */
    final String url = "http://localhost/gitlab";
    final Long id1 = 1L;
    final Long projectId1 = 1L;
    final String title1 = "test title 1";
    final String description1 = "some description";
    final String dueDate1 = "2013-12-01T01:00:00Z";
    final String state1 = "active";
    final String createdAt1 = "2013-08-25T11:25:35Z";
    final String updatedAt1 = "2013-08-25T11:55:12Z";

    final Long id2 = 2L;
    final String title2 = "test title 2";
    final String description2 = "another description";
    final String dueDate2 = null;
    final String state2 = "active";
    final String createdAt2 = "2013-08-25T11:25:35Z";
    final String updatedAt2 = "2013-08-25T11:25:35Z";
    final String json =
      "[{\"id\":" + id1 + ",\"project_id\":" + projectId1 + ",\"title\":\"" + title1 + "\",\"description\":\""
          + description1 + "\",\"due_date\":\"" + dueDate1 + "\",\"state\":\"" + state1 + "\",\"updated_at\":\""
          + updatedAt1 + "\",\"created_at\":\"" + createdAt1 + "\"}," +

          "{\"id\":" + id2 + ",\"project_id\":" + projectId1 + ",\"title\":\"" + title2 + "\",\"description\":\""
          + description2 + "\",\"due_date\":" + dueDate2 + ",\"state\":\"" + state2 + "\",\"updated_at\":\""
          + updatedAt2 + "\",\"created_at\":\"" + createdAt2 + "\"}" + "]";
    final String privateToken = "testPrivateToken";

    final List<GitLabMilestone> expected = newArrayList();
    expected.add(new GitLabMilestone(id1, projectId1, title1, description1, dueDate1, state1, createdAt1, updatedAt1));
    expected.add(new GitLabMilestone(id2, projectId1, title2, description2, dueDate2, state2, createdAt2, updatedAt2));

    final HttpRequest httpRequest = mock(HttpRequest.class);
    when(httpRequest.body()).thenReturn(json);
    final HttpRequestForJsonSource httpRequestForJsonSource = mock(HttpRequestForJsonSource.class);

    final String apiUrl = prepareUrlForMilestones(buildApiUrlForProjects(url), privateToken, projectId1);
    when(httpRequestForJsonSource.get(apiUrl)).thenReturn(httpRequest);

    final GitLabMilestoneService gitLabMilestoneService =
      new GitLabMilestoneServiceImpl(httpRequestForJsonSource, jsonStatham, url);

    /* when */
    final List<GitLabMilestone> actual = gitLabMilestoneService.getAllGitLabMilestones(privateToken, projectId1);

    /* then */
    assertThat(actual).isEqualTo(expected);
    verify(httpRequestForJsonSource, times(1)).get(apiUrl);
  }

}
