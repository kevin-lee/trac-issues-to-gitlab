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

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.elixirian.jsonstatham.annotation.Json;
import org.elixirian.jsonstatham.annotation.JsonField;
import org.elixirian.jsonstatham.annotation.ValueAccessor;

import com.lckymn.kevin.util.DateAndTimeFormatUtil;

/**
 * @author Lee, SeongHyun (Kevin)
 * @version 0.0.1 (2013-08-29)
 */
@Json
public class TracIssue
{
  @JsonField
  private final Integer id;

  @JsonField
  private final String summary;

  @JsonField
  private final List<String> keywords;

  /**
   * "accepted", "assigned", "closed", "new", "reopened"
   */
  @JsonField
  private final String status;

  @JsonField
  private final String resolution;

  @JsonField
  private final String type;

  @JsonField
  private final String version;

  @JsonField
  private final String milestone;

  @JsonField
  private final String reporter;

  @ValueAccessor(name = "getTimeInUtcString")
  @JsonField
  private final Date time;

  @JsonField
  private final String component;

  @JsonField
  private final String description;

  @JsonField
  private final String priority;

  @JsonField
  private final String severity;

  @JsonField
  private final String owner;

  @ValueAccessor(name = "getChangetimeInUtcString")
  @JsonField
  private final Date changetime;

  @JsonField
  private final List<String> cc;

  public TracIssue(final Integer id, final String summary, final List<String> keywords, final String status,
      final String resolution, final String type, final String version, final String milestone, final String reporter,
      final Date time, final String component, final String description, final String priority, final String severity,
      final String owner, final Date changetime, final List<String> cc)
  {
    this.id = id;
    this.summary = summary;
    this.keywords = keywords;
    this.status = status;
    this.resolution = resolution;
    this.type = type;
    this.version = version;
    this.milestone = milestone;
    this.reporter = reporter;
    this.time = time;
    this.component = component;
    this.description = description;
    this.priority = priority;
    this.severity = severity;
    this.owner = owner;
    this.changetime = changetime;
    this.cc = cc;
  }

  public Integer getId()
  {
    return id;
  }

  public String getSummary()
  {
    return summary;
  }

  public List<String> getKeywords()
  {
    return keywords;
  }

  public String getStatus()
  {
    return status;
  }

  public String getResolution()
  {
    return resolution;
  }

  public String getType()
  {
    return type;
  }

  public String getVersion()
  {
    return version;
  }

  public String getMilestone()
  {
    return milestone;
  }

  public String getReporter()
  {
    return reporter;
  }

  public Date getTime()
  {
    return time;
  }

  public String getTimeInUtcString()
  {
    return DateAndTimeFormatUtil.formatUtcDateAndTimeIfNotNull(time);
  }

  public String getComponent()
  {
    return component;
  }

  public String getDescription()
  {
    return description;
  }

  public String getPriority()
  {
    return priority;
  }

  public String getSeverity()
  {
    return severity;
  }

  public String getOwner()
  {
    return owner;
  }

  public Date getChangetime()
  {
    return changetime;
  }

  public String getChangetimeInUtcString()
  {
    return DateAndTimeFormatUtil.formatUtcDateAndTimeIfNotNull(changetime);
  }

  public List<String> getCc()
  {
    return cc;
  }

  @Override
  public int hashCode()
  {
    return hash(id, summary, keywords, status, resolution, type, version, milestone, reporter, time, component,
        description, priority, severity, owner, changetime, cc);
  }

  @Override
  public boolean equals(final Object tracIssue)
  {
    if (this == tracIssue)
    {
      return true;
    }
    final TracIssue that = castIfInstanceOf(TracIssue.class, tracIssue);
    /* @formatter:off */
    return null != that &&
            (equal(this.id,           that.id) &&
             equal(this.summary,      that.summary) &&
             equal(this.keywords,     that.keywords) &&
             equal(this.status,       that.status) &&
             equal(this.resolution,   that.resolution) &&
             equal(this.type,         that.type) &&
             equal(this.version,      that.version) &&
             equal(this.milestone,    that.milestone) &&
             equal(this.reporter,     that.reporter) &&
             equal(this.time,         that.time) &&
             equal(this.component,    that.component) &&
             equal(this.description,  that.description) &&
             equal(this.priority,     that.priority) &&
             equal(this.severity,     that.severity) &&
             equal(this.owner,        that.owner) &&
             equal(this.changetime,   that.changetime) &&
             equal(this.cc,           that.cc));
    /* @formatter:on */
  }

  @Override
  public String toString()
  {
    /* @formatter:off */
    return toStringBuilder(this)
            .add("id", id)
            .add("summary", summary)
            .add("keywords", keywords)
            .add("status", status)
            .add("resolution", resolution)
            .add("type", type)
            .add("version", version)
            .add("milestone", milestone)
            .add("reporter", reporter)
            .add("time", time)
            .add("component", component)
            .add("description", description)
            .add("priority", priority)
            .add("severity", severity)
            .add("owner", owner)
            .add("changetime", changetime)
            .add("cc", cc)
          .toString();
    /* @formatter:on */
  }

  public static TracIssue newInstance(final Integer id, final Map<String, Object> map)
  {
    /* @formatter:off */
    final String keywords = (String) map.get("keywords");
    final String cc = (String) map.get("cc");
    return new TracIssue(id,
                         (String) map.get("summary"),
                         isNullOrEmptyString(keywords) ?
                             Collections.<String> emptyList() :
                             Arrays.asList(keywords.split("[\\s]*,[\\s]*")),
                         (String) map.get("status"),
                         (String) map.get("resolution"),
                         (String) map.get("type"),
                         (String) map.get("version"),
                         (String) map.get("milestone"),
                         (String) map.get("reporter"),
                         (Date) map.get("time"),
                         (String) map.get("component"),
                         (String) map.get("description"),
                         (String) map.get("priority"),
                         (String) map.get("severity"),
                         (String) map.get("owner"),
                         (Date) map.get("changetime"),
                         isNullOrEmptyString(cc) ?
                             Collections.<String> emptyList() :
                             Arrays.asList(cc.split("[\\s]*,[\\s]*")));
    /* @formatter:on */
  }
}
