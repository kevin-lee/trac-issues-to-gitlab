/**
 *
 */
package com.lckymn.kevin.gitlab.json;

import static com.lckymn.kevin.util.DateAndTimeFormatUtil.*;
import static org.elixirian.kommonlee.util.Objects.*;

import java.util.Date;

import org.elixirian.jsonstatham.annotation.Json;
import org.elixirian.jsonstatham.annotation.JsonField;
import org.elixirian.jsonstatham.annotation.ValueAccessor;

/**
 * @author Lee, SeongHyun (Kevin)
 * @version 0.0.1 (2013-09-01)
 */
@Json
public class GitLabMilestone
{
  public static class GitLabMilestoneForCreation
  {
    @JsonField
    public final String title;

    @JsonField
    public final String description;

    @ValueAccessor(name = "getDueDateInUtcString")
    @JsonField(name = "due_date")
    private final Date dueDate;

    public GitLabMilestoneForCreation(final String title, final String description, final Date dueDate)
    {
      this.title = title;
      this.description = description;
      this.dueDate = dueDate;
    }

    public String getDueDateInUtcString()
    {
      return formatUtcDateIfNotNull(dueDate);
    }
  }

  public static final GitLabMilestone EMPTY_GIT_LAB_MILESTONE = new GitLabMilestone(null, null, "", "", null, "", null,
      null) {
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

  public static final GitLabMilestone[] EMPTY_GIT_LAB_MILESTONE_ARRAY = new GitLabMilestone[0];

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

  public GitLabMilestone(final Integer id, final Integer projectId, final String title, final String description,
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
    return hash(id, projectId, title, description, dueDate, state, createdAt, updatedAt);
  }

  @Override
  public boolean equals(final Object gitLabMilestone)
  {
    if (this == gitLabMilestone)
    {
      return true;
    }
    final GitLabMilestone that = castIfInstanceOf(GitLabMilestone.class, gitLabMilestone);

    /* @formatter:off */
    return null != that &&
            (equal(id, that.id) &&
             equal(projectId, that.projectId) &&
             equal(title, that.title) &&
             equal(description, that.description) &&
             equal(dueDate, that.dueDate) &&
             equal(state, that.state) &&
             equal(createdAt, that.createdAt) &&
             equal(updatedAt, that.updatedAt));
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
