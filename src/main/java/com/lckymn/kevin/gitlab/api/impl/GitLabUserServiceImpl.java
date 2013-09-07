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
import static org.elixirian.kommonlee.util.Objects.*;
import static org.elixirian.kommonlee.util.collect.Lists.*;
import static org.elixirian.kommonlee.util.collect.Sets.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.elixirian.jsonstatham.core.JsonStatham;

import com.lckymn.kevin.gitlab.api.GitLabUserService;
import com.lckymn.kevin.gitlab.json.GitLabUser;
import com.lckymn.kevin.http.HttpRequestForJsonSource;

/**
 * @author Lee, SeongHyun (Kevin)
 * @version 0.0.1 (2013-09-06)
 */
public class GitLabUserServiceImpl extends AbstractGitLabService implements GitLabUserService
{
  public GitLabUserServiceImpl(final HttpRequestForJsonSource httpRequestForJsonSource, final JsonStatham jsonStatham,
      final String url)
  {
    super(httpRequestForJsonSource, jsonStatham, url);
  }

  @Override
  public List<GitLabUser> getAllGitLabUsers(final String privateToken)
  {
    final String result = httpRequestForJsonSource.get(prepareUrl(url, privateToken))
        .body();
    final GitLabUser[] gitLabUsers =
      nullThenUse(getResultOrThrowException(jsonStatham, GitLabUser[].class, result),
          GitLabUser.EMPTY_GIT_LAB_USER_ARRAY);
    return Arrays.asList(gitLabUsers);
  }

  @Override
  public List<GitLabUser> getGitLabUsersByUsernames(final String privateToken, final Set<String> usernames)
  {
    final List<GitLabUser> gitLabUserList = getAllGitLabUsers(privateToken);

    final Set<String> usernamesToUse = newHashSet(usernames);

    final List<GitLabUser> result = newArrayList();
    for (final GitLabUser gitLabUser : gitLabUserList)
    {
      for (final String username : usernamesToUse)
      {
        if (gitLabUser.username.equalsIgnoreCase(username))
        {
          result.add(gitLabUser);
          usernamesToUse.remove(username);
          break;
        }
      }
    }
    return Collections.unmodifiableList(result);
  }

  @Override
  public List<GitLabUser> getGitLabUsersByUsernames(final String privateToken, final String username,
      final String... usernames)
  {
    final Set<String> usernameSet = newHashSet(usernames);
    usernameSet.add(username);
    return getGitLabUsersByUsernames(privateToken, usernameSet);
  }

}
