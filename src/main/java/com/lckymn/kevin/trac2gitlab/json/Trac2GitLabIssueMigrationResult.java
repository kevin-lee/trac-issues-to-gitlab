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
package com.lckymn.kevin.trac2gitlab.json;

import static org.elixirian.kommonlee.util.Objects.*;
import static org.elixirian.kommonlee.util.collect.Maps.*;

import java.util.Collections;
import java.util.Map;

import org.elixirian.jsonstatham.annotation.Json;
import org.elixirian.jsonstatham.annotation.JsonConstructor;
import org.elixirian.jsonstatham.annotation.JsonField;
import org.elixirian.kommonlee.type.GenericBuilder;

import com.lckymn.kevin.gitlab.json.GitLabIssue;
import com.lckymn.kevin.trac.json.TracIssue;

/**
 * @author Lee, SeongHyun (Kevin)
 * @version 0.0.1 (2013-10-20)
 */
@Json
public class Trac2GitLabIssueMigrationResult
{
  @Json
  public static class TracIssueAndGitLabIssue
  {
    @JsonField
    final TracIssue tracIssue;

    @JsonField
    final GitLabIssue gitLabIssue;

    public TracIssueAndGitLabIssue(final TracIssue tracIssue, final GitLabIssue gitLabIssue)
    {
      this.tracIssue = tracIssue;
      this.gitLabIssue = gitLabIssue;
    }

    @Override
    public int hashCode()
    {
      return hash(tracIssue, gitLabIssue);
    }

    @Override
    public boolean equals(final Object tracIssueAndGitLabIssue)
    {
      if (this == tracIssueAndGitLabIssue)
      {
        return true;
      }
      final TracIssueAndGitLabIssue that = castIfInstanceOf(TracIssueAndGitLabIssue.class, tracIssueAndGitLabIssue);
      /* @formatter:off */
      return null != that &&
                equal(this.tracIssue, that.tracIssue) &&
                equal(this.gitLabIssue, that.gitLabIssue);
      /* @formatter:on */
    }

    @Override
    public String toString()
    {
      /* @formatter:off */
      return toStringBuilder(this)
                .add("tracIssue", tracIssue)
                .add("gitLabIssue", gitLabIssue)
              .toString();
      /* @formatter:on */
    }
  }

  @JsonField
  private final Integer version;

  @JsonField
  private final Map<Integer, TracIssueAndGitLabIssue> gitLabIssueIdToTracIssueAndGitLabIssueMap;

  @JsonConstructor
  private Trac2GitLabIssueMigrationResult(final Integer version,
      final Map<Integer, TracIssueAndGitLabIssue> gitLabIssueIdToTracIssueAndGitLabIssueMap)
  {
    this.version = version;
    this.gitLabIssueIdToTracIssueAndGitLabIssueMap =
      Collections.unmodifiableMap(gitLabIssueIdToTracIssueAndGitLabIssueMap);
  }

  public Integer getVersion()
  {
    return version;
  }

  public Map<Integer, TracIssueAndGitLabIssue> getGitLabIssueIdToTracIssueAndGitLabIssueMap()
  {
    return gitLabIssueIdToTracIssueAndGitLabIssueMap;
  }

  @Override
  public int hashCode()
  {
    return hash(version);
  }

  @Override
  public boolean equals(final Object trac2GitLabIssueMigrationResult)
  {
    if (this == trac2GitLabIssueMigrationResult)
    {
      return true;
    }
    final Trac2GitLabIssueMigrationResult that =
      castIfInstanceOf(Trac2GitLabIssueMigrationResult.class, trac2GitLabIssueMigrationResult);
    /* @formatter:off */
    return that != null &&
          (equal(this.version, that.getVersion()) &&
           equal(this.gitLabIssueIdToTracIssueAndGitLabIssueMap, that.getGitLabIssueIdToTracIssueAndGitLabIssueMap()));
    /* @formatter:on */
  }

  @Override
  public String toString()
  {
    /* @formatter:off */
    return toStringBuilder(this)
            .add("version", version)
            .add("gitLabIssueIdToTracIssueAndGitLabIssue", gitLabIssueIdToTracIssueAndGitLabIssueMap)
          .toString();
    /* @formatter:on */
  }

  public static class Builder implements GenericBuilder<Trac2GitLabIssueMigrationResult>
  {
    private final Integer version;

    Map<Integer, TracIssueAndGitLabIssue> gitLabIssueIdToTracIssueAndGitLabIssueMap;

    public Builder(final Integer version)
    {
      this.version = version;
      this.gitLabIssueIdToTracIssueAndGitLabIssueMap = newLinkedHashMap();
    }

    public Builder add(final TracIssue tracIssue, final GitLabIssue gitLabIssue)
    {
      gitLabIssueIdToTracIssueAndGitLabIssueMap.put(gitLabIssue.id, new TracIssueAndGitLabIssue(tracIssue, gitLabIssue));
      return this;
    }

    @Override
    public Trac2GitLabIssueMigrationResult build()
    {
      return new Trac2GitLabIssueMigrationResult(version, newLinkedHashMap(gitLabIssueIdToTracIssueAndGitLabIssueMap));
    }
  }

  public static Builder builder(final Integer version)
  {
    return new Builder(version);
  }
}
