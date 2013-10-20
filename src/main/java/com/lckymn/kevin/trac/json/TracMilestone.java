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
package com.lckymn.kevin.trac.json;

import static org.elixirian.kommonlee.util.Objects.*;

import java.util.Date;
import java.util.Map;

import org.elixirian.jsonstatham.annotation.Json;
import org.elixirian.jsonstatham.annotation.JsonConstructor;
import org.elixirian.jsonstatham.annotation.JsonField;

/**
 * @author Lee, SeongHyun (Kevin)
 * @version 0.0.1 (2013-10-20)
 */
@Json
public class TracMilestone
{
  @JsonField
  private final String name;

  @JsonField
  private final String description;

  @JsonField
  private final Date due;

  @JsonField
  private final Date completed;

  @JsonConstructor
  private TracMilestone(final String name, final String description, final Date due, final Date completed)
  {
    this.name = name;
    this.description = description;
    this.due = due;
    this.completed = completed;
  }

  public String getName()
  {
    return name;
  }

  public String getDescription()
  {
    return description;
  }

  public Date getDue()
  {
    return due;
  }

  public Date getCompleted()
  {
    return completed;
  }

  @Override
  public int hashCode()
  {
    return hash(name, description, due, completed);
  }

  @Override
  public boolean equals(final Object tracMilestone)
  {
    if (this == tracMilestone)
    {
      return true;
    }
    final TracMilestone that = castIfInstanceOf(TracMilestone.class, tracMilestone);
    /* @formatter:off */
    return null != that &&
            (equal(this.name,
                   that.getName()) &&
             equal(this.description,
                 that.getDescription()) &&
             equal(this.due,
                 that.getDue()) &&
             equal(this.completed,
                 that.getCompleted()));
    /* @formatter:on */
  }

  @Override
  public String toString()
  {
    /* @formatter:off */
    return toStringBuilder(this)
            .add("name", name)
            .add("description", description)
            .add("due", due)
            .add("completed", completed)
          .toString();
    /* @formatter:on */
  }

  public static TracMilestone newTracMilestone(final String name, final String description, final Date due,
      final Date completed)
  {
    return new TracMilestone(name, description, due, completed);
  }

  public static TracMilestone newTracMilestone(final Map<String, Object> map)
  {
    final String description = (String) map.get("description");
    final String name = (String) map.get("name");
    final Object dueObject = map.get("due");
    final Date due = (Date) (dueObject instanceof Date ? dueObject : null);
    final Object completedObject = map.get("completed");
    final Date completed = (Date) (completedObject instanceof Date ? completedObject : null);
    return new TracMilestone(name, description, due, completed);
  }
}
