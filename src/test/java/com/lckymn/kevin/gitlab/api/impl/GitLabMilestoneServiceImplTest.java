/**
 * Copyright 2013 Lee, Seong Hyun (Kevin)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.lckymn.kevin.gitlab.api.impl;

import static com.lckymn.kevin.gitlab.api.GitLabApiUtil.*;
import static org.elixirian.kommonlee.util.collect.Lists.*;
import static org.elixirian.kommonlee.util.collect.Maps.*;
import static org.fest.assertions.api.Assertions.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.elixirian.jsonstatham.core.JsonStatham;
import org.elixirian.jsonstatham.core.reflect.ReflectionJsonStathams;
import org.elixirian.kommonlee.test.CauseCheckableExpectedException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InOrder;

import com.github.kevinsawicki.http.HttpRequest;
import com.lckymn.kevin.gitlab.api.GitLabMilestoneService;
import com.lckymn.kevin.gitlab.api.exception.GitLab404NotFoundMessageException;
import com.lckymn.kevin.gitlab.json.GitLabMilestone;
import com.lckymn.kevin.gitlab.json.GitLabMilestone.GitLabMilestoneForCreation;
import com.lckymn.kevin.http.HttpRequestForJsonSource;
import com.lckymn.kevin.util.DateAndTimeFormatUtil;

/**
 * @author Lee, SeongHyun (Kevin)
 * @version 0.0.1 (2013-09-01)
 */
public class GitLabMilestoneServiceImplTest
{
  @Rule
  public CauseCheckableExpectedException causeCheckableExpectedException = CauseCheckableExpectedException.none();

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
  public final void testGitLabMilestoneServiceImpl()
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
    assertThat(actual.getProjectsUrl()).isEqualTo(expectedUrl);
  }

  @Test
  public final void testGetAllGitLabMilestones()
  {
    /* given */
    final String url = "http://localhost/gitlab";
    final String privateToken = "testPrivateToken";
    final Integer id1 = 1;
    final Integer projectId1 = 1;
    final String title1 = "test title 1";
    final String description1 = "some description";
    final String dueDate1 = "2013-12-01T01:00:00Z";
    final String state1 = "active";
    final String createdAt1 = "2013-08-25T11:25:35Z";
    final String updatedAt1 = "2013-08-25T11:55:12Z";

    final Integer id2 = 2;
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
    final InOrder inOrder = inOrder(httpRequestForJsonSource, httpRequest);
    inOrder.verify(httpRequestForJsonSource, times(1))
        .get(apiUrl);
    inOrder.verify(httpRequest, times(1))
        .body();
  }

  @Test
  public final void testGetAllGitLabMilestonesWithNotExistingProjectId()
  {
    /* given */
    final String url = "http://localhost/gitlab";
    final String privateToken = "testPrivateToken";
    final Integer projectId1 = 1;

    final HttpRequest httpRequest = mock(HttpRequest.class);
    when(httpRequest.body()).thenReturn("{\"message\":\"404 Not Found\"}");

    final HttpRequestForJsonSource httpRequestForJsonSource = mock(HttpRequestForJsonSource.class);

    when(httpRequestForJsonSource.get(anyString())).thenReturn(httpRequest);

    final GitLabMilestoneService gitLabMilestoneService =
      new GitLabMilestoneServiceImpl(httpRequestForJsonSource, jsonStatham, url);

    /* expected */
    causeCheckableExpectedException.expect(GitLab404NotFoundMessageException.class);

    /* when */
    final List<GitLabMilestone> actual = gitLabMilestoneService.getAllGitLabMilestones(privateToken, projectId1);

    /* otherwise-fail */
    fail("GitLab404NotFoundMessageException was not thrown for the projectId which does not exist.\n" + "actual: "
        + actual);
  }

  @Test
  public final void testCreateMilestone()
  {
    /* given */
    final String url = "http://localhost/gitlab";
    final String privateToken = "testPrivateToken";
    final Integer id1 = 1;
    final Integer projectId1 = 1;
    final String title = "test title 1";
    final String description = "some description";
    final String dueDate = "2013-12-01";
    final String state1 = "active";
    final String createdAt1 = "2013-08-25T11:25:35Z";
    final String updatedAt1 = "2013-08-25T11:55:12Z";

    final String json =
      "{\"id\":" + id1 + ",\"project_id\":" + projectId1 + ",\"title\":\"" + title + "\",\"description\":\""
          + description + "\",\"due_date\":\"" + dueDate + "\",\"state\":\"" + state1 + "\",\"updated_at\":\""
          + updatedAt1 + "\",\"created_at\":\"" + createdAt1 + "\"}";

    final GitLabMilestone expected =
      new GitLabMilestone(id1, projectId1, title, description, dueDate, state1, createdAt1, updatedAt1);

    final Map<String, String> form = newHashMap();
    form.put("title", title);
    form.put("description", description);
    form.put("due_date", dueDate);

    final HttpRequest httpRequest = mock(HttpRequest.class);
    when(httpRequest.form(form)).thenReturn(httpRequest);
    when(httpRequest.body()).thenReturn(json);

    final HttpRequestForJsonSource httpRequestForJsonSource = mock(HttpRequestForJsonSource.class);

    final String apiUrl = prepareUrlForMilestones(buildApiUrlForProjects(url), privateToken, projectId1);
    when(httpRequestForJsonSource.post(apiUrl)).thenReturn(httpRequest);

    final GitLabMilestoneService gitLabMilestoneService =
      new GitLabMilestoneServiceImpl(httpRequestForJsonSource, jsonStatham, url);

    /* when */
    final GitLabMilestone actual =
      gitLabMilestoneService.createMilestone(privateToken, projectId1, new GitLabMilestoneForCreation(title,
          description, DateAndTimeFormatUtil.parseUtcDateIfNeitherNullNorEmpty(dueDate)));

    /* then */
    assertThat(actual).isEqualTo(expected);

    final InOrder inOrder = inOrder(httpRequestForJsonSource, httpRequest);
    inOrder.verify(httpRequestForJsonSource, times(1))
        .post(apiUrl);
    inOrder.verify(httpRequest, times(1))
        .form(form);
    inOrder.verify(httpRequest, times(1))
        .body();
  }

  @Test
  public final void testCreateMilestonesIfNotExist()
  {
    /* given */
    final String url = "http://localhost/gitlab";
    final String privateToken = "testPrivateToken";
    final Integer id1 = 1;
    final Integer projectId = 1;
    final String title1 = "test title 1";
    final String description1 = "some description";
    final String dueDate1 = "2013-12-01";
    final String state1 = "active";
    final String createdAt1 = "2013-08-25T11:25:35Z";
    final String updatedAt1 = "2013-08-25T11:55:12Z";

    final Integer id2 = 2;
    final String title2 = "title 2";
    final String description2 = "another description";
    final String dueDate2 = null;
    final String state2 = "active";
    final String createdAt2 = "2013-09-03T11:51:00Z";
    final String updatedAt2 = "2013-09-03T11:51:00Z";

    final Integer id3 = 3;
    final String title3 = "M 3";
    final String description3 = "The 3rd description";
    final String dueDate3 = null;
    final String state3 = "active";
    final String createdAt3 = "2013-09-03T11:55:00Z";
    final String updatedAt3 = "2013-09-03T11:55:00Z";

    final String json =
      "[{\"id\":" + id1 + ",\"project_id\":" + projectId + ",\"title\":\"" + title1 + "\",\"description\":\""
          + description1 + "\",\"due_date\":\"" + dueDate1 + "\",\"state\":\"" + state1 + "\",\"updated_at\":\""
          + updatedAt1 + "\",\"created_at\":\"" + createdAt1 + "\"}," + "{\"id\":" + id2 + ",\"project_id\":"
          + projectId + ",\"title\":\"" + title2 + "\",\"description\":\"" + description2 + "\",\"due_date\":"
          + dueDate2 + ",\"state\":\"" + state2 + "\",\"updated_at\":\"" + updatedAt2 + "\",\"created_at\":\""
          + createdAt2 + "\"}]";

    final String json2 =
      "{\"id\":" + id3 + ",\"project_id\":" + projectId + ",\"title\":\"" + title3 + "\",\"description\":\""
          + description3 + "\",\"due_date\":" + dueDate3 + ",\"state\":\"" + state3 + "\",\"updated_at\":\""
          + updatedAt3 + "\",\"created_at\":\"" + createdAt3 + "\"}";

    final List<GitLabMilestone> expected =
      Arrays.asList(new GitLabMilestone(id3, projectId, title3, description3, dueDate3, state3, createdAt3, updatedAt3));

    final Map<String, String> form = newHashMap();
    form.put("title", title3);
    form.put("description", description3);
    form.put("due_date", dueDate3);

    final HttpRequest httpRequestToGet = mock(HttpRequest.class);
    when(httpRequestToGet.body()).thenReturn(json);

    final HttpRequest httpRequestToPost = mock(HttpRequest.class);
    when(httpRequestToPost.form(form)).thenReturn(httpRequestToPost);
    when(httpRequestToPost.body()).thenReturn(json2);

    final HttpRequestForJsonSource httpRequestForJsonSource = mock(HttpRequestForJsonSource.class);

    final String apiUrl = prepareUrlForMilestones(buildApiUrlForProjects(url), privateToken, projectId);
    when(httpRequestForJsonSource.get(apiUrl)).thenReturn(httpRequestToGet);
    when(httpRequestForJsonSource.post(apiUrl)).thenReturn(httpRequestToPost);

    final GitLabMilestoneService gitLabMilestoneService =
      new GitLabMilestoneServiceImpl(httpRequestForJsonSource, jsonStatham, url);

    final List<GitLabMilestoneForCreation> list = newArrayList();
    list.add(new GitLabMilestoneForCreation(title3, description3,
        DateAndTimeFormatUtil.parseUtcDateIfNeitherNullNorEmpty(dueDate3)));

    /* when */
    final List<GitLabMilestone> actual =
      gitLabMilestoneService.createMilestonesIfNotExist(privateToken, projectId, list);

    /* then */
    assertThat(actual).isEqualTo(expected);

    final InOrder inOrder = inOrder(httpRequestForJsonSource, httpRequestToGet, httpRequestToPost);
    inOrder.verify(httpRequestForJsonSource, times(1))
        .get(apiUrl);
    inOrder.verify(httpRequestToGet, times(1))
        .body();
    inOrder.verify(httpRequestForJsonSource, times(1))
        .post(apiUrl);
    inOrder.verify(httpRequestToPost, times(1))
        .form(form);
    inOrder.verify(httpRequestToPost, times(1))
        .body();
  }
}
