#!/usr/bin/env node

/**
 * Downloads CDM JSON schemas from Maven Central to static/schemas/{version}/
 * Usage: node scripts/download-schemas.js
 */

const fs = require('fs');
const path = require('path');
const { execSync } = require('child_process');

// CDM versions: { version: 'maven-version', urlPath: 'url-path' }
const SCHEMA_VERSIONS = [
  { version: '6.0.0', urlPath: '6.0' },
  { version: '5.20.0', urlPath: '5.20' },
  { version: '5.13.0', urlPath: '5.13' },
];

const MAVEN_URL = 'https://repo1.maven.org/maven2/org/finos/cdm/cdm-json-schema';
const SCHEMAS_DIR = path.join(__dirname, '..', 'static', 'schemas');
const TEMP_DIR = path.join(__dirname, '..', '.schema-temp');

function downloadVersion({ version, urlPath }) {
  console.log(`\nDownloading CDM ${version} → /schemas/${urlPath}/`);
  
  const url = `${MAVEN_URL}/${version}/cdm-json-schema-${version}.zip`;
  const archive = path.join(TEMP_DIR, `${version}.tar.gz`);
  const extractDir = path.join(TEMP_DIR, version);
  const destDir = path.join(SCHEMAS_DIR, urlPath);
  
  // Download (the .zip is actually a gzipped tar)
  execSync(`curl -fsSL "${url}" -o "${archive}"`, { stdio: 'pipe' });
  
  // Extract
  fs.mkdirSync(extractDir, { recursive: true });
  execSync(`tar -xzf "${archive}" -C "${extractDir}"`, { stdio: 'pipe' });
  
  // Find and copy schema files
  const sourceDir = path.join(extractDir, 'jsonschema');
  const files = fs.readdirSync(sourceDir).filter(f => f.endsWith('.json'));
  
  if (fs.existsSync(destDir)) fs.rmSync(destDir, { recursive: true });
  fs.mkdirSync(destDir, { recursive: true });
  
  for (const file of files) {
    fs.copyFileSync(path.join(sourceDir, file), path.join(destDir, file));
  }
  
  console.log(`  ✓ Installed ${files.length} schemas`);
}

// Main
console.log('CDM Schema Downloader');
fs.mkdirSync(TEMP_DIR, { recursive: true });
fs.mkdirSync(SCHEMAS_DIR, { recursive: true });

let errors = 0;
for (const config of SCHEMA_VERSIONS) {
  try {
    downloadVersion(config);
  } catch (e) {
    console.error(`  ✗ Failed: ${e.message}`);
    errors++;
  }
}

fs.rmSync(TEMP_DIR, { recursive: true, force: true });

if (errors) {
  console.log(`\n⚠ Completed with ${errors} error(s)`);
  process.exit(1);
}
console.log('\n✓ Done! Schemas at https://cdm.finos.org/schemas/{version}/');

