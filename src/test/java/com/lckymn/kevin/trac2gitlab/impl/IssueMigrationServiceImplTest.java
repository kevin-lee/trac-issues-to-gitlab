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

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.elixirian.jsonstatham.core.JsonStatham;
import org.elixirian.jsonstatham.core.reflect.ReflectionJsonStathams;
import org.elixirian.kommonlee.io.IoCommonConstants;
import org.elixirian.kommonlee.io.util.FileUtil;
import org.elixirian.kommonlee.nio.util.NioUtil;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

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
import com.lckymn.kevin.trac2gitlab.impl.IssueMigrationServiceImpl.FilenameResolver;
import com.lckymn.kevin.trac2gitlab.json.Trac2GitLabIssueMigrationResult;

@RunWith(MockitoJUnitRunner.class)
public class IssueMigrationServiceImplTest
{
  @Rule
  public TemporaryFolder temporaryFolder = new TemporaryFolder();

  private File jsonFile;

  private JsonStatham jsonStatham;

  @Mock
  private GitLabMilestoneService gitLabMilestoneService;

  @Mock
  private GitLabUserService gitLabUserService;

  @Mock
  private Trac2GitLabIssueConverter trac2GitLabIssueConverter;

  @Mock
  private GitLabIssueService gitLabIssueService;

  private Map<String, String> labelMap;

  private GitLabProject gitLabProject;

  private GitLabUser kevinlee;
  private GitLabUser anotherUser;

  private Map<String, TracMilestone> milestoneToTracMilestoneMap;
  private TracMilestone tracMilestone1;
  private TracMilestone tracMilestone2;

  private GitLabMilestone gitLabMilestone1;
  private GitLabMilestone gitLabMilestone2;

  private TracIssue tracIssue1;
  private GitLabIssue gitLabIssue1;

  private TracIssue tracIssue2;
  private GitLabIssue gitLabIssue2;

  private TracIssue tracIssue3;
  private GitLabIssue gitLabIssue3;

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
    jsonStatham = ReflectionJsonStathams.newReflectionJsonStathamInAction();

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
    kevinlee =
      GitLabUser.newGitLabUser(2, "kevinlee", "kevinlee@some.email.address", "Kevin Lee", null, "", "", "", 1, 1,
          "active", "2013-08-25T07:47:19Z", null, null);

    anotherUser =
      new GitLabUser(3, "anotheruser", "another.user@some.email", "Another Person", null, "", "", "", 1, 1, "active",
          "2013-09-01T15:10:55Z", null, null);

    gitLabProject = GitLabProject.builder()
        .id(1)
        .description("project A")
        .defaultBranch(null)
        .sshUrlToRepo("git@gitlab.localhost:kevin/project-a.git")
        .httpUrlToRepo("http://gitlab.localhost/kevin/project-a.git")
        .webUrl("http://gitlab.localhost/kevin/project-a")
        .owner(new Owner(2, "kevin", "kevin.code@some.email.address", "Kevin Lee", "active", "2013-08-25T08:17:22Z"))
        .name("project-a")
        .nameWithNamespace("Kevin Lee / project-a")
        .path("project-a")
        .pathWithNamespace("kevin/project-a")
        .issuesEnabled()
        .mergeRequestsEnabled()
        .wikiEnabled()
        .createdAt("2013-08-31T08:42:01Z")
        .lastActivityAt("2013-09-15T01:00:54Z")
        .namespace(new Namespace(2, "Kevin Lee", "2013-08-25T08:17:23Z", "", 2, "kevin", "2013-08-25T08:17:23Z"))
        .build();

    tracMilestone1 =
      TracMilestone.newTracMilestone("M1", "First milestone", parseUtcDateIfNeitherNullNorEmpty("2013-12-01"), null);
    tracMilestone2 =
      TracMilestone.newTracMilestone("M2", "The second one", parseUtcDateIfNeitherNullNorEmpty("2014-02-01"), null);

    final Map<String, TracMilestone> milestoneToTracMilestoneMap = newLinkedHashMap();
    milestoneToTracMilestoneMap.put(tracMilestone1.getName(), tracMilestone1);
    milestoneToTracMilestoneMap.put(tracMilestone2.getName(), tracMilestone2);
    this.milestoneToTracMilestoneMap = Collections.unmodifiableMap(milestoneToTracMilestoneMap);

    gitLabMilestone1 =
      newGitLabMilestone(1, gitLabProject, tracMilestone1, "active", "2013-08-25T11:25:35Z", "2013-08-25T11:55:12Z");
    gitLabMilestone2 =
      newGitLabMilestone(2, gitLabProject, tracMilestone2, "active", "2013-09-03T16:25:11Z", "2013-09-04T01:00:02Z");

    {
      final Map<String, Object> map = newHashMap();
      map.put("summary", "Some test ticket");
      map.put("keywords", "test, ticket, blah blah");
      map.put("status", "new");
      map.put("resolution", "");
      map.put("type", "Task");
      map.put("version", "0.0.1");
      map.put("milestone", tracMilestone1.getName());
      map.put("reporter", kevinlee.username);
      map.put("time", parseUtcDateAndTimeIfNeitherNullNorEmpty("2013-01-01T15:00:00Z"));
      map.put("component", "test-component");
      map.put(
          "description",
          "This is test description.\n"
              + "{{{  \n  #!text/javascript\n{\n  \"name\":\"Kevin\"\n\"something\":{\"someObject\":{\"id\":1}}}\n}}}\nblah blah blah blah\n{{{\n#!text/javascript\n{\n  \"id\": 1\n}\n}}}");
      map.put("priority", "normal");
      map.put("severity", "major");
      map.put("owner", kevinlee.username);
      map.put("changetime", parseUtcDateAndTimeIfNeitherNullNorEmpty("2013-01-05T10:30:22Z"));
      map.put("cc", "user1, user2");

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
      gitLabIssue1 =
        new GitLabIssue(1, gitLabProject.id, tracIssue1.getSummary(),
            Trac2GitLabUtil.convertCodeBlockForGitLabMarkDown(tracIssue1.getDescription()),
            Trac2GitLabUtil.extractLabels(labelMap, tracIssue1), new Milestone(gitLabMilestone1.id,
                gitLabMilestone1.projectId, gitLabMilestone1.title, gitLabMilestone1.description,
                gitLabMilestone1.getDueDateInUtcString(), gitLabMilestone1.state,
                gitLabMilestone1.getCreatedAtInUtcString(), gitLabMilestone1.getUpdatedAtInUtcString()), new User(
                kevinlee.id, kevinlee.username, kevinlee.email, kevinlee.name, kevinlee.state,
                kevinlee.getCreatedAtInUtcString()), new User(kevinlee.id, kevinlee.username, kevinlee.email,
                kevinlee.name, kevinlee.state, kevinlee.getCreatedAtInUtcString()), "opened",
            tracIssue1.getTimeInUtcString(), tracIssue1.getChangetimeInUtcString());
    }
    {
      final Map<String, Object> map2 = newHashMap();
      map2.put("summary", "test 222222222");
      map2.put("keywords", "second test, blah2, blah2");
      map2.put("status", "closed");
      map2.put("resolution", "fixed");
      map2.put("type", "Bug");
      map2.put("version", "0.0.1");
      map2.put("milestone", tracMilestone1.getName());
      map2.put("reporter", kevinlee.username);

      map2.put("time", parseUtcDateAndTimeIfNeitherNullNorEmpty("2013-01-02T10:12:47Z"));
      map2.put("component", "test-component");
      map2.put(
          "description",
          "This is test description.\n"
              + "{{{  \n  #!text/javascript\n{\n  \"name\":\"Kevin\"\n\"something\":{\"someObject\":{\"id\":1}}}\n}}}\nblah blah blah blah\n{{{\n#!text/javascript\n{\n  \"id\": 1\n}\n}}}\nThis is test {{{code}}}.");
      map2.put("priority", "highest");
      map2.put("severity", "critical");
      map2.put("owner", anotherUser.username);

      map2.put("changetime", parseUtcDateAndTimeIfNeitherNullNorEmpty("2013-01-03T17:35:16Z"));
      map2.put("cc", kevinlee.username);

      final Object[] changeLogs2 =
        new Object[] {
            new Object[] { new Date(), anotherUser.username, "comment", "1", "It will be fixed shortly.", 1 },
            new Object[] { new Date(), anotherUser.username, "resolution", "", "fixed", 1 } };
      tracIssue2 = TracIssue.newInstance(2, map2, changeLogs2);

      /* @formatter:off */
      gitLabIssue2 =
        new GitLabIssue(2,
                      gitLabProject.id,
                      tracIssue2.getSummary(),
                      Trac2GitLabUtil.convertCodeBlockForGitLabMarkDown(tracIssue2.getDescription()),
                      Trac2GitLabUtil.extractLabels(labelMap, tracIssue2),
                      new Milestone(
                                    gitLabMilestone1.id,
                                    gitLabMilestone1.projectId,
                                    gitLabMilestone1.title,
                                    gitLabMilestone1.description,
                                    gitLabMilestone1.getDueDateInUtcString(),
                                    gitLabMilestone1.state,
                                    gitLabMilestone1.getCreatedAtInUtcString(),
                                    gitLabMilestone1.getUpdatedAtInUtcString()),
                      new User(
                               kevinlee.id,
                               kevinlee.username,
                               kevinlee.email,
                               kevinlee.name,
                               kevinlee.state,
                               kevinlee.getCreatedAtInUtcString()),
                      new User(
                               anotherUser.id,
                               anotherUser.username,
                               anotherUser.email,
                               anotherUser.name,
                               anotherUser.state,
                               anotherUser.getCreatedAtInUtcString()),
                      tracIssue2.getStatus(),
                      tracIssue2.getTimeInUtcString(),
                      tracIssue2.getChangetimeInUtcString());
    /* @formatter:on */
    }
    {
      final Map<String, Object> map3 = newHashMap();
      map3.put("summary", "ticket 3");
      map3.put("keywords", "later, failure");
      map3.put("status", "new");
      map3.put("resolution", "");
      map3.put("type", "Task");
      map3.put("version", "0.0.1");
      map3.put("milestone", tracMilestone2.getName());
      map3.put("reporter", anotherUser.username);

      map3.put("time", parseUtcDateAndTimeIfNeitherNullNorEmpty("2013-01-01T15:00:00Z"));
      map3.put("component", "new-component");
      map3.put("description",
          "This issue should be handled later.\nThe first milestone should be done before this one.");
      map3.put("priority", "highest");
      map3.put("severity", "critical");
      map3.put("owner", kevinlee.username);

      map3.put("changetime", parseUtcDateAndTimeIfNeitherNullNorEmpty("2013-01-05T10:30:22Z"));
      map3.put("cc", "user1, user2");

      final Object[] changeLogs3 = new Object[0];
      tracIssue3 = TracIssue.newInstance(1, map3, changeLogs3);

      /* @formatter:off */
      gitLabIssue3 =
        new GitLabIssue(3,
                      gitLabProject.id,
                      tracIssue3.getSummary(),
                      Trac2GitLabUtil.convertCodeBlockForGitLabMarkDown(tracIssue3
                                                                       .getDescription()),
                      Trac2GitLabUtil.extractLabels(labelMap, tracIssue3),
                      new Milestone(
                                    gitLabMilestone1.id,
                                    gitLabMilestone1.projectId,
                                    gitLabMilestone1.title,
                                    gitLabMilestone1.description,
                                    gitLabMilestone1.getDueDateInUtcString(),
                                    gitLabMilestone1.state,
                                    gitLabMilestone1.getCreatedAtInUtcString(),
                                    gitLabMilestone1.getUpdatedAtInUtcString()),
                      new User(
                               anotherUser.id,
                               anotherUser.username,
                               anotherUser.email,
                               anotherUser.name,
                               anotherUser.state,
                               anotherUser.getCreatedAtInUtcString()),
                      new User(
                               kevinlee.id,
                               kevinlee.username,
                               kevinlee.email,
                               kevinlee.name,
                               kevinlee.state,
                               kevinlee.getCreatedAtInUtcString()),
                      tracIssue3.getStatus(),
                      tracIssue3.getTimeInUtcString(),
                      tracIssue3.getChangetimeInUtcString());
      /* @formatter:on */
    }
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

    final String privateToken = "test-private-token";

    final List<GitLabMilestoneForCreation> gitLabMilestoneForCreationList =
      newArrayList(GitLabMilestoneForCreation.newGitLabMilestoneForCreation(gitLabMilestone1),
          GitLabMilestoneForCreation.newGitLabMilestoneForCreation(gitLabMilestone2));

    when(
        gitLabMilestoneService.createMilestonesIfNotExist(privateToken, gitLabProject.id,
            gitLabMilestoneForCreationList)).thenReturn(newArrayList(gitLabMilestone1, gitLabMilestone2));

    when(gitLabUserService.getAllGitLabUsers(privateToken)).thenReturn(newArrayList(kevinlee, anotherUser));

    final GitLabIssueForCreation gitLabIssueForCreation1 =
      new GitLabIssueForCreation(gitLabProject.id, gitLabIssue1.title, gitLabIssue1.description,
          Trac2GitLabUtil.extractLabels(labelMap, tracIssue1), gitLabIssue1.assignee.id, gitLabIssue1.milestone.id);
    final GitLabIssueForCreation gitLabIssueForCreation2 =
      new GitLabIssueForCreation(gitLabProject.id, gitLabIssue2.title, gitLabIssue2.description,
          Trac2GitLabUtil.extractLabels(labelMap, tracIssue2), gitLabIssue2.assignee.id, gitLabIssue2.milestone.id);
    final GitLabIssueForCreation gitLabIssueForCreation3 =
      new GitLabIssueForCreation(gitLabProject.id, gitLabIssue3.title, gitLabIssue3.description,
          Trac2GitLabUtil.extractLabels(labelMap, tracIssue3), gitLabIssue3.assignee.id, gitLabIssue3.milestone.id);

    when(trac2GitLabIssueConverter.convert(gitLabProject, kevinlee, gitLabMilestone1, labelMap, tracIssue1)).thenReturn(
        gitLabIssueForCreation1);
    when(trac2GitLabIssueConverter.convert(gitLabProject, anotherUser, gitLabMilestone1, labelMap, tracIssue2)).thenReturn(
        gitLabIssueForCreation2);
    when(trac2GitLabIssueConverter.convert(gitLabProject, kevinlee, gitLabMilestone2, labelMap, tracIssue3)).thenReturn(
        gitLabIssueForCreation3);

    when(gitLabIssueService.createIssue(privateToken, gitLabProject.id, gitLabIssueForCreation1)).thenReturn(
        gitLabIssue1);
    when(gitLabIssueService.createIssue(privateToken, gitLabProject.id, gitLabIssueForCreation2)).thenReturn(
        gitLabIssue2);
    when(gitLabIssueService.createIssue(privateToken, gitLabProject.id, gitLabIssueForCreation3)).thenReturn(
        gitLabIssue3);

    final IssueMigrationService issueMigrationService =
      new IssueMigrationServiceImpl(1, gitLabIssueService, gitLabMilestoneService, gitLabUserService,
          trac2GitLabIssueConverter, jsonStatham);

    final List<TracIssue> tracIssues = newArrayList(tracIssue1, tracIssue2, tracIssue3);
    final Trac2GitLabIssueMigrationResult expected = Trac2GitLabIssueMigrationResult.builder(1)
        .add(tracIssue1, gitLabIssue1)
        .add(tracIssue2, gitLabIssue2)
        .add(tracIssue3, gitLabIssue3)
        .build();

    /* when */
    final Trac2GitLabIssueMigrationResult actual =
      issueMigrationService.migrate(privateToken, gitLabProject, labelMap, milestoneToTracMilestoneMap, tracIssues);

    /* then */
    assertThat(actual).isEqualTo(expected);
  }

  @Test
  public void testSaveMigrationResult()
  {
    /* given */
    final File expected = new File(temporaryFolder.getRoot(), "test.json");
    jsonFile = new File(expected.toURI());

    final IssueMigrationService issueMigrationService =
      new IssueMigrationServiceImpl(1, gitLabIssueService, gitLabMilestoneService, gitLabUserService,
          trac2GitLabIssueConverter, jsonStatham);

    final Trac2GitLabIssueMigrationResult trac2GitLabIssueMigrationResult = Trac2GitLabIssueMigrationResult.builder(1)
        .add(tracIssue1, gitLabIssue1)
        .add(tracIssue2, gitLabIssue2)
        .add(tracIssue3, gitLabIssue3)
        .build();

    final String expectedJson = jsonStatham.convertIntoJson(trac2GitLabIssueMigrationResult);

    /* when */
    final File actual = issueMigrationService.saveMigrationResult(jsonFile, trac2GitLabIssueMigrationResult);

    /* then */
    assertThat(actual).isEqualTo(expected);
    final String actualJson = readJsonFile(actual);
    assertThat(actualJson).isEqualTo(expectedJson);
  }

  @Test
  public void testSaveMigrationResultWithAlreadyExistingFilename() throws IOException
  {
    /* given */
    jsonFile = new File(temporaryFolder.getRoot(), "test.json");
    jsonFile.createNewFile();

    final IssueMigrationService issueMigrationService =
      new IssueMigrationServiceImpl(1, gitLabIssueService, gitLabMilestoneService, gitLabUserService,
          trac2GitLabIssueConverter, jsonStatham);

    final Trac2GitLabIssueMigrationResult trac2GitLabIssueMigrationResult = Trac2GitLabIssueMigrationResult.builder(1)
        .add(tracIssue1, gitLabIssue1)
        .add(tracIssue2, gitLabIssue2)
        .add(tracIssue3, gitLabIssue3)
        .build();

    final String expectedJson = jsonStatham.convertIntoJson(trac2GitLabIssueMigrationResult);

    /* when */
    final File actual = issueMigrationService.saveMigrationResult(jsonFile, trac2GitLabIssueMigrationResult);

    /* then */
    final String actualPath = actual.toString();
    assertThat(actualPath).startsWith(temporaryFolder.getRoot()
        .toString() + File.separator + "test-");
    assertThat(actualPath).endsWith(".json");
    final String actualJson = readJsonFile(actual);
    assertThat(actualJson).isEqualTo(expectedJson);
  }

  @Test
  public void testSaveMigrationResultWithAlreadyExistingFilename2() throws IOException
  {
    /* given */
    jsonFile = new File(temporaryFolder.getRoot(), "test.json");
    jsonFile.createNewFile();

    final String suffix = "-some_suffix";

    final File expected = new File(temporaryFolder.getRoot(), "test" + suffix + ".json");

    final IssueMigrationService issueMigrationService =
      new IssueMigrationServiceImpl(1, gitLabIssueService, gitLabMilestoneService, gitLabUserService,
          trac2GitLabIssueConverter, jsonStatham, new FilenameResolver() {
            @Override
            public File apply(final String filename)
            {
              return new File(FileUtil.addBeforeExtensionIfExtensionExists(filename, suffix));
            }
          });

    final Trac2GitLabIssueMigrationResult trac2GitLabIssueMigrationResult = Trac2GitLabIssueMigrationResult.builder(1)
        .add(tracIssue1, gitLabIssue1)
        .add(tracIssue2, gitLabIssue2)
        .add(tracIssue3, gitLabIssue3)
        .build();

    final String expectedJson = jsonStatham.convertIntoJson(trac2GitLabIssueMigrationResult);

    /* when */
    final File actual = issueMigrationService.saveMigrationResult(jsonFile, trac2GitLabIssueMigrationResult);

    /* then */
    assertThat(actual).isEqualTo(expected);
    final String actualJson = readJsonFile(actual);
    assertThat(actualJson).isEqualTo(expectedJson);
  }

  private static String readJsonFile(final File file)
  {
    return new String(NioUtil.readFileToByteArray(file, IoCommonConstants.BUFFER_SIZE_128Ki));
  }
}
