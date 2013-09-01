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

import java.util.Arrays;
import java.util.List;

import org.elixirian.jsonstatham.core.JsonStatham;

import com.lckymn.kevin.gitlab.api.GitLabMilestoneService;
import com.lckymn.kevin.gitlab.json.GitLabMilestone;
import com.lckymn.kevin.http.HttpRequestForJsonSource;

/**
 * @author Lee, SeongHyun (Kevin)
 * @version 0.0.1 (2013-08-31)
 */
public class GitLabMilestoneServiceImpl implements GitLabMilestoneService
{
  private final HttpRequestForJsonSource httpRequestForJsonSource;
  private final JsonStatham jsonStatham;
  private final String url;

  public GitLabMilestoneServiceImpl(final HttpRequestForJsonSource httpRequestForJsonSource,
      final JsonStatham jsonStatham, final String url)
  {
    this.httpRequestForJsonSource = httpRequestForJsonSource;
    this.jsonStatham = jsonStatham;
    this.url = buildApiUrlForProjects(url);
  }

  HttpRequestForJsonSource getHttpRequestForJsonSource()
  {
    return httpRequestForJsonSource;
  }

  JsonStatham getJsonStatham()
  {
    return jsonStatham;
  }

  String getUrl()
  {
    return url;
  }

  @Override
  public List<GitLabMilestone> getAllGitLabMilestones(final String privateToken, final Long projectId)
  {
    final String result = httpRequestForJsonSource.get(prepareUrlForMilestones(url, privateToken, projectId))
        .body();
    final GitLabMilestone[] gitLabMilestones = getResultOrThrowException(jsonStatham, GitLabMilestone[].class, result);
    return Arrays.asList(gitLabMilestones);
  }

}
