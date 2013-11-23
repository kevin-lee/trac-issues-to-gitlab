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
import static java.util.Arrays.*;
import static org.elixirian.kommonlee.util.collect.Lists.*;
import static org.assertj.core.api.Assertions.*;
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
import com.lckymn.kevin.gitlab.api.GitLabProjectService;
import com.lckymn.kevin.gitlab.json.GitLabProject;
import com.lckymn.kevin.gitlab.json.GitLabProject.Namespace;
import com.lckymn.kevin.gitlab.json.GitLabProject.Owner;
import com.lckymn.kevin.http.HttpRequestForJsonSource;

/**
 * @author Lee, SeongHyun (Kevin)
 * @version 0.0.1 (2013-09-17)
 */
public class GitLabProjectServiceImplTest
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
  public final void testGitLabProjectServiceImpl() throws Exception
  {
    /* given */
    final HttpRequestForJsonSource expectedHttpRequestForJsonSource = mock(HttpRequestForJsonSource.class);
    final JsonStatham expectedJsonStatham = mock(JsonStatham.class);
    final String url = "test-value";
    final String expectedUrl = buildApiUrl(url);
    final String expectedProjectUrl = buildApiUrlForProjects(url);

    /* when */
    final GitLabProjectServiceImpl actual =
      new GitLabProjectServiceImpl(expectedHttpRequestForJsonSource, expectedJsonStatham, url);

    /* then */
    assertThat(actual.getHttpRequestForJsonSource()).isEqualTo(expectedHttpRequestForJsonSource);
    assertThat(actual.getJsonStatham()).isEqualTo(expectedJsonStatham);
    assertThat(actual.getUrl()).isEqualTo(expectedUrl);
    assertThat(actual.getProjectsUrl()).isEqualTo(expectedProjectUrl);
  }

  @Test
  public final void testGetAllGitLabProjects() throws Exception
  {
    /* given */
    final HttpRequestForJsonSource httpRequestForJsonSource = mock(HttpRequestForJsonSource.class);
    final String url = "test-value";
    final String privateToken = "testPrivateToken";

    final List<Integer> idList = asList(1, 2, 3);
    final List<String> descriptionList = asList("project A", "Project 2", "_3_");
    final List<String> defaultBranchList = asList(null, null, "1.0.0");
    final List<Boolean> isPublicList = asList(false, true, true);
    final List<String> sshUrlToRepoList =
      asList("git@gitlab.localhost:kevin/project-a.git", "git@gitlab.localhost:kevin/project-2.git",
          "git@gitlab.localhost:testuser/project3.git");
    final List<String> httpUrlToRepoList =
      asList("http://gitlab.localhost/kevin/project-a.git", "http://gitlab.localhost/kevin/project-2.git",
          "http://gitlab.localhost/testuser/project3.git");
    final List<String> webUrlList =
      asList("http://gitlab.localhost/kevin/project-a", "http://gitlab.localhost/kevin/project-2",
          "http://gitlab.localhost/testuser/project3");
    final List<Owner> ownerList =
      asList(new Owner(2, "kevin", "kevin.code@some.email.address", "Kevin Lee", "active", "2013-08-25T08:17:22Z"),
          new Owner(2, "kevin", "kevin.code@some.email.address", "Kevin Lee", "active", "2013-08-25T08:17:22Z"),
          new Owner(3, "testuser", "test@test.user", "Tester", "active", "2013-09-01T17:00:05Z"));
    final List<String> nameList = asList("project-a", "project-2", "project3");
    final List<String> nameWithNamespaceList =
      asList("Kevin Lee / project-a", "Kevin Lee / project-2", "Tester / project3");
    final List<String> pathList = asList("project-a", "project-2", "project3");
    final List<String> pathWithNamespaceList = asList("kevin/project-a", "kevin/project-2", "testuser/project3");
    final List<Boolean> issuesEnabledList = asList(true, true, true);
    final List<Boolean> mergeRequestsEnabledList = asList(true, true, true);
    final List<Boolean> wallEnabledList = asList(false, false, false);
    final List<Boolean> wikiEnabledList = asList(true, true, true);
    final List<Boolean> snippetsEnabledList = asList(false, false, false);
    final List<String> createdAtList = asList("2013-08-31T08:42:01Z", "2013-09-01T00:01:01Z", "2013-09-10T20:58:29Z");
    final List<String> lastActivityAtList =
      asList("2013-09-15T01:00:54Z", "2013-09-12T18:42:00Z", "2013-09-16T14:07:59Z");
    final List<Namespace> namespaceList =
      asList(new Namespace(2, "Kevin Lee", "2013-08-25T08:17:23Z", "", 2, "kevin", "2013-08-25T08:17:23Z"),
          new Namespace(2, "Kevin Lee", "2013-08-25T08:17:23Z", "", 2, "kevin", "2013-08-25T08:17:23Z"), new Namespace(
              3, "Tester", "2013-09-01T17:00:06Z", "", 3, "testuser", "2013-09-01T17:00:06Z"));

    final List<GitLabProject> expected = newArrayList();

    final int length = idList.size();
    for (int i = 0; i < length; i++)
    {
      final GitLabProject.Builder builder = GitLabProject.builder()
          .id(idList.get(i))
          .description(descriptionList.get(i))
          .defaultBranch(defaultBranchList.get(i));
      if (isPublicList.get(i))
      {
        builder.itIsPublic();
      }

      builder.sshUrlToRepo(sshUrlToRepoList.get(i))
          .httpUrlToRepo(httpUrlToRepoList.get(i))
          .webUrl(webUrlList.get(i))
          .owner(ownerList.get(i))
          .name(nameList.get(i))
          .nameWithNamespace(nameWithNamespaceList.get(i))
          .path(pathList.get(i))
          .pathWithNamespace(pathWithNamespaceList.get(i));

      if (issuesEnabledList.get(i))
      {
        builder.issuesEnabled();
      }

      if (mergeRequestsEnabledList.get(i))
      {
        builder.mergeRequestsEnabled();
      }

      if (wallEnabledList.get(i))
      {
        builder.wallEnabled();
      }

      if (wikiEnabledList.get(i))
      {
        builder.wikiEnabled();
      }

      if (snippetsEnabledList.get(i))
      {
        builder.snippetsEnabled();
      }

      expected.add(builder.createdAt(createdAtList.get(i))
          .lastActivityAt(lastActivityAtList.get(i))
          .namespace(namespaceList.get(i))
          .build());
    }

    final String json = jsonStatham.convertIntoJson(expected);

    final HttpRequest httpRequest = mock(HttpRequest.class);
    when(httpRequest.body()).thenReturn(json);

    final String projectsUrl = buildApiUrlForProjects(url);
    final String prepareUrlForIssues = prepareUrl(projectsUrl, privateToken);
    when(httpRequestForJsonSource.get(prepareUrlForIssues)).thenReturn(httpRequest);

    final GitLabProjectService gitLabProjectService =
      new GitLabProjectServiceImpl(httpRequestForJsonSource, jsonStatham, url);

    /* when */
    final List<GitLabProject> actual = gitLabProjectService.getAllGitLabProjects(privateToken);

    /* then */
    assertThat(actual).isEqualTo(expected);
  }

  @Test
  public final void testGetProjectByPathWithNamespace() throws Exception
  {
    /* given */
    final HttpRequestForJsonSource httpRequestForJsonSource = mock(HttpRequestForJsonSource.class);
    final String url = "test-value";
    final String privateToken = "testPrivateToken";

    final List<Integer> idList = asList(1, 2, 3);
    final List<String> descriptionList = asList("project A", "Project 2", "_3_");
    final List<String> defaultBranchList = asList(null, null, "1.0.0");
    final List<Boolean> isPublicList = asList(false, true, true);
    final List<String> sshUrlToRepoList =
      asList("git@gitlab.localhost:kevin/project-a.git", "git@gitlab.localhost:kevin/project-2.git",
          "git@gitlab.localhost:testuser/project3.git");
    final List<String> httpUrlToRepoList =
      asList("http://gitlab.localhost/kevin/project-a.git", "http://gitlab.localhost/kevin/project-2.git",
          "http://gitlab.localhost/testuser/project3.git");
    final List<String> webUrlList =
      asList("http://gitlab.localhost/kevin/project-a", "http://gitlab.localhost/kevin/project-2",
          "http://gitlab.localhost/testuser/project3");
    final List<Owner> ownerList =
      asList(new Owner(2, "kevin", "kevin.code@some.email.address", "Kevin Lee", "active", "2013-08-25T08:17:22Z"),
          new Owner(2, "kevin", "kevin.code@some.email.address", "Kevin Lee", "active", "2013-08-25T08:17:22Z"),
          new Owner(3, "testuser", "test@test.user", "Tester", "active", "2013-09-01T17:00:05Z"));
    final List<String> nameList = asList("project-a", "project-2", "project3");
    final List<String> nameWithNamespaceList =
      asList("Kevin Lee / project-a", "Kevin Lee / project-2", "Tester / project3");
    final List<String> pathList = asList("project-a", "project-2", "project3");
    final List<String> pathWithNamespaceList = asList("kevin/project-a", "kevin/project-2", "testuser/project3");
    final List<Boolean> issuesEnabledList = asList(true, true, true);
    final List<Boolean> mergeRequestsEnabledList = asList(true, true, true);
    final List<Boolean> wallEnabledList = asList(false, false, false);
    final List<Boolean> wikiEnabledList = asList(true, true, true);
    final List<Boolean> snippetsEnabledList = asList(false, false, false);
    final List<String> createdAtList = asList("2013-08-31T08:42:01Z", "2013-09-01T00:01:01Z", "2013-09-10T20:58:29Z");
    final List<String> lastActivityAtList =
      asList("2013-09-15T01:00:54Z", "2013-09-12T18:42:00Z", "2013-09-16T14:07:59Z");
    final List<Namespace> namespaceList =
      asList(new Namespace(2, "Kevin Lee", "2013-08-25T08:17:23Z", "", 2, "kevin", "2013-08-25T08:17:23Z"),
          new Namespace(2, "Kevin Lee", "2013-08-25T08:17:23Z", "", 2, "kevin", "2013-08-25T08:17:23Z"), new Namespace(
              3, "Tester", "2013-09-01T17:00:06Z", "", 3, "testuser", "2013-09-01T17:00:06Z"));

    final List<GitLabProject> expected = newArrayList();

    final int length = idList.size();
    for (int i = 0; i < length; i++)
    {
      final GitLabProject.Builder builder = GitLabProject.builder()
          .id(idList.get(i))
          .description(descriptionList.get(i))
          .defaultBranch(defaultBranchList.get(i));
      if (isPublicList.get(i))
      {
        builder.itIsPublic();
      }

      builder.sshUrlToRepo(sshUrlToRepoList.get(i))
          .httpUrlToRepo(httpUrlToRepoList.get(i))
          .webUrl(webUrlList.get(i))
          .owner(ownerList.get(i))
          .name(nameList.get(i))
          .nameWithNamespace(nameWithNamespaceList.get(i))
          .path(pathList.get(i))
          .pathWithNamespace(pathWithNamespaceList.get(i));

      if (issuesEnabledList.get(i))
      {
        builder.issuesEnabled();
      }

      if (mergeRequestsEnabledList.get(i))
      {
        builder.mergeRequestsEnabled();
      }

      if (wallEnabledList.get(i))
      {
        builder.wallEnabled();
      }

      if (wikiEnabledList.get(i))
      {
        builder.wikiEnabled();
      }

      if (snippetsEnabledList.get(i))
      {
        builder.snippetsEnabled();
      }

      expected.add(builder.createdAt(createdAtList.get(i))
          .lastActivityAt(lastActivityAtList.get(i))
          .namespace(namespaceList.get(i))
          .build());
    }

    final String json = jsonStatham.convertIntoJson(expected);

    final HttpRequest httpRequest = mock(HttpRequest.class);
    when(httpRequest.body()).thenReturn(json);

    final String projectsUrl = buildApiUrlForProjects(url);
    final String prepareUrlForIssues = prepareUrl(projectsUrl, privateToken);
    when(httpRequestForJsonSource.get(prepareUrlForIssues)).thenReturn(httpRequest);

    final GitLabProjectService gitLabProjectService =
      new GitLabProjectServiceImpl(httpRequestForJsonSource, jsonStatham, url);

    /* when */
    int i = 0;
    final GitLabProject actual =
      gitLabProjectService.getProjectByPathWithNamespace(privateToken, pathWithNamespaceList.get(i));

    /* then */
    assertThat(actual).isEqualTo(expected.get(i));

    /* when */
    i++;
    final GitLabProject actual2 =
      gitLabProjectService.getProjectByPathWithNamespace(privateToken, pathWithNamespaceList.get(i));

    /* then */
    assertThat(actual2).isEqualTo(expected.get(i));

    /* when */
    i++;
    final GitLabProject actual3 =
      gitLabProjectService.getProjectByPathWithNamespace(privateToken, pathWithNamespaceList.get(i));

    /* then */
    assertThat(actual3).isEqualTo(expected.get(i));
  }

}
