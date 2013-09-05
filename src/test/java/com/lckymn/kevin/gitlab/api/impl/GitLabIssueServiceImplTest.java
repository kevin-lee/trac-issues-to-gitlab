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
import static org.fest.assertions.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.elixirian.jsonstatham.core.JsonStatham;
import org.elixirian.jsonstatham.core.reflect.ReflectionJsonStathams;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.github.kevinsawicki.http.HttpRequest;
import com.lckymn.kevin.gitlab.api.GitLabIssueService;
import com.lckymn.kevin.gitlab.json.GitLabIssue;
import com.lckymn.kevin.gitlab.json.GitLabIssue.Milestone;
import com.lckymn.kevin.gitlab.json.GitLabIssue.User;
import com.lckymn.kevin.http.HttpRequestForJsonSource;

/**
 * @author Lee, SeongHyun (Kevin)
 * @version 0.0.1 (2013-09-04)
 */
public class GitLabIssueServiceImplTest
{
  private JsonStatham jsonStatham;

  @BeforeClass
  public static void setUpBeforeClass() throws Exception
  {
  }

  @AfterClass
  public static void tearDownAfterClass() throws Exception
  {
  }

  @Before
  public void setUp() throws Exception
  {
    this.jsonStatham = ReflectionJsonStathams.newReflectionJsonStathamInAction();
  }

  @After
  public void tearDown() throws Exception
  {
  }

  @Test
  public final void testGitLabIssueServiceImpl() throws Exception
  {
    /* given */
    final HttpRequestForJsonSource expectedHttpRequestForJsonSource = mock(HttpRequestForJsonSource.class);
    final JsonStatham expectedJsonStatham = mock(JsonStatham.class);
    final String url = "test-value";
    final String expectedUrl = buildApiUrl(url);
    final String expectedProjectUrl = buildApiUrlForProjects(url);

    /* when */
    final GitLabIssueServiceImpl actual =
      new GitLabIssueServiceImpl(expectedHttpRequestForJsonSource, expectedJsonStatham, url);

    /* then */
    assertThat(actual.getHttpRequestForJsonSource()).isEqualTo(expectedHttpRequestForJsonSource);
    assertThat(actual.getJsonStatham()).isEqualTo(expectedJsonStatham);
    assertThat(actual.getUrl()).isEqualTo(expectedUrl);
    assertThat(actual.getProjectUrl()).isEqualTo(expectedProjectUrl);
  }

  @Test
  public final void testGetAllIssues() throws Exception
  {
    /* given */
    final HttpRequestForJsonSource httpRequestForJsonSource = mock(HttpRequestForJsonSource.class);
    final String url = "test-value";

    final Long projectId = 1L;
    final String privateToken = "testPrivateToken";

    final Long id1 = 1L;
    final String title1 = "issue 1";
    final String description1 = "some test description";
    final String labels1 = "";
    final Milestone milestone1 = null;
    final User assignee1 = null;

    final Long author1Id = 2L;
    final String author1Username = "kevinlee";
    final String author1Email = "kevinlee@some.email.address";
    final String author1Name = "Kevin Lee";
    final String author1State = "active";
    final String author1CreatedAt = "2013-08-25T08:17:22Z";
    final String author1 =
      "{\"id\":" + author1Id + ",\"username\":\"" + author1Username + "\",\"email\":\"" + author1Email
          + "\",\"name\":\"" + author1Name + "\",\"state\":\"" + author1State + "\",\"created_at\":\""
          + author1CreatedAt + "\"}";

    final String state1 = "opened";
    final String createdAt1 = "2013-09-01T00:16:11Z";
    final String updatedAt1 = "2013-09-01T00:16:11Z";

    final Long id2 = 2L;
    final String title2 = "test issue 2";
    final String description2 = "test2";
    final String[] labels2 = { "task", "another" };

    final Long milestone2Id = 2L;
    final String milestone2Title = "some milestone title 2";
    final String milestone2Description = "Test milestone";
    final String milestone2DueDate = "2013-11-01";
    final String milestone2State = "active";
    final String milestone2UpdatedAt = "2013-09-01T12:10:40Z";
    final String milestone2CreatedAt = "2013-09-03T15:44:40Z";

    final Long assignee2Id = 2L;
    final String assignee2Username = "kevinlee";
    final String assignee2Email = "kevinlee@some.email.address";
    final String assignee2Name = "Kevin Lee";
    final String assignee2State = "active";
    final String assignee2CreatedAt = "2013-08-25T08:17:22Z";
    final String assignee2 =
      "{\"id\":" + assignee2Id + ",\"username\":\"" + assignee2Username + "\",\"email\":\"" + assignee2Email
          + "\",\"name\":\"" + assignee2Name + "\",\"state\":\"" + assignee2State + "\",\"created_at\":\""
          + assignee2CreatedAt + "\"}";

    final Long author2Id = 2L;
    final String author2Username = "kevinlee";
    final String author2Email = "kevinlee@some.email.address";
    final String author2Name = "Kevin Lee";
    final String author2State = "active";
    final String author2CreatedAt = "2013-08-25T08:17:22Z";
    final String author2 =
      "{\"id\":" + author2Id + ",\"username\":\"" + author2Username + "\",\"email\":\"" + author2Email
          + "\",\"name\":\"" + author2Name + "\",\"state\":\"" + author2State + "\",\"created_at\":\""
          + author2CreatedAt + "\"}";

    final String state2 = "opened";

    final String createdAt2 = "2013-09-01T01:16:11Z";
    final String updatedAt2 = "2013-09-03T10:45:52Z";

    final String json =
      "[{\"id\":" + id1 + ",\"project_id\":" + projectId + ",\"title\":\"" + title1 + "\",\"description\":\""
          + description1 + "\",\"labels\":[" + labels1 + "],\"milestone\":" + milestone1 + ",\"assignee\":" + assignee1
          + ",\"author\":" + author1 + ",\"state\":\"" + state1 + "\",\"updated_at\":\"" + updatedAt1
          + "\",\"created_at\":\"" + createdAt1 + "\"}," + "{\"id\":" + id2 + ",\"project_id\":" + projectId
          + ",\"title\":\"" + title2 + "\",\"description\":\"" + description2 + "\",\"labels\":[\"" + labels2[0]
          + "\", \"" + labels2[1] + "\"],\"milestone\":{\"id\":" + milestone2Id + ",\"project_id\":" + projectId
          + ",\"title\":\"" + milestone2Title + "\",\"description\":\"" + milestone2Description + "\",\"due_date\":\""
          + milestone2DueDate + "\",\"state\":\"" + milestone2State + "\",\"updated_at\":\"" + milestone2UpdatedAt
          + "\",\"created_at\":\"" + milestone2CreatedAt + "\"},\"assignee\":" + assignee2 + ",\"author\":" + author2
          + ",\"state\":\"" + state2 + "\",\"updated_at\":\"" + updatedAt2 + "\",\"created_at\":\"" + createdAt2
          + "\"}]";

    final HttpRequest httpRequest = mock(HttpRequest.class);
    when(httpRequest.body()).thenReturn(json);

    final String prepareUrlForIssues = prepareUrlForIssues(buildApiUrlForProjects(url), privateToken, projectId);
    when(httpRequestForJsonSource.get(prepareUrlForIssues)).thenReturn(httpRequest);

    final List<GitLabIssue> expected = newArrayList();
    expected.add(new GitLabIssue(id1, projectId, title1, description1, Collections.<String> emptyList(), milestone1,
        new User(author1Id, author1Username, author1Email, author1Name, author1State, author1CreatedAt), assignee1,
        state1, createdAt1, updatedAt1));

    expected.add(new GitLabIssue(id2, projectId, title2, description2, Arrays.asList(labels2), new Milestone(
        milestone2Id, projectId, milestone2Title, milestone2Description, milestone2DueDate, milestone2State,
        milestone2CreatedAt, milestone2UpdatedAt), new User(author2Id, author2Username, author2Email, author2Name,
        author2State, author2CreatedAt), new User(assignee2Id, assignee2Username, assignee2Email, assignee2Name,
        assignee2State, assignee2CreatedAt), state2, createdAt2, updatedAt2));

    final GitLabIssueService gitLabIssueService =
      new GitLabIssueServiceImpl(httpRequestForJsonSource, jsonStatham, url);

    /* when */
    final List<GitLabIssue> actual = gitLabIssueService.getAllIssues(privateToken, projectId);

    /* then */
    assertThat(actual).isEqualTo(expected);
  }
}
