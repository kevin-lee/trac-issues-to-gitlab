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
package com.lckymn.kevin.trac2gitlab;

import static org.elixirian.kommonlee.util.Objects.*;
import static org.elixirian.kommonlee.util.Strings.*;
import static org.elixirian.kommonlee.util.collect.Lists.*;
import static org.elixirian.kommonlee.util.string.RegularExpressionUtil.*;

import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.elixirian.kommonlee.util.CommonConstants;

import com.lckymn.kevin.trac.json.TracIssue;

/**
 * @author Lee, SeongHyun (Kevin)
 * @version 0.0.1 (2013-09-25)
 */
public final class Trac2GitLabUtil
{
  private static final String OPENING_TRIPLE_BRACES = "{{{";
  private static final int OPENING_TRIPLE_BRACES_LENGTH = OPENING_TRIPLE_BRACES.length();
  private static final String CLOSING_TRIPLE_BRACES = "}}}";
  private static final int CLOSING_TRIPLE_BRACES_LENGTH = CLOSING_TRIPLE_BRACES.length();

  private static final String APPLICATION = "#!application/";
  private static final int APPLICATION_LENGTH = APPLICATION.length();
  /**
   * {{{[\s]*#!application/
   */
  private static final Pattern TRAC_WIKI_PATTERN_WITH_APPLICATION = Pattern.compile("\\{\\{\\{[\\s]*" + APPLICATION);

  private static final String TEXT = "#!text/";
  private static final int TEXT_LENGTH = TEXT.length();
  /**
   * {{{[\s]*#!text/
   */
  private static final Pattern TRAC_WIKI_PATTERN_WITH_TEXT = Pattern.compile("\\{\\{\\{[\\s]*" + TEXT);

  private static final String MIME_TYPE_SIGNS = "#!";
  private static final int MIME_TYPE_SIGNS_LENGTH = MIME_TYPE_SIGNS.length();
  /**
   * {{{#!
   */
  private static final Pattern TRAC_WIKI_PATTERN_WITH_MIME_TYPE = Pattern.compile("\\{\\{\\{[\\s]*" + MIME_TYPE_SIGNS);
  private static final Pattern TRAC_WIKI_PATTERN_WITH_NEW_LINES = Pattern.compile("\\{\\{\\{[ \\t]*[\\n\\r]+");
  private static final Pattern TRAC_WIKI_PATTERN = Pattern.compile("\\{\\{\\{");
  private static final Pattern TRAC_WIKI_PATTERN_CLOSE = Pattern.compile("[\\n\\r][ \\t]*\\}\\}\\}");
  private static final Pattern TRAC_WIKI_PATTERN_ONE_LINE_CLOSE = Pattern.compile("[^\\n^\\r][ \\t]*\\}\\}\\}");

  private static final String GIT_LAB_CODE_BLOCK = "```";

  private Trac2GitLabUtil() throws IllegalAccessException
  {
    throw new IllegalAccessException(getClass().getName() + CommonConstants.CANNOT_BE_INSTANTIATED);
  }

  public static String convertCodeBlockForGitLabMarkDown(final String tracWiki)
  {
    final int length = tracWiki.length();
    final String result = tracWiki;

    final StringBuilder stringBuilder = new StringBuilder();

    int startIndex = 0;
    int closeIndex = 0;

    while (startIndex < length)
    {
      int indexFound = indexOf(result, TRAC_WIKI_PATTERN_WITH_APPLICATION, startIndex);
      if (0 <= indexFound)
      {
        final int contentStartIndex = result.indexOf(APPLICATION, indexFound) + APPLICATION_LENGTH;
        closeIndex = indexOf(result, TRAC_WIKI_PATTERN_CLOSE, contentStartIndex);

        if (0 <= closeIndex)
        {
          startIndex = replaceCodeBlock(result, stringBuilder, startIndex, closeIndex, indexFound, contentStartIndex);
          continue;
        }
      }

      indexFound = indexOf(result, TRAC_WIKI_PATTERN_WITH_TEXT, startIndex);
      if (0 <= indexFound)
      {
        final int contentStartIndex = result.indexOf(TEXT, indexFound) + TEXT_LENGTH;
        closeIndex = indexOf(result, TRAC_WIKI_PATTERN_CLOSE, contentStartIndex);

        if (0 <= closeIndex)
        {
          startIndex = replaceCodeBlock(result, stringBuilder, startIndex, closeIndex, indexFound, contentStartIndex);
          continue;
        }
      }

      indexFound = indexOf(result, TRAC_WIKI_PATTERN_WITH_MIME_TYPE, startIndex);
      if (0 <= indexFound)
      {
        final int contentStartIndex = result.indexOf(MIME_TYPE_SIGNS, indexFound) + MIME_TYPE_SIGNS_LENGTH;
        closeIndex = indexOf(result, TRAC_WIKI_PATTERN_CLOSE, contentStartIndex);

        if (0 <= closeIndex)
        {
          startIndex = replaceCodeBlock(result, stringBuilder, startIndex, closeIndex, indexFound, contentStartIndex);
          continue;
        }
      }

      indexFound = indexOf(result, TRAC_WIKI_PATTERN_WITH_NEW_LINES, startIndex);
      if (0 <= indexFound)
      {
        final int contentStartIndex = indexFound + OPENING_TRIPLE_BRACES_LENGTH;
        closeIndex = indexOf(result, TRAC_WIKI_PATTERN_CLOSE, contentStartIndex);

        if (0 <= closeIndex)
        {
          startIndex = replaceCodeBlock(result, stringBuilder, startIndex, closeIndex, indexFound, contentStartIndex);
          continue;
        }
      }

      indexFound = indexOf(result, TRAC_WIKI_PATTERN, startIndex);
      if (0 <= indexFound)
      {
        final int contentStartIndex = indexFound + OPENING_TRIPLE_BRACES_LENGTH;
        closeIndex = indexOf(result, TRAC_WIKI_PATTERN_ONE_LINE_CLOSE, contentStartIndex);

        if (0 <= closeIndex)
        {
          startIndex = replaceCodeBlock(result, stringBuilder, startIndex, closeIndex, indexFound, contentStartIndex);
          continue;
        }
      }
      stringBuilder.append(result.substring(startIndex));
      break;
    }
    return stringBuilder.toString();
  }

  private static int replaceCodeBlock(final String result, final StringBuilder stringBuilder, final int startIndex,
      final int closeIndex, final int indexFound, final int contentStartIndex)
  {
    final int closingTripleBracesIndex = result.indexOf(CLOSING_TRIPLE_BRACES, closeIndex);
    stringBuilder.append(result.substring(startIndex, indexFound))
        .append(GIT_LAB_CODE_BLOCK)
        .append(result.substring(contentStartIndex, closingTripleBracesIndex))
        .append(GIT_LAB_CODE_BLOCK);
    return closingTripleBracesIndex + CLOSING_TRIPLE_BRACES_LENGTH;
  }

  public static List<String> extractLabels(final Map<String, String> labelMap, final TracIssue tracIssue)
  {
    final List<String> labels = newArrayList();
    final String severity = nullSafeTrim(tracIssue.getSeverity()).toLowerCase();
    if (!severity.isEmpty())
    {
      final String labelFound = nullThenUse(labelMap.get(severity), "");
      if (!labelFound.isEmpty())
      {
        labels.add(labelFound);
      }
    }
    else
    {
      final String priority = nullSafeTrim(tracIssue.getPriority()).toLowerCase();
      if (!priority.isEmpty())
      {
        final String labelFound = nullThenUse(labelMap.get(priority), "");
        if (!labelFound.isEmpty())
        {
          labels.add(labelFound);
        }
      }
    }

    final String type = nullSafeTrim(tracIssue.getType()).toLowerCase();
    if (!type.isEmpty())
    {
      final String labelFound = nullThenUse(labelMap.get(type), "");
      if (!labelFound.isEmpty())
      {
        labels.add(labelFound);
      }
    }

    if ("closed".equalsIgnoreCase(tracIssue.getStatus()))
    {
      final String resolution = nullSafeTrim(tracIssue.getResolution()).toLowerCase();
      if (!resolution.isEmpty())
      {
        final String labelFound = nullThenUse(labelMap.get(resolution), "");
        if (!labelFound.isEmpty())
        {
          labels.add(labelFound);
        }
      }
    }
    return labels;
  }
}
