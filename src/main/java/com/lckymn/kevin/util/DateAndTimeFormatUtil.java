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
package com.lckymn.kevin.util;

import static org.elixirian.kommonlee.util.Strings.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import org.elixirian.kommonlee.util.CommonConstants;

/**
 * @author Lee, SeongHyun (Kevin)
 * @version 0.0.1 (2013-08-31)
 */
public final class DateAndTimeFormatUtil
{
  public static final String ISO8601_DATE_FORMAT_PATTERN = "yyyy-MM-dd";

  public static final String UTC_FORMAT = ISO8601_DATE_FORMAT_PATTERN + "'T'HH:mm:ssZ";

  public static final String UTC = "UTC";

  private DateAndTimeFormatUtil() throws IllegalAccessException
  {
    throw new IllegalAccessException(getClass().getName() + CommonConstants.CANNOT_BE_INSTANTIATED);
  }

  public static Date parseUtcDateAndTimeIfNeitherNullNorEmpty(final String dateAndTime)
  {
    if (isNullOrEmptyString(dateAndTime))
    {
      return null;
    }
    final SimpleDateFormat dateFormat = new SimpleDateFormat(UTC_FORMAT);
    dateFormat.setTimeZone(TimeZone.getTimeZone(UTC));
    try
    {
      return dateFormat.parse(dateAndTime.replace("Z", "+0000"));
    }
    catch (final ParseException e)
    {
      throw new RuntimeParseException(e.getMessage(), e.getErrorOffset(), e);
    }
  }

  public static String formatUtcDateAndTimeIfNotNull(final Date dateAndTime)
  {
    if (null == dateAndTime)
    {
      return null;
    }
    final SimpleDateFormat dateFormat = new SimpleDateFormat(UTC_FORMAT);
    dateFormat.setTimeZone(TimeZone.getTimeZone(UTC));
    String formattedDateAndTime;
    formattedDateAndTime = dateFormat.format(dateAndTime);
    return formattedDateAndTime.replace("+0000", "Z");
  }

  public static Date parseUtcDateIfNeitherNullNorEmpty(final String dateString)
  {
    if (isNullOrEmptyString(dateString))
    {
      return null;
    }
    final SimpleDateFormat dateFormat = new SimpleDateFormat(ISO8601_DATE_FORMAT_PATTERN);
    dateFormat.setTimeZone(TimeZone.getTimeZone(UTC));
    try
    {
      return dateFormat.parse(dateString);
    }
    catch (final ParseException e)
    {
      throw new RuntimeParseException(e.getMessage(), e.getErrorOffset(), e);
    }
  }

  public static String formatUtcDateIfNotNull(final Date date)
  {
    if (null == date)
    {
      return null;
    }
    final SimpleDateFormat dateFormat = new SimpleDateFormat(ISO8601_DATE_FORMAT_PATTERN);
    dateFormat.setTimeZone(TimeZone.getTimeZone(UTC));
    String formattedDate;
    formattedDate = dateFormat.format(date);
    return formattedDate;
  }
}
