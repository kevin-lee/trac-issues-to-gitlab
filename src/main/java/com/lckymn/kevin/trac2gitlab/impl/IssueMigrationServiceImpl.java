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

import static org.elixirian.kommonlee.functional.collect.CollectionUtil.*;
import static org.elixirian.kommonlee.util.Strings.*;
import static org.elixirian.kommonlee.util.collect.Maps.*;
import static org.elixirian.kommonlee.util.type.Tuples.*;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.elixirian.kommonlee.type.Tuple2;
import org.elixirian.kommonlee.type.functional.Function1;

import com.lckymn.kevin.gitlab.api.GitLabIssueService;
import com.lckymn.kevin.gitlab.api.GitLabMilestoneService;
import com.lckymn.kevin.gitlab.api.GitLabUserService;
import com.lckymn.kevin.gitlab.json.GitLabIssue;
import com.lckymn.kevin.gitlab.json.GitLabIssue.GitLabIssueForCreation;
import com.lckymn.kevin.gitlab.json.GitLabMilestone;
import com.lckymn.kevin.gitlab.json.GitLabMilestone.GitLabMilestoneForCreation;
import com.lckymn.kevin.gitlab.json.GitLabProject;
import com.lckymn.kevin.gitlab.json.GitLabUser;
import com.lckymn.kevin.trac.json.TracIssue;
import com.lckymn.kevin.trac.json.TracMilestone;
import com.lckymn.kevin.trac2gitlab.IssueMigrationService;
import com.lckymn.kevin.trac2gitlab.Trac2GitLabIssueConverter;
import com.lckymn.kevin.trac2gitlab.json.Trac2GitLabIssueMigrationResult;
import com.lckymn.kevin.trac2gitlab.json.Trac2GitLabIssueMigrationResult.Builder;

/**
 * @author Lee, SeongHyun (Kevin)
 * @version 0.0.1 (2013-10-20)
 */
public class IssueMigrationServiceImpl implements IssueMigrationService
{
  private final Integer version;

  private final GitLabIssueService gitLabIssueService;
  private final GitLabMilestoneService gitLabMilestoneService;
  private final GitLabUserService gitLabUserService;
  private final Trac2GitLabIssueConverter trac2GitLabIssueConverter;

  public IssueMigrationServiceImpl(final Integer version, final GitLabIssueService gitLabIssueService,
      final GitLabMilestoneService gitLabMilestoneService, final GitLabUserService gitLabUserService,
      final Trac2GitLabIssueConverter trac2GitLabIssueConverter)
  {
    this.version = version;
    this.gitLabIssueService = gitLabIssueService;
    this.gitLabMilestoneService = gitLabMilestoneService;
    this.gitLabUserService = gitLabUserService;
    this.trac2GitLabIssueConverter = trac2GitLabIssueConverter;
  }

  @Override
  public Integer getVersion()
  {
    return version;
  }

  @Override
  public Trac2GitLabIssueMigrationResult migrate(final String privateToken, final GitLabProject gitLabProject,
      final Map<String, String> labelMap, final Map<String, TracMilestone> milestoneToTracMilestoneMap,
      final List<TracIssue> tracIssues)
  {
    final List<GitLabMilestoneForCreation> gitLabMilestoneForCreationList = mapper().fromIterable()
        .toArrayList()
        .map(new Function1<TracMilestone, GitLabMilestoneForCreation>() {
          @Override
          public GitLabMilestoneForCreation apply(final TracMilestone tracMilestone)
          {
            return GitLabMilestoneForCreation.newGitLabMilestoneForCreation(tracMilestone.getName(),
                tracMilestone.getDescription(), tracMilestone.getDue());
          }
        }, milestoneToTracMilestoneMap.values());

    final Integer projectId = gitLabProject.id;
    final List<GitLabMilestone> gitLabMilestones =
      gitLabMilestoneService.createMilestonesIfNotExist(privateToken, projectId, gitLabMilestoneForCreationList);

    final Map<String, Tuple2<TracMilestone, GitLabMilestone>> gitLabMilestoneToTracMilestoneMap = newLinkedHashMap();

    for (final GitLabMilestone gitLabMilestone : gitLabMilestones)
    {
      final TracMilestone tracMilestone = milestoneToTracMilestoneMap.get(gitLabMilestone.title);
      gitLabMilestoneToTracMilestoneMap.put(tracMilestone.getName(), tuple2(tracMilestone, gitLabMilestone));
    }

    final Map<String, GitLabUser> usernameToGitLabUserMap = mapper().fromIterable()
        .toHashMap()
        .map(new Function1<GitLabUser, Tuple2<String, GitLabUser>>() {
          @Override
          public Tuple2<String, GitLabUser> apply(final GitLabUser gitLabUser)
          {
            return tuple2(gitLabUser.username, gitLabUser);
          }
        }, gitLabUserService.getAllGitLabUsers(privateToken));

    final Map<GitLabIssueForCreation, TracIssue> gitLabIssueForCreationToTracIssueMap = newLinkedHashMap();
    for (final TracIssue tracIssue : tracIssues)
    {
      final GitLabMilestone gitLabMilestone = gitLabMilestoneToTracMilestoneMap.get(tracIssue.getMilestone())
          .getValue2();

      final String username = tracIssue.getOwner();
      final GitLabUser assignee = isNullOrEmptyString(username) ? null : usernameToGitLabUserMap.get(username);

      final GitLabIssueForCreation gitLabIssueForCreation =
        trac2GitLabIssueConverter.convert(gitLabProject, assignee, gitLabMilestone, labelMap, tracIssue);

      gitLabIssueForCreationToTracIssueMap.put(gitLabIssueForCreation, tracIssue);
    }

    final Builder builder = Trac2GitLabIssueMigrationResult.builder(version);

    for (final Entry<GitLabIssueForCreation, TracIssue> entry : gitLabIssueForCreationToTracIssueMap.entrySet())
    {
      final GitLabIssue gitLabIssue = gitLabIssueService.createIssue(privateToken, projectId, entry.getKey());

      /* @formatter:off */
      builder.add(
                  gitLabIssue.id,
                  entry.getValue(),
                  gitLabIssue);
      /* @formatter:on */
    }
    return builder.build();
  }
}
