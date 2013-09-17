/**
 *
 */
package com.lckymn.kevin.trac2gitlab;

import java.util.List;
import java.util.Map;

import com.lckymn.kevin.gitlab.json.GitLabIssue.GitLabIssueForCreation;
import com.lckymn.kevin.gitlab.json.GitLabMilestone;
import com.lckymn.kevin.gitlab.json.GitLabProject;
import com.lckymn.kevin.gitlab.json.GitLabUser;
import com.lckymn.kevin.trac.json.TracIssue;

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
public interface Trac2GitLabIssueConverter
{
  GitLabIssueForCreation convert(GitLabProject gitLabProject, GitLabUser assignee, GitLabMilestone milestone,
      Map<String, String> labelMap, TracIssue tracIssue);

  List<String> extractLabels(Map<String, String> labelMap, TracIssue tracIssue);
}
