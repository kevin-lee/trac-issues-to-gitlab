/**
 *
 */
package com.lckymn.kevin.gitlab.json;

import java.util.Date;

import static com.lckymn.kevin.util.UtcDateAndTimeFormatUtil.*;
import static org.elixirian.kommonlee.util.Objects.*;

import org.elixirian.jsonstatham.annotation.Json;
import org.elixirian.jsonstatham.annotation.JsonField;
import org.elixirian.jsonstatham.annotation.ValueAccessor;

/**
 * <pre>
 *     ___  _____                                _____
 *    /   \/    /_________  ___ ____ __ ______  /    /   ______  ______
 *   /        / /  ___ \  \/  //___// //     / /    /   /  ___ \/  ___ \
 *  /        \ /  _____/\    //   //   __   / /    /___/  _____/  _____/
 * /____/\____\\_____/   \__//___//___/ /__/ /________/\_____/ \_____/
 * </pre>
 *
 * @author Lee, SeongHyun (Kevin)
 * @version 0.0.1 (2013-09-01)
 */
@Json
public class GitLabMilestone
{
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

  @JsonField
  public final Long id;

  @JsonField(name = "project_id")
  public final Long projectId;

  @JsonField
  public final String title;

  @JsonField
  public final String description;

  @ValueAccessor(name = "getDueDateInUtcString")
  @JsonField(name = "due_date")
  public final Date dueDate;

  @JsonField
  public final String state;

  @ValueAccessor(name = "getUpdatedAtInUtcString")
  @JsonField(name = "updated_at")
  public final Date updatedAt;

  @ValueAccessor(name = "getCreatedAtInUtcString")
  @JsonField(name = "created_at")
  public final Date createdAt;

  public GitLabMilestone(final Long id, final Long projectId, final String title, final String description,
      final String dueDate, final String state, final String updatedAt, final String createdAt)
  {
    this.id = id;
    this.projectId = projectId;
    this.title = title;
    this.description = description;
    this.dueDate = parseDateAndTimeIfNeitherNullNorEmpty(dueDate);
    this.state = state;
    this.updatedAt = parseDateAndTimeIfNeitherNullNorEmpty(updatedAt);
    this.createdAt = parseDateAndTimeIfNeitherNullNorEmpty(createdAt);
  }

  public String getDueDateInUtcString()
  {
    return formatDateAndTimeIfNotNull(dueDate);
  }

  public String getUpdatedAtInUtcString()
  {
    return formatDateAndTimeIfNotNull(updatedAt);
  }

  public String getCreatedAtInUtcString()
  {
    return formatDateAndTimeIfNotNull(createdAt);
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
            .add("updatedAt", updatedAt)
            .add("createdAt", createdAt)
          .toString();
    /* @formatter:on */
  }
}
