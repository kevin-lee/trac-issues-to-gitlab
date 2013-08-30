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

import java.util.List;

import org.elixirian.jsonstatham.annotation.Json;
import org.elixirian.jsonstatham.annotation.JsonField;

/**
 * @author Lee, SeongHyun (Kevin)
 * @version 0.0.1 (2013-08-29)
 */
@Json
public class GitLabIssueV3
{
  /**
   * id (required) - The ID of a project
   */
  @JsonField
  private final Long id;

  /**
   * title (required) - The title of an issue
   */
  @JsonField
  private final String title;

  /**
   * description (optional) - The description of an issue
   */
  @JsonField
  private final String description;

  /**
   * assignee_id (optional) - The ID of a user to assign issue
   */
  @JsonField(name = "assignee_id")
  private final Long assigneeId;

  /**
   * milestone_id (optional) - The ID of a milestone to assign issue
   */
  @JsonField(name = "milestone_id")
  private final Long milestoneId;

  /**
   * labels (optional) - Comma-separated label names for an issue
   */
  @JsonField
  private final List<String> labels;

  public GitLabIssueV3(final Long id, final String title, final String description, final Long assigneeId,
      final Long milestoneId, final List<String> labels)
  {
    this.id = id;
    this.title = title;
    this.description = description;
    this.assigneeId = assigneeId;
    this.milestoneId = milestoneId;
    this.labels = labels;
  }

  public Long getId()
  {
    return id;
  }

  public String getTitle()
  {
    return title;
  }

  public String getDescription()
  {
    return description;
  }

  public Long getAssigneeId()
  {
    return assigneeId;
  }

  public Long getMilestoneId()
  {
    return milestoneId;
  }

  public List<String> getLabels()
  {
    return labels;
  }

  @Override
  public String toString()
  {
    /* @formatter:off */
    return toStringBuilder(this)
            .add("id", id)
            .add("title", title)
            .add("description", description)
            .add("assigneeId", assigneeId)
            .add("milestoneId", milestoneId)
            .add("labels", labels)
          .toString();
    /* @formatter:on */
  }
}
