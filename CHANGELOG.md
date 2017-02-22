# Jenkins Automate Website Plugin Change Log
All notable changes to this project will be documented in this file.
This project adheres to [Semantic Versioning](http://semver.org/).

## [Unreleased]

## [1.1.0]

### Changed
- updated to manager-api-client 0.8.0 (introduced resolution, timeout and box-id config params)

## [1.0.0]
### Fixed
- fixed serialization/deserialization of the descriptor and related models by changing aliases

### Added
- added alias for the builder serialization/deserialization

## [0.7.0]
### Changed
- bumped the manager-client-api version

## [0.6.0]
### Changed
- retrieve only executable scenarios (non fragments)

## [0.5.0]
### Added
- introduced plugin configuration parameters

## [0.4.0]
### Changed
- upgraded to manager-api-client 0.3.1
- added environment variables execution context delegation

## [0.3.0]
### Changed
- migrated to oss.sonatype.org distribution management
- migrated to travis-ci

## [0.2.0]
### Changed
- changed artifact and group name
- changed package names
- hard wired utilized plugin versions

## [0.1.0]
### Changed
- switched from internal manager api implementation to usage of [manager-api-client]
- improved (de)serialization by utilization of aliases

## [0.0.1]
### Added
- created initial implementation of the jenkins plugin

[Unreleased]: https://github.com/automate-website/jenkins-plugin/compare/1.1.0...HEAD
[1.1.0]: https://github.com/automate-website/jenkins-plugin/compare/1.0.0...1.1.0
[1.0.0]: https://github.com/automate-website/jenkins-plugin/compare/0.7.0...1.0.0
[0.7.0]: https://github.com/automate-website/jenkins-plugin/compare/0.6.0...0.7.0
[0.6.0]: https://github.com/automate-website/jenkins-plugin/compare/0.5.0...0.6.0
[0.5.0]: https://github.com/automate-website/jenkins-plugin/compare/0.4.0...0.5.0
[0.4.0]: https://github.com/automate-website/jenkins-plugin/compare/0.3.0...0.4.0
[0.3.0]: https://github.com/automate-website/jenkins-plugin/compare/0.2.0...0.3.0
[0.2.0]: https://github.com/automate-website/jenkins-plugin/compare/0.1.0...0.2.0
[0.1.0]: https://github.com/automate-website/jenkins-plugin/compare/0.0.1...0.1.0
[0.0.1]: https://github.com/automate-website/jenkins-plugin/compare/0.0.0...0.0.1
[manager-api-client]: https://github.com/automate-website/manager-api-client