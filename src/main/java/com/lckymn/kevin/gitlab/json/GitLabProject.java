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
package com.lckymn.kevin.gitlab.json;

import static org.elixirian.kommonlee.util.Objects.*;

import java.util.Date;

import org.elixirian.jsonstatham.annotation.Json;
import org.elixirian.jsonstatham.annotation.JsonField;
import org.elixirian.jsonstatham.annotation.ValueAccessor;
import org.elixirian.kommonlee.type.GenericBuilder;

import com.lckymn.kevin.util.DateAndTimeFormatUtil;

/**
 * @author Lee, SeongHyun (Kevin)
 * @version 0.0.1 (2013-08-31)
 */
@Json
public class GitLabProject
{
  @Json
  public static class Owner
  {
    public static final Owner EMPTY_OWNER = new Owner(null, "", "", "", "", null) {
      @Override
      public boolean isEmpty()
      {
        return true;
      }

      @Override
      public boolean isNotEmpty()
      {
        return false;
      }
    };

    @JsonField
    public final Integer id;

    @JsonField
    public final String username;

    @JsonField
    public final String email;

    @JsonField
    public final String name;

    @JsonField
    public final String state;

    @ValueAccessor(name = "getCreatedAt")
    @JsonField(name = "created_at")
    public final Date createdAt;

    public Owner(final Integer id, final String username, final String email, final String name, final String state,
        final String createdAt)
    {
      this.id = id;
      this.username = username;
      this.email = email;
      this.name = name;
      this.state = state;
      this.createdAt = DateAndTimeFormatUtil.parseUtcDateAndTimeIfNeitherNullNorEmpty(createdAt);
    }

    public String getCreatedAt()
    {
      return DateAndTimeFormatUtil.formatUtcDateAndTimeIfNotNull(createdAt);
    }

    public boolean isEmpty()
    {
      return false;
    }

    public boolean isNotEmpty()
    {
      return true;
    }

    @Override
    public int hashCode()
    {
      return hash(this.id, this.username, this.email, this.name, this.state, this.createdAt);
    }

    @Override
    public boolean equals(final Object owner)
    {
      if (this == owner)
      {
        return true;
      }
      final Owner that = castIfInstanceOf(Owner.class, owner);
      /* @formatter:off */
      return null != that &&
              (equal(this.id, that.id) &&
               equal(this.username, that.username) &&
               equal(this.email, that.email) &&
               equal(this.name, that.name) &&
               equal(this.state, that.state) &&
               equal(this.createdAt, that.createdAt));
      /* @formatter:on */
    }

    @Override
    public String toString()
    {
      /* @formatter:off */
      return toStringBuilder(this)
              .add("id", id)
              .add("username", username)
              .add("email", email)
              .add("name", name)
              .add("state", state)
              .add("createdAt", createdAt)
            .toString();
      /* @formatter:on */
    }
  }

  @Json
  public static class Namespace
  {
    public static final Namespace EMPTY_NAMESPACE = new Namespace(null, "", null, "", null, "", null) {
      @Override
      public boolean isEmpty()
      {
        return true;
      }

      @Override
      public boolean isNotEmpty()
      {
        return false;
      }
    };

    @JsonField
    public final Integer id;

    @JsonField
    public final String name;

    @ValueAccessor(name = "getCreatedAtInUtcString")
    @JsonField(name = "created_at")
    public final Date createdAt;

    @JsonField
    public final String description;

    @JsonField(name = "owner_id")
    public final Integer ownerId;

    @JsonField
    public final String path;

    @ValueAccessor(name = "getUpdatedAtInUtcString")
    @JsonField(name = "updated_at")
    public final Date updatedAt;

    public Namespace(final Integer id, final String name, final String createdAt, final String description,
        final Integer ownerId, final String path, final String updatedAt)
    {
      this.id = id;
      this.name = name;
      this.createdAt = DateAndTimeFormatUtil.parseUtcDateAndTimeIfNeitherNullNorEmpty(createdAt);
      this.description = description;
      this.ownerId = ownerId;
      this.path = path;
      this.updatedAt = DateAndTimeFormatUtil.parseUtcDateAndTimeIfNeitherNullNorEmpty(updatedAt);
    }

    public String getCreatedAtInUtcString()
    {
      return DateAndTimeFormatUtil.formatUtcDateAndTimeIfNotNull(createdAt);
    }

    public String getUpdatedAtInUtcString()
    {
      return DateAndTimeFormatUtil.formatUtcDateAndTimeIfNotNull(updatedAt);
    }

    public boolean isEmpty()
    {
      return false;
    }

    public boolean isNotEmpty()
    {
      return true;
    }

    @Override
    public int hashCode()
    {
      return hash(this.id, this.name, this.createdAt, this.description, this.ownerId, this.path, this.updatedAt);
    }

    @Override
    public boolean equals(final Object namespace)
    {
      if (this == namespace)
      {
        return true;
      }
      final Namespace that = castIfInstanceOf(Namespace.class, namespace);
      /* @formatter:off */
      return null != that &&
              (equal(this.id, that.id) &&
               equal(this.name, that.name) &&
               equal(this.createdAt, that.createdAt) &&
               equal(this.description, that.description) &&
               equal(this.ownerId, that.ownerId) &&
               equal(this.path, that.path) &&
               equal(this.updatedAt, that.updatedAt));
      /* @formatter:on */
    }

    @Override
    public String toString()
    {
      /* @formatter:off */
      return toStringBuilder(this)
              .add("id", id)
              .add("name", name)
              .add("createdAt", createdAt)
              .add("description", description)
              .add("ownerId", ownerId)
              .add("path", path)
              .add("updatedAt", updatedAt)
            .toString();
      /* @formatter:on */
    }
  }

  public static final GitLabProject EMPTY_GIT_LAB_PROJECT = new GitLabProject(null, "", null, false, "", "", "",
      Owner.EMPTY_OWNER, "", "", "", "", false, false, false, false, false, null, null, Namespace.EMPTY_NAMESPACE) {

    @Override
    public boolean isEmpty()
    {
      return true;
    }

    @Override
    public boolean isNotEmpty()
    {
      return false;
    }
  };

  @JsonField
  public final Integer id;

  @JsonField
  public final String description;

  @JsonField(name = "default_branch")
  public final String defaultBranch;

  @JsonField(name = "public")
  public final boolean isPublic;

  @JsonField(name = "ssh_url_to_repo")
  public final String sshUrlToRepo;

  @JsonField(name = "http_url_to_repo")
  public final String httpUrlToRepo;

  @JsonField(name = "web_url")
  public final String webUrl;

  @JsonField
  public final Owner owner;

  @JsonField
  public final String name;

  @JsonField(name = "name_with_namespace")
  public final String nameWithNamespace;

  @JsonField
  public final String path;

  @JsonField(name = "path_with_namespace")
  public final String pathWithNamespace;

  @JsonField(name = "issues_enabled")
  public final boolean issuesEnabled;

  @JsonField(name = "merge_requests_enabled")
  public final boolean mergeRequestsEnabled;

  @JsonField(name = "wall_enabled")
  public final boolean wallEnabled;

  @JsonField(name = "wiki_enabled")
  public final boolean wikiEnabled;

  @JsonField(name = "snippets_enabled")
  public final boolean snippetsEnabled;

  @ValueAccessor(name = "getCreatedAtInUtcString")
  @JsonField(name = "created_at")
  public final Date createdAt;

  @ValueAccessor(name = "getLastActivityAtInUtcString")
  @JsonField(name = "last_activity_at")
  public final Date lastActivityAt;

  @JsonField
  public final Namespace namespace;

  public GitLabProject(final Integer id, final String description, final String defaultBranch, final boolean isPublic,
      final String sshUrlToRepo, final String httpUrlToRepo, final String webUrl, final Owner owner, final String name,
      final String nameWithNamespace, final String path, final String pathWithNamespace, final boolean issuesEnabled,
      final boolean mergeRequestsEnabled, final boolean wallEnabled, final boolean wikiEnabled,
      final boolean snippetsEnabled, final String createdAt, final String lastActivityAt, final Namespace namespace)
  {
    this.id = id;
    this.description = description;
    this.defaultBranch = defaultBranch;
    this.isPublic = isPublic;
    this.sshUrlToRepo = sshUrlToRepo;
    this.httpUrlToRepo = httpUrlToRepo;
    this.webUrl = webUrl;
    this.owner = owner;
    this.name = name;
    this.nameWithNamespace = nameWithNamespace;
    this.path = path;
    this.pathWithNamespace = pathWithNamespace;
    this.issuesEnabled = issuesEnabled;
    this.mergeRequestsEnabled = mergeRequestsEnabled;
    this.wallEnabled = wallEnabled;
    this.wikiEnabled = wikiEnabled;
    this.snippetsEnabled = snippetsEnabled;
    this.createdAt = DateAndTimeFormatUtil.parseUtcDateAndTimeIfNeitherNullNorEmpty(createdAt);
    this.lastActivityAt = DateAndTimeFormatUtil.parseUtcDateAndTimeIfNeitherNullNorEmpty(lastActivityAt);
    this.namespace = namespace;
  }

  public static class Builder implements GenericBuilder<GitLabProject>
  {
    Integer id;
    String description;
    String defaultBranch;
    boolean isPublic;
    String sshUrlToRepo;
    String httpUrlToRepo;
    String webUrl;
    Owner owner;
    String name;
    String nameWithNamespace;
    String path;
    String pathWithNamespace;
    boolean issuesEnabled;
    boolean mergeRequestsEnabled;
    boolean wallEnabled;
    boolean wikiEnabled;
    boolean snippetsEnabled;
    String createdAt;
    String lastActivityAt;
    Namespace namespace;

    public Builder id(final Integer id)
    {
      this.id = id;
      return this;
    }

    public Builder description(final String description)
    {
      this.description = description;
      return this;
    }

    public Builder defaultBranch(final String defaultBranch)
    {
      this.defaultBranch = defaultBranch;
      return this;
    }

    public Builder itIsPublic()
    {
      this.isPublic = true;
      return this;
    }

    public Builder itIsNotPublic()
    {
      this.isPublic = false;
      return this;
    }

    public Builder sshUrlToRepo(final String sshUrlToRepo)
    {
      this.sshUrlToRepo = sshUrlToRepo;
      return this;
    }

    public Builder httpUrlToRepo(final String httpUrlToRepo)
    {
      this.httpUrlToRepo = httpUrlToRepo;
      return this;
    }

    public Builder webUrl(final String webUrl)
    {
      this.webUrl = webUrl;
      return this;
    }

    public Builder owner(final Owner owner)
    {
      this.owner = owner;
      return this;
    }

    public Builder name(final String name)
    {
      this.name = name;
      return this;
    }

    public Builder nameWithNamespace(final String nameWithNamespace)
    {
      this.nameWithNamespace = nameWithNamespace;
      return this;
    }

    public Builder path(final String path)
    {
      this.path = path;
      return this;
    }

    public Builder pathWithNamespace(final String pathWithNamespace)
    {
      this.pathWithNamespace = pathWithNamespace;
      return this;
    }

    public Builder issuesEnabled()
    {
      this.issuesEnabled = true;
      return this;
    }

    public Builder issuesDisabled()
    {
      this.issuesEnabled = false;
      return this;
    }

    public Builder mergeRequestsEnabled()
    {
      this.mergeRequestsEnabled = true;
      return this;
    }

    public Builder mergeRequestsDisabled()
    {
      this.mergeRequestsEnabled = false;
      return this;
    }

    public Builder wallEnabled()
    {
      this.wallEnabled = true;
      return this;
    }

    public Builder wallDisabled()
    {
      this.wallEnabled = false;
      return this;
    }

    public Builder wikiEnabled()
    {
      this.wikiEnabled = true;
      return this;
    }

    public Builder wikiDisabled()
    {
      this.wikiEnabled = false;
      return this;
    }

    public Builder snippetsEnabled()
    {
      this.snippetsEnabled = true;
      return this;
    }

    public Builder snippetsDisabled()
    {
      this.snippetsEnabled = false;
      return this;
    }

    public Builder createdAt(final String createdAt)
    {
      this.createdAt = createdAt;
      return this;
    }

    public Builder lastActivityAt(final String lastActivityAt)
    {
      this.lastActivityAt = lastActivityAt;
      return this;
    }

    public Builder namespace(final Namespace namespace)
    {
      this.namespace = namespace;
      return this;
    }

    @Override
    public GitLabProject build()
    {
      return new GitLabProject(id, description, defaultBranch, isPublic, sshUrlToRepo, httpUrlToRepo, webUrl, owner,
          name, nameWithNamespace, path, pathWithNamespace, issuesEnabled, mergeRequestsEnabled, wallEnabled,
          wikiEnabled, snippetsEnabled, createdAt, lastActivityAt, namespace);
    }
  }

  public static Builder builder()
  {
    return new Builder();
  }

  public String getCreatedAtInUtcString()
  {
    return DateAndTimeFormatUtil.formatUtcDateAndTimeIfNotNull(createdAt);
  }

  public String getLastActivityAtInUtcString()
  {
    return DateAndTimeFormatUtil.formatUtcDateAndTimeIfNotNull(lastActivityAt);
  }

  public boolean isEmpty()
  {
    return false;
  }

  public boolean isNotEmpty()
  {
    return true;
  }

  @Override
  public int hashCode()
  {
    return hash(this.id, this.description, this.defaultBranch, this.isPublic, this.sshUrlToRepo, this.httpUrlToRepo,
        this.webUrl, this.owner, this.name, this.nameWithNamespace, this.path, this.pathWithNamespace,
        this.issuesEnabled, this.mergeRequestsEnabled, this.wallEnabled, this.wikiEnabled, this.snippetsEnabled,
        this.createdAt, this.lastActivityAt, this.namespace);
  }

  @Override
  public boolean equals(final Object gitLabProject)
  {
    if (this == gitLabProject)
    {
      return true;
    }
    final GitLabProject that = castIfInstanceOf(GitLabProject.class, gitLabProject);
    /* @formatter:off */
    return null != that &&
            (equal(this.id, that.id) &&
             equal(this.description, that.description) &&
             equal(this.defaultBranch, that.defaultBranch) &&
             equal(this.isPublic, that.isPublic) &&
             equal(this.sshUrlToRepo, that.sshUrlToRepo) &&
             equal(this.httpUrlToRepo, that.httpUrlToRepo) &&
             equal(this.webUrl, that.webUrl) &&
             equal(this.owner, that.owner) &&
             equal(this.name, that.name) &&
             equal(this.nameWithNamespace, that.nameWithNamespace) &&
             equal(this.path, that.path) &&
             equal(this.pathWithNamespace, that.pathWithNamespace) &&
             equal(this.issuesEnabled, that.issuesEnabled) &&
             equal(this.mergeRequestsEnabled, that.mergeRequestsEnabled) &&
             equal(this.wallEnabled, that.wallEnabled) &&
             equal(this.wikiEnabled, that.wikiEnabled) &&
             equal(this.snippetsEnabled, that.snippetsEnabled) &&
             equal(this.createdAt, that.createdAt) &&
             equal(this.lastActivityAt, that.lastActivityAt) &&
             equal(this.namespace, that.namespace));
    /* @formatter:on */
  }

  @Override
  public String toString()
  {
    /* @formatter:off */
    return toStringBuilder(this)
            .add("id", id)
            .add("description", description)
            .add("defaultBranch", defaultBranch)
            .add("isPublic", isPublic)
            .add("sshUrlToRepo", sshUrlToRepo)
            .add("httpUrlToRepo", httpUrlToRepo)
            .add("webUrl", webUrl)
            .add("owner", owner)
            .add("name", name)
            .add("nameWithNamespace", nameWithNamespace)
            .add("path", path)
            .add("pathWithNamespace", pathWithNamespace)
            .add("issuesEnabled", issuesEnabled)
            .add("mergeRequestsEnabled", mergeRequestsEnabled)
            .add("wallEnabled", wallEnabled)
            .add("wikiEnabled", wikiEnabled)
            .add("snippetsEnabled", snippetsEnabled)
            .add("createdAt", createdAt)
            .add("lastActivityAt", lastActivityAt)
            .add("namespace", namespace)
          .toString();
    /* @formatter:on */
  }
}
