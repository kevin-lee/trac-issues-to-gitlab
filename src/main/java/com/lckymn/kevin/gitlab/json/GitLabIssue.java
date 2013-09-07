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

import static com.lckymn.kevin.util.DateAndTimeFormatUtil.*;
import static org.elixirian.kommonlee.util.Objects.*;

import java.util.Date;
import java.util.List;

import org.elixirian.jsonstatham.annotation.Json;
import org.elixirian.jsonstatham.annotation.JsonField;
import org.elixirian.jsonstatham.annotation.ValueAccessor;
import org.elixirian.kommonlee.util.string.StringGlues;
import org.elixirian.kommonlee.util.string.StringIterableToStringGlue;

/**
 * @author Lee, SeongHyun (Kevin)
 * @version 0.0.1 (2013-08-29)
 */
@Json
public class GitLabIssue
{
  public static final GitLabIssue[] EMPTY_GITLAB_ISSUE_ARRAY = new GitLabIssue[0];

  @Json
  public static class GitLabIssueForCreation
  {
    /**
     * id (required) - The ID of a project
     */
    @JsonField
    public final Integer id;

    /**
     * title (required) - The title of an issue
     */
    @JsonField
    public final String title;

    /**
     * description (optional) - The description of an issue
     */
    @JsonField
    public final String description;

    /**
     * labels (optional) - Comma-separated label names for an issue
     */
    @ValueAccessor(name = "getLabels")
    @JsonField
    private final List<String> labels;

    /**
     * assignee_id (optional) - The ID of a user to assign issue
     */
    @JsonField(name = "assignee_id")
    public final Integer assigneeId;

    /**
     * milestone_id (optional) - The ID of a milestone to assign issue
     */
    @JsonField(name = "milestone_id")
    public final Integer milestoneId;

    private static final StringIterableToStringGlue GLUE = StringGlues.builderForIterable()
        .withSeparator(", ")
        .ignoreNull()
        .ignore("")
        .build();

    public GitLabIssueForCreation(final Integer id, final String title, final String description,
        final List<String> labels, final Integer assigneeId, final Integer milestoneId)
    {
      this.id = id;
      this.title = title;
      this.description = description;
      this.labels = labels;
      this.assigneeId = assigneeId;
      this.milestoneId = milestoneId;
    }

    public String getLabels()
    {
      return GLUE.glue(labels);
    }

    @Override
    public int hashCode()
    {
      return hash(this.id, this.title, this.description, this.labels, this.assigneeId, this.milestoneId);
    }

    @Override
    public boolean equals(final Object gitLabIssueForCreation)
    {
      if (this == gitLabIssueForCreation)
      {
        return true;
      }
      final GitLabIssueForCreation that = castIfInstanceOf(GitLabIssueForCreation.class, gitLabIssueForCreation);
      /* @formatter:off */
      return null != that &&
              (equal(this.id, that.id) &&
               equal(this.title, that.title) &&
               equal(this.description, that.description) &&
               equal(this.labels, that.labels) &&
               equal(this.assigneeId, that.assigneeId) &&
               equal(this.milestoneId, that.milestoneId));
      /* @formatter:on */
    }

    @Override
    public String toString()
    {
      /* @formatter:off */
      return toStringBuilder(this)
              .add("id", id)
              .add("title", title)
              .add("description", description)
              .add("labels", labels)
              .add("assigneeId", assigneeId)
              .add("milestoneId", milestoneId)
            .toString();
      /* @formatter:on */
    }
  }

  @Json
  public static class Milestone
  {
    @JsonField
    public final Integer id;

    @JsonField(name = "project_id")
    public final Integer projectId;

    @JsonField
    public final String title;

    @JsonField
    public final String description;

    @ValueAccessor(name = "getDueDateInUtcString")
    @JsonField(name = "due_date")
    public final Date dueDate;

    @JsonField
    public final String state;

    @ValueAccessor(name = "getCreatedAtInUtcString")
    @JsonField(name = "created_at")
    public final Date createdAt;

    @ValueAccessor(name = "getUpdatedAtInUtcString")
    @JsonField(name = "updated_at")
    public final Date updatedAt;

    public Milestone(final Integer id, final Integer projectId, final String title, final String description,
        final String dueDate, final String state, final String createdAt, final String updatedAt)
    {
      this.id = id;
      this.projectId = projectId;
      this.title = title;
      this.description = description;
      this.dueDate = parseUtcDateIfNeitherNullNorEmpty(dueDate);
      this.state = state;
      this.createdAt = parseUtcDateAndTimeIfNeitherNullNorEmpty(createdAt);
      this.updatedAt = parseUtcDateAndTimeIfNeitherNullNorEmpty(updatedAt);
    }

    public String getDueDateInUtcString()
    {
      return formatUtcDateIfNotNull(dueDate);
    }

    public String getCreatedAtInUtcString()
    {
      return formatUtcDateAndTimeIfNotNull(createdAt);
    }

    public String getUpdatedAtInUtcString()
    {
      return formatUtcDateAndTimeIfNotNull(updatedAt);
    }

    @Override
    public int hashCode()
    {
      return hash(this.id, this.projectId, this.title, this.description, this.dueDate, this.state, this.createdAt,
          this.updatedAt);
    }

    @Override
    public boolean equals(final Object milestone)
    {
      if (this == milestone)
      {
        return true;
      }
      final Milestone that = castIfInstanceOf(Milestone.class, milestone);
      /* @formatter:off */
      return null != that &&
            (equal(this.id, that.id) &&
             equal(this.projectId, that.projectId) &&
             equal(this.title, that.title) &&
             equal(this.description, that.description) &&
             equal(this.dueDate, that.dueDate) &&
             equal(this.state, that.state) &&
             equal(this.createdAt, that.createdAt) &&
             equal(this.updatedAt, that.updatedAt));
      /* @formatter:on */
    }

    @Override
    public String toString()
    {
      /* @formatter:off */
      return toStringBuilder(this)
              .add("id", id)
              .add("projectId", projectId)
              .add("title", title)
              .add("description", description)
              .add("dueDate", dueDate)
              .add("state", state)
              .add("createdAt", createdAt)
              .add("updatedAt", updatedAt)
            .toString();
      /* @formatter:on */
    }
  }

  @Json
  public static class User
  {
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

    @ValueAccessor(name = "getCreatedAtInUtcString")
    @JsonField(name = "created_at")
    public final Date createdAt;

    public User(final Integer id, final String username, final String email, final String name, final String state,
        final String createdAt)
    {
      this.id = id;
      this.username = username;
      this.email = email;
      this.name = name;
      this.state = state;
      this.createdAt = parseUtcDateAndTimeIfNeitherNullNorEmpty(createdAt);
    }

    public String getCreatedAtInUtcString()
    {
      return formatUtcDateAndTimeIfNotNull(createdAt);
    }

    @Override
    public int hashCode()
    {
      return hash(this.id, this.username, this.email, this.name, this.state, this.createdAt);
    }

    @Override
    public boolean equals(final Object user)
    {
      if (this == user)
      {
        return true;
      }
      final User that = castIfInstanceOf(User.class, user);
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

  /**
   * id (required) - The ID of a project
   */
  @JsonField
  public final Integer id;

  @JsonField(name = "project_id")
  public final Integer projectId;

  /**
   * title (required) - The title of an issue
   */
  @JsonField
  public final String title;

  /**
   * description (optional) - The description of an issue
   */
  @JsonField
  public final String description;

  /**
   * labels (optional) - Comma-separated label names for an issue
   */
  @JsonField
  public final List<String> labels;

  @JsonField
  public final Milestone milestone;

  @JsonField
  public final User author;

  @JsonField
  public final User assignee;

  @JsonField
  public final String state;

  @ValueAccessor(name = "getCreatedAtInUtcString")
  @JsonField(name = "created_at")
  public final Date createdAt;

  @ValueAccessor(name = "getUpdatedAtInUtcString")
  @JsonField(name = "updated_at")
  public final Date updatedAt;

  public GitLabIssue(final Integer id, final Integer projectId, final String title, final String description,
      final List<String> labels, final Milestone milestone, final User author, final User assignee, final String state,
      final String createdAt, final String updatedAt)
  {
    this.id = id;
    this.projectId = projectId;
    this.title = title;
    this.description = description;
    this.labels = labels;
    this.milestone = milestone;
    this.author = author;
    this.assignee = assignee;
    this.state = state;
    this.createdAt = parseUtcDateAndTimeIfNeitherNullNorEmpty(createdAt);
    this.updatedAt = parseUtcDateAndTimeIfNeitherNullNorEmpty(updatedAt);
  }

  public String getCreatedAtInUtcString()
  {
    return formatUtcDateAndTimeIfNotNull(createdAt);
  }

  public String getUpdatedAtInUtcString()
  {
    return formatUtcDateAndTimeIfNotNull(updatedAt);
  }

  @Override
  public int hashCode()
  {
    return hash(this.id, this.projectId, this.title, this.description, this.labels, this.milestone, this.author,
        this.assignee, this.state, this.createdAt, this.updatedAt);
  }

  @Override
  public boolean equals(final Object gitLabIssue)
  {
    if (this == gitLabIssue)
    {
      return true;
    }
    final GitLabIssue that = castIfInstanceOf(GitLabIssue.class, gitLabIssue);
    /* @formatter:off */
    return null != that &&
            (equal(this.id, that.id) &&
             equal(this.projectId, that.projectId) &&
             equal(this.title, that.title) &&
             equal(this.description, that.description) &&
             equal(this.labels, that.labels) &&
             equal(this.milestone, that.milestone) &&
             equal(this.author, that.author) &&
             equal(this.assignee, that.assignee) &&
             equal(this.state, that.state) &&
             equal(this.createdAt, that.createdAt) &&
             equal(this.updatedAt, that.updatedAt));
    /* @formatter:on */
  }

  @Override
  public String toString()
  {
    /* @formatter:off */
    return toStringBuilder(this)
            .add("id", id)
            .add("projectId", projectId)
            .add("title", title)
            .add("description", description)
            .add("labels", labels)
            .add("milestone", milestone)
            .add("author", author)
            .add("assignee", assignee)
            .add("state", state)
            .add("createdAt", createdAt)
            .add("updatedAt", updatedAt)
          .toString();
    /* @formatter:on */
  }
}
