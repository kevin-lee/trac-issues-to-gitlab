/**
 *
 */
package com.lckymn.kevin.trac2gitlab.impl;

import static org.elixirian.kommonlee.util.Objects.*;
import static org.elixirian.kommonlee.util.Strings.*;
import static org.elixirian.kommonlee.util.collect.Lists.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.lckymn.kevin.gitlab.json.GitLabIssue;
import com.lckymn.kevin.gitlab.json.GitLabIssue.GitLabIssueForCreation;
import com.lckymn.kevin.gitlab.json.GitLabMilestone;
import com.lckymn.kevin.gitlab.json.GitLabProject;
import com.lckymn.kevin.gitlab.json.GitLabUser;
import com.lckymn.kevin.trac.json.TracIssue;
import com.lckymn.kevin.trac2gitlab.Trac2GitLabIssueConverter;

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
 * @version 0.0.1 (2013-09-12)
 */
public class Trac2GitLabIssueConverterImpl implements Trac2GitLabIssueConverter
{
  @Override
  public GitLabIssueForCreation convert(final GitLabProject gitLabProject, final GitLabUser assignee,
      final GitLabMilestone milestone, final Map<String, String> labelMap, final TracIssue tracIssue)
  {
    final List<String> labels = extractLabels(labelMap, tracIssue);

    final GitLabIssue.GitLabIssueForCreation gitLabIssueForCreation =
      new GitLabIssueForCreation(gitLabProject.id, tracIssue.getSummary(), tracIssue.getDescription(), labels,
          assignee.id, milestone.id);
    return gitLabIssueForCreation;
  }

  @Override
  public List<String> extractLabels(final Map<String, String> labelMap, final TracIssue tracIssue)
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
