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
import static org.elixirian.kommonlee.util.Strings.*;

import java.util.Date;

import org.elixirian.jsonstatham.annotation.Json;
import org.elixirian.jsonstatham.annotation.JsonConstructor;
import org.elixirian.jsonstatham.annotation.JsonField;
import org.elixirian.jsonstatham.annotation.ValueAccessor;

import com.lckymn.kevin.util.DateAndTimeFormatUtil;

/**
 * @author Lee, SeongHyun (Kevin)
 * @version 0.0.1 (2013-08-29)
 */
@Json
public class TracIssueComment implements Comparable<TracIssueComment>
{
  @JsonField
  private final int originalIndex;

  @ValueAccessor(name = "getTimeInUtcString")
  @JsonField
  private final Date time;

  @JsonField
  private final String owner;

  @JsonField
  private final String comment;

  @JsonField
  private final Integer tracIssueId;

  @JsonConstructor
  public TracIssueComment(final int originalIndex, final Date time, final String owner, final String comment,
      final Integer tracIssueId)
  {
    this.originalIndex = originalIndex;
    this.time = time;
    this.owner = owner;
    this.comment = comment;
    this.tracIssueId = tracIssueId;
  }

  public int getOriginalIndex()
  {
    return originalIndex;
  }

  public Date getTime()
  {
    return time;
  }

  public String getTimeInUtcString()
  {
    return DateAndTimeFormatUtil.formatUtcDateAndTimeIfNotNull(time);
  }

  public String getOwner()
  {
    return owner;
  }

  public String getComment()
  {
    return comment;
  }

  public Integer getTracIssueId()
  {
    return tracIssueId;
  }

  @Override
  public int compareTo(final TracIssueComment that)
  {
    final int thisIndex = this.originalIndex;
    final int thatIndex = that.getOriginalIndex();
    return thisIndex < thatIndex ? -1 : thisIndex == thatIndex ? 0 : 1;
  }

  @Override
  public int hashCode()
  {
    return hash(originalIndex, time, owner, comment, tracIssueId);
  }

  @Override
  public boolean equals(final Object tracIssue)
  {
    if (this == tracIssue)
    {
      return true;
    }
    final TracIssueComment that = castIfInstanceOf(TracIssueComment.class, tracIssue);
    /* @formatter:off */
    return null != that &&
            (equal(this.originalIndex, that.originalIndex) &&
             equal(this.time,          that.time) &&
             equal(this.owner,         that.owner) &&
             equal(this.comment,       that.comment) &&
             equal(this.tracIssueId,   that.tracIssueId));
    /* @formatter:on */
  }

  @Override
  public String toString()
  {
    /* @formatter:off */
    return toStringBuilder(this)
            .add("originalIndex", originalIndex)
            .add("time", time)
            .add("owner", owner)
            .add("comment", comment)
            .add("tracIssueId", tracIssueId)
          .toString();
    /* @formatter:on */
  }

  public static boolean isComment(final Object[] elements)
  {
    return nullSafeTrim((String) elements[2]).equalsIgnoreCase("comment");
  }

  public static TracIssueComment newInstance(final Integer tracIssueId, final int index, final Object[] changeLog)
  {
    final Date time = (Date) changeLog[0];
    final String owner = (String) changeLog[1];
    final String comment = (String) changeLog[4];

    /* @formatter:off */
    return new TracIssueComment(index, time, owner, comment, tracIssueId);
    /* @formatter:on */
  }
}
