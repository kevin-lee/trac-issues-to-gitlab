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

import java.io.File;
import java.util.List;
import java.util.Map;

import org.elixirian.kommonlee.io.exception.RuntimeIoException;

import com.lckymn.kevin.gitlab.json.GitLabProject;
import com.lckymn.kevin.trac.json.TracIssue;
import com.lckymn.kevin.trac.json.TracMilestone;
import com.lckymn.kevin.trac2gitlab.json.Trac2GitLabIssueMigrationResult;

/**
 * @author Lee, SeongHyun (Kevin)
 * @version 0.0.1 (2013-10-20)
 */
public interface IssueMigrationService
{
  Integer getVersion();

  Trac2GitLabIssueMigrationResult migrate(String privateToken, GitLabProject gitLabProject,
      Map<String, String> labelMap, Map<String, TracMilestone> milestoneToTracMilestoneMap, List<TracIssue> tracIssues);

  File saveMigrationResult(File path, Trac2GitLabIssueMigrationResult trac2GitLabIssueMigrationResult)
      throws RuntimeIoException;
}
