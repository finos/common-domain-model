/**
 * Centralized CDM schema version configuration
 * Update this file when new CDM versions are released
 */

module.exports = {
  // Versions to download and make available
  versions: [
    { version: '6.0.0', urlPath: '6.0', status: 'Latest' },
    { version: '5.20.0', urlPath: '5.20', status: 'Previous' },
    { version: '5.13.0', urlPath: '5.13', status: 'Previous' },
  ],
  
  // Maven repository details
  mavenUrl: 'https://repo1.maven.org/maven2/org/finos/cdm/cdm-json-schema',
};
