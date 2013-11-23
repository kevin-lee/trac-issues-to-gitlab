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

import static com.lckymn.kevin.util.DateAndTimeFormatUtil.*;
import static org.assertj.core.api.Assertions.*;
import static org.elixirian.kommonlee.util.collect.Lists.*;
import static org.elixirian.kommonlee.util.collect.Maps.*;
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

import com.lckymn.kevin.gitlab.api.GitLabIssueService;
import com.lckymn.kevin.gitlab.api.GitLabMilestoneService;
import com.lckymn.kevin.gitlab.api.GitLabUserService;
import com.lckymn.kevin.gitlab.json.GitLabIssue;
import com.lckymn.kevin.gitlab.json.GitLabIssue.GitLabIssueForCreation;
import com.lckymn.kevin.gitlab.json.GitLabIssue.Milestone;
import com.lckymn.kevin.gitlab.json.GitLabIssue.User;
import com.lckymn.kevin.gitlab.json.GitLabMilestone;
import com.lckymn.kevin.gitlab.json.GitLabMilestone.GitLabMilestoneForCreation;
import com.lckymn.kevin.gitlab.json.GitLabProject;
import com.lckymn.kevin.gitlab.json.GitLabProject.Namespace;
import com.lckymn.kevin.gitlab.json.GitLabProject.Owner;
import com.lckymn.kevin.gitlab.json.GitLabUser;
import com.lckymn.kevin.trac.json.TracIssue;
import com.lckymn.kevin.trac.json.TracMilestone;
import com.lckymn.kevin.trac2gitlab.IssueMigrationService;
import com.lckymn.kevin.trac2gitlab.Trac2GitLabIssueConverter;
import com.lckymn.kevin.trac2gitlab.Trac2GitLabUtil;
import com.lckymn.kevin.trac2gitlab.json.Trac2GitLabIssueMigrationResult;

public class IssueMigrationServiceImplTest
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
    final Map<String, String> map = newLinkedHashMap();
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

  private static GitLabMilestone newGitLabMilestone(final Integer id, final GitLabProject gitLabProject,
      final TracMilestone tracMilestone, final String state, final String createdAt, final String updatedAt)
  {
    final Integer projectId = gitLabProject.id;
    final String title = tracMilestone.getName();
    final String description = tracMilestone.getDescription();
    final String dueDate = formatUtcDateAndTimeIfNotNull(tracMilestone.getDue());
    return new GitLabMilestone(id, projectId, title, description, dueDate, state, createdAt, updatedAt);
  }

  @Test
  public final void testMigrate()
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

    final GitLabUser kevinlee;
    {
      final Integer id1 = 2;
      final String username1 = "kevinlee";
      final String email1 = "kevinlee@some.email.address";
      final String name1 = "Kevin Lee";
      final String bio1 = null;
      final String skype1 = "";
      final String linkedin1 = "";
      final String twitter1 = "";
      final Integer themeId1 = 1;
      final Integer colorSchemeId1 = 1;
      final String state1 = "active";
      final String createdAt1 = "2013-08-25T07:47:19Z";
      final String externUid1 = null;
      final String provider1 = null;
      kevinlee =
        GitLabUser.newGitLabUser(id1, username1, email1, name1, bio1, skype1, linkedin1, twitter1, themeId1,
            colorSchemeId1, state1, createdAt1, externUid1, provider1);
    }
    final GitLabUser anotherUser;
    {
      final Integer id2 = 3;
      final String username2 = "anotheruser";
      final String email2 = "another.user@some.email";
      final String name2 = "Another Person";
      final String bio2 = null;
      final String skype2 = "";
      final String linkedin2 = "";
      final String twitter2 = "";
      final Integer themeId2 = 1;
      final Integer colorSchemeId2 = 1;
      final String state2 = "active";
      final String createdAt2 = "2013-09-01T15:10:55Z";
      final String externUid2 = null;
      final String provider2 = null;

      anotherUser =
        new GitLabUser(id2, username2, email2, name2, bio2, skype2, linkedin2, twitter2, themeId2, colorSchemeId2,
            state2, createdAt2, externUid2, provider2);
    }

    final Map<String, TracMilestone> milestoneToTracMilestoneMap = newLinkedHashMap();
    final TracMilestone tracMilestone1 =
      TracMilestone.newTracMilestone("M1", "First milestone", parseUtcDateIfNeitherNullNorEmpty("2013-12-01"), null);
    milestoneToTracMilestoneMap.put(tracMilestone1.getName(), tracMilestone1);
    final TracMilestone tracMilestone2 =
      TracMilestone.newTracMilestone("M2", "The second one", parseUtcDateIfNeitherNullNorEmpty("2014-02-01"), null);
    milestoneToTracMilestoneMap.put(tracMilestone2.getName(), tracMilestone2);

    final GitLabMilestone gitLabMilestone1;
    {
      final Integer id = 1;
      final String state = "active";
      final String createdAt = "2013-08-25T11:25:35Z";
      final String updatedAt = "2013-08-25T11:55:12Z";
      gitLabMilestone1 = newGitLabMilestone(id, gitLabProject, tracMilestone1, state, createdAt, updatedAt);
    }

    final GitLabMilestone gitLabMilestone2;
    {
      final Integer id = 2;
      final String state = "active";
      final String createdAt = "2013-09-03T16:25:11Z";
      final String updatedAt = "2013-09-04T01:00:02Z";
      gitLabMilestone2 = newGitLabMilestone(id, gitLabProject, tracMilestone2, state, createdAt, updatedAt);
    }

    final TracIssue tracIssue1;
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
      final String milestone1 = tracMilestone1.getName();
      map.put("milestone", milestone1);
      final String reporter = kevinlee.username;
      map.put("reporter", reporter);

      final Date time = parseUtcDateAndTimeIfNeitherNullNorEmpty("2013-01-01T15:00:00Z");
      map.put("time", time);
      final String component = "test-component";
      map.put("component", component);
      final String description =
        "This is test description.\n"
            + "{{{  \n  #!text/javascript\n{\n  \"name\":\"Kevin\"\n\"something\":{\"someObject\":{\"id\":1}}}\n}}}\nblah blah blah blah\n{{{\n#!text/javascript\n{\n  \"id\": 1\n}\n}}}";
      map.put("description", description);
      final String priority = "normal";
      map.put("priority", priority);
      final String severity = "major";
      map.put("severity", severity);
      final String owner = kevinlee.username;
      map.put("owner", owner);

      final Date changetime = parseUtcDateAndTimeIfNeitherNullNorEmpty("2013-01-05T10:30:22Z");
      map.put("changetime", changetime);
      final String cc = "user1, user2";
      map.put("cc", cc);

      /* @formatter:off */
      final Object[] changeLogs =
        new Object[] {
            new Object[] { new Date(), kevinlee.username, "comment", "1", "some test comment", 1 },
            new Object[] { new Date(), kevinlee.username, "resolution", "", "fixed", 1 },
            new Object[] { new Date(), kevinlee.username, "comment", "1", "something else", 1 },
            new Object[] { new Date(), kevinlee.username, "status", "closed", "reopened", 1 },
            new Object[] { new Date(), kevinlee.username, "resolution", "", "fixed", 1 },
            new Object[] { new Date(), kevinlee.username, "comment", "1", "blah blah", 1 }
        };
      /* @formatter:on */

      tracIssue1 = TracIssue.newInstance(1, map, changeLogs);
    }
    final GitLabIssue gitLabIssue1;
    {
      final Integer id1 = 1;
      final String title1 = tracIssue1.getSummary();
      final String description1 = Trac2GitLabUtil.convertCodeBlockForGitLabMarkDown(tracIssue1.getDescription());
      final List<String> labels1 = Trac2GitLabUtil.extractLabels(labelMap, tracIssue1);
      final Milestone milestone1 =
        new Milestone(gitLabMilestone1.id, gitLabMilestone1.projectId, gitLabMilestone1.title,
            gitLabMilestone1.description, gitLabMilestone1.getDueDateInUtcString(), gitLabMilestone1.state,
            gitLabMilestone1.getCreatedAtInUtcString(), gitLabMilestone1.getUpdatedAtInUtcString());
      final User assignee1 =
        new User(kevinlee.id, kevinlee.username, kevinlee.email, kevinlee.name, kevinlee.state,
            kevinlee.getCreatedAtInUtcString());

      final Integer author1Id = 2;
      final String author1Username = kevinlee.username;
      final String author1Email = kevinlee.email;
      final String author1Name = kevinlee.name;
      final String author1State = kevinlee.state;
      final String author1CreatedAt = kevinlee.getCreatedAtInUtcString();
      final String author1 =
        "{\"id\":" + author1Id + ",\"username\":\"" + author1Username + "\",\"email\":\"" + author1Email
            + "\",\"name\":\"" + author1Name + "\",\"state\":\"" + author1State + "\",\"created_at\":\""
            + author1CreatedAt + "\"}";

      final String state1 = "opened";
      final String createdAt1 = tracIssue1.getTimeInUtcString();
      final String updatedAt1 = tracIssue1.getChangetimeInUtcString();

      gitLabIssue1 =
        new GitLabIssue(id1, gitLabProject.id, title1, description1, labels1, milestone1, new User(author1Id,
            author1Username, author1Email, author1Name, author1State, author1CreatedAt), assignee1, state1, createdAt1,
            updatedAt1);
    }

    final TracIssue tracIssue2;
    {
      final Map<String, Object> map = newHashMap();
      final String summary = "test 222222222";
      map.put("summary", summary);
      final String keywords = "second test, blah2, blah2";
      map.put("keywords", keywords);
      final String status = "closed";
      map.put("status", status);
      final String resolution = "fixed";
      map.put("resolution", resolution);
      final String type = "Bug";
      map.put("type", type);
      final String version = "0.0.1";
      map.put("version", version);
      final String milestone1 = tracMilestone1.getName();
      map.put("milestone", milestone1);
      final String reporter = kevinlee.username;
      map.put("reporter", reporter);

      final Date time = parseUtcDateAndTimeIfNeitherNullNorEmpty("2013-01-02T10:12:47Z");
      map.put("time", time);
      final String component = "test-component";
      map.put("component", component);
      final String description =
        "This is test description.\n"
            + "{{{  \n  #!text/javascript\n{\n  \"name\":\"Kevin\"\n\"something\":{\"someObject\":{\"id\":1}}}\n}}}\nblah blah blah blah\n{{{\n#!text/javascript\n{\n  \"id\": 1\n}\n}}}\nThis is test {{{code}}}.";
      map.put("description", description);
      final String priority = "highest";
      map.put("priority", priority);
      final String severity = "critical";
      map.put("severity", severity);
      final String owner = anotherUser.username;
      map.put("owner", owner);

      final Date changetime = parseUtcDateAndTimeIfNeitherNullNorEmpty("2013-01-03T17:35:16Z");
      map.put("changetime", changetime);
      final String cc = kevinlee.username;
      map.put("cc", cc);

      final Object[] changeLogs =
        new Object[] {
            new Object[] { new Date(), anotherUser.username, "comment", "1", "It will be fixed shortly.", 1 },
            new Object[] { new Date(), anotherUser.username, "resolution", "", "fixed", 1 } };

      tracIssue2 = TracIssue.newInstance(2, map, changeLogs);
    }
    final GitLabIssue gitLabIssue2;
    {
      final Integer id2 = 2;
      final String title2 = tracIssue2.getSummary();
      final String description2 = Trac2GitLabUtil.convertCodeBlockForGitLabMarkDown(tracIssue2.getDescription());
      final List<String> labels2 = Trac2GitLabUtil.extractLabels(labelMap, tracIssue2);
      final Milestone milestone2 =
        new Milestone(gitLabMilestone1.id, gitLabMilestone1.projectId, gitLabMilestone1.title,
            gitLabMilestone1.description, gitLabMilestone1.getDueDateInUtcString(), gitLabMilestone1.state,
            gitLabMilestone1.getCreatedAtInUtcString(), gitLabMilestone1.getUpdatedAtInUtcString());
      final User assignee2 =
        new User(anotherUser.id, anotherUser.username, anotherUser.email, anotherUser.name, anotherUser.state,
            anotherUser.getCreatedAtInUtcString());

      final Integer author2Id = 2;
      final String author2Username = kevinlee.username;
      final String author2Email = kevinlee.email;
      final String author2Name = kevinlee.name;
      final String author2State = kevinlee.state;
      final String author2CreatedAt = kevinlee.getCreatedAtInUtcString();
      final String author2 =
        "{\"id\":" + author2Id + ",\"username\":\"" + author2Username + "\",\"email\":\"" + author2Email
            + "\",\"name\":\"" + author2Name + "\",\"state\":\"" + author2State + "\",\"created_at\":\""
            + author2CreatedAt + "\"}";

      final String state2 = tracIssue2.getStatus();
      final String createdAt2 = tracIssue2.getTimeInUtcString();
      final String updatedAt2 = tracIssue2.getChangetimeInUtcString();

      gitLabIssue2 =
        new GitLabIssue(id2, gitLabProject.id, title2, description2, labels2, milestone2, new User(author2Id,
            author2Username, author2Email, author2Name, author2State, author2CreatedAt), assignee2, state2, createdAt2,
            updatedAt2);
    }

    final TracIssue tracIssue3;
    {
      final Map<String, Object> map = newHashMap();
      final String summary = "ticket 3";
      map.put("summary", summary);
      final String keywords = "later, failure";
      map.put("keywords", keywords);
      final String status = "new";
      map.put("status", status);
      final String resolution = "";
      map.put("resolution", resolution);
      final String type = "Task";
      map.put("type", type);
      final String version = "0.0.1";
      map.put("version", version);
      final String milestone1 = tracMilestone2.getName();
      map.put("milestone", milestone1);
      final String reporter = anotherUser.username;
      map.put("reporter", reporter);

      final Date time = parseUtcDateAndTimeIfNeitherNullNorEmpty("2013-01-01T15:00:00Z");
      map.put("time", time);
      final String component = "new-component";
      map.put("component", component);
      final String description =
        "This issue should be handled later.\nThe first milestone should be done before this one.";
      map.put("description", description);
      final String priority = "highest";
      map.put("priority", priority);
      final String severity = "critical";
      map.put("severity", severity);
      final String owner = kevinlee.username;
      map.put("owner", owner);

      final Date changetime = parseUtcDateAndTimeIfNeitherNullNorEmpty("2013-01-05T10:30:22Z");
      map.put("changetime", changetime);
      final String cc = "user1, user2";
      map.put("cc", cc);

      final Object[] changeLogs = new Object[0];

      tracIssue3 = TracIssue.newInstance(1, map, changeLogs);
    }
    final GitLabIssue gitLabIssue3;
    {
      final Integer id3 = 3;
      final String title3 = tracIssue3.getSummary();
      final String description3 = Trac2GitLabUtil.convertCodeBlockForGitLabMarkDown(tracIssue3.getDescription());
      final List<String> labels3 = Trac2GitLabUtil.extractLabels(labelMap, tracIssue3);
      final Milestone milestone3 =
        new Milestone(gitLabMilestone1.id, gitLabMilestone1.projectId, gitLabMilestone1.title,
            gitLabMilestone1.description, gitLabMilestone1.getDueDateInUtcString(), gitLabMilestone1.state,
            gitLabMilestone1.getCreatedAtInUtcString(), gitLabMilestone1.getUpdatedAtInUtcString());
      final User assignee3 =
        new User(kevinlee.id, kevinlee.username, kevinlee.email, kevinlee.name, kevinlee.state,
            kevinlee.getCreatedAtInUtcString());

      final Integer author3Id = 2;
      final String author3Username = anotherUser.username;
      final String author3Email = anotherUser.email;
      final String author3Name = anotherUser.name;
      final String author3State = anotherUser.state;
      final String author3CreatedAt = anotherUser.getCreatedAtInUtcString();
      final String author3 =
        "{\"id\":" + author3Id + ",\"username\":\"" + author3Username + "\",\"email\":\"" + author3Email
            + "\",\"name\":\"" + author3Name + "\",\"state\":\"" + author3State + "\",\"created_at\":\""
            + author3CreatedAt + "\"}";

      final String state3 = tracIssue3.getStatus();
      final String createdAt3 = tracIssue3.getTimeInUtcString();
      final String updatedAt3 = tracIssue3.getChangetimeInUtcString();

      gitLabIssue3 =
        new GitLabIssue(id3, gitLabProject.id, title3, description3, labels3, milestone3, new User(author3Id,
            author3Username, author3Email, author3Name, author3State, author3CreatedAt), assignee3, state3, createdAt3,
            updatedAt3);
    }

    final String privateToken = "test-private-token";

    final List<GitLabMilestoneForCreation> gitLabMilestoneForCreationList =
      newArrayList(GitLabMilestoneForCreation.newGitLabMilestoneForCreation(gitLabMilestone1),
          GitLabMilestoneForCreation.newGitLabMilestoneForCreation(gitLabMilestone2));

    final GitLabMilestoneService gitLabMilestoneService = mock(GitLabMilestoneService.class);
    when(
        gitLabMilestoneService.createMilestonesIfNotExist(privateToken, gitLabProject.id,
            gitLabMilestoneForCreationList)).thenReturn(newArrayList(gitLabMilestone1, gitLabMilestone2));

    final GitLabUserService gitLabUserService = mock(GitLabUserService.class);
    when(gitLabUserService.getAllGitLabUsers(privateToken)).thenReturn(newArrayList(kevinlee, anotherUser));

    final GitLabIssueForCreation gitLabIssueForCreation1;
    {
      gitLabIssueForCreation1 =
        new GitLabIssueForCreation(gitLabProject.id, gitLabIssue1.title, gitLabIssue1.description,
            Trac2GitLabUtil.extractLabels(labelMap, tracIssue1), gitLabIssue1.assignee.id, gitLabIssue1.milestone.id);
    }
    final GitLabIssueForCreation gitLabIssueForCreation2;
    {
      gitLabIssueForCreation2 =
        new GitLabIssueForCreation(gitLabProject.id, gitLabIssue2.title, gitLabIssue2.description,
            Trac2GitLabUtil.extractLabels(labelMap, tracIssue2), gitLabIssue2.assignee.id, gitLabIssue2.milestone.id);
    }
    final GitLabIssueForCreation gitLabIssueForCreation3;
    {
      gitLabIssueForCreation3 =
        new GitLabIssueForCreation(gitLabProject.id, gitLabIssue3.title, gitLabIssue3.description,
            Trac2GitLabUtil.extractLabels(labelMap, tracIssue3), gitLabIssue3.assignee.id, gitLabIssue3.milestone.id);
    }

    final Trac2GitLabIssueConverter trac2GitLabIssueConverter = mock(Trac2GitLabIssueConverter.class);

    when(trac2GitLabIssueConverter.convert(gitLabProject, kevinlee, gitLabMilestone1, labelMap, tracIssue1)).thenReturn(
        gitLabIssueForCreation1);
    when(trac2GitLabIssueConverter.convert(gitLabProject, anotherUser, gitLabMilestone1, labelMap, tracIssue2)).thenReturn(
        gitLabIssueForCreation2);
    when(trac2GitLabIssueConverter.convert(gitLabProject, kevinlee, gitLabMilestone2, labelMap, tracIssue3)).thenReturn(
        gitLabIssueForCreation3);

    final GitLabIssueService gitLabIssueService = mock(GitLabIssueService.class);
    when(gitLabIssueService.createIssue(privateToken, gitLabProject.id, gitLabIssueForCreation1)).thenReturn(
        gitLabIssue1);
    when(gitLabIssueService.createIssue(privateToken, gitLabProject.id, gitLabIssueForCreation2)).thenReturn(
        gitLabIssue2);
    when(gitLabIssueService.createIssue(privateToken, gitLabProject.id, gitLabIssueForCreation3)).thenReturn(
        gitLabIssue3);

    final IssueMigrationService issueMigrationService =
      new IssueMigrationServiceImpl(1, gitLabIssueService, gitLabMilestoneService, gitLabUserService,
          trac2GitLabIssueConverter);

    final List<TracIssue> tracIssues = newArrayList(tracIssue1, tracIssue2, tracIssue3);
    final Trac2GitLabIssueMigrationResult expected = Trac2GitLabIssueMigrationResult.builder(1)
        .add(1, tracIssue1, gitLabIssue1)
        .add(2, tracIssue2, gitLabIssue2)
        .add(3, tracIssue3, gitLabIssue3)
        .build();

    /* when */
    final Trac2GitLabIssueMigrationResult actual =
      issueMigrationService.migrate(privateToken, gitLabProject, labelMap, milestoneToTracMilestoneMap, tracIssues);

    /* then */
    assertThat(actual).isEqualTo(expected);
  }
}
