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
package com.lckymn.kevin.trac2gitlab.impl;

import static java.util.Arrays.*;
import static org.elixirian.kommonlee.util.collect.Maps.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.lckymn.kevin.gitlab.json.GitLabIssue.GitLabIssueForCreation;
import com.lckymn.kevin.gitlab.json.GitLabMilestone;
import com.lckymn.kevin.gitlab.json.GitLabProject;
import com.lckymn.kevin.gitlab.json.GitLabProject.Namespace;
import com.lckymn.kevin.gitlab.json.GitLabProject.Owner;
import com.lckymn.kevin.gitlab.json.GitLabUser;
import com.lckymn.kevin.trac.json.TracIssue;
import com.lckymn.kevin.trac2gitlab.Trac2GitLabIssueConverter;
import com.lckymn.kevin.util.DateAndTimeFormatUtil;

/**
 * @author Lee, SeongHyun (Kevin)
 * @version 0.0.1 (2013-09-17)
 */
public class Trac2GitLabIssueConverterImplTest
{
  private Map<String, String> labelMap;

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
    final Map<String, String> map = newHashMap();
    map.put("bug", "bug");
    map.put("task", "task");
    map.put("new feature", "feature");
    map.put("enhancement", "enhancement");
    map.put("refactoring", "refactoring");

    map.put("critical", "critical");
    map.put("blocker", "blocker");
    map.put("critical", "critical");
    map.put("major", "major");
    map.put("minor", "minor");
    map.put("trivial", "trivial");

    map.put("highest", "highest");
    map.put("high", "high");
    map.put("normal", "normal");
    map.put("low", "low");
    map.put("lowest", "lowest");

    map.put("fixed", "fixed");
    map.put("invalid", "invalid");
    map.put("wontfix", "wontfix");
    map.put("duplicate", "duplicate");
    map.put("worksforme", "worksforme");
    this.labelMap = Collections.unmodifiableMap(map);
  }

  @After
  public void tearDown() throws Exception
  {
  }

  @Test
  public final void testConvert() throws Exception
  {
    /* given */
    final GitLabProject gitLabProject;
    {
      final Integer id = 1;
      final String description = "project A";
      final String defaultBranch = null;
      final Boolean isPublic = false;
      final String sshUrlToRepo = "git@gitlab.localhost:kevin/project-a.git";
      final String httpUrlToRepo = "http://gitlab.localhost/kevin/project-a.git";
      final String webUrl = "http://gitlab.localhost/kevin/project-a";
      final Owner owner =
        new Owner(2, "kevin", "kevin.code@some.email.address", "Kevin Lee", "active", "2013-08-25T08:17:22Z");
      final String name = "project-a";
      final String nameWithNamespace = "Kevin Lee / project-a";
      final String path = "project-a";
      final String pathWithNamespace = "kevin/project-a";
      final Boolean issuesEnabled = true;
      final Boolean mergeRequestsEnabled = true;
      final Boolean wallEnabled = false;
      final Boolean wikiEnabled = true;
      final Boolean snippetsEnabled = false;
      final String createdAt = "2013-08-31T08:42:01Z";
      final String lastActivityAt = "2013-09-15T01:00:54Z";
      final Namespace namespace =
        new Namespace(2, "Kevin Lee", "2013-08-25T08:17:23Z", "", 2, "kevin", "2013-08-25T08:17:23Z");

      final GitLabProject.Builder builder = GitLabProject.builder()
          .id(id)
          .description(description)
          .defaultBranch(defaultBranch);
      if (isPublic)
      {
        builder.itIsPublic();
      }

      builder.sshUrlToRepo(sshUrlToRepo)
          .httpUrlToRepo(httpUrlToRepo)
          .webUrl(webUrl)
          .owner(owner)
          .name(name)
          .nameWithNamespace(nameWithNamespace)
          .path(path)
          .pathWithNamespace(pathWithNamespace);

      if (issuesEnabled)
      {
        builder.issuesEnabled();
      }

      if (mergeRequestsEnabled)
      {
        builder.mergeRequestsEnabled();
      }

      if (wallEnabled)
      {
        builder.wallEnabled();
      }

      if (wikiEnabled)
      {
        builder.wikiEnabled();
      }

      if (snippetsEnabled)
      {
        builder.snippetsEnabled();
      }

      gitLabProject = builder.createdAt(createdAt)
          .lastActivityAt(lastActivityAt)
          .namespace(namespace)
          .build();
    }

    final GitLabUser assignee;
    {
      @SuppressWarnings("boxing")
      final Integer id1 = 2;
      final String username1 = "kevinlee";
      final String email1 = "kevinlee@some.email.address";
      final String name1 = "Kevin Lee";
      final String bio1 = null;
      final String skype1 = "";
      final String linkedin1 = "";
      final String twitter1 = "";
      @SuppressWarnings("boxing")
      final Integer themeId1 = 1;
      @SuppressWarnings("boxing")
      final Integer colorSchemeId1 = 1;
      final String state1 = "active";
      final String createdAt1 = "2013-08-25T07:47:19Z";
      final String externUid1 = null;
      final String provider1 = null;

      @SuppressWarnings("boxing")
      final Integer id2 = 3;
      final String username2 = "anotheruser";
      final String email2 = "another.user@some.email";
      final String name2 = "Another Person";
      final String bio2 = null;
      final String skype2 = "";
      final String linkedin2 = "";
      final String twitter2 = "";
      @SuppressWarnings("boxing")
      final Integer themeId2 = 1;
      @SuppressWarnings("boxing")
      final Integer colorSchemeId2 = 1;
      final String state2 = "active";
      final String createdAt2 = "2013-09-01T15:10:55Z";
      final String externUid2 = null;
      final String provider2 = null;

      assignee =
        new GitLabUser(id1, username1, email1, name1, bio1, skype1, linkedin1, twitter1, themeId1, colorSchemeId1,
            state1, createdAt1, externUid1, provider1);
    }

    final GitLabMilestone milestone;
    {
      final Integer id1 = 1;
      final Integer projectId = 1;
      final String title1 = "test title 1";
      final String description1 = "some description";
      final String dueDate1 = "2013-12-01";
      final String state1 = "active";
      final String createdAt1 = "2013-08-25T11:25:35Z";
      final String updatedAt1 = "2013-08-25T11:55:12Z";
      milestone = new GitLabMilestone(id1, projectId, title1, description1, dueDate1, state1, createdAt1, updatedAt1);
    }

    final TracIssue tracIssue;
    {
      final Map<String, Object> map = newHashMap();
      final String summary = "Some test ticket";
      map.put("summary", summary);
      final String keywords = "test, ticket, blah blah";
      map.put("keywords", keywords);
      final String status = "new";
      map.put("status", status);
      final String resolution = "";
      map.put("resolution", resolution);
      final String type = "Task";
      map.put("type", type);
      final String version = "0.0.1";
      map.put("version", version);
      final String milestone1 = "M1";
      map.put("milestone", milestone1);
      final String reporter = "kevinlee";
      map.put("reporter", reporter);

      final Date time = DateAndTimeFormatUtil.parseUtcDateAndTimeIfNeitherNullNorEmpty("2013-01-01T15:00:00Z");
      map.put("time", time);
      final String component = "test-component";
      map.put("component", component);
      final String description =
        "This is test description.\n"
            + "{{{  \n  #!text/javascript\n{\n  \"name\":\"Kevin\"\n\"something\":{\"someObject\":{\"id\":1}}}\n}}}\nblah blah blah blah\n{{{\n#!text/javascript\n{\n  \"id\": 1\n}\n}}}";
      map.put("description", description);
      final String priority = "highest";
      map.put("priority", priority);
      final String severity = "critical";
      map.put("severity", severity);
      final String owner = "kevinlee";
      map.put("owner", owner);

      final Date changetime = DateAndTimeFormatUtil.parseUtcDateAndTimeIfNeitherNullNorEmpty("2013-01-05T10:30:22Z");
      map.put("changetime", changetime);
      final String cc = "user1, user2";
      map.put("cc", cc);

      final Object[] changeLogs =
        new Object[] { new Object[] { new Date(), "kevinlee", "comment", "1", "some test comment", 1 },
            new Object[] { new Date(), "kevinlee", "resolution", "", "fixed", 1 },
            new Object[] { new Date(), "kevinlee", "comment", "1", "something else", 1 },
            new Object[] { new Date(), "kevinlee", "status", "closed", "reopened", 1 },
            new Object[] { new Date(), "kevinlee", "resolution", "", "fixed", 1 },
            new Object[] { new Date(), "kevinlee", "comment", "1", "blah blah", 1 } };

      tracIssue = TracIssue.newInstance(1, map, changeLogs);
    }
    final GitLabIssueForCreation expected =
      new GitLabIssueForCreation(
          gitLabProject.id,
          tracIssue.getSummary(),
          "This is test description.\n"
              + "```javascript\n{\n  \"name\":\"Kevin\"\n\"something\":{\"someObject\":{\"id\":1}}}\n```\nblah blah blah blah\n```javascript\n{\n  \"id\": 1\n}\n```",
          asList("critical", "task"), assignee.id, milestone.id);

    final Trac2GitLabIssueConverter trac2GitLabIssueConverter = new Trac2GitLabIssueConverterImpl();

    /* when */
    final GitLabIssueForCreation actual =
      trac2GitLabIssueConverter.convert(gitLabProject, assignee, milestone, labelMap, tracIssue);

    /* then */
    assertThat(actual).isEqualTo(expected);
  }

  @Test
  public final void testConvert2() throws Exception
  {
    /* given */
    final GitLabProject gitLabProject;
    {
      final Integer id = 1;
      final String description = "project A";
      final String defaultBranch = null;
      final Boolean isPublic = false;
      final String sshUrlToRepo = "git@gitlab.localhost:kevin/project-a.git";
      final String httpUrlToRepo = "http://gitlab.localhost/kevin/project-a.git";
      final String webUrl = "http://gitlab.localhost/kevin/project-a";
      final Owner owner =
        new Owner(2, "kevin", "kevin.code@some.email.address", "Kevin Lee", "active", "2013-08-25T08:17:22Z");
      final String name = "project-a";
      final String nameWithNamespace = "Kevin Lee / project-a";
      final String path = "project-a";
      final String pathWithNamespace = "kevin/project-a";
      final Boolean issuesEnabled = true;
      final Boolean mergeRequestsEnabled = true;
      final Boolean wallEnabled = false;
      final Boolean wikiEnabled = true;
      final Boolean snippetsEnabled = false;
      final String createdAt = "2013-08-31T08:42:01Z";
      final String lastActivityAt = "2013-09-15T01:00:54Z";
      final Namespace namespace =
        new Namespace(2, "Kevin Lee", "2013-08-25T08:17:23Z", "", 2, "kevin", "2013-08-25T08:17:23Z");

      final GitLabProject.Builder builder = GitLabProject.builder()
          .id(id)
          .description(description)
          .defaultBranch(defaultBranch);
      if (isPublic)
      {
        builder.itIsPublic();
      }

      builder.sshUrlToRepo(sshUrlToRepo)
          .httpUrlToRepo(httpUrlToRepo)
          .webUrl(webUrl)
          .owner(owner)
          .name(name)
          .nameWithNamespace(nameWithNamespace)
          .path(path)
          .pathWithNamespace(pathWithNamespace);

      if (issuesEnabled)
      {
        builder.issuesEnabled();
      }

      if (mergeRequestsEnabled)
      {
        builder.mergeRequestsEnabled();
      }

      if (wallEnabled)
      {
        builder.wallEnabled();
      }

      if (wikiEnabled)
      {
        builder.wikiEnabled();
      }

      if (snippetsEnabled)
      {
        builder.snippetsEnabled();
      }

      gitLabProject = builder.createdAt(createdAt)
          .lastActivityAt(lastActivityAt)
          .namespace(namespace)
          .build();
    }

    final GitLabUser assignee;
    {
      @SuppressWarnings("boxing")
      final Integer id1 = 2;
      final String username1 = "kevinlee";
      final String email1 = "kevinlee@some.email.address";
      final String name1 = "Kevin Lee";
      final String bio1 = null;
      final String skype1 = "";
      final String linkedin1 = "";
      final String twitter1 = "";
      @SuppressWarnings("boxing")
      final Integer themeId1 = 1;
      @SuppressWarnings("boxing")
      final Integer colorSchemeId1 = 1;
      final String state1 = "active";
      final String createdAt1 = "2013-08-25T07:47:19Z";
      final String externUid1 = null;
      final String provider1 = null;

      @SuppressWarnings("boxing")
      final Integer id2 = 3;
      final String username2 = "anotheruser";
      final String email2 = "another.user@some.email";
      final String name2 = "Another Person";
      final String bio2 = null;
      final String skype2 = "";
      final String linkedin2 = "";
      final String twitter2 = "";
      @SuppressWarnings("boxing")
      final Integer themeId2 = 1;
      @SuppressWarnings("boxing")
      final Integer colorSchemeId2 = 1;
      final String state2 = "active";
      final String createdAt2 = "2013-09-01T15:10:55Z";
      final String externUid2 = null;
      final String provider2 = null;

      assignee =
        new GitLabUser(id1, username1, email1, name1, bio1, skype1, linkedin1, twitter1, themeId1, colorSchemeId1,
            state1, createdAt1, externUid1, provider1);
    }

    final GitLabMilestone milestone;
    {
      final Integer id1 = 1;
      final Integer projectId = 1;
      final String title1 = "test title 1";
      final String description1 = "some description";
      final String dueDate1 = "2013-12-01";
      final String state1 = "active";
      final String createdAt1 = "2013-08-25T11:25:35Z";
      final String updatedAt1 = "2013-08-25T11:55:12Z";
      milestone = new GitLabMilestone(id1, projectId, title1, description1, dueDate1, state1, createdAt1, updatedAt1);
    }

    final TracIssue tracIssue;
    {
      final Map<String, Object> map = newHashMap();
      final String summary = "Some test ticket";
      map.put("summary", summary);
      final String keywords = "test, ticket, blah blah";
      map.put("keywords", keywords);
      final String status = "new";
      map.put("status", status);
      final String resolution = "";
      map.put("resolution", resolution);
      final String type = "Task";
      map.put("type", type);
      final String version = "0.0.1";
      map.put("version", version);
      final String milestone1 = "M1";
      map.put("milestone", milestone1);
      final String reporter = "kevinlee";
      map.put("reporter", reporter);

      final Date time = DateAndTimeFormatUtil.parseUtcDateAndTimeIfNeitherNullNorEmpty("2013-01-01T15:00:00Z");
      map.put("time", time);
      final String component = "test-component";
      map.put("component", component);
      final String description = "This is test description.";
      map.put("description", description);
      final String priority = "highest";
      map.put("priority", priority);
      final String severity = "critical";
      map.put("severity", severity);
      final String owner = "kevinlee";
      map.put("owner", owner);

      final Date changetime = DateAndTimeFormatUtil.parseUtcDateAndTimeIfNeitherNullNorEmpty("2013-01-05T10:30:22Z");
      map.put("changetime", changetime);
      final String cc = "user1, user2";
      map.put("cc", cc);

      final Object[] changeLogs =
        new Object[] { new Object[] { new Date(), "kevinlee", "comment", "1", "some test comment", 1 },
            new Object[] { new Date(), "kevinlee", "resolution", "", "fixed", 1 },
            new Object[] { new Date(), "kevinlee", "comment", "1", "something else", 1 },
            new Object[] { new Date(), "kevinlee", "status", "closed", "reopened", 1 },
            new Object[] { new Date(), "kevinlee", "resolution", "", "fixed", 1 },
            new Object[] { new Date(), "kevinlee", "comment", "1", "blah blah", 1 } };

      tracIssue = TracIssue.newInstance(1, map, changeLogs);
    }
    final GitLabIssueForCreation expected =
      new GitLabIssueForCreation(gitLabProject.id, tracIssue.getSummary(), "This is test description.", asList(
          "critical", "task"), assignee.id, milestone.id);

    final Trac2GitLabIssueConverter trac2GitLabIssueConverter = new Trac2GitLabIssueConverterImpl();

    /* when */
    final GitLabIssueForCreation actual =
      trac2GitLabIssueConverter.convert(gitLabProject, assignee, milestone, labelMap, tracIssue);

    /* then */
    assertThat(actual).isEqualTo(expected);
  }

  @Test
  public void testExtractLabels()
  {
    /* given */
    final TracIssue tracIssue = mock(TracIssue.class);
    when(tracIssue.getType()).thenReturn("Bug");
    when(tracIssue.getSeverity()).thenReturn("critical");
    when(tracIssue.getStatus()).thenReturn("closed");
    when(tracIssue.getResolution()).thenReturn("fixed");
    when(tracIssue.getStatus()).thenReturn("closed");

    final List<String> expected = asList("bug", "critical", "fixed");

    final Trac2GitLabIssueConverter trac2GitLabIssueConverter = new Trac2GitLabIssueConverterImpl();

    /* when */
    final List<String> actual = trac2GitLabIssueConverter.extractLabels(labelMap, tracIssue);

    /* then */
    assertThat(actual).containsAll(expected);
    assertThat(actual.size()).isEqualTo(expected.size());
  }

  @Test
  public void testExtractLabels2()
  {
    /* given */
    final TracIssue tracIssue = mock(TracIssue.class);
    when(tracIssue.getType()).thenReturn("Task");
    when(tracIssue.getSeverity()).thenReturn("minor");
    when(tracIssue.getPriority()).thenReturn("high");
    when(tracIssue.getStatus()).thenReturn("opened");
    when(tracIssue.getResolution()).thenReturn("");
    when(tracIssue.getStatus()).thenReturn("new");

    final List<String> expected = asList("task", "minor");

    final Trac2GitLabIssueConverter trac2GitLabIssueConverter = new Trac2GitLabIssueConverterImpl();

    /* when */
    final List<String> actual = trac2GitLabIssueConverter.extractLabels(labelMap, tracIssue);

    /* then */
    assertThat(actual).containsAll(expected);
    assertThat(actual.size()).isEqualTo(expected.size());
  }

  @Test
  public void testExtractLabels3()
  {
    /* given */
    final TracIssue tracIssue = mock(TracIssue.class);
    when(tracIssue.getType()).thenReturn("New Feature");
    when(tracIssue.getPriority()).thenReturn("highest");
    when(tracIssue.getStatus()).thenReturn("closed");
    when(tracIssue.getResolution()).thenReturn("duplicate");

    final List<String> expected = asList("feature", "highest", "duplicate");

    final Trac2GitLabIssueConverter trac2GitLabIssueConverter = new Trac2GitLabIssueConverterImpl();

    /* when */
    final List<String> actual = trac2GitLabIssueConverter.extractLabels(labelMap, tracIssue);

    /* then */
    assertThat(actual).containsAll(expected);
    assertThat(actual.size()).isEqualTo(expected.size());
  }
}
