---
name: create-reproduction
description: Create a JHipster reproduction from a sample using base and fix branches
agent: agent
---

Create a minimal JHipster reproduction in the current repository for the requested issue or pull request.

## Required inputs

Before running commands, collect any missing values:

- `issue_number`: the issue or pull request number.
- `sample`: the sample name passed to `generate-sample`. Ask for it if it was not supplied.
- `repository_path`: the local path to the repository. Ask for it if it was not supplied. If `~/git/generator-jhipster` exists, suggest it as the default.
- `fix_branch`: the branch containing the fix. If it was not supplied and `issue_number` is a pull
  request, resolve it from that pull request as described in "Resolving the fix branch". Ask for it
  only when that lookup does not apply or fails.

Do not guess a sample, issue number, or fix branch. Reading the head branch of the pull request
identified by `issue_number` is a lookup, not a guess; inferring a branch from branch names, git
history, or the current working tree is. Expand `~` and verify that the repository path exists
before proceeding.

## Resolving the fix branch

When `fix_branch` was not supplied, `issue_number` is often a pull request that carries the fix.
Ask the pull request for its head branch instead of asking the user:

```bash
gh pr view <issue_number> --repo jhipster/generator-jhipster \
  --json isCrossRepository,headRefName,headRepositoryOwner,state,title
```

- If the command fails because the number is an issue and not a pull request, there is nothing to
  resolve — ask the user for the fix branch.
- If `isCrossRepository` is `false`, the head branch lives in the generator-jhipster repository
  itself. Fetch upstream and use `<headRefName>`, falling back to `upstream/<headRefName>` when no
  local branch of that name exists.
- If `isCrossRepository` is `true`, the head branch lives in a fork
  (`<headRepositoryOwner>:<headRefName>`) and is not reachable by name. Fetch the pull request head
  into a local branch in the generator-jhipster repository and use that branch:

  ```bash
  cd <repository_path>
  git fetch upstream pull/<issue_number>/head:pr-<issue_number>
  ```

  `fix_branch` is then `pr-<issue_number>`.

Tell the user which pull request the fix branch came from and which ref you resolved it to before
generating anything, so a wrong `issue_number` is caught before the base branch is built. The
resolved value goes through the rest of the workflow unchanged, including the `git rev-parse
--verify` check in step 3 and the report in step 12.

## Resolving the sample name

`generate-sample` looks the sample up by the `name` field of the workflow samples in
`<repository_path>/.blueprint/generate-sample/templates/test-integration/workflow-samples/`
(`angular.json`, `react.json`, `vue.json`, `daily-ms-oauth2.json`, `daily-neo4j.json`). Only those
`name` values are valid arguments. Two nearby identifiers are **not** accepted:

- `job-name` — the CI job label, often the `name` plus a suffix such as `-webpack`.
- the directory names under `test-integration/samples/` — those are `app-sample` values that a
  workflow sample points at, not sample names.

Before running anything, confirm the supplied sample is a `name` in one of those files:

```bash
cd <repository_path>
grep -rn '"name": "<sample>"' .blueprint/generate-sample/templates/test-integration/workflow-samples/
```

If it does not match, search the same folder for the value as a `job-name` or `app-sample` and tell
the user which `name` it maps to, then use that `name`. Never pass an unresolved value through: a
missing sample only prints `Sample <name> was not found` and then fails later while copying a
`.yo-rc.json` that does not exist, which is much harder to read than an up-front check.

A `.jdl` file name is also a valid `sample` and bypasses this lookup.

## Branch names

The two branches are named after the issue and the sample, so several samples can reproduce the same
issue side by side in this repository:

```
generator-jhipster_<issue_number>_<sample>_base
generator-jhipster_<issue_number>_<sample>_2
```

`<sample>` is the resolved sample name — the `name` value from the workflow samples, the one actually
passed to `generate-sample`, never the `job-name` or `app-sample` the user may have supplied. For a
`.jdl` sample, drop the `.jdl` extension. Use these names verbatim everywhere below: the branch
creation, the pushes, and the pull request.

## Workflow

1. Treat the current repository as the reproduction repository. Inspect the repository at `repository_path` and confirm that it is a `generator-jhipster` checkout. It must contain `bin/jhipster.cjs` and its Git remotes must identify the generator-jhipster repository.
2. Identify the upstream default branch. Prefer the remote named `upstream`; otherwise ask which remote should be treated as upstream. Read it with `git remote show upstream` (the `HEAD branch` line) and fetch the upstream refs before using them.
3. Check both working trees before touching any branch:
   - In the current reproduction repository, ensure the working tree is clean. Do not discard local changes; ask the user how to proceed if it is dirty.
   - In the `generator-jhipster` repository, ensure the working tree is clean as well, since the workflow switches its branches twice. Ask how to proceed if it is dirty.
   - Verify that `fix_branch` resolves in the `generator-jhipster` repository (`git rev-parse --verify <fix_branch>`) so the run does not fail after the base has already been generated.
   - Verify that neither `generator-jhipster_<issue_number>_<sample>_base` nor `generator-jhipster_<issue_number>_<sample>_2` exists yet. Only those two exact names collide; other branches sharing the `generator-jhipster_<issue_number>` prefix are unrelated and may stay, including reproductions of the same issue built from a different sample.
4. In the current reproduction repository, create the base branch from the upstream default commit. The generator repository must be in detached HEAD at the upstream ref while generating the base:

   ```bash
   cd <repository_path>
   git switch --detach upstream/<default-branch>
   cd <current-repository-path>
   git switch main
   git switch -c generator-jhipster_<issue_number>_<sample>_base
   ```

5. From the `generator-jhipster` repository, execute exactly, with `<current-repository-path>` set to the current reproduction repository:

   ```bash
   cd <repository_path>
   bin/jhipster.cjs generate-sample <sample> --force --skip-jhipster-dependencies --project-folder <current-repository-path>
   ```

   This creates the reproduction from the upstream base.
6. Make sure the base branch is committed. Because the reproduction repository has no `.yo-rc.json`
   on the new branch, JHipster usually treats this as a fresh application and commits it itself as
   `Initial version of <app> generated by generator-jhipster@<version>`. Check whether it did, and
   if it did not, commit the generated project yourself with an equivalent message:

   ```bash
   cd <current-repository-path>
   git status --short
   git add -A && git commit -m "Initial version of <app> generated by generator-jhipster@<version>"
   ```

   The base branch must end up clean, with the generated project in a commit.
7. Still on the base branch, generate the CI/CD pipeline so that the reproduction's pull request is
   actually built. Run it from the reproduction repository, since `ci-cd` has no `--project-folder`
   and works on the current directory:

   ```bash
   cd <current-repository-path>
   <repository_path>/bin/jhipster.cjs ci-cd github --defaults --force
   git add -A && git commit -m "ci: add github workflow"
   ```

   `--defaults` answers the "What tasks/integrations do you want to include ?" checkbox, which has
   no command line option of its own; without it the run blocks on a prompt and dies with
   `User force closed the prompt`. Older generator refs ignore `--defaults` for that prompt. If the
   detached upstream ref is one of them, run this single command with `fix_branch` checked out in
   the `generator-jhipster` repository instead: the workflow is test harness rather than
   reproduction payload, so it may come from either ref, but it must be committed on the base
   branch so that the second branch inherits it and the payload diff stays clean.

   The run may also normalise files it did not write, such as `.jhipster/*.json` entity
   definitions. Commit whatever it touches, so that both branches start from the same state. The
   selected pipeline is not recorded in `.yo-rc.json`, so the second generation in step 9 does not
   regenerate `.github/workflows/`.

8. In the current reproduction repository, create the second branch from `generator-jhipster_<issue_number>_<sample>_base`:

   ```bash
   cd <current-repository-path>
   git switch generator-jhipster_<issue_number>_<sample>_base
   git switch -c generator-jhipster_<issue_number>_<sample>_2
   ```

9. In the `generator-jhipster` repository, check out the branch containing the fix. Then execute the same command again, targeting the current reproduction repository:

   ```bash
   cd <repository_path>
   git switch <fix_branch>
   bin/jhipster.cjs generate-sample <sample> --force --skip-jhipster-dependencies --project-folder <current-repository-path>
   ```

   This run finds the `.yo-rc.json` committed on the base branch, reports `So we assume this is
   application regeneration`, and does **not** commit.
10. Commit the second branch yourself, since step 9 leaves the difference introduced by `fix_branch`
   uncommitted. That difference is the reproduction's payload, so it must become a commit of its own
   on top of the base commit:

   ```bash
   cd <current-repository-path>
   git add -A
   git commit -m "<short summary of what <fix_branch> changes>"
   ```

   Summarise the actual change in the message (for example `feat: migrate to Spring Boot 4.1.0`)
   rather than naming the branch mechanically. Never stash, reset, or clean this difference away.
11. Push both branches to `origin` and open a pull request from the second branch into the base
    branch, so the fix's effect on the sample is reviewable as a diff:

    ```bash
    cd <current-repository-path>
    git push -u origin generator-jhipster_<issue_number>_<sample>_base
    git push -u origin generator-jhipster_<issue_number>_<sample>_2
    gh pr create --draft \
      --base generator-jhipster_<issue_number>_<sample>_base \
      --head generator-jhipster_<issue_number>_<sample>_2 \
      --title "<title>" --body "<body>"
    ```

    Follow the repository's convention of draft pull requests. The body should link the issue or
    pull request being reproduced (`jhipster/generator-jhipster#<issue_number>`) and record the
    sample used, the upstream ref and commit the base was generated from, and the fix branch and
    commit. Report the pull request URL.
12. Report the repository path, the two created branches with their commits, the upstream ref used for the base (branch name and commit), the fix branch used (branch name and commit), the sample name actually passed, the command results, and the pull request URL. Summarise the reproduction's payload with `git diff --stat generator-jhipster_<issue_number>_<sample>_base..generator-jhipster_<issue_number>_<sample>_2`. State which branch the `generator-jhipster` repository was left on, since step 9 leaves it on `fix_branch` rather than the detached upstream ref it started from.

If `repository_path` is not a `generator-jhipster` repository, stop after reporting the reason and ask for the correct path. If a branch already exists, do not overwrite it; ask whether to use another issue number or clean up the existing branch.
