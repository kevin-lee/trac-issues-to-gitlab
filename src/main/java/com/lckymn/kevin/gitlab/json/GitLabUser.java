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

import org.elixirian.jsonstatham.annotation.Json;
import org.elixirian.jsonstatham.annotation.JsonField;
import org.elixirian.jsonstatham.annotation.ValueAccessor;

/**
 * @author Lee, SeongHyun (Kevin)
 * @version 0.0.1 (2013-09-06)
 */
@Json
public class GitLabUser
{
  public static final GitLabUser[] EMPTY_GIT_LAB_USER_ARRAY = new GitLabUser[0];

  @JsonField
  public final Integer id;

  @JsonField
  public final String username;

  @JsonField
  public final String email;

  @JsonField
  public final String name;

  @JsonField
  public final String bio;

  @JsonField
  public final String skype;

  @JsonField
  public final String linkedin;

  @JsonField
  public final String twitter;

  @JsonField
  public final Integer themeId;

  @JsonField(name = "color_scheme_id")
  public final Integer colorSchemeId;

  @JsonField
  public final String state;

  @ValueAccessor(name = "getCreatedAtInUtcString")
  @JsonField(name = "created_at")
  public final Date createdAt;

  @JsonField(name = "extern_uid")
  public final String externUid;

  @JsonField
  public final String provider;

  public GitLabUser(final Integer id, final String username, final String email, final String name, final String bio,
      final String skype, final String linkedin, final String twitter, final Integer themeId,
      final Integer colorSchemeId, final String state, final String createdAt, final String externUid,
      final String provider)
  {
    this.id = id;
    this.username = username;
    this.email = email;
    this.name = name;
    this.bio = bio;
    this.skype = skype;
    this.linkedin = linkedin;
    this.twitter = twitter;
    this.themeId = themeId;
    this.colorSchemeId = colorSchemeId;
    this.state = state;
    this.createdAt = parseUtcDateAndTimeIfNeitherNullNorEmpty(createdAt);
    this.externUid = externUid;
    this.provider = provider;
  }

  public String getCreatedAtInUtcString()
  {
    return formatUtcDateAndTimeIfNotNull(createdAt);
  }

  @Override
  public int hashCode()
  {
    return hash(this.id, this.username, this.email, this.name, this.bio, this.skype, this.linkedin, this.twitter,
        this.themeId, this.colorSchemeId, this.state, this.createdAt, this.externUid, this.provider);
  }

  @Override
  public boolean equals(final Object gitLabUser)
  {
    if (this == gitLabUser)
    {
      return true;
    }
    final GitLabUser that = castIfInstanceOf(GitLabUser.class, gitLabUser);
    /* @formatter:off */
    return null != that &&
             (equal(this.id, that.id) &&
              equal(this.username, that.username) &&
              equal(this.email, that.email) &&
              equal(this.name, that.name) &&
              equal(this.bio, that.bio) &&
              equal(this.skype, that.skype) &&
              equal(this.linkedin, that.linkedin) &&
              equal(this.twitter, that.twitter) &&
              equal(this.themeId, that.themeId) &&
              equal(this.colorSchemeId, that.colorSchemeId) &&
              equal(this.state, that.state) &&
              equal(this.createdAt, that.createdAt) &&
              equal(this.externUid, that.externUid) &&
              equal(this.provider, that.provider));
    /* @formatter:on */
  }
}
