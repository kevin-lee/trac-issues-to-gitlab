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

import static com.lckymn.kevin.gitlab.api.GitLabApiConstants.*;
import static com.lckymn.kevin.gitlab.api.GitLabApiUtil.*;
import static org.elixirian.kommonlee.util.Objects.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.elixirian.jsonstatham.core.JsonStatham;
import org.elixirian.kommonlee.util.collect.Maps;

import com.lckymn.kevin.gitlab.api.GitLabIssueService;
import com.lckymn.kevin.gitlab.json.GitLabIssue;
import com.lckymn.kevin.gitlab.json.GitLabIssue.GitLabIssueForCreation;
import com.lckymn.kevin.http.HttpRequestForJsonSource;

/**
 * @author Lee, SeongHyun (Kevin)
 * @version 0.0.1 (2013-08-31)
 */
public class GitLabIssueServiceImpl extends AbstractGitLabService implements GitLabIssueService
{
  private final String projectUrl;

  public GitLabIssueServiceImpl(final HttpRequestForJsonSource httpRequestForJsonSource, final JsonStatham jsonStatham,
      final String url)
  {
    super(httpRequestForJsonSource, jsonStatham, url);
    this.projectUrl = this.url + _PROJECTS;
  }

  String getProjectUrl()
  {
    return projectUrl;
  }

  @Override
  public List<GitLabIssue> getAllIssues(final String privateToken, final Integer projectId)
  {
    final String result = httpRequestForJsonSource.get(prepareUrlForIssues(projectUrl, privateToken, projectId))
        .body();

    final GitLabIssue[] resultOrThrowException = getResultOrThrowException(jsonStatham, GitLabIssue[].class, result);
    final GitLabIssue[] gitLabIssues = nullThenUse(resultOrThrowException, GitLabIssue.EMPTY_GITLAB_ISSUE_ARRAY);
    return Arrays.asList(gitLabIssues);
  }

  @Override
  public GitLabIssue createIssue(final String privateToken, final Integer projectId,
      final GitLabIssueForCreation gitLabIssueForCreation)
  {
    final Map<String, Object> form = Maps.<String, Object> hashMapBuilder()
        .put("id", gitLabIssueForCreation.projectId)
        .put("title", gitLabIssueForCreation.title)
        .put("description", gitLabIssueForCreation.description)
        .put("assignee_id", gitLabIssueForCreation.assigneeId)
        .put("milestone_id", gitLabIssueForCreation.milestoneId)
        .put("labels", gitLabIssueForCreation.getLabels())
        .build();
    final String result = httpRequestForJsonSource.post(prepareUrlForIssues(projectUrl, privateToken, projectId))
        .form(form)
        .body();

    final GitLabIssue resultOrThrowException = getResultOrThrowException(jsonStatham, GitLabIssue.class, result);
    return resultOrThrowException;
  }
}
