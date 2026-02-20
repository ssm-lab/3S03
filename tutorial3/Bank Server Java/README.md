# Tutorial 3 – Automated Testing & CI/CD Tutorial

![CI Status](https://github.com/ssm-lab/3S03/actions/workflows/java-ci.yml/badge.svg)


## CI/CD Workflow Overview
This project uses GitHub Actions to automatically verify correctness on every push and pull request.

The CI pipeline performs the following steps:
1. Builds the project using Maven
3. Compiles all JUnit tests
4. Executes the full test suite
5. Fails the build if any test fails

If the badge above is green, all tests passed.
If it is red, at least one test failed.


## When the CI Build Fails
If the CI build fails:
- Click the badge or go to the Actions tab
- Open the most recent workflow run
- Inspect the failing step

