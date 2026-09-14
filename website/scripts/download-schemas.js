#!/usr/bin/env node

/**
 * Downloads CDM JSON schemas from Maven Central to static/schemas/{version}/
 * Usage: node scripts/download-schemas.js
 */

const fs = require('fs');
const path = require('path');
const { execSync } = require('child_process');
const { versions: SCHEMA_VERSIONS, mavenUrl: MAVEN_URL } = require('./schema-versions');
const SCHEMAS_DIR = path.join(__dirname, '..', 'static', 'schemas');
const TEMP_DIR = path.join(__dirname, '..', '.schema-temp');

function downloadVersion({ version, urlPath }) {
  console.log(`\nDownloading CDM ${version} → /schemas/${urlPath}/`);
  
  const url = `${MAVEN_URL}/${version}/cdm-json-schema-${version}.zip`;
  const archive = path.join(TEMP_DIR, `${version}.archive`);
  const extractDir = path.join(TEMP_DIR, version);
  const destDir = path.join(SCHEMAS_DIR, urlPath);

  // Download. Despite the .zip name, the artifact's real format varies by
  // version: <= 6.0.0 ships a gzipped tar, >= 7.0.0 ships a genuine ZIP.
  execSync(`curl -fsSL "${url}" -o "${archive}"`, { stdio: 'pipe' });

  // Extract based on the actual format (detected via magic bytes). macOS bsdtar
  // reads both, but Linux GNU tar cannot read a real ZIP, so we must dispatch to
  // the right extractor for the build to work on Netlify.
  fs.mkdirSync(extractDir, { recursive: true });
  const magic = Buffer.alloc(2);
  const fd = fs.openSync(archive, 'r');
  fs.readSync(fd, magic, 0, 2, 0);
  fs.closeSync(fd);
  if (magic[0] === 0x50 && magic[1] === 0x4b) {
    // "PK" → genuine ZIP archive
    execSync(`unzip -q -o "${archive}" -d "${extractDir}"`, { stdio: 'pipe' });
  } else if (magic[0] === 0x1f && magic[1] === 0x8b) {
    // gzip → gzipped tar
    execSync(`tar -xzf "${archive}" -C "${extractDir}"`, { stdio: 'pipe' });
  } else {
    throw new Error(`Unrecognised archive format (magic bytes: ${magic[0].toString(16)} ${magic[1].toString(16)})`);
  }
  
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

