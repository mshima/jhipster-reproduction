---
description: Create a JHipster reproduction from a sample using base and fix branches
argument-hint: [issue_number] [sample] [fix_branch] [repository_path]
allowed-tools: Read, Bash(git:*), Bash(gh:*), Bash(cd:*), Bash(ls:*), Bash(grep:*), Bash(bin/jhipster.cjs:*), AskUserQuestion
---

Read `.github/prompts/create-reproduction.prompt.md` in this repository and follow it exactly.
That file is the source of truth for the workflow; do not improvise steps, reorder them, or
substitute your own commands for the ones it specifies.

## Arguments

Arguments supplied with this command map to the prompt's required inputs, in order:

- `$1` — `issue_number`
- `$2` — `sample`
- `$3` — `fix_branch` (resolved from the pull request when omitted and `$1` is a pull request)
- `$4` — `repository_path` (defaults to `~/git/generator-jhipster` when that path exists)

Raw arguments: $ARGUMENTS

Any argument that is missing is an input you must collect before running commands. The prompt
forbids guessing a sample, issue number, or fix branch, so ask for each missing value rather
than inferring one from branch names, git history, or the current working tree. The one exception
is `fix_branch`: when it is missing and `$1` is a pull request number, resolve it from that pull
request's head branch as the prompt's "Resolving the fix branch" section describes, and ask only if
that lookup does not apply or fails.

## Reminders

- The current repository is the reproduction repository; `repository_path` is the separate
  `generator-jhipster` checkout. Verify it contains `bin/jhipster.cjs` and has a remote pointing
  at the generator-jhipster repository before doing anything else.
- Resolve the sample name first, as the prompt's "Resolving the sample name" section describes.
  Valid values are the `name` fields in
  `.blueprint/generate-sample/templates/test-integration/workflow-samples/*.json` — not the
  `job-name` values and not the folder names under `test-integration/samples/`. A wrong value
  fails late and obscurely, so check it up front and tell the user what you mapped it to.
- A missing `fix_branch` comes from the pull request numbered `$1`, via
  `gh pr view <issue_number> --repo jhipster/generator-jhipster --json isCrossRepository,headRefName,headRepositoryOwner,state,title`.
  A same-repository head is used by name; a fork head is fetched into the generator-jhipster
  checkout as `pr-<issue_number>` with `git fetch upstream pull/<issue_number>/head:pr-<issue_number>`
  and that local branch becomes `fix_branch`. Say which pull request and ref you resolved before
  generating anything.
- Branch names carry the sample: `generator-jhipster_<issue_number>_<sample>_base` and
  `generator-jhipster_<issue_number>_<sample>_2`, where `<sample>` is the resolved sample name you
  passed to `generate-sample` (extension dropped for a `.jdl` sample). See the prompt's "Branch
  names" section.
- Leave both working trees alone if they are dirty — ask how to proceed instead of stashing,
  resetting, or discarding changes. The `generator-jhipster` tree matters too, because the
  workflow switches its branches.
- If `generator-jhipster_<issue_number>_<sample>_base` or `generator-jhipster_<issue_number>_<sample>_2` already
  exists, stop and ask; never overwrite an existing branch.
- Both branches must end up committed. The base run normally commits itself because the branch has
  no `.yo-rc.json`; if it did not, commit the generated project. The second run detects the
  existing `.yo-rc.json` and never commits, so commit that difference yourself — it is the
  reproduction's payload, and it must never be stashed, reset, or cleaned away.
- Pushing both branches and opening the draft pull request from
  `generator-jhipster_<issue_number>_<sample>_2` into `generator-jhipster_<issue_number>_<sample>_base` are part of
  the workflow, so they need no extra confirmation. Anything beyond that — force-pushing, merging,
  marking the pull request ready, or touching other branches — still does.
- Finish with the report described in the prompt's last step: repository path, both created
  branches and their commits, the upstream ref used for the base, the fix branch, the sample
  actually passed, a `git diff --stat` between the two branches, the pull request URL, and which
  branch the generator repo was left on.
