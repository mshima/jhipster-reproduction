# JHipster Reproduction Repository

This repository hosts minimal, self-contained reproductions for [JHipster](https://www.jhipster.tech/) issues.

Every reproduction lives on its own branch. This branch carries only the shared scaffolding (`.gitignore`, this README); it is intentionally empty of application code so that each reproduction starts from a clean slate.

## Branch naming

Branches follow the format:

```
<repository>_<number>
```

- `<repository>` — the GitHub repository the issue or pull request was reported against.
- `<number>` — the issue or pull request number in that repository.

### Examples

| Branch                          | Refers to                                            |
| ------------------------------- | ---------------------------------------------------- |
| `generator-jhipster_28500`      | `jhipster/generator-jhipster` issue/PR **28500**      |
| `generator-jhipster-native_512` | `jhipster/generator-jhipster-native` issue/PR **512** |
| `jhipster-online_1024`          | `jhipster/jhipster-online` issue/PR **1024**          |

Keeping the repository name in the branch means reproductions for different JHipster projects can coexist here without ambiguity, and the branch name alone is enough to find the discussion it belongs to.

## Browsing reproductions

```bash
git clone https://github.com/mshima/jhipster-reproductions.git
cd jhipster-reproductions

# List every available reproduction
git branch -r

# Check one out
git switch generator-jhipster_28500
```

Each reproduction branch is a standalone generated application. Read its own notes — usually in the branch's README or in the commit messages — for the exact steps to trigger the reported behaviour.

## Adding a new reproduction

1. Branch off this branch so you start from the shared scaffolding:

   ```bash
   git switch reproduction
   git switch -c generator-jhipster_28500
   ```

2. Generate the application (or apply the minimal set of files) that reproduces the problem. Keep it as small as the problem allows — the value of a reproduction is in what it leaves out.

3. Commit the generated state, then commit the change that triggers the failure separately, so the diff between the two commits is the reproduction itself.

4. Push the branch and link it from the issue or pull request:

   ```bash
   git push -u origin generator-jhipster_28500
   ```

## Notes

- Branches here are never rebased onto one another; each one is independent and is kept at whatever JHipster, JDK, and build-tool versions were needed to show the problem.
- Once an issue is closed, its branch is kept as-is for reference rather than updated.
